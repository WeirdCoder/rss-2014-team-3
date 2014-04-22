package orc;

public class QuadratureEncoder
{
  Orc orc;
  int port;
  boolean invert;
  static final int QEI_VELOCITY_SAMPLE_HZ = 40;
  
  public QuadratureEncoder(Orc paramOrc, int paramInt, boolean paramBoolean)
  {
    assert ((paramInt >= 0) && (paramInt <= 1));
    this.orc = paramOrc;
    this.port = paramInt;
    this.invert = paramBoolean;
  }
  
  public int getPosition()
  {
    OrcStatus localOrcStatus = this.orc.getStatus();
    return localOrcStatus.qeiPosition[this.port] * (this.invert ? -1 : 1);
  }
  
  public double getVelocity()
  {
    OrcStatus localOrcStatus = this.orc.getStatus();
    return localOrcStatus.qeiVelocity[this.port] * (this.invert ? -1 : 1) * 40;
  }
}

