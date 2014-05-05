#!/usr/bin/python

from hal import RobotHardware
import time

r=RobotHardware()
state='init'

def bump():
	ts=r.read_touches()
	return (ts['front_left_touch'],ts['front_right_touch'])

try:
	while True:
		print state
		time.sleep(0.01)
		if state=='init':
			#r.command_actuators({'ramp_conveyer':1,'back_conveyer':1,'hopper':0,'left_wheel':0,'right_wheel':0})
			state='wander'
			bumps=0
		elif state=='wander':
			r.command_actuators({'left_wheel':.4,'right_wheel':.4})
			b=bump()
			if b==(1,0):
				state='hit_left'
			elif b==(0,1):
				state='hit_right'
			elif b==(1,1):
				state='hit_both'
			elif bumps>10:
				spintime=time.time()+5
				state='spin'
		elif state=='hit_left':
			r.command_actuators({'left_wheel':-.3,'right_wheel':-.4})
			b=bump()
			if b==(0,0):
				bumps+=1
				state='wander'
			elif b==(0,1):
				state='hit_right'
			elif b==(1,1):
				state='hit_both'
		elif state=='hit_right':
			r.command_actuators({'left_wheel':-.4,'right_wheel':-.3})
			b=bump()
			if b==(0,0):
				bumps+=1
				state='wander'
			elif b==(1,0):
				state='hit_right'
			elif b==(1,1):
				state='hit_both'
		elif state=='hit_both':
			r.command_actuators({'left_wheel':-.3,'right_wheel':-.3})
			b=bump()
			if b==(1,0):
				state='hit_left'
			elif b==(0,1):
				state='hit_right'
			elif b==(0,0):
				bumps+=1
				state='wander'
		elif state=='spin':
			r.command_actuators({'left_wheel':.3,'right_wheel':-.3})
			b=bump()
			bumps=0
			if b==(1,0):
				state='hit_left'
			elif b==(0,1):
				state='hit_right'
			elif b==(1,1):
				state='hit_both'
			elif time.time()>spintime:
				state='wander'

except KeyboardInterrupt:
	print "Got keyboard interrupt"
r.command_actuators({'ramp_conveyer':0,'back_conveyer':0,'hopper':0,'left_wheel':0,'right_wheel':0})
