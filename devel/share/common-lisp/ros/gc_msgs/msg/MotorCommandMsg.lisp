; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude MotorCommandMsg.msg.html

(cl:defclass <MotorCommandMsg> (roslisp-msg-protocol:ros-message)
  ((PWM
    :reader PWM
    :initarg :PWM
    :type cl:fixnum
    :initform 0)
   (motorType
    :reader motorType
    :initarg :motorType
    :type cl:fixnum
    :initform 0))
)

(cl:defclass MotorCommandMsg (<MotorCommandMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <MotorCommandMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'MotorCommandMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<MotorCommandMsg> is deprecated: use gc_msgs-msg:MotorCommandMsg instead.")))

(cl:ensure-generic-function 'PWM-val :lambda-list '(m))
(cl:defmethod PWM-val ((m <MotorCommandMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:PWM-val is deprecated.  Use gc_msgs-msg:PWM instead.")
  (PWM m))

(cl:ensure-generic-function 'motorType-val :lambda-list '(m))
(cl:defmethod motorType-val ((m <MotorCommandMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:motorType-val is deprecated.  Use gc_msgs-msg:motorType instead.")
  (motorType m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <MotorCommandMsg>) ostream)
  "Serializes a message object of type '<MotorCommandMsg>"
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:slot-value msg 'PWM)) ostream)
  (cl:let* ((signed (cl:slot-value msg 'motorType)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 65536) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    )
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <MotorCommandMsg>) istream)
  "Deserializes a message object of type '<MotorCommandMsg>"
    (cl:setf (cl:ldb (cl:byte 8 0) (cl:slot-value msg 'PWM)) (cl:read-byte istream))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'motorType) (cl:if (cl:< unsigned 32768) unsigned (cl:- unsigned 65536))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<MotorCommandMsg>)))
  "Returns string type for a message object of type '<MotorCommandMsg>"
  "gc_msgs/MotorCommandMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'MotorCommandMsg)))
  "Returns string type for a message object of type 'MotorCommandMsg"
  "gc_msgs/MotorCommandMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<MotorCommandMsg>)))
  "Returns md5sum for a message object of type '<MotorCommandMsg>"
  "027aab058e5ef57dcd79df8bbee4d54d")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'MotorCommandMsg)))
  "Returns md5sum for a message object of type 'MotorCommandMsg"
  "027aab058e5ef57dcd79df8bbee4d54d")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<MotorCommandMsg>)))
  "Returns full string definition for message of type '<MotorCommandMsg>"
  (cl:format cl:nil "uint8 PWM~%int16 motorType~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'MotorCommandMsg)))
  "Returns full string definition for message of type 'MotorCommandMsg"
  (cl:format cl:nil "uint8 PWM~%int16 motorType~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <MotorCommandMsg>))
  (cl:+ 0
     1
     2
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <MotorCommandMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'MotorCommandMsg
    (cl:cons ':PWM (PWM msg))
    (cl:cons ':motorType (motorType msg))
))
