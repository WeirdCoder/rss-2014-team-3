#!/usr/bin/python

import orc

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
    # Connect to orcboard
    def __init__(self):
        self.o=orc.OrcBoard()
        self.o.check_connectivity()

    # Private Functions
    # Consider using set_actuators instead of these

    # Speed from 1 (forward) to -1 (backwards)
    def run_ramp_conveyer(self,speed):
        self.o.set_pwm(0,int(1500+speed*500))
        self.o.set_pwm(1,int(1500-speed*500))

    # Speed from 1 (towards edge) to -1 (towards center)
    def run_back_conveyer(self,speed):
        self.o.set_pwm(2,int(1500+speed*500))

    # Angle from 0 (fully closed) to 1 (fully open)
    def set_hopper(self,angle):
        self.o.set_pwm(3,int(1800-angle*1100))
        self.o.set_pwm(4,int(1100+angle*1000))

    # Run left motor (1 forward -1 backwards)
    def drive_left_wheel(self,speed):
        self.o.set_motor(0,int(255*speed))

    # Run right motor (1 forward -1 backwards)
    def drive_right_wheel(self,speed):
        self.o.set_motor(1,int(-255*speed))

    # Public Functions

    # Command all actuators (see above functions for bounds)
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

    # Returns a reading from all the sensors
    # Format: dictionary where key = sensor name, value = sensor reading
    # TODO implement this
    def read_sensors(self):
        return {}

if __name__=='__main__':
    r=RobotHardware()
    r.command_actuators({'ramp_conveyer':0,'back_conveyer':0,'hopper':0,'left_wheel':0,'right_wheel':0})