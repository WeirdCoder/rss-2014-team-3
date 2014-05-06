#!/usr/bin/env python                                                                                                              
import rospy

from gc_msgs.msg import BumpStatusMsg    # for listening to bump sensors                                                         
from gc_msgs.msg import StateMsg
from gc_msgs.msg import MotionVoltMsg
from gc_msgs.msg import ConveyorMsg
from gc_msgs.msg import HamperMsg
import time
import random

class wanderRobotBrain(object):
    def __init__(self):
        
        # variables to track state
        self.robotState = 'wander'
        self.bumpStatus = [0,0]                  # for left and right bump sensors; 1 if either are being pressed
        self.endTime = time.time() + 300         # TODO: extend to full game time
        self.numBumps = 0                        # counts up number of bumps
        self.BUMPS_BEFORE_SPIN = 5
        self.actionTimeout = time.time()         # timer used to decide when actions to complete

        # creating publishers and subscribers
        self.bumpSub = rospy.Subscriber('/sensor/BumpStatus', BumpStatusMsg, self.handleBumpMsg);
        self.blockSeenSub = rospy.Subscriber('/sensor/blockSeen', PoseMsg, self.handleKinectMsg);
        self.wheelPub = ropsy.Publisher('/command/MotorVolt', MotionVoltMsg)
        self.conveyorPub = ropsy.Publisher('/command/Conveyor', ConveyorMsg)
        self.hamperPub = ropsy.Publisher('/command/Hamper', HamperMsg)

        # start conveyrs and close hamper
        self.startBothBelts()
        self.setHamperAngle(0)

        return

    def main(self):
        # main loop gets called from a while(true); robot's behavior depends on state
        
        if self.robotState == 'wandering':
            self.wander()

        elif self.robotState == 'hitLeft':
            self.hitLeft()

        elif self.robotState == 'hitRight':
            self.hitRight()

        elif self.robotState == 'hitBoth':
            self.hitBoth()

        elif self.robotState == 'spin':
            self.spin()

        elif self.robotState == 'dispense':
            self.dispense

        elif self.robotState == 'visualServo':
            self.visualServo()

        elif self.robotState == 'done':
            self.done()

        return

####################
# State functions ##
####################

    # TODO
    
    # robot moves forward
    def wander(self):
        self.wanderForward()
        self.changeStates()
        return 

    # behavior when left bumper is pressed
    def hitLeft(self):
        self.backUpLeft()
        
        # if done with action, revert to wander and increment bumpCount
        if time.time() > self.actionTimeout:
            self.robotState = 'wander'
            self.numBumps += 1
        return 

    # behavior when right bumper is pressed
    def hitRight(self):
        self.backUpRight()
        
        # if done with action, revert to wander and increment bumpCount
        if time.time() > self.actionTimeout:
            self.robotState = 'wander'
            self.numBumps += 1
        return

    # behavior when both bumpers are pressed
    def hitBoth(self):
        self.backUpBoth()
        
        # if done with action, revert to wander and increment bumpCount
        if time.time() > self.actionTimeout:
            self.robotState = 'wander'
            self.numBumps += 1
        return

    # robot spins around
    def spin(self):
        self.spinTurn()
        
        # if done with action, clear bumpCount
        if time.time() > self.actionTimeout:
            self.numBumps = 0
        
        # pays attention to new bumps
        self.changeStates()

        return

    # open hamper and drive off
    def dispense(self):

        # dispense1: stop and pause for a second
        self.stopWheels()
        time.sleep(1)

        # dispense2: open hopper and wait for a second
        self.setHamperAngle(1)
        time.sleep(1)

        # dispense3: drive forward for 5 seconds
        self.actionTimeout = time.time() + 5
        while time.time() > self.actionTimeout:
            self.dispenseForward
        
        self.robotState == 'done'
        return

    # done
    def done(self):
        self.setHamperAngle(0)
        return

    # method for determining transfer of states
    def changeStates(self):
        
        # if both bump sensors are hit
        if self.bumpStatus[0] and self.bumpStatus[1]:
            self.robotState == 'hitBoth'
            self.actionTimeout = time.time() + 3

        # if just left bump sensor is hit
        elif self.bumpStatus[0]:
            self.robotState == 'hitLeft'
            self.actionTimeout = time.time() + 3

        # if just right bump sensor is hit
        elif self.bumpStatus[1]:
            self.robotState == 'hitRight'
            self.actionTimeout = time.time() + 3

        # if have been bumped too many times, spin
        elif self.numBumps > self.BUMPS_BEFORE_SPIN:

            # if already spinning, don't change actionTimeout
            if self.robotState != 'spin':
                self.actionTimeout = time.time() + 5

            self.robotState == 'spin'

            
        # if have run out of time
        elif time.time() > self.endTime:
            self.robotState == 'dispense'
            self.actionTimeout = time.time() + 1

        # if not doing anything else, wander
        else:
            self.robotState == 'wander'
        return

