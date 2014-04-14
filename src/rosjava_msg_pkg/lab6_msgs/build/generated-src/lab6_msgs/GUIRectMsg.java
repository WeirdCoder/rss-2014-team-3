package lab6_msgs;

public interface GUIRectMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "lab6_msgs/GUIRectMsg";
  static final java.lang.String _DEFINITION = "lab5_msgs/ColorMsg c\nfloat32 x\nfloat32 y\nfloat32 width\nfloat32 height\nint32 filled";
  lab5_msgs.ColorMsg getC();
  void setC(lab5_msgs.ColorMsg value);
  float getX();
  void setX(float value);
  float getY();
  void setY(float value);
  float getWidth();
  void setWidth(float value);
  float getHeight();
  void setHeight(float value);
  int getFilled();
  void setFilled(int value);
}
