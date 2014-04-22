package digitalIO;

import orc.DigitalInput;
import orc.Orc;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import rss_msgs.*;

public class BreakBeamPublisher implements Runnable {
	
    ConnectedNode node;
    Orc orc;
    DigitalInput input;
    rss_msgs.BreakBeamMsg msg;
    Publisher<rss_msgs.BreakBeamMsg> pub;
    Object lock;

    public BreakBeamPublisher(ConnectedNode node, Orc orc, Object lock) {
	this.node = node;
	this.orc = orc;
	this.lock = lock;
	input = new DigitalInput(orc, 2, true, true);
	pub = node.newPublisher("rss/BreakBeam", "rss_msgs/BreakBeamMsg");
    }

    @Override public void run() {
	// TODO Auto-generated method stub
	msg = pub.newMessage();
	while (true){
	    synchronized(lock) {
            msg.setBeamBroken(input.getValue());
	    }
	    pub.publish(msg);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
    
}
