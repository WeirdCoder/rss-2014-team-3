#!/usr/bin/python

import orc
import math
import eci
from stm32f1xx import *
import time

# Orcboard Hookup

# Fast digital IO:
# 0 Left Ramp Conveyer
# 1 Right Ramp Conveyer
# 2 Back Conveyer
# 3 Left Hopper
# 4 Right Hopper

# Motors:
# 0 Left Wheel
# 1 Right Wheel

class RobotHardware:
    METERS_PER_TICK=0.0984*math.pi/(65.5*2000)
    SPEED_OF_SOUND_M_PER_US=0.00034029

    # Connect to orcboard
    def __init__(self):
        self.o=orc.OrcBoard()
        self.o.check_connectivity()
        self.e=eci.ECI()
        self.eci_init()

    def eci_init(self):
        self.e.configure_timer(TIM1_BASE,period=30000,prescaler=72) # SONAR
        self.e.configure_timer(TIM2_BASE,period=10000,prescaler=72,preload=TIM_PRELOAD_ENABLE) # PWM
        self.e.configure_timer(TIM3_BASE,period=30000,prescaler=72) # SONAR
        self.e.configure_timer(TIM4_BASE,period=10000,prescaler=72,preload=TIM_PRELOAD_ENABLE) # PWM
        self.e.set_in('A0','pupd',1)
        self.e.set_in('A1','pupd',1)
        

    def get_touch_sensor(self,pin):
        return not self.e.d_in(pin)

    def pulse_sonar(self,sonar):
        if sonar==1:
            self.e.configure_timer_slave(TIM1_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI1FP1)
            self.e.set_bits_register(TIM1_BASE+TIM_SR,0,TIM_SR_CC1IF)
            self.e.out('D0',1)
            self.e.pwm_in('D0')
            for i in range(10):
                if (self.e.read_register(TIM1_BASE+TIM_SR) & TIM_SR_CC1IF):
                    return self.e.pwm_in('D0')-750
                time.sleep(0.01)
            return None
        elif sonar==2:
            self.e.configure_timer_slave(TIM1_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI2FP2)
            self.e.set_bits_register(TIM1_BASE+TIM_SR,0,TIM_SR_CC2IF)
            self.e.out('D1',1)
            self.e.pwm_in('D1')
            for i in range(10):
                if (self.e.read_register(TIM1_BASE+TIM_SR) & TIM_SR_CC2IF):
                    return self.e.pwm_in('D1')-750
                time.sleep(0.01)
            return None
        elif sonar==3:
            self.e.configure_timer_slave(TIM3_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI1FP1)
            self.e.set_bits_register(TIM3_BASE+TIM_SR,0,TIM_SR_CC1IF)
            self.e.out('D4',1)
            self.e.pwm_in('D4')
            for i in range(10):
                if (self.e.read_register(TIM3_BASE+TIM_SR) & TIM_SR_CC1IF):
                    return self.e.pwm_in('D4')-750
                time.sleep(0.01)
            return None
        elif sonar==4:
            self.e.configure_timer_slave(TIM3_BASE,mode=TIM_MASTERSLAVEMODE_ENABLE,trigger=TIM_TS_TI2FP2)
            self.e.set_bits_register(TIM3_BASE+TIM_SR,0,TIM_SR_CC2IF)
            self.e.out('D5',1)
            self.e.pwm_in('D5')
            for i in range(10):
                if (self.e.read_register(TIM3_BASE+TIM_SR) & TIM_SR_CC2IF):
                    return self.e.pwm_in('D5')-750
                time.sleep(0.01)
            return None
        else:
            raise Exception('Bad sonar')


    # Private Functions
    # Consider using set_actuators instead of these

    # Speed from 1 (forward) to -1 (backwards)
    def run_ramp_conveyer(self,speed):
        self.e.pwm_out('D6',int(1500+speed*500))
        self.e.pwm_out('D7',int(1500-speed*500))

    # Speed from 1 (towards edge) to -1 (towards center)
    def run_back_conveyer(self,speed):
        self.e.pwm_out('D3',int(1500+speed*500))

    # Angle from 0 (fully closed) to 1 (fully open)
    def set_hopper(self,angle):
        self.e.pwm_out('D8',int(1600-angle*950))
        self.e.pwm_out('D9',int(950+angle*1000))

    # Run left motor (1 forward -1 backwards)
    def drive_left_wheel(self,speed):
        self.o.set_motor(0,int(255*speed))

    # Run right motor (1 forward -1 backwards)
    def drive_right_wheel(self,speed):
        self.o.set_motor(1,int(-255*speed))

    # Returns right robot position in meters based on right wheel encoder
    def left_position(self,status):
        return status['encoders'][0]['position']*self.METERS_PER_TICK

    # Returns left robot position in meters based on left wheel encoder
    def right_position(self,status):
        return -status['encoders'][1]['position']*self.METERS_PER_TICK

    def get_sonar(self,sonar):
        reading=self.pulse_sonar(sonar)
        if reading is None or reading > 17900:
            return None

        return reading*self.SPEED_OF_SOUND_M_PER_US

    # Public Functions

    # Command all actuators (see above functions for units / bounds)
    # Input format: dictionary where key = actuator name,
    # value = actuator command (see above private functions for units)
    def command_actuators(self,commands):
        command_functions={
            'ramp_conveyer':self.run_ramp_conveyer,
            'back_conveyer':self.run_back_conveyer,
            'hopper':self.set_hopper,
            'left_wheel':self.drive_left_wheel,
            'right_wheel':self.drive_right_wheel,
        }

        for (k,v) in commands.iteritems():
            command_functions[k](v)

    # Returns a reading from all the sensors (see above functions for units)
    # Format: dictionary where key = sensor name, value = sensor reading
    # TODO implement this
    def read_sensors(self):
        status=self.o.get_status()
        sonars=[self.get_sonar(i) for i in [1,2,3,4]]
        touches=[self.get_touch_sensor(t) for t in ['A0','A1']]
        return {
            'left_position':self.left_position(status),
            'right_position':self.right_position(status),
            'front_left_sonar':sonars[0],
            'back_left_sonar':sonars[1],
            'front_right_sonar':sonars[2],
            'back_right_sonar':sonars[3],
            'front_left_touch':touches[0],
            'front_right_touch':touches[1],
        }

if __name__=='__main__':
    r=RobotHardware()
    r.command_actuators({'ramp_conveyer':0,'back_conveyer':0,'hopper':0,'left_wheel':0,'right_wheel':0})
    #while True:
    #    time.sleep(1)

    while True:
        print r.read_sensors()
