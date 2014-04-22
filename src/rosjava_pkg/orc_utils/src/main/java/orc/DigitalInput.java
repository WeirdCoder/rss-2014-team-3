package orc;

public class DigitalInput
{
  Orc orc;
  int port;
  boolean invert;
  
  public DigitalInput(final Orc paramOrc, final int paramInt, final boolean paramBoolean1, final boolean paramBoolean2){
  
    orc = paramOrc;
    this.port = paramInt;
    this.invert = paramBoolean2;
    if (paramInt < 8) {
      paramOrc.doCommand(24576, new byte[] { (byte)paramInt, 1, (byte)(paramBoolean1 ? 1 : 0) });
    } else {
      paramOrc.doCommand(28672, new byte[] { (byte)(paramInt - 8), 1, 0, 0, 0, 0 });
    }
  }
  
  public boolean getValue()
  {
    OrcStatus localOrcStatus = this.orc.getStatus();
    boolean i;
    if (this.port < 8) {
      i = (localOrcStatus.simpleDigitalValues & 1 << this.port) != 0x0;
    } else {
      i = localOrcStatus.fastDigitalConfig[(this.port - 8)] != 0;
    }
    return i ^ this.invert;
  }
}

