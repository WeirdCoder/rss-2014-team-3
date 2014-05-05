import rospy
import math
import time
from gc_msgs.msg import ConveyorMsg
from gc_msgs.msg import HamperMsg
from gc_msgs.msg import EncoderMsg
from gc_msgs.msg import MotionMsg
from gc_msgs.msg import WheelErrorMsg
from gc_msgs.msg import MotionDistMsg
#
# This class is used by the RobotBrain to send PWM messages to the HAL to control the motion of the motors. This includes both wheel motors and theconveyor belt motors. Also has methods to abstract some elements of motor control; has proportional control for the wheels
#
#TODO: have HAL calculate and send current wheel angular velocities?

class MotionPlanner(object):
    def __init__(self):

        # kept updated to reflect robot status
        self.currentWheelVel = [0.,0.];     # has current velocities of each wheel in m/s
        self.currentWheelDist = [0., 0.];   # used for calculating velocity of each wheel
        self.previousDesiredAngVel = 0.
        self.previousDesiredTransVel = 0.
        self.currentTransVel = 0.
        self.currentAngVel = 0.
        self.lastEncoderMsgTime = time.clock() # time in seconds. Used for calculating current wheel velocities
        self.wheelError = 0.

        # constants for wheel motion
        # TODO: pick appropriate vales
        self.MAX_TRANS_ACCEL = .00001;    # maximum translation acceleration in m/s^2  
        self.MAX_ANG_ACCEL = .0001;      # maximum rotational  acceleration in rad/s^2  
        self.ANGULAR_ERR = .1;              # acceptable angular error in radians
        self.TRANS_ERR = 0.005;            # acceptable translation error in m
        self.MAX_WHEEL_ANG_VEL = 0.2;#was 1      # maximum angular velocity of wheels in rad/s
        self.WHEELBASE =  .375;             # distance from origin to wheel; similar to a robot radius
        self.LEFT_WHEEL = 0;                # for indexing into leftWheel, rightWheel tuples
        self.RIGHT_WHEEL = 1;


        
        # intialize publishers, subscribers
        self.conveyorPub = rospy.Publisher("/command/Conveyor", ConveyorMsg);
        self.hamperPub = rospy.Publisher("/command/Hamper", HamperMsg);
        self.encoderSub = rospy.Subscriber('/sensor/Encoder', EncoderMsg, self.handleEncoderMsg);
        self.motionPub = rospy.Publisher("/command/Motors", MotionMsg);
        self.positionPub = rospy.Publisher("/command/MotorsDist", MotionDistMsg);
        self.errorSub = rospy.Subscriber("/sensor/WheelErr", WheelErrorMsg, self.handleWheelErrMsg);
        return

#################################
# Publishers/Subscriber Methods #
#################################

    # params: wheel error msg
    # returns: none
    # updates self.wheelError from message contents - how far wheels have left to travel
    def handleWheelErrMsg(self, msg):
        self.wheelError = .5*(abs(msg.leftWheelError) + abs(msg.rightWheelError))
        return


    # params: EncoderMsg msg
    # returns: none
    # calculates current rWheelVel and lWheelVel from encoder message
    # assumes encoder message fixes signs so dist is positive when both wheels are moving forward
    def handleEncoderMsg(self, msg):
        # calculating how much the wheels have moved in the past time step, updating
        newDist = [msg.lWheelDist, msg.rWheelDist]; # current tick positions of the wheels             
        deltaDist = [newDist[self.LEFT_WHEEL] - self.currentWheelDist[self.LEFT_WHEEL], newDist[self.RIGHT_WHEEL]-self.currentWheelDist[self.RIGHT_WHEEL]];
        self.currentWheelDist = newDist
        
        # calculating how much time has passed, updating time
        currentTime = time.clock();
        deltaTime = currentTime - self.lastEncoderMsgTime;
        self.lastEncoderMsgTime = currentTime;

        # calculate and update the currentWheelAngVel parameter
        if (deltaTime > 0.):
            self.updateCurrentVel(deltaDist, deltaTime);
        return
        

