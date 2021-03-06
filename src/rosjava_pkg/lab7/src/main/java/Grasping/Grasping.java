package Grasping;
import VisualServo.*;

/*
 * Grasping.java
 */
import java.util.concurrent.ArrayBlockingQueue;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;


import org.ros.node.topic.Subscriber;
import rss_msgs.*;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import java.awt.geom.*;
import java.awt.Point;
import org.ros.message.MessageListener;
import org.jboss.netty.buffer.ChannelBuffers;
@SuppressWarnings("serial")
public class Grasping implements NodeMain{
	
	//Geometric Arm
	private static final double armLength = 0.245d; //in Meters
	private static double gripperLength = 0.125d;
    private static final int SHOULDER = 0;
    private static final int WRIST = 1;
    private static final int GRIPPER = 2;
    
    private static final int SHOULDER_MIN = 452; // placeholders?
    private static final int WRIST_MIN = 250;
    private static final int GRIPPER_MIN = 270;
    
    private static final int SHOULDER_MAX = 2081; // placeholders?
    private static final int WRIST_MAX = 2350;
    private static final int GRIPPER_MAX = 1290;

    // Slew rates (in PWMs per iteration) for servos
    private static final int SHOULDER_SLEW_RATE = 20;
    private static final int WRIST_SLEW_RATE = 30;
    private static final int GRIPPER_SLEW_RATE = 100;

    // Known positions
    private static final double SHOULDER_FLOOR = 3;
    private static final double WRIST_FLOOR = -1.4;
    private static final double GRIPPER_OPEN = .7;
    private static final double GRIPPER_CLOSED = 3;
    private static final double SHOULDER_CARRY = 0;
    private static final double WRIST_CARRY = 0;

    // For waiting
    private int pause=100;

    // For stepping thru gym_index
    private int gym_index=0;
    private static final double[][] GYM_POSE={
        {SHOULDER_FLOOR,WRIST_FLOOR,GRIPPER_CLOSED},
        {SHOULDER_FLOOR,WRIST_FLOOR,GRIPPER_OPEN},
        {0,0,GRIPPER_OPEN},
        {0,1,GRIPPER_OPEN},
    };

    // Grasp and Transport State Machine
    private int gt_state=-1;
    private static final double FWD_SPEED=0.2;
    private static final double TRANSPORT_DISTANCE=0.305;
    private static final int GRASP_PAUSE=30;
    private static final int LIFT_PAUSE=100;
    private double startX;
    private double startY;

    // Robot odometry
    private double robotX;
    private double robotY;
    private double robotTheta;

    // Break beam sensor
    private boolean blockDetected;

    //Blob Tracking Related constants and variables
    private static final int width = 640;
    private static final int height = 480;
    private ArrayBlockingQueue<byte[]> visionImage = new ArrayBlockingQueue<byte[]>(5);
    private boolean blobDetected = false;
    private double centroidX;
    private double centroidY;
    

	private Publisher<rss_msgs.ArmMsg> armPub;
	private Publisher<rss_msgs.MotionMsg> motionPub;
    private Publisher<sensor_msgs.Image> vidPub;
    private Subscriber<sensor_msgs.Image> vidSub;
    private Subscriber<rss_msgs.ArmMsg> armSub;
    private Subscriber<rss_msgs.OdometryMsg> odoSub;
    private Subscriber<rss_msgs.BreakBeamMsg> breakSub;
    private BlobTracking blobTrack = null;

    private double shoulderAngle=0;
    private double wristAngle=0;
    private double gripperAngle=0;

    private ArmMsg lastMsg;

    public class State{ 
        public final double shoulder; 
        public final double wrist; 
        public State(double shoulder, double wrist) { 
            this.shoulder = shoulder; 
            this.wrist = wrist;
        } 
    }
	/**
	 * Constructor for ArmPoseGUI
	 */
	public Grasping() {
        lastMsg=armPub.newMessage();
        // Approximate "at rest" positions
        long[] armPara = lastMsg.getPwms();
        armPara[SHOULDER]=742;
        armPara[WRIST]=565;
        armPara[GRIPPER]=565;
        lastMsg.setPwms(armPara);
	}

