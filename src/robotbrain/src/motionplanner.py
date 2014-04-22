import rospy
import math
import time
#
# This class is used by the RobotBrain to send PWM messages to the HAL to control the motion of the motors. This includes both wheel motors and theconveyor belt motors. Also has methods to abstract some elements of motor control; has proportional control for the wheels
#

class MotionPlanner(object):
    def __init__(self):

        # kept updated to reflect robot status
        self.currentWheelAngVel = [0.0,0.0];   # float[],set by listening to encoder messages
        self.currentTicks = [0,0];          # int ticks in encoders
        self.desiredAngleDiff = 0;          # desired difference in encoder position between the two wheels
        
        # constants for wheel motion
        # TODO: pick appropriate vales
        self.MAX_WHEEL_TRANS_ACCEL = .01;    # maximum translation acceleration in m/s^2  
        self.MAX_WHEEL_ROT_ACCEL = .01;      # maximum rotational  acceleration in rad/s^2  
        self.ANGULAR_ERR = .1;              # acceptable angular error in radians
        self.BALANCED_GAIN = .5;
        self.MAX_WHEEL_ANG_VEL = 9.95;      # maximum angular velocity of wheels in rad/s
        self.WHEEL_RADIUS = 0.0625;         # wheel radius in m
        self.WHEELBASE =  .428;             # distance from origin to wheel; similar to a robot radius
        self.MAX_PWM = 255;
        self.ENCODER_RESOLUTION = 2000;    # ticks/revolution, without gear ratio
        self.GEAR_RATIO = 65.5; 
        self.TICKS_PER_REVOLUTION = self.ENCODER_RESOLUTION*self.GEAR_RATIO;
        self.LEFT_WHEEL = 0;                # for indexing into leftWheel, rightWheel tuples
        self.RIGHT_WHEEL = 1;
        self.lastEncoderMsgTime = time.clock() # time in seconds. Used for calculating current wheel velocities

        # constants for conveyor motion
        # TODO: pick appropirate values
        self.CONVEYOR_SPEED = .1;     # speed of conveyor belts in rad/s
        self.MOTOR_INT = {'lWheel': 0, 'rWheel': 1, 'lRampConveyor': 2, 'rRampConveyor':3, 'holdConveyor': 4};

        
        # intialize publishers, subscribers
        pwmPub = rospy.Publisher("MotorCommand", MotorCommand);
        encoderSub = rospy.Subscriber('encoderData', EncoderMsg, handleEncoderMsg);
        motionPub = rospy.Publisher("command/Motors", MotionMsg);

        return;

#################################
# Publishers/Subscriber Methods #
#################################

# params: EncoderMsg msg
# returns: none
# calculates current rWheelVel and lWheelVel from encoder message
# assumes encoder message fixes signs so ticks are positive when both wheels are moving forward
def handleEncoderMsg(msg):
    # calculating how much the wheels have moved in the past time step                                
    newTicks = [msg.lWheelTicks, msg.rWheelTicks]; # current tick positions of the wheels             
    deltaTicks = [newTicks[LEFT] - currentTicks[LEFT], newTicks[RIGHT]-currentTicks[RIGHT]];
    
    # calculating how much time has passed
    currentTime = time.clock();
    deltaTime = currentTime - lastEncoderMsgTime;
    lastEncoderMsgTime = currentTime;

    # calculate and update the currentWheelAngVel parameter
    updateWheelAngVel(deltaTicks, deltaTime);
    return


    # params: rWheelPWM: int (0-255) pwm value for right wheel
    #         lWheemPWM: int (0-255) pwm value for left wheel
    # returns: void
    # send messages for command desired wheel PWMs
    def setWheelPWM(self, lWheelPWM, rWheelPWM):
        msg = MotorCommand()

        # sending message for left wheel
        msg.PWM = lWheelPWM;
        msg.motorType = self.MOTOR_INT['lWheel'];
        pwmPub.publish(msg);

        # sending message for right wheel
        msg.PWM = rWheelPWM;
        msg.motorType = self.MOTOR_INT['rWheel'];
        pwmPub.publish(msg);

        return
  

