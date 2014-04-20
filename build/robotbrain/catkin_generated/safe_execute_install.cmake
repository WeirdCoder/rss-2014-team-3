execute_process(COMMAND "/home/rss-student/rss-2014-team-3/build/robotbrain/catkin_generated/python_distutils_install.sh" RESULT_VARIABLE res)

if(NOT res EQUAL 0)
  message(FATAL_ERROR "execute_process(/home/rss-student/rss-2014-team-3/build/robotbrain/catkin_generated/python_distutils_install.sh) returned error code ")
endif()
