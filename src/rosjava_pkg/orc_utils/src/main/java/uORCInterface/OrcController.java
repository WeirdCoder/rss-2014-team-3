package uORCInterface;

import java.io.PrintStream;
import orc.AnalogInput;
import orc.DigitalInput;
import orc.DigitalOutput;
import orc.Motor;
import orc.Orc;
import orc.OrcStatus;
import orc.QuadratureEncoder;

public class OrcController
  implements OrcControlInterface
{
  private Orc orc = Orc.makeOrc();
  private Motor[] motorSet;
  private int[] portSet;
  protected static final int NUM_DIGITAL_IO = 16;
  private static final boolean[] DIGITAL_SETUP = { true, true, true, true, true, true, true, true, false, false, false, false, false, true, false, true };
  private DigitalInput[] dIn = new DigitalInput[16];
  private DigitalOutput[] dOut = new DigitalOutput[16];
  protected static final int MAX_ANALOG_PORT = 7;
  protected static final int MAX_PWM = 255;
  
  public OrcController(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt.length > 2)
    {
      System.out.println("Too many Motors");
      System.exit(1);
    }
    this.motorSet = new Motor[paramArrayOfInt.length];
    this.portSet = new int[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      checkPortRange(paramArrayOfInt[i]);
      this.motorSet[i] = new Motor(this.orc, paramArrayOfInt[i], false);
      this.portSet[i] = paramArrayOfInt[i];
    }
    for (int i = 0; i < 16; i++) {
      if (DIGITAL_SETUP[i] != false)
      {
        boolean bool = true;
        this.dIn[i] = new DigitalInput(this.orc, i, bool, false);
      }
      else
      {
        this.dOut[i] = new DigitalOutput(this.orc, i);
      }
    }
    System.out.println("uOrcBoard initialized...");
  }
  
  public double analogRead(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 7))
    {
      System.out.println("Out of bound: the analog port must be 0~7");
      System.exit(1);
    }
    return new AnalogInput(this.orc, paramInt).getVoltage();
  }
  
  public long clockReadSlave()
  {
    return this.orc.getStatus().utimeOrc;
  }
  
  public void motorSet(int paramInt1, int paramInt2)
  {
    checkMotorRange(paramInt1);
    double d = 0.0D;
    if ((paramInt2 < -255) || (paramInt2 > 255))
    {
      System.out.println("WARNING Out of bound: pwm should be [-255 255]");
      d = paramInt2 / Math.abs(paramInt2);
    }
    else
    {
      d = paramInt2 / 255.0D;
    }
    this.motorSet[paramInt1].setPWM(d);
  }
  
  public void motorSlewWrite(int paramInt1, int paramInt2)
  {
    checkMotorRange(paramInt1);
    this.motorSet[paramInt1].setSlewSeconds(paramInt2);
  }
  
  public int readEncoder(int paramInt)
  {
    checkMotorRange(paramInt);
    return new QuadratureEncoder(this.orc, this.portSet[paramInt], false).getPosition();
  }
  
  public int readVelocity(int paramInt)
  {
    checkMotorRange(paramInt);
    return (int)new QuadratureEncoder(this.orc, this.portSet[paramInt], false).getVelocity();
  }
  
  public void servoWrite(int paramInt1, int paramInt2) {}
  
  public void digitalSet(int paramInt, boolean paramBoolean)
  {
    if (DIGITAL_SETUP[paramInt] != false)
    {
      System.out.println("This port is NOT set for digital OUTPUT!!!");
      System.exit(1);
    }
    else
    {
      this.dOut[paramInt].setValue(paramBoolean);
    }
  }
  
  public boolean digitalRead(int paramInt)
  {
    if (DIGITAL_SETUP[paramInt] == false)
    {
      System.out.println("This port is NOT set for digital INPUT!!!");
      System.exit(1);
    }
    return this.dIn[paramInt].getValue();
  }
  
  private void checkMotorRange(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.motorSet.length))
    {
      System.out.println("Out of bound: check the motor number");
      System.exit(1);
    }
  }
  
  private void checkPortRange(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 2))
    {
      System.out.println("Out of bound: the port must be 0~2");
      System.exit(1);
    }
  }
}
