#!/usr/bin/env python  
import rospy
import motionplanner
import pathplanner
import location
import math
import pose
import mapParser
from gc_msgs.msg import MotionMsg  # for sending commands to motors             
from gc_msgs.msg import BumpMsg    # for listening to bump sensors              
from gc_msgs.msg import PoseMsg    # for listening to when the kinect sees a block                      
from gc_msgs.msg import ObstacleAheadMsg    # for listening to when the kinect sees a wall                                     
from gc_msgs.msg import StateMsg
from gc_msgs.msg import ObstacleMsg
from gc_msgs.msg import GUIPointMsg
from gc_msgs.msg import GUIPolyMsg
import time
import random


# this node is a state machine that implements simple wander behavior
# it moves forward, spinning ocasionally, turning 90 degrees when it sees an obstacle
# until it gets a message from kinect with block location. Then it traels to the block and eats it

class simpleRobotBrain(object):
    
    def __init__(self):

        # variables to ensure that when time is almost out, robot dumps blocks
        self.startTime = time.time() # in seconds
        self.dumpTime = 9.5*60       # with 30 seconds left, stop whatever doing and dump blocks. Time in seconds.

        # state variables 
        self.wanderCount = 0
        self.wanderCountMax = 400*30; # ten counts per second, 30 seconds before turn
        self.blockLocation = None
        self.currentPose = pose.Pose(0., 0., 0.)
        self.motionPlanner = motionplanner.MotionPlanner(self.startTime, self.dumpTime)
        self.bumpFlag = 0

        
        # making subscribers and publishers
        self.odometrySub = rospy.Subscriber("/sensor/currentPose", PoseMsg, self.handleOdometryMsg)
        self.bumpSub = rospy.Subscriber('/sensor/Bump', BumpMsg, self.handleBumpMsg);
        self.blockSeenSub = rospy.Subscriber('/sensor/blockSeen', PoseMsg, self.handleKinectMsg);    
        self.guiPointPub = rospy.Publisher('/gui/Point', GUIPointMsg);
        self.guiPolyPub = rospy.Publisher('/gui/Poly', GUIPolyMsg);
        self.statePub = rospy.Publisher('command/State', StateMsg);

        # initializing node
        rospy.init_node('simpleRobotBrain')
        msg = StateMsg()
        msg.state = "init"
        self.statePub.publish(msg)
        self.motionPlanner.stopWheels() # for hal

        # loading map
        [blockLocations, self.mapList] = mapParser.parseMap('/home/rss-student/rss-2014-team-3/src/robotbrain/src/map.txt', self.currentPose)
        self.displayMap()

        

        return

    def onShutdown(self):
        self.motionPlanner.stopWheels()
        self.motionPlanner.stopConveyorBelts()
        return


    def main(self):

        # if time is almost running out, open hamper and move away
        if (time.time() - self.startTime >= self.dumpTime): 
            self.releaseBlocks()

        # if bumpFlag is on, have just bumped into an obstacle; need to back up and turn
        if self.bumpFlag != 0:
            self.backupAndTurn()

        # if don't know where a block is, move ahead (until encounter obstacle)
        if self.blockLocation == None:
            #self.wander()
            pass
        # if know where a block is, move towards it with conveyor belts on
        else:
            self.consumeBlock()

            return


    # open hamper and drive away
    def releaseBlocks(self):
        # send message to reset hal, so it won't try to complete a previous motion
        msg = StateMsg()
        msg.state = "init"
        self.statePub.publish(msg)
        
        # open hamper
        self.motionPlanner.setHamperAngle(math.pi/2)

        # drive forward .1 meters
        self.motionPlanner.translateTo(.01)

        return



    # when hit a wall, back up and turn a little
    def backupAndTurn(self):
            
        # stop and rotate 90 degrees right or left
        self.motionPlanner.stopWheels()
        

        # back up a little
        for count in range(100): 
            self.motionPlanner.translate(-.1) 
            time.sleep(.01)


        # stop backing up
        self.motionPlanner.stopWheels()


        # turn 90 degrees right or left
        #rand = random.random() - .5 # random number from -.5 to .5
        #self.turn90(rand)
        self.turn30(1)

            
        # done with bump behavior
        self.bumpFlag = 0

        return 

    # moves to blockLocation, eats block
    def consumeBlock(self):
        print 'consuming block'
        self.motionPlanner.startBothBelts()
        self.motionPlanner.setHamperAngle(.15) # open slightly so blocks can fall
        self.motionPlanner.travelTo(self.currentPose, self.blockLocation)

        # should be at block location
        print 'at block'
        self.motionPlanner.stopWheels()

        # wait for 15 seconds for block to fall in chute, then close chute
        waitTimeStart = time.time()
        while ((time.time() - watiTimeStart < 15) and (time.time() < self.dumpTime)):
            pass
        
        self.motionPlanner.setHamperAngle(0) # cloes hamper

        # wait for 10 seconds for block to get pushed into place
        waitTimeStart = time.time()
        while ((time.time() - watiTimeStart < 10) and (time.time() < self.dumpTime)):
            pass

        # clear blockLocation to resume wandering behavior
        self.blockLocation = None 
        return


    # when don't know where a block is and haven't just hit a wall, move forward
    # rotate 360 every so often to look for a block
    def wander(self):

        # after wandercount time has passed, rotate 360 to look for a block
        if self.wanderCount > self.wanderCountMax:
            self.wanderCount = 0
            print 'rotating 360'
            self.turn360()


        # if wandercount is still counting up, move forward
        else:
            self.motionPlanner.translate(.01)
            time.sleep(.001)
            self.wanderCount += 1

        return

    # prints map to guiMAP to display
    def displayMap(self):
        for obstacle in self.mapList:
            # filling up metadata
            msg = GUIPolyMsg()
            msg.filled = 0
            msg.closed= 1

            # filling up color black
            msg.c.r = 0
            msg.c.g = 0
            msg.c.b = 0

            # opening up object to get points
            xList = []
            yList = []
            
            for point in obstacle.getLocationList():
                xList.append(point.getX())
                yList.append(point.getY())

            msg.x = xList
            msg.y = yList 
            self.guiPolyPub.publish(msg)

        # return after sending all obstacles
        return

    def handleBumpMsg(self, msg):
        # used as flag to set state
        self.bumpFlag = 1
        return


    # handles message from kinect with block location
    # if no block is currently being consumed, sets location of a block that should be consumed
    def handleKinectMsg(self, msg):
        # save blockLocation to blockLocation 
        newBlockLocation = location.Location(msg.xPosition, msg.yPosition)

        if self.blockLocation == None:
            self.blockLocation = newBlockLocation            
            print 'setting blockLocation', self.blockLocation.getX(), self.blockLocation.getY()

        return

    # updates currentPose based on odometry's message
    def handleOdometryMsg(self, msg):
        self.currentPose.setX(msg.xPosition)
        self.currentPose.setY(msg.yPosition)
        self.currentPose.setAngle(msg.angle)
        return

    # turn 90 degrees, direction indicated by sign of sign
    def turn90(self, sign):
        print 'turning 90'
        goalAngle = self.currentPose.getAngle() + math.copysign(math.pi/2, sign)
        doneRotating = False

        # rotate until done, or until is time to dump blocks                                                                      
        while ((not doneRotating) and (time.time() < self.dumpTime)):
            doneRotating = self.motionPlanner.rotateTowards(self.currentPose.getAngle(), goalAngle, .8)

        return

    # turn 30 degrees, direction indicated by sign of sign
    def turn30(self, sign):
        print 'turning 90'
        goalAngle = self.currentPose.getAngle() + math.copysign(math.pi/6, sign)
        doneRotating = False

        # rotate until done                                          
        while ((not doneRotating) and (time.time() < self.dumpTime)):                                     
            doneRotating = self.motionPlanner.rotateTowards(self.currentPose.getAngle(), goalAngle, .8)

        return



    def turn360(self):
        # make four turns                                                                                 
        # want to be able to exit if see a block and state changes; hence condition on state              
        # a 90 degree turn is probably okay                                                               
        numTurnsLeft = 4

        while (self.blockLocation == None and numTurnsLeft > 0):
            self.turn90(1);
            numTurnsLeft -=1;
        return


if __name__ == '__main__':


    try:

        robotbrain = simpleRobotBrain()
        while(True):
            robotbrain.main()             
            #robotbrain.motionPlanner.travelTowards(robotbrain.currentPose, location.Location(0., 0.), .8, .5)
        #print 'calling travelTo'
#        robotbrain.motionPlanner.rotateTo(math.pi/6)
        #robotbrain.motionPlanner.translateTo(.1)
        #robotbrain.motionPlanner.travelTo(robotbrain.currentPose, 
        #                                  location.Location(0, 0))
#        robotbrain.motionPlanner.startBothBelts()
            time.sleep(.001)
        rospy.spin()

    except rospy.ROSInterruptException: pass
