# generated from genmsg/cmake/pkg-genmsg.cmake.em

message(STATUS "gc_msgs: 20 messages, 0 services")

set(MSG_I_FLAGS "-Igc_msgs:/home/rss-student/rss-2014-team-3/src/gc_msgs/msg;-Istd_msgs:/opt/ros/hydro/share/std_msgs/cmake/../msg;-Ilab6_msgs:/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg;-Ilab5_msgs:/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg")

# Find all generators
find_package(gencpp REQUIRED)
find_package(genlisp REQUIRED)
find_package(genpy REQUIRED)

add_custom_target(gc_msgs_generate_messages ALL)

#
#  langs = gencpp;genlisp;genpy
#

### Section generating for lang: gencpp
### Generating Messages
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/EncoderMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionDistMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelVelocityMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPolyMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/StateMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleAheadMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/HamperMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionVoltMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/PoseMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/KinectMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GCServoMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ConveyorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/SonarStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPointMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelErrorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)
_generate_msg_cpp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
)

### Generating Services

### Generating Module File
_generate_module_cpp(gc_msgs
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
  "${ALL_GEN_OUTPUT_FILES_cpp}"
)

add_custom_target(gc_msgs_generate_messages_cpp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_cpp}
)
add_dependencies(gc_msgs_generate_messages gc_msgs_generate_messages_cpp)

# target for backward compatibility
add_custom_target(gc_msgs_gencpp)
add_dependencies(gc_msgs_gencpp gc_msgs_generate_messages_cpp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS gc_msgs_generate_messages_cpp)

### Section generating for lang: genlisp
### Generating Messages
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/EncoderMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionDistMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelVelocityMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPolyMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/StateMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleAheadMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/HamperMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionVoltMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/PoseMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/KinectMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GCServoMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ConveyorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/SonarStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPointMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelErrorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)
_generate_msg_lisp(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
)

### Generating Services

### Generating Module File
_generate_module_lisp(gc_msgs
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
  "${ALL_GEN_OUTPUT_FILES_lisp}"
)

add_custom_target(gc_msgs_generate_messages_lisp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_lisp}
)
add_dependencies(gc_msgs_generate_messages gc_msgs_generate_messages_lisp)

# target for backward compatibility
add_custom_target(gc_msgs_genlisp)
add_dependencies(gc_msgs_genlisp gc_msgs_generate_messages_lisp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS gc_msgs_generate_messages_lisp)

### Section generating for lang: genpy
### Generating Messages
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/EncoderMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionDistMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelVelocityMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPolyMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/StateMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleAheadMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/HamperMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionVoltMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/PoseMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/KinectMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GCServoMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ConveyorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/SonarStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPointMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelErrorMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)
_generate_msg_py(gc_msgs
  "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
)

### Generating Services

### Generating Module File
_generate_module_py(gc_msgs
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
  "${ALL_GEN_OUTPUT_FILES_py}"
)

add_custom_target(gc_msgs_generate_messages_py
  DEPENDS ${ALL_GEN_OUTPUT_FILES_py}
)
add_dependencies(gc_msgs_generate_messages gc_msgs_generate_messages_py)

# target for backward compatibility
add_custom_target(gc_msgs_genpy)
add_dependencies(gc_msgs_genpy gc_msgs_generate_messages_py)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS gc_msgs_generate_messages_py)



if(gencpp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/gc_msgs
    DESTINATION ${gencpp_INSTALL_DIR}
  )
endif()
add_dependencies(gc_msgs_generate_messages_cpp std_msgs_generate_messages_cpp)
add_dependencies(gc_msgs_generate_messages_cpp lab6_msgs_generate_messages_cpp)

if(genlisp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/gc_msgs
    DESTINATION ${genlisp_INSTALL_DIR}
  )
endif()
add_dependencies(gc_msgs_generate_messages_lisp std_msgs_generate_messages_lisp)
add_dependencies(gc_msgs_generate_messages_lisp lab6_msgs_generate_messages_lisp)

if(genpy_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs)
  install(CODE "execute_process(COMMAND \"/usr/bin/python\" -m compileall \"${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs\")")
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/gc_msgs
    DESTINATION ${genpy_INSTALL_DIR}
  )
endif()
add_dependencies(gc_msgs_generate_messages_py std_msgs_generate_messages_py)
add_dependencies(gc_msgs_generate_messages_py lab6_msgs_generate_messages_py)
