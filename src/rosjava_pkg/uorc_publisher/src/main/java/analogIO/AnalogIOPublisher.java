package analogIO;

import orc.AnalogInput;
import orc.Orc;

import rss_msgs.AnalogStatusMsg;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class AnalogIOPublisher implements Runnable {

    ConnectedNode node;
    Orc orc;
    AnalogInput[] inputs = new AnalogInput[8];
    rss_msgs.AnalogStatusMsg msg;
    Publisher<rss_msgs.AnalogStatusMsg> pub;
    Object lock;
	
    public AnalogIOPublisher(ConnectedNode node, Orc orc, Object lock){
	this.node = node;
	this.orc = orc;
	this.lock = lock;
	for (int i = 0; i < 8; i++){
	    inputs[i] = new AnalogInput(orc, i);
	}
	pub = node.newPublisher("rss/AnalogIO", "rss_msgs/AnalogStatusMsg");
    }
	
    @Override public void run() {
	msg = pub.newMessage();
	while(true){
            double[] tempValues = new double[8];
	    for (int i = 0; i < 8; i ++){
		synchronized(lock) {
		    tempValues[i] = inputs[i].getVoltage();
		}
	    }
            msg.setValues(tempValues);
	    pub.publish(msg);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
