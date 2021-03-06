#!/usr/bin/env python                                                                                                              
import rospy

from gc_msgs.msg import BumpStatusMsg    # for listening to bump sensors                                                         
from gc_msgs.msg import StateMsg
from gc_msgs.msg import MotionVoltMsg
from gc_msgs.msg import ConveyorMsg
from gc_msgs.msg import HamperMsg
from gc_msgs.msg import KinectMsg
#from gc_msgs.msg import SonarStatusMsg
import time
import random

class wanderRobotBrain(object):
    def __init__(self):

        # variables to track state
        self.robotState = 'wander'
        self.bumpStatus = [0,0,0,0]              # for left and right bump sensors; 1 if either are being pressed
        self.endTime = time.time() + 100         # TODO: extend to full game time
        self.numBumps = 0                        # counts up number of bumps
        self.BUMPS_BEFORE_SPIN = 5
        self.actionTimeout = time.time()         # timer used to decide when actions to complete
        self.captureTimer = time.time()
        self.blockSeen = 0.0
        self.blockHeading = 0.0 
        self.pastInput = None
        self.hamperStatus = 'closed'
        self.hamperTimeout = time.time()

        # creating publishers and subscribers
        self.bumpSub = rospy.Subscriber('/sensor/BumpStatus', BumpStatusMsg, self.handleBumpMsg)
        #self.sonarStatusSub = rospy.Subscriber('/sensor/SonarStatus', SonarStatusMsg,self.handleSonarMsg)
        self.blockSeenSub = rospy.Subscriber('/sensor/kinect', KinectMsg, self.handleKinectMsg)
        self.wheelPub = rospy.Publisher('/command/MotorVolt', MotionVoltMsg)
        self.conveyorPub = rospy.Publisher('/command/Conveyor', ConveyorMsg)
        self.hamperPub = rospy.Publisher('/command/Hamper', HamperMsg)

        # initialize node
        rospy.init_node('robotbrain')

        # start conveyors and close hamper
        self.startConveyorBelts()
        self.setHamperAngle(0)

        return

    def main(self):
        # main loop gets called from a while(true); robot's behavior depends on state
        print self.robotState
        time.sleep(0.05)

        if self.robotState == 'wander':
            self.wander()
            self.wiggleHamper()
        elif self.robotState == 'capture':
            self.capture()
        elif self.robotState == 'hitLeft':
            self.hitLeft()

        elif self.robotState == 'hitRight':
            self.hitRight()

        elif self.robotState == 'hitBoth':
            self.hitBoth()

        elif self.robotState == 'spin':
            self.spin()

        elif self.robotState == 'dispense':
            self.dispense()

        elif self.robotState == 'visualServo':
            self.visualServo()
        #elif self.robotState == 'wiggle':
        #    while time.time() < self.actionTimeout:
        #        x =1
        #    self.wiggleHamper()
        elif self.robotState == 'done':
            self.done()

        return

