package Motor;

import org.ros.message.MessageListener;

import MotorControl.RobotVelocityController;
import rss_msgs.MotionMsg;


public class MotorListener implements MessageListener<MotionMsg> {

    private RobotVelocityController controller;
	
    public MotorListener(RobotVelocityController rvc){
	controller = rvc;
    }
    
    @Override
	public void onNewMessage(rss_msgs.MotionMsg msg){
	
	//System.out.println("got velocity command: " + msg.translationalVelocity + ", " + msg.rotationalVelocity);
	
	double left = msg.getTranslationalVelocity();
	double right = msg.getTranslationalVelocity();
	
	left -= msg.getRotationalVelocity();
	right += msg.getRotationalVelocity();
	
	left *= 3.5;
	right *= 3.5;
	controller.setDesiredAngularVelocity(left, right);
	
    }   
}
