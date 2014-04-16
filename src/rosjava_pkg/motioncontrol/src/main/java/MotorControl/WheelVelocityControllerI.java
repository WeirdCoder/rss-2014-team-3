package MotorControl;

/**
 * <p>Closed-loop integral wheel velocity controller.</p>
 *
 * @author vona
 * @author prentice
 **/
public class WheelVelocityControllerI extends WheelVelocityController {

  /**
   * <p>The result of the previous control step.</p>
   **/
  //Motion Control Lab, Part 8
  public double lastResult = 0;
    public double errorI = 0;
    public double previous_error = 0;
    protected double gainP = .5;
    protected double scalingConstant = 20;

    //Motion Control Lab, Part 8
    //Used to clear initial variables when Stop/start is hit on gui
    public void clearError() {
	errorI = 0;
	previous_error = 0;
	lastResult = 0;
    }
  /**
   * {@inheritDoc}
   *
   * <p>This impl implements closed-loop integral control.</p>
   **/
  public double controlStep() {

    double result = 0;

    // Start Student Code
    // Motion Control Lab, Part 8
    double error = desiredAngularVelocity - currentAngularVelocity;
    errorI += error * sampleTime;
    result = ((error * gainP) + (errorI * gain)) * scalingConstant;
    // End Student Code

    if (result > MAX_PWM)
      result = MAX_PWM;

    if (result < -MAX_PWM)
      result = -MAX_PWM;

    lastResult = result;

    return result;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This impl returns "I".</p>
   **/
  public String getName() {
    return "I";
  }
}
