package GlobalNavigation;

import java.awt.Color;

import org.ros.message.MessageListener;
import lab5_msgs.ColorMsg;
import lab6_msgs.GUIRectMsg;

public class RectMessageListener implements MessageListener<lab6_msgs.GUIRectMsg> {

	private MapGUI gui;

	public RectMessageListener(MapGUI mapGUI) {
		this.gui = mapGUI;
	}

	@Override
	public void onNewMessage(lab6_msgs.GUIRectMsg message) {
		boolean filled = message.getFilled() == 1;
		Color color = getColorFromMsg(message.getC());
		gui.addRect(message.getX(), message.getY(), message.getWidth(), message.getHeight(),
	              filled, color);
		
	}

	public Color getColorFromMsg(lab5_msgs.ColorMsg c) {
		Color color;
		if (c== null){
			color = gui.rectColor;
		}else {
			color = new Color((int)c.getR(), (int)c.getG(), (int)c.getB());
		}
		return color;
	}

}
