#!/usr/bin/env python
import rospy
import motionplanner
import pathplanner
import location
import pose
import mapParser
from gc_msgs.msg import MotionMsg  # for sending commands to motors
from gc_msgs.msg import BumpMsg    # for listening to bump sensors
from gc_msgs.msg import PoseMsg    # for listening to when the kinect sees a block
from gc_msgs.msg import ObstacleAheadMsg    # for listening to when the kinect sees a wall
from gc_msgs.msg import ObstacleMsg
import time
import random

#
# This node is the state machine that controls robot behavior
#
# TODO: call mapUpdater.make obstacle when 


class RobotBrain(obstacle):

#####################
## state methods ####
#####################

    # params: none
    # returns: void
    # starts all publishers, subscribers; loads map and fills associated variables 
    def __init__():
        
        # defining variables related to robot's state
        self.robotState = 'wandering'               # the state of the robot; can be wandering, traveling, searching, consuming
                                               #    digesting, dispensing 
        self.searchCount = 0                        # counter used to ensure that don't spend too long searching
        self.numBlocksCollected = 0                 # number of blocks that the robot has collected so far
        self.mapList = []                           # list of obstacles
        self.blockLocations = []                    # list of locations of blocks
        self.waypointsList = []                      # list of waypoints to current destination
        self.inHamperFlag = 0                       # flag used to indicate whether a block on the conveyor belt
                                               #  has made it to the hamper
        self.wanderCount = 0                        # while wandering, count up and turn
        self.currentPose = pose.Pose(0.0,0.0,0.0);
        self.previousWaypointPose = pose.Pose(0.0, 0.0, 0.0); #used for travel
   

        # constants
        # TODO: pick values well
        self.NUM_BLOCKS_NEEDED = 9                  # number of blocks needed to complete wall
        self.MAX_SEARCH_COUNT = 500                 # arbitrary value; should be tested and set
        self.POSITION_THRESHOLD = .005              # acceptable error to reaching a position. In meters.    
        self.END_LOCATION = location.Location(0.0, 0.0);
        self.MAX_WANDER_COUNT = 30;
        self.KINECT_ERROR = .01;                    # uncertainty of the kinect in m
        self.ROBOT_RADIUS = .6                      # radius of robot in m


        # making publishers/subscribers
        self.motionPub = rospy.Publisher("command/Motors", MotionMsg);
        self.bumpSub = rospy.Subscriber('bumpData', BumpMsg, handleBumpMsg);
        self.blockSeenSub = rospy.Subscriber('blockSeen', PoseMsg, handleBlockSeenMsg);
        self.obstacleAheadSub = rospy.Subscriber('obstacleAhead', ObstacleAheadMsg, handleObstacleAheadMsg);
        

        # loading and processing map
        [self.blockLocations, self.mapList] = mapParser.parseMap('map.txt', currentPose);
        
        # TODO for debugging in wander
        self.blockLocations = []

        # objects
        self.motionPlanner =motionplanner.MotionPlanner();
        self.pathPlanner = pathplanner.PathPlanner(mapList, ROBOT_RADIUS);   
        #TODO: mapUpdater

        # ROSPY methods
        rospy.init_node('robotbrain')
        rospy.on_shutdown(onShutdown)

        return

    # params: none
    # returns: none
    # Robot needs more blocks, but does not know where to find them.
    # perform ant-like motion; move forward. If hit an obstacle, turn right or left randomly
    # increment wanderCount; when it maxes out, spin around
    def wander(self):
        
        # if wanderCount has counted up enough, spin 360 degrees
        if (self.wanderCount >= self.MAX_WANDER_COUNT):
            
            # stop forward motion
            self.motionPlanner.stopWheels() 
            
            # wanderCount holds at the max value while turning
            # TODO: that method no longer exists?
            doneTurning = self.motionPlanner.rotate360()
            if(doneTurning):
                self.wanderCount = 0
                
        # travel forward, incrementing wanderCount
        # when an obstacle is seen, handleObstacleAhead will be called to cause turning
        else:

            self.motionPlanner.translate(.01)
            time.sleep(.5)
            self.wanderCount +=1
        return 

    # params: none
    # returns: none
    # Robot needs to move from it's current location to a destination
    # global waypoints contains a list of points the robot can travel to in straight-line paths
    def travel(self):

        # if have reached the current waypoint, stop and move to the next waypoint. If at end of wayponts, enter seatch
        if (self.getDistance(currentPose, waypointsList[0]) < self.POSITION_THRESHOLD):
            self.motionPlanner.stopWheels();
            self.previousWaypointPose = self.currentPose;

            # if there are more waypoints, travel to next one
            if len(self.waypointsList > 1):
                self.waypointsList = self.waypointsList[1:];

            # if there are no more waypoints, are at expected block location. start searching.     
            else:
                self.robotState = 'searching';
                self.searchCount = 0;

        # if haven't reached the current waypoint, travel towards it
        else:
            # takes currentPose, destinationLoc, angVel, vel, startPose
            self.motionPlanner.travelTowards(currentPose, waypointsList[0], .01, .1, previousWaypointPose); 
        return

    # params: none
    # returns: none
    # Robot has reached a place where it believes a block is
    # turn in slow circle looking for block. If time-out, reject block location and set course for a new one
    # search gets called repeatedly, so can switch states in the middle
    def search(self):

        self.motionPlanner.rotate(.1);  #TODO: check that this is a good speed
        
        # if haven't been searching too long, keep searching
        if(self.searchCount < self.MAX_SEARCH_COUNT):
            time.sleep(.5);            # should be moving slow enough that nothing passes in and out of view in this time
                                       # sleeping keeps count from getting crazy-high
            self.searchCount += 1;

        # if have been searching too long, give up on this block. Might not actually exist
        else: 

            # if know the locations of more blocks, set path to them and go get them                                      
            if (len(self.blockLocations) > 1):
                self.blockLocations = blockLocations[1:];
                self.waypointsList = self.pathPlanner.plotPath(self.currentPose, self.blockLocations[0])
                self.robotState = 'travelling'

            # if don't know the location of more blocks, wander                                                           
            else:
                self.robotState = 'wandering'
                self.wanderCount = 0
        return

    # params: none
    # returns: none
    # Robot has a block currently in its vision. 
    # Move until block hits the bump sensors at the mouth of the conveyor belt
    def consume(self):

        # blockLocation is updatd by kinect to be accurate
        self.motionPlanner.travelTowards(self.currentPose, self.blockLocations[0], .01, .01, self.previousWaypointPose);
        return

    # params: none
    # returns: none
    # Robot has a block on the conveyor belt
    # Move conveyor belts so block is added to wall
    # this is an atomic action
    def digest(self):
        # TODO: this is a simple implementation that doesn't reverse belts if something gets stuck
        
        # stop wheel motors and start ramp motors to move block up ramp
        self.motionPlanner.stopWheels();
        self.motionPlanner.startEatingBelts();
        self.motionPlanner.setHamperAngle(10); # so block can fall down sucessfully
                                      # TODO: get a real value for this

        # wait for block to go up the ramp
        while(self.inHamperFlag < 1): 
            time.sleep(1);   # wait for one second
        
        time.sleep(1);       # in case block needs to finish falling
        self.inHamperFlag = 0     # resetting flag

        # once block is in hamper, use hamper belts to push block into forming wall
        self.motionPlanner.setHamperAngle(0); # fully close hamper so conveyor belts can work
        self.motionPlanner.startHamperBelt();
        time.sleep(5)                    # wait for blocks to be pushed into right shape

    
        # turn off motors 
        self.motionPlanner.stopConveyorBelt();

        # if know the locations of more blocks, set path to them and go get them
        if (len(self.blockLocations) > 1):
            self.blockLocations = self.blockLocations[1:];
            self.waypointsList = self.pathPlanner.plotPath(self.currentPose, self.blockLocations[0])
            self.robotState = 'travelling'
        # if don't know the location of more blocks, wander
        else:
            self.robotState = 'wandering'
            self.wanderCount = 0
        return

    # params: none
    # returns: void
    # Robot has finished gathering blocks; commands robot to travel to endLocation and open hatch
    def dispense(self):

        self.waypointsList = self.pathPlanner.plotPath(self.currentPose, self.END_LOCATION);

        while len(self.waypointsList) > 0: 
            # if have reached the current waypoint, stop and move to the next waypoint. 
            if (self.getDistance(self.currentPose, self.waypointsList[0]) < self.POSITION_THRESHOLD):
                self.motionPlanner.stopWheels();
                self.previousWaypointPose = self.currentPose;
                
                # if there are more waypoints, travel to next one
                if len(self.waypointsList > 1):
                    self.waypointsList = self.waypointsList[1:];
                    
                # otherwise, at end location
                else: 
                    break

            # if haven't reached waypoint, keep travelling towards it
            else: 
                self.motionPlanner.travelTowards(self.currentPose, self.waypointsList[0], .01, .1, self.previousWaypointPose); 

            # have reached end location. Open hamper and drive away
            self.motionPlanner.setHamperAngle(math.pi/2.0); 
            self.motionPlanner.translate(.01)
            time.sleep(.5)
            self.motionPlanner.stopWheels()
            return


