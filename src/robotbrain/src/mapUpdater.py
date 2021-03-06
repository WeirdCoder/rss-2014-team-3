#!/usr/bin/env python                                                                                   
import rospy
import math
from location import Location
from gc_msgs import ObstacleMsg
from gc_msgs import PoseMsg
#
# This class takes in messages from the sonar (and maybe kinect?) containing the locations of 'hits', It converts those points 
# into obstacles, and sends those obstacles out in ObstacleMsgs to the robot brain to update the map. 
#

class mapUpdater(object):

    # params: Obstacle[] mapList: world map
    # returns: none
    # initialises publishers, etc
    def __init__(self, mapList):
        # creating subscribers and publishers
        self.obstaclePub = rospy.Publisher("mapUpdates", ObstacleMsg);
        self.sonarSub = rospy.Subscriber("sensors/Sonar", PoseMsg, self.handleSonarMsg)
        self.currPoseSub = rospy.Subscriber("sensor/CurrentPose", PoseMsg, self.handleCurrPoseMsg)

        # keep track of state
        self.currentPose = PoseMsg()
        self.mapList = mapList
        self.pointList = []

        # defining constants
        # TODO: get real values for these
        self.THRESHOLD_DIST_BETWEEN_POINTS = .05 # in meters; if two consecutive points are more than this distance apart
                                             # they will be considered part of sepearate obstacles

        self.LINE_DEVIATION_THRESHOLD = .03  # in meters; if a point is this far from a line, it will not be considered to be on that line
        self.SONAR_UNCERTAINTY = .01         # in meters; the error of the sonar reading

        return 

############
# Handlers #
############

    # params: msg: CurrentPose
    # returns: none
    # update the currentpose of the robot from odometry
    def handleCurrPoseMsg(self, msg): 
        self.currentPose = msg

    # params: msg: SonarMsg
    # returns: none
    # adds point from sonar message to point list
    # on certain conditions, turn points in list into an obstacle and send out obstacle message
    def handleSonarMsg(self, msg):
        #Coordinate shift from robot frame to world frame
        pointX = self.currentPose.xPosition + math.sin(self.currentPose.angle)*msg.yPosition - math.cos(self.currentPose.angle)*msg.xPosition
        pointY = self.currentPose.yPosition + math.cos(self.currentPose.angle)*msg.xPosition + math.sin(self.currentPose.angle)*msg.yPosition
        newPoint = Location(pointX, pointY)

        # check if the point is part of a new obstacle
        if (self.checkIfNewObstacle(newPoint)):
            # check if the point is part of the obstacle being formed in pointList
            if (self.checkSameObstacle(newPoint,pointList)):
                pointList.append(newPoint)
        
            # if new point is part of different obstacle, turn points in pointList into obstacle
            # and start new obstacle
            else:
                self.makeObstacle() # creates and sends obstacle
                self.pointList = [newPoint]
        return


    # params: Obstacle obstacle
    # returns: none
    # puts obstacle into message and sends
    def sendObstacleMsg(self, obstacle):
        # from obstacle, create list of x,y points
        xList = []
        yLst = []
        pointList = obstacle.getLocationList()
        for point in pointList:
            xList.append(point.getX())
            yList.append(point.getY())
            
        # create and publish message
        msg = ObstacleMsg()
        msg.xPosList = xList
        msg.yPosList = yList
        self.obstaclePub.publish(msg)

        return


