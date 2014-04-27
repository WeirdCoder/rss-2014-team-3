#!/usr/bin/env python                                                           
import rospy
import math
import networkx
import location
import convexhull
import obstacle
import numpy
#from gc_msgs.msg import ObstacleMsg 

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
    def __init__(self, startMap, robotRadius):
        
        self.gridDist = robotRadius/2; 

        # finding edges of map
        xList = []
        yList = []
    
        for obstacle in startMap:
            for point in obstacle.getLocationList():
                xList.append(point.getX())
                yList.append(point.getY())


        self.minX = min(xList);
        self.maxX = max(xList); 
        self.minY = min(yList); 
        self.maxY = max(yList); 
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
        self.cspace.append(self.makeCSObstacleFromCircle(obstacle, self.robotRadius));
        return
                           

#    def createGridGraph(self)
#    x = (minY - minX)/
#    self.gridGraph = networkx.grid_graph(dim=[x,y])
    # param: none
    # returns: none
    # makes grid of points that do not fall within cspace obstacles
    # turns grid into a graph
    def createGraph(self):
        self.graph = networkx.Graph()
        
        # creating graph by adding nodes and edges
        for x in numpy.arange(self.minX, self.maxX, self.gridDist):
            for y in numpy.arange(self.minY, self.maxY, self.gridDist):
                # add node
                self.graph.add_node((x,y))
                
                # add edges of node
                if x > self.minX:
                    self.graph.add_edge((x*self.gridDist,y*self.gridDist),((x-1)*self.gridDist,y*self.gridDist))
                if y > self.minY:
                    self.graph.add_edge((x*self.gridDist,y*self.gridDist),(x*self.gridDist,(y-1)*self.gridDist))
           
        # iterating through cspace, removing edges that cross obstacles
        edges = self.graph.edges()
        for obs in self.cspace:
            corners = obs.getLocationList()

            # getting endponts of each line in obstacle
            for i in range(len(corners)):
                x1 = corners[i].getX()
                y1 = corners[i].getY()

                # might have to wrap around list to get next point
                nextPointIndex = (i+1)%len(corners)
                x2 = corners[nextPointIndex].getX()
                y2 = corners[nextPointIndex].getY()

                # find endpoints of edges
                # each edge is ([node1], [node2]) and each node is [x,y]
                for e in edges:
                    edgeX1 = e[0][0]
                    edgeY1 = e[0][1]
                    edgeX2 = e[1][0]
                    edgeY2 = e[1][1]
                    
                    # check for intersection; remove edge if there is intersection
                    if self.intersects(edgeX1, edgeY1, edgeX2, edgeY2, x1, y1, x2, y2):
                        self.graph.removeEdge((e[0],e[1]))
                     
    # param: none
    # returns: none
    # makes grid of points that do not fall within cspace obstacles
    # turns grid into a graph
    def updateGraph(self, obstacle):
        corners = obstacle.getLocationList()
        edges = self.graph.edges()

        # for each line in the obstacle
        for i in range(len(corners)):
            # get one endpoint
            x1 = corners[i].getX()
            y1 = corners[i].getY()
            
            # get the other endpoint
            # may have to wrap around to get endpoint of line
            nextPointIndex = (i+1)%len(corners)
            x2 = corners[nextPointIndex].getX()
            y2 = corners[nextPointIndex].getY()

            # for each edge in the graph
            for e in edges:
                # get the endpoints of the edges
                edgeX1 = e[0][0]
                edgeY1 = e[0][1]
                edgeX2 = e[1][0]
                edgeY2 = e[1][1]
                
                # if the edge intersects a line from the new obstacle, remove it
                if self.intersects(edgeX1, edgeY1, edgeX2, edgeY2, x1, y1, x2, y2):
                    self.graph.removeEdge((e[0],e[1]))
       
    # param: startLocation: Location of start of path
    #        endLocation: Location of end of path
    # returns: List of Locations, including startLocation and endLocation
    #          that can be used to travel in stragiht lines from start to end
    # finds points in graph nearest startLocation and endLocation
    # uses graph to path between them
    # runs through, smoothing
    def planPath(self, startLocation, endLocation):
        # TODO
        # Make this more efficient
        startX = startLocation.getX()
        startY = startLocation.getY()
        endX = endLocation.getX()
        endY = endLocation.getY()
        nodes = self.graph.getNodes()
        closestStartNode = nodes[0]
        closestStartX = closestStartNode[0]
        closestStartY = closestStartNode[1]
        closestEndNode = nodes[0]
        closestEndX = closestEndNode[0]
        closestEndY = closestEndNode[1]
        closestStartDist = self.getDistance(startX, startY, closestStartX, closestStartY)
        closestEndDist = self.getDistance(endX, endY, closestEndX, closestEndY)
        for node in nodes:
            startDist = self.getDistance(node[0], node[1], closestStartX, closestStartY)
            if startDist < closestStartDist:
                closestStartNode = node
                closestStartDist = startDist
            endDist = self.getDistance(node[0], node[1], closestEndX, closestEndY)
            if endDist < closestEndDist:
                closestEndNode = node
                closestEndDist = endDist

        
        
        path = networkx.astar_path(self.graph, closestStartNode, closestEndNode, self.nodeDistance)
        return path

    def getDistance(self, x1, y1, x2, y2):
        dist = math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2))
        return dist
    
    def nodeDistance(self, a, b):
        dist = math.sqrt((a[0] - b[0])*(a[0] - b[0]) + (a[1] - b[1])*(a[1]-b[1]))
        return dist
###################################
# helper functions from lab5 code #
###################################

    # returns: boolean whether lines intersect
    # tests to see if a line 1-2 intersects with a line 3-4
    # taken from Eric's code in lab 5
    def intersects(self, x1, y1, x2, y2, x3, y3, x4, y4):
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
    def makeCSObstacleFromCircle(self, realObstacle, robotRadius):

        csoPoints = [];
        roVertices = realObstacle.getLocationList();

        print type(robotRadius)

        for roVertex in  roVertices:

            csoPoints.append((roVertex.getX() + robotRadius, roVertex.getY() + robotRadius));
            csoPoints.append((roVertex.getX() - robotRadius, roVertex.getY() + robotRadius));
            csoPoints.append((roVertex.getX() - robotRadius, roVertex.getY() - robotRadius));
            csoPoints.append((roVertex.getX() + robotRadius, roVertex.getY() - robotRadius));
        

        hullPoints = convexhull.convexHull(csoPoints);
        
        # forming points into an obstacle
        convexObstacle = obstacle.Obstacle([]);
        for (x,y) in hullPoints:
            convexObstacle.addXY(x,y);

        return convexObstacle

