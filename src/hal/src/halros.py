#!/usr/bin/env python
import rospy
import gc_msgs
from gc_msgs.msg import MotionMsg
from gc_msgs.msg import MotionDistMsg
from gc_msgs.msg import MotionVoltMsg
from gc_msgs.msg import ConveyorMsg
from gc_msgs.msg import HamperMsg
from gc_msgs.msg import EncoderMsg
from gc_msgs.msg import BumpMsg
from gc_msgs.msg import BumpStatusMsg
from gc_msgs.msg import StateMsg
from gc_msgs.msg import PoseMsg
from gc_msgs.msg import WheelErrorMsg
from hal import RobotHardware
from wheel_controller import WheelController
import time
import hal
import threading
class RobotHardwareROS(RobotHardware):
    def __init__(self):
        RobotHardware.__init__(self)
        #Subscribers
        self.motionSub = rospy.Subscriber("command/Motors",MotionMsg,self.handleMsg_MotionMsg)
        self.motionDistSub = rospy.Subscriber("command/MotorsDist", MotionDistMsg, self.handleMsg_MotionDistMsg)
        self.motionVoltSub = rospy.Subscriber("command/MotorVolt", MotionVoltMsg, self.handleMsg_MotionVoltMsg)
        self.conveyorSub = rospy.Subscriber("command/Conveyor",ConveyorMsg,self.handleMsg_ConveyorMsg)
        self.hamperSub = rospy.Subscriber("command/Hamper",HamperMsg,self.handleMsg_HamperMsg)
        self.stateSub = rospy.Subscriber("command/State", StateMsg, self.handleMsg_StateMsg)
        #Publishers
        self.encoderPub = rospy.Publisher("sensor/Encoder",EncoderMsg)
        self.bumpPub = rospy.Publisher("sensor/Bump",BumpMsg)
        self.sonarPub = rospy.Publisher("sensor/Sonar", PoseMsg)
        self.bumpStatusPub = rospy.Publisher("sensor/BumpStatus",BumpStatusMsg)
        self.wheelErrPub = rospy.Publisher("sensor/WheelErr", WheelErrorMsg)

        self.sonarId = ["front_left_sonar","front_right_sonar","back_left_sonar","back_right_sonar"]
        self.bumpId = ["block_front_touch","block_back_touch","front_left_touch","front_right_touch"]
        self.bumpState = [False,False,False,False]
        #Wheel Controller
        self.wController = WheelController()
	sensordict=self.read_wheels()
        self.wController.reset(sensordict['left_position'],sensordict['right_position'],time.time())
        #self.wController.velocity(0,0,time.time());
        self.lastVelocityMsgTime=None
        rospy.init_node("HalRos")
       
######################
# Subscriber Methods #
######################
    def handleMsg_StateMsg(self,msg):
        #if msg.source == "robotbrain":
            if msg.state == "init":                
                #self.wController = WheelController()
	        sensordict=self.read_wheels()
                self.wController.reset(sensordict['left_position'],sensordict['right_position'],time.time())

    def handleMsg_MotionVoltMsg(self,msg):
        self.command_actuators({"left_wheel":msg.leftVoltage,"right_wheel":msg.rightVoltage})
    def handleMsg_MotionDistMsg(self,msg):
        print 'recieved msg'
        td = msg.translationalDist
        rd = msg.rotationalDist
        print td, rd
        self.wController.position(td,rd)

    def handleMsg_MotionMsg(self,msg):

        tv = msg.translationalVelocity
        rv = msg.rotationalVelocity

        # Compute dt
        t=time.time()
        if self.lastVelocityMsgTime is None:
            self.lastVelocityMsgTime=t
        dt=t-self.lastVelocityMsgTime
        self.lastVelocityMsgTime=t
        self.wController.velocity(tv,rv,dt)


    def handleMsg_HamperMsg(self,msg):
       fo = msg.fractionOpen
       self.command_actuators({'hopper':fo})
    def handleMsg_ConveyorMsg(self,msg):
       frontfo = msg.frontConveyorFractionOn
       backfo = msg.backConveyorFractionOn
       self.command_actuators({'ramp_conveyer':frontfo,'back_conveyer':backfo})

