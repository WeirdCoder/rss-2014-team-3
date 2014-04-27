#!/usr/bin/env python
import rospy
import motionplanner
import pathplanner
import location
import pose
import mapParser
from gc_msgs import MotionMsg  # for sending commands to motors
from gc_msgs import BumpMsg    # for listening to bump sensors
from gc_msgs import PoseMsg    # for listening to when the kinect sees a block
import time
import random

#
# This node is the state machine that controls robot behavior
#
# TODO: call mapUpdater.make obstacle when 

#####################
## state methods ####
#####################

# params: none
# returns: void
# starts all publishers, subscribers; loads map and fills associated variables 
def init():
    
    motionPub = rospy.Publisher("command/Motors", MotionMsg);
    bumpSub = rospy.Subscriber('bumpData', BumpMsg, handleBumpMsg);
    blockSeenSub = rospy.Subscriber('blockSeen', PoseMsg, handleBlockSeenMsg);
    obstacleAheadSub = rospy.Subscriber('obstacleAhead', ObstacleAheadMsg, handleObstacleAheadMsg);


    # loading and processing map
    #[blockLocations, mapList] = mapParser.parseMap('map.txt', currentPose);
    return

# params: none
# returns: none
# Robot needs more blocks, but does not know where to find them.
# perform ant-like motion; move forward. If hit an obstacle, turn right or left randomly
# increment wanderCount; when it maxes out, spin around
def wander():
    
    # if wanderCount has counted up enough, spin 360 degrees
    if (wanderCount >= MAX_WANDER_COUNT):

       # stop forward motion
       motionPlanner.stopWheels() 

       # wanderCount holds at the max value while turning
       doneTurning = motionPlanner.rotate360()
       if(doneTurning):
           wanderCount = 0

    # travel forward, incrementing wanderCount
    # when an obstacle is seen, handleObstacleAhead will be called to cause turning
    else:
       motionPlanner.translate(.01)
       time.sleep(.5)
       wanderCount +=1
    return 

# params: none
# returns: none
# Robot needs to move from it's current location to a destination
# global waypoints contains a list of points the robot can travel to in straight-line paths
def travel():
     
    # if have reached the current waypoint, stop and move to the next waypoint. If at end of wayponts, enter seatch
    if (getDistance(currentPose, waypointList[0]) < POSITION_THRESHOLD):
        motionPlanner.stopWheels();
        previousWaypointPose = currentPose;

        # if there are more waypoints, travel to next one
        if len(waypointList > 1):
            waypointList = waypointList[1:];

        # if there are no more waypoints, are at expected block location. start searching.     
        else:
            robotState = 'searching';

    # if haven't reached the current waypoint, travel towards it
    else:
        # takes currentPose, destinationLoc, angVel, vel, startPose
        motionPlanner.travelTowards(currentPose, waypointList[0], .01, .1, previousWaypointPose); 
    return

# params: none
# returns: none
# Robot has reached a place where it believes a block is
# turn in slow circle looking for block. If time-out, reject block location and set course for a new one
# search gets called repeatedly, so can switch states in the middle
def search():
    motionPlanner.rotate(.1);  #TODO: check that this is a good speed
    
    # if haven't been searching too long, keep searching
    if(searchCount < MAX_SEARCH_COUNT):
        time.sleep(.5);            # should be moving slow enough that nothing passes in and out of view in this time
                                   # sleeping keeps count from getting crazy-high
        searchCount += 1;

    # if have been searching too long, give up on this block. Might not actually exist
    else: 

       # if know the locations of more blocks, set path to them and go get them                                      
       if (len(blockLocations) > 1):
           blockLocations = blockLocations[1:];
           waypointsList = pathPlanner.plotPath(currentPose, blockLocations[0])
           robotState = 'travelling'

       # if don't know the location of more blocks, wander                                                           
       else:
           robotState = 'wandering'
           wanderCount = 0
    return

# params: none
# returns: none
# Robot has a block currently in its vision. 
# Move until block hits the bump sensors at the mouth of the conveyor belt
def consume():
    # blockLocation is updatd by kinect to be accurate
    motionPlanner.travelTowards(currentPose, blockLocations[0], .01, .01, lastWaypointPose);
    return

