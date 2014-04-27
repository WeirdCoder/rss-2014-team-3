; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude ObstacleAheadMsg.msg.html

(cl:defclass <ObstacleAheadMsg> (roslisp-msg-protocol:ros-message)
  ()
)

(cl:defclass ObstacleAheadMsg (<ObstacleAheadMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <ObstacleAheadMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'ObstacleAheadMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<ObstacleAheadMsg> is deprecated: use gc_msgs-msg:ObstacleAheadMsg instead.")))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <ObstacleAheadMsg>) ostream)
  "Serializes a message object of type '<ObstacleAheadMsg>"
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <ObstacleAheadMsg>) istream)
  "Deserializes a message object of type '<ObstacleAheadMsg>"
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<ObstacleAheadMsg>)))
  "Returns string type for a message object of type '<ObstacleAheadMsg>"
  "gc_msgs/ObstacleAheadMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'ObstacleAheadMsg)))
  "Returns string type for a message object of type 'ObstacleAheadMsg"
  "gc_msgs/ObstacleAheadMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<ObstacleAheadMsg>)))
  "Returns md5sum for a message object of type '<ObstacleAheadMsg>"
  "d41d8cd98f00b204e9800998ecf8427e")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'ObstacleAheadMsg)))
  "Returns md5sum for a message object of type 'ObstacleAheadMsg"
  "d41d8cd98f00b204e9800998ecf8427e")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<ObstacleAheadMsg>)))
  "Returns full string definition for message of type '<ObstacleAheadMsg>"
  (cl:format cl:nil " ~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'ObstacleAheadMsg)))
  "Returns full string definition for message of type 'ObstacleAheadMsg"
  (cl:format cl:nil " ~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <ObstacleAheadMsg>))
  (cl:+ 0
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <ObstacleAheadMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'ObstacleAheadMsg
))