################
# Step Methods #
################
    def step(self):
        sensordict = self.read_wheels()
        #print sensordict
        ##Encoder##
        msg = EncoderMsg()
        msg.lWheelDist = sensordict['left_position']
        msg.rWheelDist = sensordict['right_position']
        self.encoderPub.publish(msg)
        ##Ping Sensor#
        # TODO: get bump sensors, sonars working 
        

        # send out wheel error
        [leftWheelErr, rightWheelErr]=self.wController.get_error(sensordict['left_position'], sensordict['right_position'])
        msg = WheelErrorMsg()
        msg.leftWheelError = leftWheelErr
        msg.rightWheelError = rightWheelErr
        self.wheelErrPub.publish(msg)

        ##Control##
        t= time.time()

        motors = self.wController.step(sensordict['left_position'],sensordict['right_position'],t)
        self.command_actuators({'left_wheel':motors[0], 'right_wheel':motors[1]})

####################
# Sensor Retrieval #
####################
    
    def get_Encoder(self):
        sensordict = self.read_wheels()
        msg = EncoderMsg()
        msg.lWheelDist = sensordict['left_position']
        msg.rWheelDist = sensordict['right_position']
        self.encoderPub.publish(msg)

    def get_Sonar(self): ##Straight Sonar Assumption
        dx = 0.25 #m
        dy = 0.25 #m
        sensordict = self.read_sonars()
        ##FrontLeft
        read_dist = sensordict[self.sonarId[0]]
        if read_dist != None:
            msg = PoseMsg()
            msg.xPosition = -dx - read_dist
            msg.yPosition = dy
            self.sonarPub.publish(msg)
        ##FrontRight
        read_dist = sensordict[self.sonarId[1]]
        if read_dist != None:
            msg = PoseMsg()
            msg.xPosition = dx + read_dist
            msg.yPosition = dy
            self.sonarPub.publish(msg)
        ##BackLeft
        read_dist = sensordict[self.sonarId[2]]
        if read_dist != None:
            msg = PoseMsg()
            msg.xPosition = -dx - read_dist
            msg.yPosition = -dy
            self.sonarPub.publish(msg)
        ##BackRight
        read_dist = sensordict[self.sonarId[3]]
        if read_dist != None:
            msg = PoseMsg()
            msg.xPosition = dx + read_dist
            msg.yPosition = -dy
            self.sonarPub.publish(msg)
            
        #msg = SonarMsg?
    def get_Bump(self):
        sensordict = self.read_touches()
        for i in range(len(self.bumpId)):
            if self.bumpId[i] in sensordict:
                #if sensordict[self.bumpId[i]]:
                #    print "bump", i
                if (not self.bumpState[i]) and sensordict[self.bumpId[i]]:
                    #Bump is triggered
                    msg = BumpMsg()
                    msg.bumpNumber = i
                    self.bumpPub.publish(msg)
                self.bumpState[i] = sensordict[self.bumpId[i]]
        msg = BumpStatusMsg()
        msg.bumpStatus = self.bumpState
        self.bumpStatusPub.publish(msg)
########
# Main #
########

def sensorThread(rs,wtv):
    while True:
        rs.get_Sonar()
        rs.get_Bump()
        time.sleep(0.05)

if __name__=='__main__':
    
    rs = RobotHardwareROS();
    #rs.startTime = time.time()
    ##Spawn SensorThread
    t = threading.Thread(target=sensorThread, args = (rs,1))
    t.start()
    #while rs.startTime - time.time() < 600: #10:00 Min Operation Time
    #   time.sleep(.01)
    #   try:
    #      rs.step()
    #   except :
    #      x =1
    #self.command_actuators({'left_wheel':0.0, 'right_wheel':0.0})
    #rs.signal_shutdown("Game Time Over")
    rospy.spin()
