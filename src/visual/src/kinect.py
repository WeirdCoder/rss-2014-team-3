#!/usr/bin/env python

""" cv_bridge_demo.py - Version 0.1 2011-05-29

    A ROS-to-OpenCV node that uses cv_bridge to map a ROS image topic and optionally a ROS
    depth image topic to the equivalent OpenCV image stream(s).
    
    Created for the Pi Robot Project: http://www.pirobot.org
    Copyright (c) 2011 Patrick Goebel.  All rights reserved.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details at:
    
    http://www.gnu.org/licenses/gpl.html
      
"""


import roslib; roslib.load_manifest('visual')
import rospy
import sys
import cv2
import cv2.cv as cv
from sensor_msgs.msg import Image, CameraInfo
#from gc_msgs.msg import PoseMsg # Comment this back in when it is part of the main repo
from cv_bridge import CvBridge, CvBridgeError
import numpy as np


class kinect():

    centroid_x = []
    centroid_y = []
    centroids = []
    depths = []
    blob_exists = False
    # Default resolution of the kinect cameras
    max_width = 640
    max_height = 480

    def __init__(self):
        self.node_name = "kinect"

       
        rospy.init_node(self.node_name)
        # What we do during shutdown
        rospy.on_shutdown(self.cleanup)
        
        # Create the OpenCV display window for the RGB image
        self.cv_window_name = self.node_name
        cv.NamedWindow(self.cv_window_name, cv.CV_WINDOW_AUTOSIZE)
        cv.MoveWindow(self.cv_window_name, 25, 75)
        
        # And one for the depth image
        cv.NamedWindow("Depth Image", cv.CV_WINDOW_AUTOSIZE)
        cv.MoveWindow("Depth Image", 25, 350)
        
        # Create the cv_bridge object
        self.bridge = CvBridge()
        
        # Subscribe to the camera image and depth topics and set
        # the appropriate callbacks
        self.image_sub = rospy.Subscriber("/camera/rgb/image_color", Image, self.image_callback)
        #self.depth_sub = rospy.Subscriber("/camera/depth/image_raw", Image, self.depth_callback)
        self.depth_sub = rospy.Subscriber("/camera/depth/image", Image, self.depth_callback)

        # The position publisher and subscriber.  The topic for position_sub should be 
        # what odometry or mapupdater is publishing to.
