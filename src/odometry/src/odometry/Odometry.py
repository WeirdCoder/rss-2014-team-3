#!/usr/bin/env python
import rospy
import math
import time
from gc_msgs.msg import PoseMsg
from gc_msgs.msg import EncoderMsg
import location 
import pose

#
# This node recieves encoder messages, calculates the robot's current position, and sends out the robot's current position.
# It also keeps track of the robot's uncertainty in position, which it updates upon recieving an uncertainty message. The uncertainty is sent out in the position message. The position includes (x position, y position, angle)
#


class Odometry(object):
    
    # params: none
    # returns: none
    # assigns constants, create publishers, init node
    def __init__(self):
        # TODO
        # initial values
        START_POSE = pose.Pose(0.0, 0.0, 0.0)

        # global variables
        self.currentDist = [0.0,0.0]        # left, right values of ticks from last update
        self.currentPose = START_POSE    # xPosition of robot origin, yPosiiton of robot origin
                                    #   angle of robot axis of symmetry with respect to x axis.
                                    #   All pose fields are floats

        #constants
        #TODO: get right values
        self.WHEELBASE = .375       # in m
                               # TODO: measure
        self.LEFT = 0              # for indexing into encoder lists
        self.RIGHT = 0             # for indexing into encoder lists
    

        #publishers and subscribers
        #TODO: uncertainty publisher and subscriber

        self.posePub = rospy.Publisher('currentPose', PoseMsg);
        self.encoderSub = rospy.Subscriber('sensor/Encoder', EncoderMsg, handleEncoderMsg);

        # initializing node
        rospy.init_node('odometry')
        rospy.onShutdown(self.onShutdown)

        return

####################
# helper functions #
####################

    # param: none
    # return: none
    # on shutdown behavior
    def onShutdown(self):
        return

    # params: deltaTicks: int[] [new ticks on left wheel, new ticks on right wheel]
    # returns: void
    # updates currentPose given new Ticks, sends message 
    def updateCurrentPose(self, deltaDist):

        # calculating distance travelled by each wheel
        leftWheelDist = deltaDist[LEFT]
        rightWheelDist = deltaDist[RIGHT]
    
        # calculating and changing theta
        deltaTheta = (leftWheelDist - rightWheelDist)/self.WHEELBASE         # using small angle approximation
        newAngle = (self.currentPose.getAngle() - deltaTheta)%(2*math.pi)     # calculating new angle, wrapping aroun 2pi
        self.currentPose.setAngle(newAngle)

        # calculating and changing x, y position
        deltaX = (leftWheelDist + rightWheelDist)*math.cos(self.currentPose.getAngle())/2.0
        deltaY = (leftWheelDist + rightWheelDist)*math.sin(self.currentPose.getAngle())/2.0
        self.currentPose.setX(currentPose.getX() + deltaX)
        self.currrentPose.setY(currentPose.getY() + deltaY)

        # sending updated currentPose
        self.reportCurrentPose()
        
        return


################################
# recieving, sending messages  #
################################


    # params: EncoderMsg msg
    # returns: none
    # updates currentPosition based on the message; sends out updated currentPosition value (xPos, yPos, angle)
    # assumes encoder message fixes signs so both wheels have posive ticks when the robot is moving forward
    def handleEncoderMsg(self, msg):
        # calculating how much the wheels have moved in the past time step
        newDist = [msg.lWheelDist, msg.rWheelDist] # current tick positions of the wheels
        deltaDist = [newDist[LEFT] - currentDist[LEFT], newDist[RIGHT]-currentDist[RIGHT]]
    
        # updating the current position and sending
        self.updateCurrentPose(deltaDist)

        return

    # params: uncertaintyMsg
    # returns: none
    # updates uncertainty based on the message; sends out updates currentPosition value
    def handleUncertaintyMsg(self, msg):
        # TODO
        pass

    # params: none
    # returns none
    # sends message containing currentPose
    def reportCurrentPose(self):
        # forming message from currentPose
        msg = PoseMsg()
        msg.xPosition = currentPose.getX()
        msg.yPosition = currentPose.getY()
        msg.angle = currentPose.getAngle()
        
        self.posePub.publish(msg)
        
        return

#########
## main #
#########
if __name__ == '__main__':


    try:
        odometry = Odometry()
        rospy.spin()                    # keep python from exiting until this node is stopped 
                                        # wait for odometry interrupts
    except rospy.ROSInterruptException: pass
