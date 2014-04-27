; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude GCServoMsg.msg.html

(cl:defclass <GCServoMsg> (roslisp-msg-protocol:ros-message)
  ((frontTrackFractionOn
    :reader frontTrackFractionOn
    :initarg :frontTrackFractionOn
    :type cl:float
    :initform 0.0)
   (backTrackFractionOn
    :reader backTrackFractionOn
    :initarg :backTrackFractionOn
    :type cl:float
    :initform 0.0)
   (hamperFractionOpen
    :reader hamperFractionOpen
    :initarg :hamperFractionOpen
    :type cl:float
    :initform 0.0))
)

(cl:defclass GCServoMsg (<GCServoMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <GCServoMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'GCServoMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<GCServoMsg> is deprecated: use gc_msgs-msg:GCServoMsg instead.")))

(cl:ensure-generic-function 'frontTrackFractionOn-val :lambda-list '(m))
(cl:defmethod frontTrackFractionOn-val ((m <GCServoMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:frontTrackFractionOn-val is deprecated.  Use gc_msgs-msg:frontTrackFractionOn instead.")
  (frontTrackFractionOn m))

(cl:ensure-generic-function 'backTrackFractionOn-val :lambda-list '(m))
(cl:defmethod backTrackFractionOn-val ((m <GCServoMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:backTrackFractionOn-val is deprecated.  Use gc_msgs-msg:backTrackFractionOn instead.")
  (backTrackFractionOn m))

(cl:ensure-generic-function 'hamperFractionOpen-val :lambda-list '(m))
(cl:defmethod hamperFractionOpen-val ((m <GCServoMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:hamperFractionOpen-val is deprecated.  Use gc_msgs-msg:hamperFractionOpen instead.")
  (hamperFractionOpen m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <GCServoMsg>) ostream)
  "Serializes a message object of type '<GCServoMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'frontTrackFractionOn))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'backTrackFractionOn))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'hamperFractionOpen))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <GCServoMsg>) istream)
  "Deserializes a message object of type '<GCServoMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'frontTrackFractionOn) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'backTrackFractionOn) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'hamperFractionOpen) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<GCServoMsg>)))
  "Returns string type for a message object of type '<GCServoMsg>"
  "gc_msgs/GCServoMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'GCServoMsg)))
  "Returns string type for a message object of type 'GCServoMsg"
  "gc_msgs/GCServoMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<GCServoMsg>)))
  "Returns md5sum for a message object of type '<GCServoMsg>"
  "0da8e84971f583398e0e97d0e835b4ee")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'GCServoMsg)))
  "Returns md5sum for a message object of type 'GCServoMsg"
  "0da8e84971f583398e0e97d0e835b4ee")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<GCServoMsg>)))
  "Returns full string definition for message of type '<GCServoMsg>"
  (cl:format cl:nil "float64 frontTrackFractionOn~%float64 backTrackFractionOn~%float64 hamperFractionOpen~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'GCServoMsg)))
  "Returns full string definition for message of type 'GCServoMsg"
  (cl:format cl:nil "float64 frontTrackFractionOn~%float64 backTrackFractionOn~%float64 hamperFractionOpen~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <GCServoMsg>))
  (cl:+ 0
     8
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <GCServoMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'GCServoMsg
    (cl:cons ':frontTrackFractionOn (frontTrackFractionOn msg))
    (cl:cons ':backTrackFractionOn (backTrackFractionOn msg))
    (cl:cons ':hamperFractionOpen (hamperFractionOpen msg))
))
