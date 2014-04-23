package gc_msgs;

public interface EncoderMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "gc_msgs/EncoderMsg";
  static final java.lang.String _DEFINITION = "int16 lWheelTicks\nint16 rWheelTicks\n";
  short getLWheelTicks();
  void setLWheelTicks(short value);
  short getRWheelTicks();
  void setRWheelTicks(short value);
}
