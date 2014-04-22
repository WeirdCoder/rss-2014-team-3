package GlobalNavigation;


import org.ros.message.MessageListener;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import rss_msgs.*;

import lab5_msgs.*;
import lab6_msgs.*;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.geom.*; 

public class GlobalNavigation implements NodeMain{
    public static final String APPNAME = "GlobalNavgation";                        // name to use when run as application
    public static final int STOP = 0;                             // state constant, robot stopepd
    public static final int GO = 1;                               // state constant, gollowing path
    public static int ROBOT_BUMPER_RIGHT = 8;                     // right bumpter (orc port 8) index
    public static int ROBOT_BUMPER_LEFT = 9;                      // left bumper (orc port 9) index
    
    public static final double FOLLOW_PATH_TV = 1.5;                // translational velocity  while following path (m/s)
    public static final double FOLLOW_PATH_RV_GAIN = 1;           // proportional gain for rotation controller while following path
    public static final double FOLLOW_PATH_MAX_RV = .01;            // max rotational velocity while following path (rad/s)
    public static final double ROTATE_FIRST_THRESHOLD = .01;        // rotate before trabskation threshold (radians) 
    public static final double WP_THRESHOLD = .01;                  // waypoint reached threshold (m)

    protected PolygonMap map;                                 // the map, read from file in #main
                                                              // the map is specified in the launch file
    protected java.awt.geom.Rectangle2D.Double worldRect;     // world bounds
    protected java.awt.geom.Point2D.Double startPoint;        // start point
    protected java.awt.geom.Point2D.Double goalPoint;         // goal point
    protected VisibilityGraph visibilityGraph;                // the graph
    protected CSpace cspace;                                  // the configuration space
    
    protected Grid grid;
    public static final double GRID_RESOLUTION = 1;               
    public static final java.awt.Color FAR_COLOR = Color.gray;             // color of the farthest cells
    public static final java.awt.Color GOAL_COLOR = Color.green;            // color of the goal cell
    public static final java.awt.Color PATH_COLOR = Color.orange;            // color of the path
    
    protected java.util.List<java.awt.geom.Point2D.Double> waypoints;
    protected java.awt.geom.Point2D.Double currentWaypoint;   // the current waypoint
    protected int currentWaypointIndex;
    
    protected double maxDist;                                 // maximum distance from any cell that can reach the goal to the goal
    protected int state;                                      // current robot state
    protected int redrawCounter;                              
    protected static double robotRadius = 0.33;
   
    public Subscriber<rss_msgs.BumpMsg> bumpSub;
    public Subscriber<rss_msgs.OdometryMsg> odoSub;
    public Publisher<std_msgs.String> statePub;
    public Publisher<rss_msgs.MotionMsg> motionPub;
    public Publisher<lab6_msgs.GUIRectMsg> guiRectPub;
    public Publisher<lab6_msgs.GUIPolyMsg> guiPolyPub;
    public Publisher<lab5_msgs.GUIPointMsg> guiPointPub;
    public Publisher<lab5_msgs.GUISegmentMsg> guiSegPub;

    public GeomUtils geomUtil = new GeomUtils();


    //constructor
    public GlobalNavigation(){
    }

