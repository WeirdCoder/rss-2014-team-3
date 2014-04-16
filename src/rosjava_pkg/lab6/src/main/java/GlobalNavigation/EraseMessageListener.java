package GlobalNavigation;

import org.ros.message.MessageListener;
import lab5_msgs.GUIEraseMsg;

public class EraseMessageListener extends LocalNavigation.EraseMessageListener
implements MessageListener<lab5_msgs.GUIEraseMsg> {

	MapGUI gui;

	public EraseMessageListener(MapGUI mapGUI) {
		super(mapGUI);
		this.gui = mapGUI;
	}

	@Override
	public void onNewMessage(GUIEraseMsg arg0) {
		gui.eraseRects();
		gui.erasePolys();
		super.onNewMessage(arg0);
	}

}
