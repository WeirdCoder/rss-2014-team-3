package orc;

import java.io.PrintStream;

public class DigitalOutput
{
  Orc orc;
  int port;
  boolean invert;
  
  public DigitalOutput(Orc paramOrc, int paramInt)
  {
    this(paramOrc, paramInt, false);
  }
  
  public DigitalOutput(Orc paramOrc, int paramInt, boolean paramBoolean)
  {
    this.orc = paramOrc;
    this.port = paramInt;
    this.invert = paramBoolean;
    if (paramInt < 8) {
      paramOrc.doCommand(24576, new byte[] { (byte)paramInt, 0, 0 });
    } else {
      paramOrc.doCommand(28672, new byte[] { (byte)(paramInt - 8), 2, 0, 0, 0, 0 });
    }
  }
  
  public void setValue(boolean paramBoolean)
  {
    if (this.port < 8) {
      this.orc.doCommand(24577, new byte[] { (byte)this.port, (byte)((paramBoolean ^ this.invert) ? 1 : 0) });
    } else {
      this.orc.doCommand(28672, new byte[] { (byte)(this.port - 8), 2, 0, 0, 0, (byte)((paramBoolean ^ this.invert) ? 1 : 0) });
    }
  }
  
  public static void main(String[] paramArrayOfString)
  {
    Orc localOrc = Orc.makeOrc();
    DigitalOutput localDigitalOutput = new DigitalOutput(localOrc, 0);
    try
    {
      for (;;)
      {
        localDigitalOutput.setValue(true);
        System.out.println("true");
        Thread.sleep(1000L);
        localDigitalOutput.setValue(false);
        System.out.println("false");
        Thread.sleep(1000L);
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
}

