; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude WheelVelocityMsg.msg.html

(cl:defclass <WheelVelocityMsg> (roslisp-msg-protocol:ros-message)
  ((leftWheelAngVel
    :reader leftWheelAngVel
    :initarg :leftWheelAngVel
    :type cl:float
    :initform 0.0)
   (rightWheelAngVel
    :reader rightWheelAngVel
    :initarg :rightWheelAngVel
    :type cl:float
    :initform 0.0))
)

(cl:defclass WheelVelocityMsg (<WheelVelocityMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <WheelVelocityMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'WheelVelocityMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<WheelVelocityMsg> is deprecated: use gc_msgs-msg:WheelVelocityMsg instead.")))

(cl:ensure-generic-function 'leftWheelAngVel-val :lambda-list '(m))
(cl:defmethod leftWheelAngVel-val ((m <WheelVelocityMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:leftWheelAngVel-val is deprecated.  Use gc_msgs-msg:leftWheelAngVel instead.")
  (leftWheelAngVel m))

(cl:ensure-generic-function 'rightWheelAngVel-val :lambda-list '(m))
(cl:defmethod rightWheelAngVel-val ((m <WheelVelocityMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:rightWheelAngVel-val is deprecated.  Use gc_msgs-msg:rightWheelAngVel instead.")
  (rightWheelAngVel m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <WheelVelocityMsg>) ostream)
  "Serializes a message object of type '<WheelVelocityMsg>"
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'leftWheelAngVel))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'rightWheelAngVel))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <WheelVelocityMsg>) istream)
  "Deserializes a message object of type '<WheelVelocityMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'leftWheelAngVel) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'rightWheelAngVel) (roslisp-utils:decode-single-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<WheelVelocityMsg>)))
  "Returns string type for a message object of type '<WheelVelocityMsg>"
  "gc_msgs/WheelVelocityMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'WheelVelocityMsg)))
  "Returns string type for a message object of type 'WheelVelocityMsg"
  "gc_msgs/WheelVelocityMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<WheelVelocityMsg>)))
  "Returns md5sum for a message object of type '<WheelVelocityMsg>"
  "78934bf728918c9c6b944758b1934280")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'WheelVelocityMsg)))
  "Returns md5sum for a message object of type 'WheelVelocityMsg"
  "78934bf728918c9c6b944758b1934280")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<WheelVelocityMsg>)))
  "Returns full string definition for message of type '<WheelVelocityMsg>"
  (cl:format cl:nil "float32 leftWheelAngVel~%float32 rightWheelAngVel~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'WheelVelocityMsg)))
  "Returns full string definition for message of type 'WheelVelocityMsg"
  (cl:format cl:nil "float32 leftWheelAngVel~%float32 rightWheelAngVel~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <WheelVelocityMsg>))
  (cl:+ 0
     4
     4
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <WheelVelocityMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'WheelVelocityMsg
    (cl:cons ':leftWheelAngVel (leftWheelAngVel msg))
    (cl:cons ':rightWheelAngVel (rightWheelAngVel msg))
))
