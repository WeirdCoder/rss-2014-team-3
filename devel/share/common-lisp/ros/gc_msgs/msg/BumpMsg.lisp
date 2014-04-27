; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude BumpMsg.msg.html

(cl:defclass <BumpMsg> (roslisp-msg-protocol:ros-message)
  ((bumpNumber
    :reader bumpNumber
    :initarg :bumpNumber
    :type cl:fixnum
    :initform 0))
)

(cl:defclass BumpMsg (<BumpMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <BumpMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'BumpMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<BumpMsg> is deprecated: use gc_msgs-msg:BumpMsg instead.")))

(cl:ensure-generic-function 'bumpNumber-val :lambda-list '(m))
(cl:defmethod bumpNumber-val ((m <BumpMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:bumpNumber-val is deprecated.  Use gc_msgs-msg:bumpNumber instead.")
  (bumpNumber m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <BumpMsg>) ostream)
  "Serializes a message object of type '<BumpMsg>"
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:slot-value msg 'bumpNumber)) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <BumpMsg>) istream)
  "Deserializes a message object of type '<BumpMsg>"
    (cl:setf (cl:ldb (cl:byte 8 0) (cl:slot-value msg 'bumpNumber)) (cl:read-byte istream))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<BumpMsg>)))
  "Returns string type for a message object of type '<BumpMsg>"
  "gc_msgs/BumpMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'BumpMsg)))
  "Returns string type for a message object of type 'BumpMsg"
  "gc_msgs/BumpMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<BumpMsg>)))
  "Returns md5sum for a message object of type '<BumpMsg>"
  "e26c7279c94f9c324f9891f26c5631f0")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'BumpMsg)))
  "Returns md5sum for a message object of type 'BumpMsg"
  "e26c7279c94f9c324f9891f26c5631f0")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<BumpMsg>)))
  "Returns full string definition for message of type '<BumpMsg>"
  (cl:format cl:nil "uint8 bumpNumber~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'BumpMsg)))
  "Returns full string definition for message of type 'BumpMsg"
  (cl:format cl:nil "uint8 bumpNumber~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <BumpMsg>))
  (cl:+ 0
     1
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <BumpMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'BumpMsg
    (cl:cons ':bumpNumber (bumpNumber msg))
))
