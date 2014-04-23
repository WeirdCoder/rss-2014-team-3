#!/usr/bin/python

# Control loop for the robot wheels
class WheelController:
	# Distance between wheels in meters
	WHEEL_BASE=.372

	# Proportional gain for feedback loop
	P_GAIN=10

	# Integral gain for feedback loop
	I_GAIN=5

	# Derivative gain for the feedback loop
	D_GAIN=10

	# Bounds the motor values returned by step()
	MAX_COMMAND=1

	# Bounds the error integrator
	MAX_INTEGRAL=10

	# Bounds the acceleration
	MAX_ACCEL=4

	def __init__(self):
		pass

	# Sets current values as "zero" and resets state of the control loop
	# Input: Left and right wheel position values in meters and current time
	def reset(self,left_position,right_position,t):
		self.desired_left=left_position
		self.desired_right=right_position
		self.last_time=t

		self.left_error_integral=0
		self.right_error_integral=0

		self.last_left_error=0
		self.last_right_error=0

		self.last_left=0
		self.last_right=0

	# Input: desired rotational velocity (radians per second) and translational velocity (meters per second) for the next step
	# as well time of the next step
	def velocity(self,translational_velocity,rotational_velocity,t):
		(left,right)=self.polar_to_tank(translational_velocity,rotational_velocity)
		self.desired_left+=left*(t-self.last_time)
		self.desired_right+=right*(t-self.last_time)

	# Input: desired rotational displacement (radians) and translational displacement (meters)
	def position(self,translational_position,rotational_position):
		(left,right)=self.polar_to_tank(translational_position,rotational_position)
		self.desired_left+=left
		self.desired_right+=right

	# Input: left and right encoder values
	# Output: left and right motor speeds as a fraction of full power
	def step(self,left_position,right_position,t):
		(left_error,right_error)=self.get_error(left_position,right_position)

		self.left_error_integral+=left_error*(t-self.last_time)
		self.right_error_integral+=right_error*(t-self.last_time)

		left_error_derivative=(left_error-self.last_left_error)/(t-self.last_time)
		right_error_derivative=(right_error-self.last_right_error)/(t-self.last_time)

		self.left_error_integral=max(min(self.left_error_integral,self.MAX_INTEGRAL),-self.MAX_INTEGRAL)
		self.right_error_integral=max(min(self.right_error_integral,self.MAX_INTEGRAL),-self.MAX_INTEGRAL)

		left_command=-left_error*self.P_GAIN-self.left_error_integral*self.I_GAIN-left_error_derivative*self.D_GAIN
		right_command=-right_error*self.P_GAIN-self.right_error_integral*self.I_GAIN-right_error_derivative*self.D_GAIN

		left_command=max(min(left_command,self.MAX_COMMAND),-self.MAX_COMMAND)
		right_command=max(min(right_command,self.MAX_COMMAND),-self.MAX_COMMAND)

		max_step=self.MAX_ACCEL*(t-self.last_time)
		left_command=max(min(left_command,self.last_left+max_step),self.last_left-max_step)
		right_command=max(min(right_command,self.last_right+max_step),self.last_right-max_step)

		self.last_left=left_command
		self.last_right=right_command
		self.last_left_error=left_error
		self.last_right_error=right_error
		self.last_time=t

		return (left_command,right_command)

	# Gets the error for each wheel
	# Input: left and right encoder values
	# Output: left and right wheel position errors (in meters)
	def get_error(self,left_position,right_position):
		return (left_position-self.desired_left,right_position-self.desired_right)

	# Converts polar coordinates to tank-drive coordinates for the given the wheel base
	# Input: polar coordinates (translation and rotation)
	# Output: tank-drive coordinates (left and right wheel)
	def polar_to_tank(self,translation,rotation):
		left=translation-rotation*self.WHEEL_BASE/4
		right=translation+rotation*self.WHEEL_BASE/4
		return (left,right)

if __name__=='__main__':
	import time
	import hal

	r=hal.RobotHardware()

	vc=WheelController()
	sensors=r.read_sensors()
	vc.reset(sensors['left_position'],sensors['right_position'],time.time())
	#vc.reset(0,0,time.time())
	vc.position(.3,0) # testing step response

	while True:
		t=time.time()
		#vc.velocity(.1,0,t)
		sensors=r.read_sensors()
		motors=vc.step(sensors['left_position'],sensors['right_position'],time.time())
		#motors=vc.step(0,0,t)
		#print motors
		r.command_actuators({'left_wheel':motors[0],'right_wheel':motors[1]})
		#print vc.get_error(sensors['left_position'],sensors['right_position'])
		time.sleep(0.01)