#################
# Wheel motion ##
################# 

    # params: currentPose: pose containing current location of robot
    #         destinationPose: pose containing desination of robot (angle is 0)
    #         angVel: float angular velocity in rad/s
    #         vel; float velocity in m/s
    # returns: boolean: true when motion is complete
    # using rotateTowards and translateTowards, first rotates to face destination and then translates to it
    def travelTowards(self, currentPose, destinationLoc, angVel, vel):

        angleToDestination = math.atan2((destinationLoc.getY()-currentPose.getY()),(destinationLoc.getX()-currentPose.getX()));
        
        # if not currently facing the destination, rotate towards it so can translate there in a straight line
        # rotateTowards will not move if close enough

        doneRotating = self.rotateTowards(currentPose.getAngle(), angleToDestination, angVel)

        if doneRotating:
            # if the robot is facing the destination, move towards it

            doneTravelling = self.translateTowards(currentPose, destinationLoc, vel); 

            return doneTravelling;


        # if not done rotating, not done travelling
        else:
            return False

    # params: currentAngle: float currentAngle in radians
    #         destAngle: float destination angle in radians 
    #         angSpeed: float angular speed in rad/s 
    # returns: boolean: true when motion is complete
    # Calculates appropriate rotational speed using proportional control (accelerates and deacellerates
    #   based on distance to currentLoc. Calls rotate.
    def rotateTowards(self, currentAngle, destAngle, angSpeed):
        # calculating distance left to rotate; from [-pi, pi]

        distanceLeft = destAngle - currentAngle; 
        
        # want distance left [-pi, pi]
        if distanceLeft > math.pi:
            distanceLeft = distanceLeft - 2*math.pi

        elif distanceLeft < -math.pi:
            distanceLeft = distanceLeft + 2*math.pi

        # if are close enough already, don't move and return true
        if abs(distanceLeft) < self.ANGULAR_ERR:

            self.rotate(0.)
            return True


        # maximum current velocity is related to distanceLeft and acceleration
        # Want to deaccelerate to 0 when distanceLeft = 0. Hence the cap
        # v(t) = a*t
        # d(t) = .5a**2
        # t = sqrt(dist/.5a**2)
        # vmax = a*t = a*sqrt(distLeft/.5a**2)

        # speed is minimum of maxSpeed(distance), speed given, speed from accelerating
        currentMaxSpeed = self.MAX_ANG_ACCEL*math.sqrt(abs(distanceLeft)/(.5*self.MAX_ANG_ACCEL**2))
        acceleratedSpeed = abs(self.previousDesiredAngVel + math.copysign(self.MAX_ANG_ACCEL, distanceLeft))
        desiredSpeed = min(currentMaxSpeed, angSpeed, acceleratedSpeed)

         # moving in direction of distanceLeft
        desiredAngVel = math.copysign(desiredSpeed, distanceLeft)
        self.previousDesiredAngVel = desiredAngVel                    
        self.rotate(desiredAngVel);
        
        # still have more rotating to do
        return False
        
    # params: currentPose: Pose currentLocation
    #         destination: Pose destination
    #         speed: fload desired speed of motion in m/s 
    #         startPose: Pose robot held when motion was started
    # returns: void
    # Calculates appropriate translational speed using proportional control (accelerates and deacellerates
    #   based on distance to currentLoc. Calls translate
    # assumes that robot is facing desination 
    def translateTowards(self, currentPose, destination, speed):
        # calculating the magnitude and sign of the distance from currentLocation to desination
        currentDistanceVector = (destination.getX() - currentPose.getX(), destination.getY() - currentPose.getY());
        distanceMagnitude = math.sqrt(currentDistanceVector[0]**2 + currentDistanceVector[1]**2); 
        currentAngleVector = (math.cos(currentPose.getAngle()), math.sin(currentPose.getAngle()));
                              
        # if currentDistanceVector dot currentAngleVector is positive, then need to move forward
        # otherwise, need to move backwards, so distance will be negative
        dotProduct = currentDistanceVector[0]*currentAngleVector[0] + currentDistanceVector[1]*currentAngleVector[1];        
        velocitySign = math.copysign(1, dotProduct);

        # if are close enough already, don't move and return true
        if abs(distanceMagnitude) < self.TRANS_ERR:
            self.translate(0.)
            return True


        # maximum current velocity is related to distanceLeft and acceleration
        # Want to deaccelerate to 0 when distanceLeft = 0. Hence the cap
        # v(t) = a*t
        # d(t) = .5a**2
        # t = sqrt(dist/.5a**2)
        # vmax = a*t = a*sqrt(distLeft/.5a**2)

        # speed is minimum of maxSpeed(distance), speed given, speed from accelerating
        currentMaxSpeed = self.MAX_TRANS_ACCEL*math.sqrt(distanceMagnitude/(.5*self.MAX_TRANS_ACCEL**2))
        acceleratedSpeed = abs(self.previousDesiredTransVel + math.copysign(self.MAX_TRANS_ACCEL, velocitySign))
        desiredSpeed = min(currentMaxSpeed, speed, acceleratedSpeed)

         # moving in direction of distanceLeft
        desiredTransVel = math.copysign(desiredSpeed, velocitySign)

        self.previousDesiredTransVel = desiredTransVel                    
        self.translate(desiredTransVel);
        
        # still have more rotating to do
        return False

        # OLD CODE
        # calculating the magnitude of the velocity
        # if within slow-down region (less than .5*a^2 from destination and at least halfway there), use velocity proportional to distance 
        #currentTransVelocity = .5*self.WHEEL_RADIUS*(self.currentWheelAngVel[self.LEFT_WHEEL] + self.currentWheelAngVel[self.RIGHT_WHEEL]);

        #if (distanceMagnitude < .5*self.MAX_WHEEL_TRANS_ACCEL**2) and (distanceMagnitude/startDistance < .5):
        #    desiredVelocity = velocitySign*distanceMagnitude/self.MAX_WHEEL_TRANS_ACCEL;
        
        # otherwise, if less than the desired velocity, accelerate
        #elif abs(currentTransVelocity) < speed:
        #    desiredVelocity = currentTransVelocity + velocitySign*self.MAX_WHEEL_TRANS_ACCEL;
        
        # otherwise, want to keep traveling at the desired velocity
        #else:
        #    desiredVelocity = velocitySign*speed;
            
        #self.translate(desiredVelocity);
        
        #return

    # params: angVelocity: float angular velocity in rad/s
    # returns: void
    # sends MotionControl message to rotate
    def rotate(self, angVelocity):
        msg = MotionMsg();
        msg.translationalVelocity = 0.0;
        msg.rotationalVelocity = angVelocity;
        self.motionPub.publish(msg);
        return

    # params: velocity: float translational velocity in m/s
    # returns: void
    # sends MotionControl message to translate
    def translate(self, velocity):
        # send MotionControl Message
        msg = MotionMsg();
        msg.translationalVelocity = velocity;
        msg.rotationalVelocity = 0;
        self.motionPub.publish(msg);
        return

    # params: none
    # returns: none
    # sends MotionMsg stopping both wheels
    def stopWheels(self):

        self.previousDesiredAngVel = 0;
        self.previousDesiredTransVel = 0;
        msg = MotionMsg();
        msg.translationalVelocity = 0;
        msg.rotationalVelocity = 0;
        self.motionPub.publish(msg);
        return
    # param: angularVel: float angular velocity of robot in rad/s 
    # returns: float translational velocity for right wheel
    # converts angular velocity of robot to translational velocity of right wheel based on robot radius
    def convertAngVelToVel(self, angularVel):
        return angularVel*self.WHEELBASE;


    # param: angVel: angluar velocity of wheel in rad/s
    # returns: PWM (0-255) to achieve velocity
    # (the 'flipping' of one wheel's velocity is handled in the HAL)
    def convertAngVelToPWM(self, angVel):
        return angVel/self.MAX_WHEEL_ANG_VEL * self.MAX_PWM;


    # params: deltaDists: int[] [new distance on left wheel, new distance on right wheel] in m 
    #       deltaTime: float, time elapsed in which wheels have progressed by deltaDist
    # returns: void
    # calculates the current anglar velocity of each wheel and updates global variable currentWheelAngVel[]
    def updateCurrentVel(self, deltaDist, deltaTime):
        # calculating velocity of each wheel
        self.currentWheelVel[self.LEFT_WHEEL] = deltaDist[self.LEFT_WHEEL]/deltaTime
        self.currentWheelVel[self.RIGHT_WHEEL] = deltaDist[self.RIGHT_WHEEL]/deltaTime

        # calculating translational velocity by averaging
        self.currentTransVel = .5*(self.currentWheelVel[self.LEFT_WHEEL] + self.currentWheelVel[self.RIGHT_WHEEL]) # average 
        
        # calculating rotational velocity by taking the difference and dividing by wheel base
        # turning to the left is positive angle - when left wheel is moving backwards
        self.currentAngVel = .5*(self.currentWheelVel[self.RIGHT_WHEEL] - self.currentWheelVel[self.LEFT_WHEEL])/self.WHEELBASE


        return 