    public void armController()
    {
        //System.err.println("Angles: "+shoulderAngle+","+wristAngle+","+gripperAngle);
        //System.err.println("PWMS: "+angleToPWM(SHOULDER,shoulderAngle)+","+angleToPWM(WRIST,wristAngle)+","+angleToPWM(GRIPPER,gripperAngle));
        ArmMsg newMsg = armPub.newMessage();
        long[] armPWMs = newMsg.getPwms();
        armPWMs[SHOULDER] = slew((int)lastMsg.getPwms()[SHOULDER],angleToPWM(SHOULDER,shoulderAngle),SHOULDER_SLEW_RATE);
        armPWMs[WRIST] = slew((int)lastMsg.getPwms()[WRIST],angleToPWM(WRIST,wristAngle),WRIST_SLEW_RATE);
        armPWMs[GRIPPER] = slew((int)lastMsg.getPwms()[GRIPPER],angleToPWM(GRIPPER,gripperAngle),GRIPPER_SLEW_RATE);
        newMsg.setPwms(armPWMs);
        armPub.publish(newMsg);
        lastMsg=newMsg;
    }

    public static int slew(int cur,int goal,int rate)
    {
        int delta=goal-cur;
        if(delta>rate)
        {
            delta=rate;
        }
        else if(delta<-rate)
        {
            delta=-rate;
        }
        return cur+delta;
    } 

    // cycle through motions
    public void handle(rss_msgs.ArmMsg discard) {

        graspAndTransport();
        armController();

   }

    public void gymnastics()
    {
        pause--;
        if(pause>0)
        {
            return;
        }

        pause=100;
        gym_index=(gym_index+1)%GYM_POSE.length;
        System.err.println("Pose "+gym_index);

        shoulderAngle=GYM_POSE[gym_index][0];
        wristAngle=GYM_POSE[gym_index][1];
        gripperAngle=GYM_POSE[gym_index][2];
    }
   public boolean visualServo(){
      double camTheta = 0.0;//rad Camera angle below horizon
      double ballRadius = 0.09 ;//meter
      double tarAngle = 0 ;//rad
      double tarDist = 0.5 ;//meter    //Target Distance
      double currentDist = 0;
      double currentAngle = 0;
      double[] currentBlobCenter;
      double tv = 0.0 ;// m/s Translational Velocity
      double rv = 0.0 ;// rad/s Rotational Velocity
      double maxSpeed = 0.35; // m/s?
      double angularMaxSpeed = 0.4;
      double distPerPixel = 0.46/width; // @ 0.5Meter  Scaling needed.
      double targetX = width/2; //pixel x
      double targetY = height/2; //pixel y
      double targetA = 18000.0; //pixel^2 area
      tv =0.0;
      rv =0.0;
      Image src = null;
      try {
          src = new Image(visionImage.take(), width, height);
          System.err.println("Got an Image!!");
      } catch (InterruptedException e) {
          e.printStackTrace();
          return false;
      }
      Image dest = new Image(src);
      blobTrack.apply(src, dest);
      // update newly formed vision message
      sensor_msgs.Image pubImage = vidPub.newMessage();
      pubImage.setWidth(width);
      pubImage.setHeight(height);
      pubImage.setEncoding("rgb8");
      //pubImage.setIs_bigendian(0);
      pubImage.setStep(width*3);
      pubImage.setData(ChannelBuffers.copiedBuffer(dest.toArray()));
      vidPub.publish(pubImage);
      if(blobTrack.getTargetDetected()){
          // Begin Student Code
          currentBlobCenter = blobTrack.getCentroid();  // XY array,  Positive Axis with Origin on the upper lefthand side.
          currentDist = tarDist*Math.sqrt(targetA/blobTrack.getTargetArea());
          currentAngle = Math.atan(distPerPixel*(currentBlobCenter[0]-targetX)/tarDist); //Y = Displacement in X in meters, X = Current Distance in meters                
          //Convert INPUT TO OUTPUT `
          if(Math.abs(currentAngle - tarAngle) > 0.2){
              rv = -1*angularMaxSpeed*(currentAngle-tarAngle);
          }
          else if(Math.abs(currentDist - tarDist) > 0.05 ){
              tv = maxSpeed*Math.min((currentDist-tarDist)/tarDist,1);
          }
          else{
              return true;
          }
      }
      //Assigning New Motion
      MotionMsg msg= motionPub.newMessage();
      msg.setTranslationalVelocity(tv);
      msg.setRotationalVelocity(rv);
      motionPub.publish(msg);
      return false;
   }

