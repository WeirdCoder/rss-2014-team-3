; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude HamperMsg.msg.html

(cl:defclass <HamperMsg> (roslisp-msg-protocol:ros-message)
  ((fractionOpen
    :reader fractionOpen
    :initarg :fractionOpen
    :type cl:float
    :initform 0.0))
)

(cl:defclass HamperMsg (<HamperMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <HamperMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'HamperMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<HamperMsg> is deprecated: use gc_msgs-msg:HamperMsg instead.")))

(cl:ensure-generic-function 'fractionOpen-val :lambda-list '(m))
(cl:defmethod fractionOpen-val ((m <HamperMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:fractionOpen-val is deprecated.  Use gc_msgs-msg:fractionOpen instead.")
  (fractionOpen m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <HamperMsg>) ostream)
  "Serializes a message object of type '<HamperMsg>"
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'fractionOpen))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <HamperMsg>) istream)
  "Deserializes a message object of type '<HamperMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'fractionOpen) (roslisp-utils:decode-single-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<HamperMsg>)))
  "Returns string type for a message object of type '<HamperMsg>"
  "gc_msgs/HamperMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'HamperMsg)))
  "Returns string type for a message object of type 'HamperMsg"
  "gc_msgs/HamperMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<HamperMsg>)))
  "Returns md5sum for a message object of type '<HamperMsg>"
  "7b7f74101566817ddf4f4048d8ed8c35")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'HamperMsg)))
  "Returns md5sum for a message object of type 'HamperMsg"
  "7b7f74101566817ddf4f4048d8ed8c35")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<HamperMsg>)))
  "Returns full string definition for message of type '<HamperMsg>"
  (cl:format cl:nil "float32 fractionOpen~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'HamperMsg)))
  "Returns full string definition for message of type 'HamperMsg"
  (cl:format cl:nil "float32 fractionOpen~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <HamperMsg>))
  (cl:+ 0
     4
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <HamperMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'HamperMsg
    (cl:cons ':fractionOpen (fractionOpen msg))
))
