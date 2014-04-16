; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude EncoderMsg.msg.html

(cl:defclass <EncoderMsg> (roslisp-msg-protocol:ros-message)
  ((lWheelTicks
    :reader lWheelTicks
    :initarg :lWheelTicks
    :type cl:fixnum
    :initform 0)
   (rWheelTicks
    :reader rWheelTicks
    :initarg :rWheelTicks
    :type cl:fixnum
    :initform 0))
)

(cl:defclass EncoderMsg (<EncoderMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <EncoderMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'EncoderMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<EncoderMsg> is deprecated: use gc_msgs-msg:EncoderMsg instead.")))

(cl:ensure-generic-function 'lWheelTicks-val :lambda-list '(m))
(cl:defmethod lWheelTicks-val ((m <EncoderMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:lWheelTicks-val is deprecated.  Use gc_msgs-msg:lWheelTicks instead.")
  (lWheelTicks m))

(cl:ensure-generic-function 'rWheelTicks-val :lambda-list '(m))
(cl:defmethod rWheelTicks-val ((m <EncoderMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:rWheelTicks-val is deprecated.  Use gc_msgs-msg:rWheelTicks instead.")
  (rWheelTicks m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <EncoderMsg>) ostream)
  "Serializes a message object of type '<EncoderMsg>"
  (cl:let* ((signed (cl:slot-value msg 'lWheelTicks)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 65536) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    )
  (cl:let* ((signed (cl:slot-value msg 'rWheelTicks)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 65536) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    )
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <EncoderMsg>) istream)
  "Deserializes a message object of type '<EncoderMsg>"
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'lWheelTicks) (cl:if (cl:< unsigned 32768) unsigned (cl:- unsigned 65536))))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'rWheelTicks) (cl:if (cl:< unsigned 32768) unsigned (cl:- unsigned 65536))))
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
  "ac56cbac03bfc29f4fdbd2bf85ddcbd4")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'EncoderMsg)))
  "Returns md5sum for a message object of type 'EncoderMsg"
  "ac56cbac03bfc29f4fdbd2bf85ddcbd4")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<EncoderMsg>)))
  "Returns full string definition for message of type '<EncoderMsg>"
  (cl:format cl:nil "int16 lWheelTicks~%int16 rWheelTicks~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'EncoderMsg)))
  "Returns full string definition for message of type 'EncoderMsg"
  (cl:format cl:nil "int16 lWheelTicks~%int16 rWheelTicks~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <EncoderMsg>))
  (cl:+ 0
     2
     2
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <EncoderMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'EncoderMsg
    (cl:cons ':lWheelTicks (lWheelTicks msg))
    (cl:cons ':rWheelTicks (rWheelTicks msg))
))