###################
# Helper messages #
###################

    # params: point: Location of latest observed sonar hit
    #         pointList: list of Locations of already observed sonar hits
    # returns: boolean
    # compared the new point to the previous points and assesses whether they are part of the same obstacle
    def checkSameObstacle(self, point, pointList):

        # a new point belongs to the obstacle that is being formed in pointList if the following condition are met:
        # - the new point is not too far from the previous point (where 'too far' is defined by a constant)
        # - the new point is not too far from a line between the first point and the middle point (defined by a constant)
   

        # checking the first condition
        if (self.distancePointToPoint(point, pointList[-1]) < self.THRESHOLD_DIST_BETWEEN_POINTS):
            middleIndex = len(pointList)/2
            # if there aren't enough points to check the second condition, assume point should be included
            if (middleIndex < 1):
                return True

            # checking second condition occurs with first
            elif:(self.distancePointToLine(point, pointList[0], pointList[middleIndex]) < self.LINE_DEVIATION_THRESHOLD):
                return True
        
        # if NAND both conditions
        return False 

    # param Location point1
    #       Location point2
    # returns: float distance between points
    # calculates distance between two points
    def distancePointToPoint(self, pont1, point2):
        distance = math.sqrt((point1.getX() - point2.getX())**2 + (point1.getY()-point2.getY())**2)
        return distance


    # params: Location point
    #         Location endpoint1 an endpoint of the line
    #         Location endpoint2 other endpoint of the line
    # returns: distance of point to line
    # calculates distance of point to line
    def distancePointToLine(self, point, endpoint1, endpoint2):
        # TODO
        # finding equation of line in a*x + b*y + c = 0 form
        # starting with y = mx + intercept
        m = (endpoint1.getY()-endpoint2.getY())/(endpoint1.getX()-endpoint2.getX());
        intercept = endpoint1.getY()-m*endpoint2.getX()
        a = -m
        b = 1 
        c = -intercept
        
        # use formula distance = (a*x_o + b*y_0 + c) / sqrt(a**2 + b**2)
        distance = (a*point.getX() + b*point.getY() + c) / math.sqrt(a**2 + b**2)
        return distance

    # parans: point: Location of latest observed sonar hit
    #         mapList: list of objects in map
    # returns: boolean
    # checks to see if the point is part of any of the currently known obstacles
    def checkIfNewObstacle(self, point, mapList):
        # for each obstacle
        for obstacle in mapList:
            
            # check if point is close to obstacle:
            if (point.getX() > obstacle.getMinX) and \
                    (point.getX() < obstacle.getMaxX()) and \
                    (point.getY() > obstacle.getMinY()) and \
                    (point.getY() < obstacle.getMaxY()):
                
                # check each line in the obstacle
                obsPointList = obstacle.getLocationList()
                for i in range(len(obsPointList)):
                    if self.distancePointToLine(point, obsPointList[i], obsPointList[(i+1)%len(obsPointList)]) < self.SONAR_UNCERTAINTY:
                        return False

        # if point doesn't fall on any obstacle lines
        return True

    # param: none
    # returns: none
    # makes an obstacle from pointList, sends it, clears pointList
    def makeObstacle(self):
        [endpoint1, endpoint2, width] = fitLine(pointList)
        obstacle = self.createRectangle(enddpoint1, endpoint2, width)
        self.sendObstacleMsg(obstacle)
        self.mapList.append(obstacle)
        self.pointList = []
        return


    # param: pointList: list of Locations that should be fit to a line
    # returns: [Location endpoint1, Location endpoint2, error]
    # fits a line to given list of points. Returns line parameters and greatest distance of point to line
    def fitLine(self, pointList):
        # This uses the method Eric used in LineEstimator in Lab 5

        sum_x = 0.
        sum_y = 0.
        sum_xx = 0.
        sum_yy = 0.
        sum_xy = 0.
        
        # filling helper variables
        for point in self.pointList:
            sum_x += point.getX()
            sum_y += point.getY()
            sum_xx += point.getX()**2
            sum_yy += point.getY()**2
            sum_xy += point.getX()*point.getY()
            
        # using helper variables to get line equation
        d = sum_xx*sum_yy-sum_xy*sum_xy;
        a_prime = (sum_x*sum_yy-sum_y*sum_xy)/d;
        b_prime = (sum_y*sum_xx-sum_x*sum_xy)/d;
        h = math.hypot(a_prime,b_prime);

        a = a_prime/h
        b = b_prime/h
        c = -1/h

        # creating a line that stretches from minimum x value to maximum x value of points in list
        xVals = [point.getX() for point in pointList]
        xStart = min(xVals)
        xEnd = max(xVals)
    
        # using x values and line equation to calculate y values
        # a*x + b*y + c = 0
        # y = -a/b x -c/b
        yStart = -a/b*xStart -c/b
        yEnd = -a/b*xEnd -c/b


        # calculating the width - the farthest distance of any point to the calculated line
        endpoint1 = Location(xStart, yStart)
        endpoint2 = Location(xEnd, yEnd)
        pointDistances = [distancePointToLine(point, endpoint1, endpoint2) for point in pointList]
        return [endpoint1, endpoint2, max(pointDistances)]

    # param: Location axisEndpoint1: one endpoint of axis of symmetry
    #        Location axisEndpoint2: other endpoint of axis of symmetry
    #        float width: width of rectangle
    # returns: Obstacle object containing points of rectangle
    # creates rectangle given line of symmetry and width
    def createRectangle(self, axisEndpoint1, axisEndpoint2, width):
        
        # the corners will lie along a perpindicular line that passes through each end point
        # each corner will lie a distance of width/2 along this perpendicular line
        perpindicularSlope = -(axisEndpoint1.getX()-axixEndpoint2.getX())/(axisEndpoint1.getY()-axixEndpoint2.getY()) 
        perpAngle = math.atan(perpindicularSlope)

        # creating the points of the rectangle
        corner1 = Location(axisEndpoint1.getX() + .5*width*math.cos(perpAngle), axisEndpoint1.getY() + .5*width*math.sin(perpAngle))
        corner2 = Location(axisEndpoint1.getX() - .5*width*math.cos(perpAngle), axisEndpoint1.getY() -.5*width*math.sin(perpAngle))
        corner3 = Location(axisEndpoint2.getX() - .5*width*math.cos(perpAngle), axisEndpoint2.getY() -.5*width*math.sin(perpAngle))
        corner4 = Location(axisEndpoint2.getX() + .5*width*math.cos(perpAngle), axisEndpoint2.getY() +.5*width*math.sin(perpAngle))

        newObstacle = Obstacle([corner1, corner2, corner3, corner4])
        return newObstacle

        
