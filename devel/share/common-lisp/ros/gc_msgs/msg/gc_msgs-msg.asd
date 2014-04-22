
(cl:in-package :asdf)

(defsystem "gc_msgs-msg"
  :depends-on (:roslisp-msg-protocol :roslisp-utils )
  :components ((:file "_package")
    (:file "MotorCommandMsg" :depends-on ("_package_MotorCommandMsg"))
    (:file "_package_MotorCommandMsg" :depends-on ("_package"))
    (:file "PoseMsg" :depends-on ("_package_PoseMsg"))
    (:file "_package_PoseMsg" :depends-on ("_package"))
    (:file "BumpMsg" :depends-on ("_package_BumpMsg"))
    (:file "_package_BumpMsg" :depends-on ("_package"))
    (:file "EncoderMsg" :depends-on ("_package_EncoderMsg"))
    (:file "_package_EncoderMsg" :depends-on ("_package"))
    (:file "MotionMsg" :depends-on ("_package_MotionMsg"))
    (:file "_package_MotionMsg" :depends-on ("_package"))
    (:file "WheelVelocityMsg" :depends-on ("_package_WheelVelocityMsg"))
    (:file "_package_WheelVelocityMsg" :depends-on ("_package"))
  ))