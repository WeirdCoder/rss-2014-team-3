; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude EncoderMsg.msg.html

(cl:defclass <EncoderMsg> (roslisp-msg-protocol:ros-message)
  ((lWheelDist
    :reader lWheelDist
    :initarg :lWheelDist
    :type cl:float
    :initform 0.0)
   (rWheelDist
    :reader rWheelDist
    :initarg :rWheelDist
    :type cl:float
    :initform 0.0))
)

(cl:defclass EncoderMsg (<EncoderMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <EncoderMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'EncoderMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<EncoderMsg> is deprecated: use gc_msgs-msg:EncoderMsg instead.")))

(cl:ensure-generic-function 'lWheelDist-val :lambda-list '(m))
(cl:defmethod lWheelDist-val ((m <EncoderMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:lWheelDist-val is deprecated.  Use gc_msgs-msg:lWheelDist instead.")
  (lWheelDist m))

(cl:ensure-generic-function 'rWheelDist-val :lambda-list '(m))
(cl:defmethod rWheelDist-val ((m <EncoderMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:rWheelDist-val is deprecated.  Use gc_msgs-msg:rWheelDist instead.")
  (rWheelDist m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <EncoderMsg>) ostream)
  "Serializes a message object of type '<EncoderMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'lWheelDist))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'rWheelDist))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <EncoderMsg>) istream)
  "Deserializes a message object of type '<EncoderMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'lWheelDist) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'rWheelDist) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<EncoderMsg>)))
  "Returns string type for a message object of type '<EncoderMsg>"
  "gc_msgs/EncoderMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'EncoderMsg)))
  "Returns string type for a message object of type 'EncoderMsg"
  "gc_msgs/EncoderMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<EncoderMsg>)))
  "Returns md5sum for a message object of type '<EncoderMsg>"
  "399c9051a81b8f84f6e69a8b8eaa8666")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'EncoderMsg)))
  "Returns md5sum for a message object of type 'EncoderMsg"
  "399c9051a81b8f84f6e69a8b8eaa8666")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<EncoderMsg>)))
  "Returns full string definition for message of type '<EncoderMsg>"
  (cl:format cl:nil "float64 lWheelDist~%float64 rWheelDist~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'EncoderMsg)))
  "Returns full string definition for message of type 'EncoderMsg"
  (cl:format cl:nil "float64 lWheelDist~%float64 rWheelDist~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <EncoderMsg>))
  (cl:+ 0
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <EncoderMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'EncoderMsg
    (cl:cons ':lWheelDist (lWheelDist msg))
    (cl:cons ':rWheelDist (rWheelDist msg))
))