##########################
# Wheel position methods #
##########################
# These methods use the positional controller in the HAL code
    
    #params: Pose currentPose current pose of robot
    #        Location location destination
    # returns: none
    # travels to given point. 
    def travelTo(self, currentPose, destination):
        print 'inside travelTo'
        currentDistanceVector = (destination.getX() - currentPose.getX(), destination.getY() - currentPose.getY());
        angleToDestination = math.atan2((destination.getY()-currentPose.getY()),(destination.getX()-currentPose.getX())) - currentPose.getAngle();
        print 'angle to dest', angleToDestination
        
        # want angleToDestination [-pi, pi]
        if angleToDestination > math.pi:
            angleToDestination = angleToDestination - 2*math.pi

        elif angleToDestination < -math.pi:
            angleToDestination = angleToDestination + 2*math.pi

        distanceMagnitude = math.sqrt(currentDistanceVector[0]**2 + currentDistanceVector[1]**2);
        print 'angle', angleToDestination
        print 'distance', distanceMagnitude

        # turn to face destination point
        print 'calling rotateTo'
        self.rotateTo(angleToDestination)
        print 'wheelErr', self.wheelError
        # wait for wheel error message to get sent
        time.sleep(.01)
        while (self.wheelError > self.ANGULAR_ERR):
            # do nothing and wait for wheels to turn
            pass

        print 'done turning'
        time.sleep(5) # wait because there is overshoot
        # once done turning, translate to destination
        print 'start travel'
        self.translateTo(distanceMagnitude)
        self.translate(0.01)
        time.sleep(.01) # wait for wheel error message to get sent
        while(self.wheelError > self.TRANS_ERR):
            self.translate(0.01)
            time.sleep(.02)
            # do nothing and wait for wheels to turn
            pass
        print 'done translating'
        return

    # params: angle to turn to
    # returns: none
    # send message to wheel controller to turn
    def rotateTo(self, angle):
        print 'sending message'
        print 'angle', angle
        msg = MotionDistMsg()
        msg.translationalDist = 0
        msg.rotationalDist = angle
        print msg.rotationalDist
        self.positionPub.publish(msg)
        print 'message sent'
        return

    # params: distance to travel
    # returns: none
    # send message to wheel controller to translate distance
    def translateTo(self, distance):
        msg = MotionDistMsg()
        msg.translationalDist = distance
        msg.rotationalDist = 0
        self.positionPub.publish(msg)
        return


