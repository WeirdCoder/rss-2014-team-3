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