#        self.position_pub = rospy.Publisher("blockSeen", PoseMsg)
#        self.position_sub = rospy.Subscriber("blockSeen", PoseMsg, self.pose_callback)
        
        rospy.loginfo("Waiting for image topics...")

    def image_callback(self, ros_image):
        # Use cv_bridge() to convert the ROS image to OpenCV format
        try:
            frame = self.bridge.imgmsg_to_cv(ros_image, "bgr8")
        except CvBridgeError, e:
            print e
        
        # Convert the image to a Numpy array since most cv2 functions
        # require Numpy arrays.
        frame = np.array(frame, dtype=np.uint8)
        
        # Process the frame using the process_image() function
        display_image = self.process_image(frame)
                       
        # Display the image.
        cv2.imshow(self.node_name, display_image)
        
        # Process any keyboard commands
        self.keystroke = cv.WaitKey(5)
        if 32 <= self.keystroke and self.keystroke < 128:
            cc = chr(self.keystroke).lower()
            if cc == 'q':
                # The user has press the q key, so exit
                rospy.signal_shutdown("User hit q key to quit.")
                
    def depth_callback(self, ros_image):
        # Use cv_bridge() to convert the ROS image to OpenCV format
        try:
            # The depth image is a single-channel float32 image
            depth_image = self.bridge.imgmsg_to_cv(ros_image, "32FC1")
        except CvBridgeError, e:
            print e

        # Convert the depth image to a Numpy array since most cv2 functions
        # require Numpy arrays.
        depth_array = np.array(depth_image, dtype=np.float32)
                
        # Normalize the depth image to fall between 0 (black) and 1 (white)
        #cv2.normalize(depth_array, depth_array, 0, 1, cv2.NORM_MINMAX)
        
       # Process the depth image
        depth_display_image = self.process_depth_image(depth_array)
    
        # Display the result
        cv2.imshow("Depth Image", depth_display_image)
          
    # Publishes the coordinates of a block to topic blockSeen
    def pose_callback(self, pose):
        for i in range (0, len(centroids)):
            x = centroids[i][0] # horizontal pixels from edge of block
            d = depths[i] # depth of block
            x_view = horizontalPosition(x, d) # meters from center of robot's view 
            # dist = math.sqrt(d*d + x*x) # distance in meters from nose of robot to block
            theta = pose.angle # angle of robot in absolute (x,y) coordinate space
            x_displacement = d*math.cos(theta) + x_view*math.cos(3.1416/2 - theta)
            y_displacement = d*math.sin(theta) + x_view*math.sin(3.1416/2 - theta)
            msg = PoseMsg
            msg.xPosition = x_displacement
            msg.yPosition = y_displacement
            position_pub.publish(msg)
        
    def process_image(self, frame):
        # Gaussian blur the image
        blur = cv2.GaussianBlur(frame, (15,15), 0)
        # Create a greyscale image (for debugging purposes)
        grey = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        # Convert the blurred image from RGB to HSV
        hsv = cv2.cvtColor(blur, cv2.COLOR_BGR2HSV)
        
        # Color ranges in HSV
        lower_green = np.array([45,60,60], np.uint8)
        upper_green = np.array([75,255,255], np.uint8)
        
        lower_blue = np.array([20,60,60], np.uint8)
        upper_blue = np.array([40,255,255], np.uint8)

        # Masks.  Only pixels of the given color exist in the masks.
        mask_green = cv2.inRange(hsv, lower_green, upper_green)
        mask_blue = cv2.inRange(hsv, lower_blue, upper_blue)
        # Foreground_* and masked_image are used for display and debugging only. 
        foreground_green = cv2.bitwise_and(frame, frame, mask=mask_green)
        foreground_blue = cv2.bitwise_and(frame, frame, mask=mask_blue)
        backtorgb = cv2.cvtColor(grey,cv2.COLOR_GRAY2RGB)
        masked_image = cv2.add(backtorgb, foreground_green)
        masked_image = cv2.add(masked_image, foreground_blue)
       
        # Dilation fluffs out the image to fill in gaps and rough edges
        dilation = np.ones((7, 7), "uint8") # originally 15,15
        green_binary = cv2.dilate(mask_green, dilation)
        blue_binary = cv2.dilate(mask_blue, dilation)
        binary = cv2.add(green_binary, blue_binary)
        #contours, hierarchy = cv2.findContours(mask, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
        # Find all the contours of every shape that appears in the masked images.
        contours, hierarchy = cv2.findContours(binary, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
        max_area = 0
        largest_contour = None
        moments = []
        blobs = []
        if len(contours)>0: # If there are any contours
            self.centroids = []
            for idx, contour in enumerate(contours):
                hull = cv2.convexHull(contour) # Convex hull makes the perimeter more accurate.
                moment = cv2.moments(hull) # Generates a list of all the moments.
                perimeter = cv2.arcLength(hull, True) # Perimeter of the contour
                area = moment["m00"] # Oth moment is area
                roundness = perimeter*perimeter / (4 * 3.1416 * area) # 1 for a circle, >1 for other shapes
                if area > 2000 and roundness > 1.05: # If the contour is large enough and is not spherical.
                    # print roundness
                    # Create bounding rectangle (for display/debugging)
                    x,y,w,h = cv2.boundingRect(hull)
                    cv2.rectangle(masked_image,(x,y),(x+w, y+h),(0,255,0),4)
                    temp_x = x+(w/2)
                    if temp_x >= self.max_width:
                        temp_x = self.max_width-1
                    if temp_x < 0:
                        temp_x = 0
                    temp_y = y+(h/2)
                    if temp_y >= self.max_height:
                        temp_y = self.max_height-1
                    if temp_y < 0:
                        temp_y = 0
                    # Add the center point of the blob to the list of centroids
                    self.centroids.append((temp_x,temp_y))
            self.blob_exists = True
        else:
            self.blob_exists = False
   #         area = cv2.contourArea(contour)
  #          if area > max_area:
 #               max_area = area
 #               largest_contour = contour
#        if not largest_contour == None:

#            moment = cv2.moments(largest_contour)
 #           if moment["m00"] > 4000 / 1:
  #               x,y,w,h = cv2.boundingRect(largest_contour)
   #              cv2.rectangle(masked_image,(x,y),(x+w,y+h),(0,255,0),5)
#
 #                self.centroid_x = (x+(w/2))
  #               if self.centroid_x >= self.max_width:
   #                  self.centroid_x = self.max_width-1
    #             if self.centroid_x < 0:
     #                self.centroid_x = 0
#
 #                self.centroid_y = (y+(h/2))
  #               if self.centroid_y >= self.max_height:
   #                  self.centroid_y = self.max_height-1
    #             if self.centroid_y < 0:
     #                self.centroid_y = 0
#
 #                self.blob_exists = True
  #          else:
    #             self.blob_exists = False
            
                 #rect = ((rect[0][0] * 1, rect[0][1] * 1), (rect[1][0] *1, rect[1][1] * 1), rect[2])
                 #box = cv2.cv.BoxPoints(rect)
                 #box = np.int0(box)
                 #cv2.drawContours(masked_image,[box], 0, (0, 0, 255), 2)

        return masked_image
        

        # Convert to greyscale
#        grey = cv2.cvtColor(frame, cv.CV_BGR2GRAY)
        
        # Blur the image
#        grey = cv2.blur(grey, (7, 7))
        
        # Compute edges using the Canny edge filter
#        edges = cv2.Canny(grey, 15.0, 30.0)
        
#        return edges
    
    def process_depth_image(self, frame):
        # If the kinect freaks out and doesn't give a depth image
        if frame.shape[0] == 0:
            return frame
        #print frame.shape[1]#self.centroid_x, self.centroid_y
        #frame = cv2.cvtColor(frame,cv2.COLOR_GRAY2RGB)
        if self.blob_exists == True:
            self.depths = []
            for coord in self.centroids:
                centroid_x = coord[0]
                centroid_y = coord[1]
                depth = frame[centroid_y,centroid_x] # Gets the distance from the camera of the blob
                self.depths.append(depth)
 #               print centroid_x, centroid_y, depth
        #depth = frame[self.centroid_y, self.centroid_x]

                # Create crosshairs and text.
                if (centroid_x - 15 > 10 and centroid_x + 20 < frame.shape[1] and centroid_y - 15 > 10 and centroid_y + 20 < frame.shape[0]):
                    cv2.line(frame, (centroid_x - 15, centroid_y), (centroid_x + 15, centroid_y), (255,255,255), 5)
                    cv2.line(frame, (centroid_x, centroid_y-15), (centroid_x, centroid_y+15), (255,255,255), 5)
                    depth_str = str(depth)
                    textSize, baseline = cv2.getTextSize(depth_str, cv2.FONT_HERSHEY_SIMPLEX, .5, 1)
                #print textSize
                    if textSize[0] + centroid_x - 10 < self.max_width and centroid_x - 10 >0 and textSize[1] + centroid_y + 40 < self.max_height and centroid_y >0:
                        cv2.putText(frame, depth_str, (centroid_x -10, centroid_y + 40), cv2.FONT_HERSHEY_SIMPLEX, .5, (0,0,0),1)
        return frame
    
    # A helper method to get the horizontal distance in meters of a blob from the center of the robot.
    def horizontalPosition(self, x, d):
        theta = math.pi/6 # Angle from horizontal of kinect mount
        distance = d*math.cos(theta)
        fov = 57 * math.pi/180 # the FOV of a kinect is 57 degrees
        center_dist = x - self.max_width/2
        x_meters = 2*x*d*math.tan(fov/2)/self.max_width
        return x_meters
        
    
    def cleanup(self):
        print "Shutting down vision node."
        cv2.destroyAllWindows()   
    
def main(args):       
    try:
        kinect()
        rospy.spin()
    except KeyboardInterrupt:
        print "Shutting down vision node."
        cv.DestroyAllWindows()

if __name__ == '__main__':
    main(sys.argv)
    
