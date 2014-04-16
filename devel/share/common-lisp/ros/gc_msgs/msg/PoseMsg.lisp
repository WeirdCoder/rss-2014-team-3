; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude PoseMsg.msg.html

(cl:defclass <PoseMsg> (roslisp-msg-protocol:ros-message)
  ((xPosition
    :reader xPosition
    :initarg :xPosition
    :type cl:float
    :initform 0.0)
   (yPosition
    :reader yPosition
    :initarg :yPosition
    :type cl:float
    :initform 0.0)
   (angle
    :reader angle
    :initarg :angle
    :type cl:float
    :initform 0.0))
)

(cl:defclass PoseMsg (<PoseMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <PoseMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'PoseMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<PoseMsg> is deprecated: use gc_msgs-msg:PoseMsg instead.")))

(cl:ensure-generic-function 'xPosition-val :lambda-list '(m))
(cl:defmethod xPosition-val ((m <PoseMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:xPosition-val is deprecated.  Use gc_msgs-msg:xPosition instead.")
  (xPosition m))

(cl:ensure-generic-function 'yPosition-val :lambda-list '(m))
(cl:defmethod yPosition-val ((m <PoseMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:yPosition-val is deprecated.  Use gc_msgs-msg:yPosition instead.")
  (yPosition m))

(cl:ensure-generic-function 'angle-val :lambda-list '(m))
(cl:defmethod angle-val ((m <PoseMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:angle-val is deprecated.  Use gc_msgs-msg:angle instead.")
  (angle m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <PoseMsg>) ostream)
  "Serializes a message object of type '<PoseMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'xPosition))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'yPosition))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'angle))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <PoseMsg>) istream)
  "Deserializes a message object of type '<PoseMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'xPosition) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'yPosition) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'angle) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<PoseMsg>)))
  "Returns string type for a message object of type '<PoseMsg>"
  "gc_msgs/PoseMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'PoseMsg)))
  "Returns string type for a message object of type 'PoseMsg"
  "gc_msgs/PoseMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<PoseMsg>)))
  "Returns md5sum for a message object of type '<PoseMsg>"
  "85526f8f85d21474cf130f1420a62be2")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'PoseMsg)))
  "Returns md5sum for a message object of type 'PoseMsg"
  "85526f8f85d21474cf130f1420a62be2")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<PoseMsg>)))
  "Returns full string definition for message of type '<PoseMsg>"
  (cl:format cl:nil "float64 xPosition~%float64 yPosition~%float64 angle~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'PoseMsg)))
  "Returns full string definition for message of type 'PoseMsg"
  (cl:format cl:nil "float64 xPosition~%float64 yPosition~%float64 angle~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <PoseMsg>))
  (cl:+ 0
     8
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <PoseMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'PoseMsg
    (cl:cons ':xPosition (xPosition msg))
    (cl:cons ':yPosition (yPosition msg))
    (cl:cons ':angle (angle msg))
))
