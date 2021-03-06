import orc.Orc;

import gc_msgs.MotorCommandMsg;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.Node;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import rss_msgs.MotionMsg;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.topic.Subscriber;
import org.apache.commons.logging.Log;
import org.ros.internal.node.topic.PublisherIdentifier;
import org.ros.internal.node.topic.DefaultSubscriber;

import Motor.MotorListener;
import MotorControl.RobotBase;
import MotorControl.RobotVelocityController;
import MotorControl.RobotVelocityControllerBalanced;
import Servo.GCServoListener;
import java.util.ArrayList;

public class GCListener implements NodeMain {
	
    Orc orc;
    GCServoListener sl;
    private Subscriber<rss_msgs.MotionMsg> motorSub;
    private Subscriber<gc_msgs.MotorCommandMsg> servoSub;

    @Override public void onError(Node node, Throwable thrown){
    }
    @Override public void onStart(final ConnectedNode node) {
	final Log log = node.getLog();
	    
	// TODO Auto-generated method stub
	//Motor Control
	try{
	    
	    System.out.println("in main");
	    orc = Orc.makeOrc(); //this orc is used only by the servo controller, the robotbase makes its own
	    RobotBase robot = new RobotBase();
	    
	    System.out.println("robot base made");
	    
	    RobotVelocityController robotVelocityController = null;
	    robotVelocityController = new RobotVelocityControllerBalanced();
	    System.out.println("robot velocity controller created");
	    robot.setRobotVelocityController(robotVelocityController);
	    robotVelocityController.setGain(1);
	    for(int i = 0; i < 2; i++){
		robotVelocityController.getWheelVelocityController(i).setGain(6);
	    }
	    robot.enableMotors(true);
	    
	    motorSub = node.newSubscriber("command/Motors", "rss_msgs/MotionMsg");
	    motorSub.addMessageListener(new MotorListener(robotVelocityController));
	    log.info("motor Subscriber created");

            //Servo Control surpressed, task passed to GCpython
	    //sl = new GCServoListener(orc, true);//to use safe servos set to true
	    //this requires modification of the ServoListener class to have the correct upper and lower bounds	    
	    //servoSub = node.newSubscriber("MotorCommand", "gc_msgs/MotorCommandMsg");
	    //servoSub.addMessageListener(sl);
	    //log.info("servo subscriber created");

	    /*
	    subMonitorThreadBase = new SubMonitorThread();
	    subMonitorThread = new Thread(this.subMonitorThreadBase);
	    subMonitorThread.start();
	    */
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    /*
    private SubMonitorThread subMonitorThreadBase;
    private Thread subMonitorThread; 

    private class SubMonitorThread implements Runnable{
	@Override
	    public void run() {
	    while (true){
		synchronized(armSub)  {
		    java.util.Collection<PublisherIdentifier> publishers = new ArrayList<PublisherIdentifier>();
		    DefaultSubscriber<?> sub = (DefaultSubscriber<?>)armSub;
		    sub.updatePublishers(publishers);
		    System.out.println(publishers);
		}
		try {
		    Thread.sleep(50);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
    */

    @Override public void onShutdown(Node node) {
	node.shutdown();
	System.out.println(sl.received);
    }

    @Override public void onShutdownComplete(Node node) {
    }
    @Override public GraphName getDefaultNodeName() {
	return GraphName.of("rss/uorc_listener");
    }
}
