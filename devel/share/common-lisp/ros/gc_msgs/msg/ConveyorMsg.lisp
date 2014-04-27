; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude ConveyorMsg.msg.html

(cl:defclass <ConveyorMsg> (roslisp-msg-protocol:ros-message)
  ((frontConveyorFractionOn
    :reader frontConveyorFractionOn
    :initarg :frontConveyorFractionOn
    :type cl:float
    :initform 0.0)
   (backConveyorFractionOn
    :reader backConveyorFractionOn
    :initarg :backConveyorFractionOn
    :type cl:float
    :initform 0.0))
)

(cl:defclass ConveyorMsg (<ConveyorMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <ConveyorMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'ConveyorMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<ConveyorMsg> is deprecated: use gc_msgs-msg:ConveyorMsg instead.")))

(cl:ensure-generic-function 'frontConveyorFractionOn-val :lambda-list '(m))
(cl:defmethod frontConveyorFractionOn-val ((m <ConveyorMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:frontConveyorFractionOn-val is deprecated.  Use gc_msgs-msg:frontConveyorFractionOn instead.")
  (frontConveyorFractionOn m))

(cl:ensure-generic-function 'backConveyorFractionOn-val :lambda-list '(m))
(cl:defmethod backConveyorFractionOn-val ((m <ConveyorMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:backConveyorFractionOn-val is deprecated.  Use gc_msgs-msg:backConveyorFractionOn instead.")
  (backConveyorFractionOn m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <ConveyorMsg>) ostream)
  "Serializes a message object of type '<ConveyorMsg>"
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'frontConveyorFractionOn))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'backConveyorFractionOn))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <ConveyorMsg>) istream)
  "Deserializes a message object of type '<ConveyorMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'frontConveyorFractionOn) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'backConveyorFractionOn) (roslisp-utils:decode-single-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<ConveyorMsg>)))
  "Returns string type for a message object of type '<ConveyorMsg>"
  "gc_msgs/ConveyorMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'ConveyorMsg)))
  "Returns string type for a message object of type 'ConveyorMsg"
  "gc_msgs/ConveyorMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<ConveyorMsg>)))
  "Returns md5sum for a message object of type '<ConveyorMsg>"
  "4012de0139fe5e228a6fb8a41850a5eb")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'ConveyorMsg)))
  "Returns md5sum for a message object of type 'ConveyorMsg"
  "4012de0139fe5e228a6fb8a41850a5eb")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<ConveyorMsg>)))
  "Returns full string definition for message of type '<ConveyorMsg>"
  (cl:format cl:nil "float32 frontConveyorFractionOn~%float32 backConveyorFractionOn~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'ConveyorMsg)))
  "Returns full string definition for message of type 'ConveyorMsg"
  (cl:format cl:nil "float32 frontConveyorFractionOn~%float32 backConveyorFractionOn~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <ConveyorMsg>))
  (cl:+ 0
     4
     4
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <ConveyorMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'ConveyorMsg
    (cl:cons ':frontConveyorFractionOn (frontConveyorFractionOn msg))
    (cl:cons ':backConveyorFractionOn (backConveyorFractionOn msg))
))