# params: none
# returns: none
# Robot has a block on the conveyor belt
# Move conveyor belts so block is added to wall
# this is an atomic action
def digest():
    # TODO: this is a simple implementation that doesn't reverse belts if something gets stuck

    # stop wheel motors and start ramp motors to move block up ramp
    motionPlanner.stopWheels();
    motionPlanner.startEatingBelts();
    motionPlanner.setHamperAngle(10); # so block can fall down sucessfully
                                      # TODO: get a real value for this

    # wait for block to go up the ramp
    while(inHamperFlag < 1): 
        time.sleep(1);   # wait for one second
  
    time.sleep(1);       # in case block needs to finish falling
    inHamperFlag = 0     # resetting flag

    # once block is in hamper, use hamper belts to push block into forming wall
    motionPlanner.setHamperAngle(0); # fully close hamper so conveyor belts can work
    motionPlanner.startHamperBelt();
    time.sleep(5)                    # wait for blocks to be pushed into right shape

    
    # turn off motors 
    motionPlanner.stopConveyorBelt();

    # if know the locations of more blocks, set path to them and go get them
    if (len(blockLocations) > 1):
        blockLocations = blockLocations[1:];
        waypointsList = pathPlanner.plotPath(currentPose, blockLocations[0])
        robotState = 'travelling'
    # if don't know the location of more blocks, wander
    else:
        robotState = 'wandering'
        wanderCount = 0
    return

# params: none
# returns: void
# Robot has finished gathering blocks; commands robot to travel to endLocation and open hatch
def dispense():
    
    wayPointList = pathPlanner.plotPath(currentPose, END_LOCATION);

    while len(wayPointList) > 0: 
        # if have reached the current waypoint, stop and move to the next waypoint. If at end of wayponts, enter seatch
        if (getDistance(currentPose, waypointList[0]) < POSITION_THRESHOLD):
            motionPlanner.stopWheels();
            previousWaypointPose = currentPose;

            # if there are more waypoints, travel to next one
            if len(waypointList > 1):
                waypointList = waypointList[1:];
        
            # otherwise, at end location
            else: 
                break

        # if haven't reached waypoint, keep travelling towards it
        else: 
            motionPlanner.travelTowards(currentPose, waypointList[0], .01, .1, previousWaypointPose); 

      # have reached end location. Open hamper and drive away
      motionPlanner.setHamperAngle(math.pi/2.0); 
      motionPlanner.translate(.01)
      time.sleep(.5)
      motionPlanner.stopWheels()
    return


##################
# Helper methods #
##################

# params: pose: a Pose
#         loc: a Location 
# returns: distance between them (absolute value)
def getDistance(pose, loc):
   return math.sqrt((loc1.getX()-loc2.getX())**2 + (loc1.getY()-loc2.getY())**2) 

# params: loc1: a Location
#         loc2: a Location 
# returns: distance between them (absolute value)
def getDistance(loc1, loc2):
   return math.sqrt((pose.getX()-loc.getX())**2 + (pose.getY()-loc.getY())**2) 

###############################
# Interrupt-handling methods ##
###############################

# params: none
# returns: none
# based on which bump sensor went off, call appropriate respose
def handleBumpMsg(msg):
    # bump sensors 0 and 1 are at the mouth
    if (msg.bumpNumber <= 1):
        #if are currently searching and something hits these bump sensors, are eating the block
        if robotState == 'consuming': 
            searchCount = 0;
            robotState = 'digesting';
    # bump sensor 2 is at the end of the conveyor belt. Indicates that partially done digesting
    elif (msg.bumpNumber == 2):
        if (robotState == 'digesting'):
            inHamperFlag = 1; #used by digesting method

    else:
        #msg = MotionMsg(); # defaults to 0
        motionPlanner.stopWheels();
        # not sure where other bump sensors on chassis will be; 
        # should stop and back away if they are hit
        # TODO    
    return

