package orc;

import java.io.DataInputStream;
import java.io.IOException;

public class OrcStatus
{
  public long utime;
  public int statusFlags;
  public int debugCharsWaiting;
  public int[] analogInput = new int[13];
  public int[] analogInputFiltered = new int[13];
  public int[] analogInputFilterAlpha = new int[13];
  public int simpleDigitalValues;
  public int simpleDigitalDirections;
  public boolean[] motorEnable = new boolean[3];
  public int[] motorPWMactual = new int[3];
  public int[] motorPWMgoal = new int[3];
  public int[] motorSlewRaw = new int[3];
  public double[] motorSlewSeconds = new double[3];
  public int[] qeiPosition = new int[2];
  public int[] qeiVelocity = new int[2];
  public int[] fastDigitalMode = new int[8];
  public int[] fastDigitalConfig = new int[8];
  public long[] gyroIntegrator = new long[3];
  public int[] gyroIntegratorCount = new int[3];
  
  static final int readU16(DataInputStream paramDataInputStream)
    throws IOException
  {
    return paramDataInputStream.readShort() & 0xFFFF;
  }
  
  static final int readS16(DataInputStream paramDataInputStream)
    throws IOException
  {
    return paramDataInputStream.readShort();
  }
  
  static final int readU8(DataInputStream paramDataInputStream)
    throws IOException
  {
    return paramDataInputStream.readByte() & 0xFF;
  }
  
  public OrcStatus(OrcResponse paramOrcResponse)
    throws IOException
  {
    DataInputStream localDataInputStream = paramOrcResponse.ins;
    this.utime = paramOrcResponse.utime;
    this.statusFlags = localDataInputStream.readInt();
    this.debugCharsWaiting = readU16(localDataInputStream);
    for (int i = 0; i < 13; i++)
    {
      this.analogInput[i] = readU16(localDataInputStream);
      this.analogInputFiltered[i] = readU16(localDataInputStream);
      this.analogInputFilterAlpha[i] = readU16(localDataInputStream);
    }
    this.simpleDigitalValues = localDataInputStream.readInt();
    this.simpleDigitalDirections = localDataInputStream.readInt();
    for (i = 0; i < 3; i++)
    {
      this.motorEnable[i] = (readU8(localDataInputStream) != 0 ? 1 : false);
      this.motorPWMactual[i] = readS16(localDataInputStream);
      this.motorPWMgoal[i] = readS16(localDataInputStream);
      this.motorSlewRaw[i] = readU16(localDataInputStream);
      this.motorSlewSeconds[i] = (510.0D / this.motorSlewRaw[i] / 1000.0D * 128.0D);
    }
    for (i = 0; i < 2; i++)
    {
      this.qeiPosition[i] = localDataInputStream.readInt();
      this.qeiVelocity[i] = localDataInputStream.readInt();
    }
    for (i = 0; i < 8; i++)
    {
      this.fastDigitalMode[i] = readU8(localDataInputStream);
      this.fastDigitalConfig[i] = localDataInputStream.readInt();
    }
    for (i = 0; i < 3; i++)
    {
      this.gyroIntegrator[i] = localDataInputStream.readLong();
      this.gyroIntegratorCount[i] = localDataInputStream.readInt();
    }
  }
}

