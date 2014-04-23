package gc_msgs;

public interface WheelVelocityMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "gc_msgs/WheelVelocityMsg";
  static final java.lang.String _DEFINITION = "float32 leftWheelAngVel\nfloat32 rightWheelAngVel";
  float getLeftWheelAngVel();
  void setLeftWheelAngVel(float value);
  float getRightWheelAngVel();
  void setRightWheelAngVel(float value);
}