    // display map to console and the GUI
    public void displayMap(){
        System.err.println("displaying Map");
	   // send rectangle representing boundary of map
       lab6_msgs.GUIRectMsg rectMsg = guiRectPub.newMessage();
       fillRectMsg(rectMsg, worldRect, Color.black,false); //msg, rectangle, color, filled
       guiRectPub.publish(rectMsg);
       
   	   // for each obstacle in map, send polygon
	   java.util.List<PolygonObstacle> obstacleList = map.getObstacles();
	
	   for(int i = 0; i < obstacleList.size(); i++){
           lab6_msgs.GUIPolyMsg polyMsg = guiPolyPub.newMessage();
           fillPolyMsg(polyMsg, obstacleList.get(i), Color.black, true, true); //picking a color for all; assuming are closed, draw filled
           guiPolyPub.publish(polyMsg);
       } 
        
    }

  
    public void fillPointMsg(lab5_msgs.GUIPointMsg msg, java.awt.geom.Point2D.Double point, java.awt.Color color, long shape){
	// filling simple values
	msg.setX((float) point.getX());
	msg.setY((float) point.getY());
	msg.setShape((int) shape);

	//setting color
	//TODO msg.color = new ColorMsg();
	msg.getColor().setR(color.getRed());
	msg.getColor().setB(color.getBlue());
        msg.getColor().setG(color.getGreen());
    }

    
    public static void fillPolyMsg(GUIPolyMsg msg, PolygonObstacle obstacle, java.awt.Color c, boolean filled, boolean closed){
	// filling message
	// setting filled
	msg.setFilled(0) ;
	if(filled){
	    msg.setFilled(1);
	}
	
	//setting closed
	msg.setClosed(0);
	if(closed){
	    msg.setClosed(1);
	}

	//setting color
	//TODO msg.c = new ColorMsg();
	msg.getC().setR(c.getRed());
	msg.getC().setB(c.getBlue());
        msg.getC().setG(c.getGreen());

	//opening up Polygon object to get points
	java.util.List<java.awt.geom.Point2D.Double> pointList = obstacle.getVertices();
	msg.setNumVertices(pointList.size());
	float[] xList = new float[msg.getNumVertices()];
	float[] yList = new float[msg.getNumVertices()];

	//looping through all points, adding to list
	for (int i = 0; i < msg.getNumVertices(); i++){
	    java.awt.geom.Point2D.Double currentPoint = pointList.get(i);
	    xList[i] = (float) currentPoint.getX();
	    yList[i] = (float) currentPoint.getY();
	}
	
	msg.setX(xList);
	msg.setY(yList);
	

    }

    public static void fillRectMsg(GUIRectMsg msg, java.awt.geom.Rectangle2D.Double r, java.awt.Color c, boolean filled){
        msg.setX( (float) r.getX());
        msg.setY((float) r.getY());
        msg.setWidth((float) r.getWidth());
        msg.setHeight((float) r.getHeight());
        msg.setFilled(0);

        if (filled){
            msg.setFilled(1); //msg.filled should be an int32
        }

        //TODO msg.c = new ColorMsg();
        msg.getC().setR(c.getRed());
        msg.getC().setB(c.getBlue());
        msg.getC().setG(c.getGreen());
    }

    // display grid to the gui; obstacle cells are not displayedl all others are colored according to their distance to the goal
    // using FAR_COLOR and GOAL_COLOR
    public void displayGrid(){
    }

