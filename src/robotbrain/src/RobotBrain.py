#!/usr/bin/env python
import rospy
import * from MotionPlanner
from gc_msgs import MotionMsg  # for sending commands to motors
from gc_msgs import BumpMsg    # for listening to bump sensors
from gc_msgs import PoseMsg    # for listening to when the kinect sees a block
from MotionPlanner import MotionPlanner
from PathPlanner import PathPlanner 
#
# This node is the state machine that controls robot behavior
#

#####################
## state methods ####
#####################

# params: none
# returns: void
# starts all publishers, subscribers; loads map and fills associated variables 
def init():
    #TODO
    motionPub = rospy.Publisher("command/Motors", MotionMsg);
    bumpSub = rospy.Subscriber('bumpData', BumpMsg, handleBumpMsg);
    pass

# params: none
# returns: none
# Robot needs more blocks, but does not know where to find them.
# Travel to unexplored parts of the map to look for new blocks
def wander():
    # TODO
    pass

# params: none
# returns: none
# Robot needs to move from it's current location to a destination
# global waypoints contains a list of points the robot can travel to in straight-line paths
def travel():
    # TODO
    pass

# params: none
# returns: none
# Robot has reached a place where it believes a block is
# turn in slow circle looking for block. If time-out, reject block location and set course for a new one
def search():
    pass

# params: none
# returns: none
# Robot has a block currently in its vision. 
# Move until block hits the bump sensors at the mouth of the conveyor belt
def consume():
    # TODO
    pass

# params: none
# returns: none
# Robot has a block on the conveyor belt
# Move conveyor belts so block is added to wall
def digest():
    # TODO
    # start ramp motors
    motorController.startEatingBelts();
    # wait for flag from hamper bump sensors
    # if haven't gotten flag, reverse motors and call again; return
    #
    pass

# params: none
# returns: void
# Robot has finished gathering blocks; commands robot to travel to endLocation and open hatch
def dispense():
    # TODO
    pass


###############################
# Interrupt-handling methods ##
###############################
# TODO
# params: none
# returns: none
# based on which bump sensor went off, call appropriate respose
def handleBumpMsg(msg):
    # bump sensors 0 and 1 are at the mouth
    if (msg.bumpNumber <= 1):
        #if are currently searching and something hits these bump sensors, are eating the block
        if robotState = 'consuming': 
            searchCount = 0;
            robotState = 'digesting';
    # bump sensor 2 is at the end of the conveyor belt
    elif (msg.bumpNumber == 2):
        if (robotState = 'digesting') and (

    else
        msg = MotionMsg(); # defaults to 0
        motionPub.publish(msg);
        # not sure where other bump sensors on chassis will be; 
        # should stop and back away if they are hit
    
    return



############
## Main ####
############


if __name__ == '__main__':
    # defining variables related to robot's state
    robotState = 'wandering'               # the state of the robot; can be wandering, traveling, searching, consuming
                                           #    digesting, dispensing 
    searchCount = 0                        # counter used to ensure that don't spend too long searching
    MAX_SEARCh_COUNT = 100                 # arbitrary value; should be tested and set
    
    NUM_BLOCKS_NEEDED = 9                  # number of blocks needed to complete wall
    numBlocksCollected = 0                 # number of blocks that the robot has collected so far

    blockLocations = []                    # list of locations of blocks
    waypointList = []                      # list of waypoints to current destination
    
    # objects
    motorController = MotionPlanner();
    pathPlanner = PathPlanner();
    # constants

    try:
        init()                             # load map, initialize publishers, subscribers

        # while the robot needs more blocks to complete the wall, gather blocks
        while(numBlocksCollected < NUM_BLOCKS_NEEDED):
            if (robotState == 'wandering'):
                wander()

            else if (robotState == 'traveling'):
                travel()

            else if (robotState == 'searching'):
                search()

            else if (robotState == 'consuming'):
                consume()

            else if (robotState == 'digesting'):
                digest()

        # once the robot has enough blocks to complete the wall, leave the wall    
        dispense()
        rospy.spin()          # keeps python from exiting until node is stopped
    except rospy.ROSInterruptException: pass
