; Auto-generated. Do not edit!


(cl:in-package gc_msgs-msg)


;//! \htmlinclude BumpStatusMsg.msg.html

(cl:defclass <BumpStatusMsg> (roslisp-msg-protocol:ros-message)
  ((bumpStatus
    :reader bumpStatus
    :initarg :bumpStatus
    :type (cl:vector cl:boolean)
   :initform (cl:make-array 0 :element-type 'cl:boolean :initial-element cl:nil)))
)

(cl:defclass BumpStatusMsg (<BumpStatusMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <BumpStatusMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'BumpStatusMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name gc_msgs-msg:<BumpStatusMsg> is deprecated: use gc_msgs-msg:BumpStatusMsg instead.")))

(cl:ensure-generic-function 'bumpStatus-val :lambda-list '(m))
(cl:defmethod bumpStatus-val ((m <BumpStatusMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader gc_msgs-msg:bumpStatus-val is deprecated.  Use gc_msgs-msg:bumpStatus instead.")
  (bumpStatus m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <BumpStatusMsg>) ostream)
  "Serializes a message object of type '<BumpStatusMsg>"
  (cl:let ((__ros_arr_len (cl:length (cl:slot-value msg 'bumpStatus))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) __ros_arr_len) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) __ros_arr_len) ostream))
  (cl:map cl:nil #'(cl:lambda (ele) (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if ele 1 0)) ostream))
   (cl:slot-value msg 'bumpStatus))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <BumpStatusMsg>) istream)
  "Deserializes a message object of type '<BumpStatusMsg>"
  (cl:let ((__ros_arr_len 0))
    (cl:setf (cl:ldb (cl:byte 8 0) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 8) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 16) __ros_arr_len) (cl:read-byte istream))
    (cl:setf (cl:ldb (cl:byte 8 24) __ros_arr_len) (cl:read-byte istream))
  (cl:setf (cl:slot-value msg 'bumpStatus) (cl:make-array __ros_arr_len))
  (cl:let ((vals (cl:slot-value msg 'bumpStatus)))
    (cl:dotimes (i __ros_arr_len)
    (cl:setf (cl:aref vals i) (cl:not (cl:zerop (cl:read-byte istream)))))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<BumpStatusMsg>)))
  "Returns string type for a message object of type '<BumpStatusMsg>"
  "gc_msgs/BumpStatusMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'BumpStatusMsg)))
  "Returns string type for a message object of type 'BumpStatusMsg"
  "gc_msgs/BumpStatusMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<BumpStatusMsg>)))
  "Returns md5sum for a message object of type '<BumpStatusMsg>"
  "205b9bdb16cbe80c79e877b549b1d588")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'BumpStatusMsg)))
  "Returns md5sum for a message object of type 'BumpStatusMsg"
  "205b9bdb16cbe80c79e877b549b1d588")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<BumpStatusMsg>)))
  "Returns full string definition for message of type '<BumpStatusMsg>"
  (cl:format cl:nil "bool[] bumpStatus~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'BumpStatusMsg)))
  "Returns full string definition for message of type 'BumpStatusMsg"
  (cl:format cl:nil "bool[] bumpStatus~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <BumpStatusMsg>))
  (cl:+ 0
     4 (cl:reduce #'cl:+ (cl:slot-value msg 'bumpStatus) :key #'(cl:lambda (ele) (cl:declare (cl:ignorable ele)) (cl:+ 1)))
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <BumpStatusMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'BumpStatusMsg
    (cl:cons ':bumpStatus (bumpStatus msg))
))