#################
# Wheel motion ##
################# 


    # params: currentPose: pose containing current location of robot
    #         destinationPose: pose containing desination of robot (angle is 0)
    #         angVel: float angular velocity in rad/s
    #         vel; float velocity in m/s
    # returns: void
    # using rotateTowards and translateTowards, first rotates to face destination and then translates to it
    def travelTowards(self, currentPose, destinationLoc, angVel, vel, startPose):
        
        angleToDestination = math.artan((destinationLoc.getY()-currentPose.getY())/(destinationLoc.getX()-currentPose.getX()));
        
        # if not currently facing the destination, rotate towards it so can translate there in a straight line
        if (math.abs(angleToDestination - currentPose.getAngle()) > self.ANG_ERROR):
            self.rotateTowards(currentPose.getAngle(), angleToDestination, angVel, startPose.getAngle());

        # if the robot is facing the destination, move towards it
        else:
            self.translateTowards(currentPose, destinationLoc, vel, startPose); 

        return;

    # params: currentAngle: float currentAngle in radians
    #         destAngle: float destination angle in radians 
    #         angSpeed: float angular speed in rad/s 
    #         startAngle: float angle of robot when motion was started in rad/s
    # returns: void
    # Calculates appropriate rotational speed using proportional control (accelerates and deacellerates
    #   based on distance to currentLoc. Calls rotate.
    def rotateTowards(self, currentAngle, destAngle, angSpeed, startAngle):
        distance = destAngle - currentAngle; 
        startDistance = destAngle-startAngle;
        fractionLeft = distance/startDistance;

        # if have travelled more than halfway and distance < .5*acceleration^2, in slow-down region
        rotationalVelocity = (self.currentWheelAngVel[RIGHT] - self.currentWheelAngVel[LEFT])/2.0; # rotating left is positive
                                                                                              # robot turns left when left wheel moves back

        if (distance < .5*self.MAX_WHEEL_ROT_ACCEL**2) and (abs(fractionLeft) < .5):
            desiredAngVel = distance/self.MAX_WHEEL_ROT_ACCEL;

        # if not in slow-down region and haven't reached speed yet, accelerate    
        elif (abs(rotationalVelocity) < angSpeed):
            desiredAngVel = rotationalVelocity + math.copysign(self.MAX_WHEEL_ROT_ACCEL, distance);

        # if not slowing down or speeding up, then at the desired speed, and contiue at it    
        else:
            desiredAngVel = math.copysign(angSpeed, distance);

        self.rotate(desiredAngVel);

        return
        
    # params: currentPose: Pose currentLocation
    #         destination: Pose destination
    #         speed: fload desired speed of motion in m/s 
    #         startPose: Pose robot held when motion was started
    # returns: void
    # Calculates appropriate translational speed using proportional control (accelerates and deacellerates
    #   based on distance to currentLoc. Calls translate
    # assumes that robot is facing desination 
    def translateTowards(self, currentPose, destination, speed, startPose):
        # calculating the magnitude and sign of the distance from currentLocation to desination
        currentDistanceVector = (destination.getX() - currentPose.getX(), destination.getY() - currentPose.getY());
        startDistance = math.sqrt((destination.getX() - startPose.getX())**2 +( destination.getY() - startPose.getY())**2);
        distanceMagnitude = math.sqrt(currentDistanceVector[0]**2 + currentDistanceVector[1]**2); 
        currentAngleVector = (math.cos(currentPose.getAngle()), math.sin(currentPose.getAngle()));
                                    
        # if currentDistanceVector dot currentAngleVector is positive, then need to move forward
        # otherwise, need to move backwards, so distance will be negative
        dotProduct = currentDistanceVector[0]*currentAngleVector[0] + currentDistanceVector[1]*currentAngleVector[1];        
        velocitySign = math.copysign(1, dotProduct);

        # calculating the magnitude of the velocity
        # if within slow-down region (less than .5*a^2 from destination and at least halfway there), use velocity proportional to distance 
        currentTransVelocity = .5*self.WHEEL_RADIUS*(self.currentWheelAngVel[LEFT] + self.currentWheelAngVel[RIGHT]);

        if (distanceMagnitude < .5*self.MAX_WHEEL_TRANS_ACCEL**2) and (distanceMagnitude/startDistance < .5):
            desiredVelocity = velocitySign*distanceMagnitude/self.MAX_WHEEL_TRANS_ACCEL;
        
        # otherwise, if less than the desired velocity, accelerate
        elif abs(currentTransVelocity) < speed:
            desiredVelocity = currentTransVelocity + velocitySign*self.MAX_WHEEL_TRANS_ACCEL;
        
        # otherwise, want to keep traveling at the desired velocity
        else:
            desiredVelocity = velocitySign*speed;
            
        self.translate(desiredVelocity);
        
        return
    # params: angVelocity: float angular velocity in rad/s
    # returns: void
    # sends MotionControl message to rotate
    def rotate(self, angVelocity):
        msg = MotionMsg();
        msg.translationalVelocity = 0.0;
        msg.rotationalVelocity = angVelocity;
        motionPub.publish(msg);
        return

    # params: velocity: float translational velocity in m/s
    # returns: void
    # sends MotionControl message to translate
    def translate(self, velocity):
        # send MotionControl Message
        msg = MotionMsg();
        msg.translationalVelocity = velocity;
        msg.rotationalVelocity = 0;
        motionPub.publish(msg);
        return

    # params: none
    # returns: none
    # sends MotionMsg stopping both wheels
    def stopWheels(self):
        msg = MotionMsg();
        msg.translationalVelocity = 0;
        msg.rotationalVelocity = 0;
        motionPub.publish(msg);
        return
    # param: angularVel: float angular velocity of robot in rad/s 
    # returns: float translational velocity for right wheel
    # converts angular velocity of robot to translational velocity of right wheel based on robot radius
    def convertAngVelToVel(self, angularVel):
        return angularVel*self.WHEELBASE;

    # param: velocity: float velocity of wheel in m/s
    # returns: float angular velocity
    # converts wheel velocity to angular velocity of wheel based on wheel radius
    def convertVelToAngVel(self, velocity):
        return velocity/self.WHEEL_RADIUS;

    # param: angVel: angluar velocity of wheel in rad/s
    # returns: PWM (0-255) to achieve velocity
    # (the 'flipping' of one wheel's velocity is handled in the HAL)
    def convertAngVelToPWM(self, angVel):
        return angVel/self.MAX_WHEEL_ANG_VEL * self.MAX_PWM;


    # params: deltaTicks: int[] [new ticks on left wheel, new ticks on right wheel] 
    #       deltaTime: float, time elapsed in which wheels have progressed by deltaTicks
    # returns: void
    # calculates the current anglar velocity of each wheel and updates global variable currentWheelAngVel[]
    def updateWheelAngVel(deltaTicks, deltaTime):
        # calculating change in distance
        leftWheelAngle = float(deltaTicks[LEFT])*(2*math.pi)/self.TICKS_PER_REVOLUTION;
        rightWheelAngle = float(deltaTicks[RIGHT])*(2*math.pi)/self.TICKS_PER_REVOLUTION;
        
        # calculating and updating change in velocity
        leftWheelAngVel = leftWheelAngle/deltaTime;
        rightWheelAngVel = rightWheelAngle/deltaTime;
        currentWheelAngVel[LEFT] = leftWheelAngVel;
        currentWheelAngVel[RIGHT] = rightWheelAngVel;
        
        return 