    //display cspace to the gui
    public void displayCSpace(){
        List<PolygonObstacle> realObstacles = map.getObstacles();
        List<Point2D.Double> robotPointList = new LinkedList<Point2D.Double>();
        robotPointList.add(new Point2D.Double(-0.33, -0.225));
        robotPointList.add(new Point2D.Double(0.048, -0.225));
        robotPointList.add(new Point2D.Double(0.048, 0.225));
        robotPointList.add(new Point2D.Double(-0.33, 0.225));

        
        // making obstacles representing the end of the world rectangle in polygon form
        PolygonObstacle worldSide1 = new PolygonObstacle();
        PolygonObstacle worldSide2 = new PolygonObstacle();
        PolygonObstacle worldSide3 = new PolygonObstacle();
        PolygonObstacle worldSide4 = new PolygonObstacle();

        worldSide1.addVertex(worldRect.getX(), worldRect.getY());
        worldSide1.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY());
        worldSide2.addVertex(worldRect.getX(), worldRect.getY());
        worldSide2.addVertex(worldRect.getX(), worldRect.getY() + worldRect.getHeight());
        worldSide3.addVertex(worldRect.getX(), worldRect.getY() + worldRect.getHeight());
        worldSide3.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY() + worldRect.getHeight());
        worldSide4.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY());
        worldSide4.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY() + worldRect.getHeight());

        realObstacles.add(worldSide1);
        realObstacles.add(worldSide2);
        realObstacles.add(worldSide3);
        realObstacles.add(worldSide4);

	// making cspace using obstacles
        //With circle robot
        //cspace = new CSpace(realObstacles, robotRadius/Math.sqrt(2.0));
        cspace = new CSpace(realObstacles,robotPointList); 
	// displaying obstacles
        List<PolygonObstacle> cspaceObstacles = cspace.getObstacles();
        for(int i = 0; i < cspaceObstacles.size(); i++){
           GUIPolyMsg polyMsg = guiPolyPub.newMessage();
           fillPolyMsg(polyMsg, cspaceObstacles.get(i), Color.red, false, true); //picking a color for all; assuming are closed, draw filled
           guiPolyPub.publish(polyMsg);
	}
    }

    public void displayVisibilityGraph(){
    }

    public void fillSegmentMsg(lab5_msgs.GUISegmentMsg msg, java.awt.Color c, java.awt.geom.Point2D.Double start, java.awt.geom.Point2D.Double other){
        msg.setStartX(start.getX());
        msg.setStartY(start.getY());
        msg.setEndX(other.getX());
        msg.setEndY(other.getY());
        msg.getColor().setR(c.getRed());
        msg.getColor().setG(c.getGreen());
        msg.getColor().setB(c.getBlue());
    }

    public static void fillColor(ColorMsg color2,java.awt.Color color){
	color2.setR(color.getRed());
	color2.setG(color.getGreen());
	color2.setB(color.getBlue());
    }

    // generate an interpolated colro between FAR_COLOR and GOAL_COLOR
    // @param: maxDist - the distance of the farthest cell(s) from the goal (m)
    // @param: dist - the distance of the specified cell from the goal (m)
    //    protected static java.awt.Color interpolateColor(double maxDist, double dist){
    //}

    // display a set of line segmets from currentWaypoint to the goal
    public void displayPath(){
    }

    // exercise the convex hull and polygon display code
    public void testConvexHull(){
	// list of points to define desired shape
        List<java.awt.geom.Point2D.Double> pointList = new LinkedList<java.awt.geom.Point2D.Double>();

	Point2D.Double point1 = new java.awt.geom.Point2D.Double(0, 0);
	Point2D.Double point2 = new java.awt.geom.Point2D.Double(1, 1);
	Point2D.Double point3 = new java.awt.geom.Point2D.Double(2, 0);
	Point2D.Double point4 = new java.awt.geom.Point2D.Double(2, 3);
	Point2D.Double point5 = new java.awt.geom.Point2D.Double(1, 3);
	Point2D.Double point6 = new java.awt.geom.Point2D.Double(0, 3);

	pointList.add(point1);
	pointList.add(point2);
	pointList.add(point3);
	pointList.add(point4);
	pointList.add(point5);
	pointList.add(point6);

    
	//testing convexHull
	PolygonObstacle convexTestShape = geomUtil.convexHull(pointList);
	PolygonObstacle testShape = new PolygonObstacle();
	
        //sending message to gui to print each point
	//also filling test shape; another way to display to gui
	for (int i = 0; i < pointList.size(); i++){
	    testShape.addVertex(pointList.get(i));
	    
	    GUIPointMsg pointMsg = guiPointPub.newMessage();
	    fillPointMsg(pointMsg, pointList.get(i), Color.magenta, 0);
	    guiPointPub.publish(pointMsg);
	}

	//sending message to make gui draw
	GUIPolyMsg polyMsg = guiPolyPub.newMessage();
	GUIPolyMsg polyMsg2 = guiPolyPub.newMessage();
	fillPolyMsg(polyMsg, convexTestShape, Color.black, false, true); //picking a color for all; assuming are closed, draw filled
	fillPolyMsg(polyMsg2, testShape, Color.red, false, true); //picking a color for all; assuming are closed, draw filled
	guiPolyPub.publish(polyMsg);
	guiPolyPub.publish(polyMsg2);
	
    }


    //testing CSPACE code
    public void testCspace(){
        List<PolygonObstacle> realObstacles = map.getObstacles();
        List<Point2D.Double> robotPointList = new LinkedList<Point2D.Double>();
        robotPointList.add(new Point2D.Double(-0.33, -0.225));
        robotPointList.add(new Point2D.Double(0.048, -0.225));
        robotPointList.add(new Point2D.Double(0.048, 0.225));
        robotPointList.add(new Point2D.Double(-0.33, 0.225));

        
        //the world rectangle in polygon form
        PolygonObstacle worldSide1 = new PolygonObstacle();
        PolygonObstacle worldSide2 = new PolygonObstacle();
        PolygonObstacle worldSide3 = new PolygonObstacle();
        PolygonObstacle worldSide4 = new PolygonObstacle();

        worldSide1.addVertex(worldRect.getX(), worldRect.getY());
        worldSide1.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY());
        worldSide2.addVertex(worldRect.getX(), worldRect.getY());
        worldSide2.addVertex(worldRect.getX(), worldRect.getY() + worldRect.getHeight());
        worldSide3.addVertex(worldRect.getX(), worldRect.getY() + worldRect.getHeight());
        worldSide3.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY() + worldRect.getHeight());
        worldSide4.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY());
        worldSide4.addVertex(worldRect.getX() + worldRect.getWidth(), worldRect.getY() + worldRect.getHeight());

        realObstacles.add(worldSide1);
        realObstacles.add(worldSide2);
        realObstacles.add(worldSide3);
        realObstacles.add(worldSide4);

        cspace = new CSpace(realObstacles, robotPointList);
        List<PolygonObstacle> cspaceObstacles = cspace.getObstacles();
        for(int i = 0; i < cspaceObstacles.size(); i++){
           GUIPolyMsg polyMsg = guiPolyPub.newMessage();
           fillPolyMsg(polyMsg, cspaceObstacles.get(i), Color.red, false, true); //picking a color for all; assuming are closed, draw filled
           guiPolyPub.publish(polyMsg);
        }
    }
       


 //ACSTART 
    private final int BEGIN_WAYPOINT = 51;
    private final int ERROR_INVALID_START_POINT = 52;
    private final int CONTINUE_WAYPOINT = 53;//Post every new waypoint set.
    private final int FINISH_WAYPOINT = 54;
    // implements path following
    
    public void handle(OdometryMsg msg){
        if(msg == null || waypoints == null){
            publishState("waypointNull");
            return ;
        }
        //Method Constants
        double poseTolerance = 0.1; //in meters
        //Current Pose
        Point2D.Double currentPose = new Point2D.Double(msg.getX(),msg.getY());
        double x = msg.getX();
        double y = msg.getY();
        double theta = msg.getTheta();
        //Determine if CurrentWaypoint is still valid.
        //If Current Waypoint if Null, set 1st Waypoint as waypoint.  Check validity
        if(currentWaypoint == null){
            if(waypoints.size()==0){
                changeState(ERROR_INVALID_START_POINT);
            }
            currentWaypoint=waypoints.get(0);
            changeState(BEGIN_WAYPOINT);
            if(!waypointMatch(currentWaypoint,currentPose)){
                changeState(ERROR_INVALID_START_POINT);
            }
        }
        if(waypointMatch(currentWaypoint,currentPose)){
            //Waypoint match Moving forward.
            int index = waypoints.indexOf(currentWaypoint);
            if(waypoints.size() == index +1){
            	//No more waypoint.  FINISH
            	changeState(FINISH_WAYPOINT);
            	setMotionMsg(motionPub.newMessage(), 0,0);//TODO
            	return;
    	    }
            publishState(new Integer(index+1).toString());
            GUIPointMsg pointMsg = guiPointPub.newMessage();
            fillPointMsg(pointMsg, currentWaypoint, Color.blue, 1);
            guiPointPub.publish(pointMsg); 
    	    currentWaypoint = waypoints.get(index+1);
    	    changeState(CONTINUE_WAYPOINT);
    	}
    	//Update motor headings.
        double desiredTheta = Math.atan2(currentWaypoint.y-y,currentWaypoint.x-x);
        if(theta > 3.1415)
            theta = -6.2831+theta;
        double tv = 0.0;
        double rv = 0.0;
        if(Math.abs(theta - desiredTheta) < .06){
            tv = 0.10;
        }else{
            double dist = Math.cos(theta)*(double)(currentWaypoint.x-x) + Math.sin(theta)*(double)(currentWaypoint.y-y);
            rv = 0.05*(desiredTheta-theta )/Math.abs(desiredTheta- theta)/Math.abs(dist);
            if (rv > 0.05)
                rv = 0.05;
            else if (rv < -0.05)
                rv = -0.05;
        }
        setMotionMsg(motionPub.newMessage(), tv,rv); //TODO
    	//setMotionMsg(new MotionMsg(),0.1*(Math.cos(theta)*(double)(currentWaypoint.x-x) + Math.sin(theta)*(double)(currentWaypoint.y-y)),0.4*(desiredTheta - theta));
    }
        
    //Determine if the two waypoints match within tolerance.
    //Can be edit into a more complex form.
    public boolean waypointMatch(Point2D.Double pta, Point2D.Double ptb ){
	    double poseTolerance = 0.05;
	    return (Math.abs(pta.x - ptb.x) < poseTolerance) && (Math.abs(pta.y-ptb.y)<poseTolerance);
    }


    public void changeState(int instate){
	state = instate;
	switch(instate){
	   case BEGIN_WAYPOINT:
	       publishState("BEGIN_WAYPOINT");
	       break;
	   case ERROR_INVALID_START_POINT:
	       publishState("ERROR_INVALID_START_POINT");
	       break;
	   case CONTINUE_WAYPOINT:
	       publishState("CONTINUE_WAYPOINT");
	       break;
	   case FINISH_WAYPOINT:
	       publishState("FINISH_WAYPOINT");
	       break;
	}
    }

    //Publish State   //Should be called by listeners to update state
    public void publishState(java.lang.String str){
        std_msgs.String std_str = statePub.newMessage();
        std_str.setData(str);
        statePub.publish(std_str);
    }

    public void setMotionMsg(MotionMsg msg, double tv, double rv){
        msg.setRotationalVelocity(rv);
	msg.setTranslationalVelocity(tv);
	motionPub.publish(msg);
    }

    // implements stop on bump
    public void handle(BumpMsg msg){
        if(msg.getLeft() == true && msg.getRight() == true)
	    setMotionMsg(motionPub.newMessage(), 0,0);//TODO
    }

    public void onError(Node node, Throwable thrown){
    }
    // driver, calls instanceMain
    public void onStart(final ConnectedNode node){

        // creating publishers
        guiRectPub = node.newPublisher("gui/Rect", "lab6_msgs/GUIRectMsg");
        guiPolyPub = node.newPublisher("gui/Poly", "lab6_msgs/GUIPolyMsg");
        guiPointPub = node.newPublisher("gui/Point", "lab5_msgs/GUIPointMsg");
        guiSegPub = node.newPublisher("gui/Segment", "lab5_msgs/GUISegmentMsg");
        statePub = node.newPublisher("rss/state","std_msgs/String");
        motionPub = node.newPublisher("command/Motors","rss_msgs/MotionMsg");

        // creating subscribers
        odoSub = node.newSubscriber("rss/odometry","rss_msgs/OdometryMsg");
        odoSub.addMessageListener(new MessageListener<rss_msgs.OdometryMsg>(){ 
		public void onNewMessage(rss_msgs.OdometryMsg msg){
		    handle(msg);}
	    });
        bumpSub = node.newSubscriber("rss/BumpSensors","rss_msgs/BumpMsg");
        bumpSub.addMessageListener(new MessageListener<rss_msgs.BumpMsg>(){ 
		public void onNewMessage(rss_msgs.BumpMsg msg){
		    handle(msg);}
	    });

	    // loading map; set goal and start points
        String mapFile = node.getParameterTree().getString(node.resolveName("~/mapFileName")); // loads map specified in launch file

        try {
            System.err.println(mapFile);
            map = new PolygonMap(mapFile);
        }
        catch(Exception e){
            System.err.println("Could not open map file");
        }
       
        startPoint = map.getRobotStart();
        goalPoint = map.getRobotGoal();
        worldRect = map.getWorldRect();
        try {
            Thread.sleep(30000);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        //display the map
        displayMap();
        //testConvexHull();
        //testCspace();
	displayCSpace();
         	

        // Eric's code
        System.err.println("Starting A* search from "+startPoint+" to "+goalPoint);
        MotionPlanner mp=new MotionPlanner(cspace,startPoint,goalPoint);

        Point2D.Double prev=startPoint;
        waypoints=mp.search();
        for(int i=0;i<waypoints.size();i++)
        {
            System.err.println(waypoints.get(i).x + "|" + waypoints.get(i).y);
            GUISegmentMsg segMsg = guiSegPub.newMessage();
            fillSegmentMsg(segMsg, Color.magenta, prev, waypoints.get(i));
            guiSegPub.publish(segMsg);
            prev=waypoints.get(i);
        }

        waypoints.add(0,startPoint);
    }

    // displays map, computes cspace and grid, displays grid and path, and initiates path following
    public void instanceMain(java.lang.String[] arg){
    }


    public void onShutdown(Node arg0){
    }

    public void onShutdownComplete(Node node){
    }

    public GraphName getDefaultNodeName(){
        return GraphName.of("rss/global_navigation");
    }
}