    public void graspAndTransport()
    {
        double dist;
        switch(gt_state)
        {
            case -1://Visual Servoing
                System.err.println("Seeking block");
                if(visualServo()){
                    gt_state = 0;
                }
                break;
            case 0: // Wait for block
                System.err.println("Waiting for block");
                shoulderAngle=SHOULDER_FLOOR;
                wristAngle=WRIST_FLOOR;
                gripperAngle=GRIPPER_OPEN;
                if(blockDetected)
                {
                    // Switch to state 1: grab block
                    pause=GRASP_PAUSE;
                    gt_state=1;
                }
                break;
            case 1: // Grab block
                System.err.println("Grasping block");
                shoulderAngle=SHOULDER_FLOOR;
                wristAngle=WRIST_FLOOR;
                gripperAngle=GRIPPER_CLOSED;
                pause--;
                if(pause<0)
                {
                    // Switch to state 2: lift block
                    pause=LIFT_PAUSE;
                    gt_state=2;
                }
                break;
            case 2: // Lift block
                System.err.println("Lifting block");
                shoulderAngle=SHOULDER_CARRY;
                wristAngle=WRIST_CARRY;
                gripperAngle=GRIPPER_CLOSED;
                pause--;
                if(pause<0)
                {
                    // Switch to state 3: drive
                    drive(FWD_SPEED,0);
                    startX=robotX;
                    startY=robotY;
                    gt_state=3;
                }
                break;
            case 3: // Drive
                System.err.println("Driving");
                dist=Math.hypot(startX-robotX,startY-robotY);
                System.err.println("X="+robotX+",Y="+robotY+",Theta"+robotTheta);
                System.err.println("Dist travelled="+dist);
                if(dist>=TRANSPORT_DISTANCE)
                {
                    // Switch to state 4: lower block
                    drive(0,0);
                    pause=LIFT_PAUSE;
                    gt_state=4;
                }
                break;
             case 4: // Lower block
                System.err.println("Lowering block");
                shoulderAngle=SHOULDER_FLOOR;
                wristAngle=WRIST_FLOOR;
                gripperAngle=GRIPPER_CLOSED;
                pause--;
                if(pause<0)
                {
                    // Switch to state 5: release block
                    pause=GRASP_PAUSE;
                    gt_state=5;
                }
                break;
            case 5: // Release block
                System.err.println("Releasing block");
                shoulderAngle=SHOULDER_FLOOR;
                wristAngle=WRIST_FLOOR;
                gripperAngle=GRIPPER_OPEN;
                pause--;
                if(pause<0)
                {
                    // Switch to state 6: drive backwards
                    drive(-FWD_SPEED,0);
                    startX=robotX;
                    startY=robotY;
                    gt_state=6;
                }
                break;
            case 6: // Back up
                System.err.println("Backing up"); 
                dist=Math.hypot(startX-robotX,startY-robotY);
                System.err.println("X="+robotX+",Y="+robotY+",Theta"+robotTheta);
                System.err.println("Dist travelled="+dist);
                if(dist>=TRANSPORT_DISTANCE)
                {
                    // Switch to state 7: done
                    drive(0,0);
                    gt_state=7;
                }
                break;
            case 7:
                System.err.println("Done");
        }
    }
    
    // For a given servo channel, return a pwm value from an angle in radians
    public int  angleToPWM(int channel, double angle){
        int pwm = 0;
        //double range = 0;
        int lowerPWM = 0;
        int upperPWM = 0;
        double slope[] = new double[]{-.00209, .00184, -.00203};
        double offset[] = new double[]{4.35, -2.015, 1.7661}; // 4.18
        
        if (channel == SHOULDER) {
            lowerPWM = SHOULDER_MIN;
            upperPWM = SHOULDER_MAX;
        }
        if (channel == WRIST) {
            lowerPWM = WRIST_MIN;
            upperPWM = WRIST_MAX;
        }
        if (channel == GRIPPER) {
            lowerPWM = GRIPPER_MIN;
            upperPWM = GRIPPER_MAX;
        }
        
        pwm = (int)((angle - offset[channel])/slope[channel]);
        if(pwm<lowerPWM)
        {
            pwm=lowerPWM;
        }
        else if(pwm>upperPWM)
        {
            pwm=upperPWM;
        }
        return pwm;
    }

