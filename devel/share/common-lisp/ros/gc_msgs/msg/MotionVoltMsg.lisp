; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude MotionVoltMsg.msg.html

(cl:defclass <MotionVoltMsg> (roslisp-msg-protocol:ros-message)
  ((rightVoltage
    :reader rightVoltage
    :initarg :rightVoltage
    :type cl:float
    :initform 0.0)
   (leftVoltage
    :reader leftVoltage
    :initarg :leftVoltage
    :type cl:float
    :initform 0.0))
)

(cl:defclass MotionVoltMsg (<MotionVoltMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <MotionVoltMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'MotionVoltMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<MotionVoltMsg> is deprecated: use gc_msgs-msg:MotionVoltMsg instead.")))

(cl:ensure-generic-function 'rightVoltage-val :lambda-list '(m))
(cl:defmethod rightVoltage-val ((m <MotionVoltMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:rightVoltage-val is deprecated.  Use gc_msgs-msg:rightVoltage instead.")
  (rightVoltage m))

(cl:ensure-generic-function 'leftVoltage-val :lambda-list '(m))
(cl:defmethod leftVoltage-val ((m <MotionVoltMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:leftVoltage-val is deprecated.  Use gc_msgs-msg:leftVoltage instead.")
  (leftVoltage m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <MotionVoltMsg>) ostream)
  "Serializes a message object of type '<MotionVoltMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'rightVoltage))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'leftVoltage))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <MotionVoltMsg>) istream)
  "Deserializes a message object of type '<MotionVoltMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'rightVoltage) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'leftVoltage) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<MotionVoltMsg>)))
  "Returns string type for a message object of type '<MotionVoltMsg>"
  "gc_msgs/MotionVoltMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'MotionVoltMsg)))
  "Returns string type for a message object of type 'MotionVoltMsg"
  "gc_msgs/MotionVoltMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<MotionVoltMsg>)))
  "Returns md5sum for a message object of type '<MotionVoltMsg>"
  "e4bf12f6875584236ceaf9ce7a867639")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'MotionVoltMsg)))
  "Returns md5sum for a message object of type 'MotionVoltMsg"
  "e4bf12f6875584236ceaf9ce7a867639")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<MotionVoltMsg>)))
  "Returns full string definition for message of type '<MotionVoltMsg>"
  (cl:format cl:nil "float64 rightVoltage~%float64 leftVoltage~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'MotionVoltMsg)))
  "Returns full string definition for message of type 'MotionVoltMsg"
  (cl:format cl:nil "float64 rightVoltage~%float64 leftVoltage~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <MotionVoltMsg>))
  (cl:+ 0
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <MotionVoltMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'MotionVoltMsg
    (cl:cons ':rightVoltage (rightVoltage msg))
    (cl:cons ':leftVoltage (leftVoltage msg))
))
