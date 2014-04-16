package GlobalNavigation;

import org.ros.message.MessageListener;
import rss_msgs.BumpMsg;

public class BumpListener implements MessageListener<rss_msgs.BumpMsg> {
	
	GlobalNavigation nav;
	
	public BumpListener(GlobalNavigation gn){
		nav = gn;
	}

	@Override
	public void onNewMessage(rss_msgs.BumpMsg arg0) {
		// TODO Auto-generated method stub
		nav.handle(arg0);
	}

}
