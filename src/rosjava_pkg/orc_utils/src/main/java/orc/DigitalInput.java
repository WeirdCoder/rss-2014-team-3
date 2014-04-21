

DigitalInput

  orc
  port
  invert
  
  DigitalInput, , , 
  
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
    int i;
    if (this.port < 8) {
      i = (localOrcStatus.simpleDigitalValues & 1 << this.port) != 0 ? 1 : 0;
    } else {
      i = localOrcStatus.fastDigitalConfig[(this.port - 8)] != 0 ? 1 : 0;
    }
    return i ^ this.invert;
  }
}


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\uORCInterface-0.0.jar
 * Qualified Name:     orc.DigitalInput
 * JD-Core Version:    0.7.0.1
 */