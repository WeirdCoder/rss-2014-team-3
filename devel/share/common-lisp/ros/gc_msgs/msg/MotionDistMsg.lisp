; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude MotionDistMsg.msg.html

(cl:defclass <MotionDistMsg> (roslisp-msg-protocol:ros-message)
  ((translationalDist
    :reader translationalDist
    :initarg :translationalDist
    :type cl:float
    :initform 0.0)
   (rotationalDist
    :reader rotationalDist
    :initarg :rotationalDist
    :type cl:float
    :initform 0.0))
)

(cl:defclass MotionDistMsg (<MotionDistMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <MotionDistMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'MotionDistMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<MotionDistMsg> is deprecated: use gc_msgs-msg:MotionDistMsg instead.")))

(cl:ensure-generic-function 'translationalDist-val :lambda-list '(m))
(cl:defmethod translationalDist-val ((m <MotionDistMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:translationalDist-val is deprecated.  Use gc_msgs-msg:translationalDist instead.")
  (translationalDist m))

(cl:ensure-generic-function 'rotationalDist-val :lambda-list '(m))
(cl:defmethod rotationalDist-val ((m <MotionDistMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:rotationalDist-val is deprecated.  Use gc_msgs-msg:rotationalDist instead.")
  (rotationalDist m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <MotionDistMsg>) ostream)
  "Serializes a message object of type '<MotionDistMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'translationalDist))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'rotationalDist))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <MotionDistMsg>) istream)
  "Deserializes a message object of type '<MotionDistMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'translationalDist) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'rotationalDist) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<MotionDistMsg>)))
  "Returns string type for a message object of type '<MotionDistMsg>"
  "gc_msgs/MotionDistMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'MotionDistMsg)))
  "Returns string type for a message object of type 'MotionDistMsg"
  "gc_msgs/MotionDistMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<MotionDistMsg>)))
  "Returns md5sum for a message object of type '<MotionDistMsg>"
  "d058b5a36d2621f2246c4647603c88ce")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'MotionDistMsg)))
  "Returns md5sum for a message object of type 'MotionDistMsg"
  "d058b5a36d2621f2246c4647603c88ce")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<MotionDistMsg>)))
  "Returns full string definition for message of type '<MotionDistMsg>"
  (cl:format cl:nil "float64 translationalDist~%float64 rotationalDist~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'MotionDistMsg)))
  "Returns full string definition for message of type 'MotionDistMsg"
  (cl:format cl:nil "float64 translationalDist~%float64 rotationalDist~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <MotionDistMsg>))
  (cl:+ 0
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <MotionDistMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'MotionDistMsg
    (cl:cons ':translationalDist (translationalDist msg))
    (cl:cons ':rotationalDist (rotationalDist msg))
))