##########################
## Conveyor Belt Motion ##
##########################

    # params: none
    # returns: none
    # sends messages to start the conveyor belts that consume blocks at default speed
    def startEatingBelts(self):

        # calculate desired pwm
        pwmValue =  self.convertVelToPWM(self.CONVEYOR_SPEED);

        # tell right conveor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('rRampConveyor'));
        pwmPub.publish(msg)

        # tell right conveor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('lRampConveyor'));
        pwmPub.publish(msg)
        
        return

    # params: none
    # returns: none
    # sends messages to start the conveyor belts that consume blocks at default speed
    def reverseEatingBelts(self):

        # calculate desired pwm
        pwmValue =  self.convertVelToPWM(-1*self.CONVEYOR_SPEED);

        # tell right conveor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('rRampConveyor'));
        pwmPub.publish(msg)

        # tell right conveor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('lRampConveyor'));
        pwmPub.publish(msg)
        
        return


    # params: none
    # returns: none
    # sends messages to stop the conveyor belts that consume blocks
    def stopEatingBelts(self):
        
        # calculate desired PWM
        pwmValue = self.convertVelToPWM(0);

        # tell right conveor motor to stop at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('rRampConveyor'));
        pwmPub.publish(msg)

        # tell right conveor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('lRampConveyor'));
        pwmPub.publish(msg)
        
        return


    # params: none
    # returns: none
    # sends messages to start the conveyor belt that moves blocks within the hamper
    def startHamperBelt(self):
        
        # calculate desired pwm
        pwmValue =  self.convertVelToPWM(self.CONVEYOR_SPEED);

        # tell hold conveyor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('holdConveyor'));
        pwmPub.publish(msg)

        return

    # params: none
    # returns: none
    # sends messages to start the conveyor belt that moves blocks within the hamper
    def reverseHamperBelt(self):
        
        # calculate desired pwm
        pwmValue =  self.convertVelToPWM(-1*self.CONVEYOR_SPEED);

        # tell hold conveyor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('holdConveyor'));
        pwmPub.publish(msg)

        return

    # params: none
    # returns: none
    # sends messages to stop the conveyor belt that moves blocks within the hamper
    def stopHamperBelt(self):
                # calculate desired pwm
        pwmValue =  self.convertVelToPWM(0);

        # tell hold conveyor motor to start at standard speed
        msg = MotorCommand(pwmValue, self.MOTOR_INT('holdConveyor'));
        pwmPub.publish(msg)
        
        return

#################
# Hamper motion #
#################
    # params: angle, in radians, that hamper should be set to
    # returns: none
    # sets hamper to desired angle. This is the angle to vertical; hamper is closed at 0 and open at 90
    def setHamperAngle(self, angle):
        #TODO
        pass


    
