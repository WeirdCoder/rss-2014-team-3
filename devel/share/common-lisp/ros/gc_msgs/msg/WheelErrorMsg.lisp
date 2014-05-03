; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude WheelErrorMsg.msg.html

(cl:defclass <WheelErrorMsg> (roslisp-msg-protocol:ros-message)
  ((leftWheelError
    :reader leftWheelError
    :initarg :leftWheelError
    :type cl:float
    :initform 0.0)
   (rightWheelError
    :reader rightWheelError
    :initarg :rightWheelError
    :type cl:float
    :initform 0.0))
)

(cl:defclass WheelErrorMsg (<WheelErrorMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <WheelErrorMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'WheelErrorMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<WheelErrorMsg> is deprecated: use gc_msgs-msg:WheelErrorMsg instead.")))

(cl:ensure-generic-function 'leftWheelError-val :lambda-list '(m))
(cl:defmethod leftWheelError-val ((m <WheelErrorMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:leftWheelError-val is deprecated.  Use gc_msgs-msg:leftWheelError instead.")
  (leftWheelError m))

(cl:ensure-generic-function 'rightWheelError-val :lambda-list '(m))
(cl:defmethod rightWheelError-val ((m <WheelErrorMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:rightWheelError-val is deprecated.  Use gc_msgs-msg:rightWheelError instead.")
  (rightWheelError m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <WheelErrorMsg>) ostream)
  "Serializes a message object of type '<WheelErrorMsg>"
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'leftWheelError))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-single-float-bits (cl:slot-value msg 'rightWheelError))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <WheelErrorMsg>) istream)
  "Deserializes a message object of type '<WheelErrorMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'leftWheelError) (roslisp-utils:decode-single-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'rightWheelError) (roslisp-utils:decode-single-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<WheelErrorMsg>)))
  "Returns string type for a message object of type '<WheelErrorMsg>"
  "gc_msgs/WheelErrorMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'WheelErrorMsg)))
  "Returns string type for a message object of type 'WheelErrorMsg"
  "gc_msgs/WheelErrorMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<WheelErrorMsg>)))
  "Returns md5sum for a message object of type '<WheelErrorMsg>"
  "f12e4019d81b296d39a48ef1f7e36bfc")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'WheelErrorMsg)))
  "Returns md5sum for a message object of type 'WheelErrorMsg"
  "f12e4019d81b296d39a48ef1f7e36bfc")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<WheelErrorMsg>)))
  "Returns full string definition for message of type '<WheelErrorMsg>"
  (cl:format cl:nil "float32 leftWheelError~%float32 rightWheelError~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'WheelErrorMsg)))
  "Returns full string definition for message of type 'WheelErrorMsg"
  (cl:format cl:nil "float32 leftWheelError~%float32 rightWheelError~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <WheelErrorMsg>))
  (cl:+ 0
     4
     4
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <WheelErrorMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'WheelErrorMsg
    (cl:cons ':leftWheelError (leftWheelError msg))
    (cl:cons ':rightWheelError (rightWheelError msg))
))
