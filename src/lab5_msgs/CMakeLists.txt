cmake_minimum_required(VERSION 2.8.3)
project(lab5_msgs)

# build-time dependencies
find_package(catkin REQUIRED rosjava_build_tools)
find_package(catkin REQUIRED COMPONENTS message_generation std_msgs)

#message files to build
add_message_files( FILES
  ColorMsg.msg
  GUIEraseMsg.msg
  GUILineMsg.msg
  GUIPointMsg.msg
  GUISegmentMsg.msg  
)

#generated the message with declared message dependencies.
generate_messages( DEPENDENCIES
   std_msgs
)

# Runtime dependencies
catkin_package(
   CATKIN_DEPENDS message_runtime std_msgs
)