####################
# State functions ##
####################

    # TODO
   
    #robot VisualServo
    def visualServo(self):
        input = self.blockHeading
        P = 1.0
        D = 0.1
        DMax = 0.4 #Maximum of differential correction.  Counter Dirac Changes
        RotationMax = 0.4 #Maximum rotation command
        MotorMax = 0.5
        output = P * input
        if self.pastInput != None and abs(input-self.pastInput) < DMax:
            #Able to add D term to controller
            output += D * (input - self.pastInput)
        self.pastInput = input

        #Transform output to equivalent L&R motor value
        forward = 0.3
        rotate = max(min(RotationMax, output),-RotationMax)
        motormsg = MotionVoltMsg()
        motormsg.leftVoltage = max(min((forward - rotate),MotorMax),-MotorMax)
        motormsg.rightVoltage = max(min((forward + rotate),MotorMax),-MotorMax)
        #print motormsg.leftVoltage, motormsg.rightVoltage
        self.wheelPub.publish(motormsg)
        time.sleep(0.03) #TODO adjust for the encoder loop
        if self.capture:
            self.captureTimer = time.time()+10
        self.changeStates()
    # capture: robot moves forward
    def capture(self):
        self.wanderForward()
        self.changeStates()
        return
    # robot moves forward
    def wander(self):
        self.wanderForward()
        #self.wiggleHamper()
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
        self.actionTimeout = time.time() + 3
        while time.time() < self.actionTimeout:
            time.sleep(.005)
            self.dispenseForward()
        
        self.robotState = 'done'
        time.sleep(0.05)
        return

    # done
    def done(self):
        self.setHamperAngle(0)
        self.stopConveyorBelts()
        while(True):
            self.stopWheels()
        return

    # method for determining transfer of states
    def changeStates(self):
        # back Digest bump sensor is hit
        #if self.bumpStatus[1]:
        #    self.robotState = 'wiggle'
        #    self.actionTimeout = time.time() + 2
        
        # if both bump sensors are hit
        if self.bumpStatus[2] and self.bumpStatus[3]:
            self.robotState = 'hitBoth'
            self.actionTimeout = time.time() + 3

        # if just left bump sensor is hit
        elif self.bumpStatus[2]:
            self.robotState = 'hitLeft'
            self.actionTimeout = time.time() + 3

        # if just right bump sensor is hit
        elif self.bumpStatus[3]:
            self.robotState = 'hitRight'
            self.actionTimeout = time.time() + 3

        # if have been bumped too many times, spin
        elif self.numBumps > self.BUMPS_BEFORE_SPIN:

            # if already spinning, don't change actionTimeout
            if self.robotState != 'spin':
                self.actionTimeout = time.time() + 5

            self.robotState = 'spin'

            
        # if have run out of time
        elif time.time() > self.endTime:
            self.robotState = 'dispense'
            self.actionTimeout = time.time() + 1
        elif self.captureTimer > time.time():
            self.robotState = 'capture'
        elif self.blockSeen>0.6:
            self.robotState = 'visualServo'
        # if not doing anything else, wander
        else:
            self.robotState = 'wander'
        return

################################
# Subscriber/Publisher Handles #
################################

    #############################
    # Wheel Control Publishers ##
    #############################

    # stop wheels
    def stopWheels(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = 0
        msg.leftVoltage = 0
        self.wheelPub.publish(msg)
        return
 
    # move forward (called when wandering)
    def wanderForward(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = .31
        msg.leftVoltage = .3
        self.wheelPub.publish(msg)
        return

    # back up and turn, as called in hit left state
    def backUpLeft(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = -.5
        msg.leftVoltage = -.3
        self.wheelPub.publish(msg)
        return

    # back up and turn, as called in hit right state
    def backUpRight(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = -.3
        msg.leftVoltage = -.5
        self.wheelPub.publish(msg)
        return        

    # back up, as called in hitBoth state
    def backUpBoth(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = -.4
        msg.leftVoltage = -.4
        self.wheelPub.publish(msg)
        return        

    # turn, as called in spin
    def spinTurn(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = -.5
        msg.leftVoltage = .5
        self.wheelPub.publish(msg)
        return
    
    # move forward, as called in dispense
    def dispenseForward(self):
        msg = MotionVoltMsg()
        msg.rightVoltage = .5
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
    
    #given sonar messages, update self.sonar
    
    #def handleSonarMsg(self,msg):
    #    self.sonarStatus = msg.sonarStatus
    #    return
    
    # given bumper messages, update self.bump
    def handleBumpMsg(self, msg):
        # messages gives too booleans [leftPressed, rightPressed]
        self.bumpStatus = msg.bumpStatus
        return

    # handle message from kinect
    def handleKinectMsg(self, msg):
        self.blockHeading = self.blockHeading*0.95+msg.blockHeading*0.05
        self.blockSeen = self.blockSeen*0.95+ float(int(msg.blockSeen)) *0.05
        pass


####################
# Helper functions #
####################

    # wiggles hamper so blocks can 
    def wiggleHamper(self):

        # if it is time to move the hamper
        if time.time() > self.hamperTimeout:
            # if the hamper is closed, open it
            if self.hamperStatus == 'closed':
                self.setHamperAngle(-.04)
                self.hamperStatus = 'ajar'

            # if the hamper is ajar, close it
            elif self.hamperStatus == 'ajar':
                self.setHamperAngle(.01)
                self.hamperStatus = 'closed'

            # increment hamperTimeout
            self.hamperTimeout = time.time() + 5
        self.changeStates()
        return
########
# Main #
########

if __name__ == '__main__':
    try:

        robotbrain = wanderRobotBrain()
        
        while(True):
            robotbrain.main()
        
    except rospy.ROSInterruptException: pass

