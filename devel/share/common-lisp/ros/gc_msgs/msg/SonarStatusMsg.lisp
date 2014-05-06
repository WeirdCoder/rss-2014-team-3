; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude SonarStatusMsg.msg.html

(cl:defclass <SonarStatusMsg> (roslisp-msg-protocol:ros-message)
  ((sonarStatus
    :reader sonarStatus
    :initarg :sonarStatus
    :type (cl:vector cl:float)
   :initform (cl:make-array 0 :element-type 'cl:float :initial-element 0.0)))
)

(cl:defclass SonarStatusMsg (<SonarStatusMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <SonarStatusMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'SonarStatusMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<SonarStatusMsg> is deprecated: use gc_msgs-msg:SonarStatusMsg instead.")))

(cl:ensure-generic-function 'sonarStatus-val :lambda-list '(m))
(cl:defmethod sonarStatus-val ((m <SonarStatusMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:sonarStatus-val is deprecated.  Use gc_msgs-msg:sonarStatus instead.")
  (sonarStatus m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <SonarStatusMsg>) ostream)
  "Serializes a message object of type '<SonarStatusMsg>"
  (cl:let ((__ros_arr_len (cl:length (cl:slot-value msg 'sonarStatus))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) __ros_arr_len) ostream))
  (cl:map cl:nil #'(cl:lambda (ele) (cl:let ((bits (roslisp-utils:encode-double-float-bits ele)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream)))
   (cl:slot-value msg 'sonarStatus))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <SonarStatusMsg>) istream)
  "Deserializes a message object of type '<SonarStatusMsg>"
  (cl:let ((__ros_arr_len 0))
    (cl:setf (cl:ldb (cl:byte 8 0) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 8) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 16) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 24) __ros_arr_len) (cl:read-byte istream))
  (cl:setf (cl:slot-value msg 'sonarStatus) (cl:make-array __ros_arr_len))
  (cl:let ((vals (cl:slot-value msg 'sonarStatus)))
    (cl:dotimes (i __ros_arr_len)
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:aref vals i) (roslisp-utils:decode-double-float-bits bits))))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<SonarStatusMsg>)))
  "Returns string type for a message object of type '<SonarStatusMsg>"
  "gc_msgs/SonarStatusMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'SonarStatusMsg)))
  "Returns string type for a message object of type 'SonarStatusMsg"
  "gc_msgs/SonarStatusMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<SonarStatusMsg>)))
  "Returns md5sum for a message object of type '<SonarStatusMsg>"
  "52ba6afcf7d449b1d16c7d561ad4ec82")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'SonarStatusMsg)))
  "Returns md5sum for a message object of type 'SonarStatusMsg"
  "52ba6afcf7d449b1d16c7d561ad4ec82")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<SonarStatusMsg>)))
  "Returns full string definition for message of type '<SonarStatusMsg>"
  (cl:format cl:nil "float64[] sonarStatus~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'SonarStatusMsg)))
  "Returns full string definition for message of type 'SonarStatusMsg"
  (cl:format cl:nil "float64[] sonarStatus~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <SonarStatusMsg>))
  (cl:+ 0
     4 (cl:reduce #'cl:+ (cl:slot-value msg 'sonarStatus) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 8)))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <SonarStatusMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'SonarStatusMsg
    (cl:cons ':sonarStatus (sonarStatus msg))
))
