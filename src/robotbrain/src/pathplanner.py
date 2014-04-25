#!/usr/bin/env python                                                           
import rospy
import math
import networkx
import location
import convexhull
import obstacle


#
# This class is used by the RobotBrain to create a list of waypoints to use as a path.
#

class PathPlanner(object):
    # TODO
    # params: startMap: list of obstacles
    #         minX: float, minimum X value on map
    #         maxX: float, maxmum X value on map
    #         minY: float, minimum Y value on map
    #         maxY: float, maximum Y value on map
    #         robotRadius: maximum radius of robot, float
    # turns start Map into CSpace
    # makes graph of points
    def __init__(self, startMap, minX, maxX, minY, maxY, robotRadius):
        
        self.gridDist = robotRadius/2; 
        self.minX = minX;
        self.maxX = maxX; 
        self.minY = minY; 
        self.maxY = maxY; 
        self.robotRadius = robotRadius;

        self.cspace = [];
        self.createCSpace(startMap);
        self.createGraph();

        # creating the subscriber that listens to the mapUpdater
        mapSub = rospy.Subscriber('mapUpdates', ObstacleMsg, self.handleObstacleMsg)
        return

    # param: ObstacleMsg msg
    # returns: none
    # takes in a new obstacle from the mapupdater, updates CSpace and grid
    def handleObstacleMsg(self, msg):
        
        #iterate through message, retrieving points
        locationList = []
        for i in range(len(msg.xPosList)):
            locationList.append(Location(xPosList[i], yPosList[i]))

        # create new obstacle    
        newObstacle = Obstacle(locationList)

        # call appropriate methods to update CSpace and Grid
        self.updateCSpace(newObstacle)
        self.updateGraph(newObstacle)
        return


    # param: obstacleList: list of Obstacles
    # returns: CSpace object
    # creates CSpace from list of obstacles
    def createCSpace(self, obstacleList):
        
        for obstacle in obstacleList:
            self.cspace.append(self.makeCSObstacleFromCircle(obstacle, self.robotRadius));

        return

    # param: obstacle to add to the CSpace
    # returns: none
    # updates CSpace with new obstacle
    def updateCSpace(self, obstacle):
        self.cspace.append((self.makeCSObstacleFromCircle(obstacle, self.robotRadius));
        return 

    # param: none
    # returns: none
    # makes grid of points that do not fall within cspace obstacles
    # turns grid into a graph
    def createGraph(self):
        # TODO 
  
        pass

    # param: none
    # returns: none
    # makes grid of points that do not fall within cspace obstacles
    # turns grid into a graph
    def updateGraph(self, obstacle):
        # TODO 
        pass

    # param: startLocation: Location of start of path
    #        endLocation: Location of end of path
    # returns: List of Locations, including startLocation and endLocation
    #          that can be used to travel in stragiht lines from start to end
    # finds points in graph nearest startLocation and endLocation
    # uses graph to path between them
    # runs through, smoothing
    def planPath(self, startLocation, endLocation):
        # TODO
        pass
    
###################################
# helper functions from lab5 code #
###################################

    # returns: boolean whether lines intersect
    # tests to see if a line 1-2 intersects with a line 3-4
    # taken from Eric's code in lab 5
    def intersects(x1, y1, x2, y2, x3, y3, x4, y4):
        denom=(x2-x1)*(y4-y3)-(y2-y1)*(x4-x3);

        if(denom == 0):
            return False;
        
        t=((x3-x1)*(y4-y3)-(y3-y1)*(x4-x3))/denom;
        u=((x3-x1)*(y2-y1)-(y3-y1)*(x2-x1))/denom;

        if(t>0 and t<1 and u>0 and u<1):
            return True;
        
        return False;

    # param: Obstacle realObstacle
    #        float robotRadius: the robot bounding disk radius (m)
    # returns: CS obstacle
    def makeCSObstacleFromCircle(realObstacle, robotRadius):

        csoPoints = [];
        roVertices = realObstacle.getLocationList();

        for roVertex in  roVertices:
            csoPoints.append((roVertex.getX() + robotRadius, roVertex.getY() + robotRadius));
            csoPoints.append((roVertex.getX() - robotRadius, roVertex.getY() + robotRadius));
            csoPoints.append((roVertex.getX() - robotRadius, roVertex.getY() - robotRadius));
            csoPoints.append((roVertex.getX() + robotRadius, roVertex.getY() - robotRadius));
        

        hullPoints = convexhull.convexHull(csoPoints);
        
        # forming points into an obstacle
        convexObstacle = obstacle.Obstacle();
        for (x,y) in hullPoints:
            convexObstacle.addPoint(x,y);

        return convexObstacle

