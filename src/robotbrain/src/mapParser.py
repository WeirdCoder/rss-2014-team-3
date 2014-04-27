import re
import location
import obstacle
import pose

# params: mapFile: string, name of file with map
#         currentPose: current pose of robot 
# returns: [blockLocations, map]
# opens map file and parses to retrieve block locations and obstacles known to exist on map 

def parseMap(mapFileName, currentPose):
    # opening file, splitting information into information about fiducials, blocks and obstacles
    f = open(mapFileName, 'r')
    maptxt = f.read()
    [fiducialtxt, rest] = re.split('construction_objects {', maptxt)
    [blocktxt, obstacletxt] = re.split('obstacles {', rest)
    f.close()


    # parsing block information
    # begin by finding number of blocks
    m = re.search('num_construction_objects ([0-9]+)', maptxt)
    numBlocks = int(m.group(1))
    
    # for each block, extract its location
    blockLocations = []

    for i in range(numBlocks):
        m = re.search(str(i)+' {\n [ ]*? position { ([-0-9.]+) ([-0-9.]+) }', blocktxt)    
        blockLocations.append(location.Location(float(m.group(1)), float(m.group(2))))

    # if we are sitting on top of a block, move it to the end of the list
    # because need to be approaching a block to see it and retrieve it
    if (blockLocations[0] == currentPose):
        blockLocations = blockLocations[1:].append(blockLocations[0])


    # parsing obstacle information
    # begin by finding number of obstacles
    m = re.search('num_obstacles ([0-9]+)', obstacletxt)
    obstacleList = re.split('num_points', obstacletxt) #this list should contain the points for each obstacle
    mapList = []

    # for each obstacle
    for obstacleTxt in obstacleList:

        locationList = []
    
        # find the number of points
        m = re.search('[0-9]\n', obstacleTxt)
        numPoints = int(m.group(0))

        # add each point to the obstacle
        for i in range(numPoints):
            m = re.search(str(i)+' {\n [ ]*? position { ([-0-9.]+) ([-0-9.]+) }', blocktxt)
            locationList.append(location.Location(float(m.group(1)), float(m.group(2))))
        
        # add obstacle to map
        currentObstacle = obstacle.Obstacle(locationList)
        mapList.append(currentObstacle)

        return [blockLocations, mapList]
