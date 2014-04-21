package orc;

import java.io.DataInputStream;
import java.io.PrintStream;

public final class OrcResponse
{
  public boolean responded = false;
  public int transactionId;
  public long utime;
  public int responseId;
  public DataInputStream ins;
  public byte[] responseBuffer;
  public int responseBufferOffset;
  public int responseBufferLength;
  
  public synchronized void gotResponse()
  {
    this.responded = true;
    notifyAll();
  }
  
  public synchronized boolean waitForResponse(int paramInt)
  {
    if (this.responded) {
      return true;
    }
    try
    {
      wait(paramInt);
    }
    catch (InterruptedException localInterruptedException) {}
    return this.responded;
  }
  
  public void print()
  {
    for (int i = 0; i < this.responseBufferLength; i++)
    {
      if (i % 16 == 0) {
        System.out.printf("%04x: ", new Object[] { Integer.valueOf(i) });
      }
      System.out.printf("%02x ", new Object[] { Byte.valueOf(this.responseBuffer[(this.responseBufferOffset + i)]) });
      if ((i % 16 == 15) || (i == this.responseBufferLength - 1)) {
        System.out.printf("\n", new Object[0]);
      }
    }
  }
}


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\uORCInterface-0.0.jar
 * Qualified Name:     orc.OrcResponse
 * JD-Core Version:    0.7.0.1
 */