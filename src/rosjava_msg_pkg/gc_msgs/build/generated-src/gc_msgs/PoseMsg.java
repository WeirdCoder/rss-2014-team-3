package gc_msgs;

public interface PoseMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "gc_msgs/PoseMsg";
  static final java.lang.String _DEFINITION = "float64 xPosition\nfloat64 yPosition\nfloat64 angle";
  double getXPosition();
  void setXPosition(double value);
  double getYPosition();
  void setYPosition(double value);
  double getAngle();
  void setAngle(double value);
}
