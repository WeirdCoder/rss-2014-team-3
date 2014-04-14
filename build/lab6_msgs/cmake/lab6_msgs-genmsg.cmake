# generated from genmsg/cmake/pkg-genmsg.cmake.em

message(STATUS "lab6_msgs: 2 messages, 0 services")

set(MSG_I_FLAGS "-Ilab6_msgs:/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg;-Ilab5_msgs:/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg;-Istd_msgs:/opt/ros/hydro/share/std_msgs/cmake/../msg")

# Find all generators
find_package(gencpp REQUIRED)
find_package(genlisp REQUIRED)
find_package(genpy REQUIRED)

add_custom_target(lab6_msgs_generate_messages ALL)

#
#  langs = gencpp;genlisp;genpy
#

### Section generating for lang: gencpp
### Generating Messages
_generate_msg_cpp(lab6_msgs
  "/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIRectMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/lab6_msgs
)
_generate_msg_cpp(lab6_msgs
  "/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIPolyMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/lab6_msgs
)

### Generating Services

### Generating Module File
_generate_module_cpp(lab6_msgs
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/lab6_msgs
  "${ALL_GEN_OUTPUT_FILES_cpp}"
)

add_custom_target(lab6_msgs_generate_messages_cpp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_cpp}
)
add_dependencies(lab6_msgs_generate_messages lab6_msgs_generate_messages_cpp)

# target for backward compatibility
add_custom_target(lab6_msgs_gencpp)
add_dependencies(lab6_msgs_gencpp lab6_msgs_generate_messages_cpp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS lab6_msgs_generate_messages_cpp)

### Section generating for lang: genlisp
### Generating Messages
_generate_msg_lisp(lab6_msgs
  "/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIRectMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/lab6_msgs
)
_generate_msg_lisp(lab6_msgs
  "/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIPolyMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/lab6_msgs
)

### Generating Services

### Generating Module File
_generate_module_lisp(lab6_msgs
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/lab6_msgs
  "${ALL_GEN_OUTPUT_FILES_lisp}"
)

add_custom_target(lab6_msgs_generate_messages_lisp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_lisp}
)
add_dependencies(lab6_msgs_generate_messages lab6_msgs_generate_messages_lisp)

# target for backward compatibility
add_custom_target(lab6_msgs_genlisp)
add_dependencies(lab6_msgs_genlisp lab6_msgs_generate_messages_lisp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS lab6_msgs_generate_messages_lisp)

### Section generating for lang: genpy
### Generating Messages
_generate_msg_py(lab6_msgs
  "/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIRectMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/lab6_msgs
)
_generate_msg_py(lab6_msgs
  "/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIPolyMsg.msg"
  "${MSG_I_FLAGS}"
  "/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg/ColorMsg.msg"
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/lab6_msgs
)

### Generating Services

### Generating Module File
_generate_module_py(lab6_msgs
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/lab6_msgs
  "${ALL_GEN_OUTPUT_FILES_py}"
)

add_custom_target(lab6_msgs_generate_messages_py
  DEPENDS ${ALL_GEN_OUTPUT_FILES_py}
)
add_dependencies(lab6_msgs_generate_messages lab6_msgs_generate_messages_py)

# target for backward compatibility
add_custom_target(lab6_msgs_genpy)
add_dependencies(lab6_msgs_genpy lab6_msgs_generate_messages_py)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS lab6_msgs_generate_messages_py)



if(gencpp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/lab6_msgs)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/lab6_msgs
    DESTINATION ${gencpp_INSTALL_DIR}
  )
endif()
add_dependencies(lab6_msgs_generate_messages_cpp lab5_msgs_generate_messages_cpp)

if(genlisp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/lab6_msgs)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/lab6_msgs
    DESTINATION ${genlisp_INSTALL_DIR}
  )
endif()
add_dependencies(lab6_msgs_generate_messages_lisp lab5_msgs_generate_messages_lisp)

if(genpy_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/lab6_msgs)
  install(CODE "execute_process(COMMAND \"/usr/bin/python\" -m compileall \"${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/lab6_msgs\")")
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/lab6_msgs
    DESTINATION ${genpy_INSTALL_DIR}
  )
endif()
add_dependencies(lab6_msgs_generate_messages_py lab5_msgs_generate_messages_py)
