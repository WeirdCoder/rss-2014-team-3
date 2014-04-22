package orc;

public class AnalogInput
{
  Orc orc;
  int port;
  
  public AnalogInput(Orc paramOrc, int paramInt)
  {
    assert ((paramInt >= 0) && (paramInt <= 7));
    this.orc = paramOrc;
    this.port = paramInt;
  }
  
  public double getVoltage()
  {
    OrcStatus localOrcStatus = this.orc.getStatus();
    return localOrcStatus.analogInputFiltered[this.port] / 65535.0D * 5.0D;
  }
  
  public double getVoltageUnfiltered()
  {
    OrcStatus localOrcStatus = this.orc.getStatus();
    return localOrcStatus.analogInput[this.port] / 65535.0D * 5.0D;
  }
  
  public void setLPF(double paramDouble)
  {
    assert ((paramDouble >= 0.0D) && (paramDouble <= 1.0D));
    int i = (int)(paramDouble * 65536.0D);
    this.orc.doCommand(12288, new byte[] { (byte)this.port, (byte)(i >> 8 & 0xFF), (byte)(i >> 0 & 0xFF) });
  }
}

