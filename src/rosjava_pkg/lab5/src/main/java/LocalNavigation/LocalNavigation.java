package LocalNavigation;

import java.io.*;
import java.util.*;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.ConnectedNode;
import org.ros.message.*;
import org.ros.message.MessageListener;
import org.ros.node.topic.Publisher;
import rss_msgs.*;
//import org.ros.message.rss_msgs.ResetMsg;
import std_msgs.String;
import org.ros.node.topic.Subscriber;
import lab5_msgs.*;


public class LocalNavigation implements NodeMain{
    //Object lock;
    public Subscriber<rss_msgs.SonarMsg> ftSonarSub;
    public Subscriber<rss_msgs.SonarMsg> bkSonarSub;
    public Subscriber<rss_msgs.BumpMsg> bumpSub;
    public Subscriber<rss_msgs.OdometryMsg> odomSub;
    public Subscriber<std_msgs.String> cmdSub;
    //  public Subscriber<ResetMsg> resetSub;
    
    public int state = -1;
    //States
    private final int STOP = -1;
    private final int STOP_ON_BUMP = 0;
    private final int ALIGN_ON_BUMP = 1;
    private final int ALIGNING = 2;
    private final int ALIGNED = 3;
    private final int AR_1 = 4;
    private final int AR_2 = 5;
    private final int ALIGNED_AND_ROTATED = 6;
    private final int BACKING_UP = 7;
    private final int FINDING_WALL = 8;
    private final int TRACKING_WALL = 9;
    private final int WALL_ENDED = 10;
    private final int DONE = 11;
    

    private final double DISTANCE_TO_RETREAT = 0.4;
    private final double WALL_TRACKING_DISTANCE = DISTANCE_TO_RETREAT;
    private final double WALL_TRACKING_KP = 0.5;
    private final double CLOSED_DISTANCE = .2;

    private double[] currentOdometry = {0,0,0};
    private boolean[] currentBump = {false, false};
    private double[] currentSonar = {0,0};
    private final double sonarYPos = .1875; // in m
    private final double sonarFrontXPos = .048; //in m
    private final double sonarBackXPos = -.333; //in m

    public final double sonarThreshold = 1.0; // in m; threshold for an obstacle vs. free space

    private boolean frontSonarClear = true; //whether front sonar sees an obstacle
    private boolean backSonarClear = true; // whether back sonar sees an obstacle

    public Publisher<std_msgs.String> statePub;
    public Publisher<rss_msgs.MotionMsg> motionPub;
    public Publisher<lab5_msgs.GUIPointMsg> guiPointPub;
    public Publisher<lab5_msgs.GUILineMsg> guiLinePub;
    public Publisher<lab5_msgs.GUISegmentMsg> guiSegPub;
    public Publisher<rss_msgs.OdometryMsg> resetOdometryPub;

    private LineEstimator lineMaker = new LineEstimator();    
    //Changes state to target int.  And publishes the change.

    private double pos_x;
    private double pos_y;
    private double pos_theta;
    private double dist_pose;
    private double init_pose[];
    private double init_theta;
    private double dist_theta;
    private double last_theta;

    private double aveInitialSonar = 0.0;;

    private ArrayList<double[]> segments = new ArrayList<double[]>();
    
    private boolean saveErrors = false;
    private PrintWriter errorWriter;
    private final java.lang.String errorFile = "errorFile.txt";
    
    private void changeState(int inState){
        state = inState;
        switch (inState){
            case STOP:
                publishState("STOP");
                break;
            case STOP_ON_BUMP:
                publishState("STOP_ON_BUMP");
                break;
            case ALIGN_ON_BUMP:
                publishState("ALIGN_ON_BUMP");
                break;
            case ALIGNING:
                publishState("ALIGNING");
                break;
            case ALIGNED:
                publishState("ALIGNED");
                break;
            case AR_1:
                publishState("AR_1");
                break;
            case AR_2:
                publishState("AR_2");
                break;
            case ALIGNED_AND_ROTATED:
                publishState("ALIGNED_AND_ROTATED");
                break;
            case BACKING_UP:
                publishState("BACKING_UP");
                break;
            case FINDING_WALL:
                publishState("FINDING_WALL");
                break;
            case TRACKING_WALL:
                publishState("TRACKING_WALL");
                break;
            case WALL_ENDED:
                publishState("WALL_ENDED");
                break;
            case DONE:
                publishState("DONE");
                break;
        }
    }

