#!/usr/bin/env python
import rospy
import math
import time
import location.Location as Location
import pose.Pose as Pose
#
# This node recieves encoder messages, calculates the robot's current position, and sends out the robot's current position.
# It also keeps track of the robot's uncertainty in position, which it updates upon recieving an uncertainty message. The uncertainty is sent out in the position message. The position includes (x position, y position, angle)
#


####################
# helper functions #
####################

# params: deltaTicks: int[] [new ticks on left wheel, new ticks on right wheel]
# returns: void
# updates currentPose given new Ticks, sends message 
def updateCurrentPose(deltaTicks):

    # calculating distance travelled by each wheel
    leftWheelDist = float(deltaTicks[LEFT])*WHEEL_METERS_PER_TICK;
    rightWheelDist = float(deltaTicks[RIGHT])*WHEEL_METERS_PER_TICK;
    
    # calculating and changing theta
    deltaTheta = (leftWheelDist - rightWheelDist)/WHEELBASE;         # using small angle approximation
    newAngle = (currentPose.getAngle() - deltaTheta)%(2*math.pi)     # calculating new angle, wrapping aroun 2pi
    currentPose.setAngle(newAngle);

    # calculating and changing x, y position
    deltaX = (leftWheelDist + rightWheelDist)*math.cos(currentPose.getAngle())/2.0);
    deltaY = (leftWheelDist + rightWheelDist)*math.sin(currentPose.getAngle())/2.0);
    currentPose.setX(currentPose.getX() + deltaX);
    currrentPose.setY(currentPose.getY() + deltaY);

    # sending updated currentPose
    reportCurrentPose();

    return;


################################
# recieving, sending messages  #
################################


# params: EncoderMsg msg
# returns: none
# updates currentPosition based on the message; sends out updated currentPosition value (xPos, yPos, angle)
# assumes encoder message fixes signs so both wheels have posive ticks when the robot is moving forward
def handleEncoderMsg(msg):
    # calculating how much the wheels have moved in the past time step
    newTicks = [msg.lWheelTicks, msg.rWheelTicks]; # current tick positions of the wheels
    deltaTicks = [newTicks[LEFT] - currentTicks[LEFT], newTicks[RIGHT]-currentTicks[RIGHT]];
    
    # updating the current position and sending
    updateCurrentPose(deltaTicks);

    return

# params: uncertaintyMsg
# returns: none
# updates uncertainty based on the message; sends out updates currentPosition value
def handleUncertaintyMsg(msg):
    # TODO
    pass

# params: none
# returns none
# sends message containing currentPose
def reportCurrentPose():
    # forming message from currentPose
    msg = PoseMsg();
    msg.xPosition = currentPose.getX();
    msg.yPosition = currentPose.getY();
    msg.angle = currentPose.getAngle();

    posePub.publish(msg);
    
    return

#########
## main #
#########
if __name__ == '__main__':

    # initial values
    START_POSE = [0.0, 0.0, 0.0];

    # global variables
    currentTicks = [0.0,0.0];        # left, right values of ticks from last update
    currentPose = START_POSE;    # xPosition of robot origin, yPosiiton of robot origin
                                 #   angle of robot axis of symmetry with respect to x axis.
                                 #   All pose fields are floats

    #constants
    #TODO: get right values
    ENCODER_RESOLUITON = 2000;
    GEAR_RATIO = 65.5;
    WHEEL_RADIUS = .0625;  # in m
    WHEELBASE = .428       # in m
    TICKS_PER_REVOLUTION = ENCODER_RESOLUTION * GEAR_RATIO;
    WHEEL_METERS_PER_TICK = WHEEL_RADIUS * 2*math.pi/TICKS_PER_REVOLUTION;
    LEFT = 0;              # for indexing into encoder lists
    RIGHT = 0;             # for indexing into encoder lists
    

    #publishers and subscribers
    #TODO: uncertainty publisher and subscriber
    posePub = rospy.Publisher('currentPose', PoseMsg);
    encoderSub = rospy.Subscriber('sensor/Encoder', EncoderMsg, handleEncoderMsg);

    try:
        rospy.spin()                    # keep python from exiting until this node is stopped 
                                        # wait for odometry interrupts
    except rospy.ROSInterruptException: pass
