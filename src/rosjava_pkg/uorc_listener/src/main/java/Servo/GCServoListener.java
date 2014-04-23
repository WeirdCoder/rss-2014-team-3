package Servo;

import orc.Orc;
import orc.Servo;

import org.ros.message.MessageListener;
import gc_msgs.MotorCommandMsg;
import org.ros.node.topic.Publisher;

/**
 * <p>
 * Implements a message listener to listen for ROS arm commands. 
 * Also publishes data about the current arm status at 20Hz. 
 * Information about which channel it publishes to is found in the Listener class.
 * This class will need to be modified for a specific robot in order to set the correct bounds on the arm motion.
 * </p>
 * @author rss-staff
 *
 */

public class GCServoListener implements MessageListener<gc_msgs.MotorCommandMsg>{

        private Orc orc;
	private Servo frontleftTrackServo;
	private Servo frontrightTrackServo;
	private Servo backTrackServo;
        private Servo backHatchServo;
        private Servo[] controlServos = new Servo[4];
	public int received =0;

	public GCServoListener(Orc orc, boolean safe){
	        this.orc = orc;
		//TODO pin assignment
		if (safe){
			frontleftTrackServo = new SafeServo(orc, 5, 452, 2081, 1500);//start in the upright position
			frontrightTrackServo = new SafeServo(orc, 5, 452, 2081, 1500);//start in the upright position
			backTrackServo = new SafeServo(orc, 5, 452, 2081, 1500);//start in the upright position
			backHatchServo = new SafeServo(orc, 5, 452, 2081, 1500);//start in the upright position
		} else {
			frontleftTrackServo = new SafeServo(orc, 5, 0, 0, 0);
			frontrightTrackServo = new SafeServo(orc, 5, 0, 0, 0);
			backTrackServo = new SafeServo(orc, 5, 0, 0, 0);
			backHatchServo = new SafeServo(orc, 5, 0, 0, 0);
		}
                controlServos[0] = frontleftTrackServo;
                controlServos[1] = frontrightTrackServo;
                controlServos[2] = backTrackServo;
                controlServos[3] = backHatchServo;
		
                //this.controlServos = {frontleftTrackServo,frontrightTrackServo,backTrackServo,backHatchServo};
                //New GC not publishing State of the servos
		//this.armThreadBase = new ArmPubThread(armPublisher);
		//pubThread = new Thread(this.armThreadBase);
		//pubThread.start();
	}

	@Override
	public void onNewMessage(gc_msgs.MotorCommandMsg msg) {
               int motorType = msg.getMotorType();
               if (motorType < 2){
		//Received a message for the deprecated Wheel Motor Controls, Ignore message.
		return;
               }
               else{ //Received a valid Servo control message
                     //Assuming that the Servo List in brain is inline with HAL.
                     controlServos[motorType -2].setPulseWidth(msg.getPWM());
		}
               /*i
		// TODO Auto-generated method stub
		long bigPWM = msg.getPwms()[0];
		long wristPWM = msg.getPwms()[1];
		long gripperPWM = msg.getPwms()[2];

		if(bigPWM == 0){
		    bigServo.idle();
		} else{
		    bigServo.setPulseWidth((int) bigPWM);
		} 
		
		if (wristPWM == 0){
		    wristServo.idle();
		} else {
		    wristServo.setPulseWidth((int) wristPWM);
		}
		
		if (gripperPWM == 0){
		    gripperServo.idle();
		} else {
		    gripperServo.setPulseWidth((int)gripperPWM);
		}
		
		this.armThreadBase.update(msg);
		//System.out.println("got servo message: " + bigPWM + ", " + gripperPWM + ", " + wristPWM);*/
	}

	/**
	 * <p>Class that enables this servo listener to publish the current state of the arm. 
	 * The run loop publishes arm messages at 20 Hz.</p>
	 * @author Dylan Hadfield-Menell
	 *
	 */
	/*private class ArmPubThread implements Runnable{

		private Publisher<rss_msgs.ArmMsg> armPub;
		private rss_msgs.ArmMsg msg;

		private ArmPubThread(Publisher<rss_msgs.ArmMsg> pub){
			this.armPub = pub;
			msg = armPub.newMessage();
			msg.setPwms(new long[8]);
		}

		private void update(rss_msgs.ArmMsg msg){
			synchronized(this.msg){
				this.msg = msg;
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true){
				synchronized(this.msg) {
					armPub.publish(this.msg);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}*/

}