################################
# Subscriber/Publisher Handles #
################################

    #############################
    # Wheel Control Publishers ##
    #############################

    # stop wheels
    def stopWheels(self):
        msg = MotionVoltMsg
        msg.rightVolage = 0
        msg.leftVoltage = 0
        self.wheelPub.publish(msg)
        return

    # move forward (called when wandering)
    def wanderForward(self):
        msg = MotionVoltMsg
        msg.rightVolage = .55
        msg.leftVoltage = .5
        self.wheelPub.publish(msg)
        return

    # back up and turn, as called in hit left state
    def backUpLeft(self):
        msg = MotionVoltMsg
        msg.rightVolage = -.5
        msg.leftVoltage = -.3
        self.wheelPub.publish(msg)
        return

    # back up and turn, as called in hit right state
    def backUpRight(self):
        msg = MotionVoltMsg
        msg.rightVolage = -.5
        msg.leftVoltage = .3
        self.wheelPub.publish(msg)
        return        

    # back up, as called in hitBoth state
    def backUpBoth(self):
        msg = MotionVoltMsg
        msg.rightVolage = -.4
        msg.leftVoltage = -.4
        self.wheelPub.publish(msg)
        return        

    # turn, as called in spin
    def spinTurn(self):
        msg = MotionVoltMsg
        msg.rightVolage = -.5
        msg.leftVoltage = .5
        self.wheelPub.publish(msg)
        return
    
    # move forward, as called in dispense
    def dispenseForward(self):
        msg = MotionVoltMsg
        msg.rightVolage = .5
        msg.leftVoltage = .5
        self.wheelPub.publish(msg)
        return        


    ##################################
    # Hamper and Conveyor Publishers #
    ##################################
    
    # sends messages to start both conveyor belts                                                                                
    def startConveyorBelts(self):
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = 1.0
        msg.backConveyorFractionOn = 1.0
        self.conveyorPub.publish(msg)
        return

    # sends messages to stop the conveyor belts that consume blocks                                                               
    def stopConveyorBelts(self):
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = 0.0
        msg.backConveyorFractionOn = 0.0
        self.conveyorPub.publish(msg)
        return

    # open/close hamper
    def setHamperAngle(self, fractionOpen):
        msg = HamperMsg()
        msg.fractionOpen = fractionOpen
        self.hamperPub.publish(msg)
        return


    ######################
    # Subscriber Methods #
    ######################
    
    # given bumper messages, update self.bump
    def handleBumpMsg(self, msg):
        # messages gives too booleans [leftPressed, rightPressed]
        self.bumpStatus = msg.bumpStatus
        return

    # handle message from kinect
    def handleKinectMsg(self, msg):
        # TODO
        pass


####################
# Helper functions #
####################


########
# Main #
########

if __name__ == '__main__':
    try:
        print 'running as main'
        robotbrain = wanderRobotBrain()
        
        while(True):
            robotbrain.main()
        
    except rospy.ROSInterruptException: pass
