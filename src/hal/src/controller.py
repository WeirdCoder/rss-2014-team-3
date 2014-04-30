import pygame

pygame.init()
 
# Set the width and height of the screen [width,height]
size = [500, 700]
screen = pygame.display.set_mode(size)

pygame.display.set_caption("robot")

#Loop until the user clicks the close button.
done = False

# Used to manage how fast the screen updates
clock = pygame.time.Clock()

# Initialize the joysticks
pygame.joystick.init()
    
# -------- Main Program Loop -----------
joystick = pygame.joystick.Joystick(0)
joystick.init()

import hal

r=hal.RobotHardware()

while done==False:
    # EVENT PROCESSING STEP
    for event in pygame.event.get(): # User did something
        if event.type == pygame.QUIT: # If user clicked close
            done=True # Flag that we are done so we exit this loop
        
        # Possible joystick actions: JOYAXISMOTION JOYBALLMOTION JOYBUTTONDOWN JOYBUTTONUP JOYHATMOTION
        if event.type == pygame.JOYBUTTONDOWN:
            pass
            #print("Joystick button pressed.")
        if event.type == pygame.JOYBUTTONUP:
            pass
            #print("Joystick button released.")
            
        buttons = [joystick.get_button( i ) for i in range(8)]

        if buttons[0]:
            ramp=1
        elif buttons[1]:
            ramp=-1
        else:
            ramp=0

        if buttons[3]:
            back=1
        elif buttons[2]:
            back=-1
        else:
            back=0

        if buttons[7]:
            hopper=1
        else:
            hopper=0

        axes = [joystick.get_axis(i) for i in range(3)]

        drive_left=-axes[1]
        drive_right=-axes[2]

        r.command_actuators({
            'ramp_conveyer':ramp,
            'back_conveyer':back,
            'left_wheel':drive_left,
            'right_wheel':drive_right,
            'hopper':hopper
        })
   
    # Go ahead and update the screen with what we've drawn.
    pygame.display.flip()

    # Limit to 20 frames per second
    clock.tick(20)
    
# Close the window and quit.
# If you forget this line, the program will 'hang'
# on exit if running from IDLE.
pygame.quit ()
