package gc_msgs;

public interface MotorCommandMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "gc_msgs/MotorCommandMsg";
  static final java.lang.String _DEFINITION = "uint8 PWM\nint16 motorType\n";
  byte getPWM();
  void setPWM(byte value);
  short getMotorType();
  void setMotorType(short value);
}
