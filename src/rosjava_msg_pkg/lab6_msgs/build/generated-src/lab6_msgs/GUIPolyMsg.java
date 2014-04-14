package lab6_msgs;

public interface GUIPolyMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "lab6_msgs/GUIPolyMsg";
  static final java.lang.String _DEFINITION = "lab5_msgs/ColorMsg c\nint32 numVertices\nfloat32[] x\nfloat32[] y\nint32 closed\nint32 filled";
  lab5_msgs.ColorMsg getC();
  void setC(lab5_msgs.ColorMsg value);
  int getNumVertices();
  void setNumVertices(int value);
  float[] getX();
  void setX(float[] value);
  float[] getY();
  void setY(float[] value);
  int getClosed();
  void setClosed(int value);
  int getFilled();
  void setFilled(int value);
}
