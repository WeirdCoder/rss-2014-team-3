# generated from genmsg/cmake/pkg-genmsg.cmake.em

message(STATUS "rss_msgs: 10 messages, 0 services")

set(MSG_I_FLAGS "-Irss_msgs:/home/rss-student/rss-2014-team-3/src/rss_msgs/msg;-Istd_msgs:/opt/ros/hydro/share/std_msgs/cmake/../msg")

# Find all generators
find_package(gencpp REQUIRED)
find_package(genlisp REQUIRED)
find_package(genpy REQUIRED)

add_custom_target(rss_msgs_generate_messages ALL)

#
#  langs = gencpp;genlisp;genpy
#

### Section generating for lang: gencpp
### Generating Messages
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/AnalogStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/ArmMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/OdometryMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/SonarMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/EncoderMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/BreakBeamMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/DigitalStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/MotionMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/ResetMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)
_generate_msg_cpp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/BumpMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
)

### Generating Services

### Generating Module File
_generate_module_cpp(rss_msgs
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
  "${ALL_GEN_OUTPUT_FILES_cpp}"
)

add_custom_target(rss_msgs_generate_messages_cpp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_cpp}
)
add_dependencies(rss_msgs_generate_messages rss_msgs_generate_messages_cpp)

# target for backward compatibility
add_custom_target(rss_msgs_gencpp)
add_dependencies(rss_msgs_gencpp rss_msgs_generate_messages_cpp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS rss_msgs_generate_messages_cpp)

### Section generating for lang: genlisp
### Generating Messages
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/AnalogStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/ArmMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/OdometryMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/SonarMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/EncoderMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/BreakBeamMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/DigitalStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/MotionMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/ResetMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)
_generate_msg_lisp(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/BumpMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
)

### Generating Services

### Generating Module File
_generate_module_lisp(rss_msgs
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
  "${ALL_GEN_OUTPUT_FILES_lisp}"
)

add_custom_target(rss_msgs_generate_messages_lisp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_lisp}
)
add_dependencies(rss_msgs_generate_messages rss_msgs_generate_messages_lisp)

# target for backward compatibility
add_custom_target(rss_msgs_genlisp)
add_dependencies(rss_msgs_genlisp rss_msgs_generate_messages_lisp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS rss_msgs_generate_messages_lisp)

### Section generating for lang: genpy
### Generating Messages
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/AnalogStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/ArmMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/OdometryMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/SonarMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/EncoderMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/BreakBeamMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/DigitalStatusMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/MotionMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/ResetMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)
_generate_msg_py(rss_msgs
  "/home/rss-student/rss-2014-team-3/src/rss_msgs/msg/BumpMsg.msg"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
)

### Generating Services

### Generating Module File
_generate_module_py(rss_msgs
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
  "${ALL_GEN_OUTPUT_FILES_py}"
)

add_custom_target(rss_msgs_generate_messages_py
  DEPENDS ${ALL_GEN_OUTPUT_FILES_py}
)
add_dependencies(rss_msgs_generate_messages rss_msgs_generate_messages_py)

# target for backward compatibility
add_custom_target(rss_msgs_genpy)
add_dependencies(rss_msgs_genpy rss_msgs_generate_messages_py)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS rss_msgs_generate_messages_py)



if(gencpp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/rss_msgs
    DESTINATION ${gencpp_INSTALL_DIR}
  )
endif()
add_dependencies(rss_msgs_generate_messages_cpp std_msgs_generate_messages_cpp)

if(genlisp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/rss_msgs
    DESTINATION ${genlisp_INSTALL_DIR}
  )
endif()
add_dependencies(rss_msgs_generate_messages_lisp std_msgs_generate_messages_lisp)

if(genpy_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs)
  install(CODE "execute_process(COMMAND \"/usr/bin/python\" -m compileall \"${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs\")")
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/rss_msgs
    DESTINATION ${genpy_INSTALL_DIR}
  )
endif()
add_dependencies(rss_msgs_generate_messages_py std_msgs_generate_messages_py)
