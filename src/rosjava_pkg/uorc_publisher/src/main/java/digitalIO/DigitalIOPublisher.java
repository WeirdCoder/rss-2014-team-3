package digitalIO;

import orc.DigitalInput;
import orc.Orc;

import rss_msgs.DigitalStatusMsg;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
public class DigitalIOPublisher implements Runnable {

    ConnectedNode node;
    Orc orc;
    DigitalInput[] slowInputs = new DigitalInput[8];
    DigitalInput[] fastInputs = new DigitalInput[8];
    rss_msgs.DigitalStatusMsg msg;
    Publisher<rss_msgs.DigitalStatusMsg> pub;
    Object lock;

    public DigitalIOPublisher(ConnectedNode node, Orc orc, Object lock){
	this.node = node;
	this.orc = orc;
	this.lock = lock;
	for (int i = 0; i < 16; i++){
	    if(i < 8){
		slowInputs[i] = new DigitalInput(orc, i, false, false); 
	    } else {
		int j = i - 8;
		fastInputs[j] = new DigitalInput(orc, i, false, false);
	    }
	}
	pub = node.newPublisher("rss/DigitalIO", "rss_msgs/DigitalStatusMsg");
    }


    @Override public void run() {
	msg = pub.newMessage();
	while (true){
	    boolean[] tempFast = new boolean[8];
            boolean[] tempSlow = new boolean[8];
	    for (int i = 0; i <8; i ++) {
		synchronized(lock) {
		    tempFast[i] = fastInputs[i].getValue();
		    tempSlow[i] = slowInputs[i].getValue();
		}
	    }
            msg.setFast(tempFast);
            msg.setSlow(tempSlow);
	    pub.publish(msg);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e){
		e.printStackTrace();
	    }
	}
    }
}
