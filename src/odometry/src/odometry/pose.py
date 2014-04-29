# stores the pose of the object: xPosition, yPosition and angle
class Pose:
    # param xPos: double x position of Robot's origin
    #       yPos: double y posiiton of Robot's origin
    #       angle: double angle of robot's axis of symmetry with respect to the x-axis
    
    def __init__(self, xPos, yPos, angle):
        self.x = xPos;
        self.y = yPos;
        self.angle = angle;
        return;
        
    def getX(self):
        return self.x;

    def getY(self):
        return self.y;

    def getAngle(self):
        return self.angle;

    def setX(self,xPos):
        self.x = xPos;

    def setY(self,yPos):
        self.y = yPos;
        return;

    def setAngle(self,angle):
        self.angle = angle;
        return;

    def setPost(self,xPos, yPos, angle):
        self.x = xPos;
        self.y = yPos;
        self.angle = angle;
        return;

    def getPose(self):
        return [self.x, self.y, self.angle];
