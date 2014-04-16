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
 * Auto-generated by genmsg_cpp from file /home/rss-student/rss-2014-team-3/src/gc_msgs/msg/MotorCommandMsg.msg
 *
 */


#ifndef GC_MSGS_MESSAGE_MOTORCOMMANDMSG_H
#define GC_MSGS_MESSAGE_MOTORCOMMANDMSG_H


#include <string>
#include <vector>
#include <map>

#include <ros/types.h>
#include <ros/serialization.h>
#include <ros/builtin_message_traits.h>
#include <ros/message_operations.h>


namespace gc_msgs
{
template <class ContainerAllocator>
struct MotorCommandMsg_
{
  typedef MotorCommandMsg_<ContainerAllocator> Type;

  MotorCommandMsg_()
    : PWM(0)
    , motorType(0)  {
    }
  MotorCommandMsg_(const ContainerAllocator& _alloc)
    : PWM(0)
    , motorType(0)  {
    }



   typedef uint8_t _PWM_type;
  _PWM_type PWM;

   typedef int16_t _motorType_type;
  _motorType_type motorType;




  typedef boost::shared_ptr< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> > Ptr;
  typedef boost::shared_ptr< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> const> ConstPtr;
  boost::shared_ptr<std::map<std::string, std::string> > __connection_header;

}; // struct MotorCommandMsg_

typedef ::gc_msgs::MotorCommandMsg_<std::allocator<void> > MotorCommandMsg;

typedef boost::shared_ptr< ::gc_msgs::MotorCommandMsg > MotorCommandMsgPtr;
typedef boost::shared_ptr< ::gc_msgs::MotorCommandMsg const> MotorCommandMsgConstPtr;

// constants requiring out of line definition



template<typename ContainerAllocator>
std::ostream& operator<<(std::ostream& s, const ::gc_msgs::MotorCommandMsg_<ContainerAllocator> & v)
{
ros::message_operations::Printer< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >::stream(s, "", v);
return s;
}

} // namespace gc_msgs

namespace ros
{
namespace message_traits
{



// BOOLTRAITS {'IsFixedSize': True, 'IsMessage': True, 'HasHeader': False}
// {'std_msgs': ['/opt/ros/hydro/share/std_msgs/cmake/../msg'], 'gc_msgs': ['/home/rss-student/rss-2014-team-3/src/gc_msgs/msg']}

// !!!!!!!!!!! ['__class__', '__delattr__', '__dict__', '__doc__', '__eq__', '__format__', '__getattribute__', '__hash__', '__init__', '__module__', '__ne__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_parsed_fields', 'constants', 'fields', 'full_name', 'has_header', 'header_present', 'names', 'package', 'parsed_fields', 'short_name', 'text', 'types']




template <class ContainerAllocator>
struct IsFixedSize< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct IsFixedSize< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> const>
  : TrueType
  { };

template <class ContainerAllocator>
struct IsMessage< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct IsMessage< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> const>
  : TrueType
  { };

template <class ContainerAllocator>
struct HasHeader< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
  : FalseType
  { };

template <class ContainerAllocator>
struct HasHeader< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> const>
  : FalseType
  { };


template<class ContainerAllocator>
struct MD5Sum< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "027aab058e5ef57dcd79df8bbee4d54d";
  }

  static const char* value(const ::gc_msgs::MotorCommandMsg_<ContainerAllocator>&) { return value(); }
  static const uint64_t static_value1 = 0x027aab058e5ef57dULL;
  static const uint64_t static_value2 = 0xcd79df8bbee4d54dULL;
};

template<class ContainerAllocator>
struct DataType< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "gc_msgs/MotorCommandMsg";
  }

  static const char* value(const ::gc_msgs::MotorCommandMsg_<ContainerAllocator>&) { return value(); }
};

template<class ContainerAllocator>
struct Definition< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "uint8 PWM\n\
int16 motorType\n\
";
  }

  static const char* value(const ::gc_msgs::MotorCommandMsg_<ContainerAllocator>&) { return value(); }
};

} // namespace message_traits
} // namespace ros

namespace ros
{
namespace serialization
{

  template<class ContainerAllocator> struct Serializer< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
  {
    template<typename Stream, typename T> inline static void allInOne(Stream& stream, T m)
    {
      stream.next(m.PWM);
      stream.next(m.motorType);
    }

    ROS_DECLARE_ALLINONE_SERIALIZER;
  }; // struct MotorCommandMsg_

} // namespace serialization
} // namespace ros

namespace ros
{
namespace message_operations
{

template<class ContainerAllocator>
struct Printer< ::gc_msgs::MotorCommandMsg_<ContainerAllocator> >
{
  template<typename Stream> static void stream(Stream& s, const std::string& indent, const ::gc_msgs::MotorCommandMsg_<ContainerAllocator>& v)
  {
    s << indent << "PWM: ";
    Printer<uint8_t>::stream(s, indent + "  ", v.PWM);
    s << indent << "motorType: ";
    Printer<int16_t>::stream(s, indent + "  ", v.motorType);
  }
};

} // namespace message_operations
} // namespace ros

#endif // GC_MSGS_MESSAGE_MOTORCOMMANDMSG_H