    //Publish State   //Should be called by listeners to update state
    public void publishState(java.lang.String str){
            std_msgs.String std_str = statePub.newMessage();
            std_str.setData(str);
        //statePub.publish(new org.ros.message.std_msgs.String(str));
            statePub.publish(std_str);
    }
    
    @Override
    public void onStart(final ConnectedNode node){
        
//        System.err.println("staring node");
        //StateUpDate
        resetOdometryPub = node.newPublisher("/rss/odometry_update", "rss_msgs/OdometryMsg");
        statePub = node.newPublisher("/rss/state","std_msgs/String");
        motionPub = node.newPublisher("command/Motors","rss_msgs/MotionMsg");
        guiPointPub = node.newPublisher("/gui/Point", "lab5_msgs/GUIPointMsg");
        guiLinePub = node.newPublisher("/gui/Line", "lab5_msgs/GUILineMsg");
        guiSegPub = node.newPublisher("/gui/Segment", "lab5_msgs/GUISegmentMsg");

    try{
	    errorWriter = new PrintWriter(errorFile);
    }
    catch(Exception e){}
        // lock = new Object();

        //User Command Subscriber
        cmdSub = node.newSubscriber("/rss/usercmd","std_msgs/String");
        cmdSub.addMessageListener(new MessageListener<std_msgs.String>(){
                @Override
                public void onNewMessage(std_msgs.String msg){
                    if (msg.getData().equals("START")){
                        changeState(ALIGNING);
                    }
                    else if (msg.getData().equals("STOP")){
                        MotionMsg tempmsg = motionPub.newMessage();
                        motionPub.publish(tempmsg);
                        changeState(STOP);
                    }
                }
            });
        
        //Reset Command
        /*
        resetSub = node.newSubscriber("/rss/reset", "rss_msgs/ResetMsg");
        resetSub.addMessageListener(new MessageListener<ResetMsg>(){
                @Override
                public void onNewMessage(ResetMsg msg){
                    lineMaker.clear();
                    MotionMsg tempmsg = new MotionMsg();
                    motionPub.publish(tempmsg);
                    OdometryMsg reset = new OdometryMsg();
                    reset.x = 0;
                    reset.y = 0;
                    reset.theta = 0;
                    resetOdometryPub.publish(reset);
                    changeState(STOP);
                }
            });
        */

        //FRONT SONAR
        ftSonarSub = node.newSubscriber("rss/Sonars/Front","rss_msgs/SonarMsg");
        ftSonarSub.addMessageListener(new MessageListener<rss_msgs.SonarMsg>(){
                @Override
                public void onNewMessage(rss_msgs.SonarMsg msg){ 
                    //System.err.println("Front Sonar: " + msg.range);
                     currentSonar[0]=msg.getRange();
                     GUIPointMsg  tempmsg  = guiPointPub.newMessage();
                     double poseX = currentOdometry[0];
                     double poseY = currentOdometry[1];
                     double poseTheta = -currentOdometry[2];

                     // transforming into global coordinates from robot coordinates; sonars are in -y direction
                     tempmsg.setY(poseY + Math.cos(poseTheta)*(-sonarYPos - msg.getRange()) - Math.sin(poseTheta)*sonarFrontXPos);
                     tempmsg.setX(poseX + Math.cos(poseTheta)*sonarFrontXPos + Math.sin(poseTheta)*(-sonarYPos - msg.getRange()));
                     tempmsg.setShape(0);

                     //obstacle points blue. Non-obstacle points are red
                     lab5_msgs.ColorMsg msgColor = node.getTopicMessageFactory().newFromType(lab5_msgs.ColorMsg._TYPE);
                     if(msg.getRange() < sonarThreshold){
                         msgColor.setB(255);
                         frontSonarClear = false;

                         if(state == BACKING_UP || state == TRACKING_WALL)
                         {
                            lineMaker.add(tempmsg.getX(), tempmsg.getY()); //adding point to lineMaker
                            double[] lineParams = lineMaker.getLineParams(); // returns a, b, c
                         
                             //sending line representing obstacle to be drawn by GUI
                             GUILineMsg lineMsg = guiLinePub.newMessage();
                             lineMsg.setLineA(lineParams[0]);
                             lineMsg.setLineB(lineParams[1]);
                             lineMsg.setLineC(lineParams[2]);
                             guiLinePub.publish(lineMsg);
                        }
                     }
                     else{
                         frontSonarClear = true;
                         msgColor.setR(255);

                         //if neither front nor back sonars see obstacle, past obstacle - remove line
                         //if (frontSonarClear && backSonarClear){
                         //    lineMaker.clear();
                         //}
                     }
                     tempmsg.setColor(msgColor);
                     guiPointPub.publish(tempmsg);
                }
            });

        //BACK SONAR
        bkSonarSub = node.newSubscriber("rss/Sonars/Back","rss_msgs/SonarMsg");
        bkSonarSub.addMessageListener(new MessageListener<rss_msgs.SonarMsg>(){
                @Override
                public void onNewMessage(rss_msgs.SonarMsg msg){
                    currentSonar[1]=msg.getRange();
                    //System.err.println("Back Sonar message");
                    lab5_msgs.GUIPointMsg  tempmsg  = guiPointPub.newMessage();
                    double poseX = currentOdometry[0];
                    double poseY = currentOdometry[1];
                    double poseTheta = -currentOdometry[2];
                    // transforming into global coordinates from robot coordinates
                    tempmsg.setY(poseY + Math.cos(poseTheta)*(-sonarYPos - msg.getRange()) - Math.sin(poseTheta)*sonarBackXPos);
                    tempmsg.setX(poseX + Math.cos(poseTheta)*sonarBackXPos + Math.sin(poseTheta)*(-sonarYPos - msg.getRange()));
                    tempmsg.setShape(1);
                    ColorMsg msgColor = node.getTopicMessageFactory().newFromType(lab5_msgs.ColorMsg._TYPE);

                     //back sonar sees an obstacle
                     if(msg.getRange() < sonarThreshold){
                         msgColor.setB(255);
                         //backSonarClear = false;

                         // creating line to demarcate obstacle in model of environment
                         if(state == BACKING_UP || state == TRACKING_WALL)
                         {
                             lineMaker.add(tempmsg.getX(), tempmsg.getY()); //adding point to lineMaker's calculations
                             double[] lineParams = lineMaker.getLineParams(); // returns a, b, c
                             
                             //sending line representing obstacle to be drawn by GUI
                             lab5_msgs.GUILineMsg lineMsg = guiLinePub.newMessage();
                             lineMsg.setLineA(lineParams[0]);
                             lineMsg.setLineB(lineParams[1]);
                             lineMsg.setLineC(lineParams[2]);
                             guiLinePub.publish(lineMsg);
                         }
                     }

                     //back sonar does not see an obstacle
                     else{
                         msgColor.setR(255);
                         //backSonarClear = true;

                         //if neither front nor back sonars see obstacle, past obstacle - remove line
                         //if (frontSonarClear && backSonarClear){
                         //    lineMaker.clear();
                         //}
                     }
                     tempmsg.setColor(msgColor);
                    guiPointPub.publish(tempmsg);
                    
                }
            });
        //Odometry
        odomSub = node.newSubscriber("rss/odometry", "rss_msgs/OdometryMsg");
        odomSub.addMessageListener(new MessageListener<OdometryMsg>(){
                @Override
                public void onNewMessage(rss_msgs.OdometryMsg msg){
                    //System.err.println("ODOM");
                    currentOdometry[0] = msg.getX();
                    currentOdometry[1] = msg.getY();
                    currentOdometry[2] = msg.getTheta();
                    //System.err.println("Odometry: X: " + msg.x + ", Y: " + msg.y + ", TH: " + msg.theta);
                    runStateMachine();
                }
            });

        //BUMP SENSORS
        bumpSub = node.newSubscriber("rss/BumpSensors","rss_msgs/BumpMsg");
        bumpSub.addMessageListener(new MessageListener<BumpMsg>(){
                @Override
                public void onNewMessage(rss_msgs.BumpMsg msg){
                    //System.err.println("Bumping");
                    currentBump[0] = msg.getLeft();
                    currentBump[1] = msg.getRight();
                    
                }
            });
            }
    
