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
from gc_msgs.msg import ObstacleMsg
import time
import random


# this node is a state machine that implements simple wander behavior
# it moves forward, spinning ocasionally, turning 90 degrees when it sees an obstacle
# until it gets a message from kinect with block location. Then it traels to the block and eats it

class simpleRobotBrain(object):
    
    def __init__(self):
        # state variables 
        self.wanderCount = 0
        self.wanderCountMax = 400*30; # ten counts per second, 30 seconds before turn
        self.blockLocation = None
        self.currentPose = pose.Pose(0., 0., 0.)
        self.motionPlanner = motionplanner.MotionPlanner()
        self.bumpFlag = 0
        
        # making subscribers and publishers
        self.odometrySub = rospy.Subscriber("/sensor/currentPose", PoseMsg, self.handleOdometryMsg)
        self.bumpSub = rospy.Subscriber('/sensor/Bump', BumpMsg, self.handleBumpMsg);
        self.blockSeenSub = rospy.Subscriber('/sensor/blockSeen', PoseMsg, self.handleKinectMsg);
        
        self.guiPointPub = rospy.Publisher('/gui/Point', GUIPointMsg);
        self.guiPolyPub = rospy.Publisher('/gui/Poly', GUIPolyMsg);
        # initializing node
        rospy.init_node('simpleRobotBrain')
        self.motionPlanner.stopWheels() # for hal

        # loading map
        [blockLocations, mapList] = mapParser.parseMap('/home/rss-student/rss-2014-team-3/src/robotbrain/src/map.txt', self.currentPose)

        return

    def onShutdown(self):
        return


    def main(self):

        # if bumpFlag is on, have just bumped into an obstacle; need to back up and turn
        if self.bumpFlag != 0:
            
            # stop and rotate 90 degrees right or left
            self.motionPlanner.stopWheels()

            print 'backing up'
            # back up a little
            for count in range(100): 
                self.motionPlanner.translate(-.1) 
                time.sleep(.01)

            # stop backing up
            self.motionPlanner.stopWheels()

            print 'turning'
            # turn 90 degrees right or left
            #rand = random.random() - .5 # random number from -.5 to .5
            #self.turn90(rand)
            self.turn30(1)
            print 'done handling bump'
            
            # done with bump behavior
            self.bumpFlag = 0

        # if don't know where a block is, move ahead (until encounter obstacle)
        if self.blockLocation == None:

            # after wandercount time has passed, rotate 360 to look for a block
            if self.wanderCount > self.wanderCountMax:
                self.wanderCount = 0
                print 'rotating 360'
                self.turn360()


            # if wandercount is still counting up, move forward
            else:
                print 'moving forward'
                self.motionPlanner.translate(.1)
                time.sleep(.001)
                self.wanderCount += 1

        # if know where a block is, move towards it with conveyor belts on
        else:
            print 'moving to block'
            self.motionPlanner.startBothBelts()
            self.motionPlanner.setHamperAngle(.15) # open slightly so blocks can fall
            atblock = self.motionPlanner.travelTowards(currentPose, blockLocation, 0.2, 0.5)

            # if have arrived at block location, presumably have eaten it; stand still and wait to consume. 
            if atblock:
                print 'at block'
                self.motionPlanner.stopWheels()
                time.sleep(15) # wait for block to fall in chute, then close chute
                self.motionPlanner.setHamperAngle(0) # cloes hamper
                time.sleep(10) # wait for block to get pushed into place
                self.blockLocation = None # clear blockLocation and resume wandering behavior

            return

    # prints map to guiMAP to display
    def displayMap(self):
        for obstacle in self.mapList:
            # filling up metadata
            msg = GUIPolyMsg()
            msg.setFilled(0)
            msg.setClosed(1)

            # filling up color black
            msg.getC().seR(0)
            msg.getC().setB(0)
            msg.getC().setG(0)

            # opening up object to get points
            xList = []
            yList = []
            
            for point in obstacle.getLocationList():
                xList.append(point.getX())
                yList.append(point.getY())

            msg.setX(xList)
            msg.setY(yList)
            guiPolyPub.publish(msg)

        # return after sending all obstacles
        return

    def handleBumpMsg(self, msg):

        print 'in bump msg'
        if msg.bumpNumber == 2:
            self.bumpFlag = -1
        elif msg.bumpNumber ==3:
            self.bumpFlag = 1
        return


    def handleKinectMsg(self, msg):
        # save blockLocation to blockLocation 
        newBlockLocation = location.Location(msg.xLoc, msg.yLoc)
        self.blockLocation = newBlockLocation
        
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

        # rotate until done                                                                               
        while(not doneRotating):
                doneRotating = self.motionPlanner.rotateTowards(self.currentPose.getAngle(), goalAngle, .8)

        return

    # turn 30 degrees, direction indicated by sign of sign
    def turn30(self, sign):
        print 'turning 90'
        goalAngle = self.currentPose.getAngle() + math.copysign(math.pi/6, sign)
        doneRotating = False

        # rotate until done                                                                               
        while(not doneRotating):
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
            time.sleep(.001)
        rospy.spin()

    except rospy.ROSInterruptException: pass