##################
# Helper methods #
##################

        # params: pose: a Pose
        #         loc: a Location 
        # returns: distance between them (absolute value)
        def getDistance(self, pose, loc):
            return math.sqrt((loc1.getX()-loc2.getX())**2 + (loc1.getY()-loc2.getY())**2) 

        # params: loc1: a Location
        #         loc2: a Location 
        # returns: distance between them (absolute value)
        def getDistance(self, loc1, loc2):
            return math.sqrt((pose.getX()-loc.getX())**2 + (pose.getY()-loc.getY())**2) 

###############################
# Interrupt-handling methods ##
###############################

        # params: none
        # returns: none
        # based on which bump sensor went off, call appropriate respose
        def handleBumpMsg(self, msg):
            # declaring global variables

            # bump sensors 0 and 1 are at the mouth
            if (msg.bumpNumber <= 1):

                #if are currently searching and something hits these bump sensors, are eating the block
                if self.robotState == 'consuming': 
                    self.searchCount = 0;
                    self.robotState = 'digesting';

                # bump sensor 2 is at the end of the conveyor belt. Indicates that partially done digesting
            elif (msg.bumpNumber == 2):
                if (self.robotState == 'digesting'):
                    self.inHamperFlag = 1; #used by digesting method

            else:
                #msg = MotionMsg(); # defaults to 0
                self.motionPlanner.stopWheels();
                # not sure where other bump sensors on chassis will be; 
                # should stop and back away if they are hit
                # TODO let bump sensors handle turning in wander state when see obstacle   
            return

        # param: msg PoseMsg
        # return: none
        # takes in message from kinect indicating that a block has been seen
        def handleBlockSeenMsg(self, msg):
            
            newBlockLocation = location.Location(msg.xLoc, msg.yLoc)
            
            if self.robotState == 'wandering': 
                self.motionPlanner.stopWheels();
                self.blockLocations.append(newBlockLocation)
                self.robotState = 'searching'
                self.searchCount = 0
        
            elif robotState == 'travelling':
        
                self.motionPlanner.stopWheels();
        
                # if have reache the block travelling towards, just enter search
                if (self.getDistance(newBlockLocation, self.blockLocations[0]) < self.KINECT_ERROR):
                    self.robotState = 'searching'
                    self.searchCount = 0

                # if found a new block while travelling, add it to blockLocations
                else:
                    newblockLocations = [newBlockLocation]
                    for i in range(len(self.blockLocations))): newblockLocations.append(self.blockLocations[i])
                    self.blockLocations = newblockLocations
                    self.robotState = 'searching'
                    
            # if consuming and see a new block
            elif (self.robotState == 'consuming') and (self.getDistance(newBlockLocation, self.blockLocations[0]) < self.KINECT_ERROR):
                # will continue consuming, but make this block the next block to get

                newBlockLocations = [self.blockLocations[0], newBlockLocation]
                for i in range(len(blockLocations)-1):
                    newBlockLocations.append(self.blockLocations[i+1])
                self.blockLocations = newBlockLocations

            elif robotState == 'digesting':
                # continue digesting, bt make this next block to get

                newBlockLocations = [self.blockLocations[0], newBlockLocation]
                for i in range(len(blockLocations)-1):
                    newBlockLocations.append(self.blockLocations[i+1])
                self.blockLocations = newBlockLocations

            return

        # param: msg
        # return: none
        # when an obstacle is directly ahead: stop. if travelling or depositing, wait and then remake path. 
        # If wandering, turn randomly 90 deg right or left
        def handleObstacleAheadMsg(self, msg):
            
            # if robot is wandering, turn 90 deg left or right
            if self.robotState == 'wandering':
                self.motionPlanner.stopWheels()
                startAngle = self.currentPose.getAngle()
       
                # decide whether to turn right or left
                rand = random.random()
                if rand < 0.5: 
                    goalAngle = startAngle + math.pi/2.0
                else:
                    goalAngle = startAngle - math.pi/2.0
        
                    # turn until turn is completed
                    while (math.abs(self.currentPose.getAngle() - goalAngle) > self.motionPlanner.ANG_ERR):
                        self.motionPlanner.rotateTowards(self.currentPose.getAngle(), goalAngle, .01, startAngle)

                    # if not wandering, wait for mapUpdater to update with obstacle ahead and replan path
                    else:
                        time.sleep(2)
                        self.waypointsList = self.pathPlanner.plotPath(self.currentPose, self.blockLocations[0])
        
            return


        # param: none
        # return: none
        # shutdown behavior for rospy
        def onShutdown(self):
            return

        # param: none
        # return: none
        # carried the main state machine
        def main(self):
                    while(self.numBlocksCollected < self.NUM_BLOCKS_NEEDED):
                        if (self.robotState == 'wandering'):
                            self.wander()
                            
                        elif (self.robotState == 'traveling'):
                            self.travel()

                        elif (self.robotState == 'searching'):
                            self.search()

                        elif (self.robotState == 'consuming'):
                            self.consume()

                        elif (self.robotState == 'digesting'):
                            self.digest()
                    
                    self.dispense()
                    return

############
## Main ####
############


if __name__ == '__main__':

    
    try:
        
        robotbrain = RobotBrain()
        robotbrain.main()
        rospy.spin()          # keeps python from exiting until node is stopped
    except rospy.ROSInterruptException: pass
