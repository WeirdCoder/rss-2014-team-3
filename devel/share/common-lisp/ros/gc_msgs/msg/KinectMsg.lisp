; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude KinectMsg.msg.html

(cl:defclass <KinectMsg> (roslisp-msg-protocol:ros-message)
  ((blockHeading
    :reader blockHeading
    :initarg :blockHeading
    :type cl:float
    :initform 0.0)
   (blockSeen
    :reader blockSeen
    :initarg :blockSeen
    :type cl:boolean
    :initform cl:nil)
   (capture
    :reader capture
    :initarg :capture
    :type cl:boolean
    :initform cl:nil))
)

(cl:defclass KinectMsg (<KinectMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <KinectMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'KinectMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<KinectMsg> is deprecated: use gc_msgs-msg:KinectMsg instead.")))

(cl:ensure-generic-function 'blockHeading-val :lambda-list '(m))
(cl:defmethod blockHeading-val ((m <KinectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:blockHeading-val is deprecated.  Use gc_msgs-msg:blockHeading instead.")
  (blockHeading m))

(cl:ensure-generic-function 'blockSeen-val :lambda-list '(m))
(cl:defmethod blockSeen-val ((m <KinectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:blockSeen-val is deprecated.  Use gc_msgs-msg:blockSeen instead.")
  (blockSeen m))

(cl:ensure-generic-function 'capture-val :lambda-list '(m))
(cl:defmethod capture-val ((m <KinectMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:capture-val is deprecated.  Use gc_msgs-msg:capture instead.")
  (capture m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <KinectMsg>) ostream)
  "Serializes a message object of type '<KinectMsg>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'blockHeading))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'blockSeen) 1 0)) ostream)
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'capture) 1 0)) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <KinectMsg>) istream)
  "Deserializes a message object of type '<KinectMsg>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'blockHeading) (roslisp-utils:decode-double-float-bits bits)))
    (cl:setf (cl:slot-value msg 'blockSeen) (cl:not (cl:zerop (cl:read-byte istream))))
    (cl:setf (cl:slot-value msg 'capture) (cl:not (cl:zerop (cl:read-byte istream))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<KinectMsg>)))
  "Returns string type for a message object of type '<KinectMsg>"
  "gc_msgs/KinectMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'KinectMsg)))
  "Returns string type for a message object of type 'KinectMsg"
  "gc_msgs/KinectMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<KinectMsg>)))
  "Returns md5sum for a message object of type '<KinectMsg>"
  "1438d4f66ae39d8bbd549672f58db83f")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'KinectMsg)))
  "Returns md5sum for a message object of type 'KinectMsg"
  "1438d4f66ae39d8bbd549672f58db83f")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<KinectMsg>)))
  "Returns full string definition for message of type '<KinectMsg>"
  (cl:format cl:nil "float64 blockHeading~%bool blockSeen~%bool capture~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'KinectMsg)))
  "Returns full string definition for message of type 'KinectMsg"
  (cl:format cl:nil "float64 blockHeading~%bool blockSeen~%bool capture~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <KinectMsg>))
  (cl:+ 0
     8
     1
     1
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <KinectMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'KinectMsg
    (cl:cons ':blockHeading (blockHeading msg))
    (cl:cons ':blockSeen (blockSeen msg))
    (cl:cons ':capture (capture msg))
))
