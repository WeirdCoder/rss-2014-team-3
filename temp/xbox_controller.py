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
            
        buttons = [joystick.get_button( i ) for i in range(4)]

        if buttons[0]:
            r.run_ramp_conveyer(1)
        elif buttons[1]:
            r.run_ramp_conveyer(-1)
        else:
            r.run_ramp_conveyer(0)

        if buttons[2]:
            r.run_back_conveyer(1)
        elif buttons[3]:
            r.run_back_conveyer(-1)
        else:
            r.run_back_conveyer(0)

        axes = [joystick.get_axis(i) for i in range(6)]

        r.set_hopper((axes[5]+1)/2)

        r.o.set_motor(1,int(100*axes[4]))
        r.o.set_motor(0,int(-100*axes[1]))
   
    # Go ahead and update the screen with what we've drawn.
    pygame.display.flip()

    # Limit to 20 frames per second
    clock.tick(20)
    
# Close the window and quit.
# If you forget this line, the program will 'hang'
# on exit if running from IDLE.
pygame.quit ()