##########################
## Conveyor Belt Motion ##
##########################

    # params: none
    # returns: none
    # sends messages to start the conveyor belts that consume blocks at default speed
    def startEatingBelts(self):

        # tell right conveor motor to start at standard speed
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = .1
        msg.backConveyorFractionOn = 0.0
        self.conveyorPub.publish(msg)
        
        return

    # params: none
    # returns: none
    # sends messages to start both conveyor belts 
    def startBothBelts(self):

        # tell right conveor motor to start at standard speed
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = .1
        msg.backConveyorFractionOn = .1
        self.conveyorPub.publish(msg)
        
        return

    # params: none
    # returns: none
    # sends messages to start both conveyor belts 
    def startBothBelts(self):

        # tell right conveor motor to start at standard speed
        msg = ConveyorMsg()
        msg.frontTrackFractionOn = 1.0
        msg.backTrackFractionOn = 1.0
        self.conveyorPub.publish(msg)
        
        return

    # params: none
    # returns: none
    # sends messages to reverse conveyor belts that consumes blocks 
    def reverseEatingBelts(self):

        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = -.1
        msg.backConveyorFractionOn = 0.0
        self.conveyorPub.publish(msg)
        
        return


    # params: none
    # returns: none
    # sends messages to stop the conveyor belts that consume blocks
    def stopConveyorBelts(self):
        
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = 0.0
        msg.backConveyorFractionOn = 0.0
        self.conveyorPub.publish(msg)

        return


    # params: none
    # returns: none
    # sends messages to start the conveyor belt that moves blocks within the hamper
    def startHamperBelt(self):
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = 0.0
        msg.backConveyorFractionOn = .1
        self.conveyorPub.publish(msg)

        return

    # params: none
    # returns: none
    # sends messages to start the conveyor belt that moves blocks within the hamper
    def reverseHamperBelt(self):
        msg = ConveyorMsg()
        msg.frontConveyorFractionOn = 0.0
        msg.backConveyorFractionOn = -.1
        self.conveyorPub.publish(msg)

        return

#################
# Hamper motion #
#################
    # params: angle, in radians, that hamper should be set to
    # returns: none
    # sets hamper to desired angle. This is the angle to vertical; hamper is closed at 0 and open at pi/2
    def setHamperAngle(self, angle):
        fractionOpen = angle/(math.pi/2.0)
        
        msg = HamperMsg()
        msg.fractionOpen = fractionOpen
        self.hamperPub.publish(msg)

        pass


    
