package GlobalNavigation;

import org.ros.message.MessageListener;
import rss_msgs.OdometryMsg;

public class OdometryListener implements MessageListener<rss_msgs.OdometryMsg> {

	private GlobalNavigation nav;

	public OdometryListener(GlobalNavigation globalNavigation) {
		// TODO Auto-generated constructor stub
		nav = globalNavigation;
	}

	@Override
	public void onNewMessage(rss_msgs.OdometryMsg arg0) {
		// TODO Auto-generated method stub
		nav.handle(arg0);
	}

}
