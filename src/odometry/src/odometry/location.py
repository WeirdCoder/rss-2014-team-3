# stores the pose of the object: xPosition, yPosition and angle
class Location:
    # param xPos: double x position of Robot's origin
    #       yPos: double y posiiton of Robot's origin
    
    def __init__(self, xPos, yPos):
        self.x = xPos;
        self.y = yPos;
        return;
        
    def getX(self):
        return self.x;

    def getY(self):
        return self.y;

    def setX(self, xPos):
        self.x = xPos;

    def setY(self, yPos):
        self.y = yPos;
        return;

    def setLocation(self, xPos, yPos):
        self.x = xPos;
        self.y = yPos;
        return;

    def getLocation(self):
        return [self.x, self.y];
