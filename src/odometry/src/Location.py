# stores the pose of the object: xPosition, yPosition and angle
class Location:
    # param xPos: double x position of Robot's origin
    #       yPos: double y posiiton of Robot's origin
    
    def __init__(xPos, yPos):
        self.x = xPos;
        self.y = yPos;
        return;
        
    def getX():
        return self.x;

    def getY():
        return self.y;

    def setX(xPos):
        self.x = xPos;

    def setY(yPos):
        self.y = yPos;
        return;

    def setLocation(xPos, yPos):
        self.x = xPos;
        self.y = yPos;
        return;

    def getLocation():
        return [self.x, self.y];