    public void drive(double translate, double rotate)
    {
        MotionMsg msg=motionPub.newMessage();
        msg.setTranslationalVelocity(translate);
        msg.setRotationalVelocity(rotate);
        motionPub.publish(msg);
    }

   
	@Override
    public void onShutdownComplete(Node node) {
	}

	@Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rss/grasping");
	}

	@Override
	public void onShutdown(Node node) {
	}

	@Override
	public void onStart(final ConnectedNode node) {
		blobTrack = new BlobTracking(width,height);
        armPub = node.newPublisher("command/Arm", "rss_msgs/ArmMsg");
        armSub = node.newSubscriber("rss/ArmStatus", "rss_msgs/ArmMsg");
        odoSub = node.newSubscriber("rss/odometry","rss_msgs/OdometryMsg");
        vidSub = node.newSubscriber("/rss/video", "sensor_msgs/Image");
        breakSub = node.newSubscriber("rss/BreakBeam","rss_msgs/BreakBeamMsg");
        motionPub = node.newPublisher("command/Motors","rss_msgs/MotionMsg");
        vidPub = node.newPublisher("/rss/blobVideo","sensor_msgs/Image");
        armSub.addMessageListener(new ArmListener());
        odoSub.addMessageListener(new OdoListener());
        breakSub.addMessageListener(new BreakListener());
        VidListener vidList = new VidListener();
        vidList.reverseRGB = node.getParameterTree().getBoolean("reverse_rgb", false);
        vidSub.addMessageListener(vidList);
	}

    public class ArmListener implements MessageListener<rss_msgs.ArmMsg> {
        @Override
        public void onNewMessage(rss_msgs.ArmMsg msg) {
            handle(msg);
        }
    }

    public class OdoListener implements MessageListener<rss_msgs.OdometryMsg> {
        @Override
        public void onNewMessage(rss_msgs.OdometryMsg msg) {
            robotX=msg.getX();
            robotY=msg.getY();
            robotTheta=msg.getTheta();
        }
    }
    public class VidListener implements MessageListener<sensor_msgs.Image>{
        public boolean reverseRGB;
        @Override
        public void onNewMessage(sensor_msgs.Image message){
            byte[] rgbData;
            if (!reverseRGB) {
                rgbData = Image.RGB2BGR(message.getData().array(),
                        (int) message.getWidth(), (int) message.getHeight());
            }
            else {
                rgbData = message.getData().array();
            }
            assert ((int) message.getWidth() == width);
            assert ((int) message.getHeight() == height);
            try{
                visionImage.add(rgbData);
            }catch(Exception e){
                visionImage.poll();
            }
        }
    }

    public class BreakListener implements MessageListener<rss_msgs.BreakBeamMsg> {
        public void onNewMessage(rss_msgs.BreakBeamMsg msg) {
            blockDetected=msg.getBeamBroken();
        }
    }
    
	//Kinematics  And Inverse Kinematics
    //Kinematics calculate point of grasper relative to the center
    //of the two wheels projected onto the ground.
    //Note: plane is XZ.
    public Point2D.Double kinematics(double shoulder, double wrist){
        double x = +0.09d + Math.cos(shoulder)*armLength + Math.cos(shoulder+wrist)*gripperLength;
        double z = 0.27d + Math.sin(shoulder)*armLength + Math.sin(shoulder+wrist)*gripperLength;
        return new Point2D.Double(x,z);
    }

    public Point2D.Double kinematics(State s){
        return kinematics(s.shoulder, s.wrist);
    }

    //inverse kinematics will define the ideal state to be where
    //the grasper is most parallel with the ground.
    public State inverseKinematics(double x, double z){
        x -= 0.09d;
        z -= 0.27d; //transform origin point to the shoulder.
        //Use law of cosine to determine wrist angle.
        double wrist = 3.1415 - Math.acos((armLength*armLength + gripperLength*gripperLength-(x*x + z*z))/(2*armLength*gripperLength));
        
        //Use law of cosine to determine shoulder angle.
        double shoulder = Math.atan2(z,-x) - Math.acos((armLength*armLength + (x*x + z*z) - gripperLength*gripperLength)/(2*armLength*Math.sqrt(x*x+z*z)));
        
        return new State(shoulder,wrist);
    }

    public State inverseKinematics(Point2D.Double pt){
        return inverseKinematics(pt.getX(), pt.getY()); // actually x, z.
    }
    @Override
    public void onError(Node node, Throwable thrown){
    }
}



