#!/usr/bin/env python
import rospy
import gc_msgs
from gc_msgs.msg import MotionMsg
from gc_msgs.msg import ConveyorMsg
from gc_msgs.msg import HamperMsg
from gc_msgs.msg import EncoderMsg
from hal import RobotHardware
from wheel_controller import WheelController
import time
import hal
class RobotHardwareROS(RobotHardware):
    def __init__(self):
        RobotHardware.__init__(self)
        #Subscribers
        self.motionSub = rospy.Subscriber("command/Motors",MotionMsg,self.handleMsg_MotionMsg)
        self.conveyorSub = rospy.Subscriber("command/Conveyor",ConveyorMsg,self.handleMsg_ConveyorMsg)
        self.hamperSub = rospy.Subscriber("command/Hamper",HamperMsg,self.handleMsg_HamperMsg)
        #Publishers
        self.encoderPub = rospy.Publisher("sensor/Encoder",EncoderMsg)
        #Wheel Controller
        self.wController = WheelController()
        self.wController.reset(0,0,time.time());
        #self.wController.velocity(0,0,time.time());
        self.lastVelocityMsgTime=None
        rospy.init_node("HalRos")
       
######################
# Subscriber Methods #
######################
    def handleMsg_MotionMsg(self,msg):
        tv = msg.translationalVelocity;
        rv = msg.rotationalVelocity;

        # Compute dt
        t=time.time()
        if self.lastVelocityMsgTime is None:
            self.lastVelocityMsgTime=t
        dt=t-self.lastVelocityMsgTime
        self.lastVelocityMsgTime=t

        self.wController.velocity(tv,rv,dt);

    def handleMsg_HamperMsg(self,msg):
       fo = msg.fractionOpen;
       self.command_actuators({'hopper':fo})
    def handleMsg_ConveyorMsg(self,msg):
       frontfo = msg.frontConveyorFractionOn;
       backfo = msg.backConveyorFractionOn;
       self.command_actuators({'ramp_conveyer':frontfo,'back_conveyer':backfo})

################
# Step Methods #
################
    def step(self):
        sensordict = self.read_sensors();
        print sensordict
        ##Encoder##
        msg = EncoderMsg()
        msg.lWheelTicks = sensordict['left_position']
        msg.rWheelTicks = sensordict['right_position']
        self.encoderPub.publish(msg);
        ##Ping Sensor#
        # TODO: get bump sensors, sonars working 
        

        ##Control##
        t= time.time()
        self.wController.step(sensordict['left_position'],sensordict['right_position'],t)
    
if __name__=='__main__':
    rs = RobotHardwareROS();
    while True:
       time.sleep(1)
       rs.step()