    public void startBackingUp()
    {
        if (currentSonar[0] < sonarThreshold && currentSonar[1] < sonarThreshold){
            aveInitialSonar = (currentSonar[0] + currentSonar[1])/2;
        }
        else if (aveInitialSonar == 0.0){
            aveInitialSonar = currentSonar[0];
        }
        MotionMsg tempMsg= motionPub.newMessage();
        tempMsg.setTranslationalVelocity(-0.2);
        tempMsg.setRotationalVelocity(0);
        motionPub.publish(tempMsg);
        lineMaker.clear();
        changeState(BACKING_UP);
    }

    private void runStateMachine()
    {
        if(state == STOP_ON_BUMP&&(currentBump[0]||currentBump[1])){
            //State is STOP_ON_BUMP and either bumper is depressed.
            //Lab Step 3.2
            MotionMsg tempMsg = motionPub.newMessage(); //Defaults at Stop
            motionPub.publish(tempMsg);
            publishState("HIT");
        }
        else if(state == ALIGN_ON_BUMP){
            if (currentBump[0] || currentBump[1]){
                changeState(ALIGNING);
            }
            else{
                MotionMsg tempMsg = motionPub.newMessage();
                tempMsg.setRotationalVelocity(-.15);
                tempMsg.setTranslationalVelocity(.4);
                motionPub.publish(tempMsg);
            }
        }
        else if(state == ALIGNING){
            //while in state
            MotionMsg tempMsg = motionPub.newMessage();
            double rotVel = 0.05;
            double transVel = 0.1;
            if(currentBump[0]&&!currentBump[1]){
                tempMsg.setRotationalVelocity(rotVel);
                tempMsg.setTranslationalVelocity(0.02);
            }
            else if (!currentBump[0]&&currentBump[1]){
                tempMsg.setRotationalVelocity(-rotVel);
                tempMsg.setTranslationalVelocity(0.002);
            }
            else if (!currentBump[0]&&!currentBump[1])
            {
                tempMsg.setRotationalVelocity(0);
                tempMsg.setTranslationalVelocity(transVel);
            }
            motionPub.publish(tempMsg);
            //State Change
            if(currentBump[0] &&currentBump[1]){
                changeState(ALIGNED); 
            }
        }
        else if(state == ALIGNED){
            MotionMsg tempMsg = motionPub.newMessage();
            init_pose = new double[]{currentOdometry[0], currentOdometry[1]};
            tempMsg.setTranslationalVelocity(-0.2);
            motionPub.publish(tempMsg);
            changeState(AR_1);
        }
        else if (state == AR_1){
            MotionMsg tempMsg = motionPub.newMessage();
            //System.err.println(currentOdometry[0] - init_pose[0]);
            //System.err.println(currentOdometry[1] - init_pose[1]);
            
            dist_pose = Math.sqrt( Math.pow((currentOdometry[0] - init_pose[0]),2) + Math.pow((currentOdometry[1] - init_pose[1]),2));
            //System.err.println(dist_pose);
            if (dist_pose >= DISTANCE_TO_RETREAT){
                init_theta = currentOdometry[2];
                last_theta = init_theta;
                dist_theta = 0;
                tempMsg.setTranslationalVelocity(0);
                tempMsg.setRotationalVelocity(0.2);
                motionPub.publish(tempMsg);
                changeState(AR_2);
            }
        }
        else if (state == AR_2){
            MotionMsg tempMsg = motionPub.newMessage();
            double delta_theta=currentOdometry[2]-last_theta+Math.PI;
            last_theta=currentOdometry[2];
            delta_theta=((delta_theta % (2*Math.PI))+2*Math.PI)%(2*Math.PI); // Real mod
            delta_theta-=Math.PI;
            
            dist_theta += delta_theta;
            //System.err.println("rotating: theta="+dist_theta);
            if (dist_theta >= (Math.PI/2-0.04)) {
                startBackingUp();
            }
        }
        
  
        else if(state==BACKING_UP)
        {
            int n=0;
            for(int i=0;i<2;i++)
            {
                if(currentSonar[i] < sonarThreshold)
                {
                    n++;
                }
            }
            System.err.println(n);
            MotionMsg tempMsg= motionPub.newMessage();
            tempMsg.setTranslationalVelocity(-0.2);

            if(n==2)
            {
                //double error=lineMaker.distance(currentOdometry[0],currentOdometry[1])-WALL_TRACKING_DISTANCE;
                tempMsg.setRotationalVelocity(-0.6*(aveInitialSonar - (currentSonar[0] + currentSonar[1])/2));
                //tempMsg.rotationalVelocity =0.15*(currentSonar[1] - currentSonar[0]);//error*WALL_TRACKING_KP; // Differences between sonar
            }
            motionPub.publish(tempMsg);
            
            if(n==0) // Neither sonar detected wall
            {
                System.err.println("moving to finding wall");
                startFindingWall();
            }
        }
        else if(state==FINDING_WALL)
        {
            int n=0;
            for(int i=0;i<2;i++)
            {
                if(currentSonar[i] < sonarThreshold)
                {
                    n++;
                }
            }
            
            if(n==2) // Both sonar detected wall
            {
                startTrackingWall();
            }
        }
        else if(state==TRACKING_WALL)
        {
            int n=0;
            for(int i=0;i<2;i++)
            {
                if(currentSonar[i] < sonarThreshold)
                {
                    n++;
                }
            }
            System.err.println("Tracking n: " + n);
            MotionMsg tempMsg= motionPub.newMessage();
            tempMsg.setTranslationalVelocity(0.2);
            if(n==2)
            {
                double error=lineMaker.distance(currentOdometry[0],currentOdometry[1])-WALL_TRACKING_DISTANCE;
                tempMsg.setRotationalVelocity(0.6 * (aveInitialSonar - (currentSonar[0] + currentSonar[1])/2));
                //tempMsg.rotationalVelocity =0.3*(currentSonar[1] - currentSonar[0]);//error*WALL_TRACKING_KP; // Differences between sonar
            }
            motionPub.publish(tempMsg);
            if(n==0)
            {
                startWallEnded();
            }
        }
        else if(state==WALL_ENDED){
            double[] firstSeg = segments.get(0);
            double[] lastSeg = segments.get(segments.size()-1);
            double segDistance = Math.sqrt( Math.pow((lastSeg[2] - firstSeg[0]), 2) + Math.pow( (lastSeg[3] - firstSeg[1]),2));
//            System.err.println(segDistance);
            if (segDistance < CLOSED_DISTANCE) {
                changeState(DONE);
            }
            else{
                changeState(ALIGN_ON_BUMP);
            }
        }
    }
    private void startFindingWall()
    {
        rss_msgs.MotionMsg tempMsg = motionPub.newMessage();
        tempMsg.setRotationalVelocity(0);
        tempMsg.setTranslationalVelocity(0.2);
        motionPub.publish(tempMsg);
        lineMaker.saveNextPointAsStart();
        changeState(FINDING_WALL);
    }

