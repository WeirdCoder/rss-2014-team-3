package LocalNavigation;

import org.ros.message.MessageListener;
import lab5_msgs.GUIEraseMsg;

public class EraseMessageListener implements MessageListener<lab5_msgs.GUIEraseMsg> {

	private SonarGUI gui;
	
	public EraseMessageListener(SonarGUI sonarGUI) {
		this.gui = sonarGUI;
	}

	@Override
	public void onNewMessage(lab5_msgs.GUIEraseMsg arg0) {
		gui.eraseLine();
		gui.erasePoints();
		gui.eraseSegments();
	}

}