# param: msg PoseMsg
# return: none
# takes in message from kinect indicating that a block has been seen
def handleBlockSeenMsg(msg):
   
   newBlockLocation = location.Location(msg.xLoc, msg.yLoc)

   if robotState == 'wandering': 
       motionPlanner.stopWheels();
       blockLocations.append(newBlockLocation)
       robotState == 'searching'

   elif robotState == 'travelling':

       motionPlanner.stopWheels();

       # if have reache the block travelling towards, just enter search
       if (getDistance(newBlockLocation, blockLocations[0]) < KINECT_ERROR):
            robotState = 'searching'

       # if found a new block while travelling, add it to blockLocations
       else:
           blockLocations = [newBlockLocation].append(blockLocations)
           robotState = 'searching'

   # if consuming and see a new block
   elif (robotState == 'consuming') and (getDistance(newBlockLocation, blockLocations[0]) < KINECT_ERROR):
       # will continue consuming, but make this block the next block to get
       blockLocations = [blockLocations[0], newBlockLocation].append(blockLocations[1:])

   elif robotState == 'digesting':
       # continue digesting, bt make this next block to get
       blockLocations = [blockLocations[0], newBlockLocation].append(blockLocations[1:])

   return

# param: msg
# return: none
# when an obstacle is directly ahead: stop. if travelling or depositing, wait and then remake path. 
# If wandering, turn randomly 90 deg right or left
def handleObstacleAheadMsg(msg):

    # if robot is wandering, turn 90 deg left or right
    if robotState == 'wandering':
        motionPlanner.stopWheels()
        startAngle = currentPose.getAngle()
       
        # decide whether to turn right or left
        rand = random.random()
        if rand < 0.5: 
            goalAngle = startAngle + math.pi/2.0
        else:
            goalAngle = startAngle - math.pi/2.0
        
        # turn until turn is completed
        while (math.abs(currentPose.getAngle() - goalAngle) > motionPlanner.ANG_ERR):
            motionPlanner.rotateTowards(currentPose.getAngle(), goalAngle, .01, startAngle)

    # if not wandering, wait for mapUpdater to update with obstacle ahead and replan path
    else:
        time.sleep(2)
        waypointsList = pathPlanner.plotPath(currentPose, blockLocations[0])
        
    return

############
## Main ####
############


if __name__ == '__main__':
    # defining variables related to robot's state
    robotState = 'wandering'               # the state of the robot; can be wandering, traveling, searching, consuming
                                           #    digesting, dispensing 
    searchCount = 0                        # counter used to ensure that don't spend too long searching
    numBlocksCollected = 0                 # number of blocks that the robot has collected so far
    mapList = []                           # list of obstacles
    blockLocations = []                    # list of locations of blocks
    waypointList = []                      # list of waypoints to current destination
    inHamperFlag = 0                       # flag used to indicate whether a block on the conveyor belt
                                           #  has made it to the hamper
    wanderCount = 0                        # while wandering, count up and turn
    currentPose = pose.Pose(0.0,0.0,0.0);
    previousWaypointPose = pose.Pose(0.0, 0.0, 0.0); #used for travel
   

    # objects
    motionPlanner =motionplanner.MotionPlanner();
    pathPlanner = pathplanner.PathPlanner();   

    # constants
    NUM_BLOCKS_NEEDED = 9                  # number of blocks needed to complete wall
    MAX_SEARCH_COUNT = 500                 # arbitrary value; should be tested and set
    POSITION_THRESHOLD = .005              # acceptable error to reaching a position. In meters.    
    END_LOCATION = location.Location(0.0, 0.0);
    MAX_WANDER_COUNT = 30;
    KINECT_ERROR = .01;                    # uncertainty of the kinect in m

    try:
        init()                             # load map, initialize publishers, subscribers

        # while the robot needs more blocks to complete the wall, gather blocks
        while(numBlocksCollected < NUM_BLOCKS_NEEDED):
            if (robotState == 'wandering'):
                wander()

            elif (robotState == 'traveling'):
                travel()

            elif (robotState == 'searching'):
                search()

            elif (robotState == 'consuming'):
                consume()

            elif (robotState == 'digesting'):
                digest()

        # once the robot has enough blocks to complete the wall, leave the wall    
        dispense()
        rospy.spin()          # keeps python from exiting until node is stopped
    except rospy.ROSInterruptException: pass
