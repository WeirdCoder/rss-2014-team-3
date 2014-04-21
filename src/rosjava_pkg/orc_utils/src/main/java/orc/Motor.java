package orc;

public class Motor
{
  Orc orc;
  int port;
  boolean invert;
  
  public Motor(Orc paramOrc, int paramInt, boolean paramBoolean)
  {
    this.orc = paramOrc;
    this.port = paramInt;
    this.invert = paramBoolean;
  }
  
  public void idle()
  {
    this.orc.doCommand(4096, new byte[] { (byte)this.port, 0, 0, 0 });
  }
  
  public void setPWM(double paramDouble)
  {
    assert ((paramDouble >= -1.0D) && (paramDouble <= 1.0D));
    int i = (int)(paramDouble * 255.0D);
    if (this.invert) {
      i *= -1;
    }
    this.orc.doCommand(4096, new byte[] { (byte)this.port, 1, (byte)(i >> 8 & 0xFF), (byte)(i & 0xFF) });
  }
  
  public double getCurrent()
  {
    OrcStatus localOrcStatus = this.orc.getStatus();
    double d1 = localOrcStatus.analogInput[(this.port + 8)] / 65535.0D * 3.0D;
    double d2 = d1 * 375.0D / 200.0D;
    return d2;
  }
  
  public void setSlewSeconds(double paramDouble)
  {
    assert ((paramDouble >= 0.0D) && (paramDouble < 120.0D));
    paramDouble = Math.max(paramDouble, 0.001D);
    double d = 0.51D / paramDouble * 128.0D;
    int i = (int)d;
    i = Math.max(i, 1);
    i = Math.min(i, 65535);
    this.orc.doCommand(4097, new byte[] { (byte)this.port, (byte)(i >> 8 & 0xFF), (byte)(i & 0xFF) });
  }
}


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\uORCInterface-0.0.jar
 * Qualified Name:     orc.Motor
 * JD-Core Version:    0.7.0.1
 */