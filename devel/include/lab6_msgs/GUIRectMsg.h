/* Software License Agreement (BSD License)
 *
 * Copyright (c) 2011, Willow Garage, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 *  * Neither the name of Willow Garage, Inc. nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Auto-generated by genmsg_cpp from file /home/rss-student/rss-2014-team-3/src/lab6_msgs/msg/GUIRectMsg.msg
 *
 */


#ifndef LAB6_MSGS_MESSAGE_GUIRECTMSG_H
#define LAB6_MSGS_MESSAGE_GUIRECTMSG_H


#include <string>
#include <vector>
#include <map>

#include <ros/types.h>
#include <ros/serialization.h>
#include <ros/builtin_message_traits.h>
#include <ros/message_operations.h>

#include <lab5_msgs/ColorMsg.h>

namespace lab6_msgs
{
template <class ContainerAllocator>
struct GUIRectMsg_
{
  typedef GUIRectMsg_<ContainerAllocator> Type;

  GUIRectMsg_()
    : c()
    , x(0.0)
    , y(0.0)
    , width(0.0)
    , height(0.0)
    , filled(0)  {
    }
  GUIRectMsg_(const ContainerAllocator& _alloc)
    : c(_alloc)
    , x(0.0)
    , y(0.0)
    , width(0.0)
    , height(0.0)
    , filled(0)  {
    }



   typedef  ::lab5_msgs::ColorMsg_<ContainerAllocator>  _c_type;
  _c_type c;

   typedef float _x_type;
  _x_type x;

   typedef float _y_type;
  _y_type y;

   typedef float _width_type;
  _width_type width;

   typedef float _height_type;
  _height_type height;

   typedef int32_t _filled_type;
  _filled_type filled;




  typedef boost::shared_ptr< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> > Ptr;
  typedef boost::shared_ptr< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> const> ConstPtr;
  boost::shared_ptr<std::map<std::string, std::string> > __connection_header;

}; // struct GUIRectMsg_

typedef ::lab6_msgs::GUIRectMsg_<std::allocator<void> > GUIRectMsg;

typedef boost::shared_ptr< ::lab6_msgs::GUIRectMsg > GUIRectMsgPtr;
typedef boost::shared_ptr< ::lab6_msgs::GUIRectMsg const> GUIRectMsgConstPtr;

// constants requiring out of line definition



template<typename ContainerAllocator>
std::ostream& operator<<(std::ostream& s, const ::lab6_msgs::GUIRectMsg_<ContainerAllocator> & v)
{
ros::message_operations::Printer< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >::stream(s, "", v);
return s;
}

} // namespace lab6_msgs

namespace ros
{
namespace message_traits
{



// BOOLTRAITS {'IsFixedSize': True, 'IsMessage': True, 'HasHeader': False}
// {'std_msgs': ['/opt/ros/hydro/share/std_msgs/cmake/../msg'], 'lab5_msgs': ['/home/rss-student/rss-2014-team-3/src/lab5_msgs/msg'], 'lab6_msgs': ['/home/rss-student/rss-2014-team-3/src/lab6_msgs/msg']}

// !!!!!!!!!!! ['__class__', '__delattr__', '__dict__', '__doc__', '__eq__', '__format__', '__getattribute__', '__hash__', '__init__', '__module__', '__ne__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_parsed_fields', 'constants', 'fields', 'full_name', 'has_header', 'header_present', 'names', 'package', 'parsed_fields', 'short_name', 'text', 'types']




template <class ContainerAllocator>
struct IsFixedSize< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct IsFixedSize< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> const>
  : TrueType
  { };

template <class ContainerAllocator>
struct IsMessage< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct IsMessage< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> const>
  : TrueType
  { };

template <class ContainerAllocator>
struct HasHeader< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
  : FalseType
  { };

template <class ContainerAllocator>
struct HasHeader< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> const>
  : FalseType
  { };


template<class ContainerAllocator>
struct MD5Sum< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "c0bf17dcdde695b717319df27b23e5f8";
  }

  static const char* value(const ::lab6_msgs::GUIRectMsg_<ContainerAllocator>&) { return value(); }
  static const uint64_t static_value1 = 0xc0bf17dcdde695b7ULL;
  static const uint64_t static_value2 = 0x17319df27b23e5f8ULL;
};

template<class ContainerAllocator>
struct DataType< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "lab6_msgs/GUIRectMsg";
  }

  static const char* value(const ::lab6_msgs::GUIRectMsg_<ContainerAllocator>&) { return value(); }
};

template<class ContainerAllocator>
struct Definition< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "lab5_msgs/ColorMsg c\n\
float32 x\n\
float32 y\n\
float32 width\n\
float32 height\n\
int32 filled\n\
================================================================================\n\
MSG: lab5_msgs/ColorMsg\n\
int64 r\n\
int64 g\n\
int64 b\n\
";
  }

  static const char* value(const ::lab6_msgs::GUIRectMsg_<ContainerAllocator>&) { return value(); }
};

} // namespace message_traits
} // namespace ros

namespace ros
{
namespace serialization
{

  template<class ContainerAllocator> struct Serializer< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
  {
    template<typename Stream, typename T> inline static void allInOne(Stream& stream, T m)
    {
      stream.next(m.c);
      stream.next(m.x);
      stream.next(m.y);
      stream.next(m.width);
      stream.next(m.height);
      stream.next(m.filled);
    }

    ROS_DECLARE_ALLINONE_SERIALIZER;
  }; // struct GUIRectMsg_

} // namespace serialization
} // namespace ros

namespace ros
{
namespace message_operations
{

template<class ContainerAllocator>
struct Printer< ::lab6_msgs::GUIRectMsg_<ContainerAllocator> >
{
  template<typename Stream> static void stream(Stream& s, const std::string& indent, const ::lab6_msgs::GUIRectMsg_<ContainerAllocator>& v)
  {
    s << indent << "c: ";
    s << std::endl;
    Printer< ::lab5_msgs::ColorMsg_<ContainerAllocator> >::stream(s, indent + "  ", v.c);
    s << indent << "x: ";
    Printer<float>::stream(s, indent + "  ", v.x);
    s << indent << "y: ";
    Printer<float>::stream(s, indent + "  ", v.y);
    s << indent << "width: ";
    Printer<float>::stream(s, indent + "  ", v.width);
    s << indent << "height: ";
    Printer<float>::stream(s, indent + "  ", v.height);
    s << indent << "filled: ";
    Printer<int32_t>::stream(s, indent + "  ", v.filled);
  }
};

} // namespace message_operations
} // namespace ros

#endif // LAB6_MSGS_MESSAGE_GUIRECTMSG_H