#!/bin/sh -x

if [ -n "$DESTDIR" ] ; then
    case $DESTDIR in
        /*) # ok
            ;;
        *)
            /bin/echo "DESTDIR argument must be absolute... "
            /bin/echo "otherwise python's distutils will bork things."
            exit 1
    esac
    DESTDIR_ARG="--root=$DESTDIR"
fi

cd "/home/rss-student/rss-2014-team-3/src/hal"

# Note that PYTHONPATH is pulled from the environment to support installing
# into one location when some dependencies were installed in another
# location, #123.
/usr/bin/env \
    PYTHONPATH="/home/rss-student/rss-2014-team-3/install/lib/python2.7/dist-packages:/home/rss-student/rss-2014-team-3/build/lib/python2.7/dist-packages:$PYTHONPATH" \
    CATKIN_BINARY_DIR="/home/rss-student/rss-2014-team-3/build" \
    "/usr/bin/python" \
    "/home/rss-student/rss-2014-team-3/src/hal/setup.py" \
    build --build-base "/home/rss-student/rss-2014-team-3/build/hal" \
    install \
    $DESTDIR_ARG \
    --install-layout=deb --prefix="/home/rss-student/rss-2014-team-3/install" --install-scripts="/home/rss-student/rss-2014-team-3/install/bin"
