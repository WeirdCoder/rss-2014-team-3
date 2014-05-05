# Install script for directory: /home/rss-student/rss-2014-team-3/src/gc_msgs

# Set the install prefix
IF(NOT DEFINED CMAKE_INSTALL_PREFIX)
  SET(CMAKE_INSTALL_PREFIX "/home/rss-student/rss-2014-team-3/install")
ENDIF(NOT DEFINED CMAKE_INSTALL_PREFIX)
STRING(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
IF(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  IF(BUILD_TYPE)
    STRING(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  ELSE(BUILD_TYPE)
    SET(CMAKE_INSTALL_CONFIG_NAME "")
  ENDIF(BUILD_TYPE)
  MESSAGE(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
ENDIF(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)

# Set the component getting installed.
IF(NOT CMAKE_INSTALL_COMPONENT)
  IF(COMPONENT)
    MESSAGE(STATUS "Install component: \"${COMPONENT}\"")
    SET(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  ELSE(COMPONENT)
    SET(CMAKE_INSTALL_COMPONENT)
  ENDIF(COMPONENT)
ENDIF(NOT CMAKE_INSTALL_COMPONENT)

# Install shared libraries without execute permission?
IF(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  SET(CMAKE_INSTALL_SO_NO_EXE "1")
ENDIF(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/gc_msgs/msg" TYPE FILE FILES
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/BumpMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ConveyorMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ColorMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/EncoderMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GCServoMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPointMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/GUIPolyMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/HamperMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotionDistMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleAheadMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/ObstacleMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/PoseMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/StateMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelErrorMsg.msg"
    "/home/rss-student/rss-2014-team-3/src/gc_msgs/msg/WheelVelocityMsg.msg"
    )
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/gc_msgs/cmake" TYPE FILE FILES "/home/rss-student/rss-2014-team-3/build/gc_msgs/catkin_generated/installspace/gc_msgs-msg-paths.cmake")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include" TYPE DIRECTORY FILES "/home/rss-student/rss-2014-team-3/devel/include/gc_msgs")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/common-lisp/ros" TYPE DIRECTORY FILES "/home/rss-student/rss-2014-team-3/devel/share/common-lisp/ros/gc_msgs")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  execute_process(COMMAND "/usr/bin/python" -m compileall "/home/rss-student/rss-2014-team-3/devel/lib/python2.7/dist-packages/gc_msgs")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib/python2.7/dist-packages" TYPE DIRECTORY FILES "/home/rss-student/rss-2014-team-3/devel/lib/python2.7/dist-packages/gc_msgs")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib/pkgconfig" TYPE FILE FILES "/home/rss-student/rss-2014-team-3/build/gc_msgs/catkin_generated/installspace/gc_msgs.pc")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/gc_msgs/cmake" TYPE FILE FILES "/home/rss-student/rss-2014-team-3/build/gc_msgs/catkin_generated/installspace/gc_msgs-msg-extras.cmake")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/gc_msgs/cmake" TYPE FILE FILES
    "/home/rss-student/rss-2014-team-3/build/gc_msgs/catkin_generated/installspace/gc_msgsConfig.cmake"
    "/home/rss-student/rss-2014-team-3/build/gc_msgs/catkin_generated/installspace/gc_msgsConfig-version.cmake"
    )
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/gc_msgs" TYPE FILE FILES "/home/rss-student/rss-2014-team-3/src/gc_msgs/package.xml")
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

