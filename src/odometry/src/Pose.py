# stores the pose of the object: xPosition, yPosition and angle
class Pose:
    # param xPos: double x position of Robot's origin
    #       yPos: double y posiiton of Robot's origin
    #       angle: double angle of robot's axis of symmetry with respect to the x-axis
    
    def __init__(xPos, yPos, angle):
        self.x = xPos;
        self.y = yPos;
        self.angle = angle;
        return;
        
    def getX():
        return self.x;

    def getY():
        return self.y;

    def getAngle():
        return self.angle;

    def setX(xPos):
        self.x = xPos;

    def setY(yPos):
        self.y = yPos;
        return;

    def setAngle(angle):
        self.angle = angle;
        return;

    def setPost(xPos, yPos, angle):
        self.x = xPos;
        self.y = yPos;
        self.angle = angle;
        return;

    def getPose():
        return [self.x, self.y, self.angle];