    private void startTrackingWall()
    {
        rss_msgs.MotionMsg tempMsg = motionPub.newMessage();
        tempMsg.setRotationalVelocity(0);
        tempMsg.setTranslationalVelocity(0.2);
        motionPub.publish(tempMsg);
        lineMaker.saveLastPointAsEnd();
        changeState(TRACKING_WALL);
    }

    private void startWallEnded()
    {
        rss_msgs.MotionMsg tempMsg = motionPub.newMessage();
        tempMsg.setRotationalVelocity(0);
        tempMsg.setTranslationalVelocity(0);
        motionPub.publish(tempMsg);
        lab5_msgs.GUISegmentMsg guiMsg = guiSegPub.newMessage();
        double[] sxsw = lineMaker.getSegment();
        guiMsg.setStartX(sxsw[0]);
        guiMsg.setStartY(sxsw[1]);
        guiMsg.setEndX(sxsw[2]);
        guiMsg.setEndY(sxsw[3]);
        segments.add(sxsw);
        guiSegPub.publish(guiMsg);
        lineMaker.clear();
        changeState(WALL_ENDED);
    }

    @Override
    public void onError(Node node,Throwable thrown){
    }
    public void onShutdown(Node node){
        node.shutdown();
    }
    @Override
    public void onShutdownComplete(Node node){}
    @Override
    public GraphName getDefaultNodeName(){
        return GraphName.of("rss/local_navigation");
    }

    private void saveErrors(double frontRange, double backRange){
	double distanceError = .5*((frontRange-.5) + (backRange - .5)); // should be .5 from wall, seen by both sensors
	double distBetweenSonars = sonarFrontXPos -sonarBackXPos;
	double angleError = Math.asin((frontRange-backRange)/distBetweenSonars);
	try{
    	errorWriter.println(Float.toString(System.currentTimeMillis()) + " " + Double.toString(distanceError) + " " + Double.toString(angleError)); 
    }
    catch(Exception e){
    }
    }
    
}
