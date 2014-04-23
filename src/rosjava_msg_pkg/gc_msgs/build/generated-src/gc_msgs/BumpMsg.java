package gc_msgs;

public interface BumpMsg extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "gc_msgs/BumpMsg";
  static final java.lang.String _DEFINITION = "uint8 bumpNumber\n";
  byte getBumpNumber();
  void setBumpNumber(byte value);
}
