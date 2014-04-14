package VisualServo;

import java.util.concurrent.ArrayBlockingQueue;

import org.ros.message.*;
import org.ros.message.MessageListener;
import rss_msgs.MotionMsg;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.node.ConnectedNode;
/**
 * 
 * @author previous TA's, prentice, vona
 *
 */
public class VisualServo implements NodeMain, Runnable {

    private static final int width = 640; //modified
 
    private static final int height = 480; // modified 


	/**
	 * <p>The blob tracker.</p>
	 **/
	private BlobTracking blobTrack = null;
        private Publisher<rss_msgs.MotionMsg> publisher;

	private VisionGUI gui;
	private ArrayBlockingQueue<byte[]> visionImage = new ArrayBlockingQueue<byte[]>(1);

	protected boolean firstUpdate = true;

	public Subscriber<sensor_msgs.Image> vidSub;
	public Subscriber<rss_msgs.OdometryMsg> odoSub;

	/**
	 * <p>Create a new VisualServo object.</p>
	 */
	public VisualServo() {

		setInitialParams();

		gui = new VisionGUI();
	}

	protected void setInitialParams() {

	}

	/**
	 * <p>Handle a CameraMessage. Perform blob tracking and
	 * servo robot towards target.</p>
	 * 
	 * @param rawImage a received camera message
	 */
	public void handle(byte[] rawImage) {

		visionImage.offer(rawImage);
	}

	@Override
	public void run() {
	    // student-defined variables
	    //double camWidth = dest.getWidth();
	    //double camHeight = dest.getHeight();
	    
	    double camTheta = 0.0;//rad Camera angle below horizon
            double ballRadius = 0.09 ;//meter
	    double tarAngle = 0 ;//rad
	    double tarDist = 0.5 ;//meter
	   
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
		while (true) { 
			tv =0.0;
			rv =0.0;
			Image src = null;
			try {

				src = new Image(visionImage.take(), width, height);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}

			Image dest = new Image(src);

			blobTrack.apply(src, dest);

			// update newly formed vision message
			gui.setVisionImage(dest.toArray(), width, height);
			if(blobTrack.getTargetDetected()){
				// Begin Student Code
				currentBlobCenter = blobTrack.getCentroid();  // XY array,  Positive Axis with Origin on the upper lefthand side.
				
				currentDist = tarDist*Math.sqrt(targetA/blobTrack.getTargetArea());
				currentAngle = Math.atan(distPerPixel*(currentBlobCenter[0]-targetX)/tarDist); //Y = Displacement in X in meters, X = Current Distance in meters				
				//Convert INPUT TO OUTPUT `
				if(Math.abs(currentDist - tarDist) > 0.05 ){
					tv = maxSpeed*Math.min((currentDist-tarDist)/tarDist,1);
				}
				if(Math.abs(currentAngle - tarAngle) > 0.2){
					rv = -1*angularMaxSpeed*(currentAngle-tarAngle);
				}
				
			}
			//Assigning New Motion
			MotionMsg msg= publisher.newMessage();
			msg.setTranslationalVelocity(tv);
			msg.setRotationalVelocity(rv);
			publisher.publish(msg);
			// End Student Code
		}
	}
        @Override
	public void onError(Node node, Throwable T){
}
	/**
	 * <p>
	 * Run the VisualServo process
	 * </p>
	 * 
	 * @param node optional command-line argument containing hostname
	 */
	@Override
	public void onStart(final ConnectedNode node) {
		blobTrack = new BlobTracking(width, height);

		// Begin Student Code

 		// set parameters on blobTrack as you desire

		

		// initialize the ROS publication to command/Motors

		publisher = node.newPublisher("command/Motors", "rss_msgs/MotionMsg");
		// End Student Code


		final boolean reverseRGB = node.getParameterTree().getBoolean("reverse_rgb", false);

		vidSub = node.newSubscriber("/rss/video", "sensor_msgs/Image");
		vidSub.addMessageListener(new MessageListener<sensor_msgs.Image>() {
			@Override
			public void onNewMessage(
					sensor_msgs.Image message) {
				byte[] rgbData;
				if (reverseRGB) {
					rgbData = Image.RGB2BGR(message.getData().array(),
							(int) message.getWidth(), (int) message.getHeight());
				}
				else {
					rgbData = message.getData().array();
				}
				assert ((int) message.getWidth() == width);
				assert ((int) message.getHeight() == height);
				handle(rgbData);
			}
		});

		odoSub = node.newSubscriber("/rss/odometry", "rss_msgs/OdometryMsg");
		odoSub
		.addMessageListener(new MessageListener<rss_msgs.OdometryMsg>() {
			@Override
			public void onNewMessage(
					rss_msgs.OdometryMsg message) {
				if (firstUpdate) {
					firstUpdate = false;
					gui.resetWorldToView(message.getX(), message.getY());
				}
				gui.setRobotPose(message.getX(), message.getY(), message.getTheta());
			}
		});
		Thread runningStuff = new Thread(this);
		runningStuff.start();
	}

	@Override
	public void onShutdown(Node node) {
		if (node != null) {
			node.shutdown();
		}
	}

	@Override
	public void onShutdownComplete(Node node) {
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rss/visualservo");
	}
}
