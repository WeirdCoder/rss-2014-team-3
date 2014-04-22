package orc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Orc
{
  DatagramSocket sock;
  ReaderThread reader;
  public InetAddress orcAddr;
  int nextTransactionId;
  static final int ORC_BASE_PORT = 2378;
  static final double MIN_TIMEOUT = 0.002D;
  static final double MAX_TIMEOUT = 0.01D;
  public static final int FAST_DIGIO_MODE_IN = 1;
  public static final int FAST_DIGIO_MODE_OUT = 2;
  public static final int FAST_DIGIO_MODE_SERVO = 3;
  double meanRTT = 0.002D;
  public boolean verbose = false;
  HashMap<Integer, OrcResponse> transactionResponses = new HashMap();
  ArrayList<OrcListener> listeners = new ArrayList();
  
  public static void main(String[] paramArrayOfString)
  {
    Orc localOrc = makeOrc();
    try
    {
      for (;;)
      {
        Thread.sleep(500L);
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  public static Orc makeOrc()
  {
    return makeOrc("192.168.237.7");
  }
  
  public static Orc makeOrc(String paramString)
  {
    for (;;)
    {
      try
      {
        return new Orc(Inet4Address.getByName(paramString));
      }
      catch (IOException localIOException)
      {
        System.out.println("Exception creating Orc: " + localIOException);
        try
        {
          Thread.sleep(500L);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
  }
  
  public Orc(InetAddress paramInetAddress)
    throws IOException
  {
    this.orcAddr = paramInetAddress;
    this.sock = new DatagramSocket();
    this.reader = new ReaderThread();
    this.reader.setDaemon(true);
    this.reader.start();
  }
  
  public void addListener(OrcListener paramOrcListener)
  {
    this.listeners.add(paramOrcListener);
  }
  
  OrcResponse doCommand(int paramInt, byte[] paramArrayOfByte)
  {
    for (;;)
    {
      try
      {
        return doCommandEx(paramInt, paramArrayOfByte);
      }
      catch (IOException localIOException)
      {
        System.out.println("ex: " + localIOException);
      }
    }
  }
  
  OrcResponse doCommandEx(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    OrcResponse localOrcResponse = new OrcResponse();
    localDataOutputStream.writeInt(216858626);
    int i;
    synchronized (this)
    {
      i = this.nextTransactionId++;
      this.transactionResponses.put(Integer.valueOf(i), localOrcResponse);
    }
    localDataOutputStream.writeInt(i);
    localDataOutputStream.writeLong(System.nanoTime() / 1000L);
    localDataOutputStream.writeInt(paramInt);
    if (paramArrayOfByte != null) {
      localDataOutputStream.write(paramArrayOfByte);
    }
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length, this.orcAddr, 2378 + (paramInt >> 24 & 0xFF));
    long l1 = System.nanoTime();
    try
    {
      boolean bool;
      do
      {
        this.sock.send(localDatagramPacket);
        bool = localOrcResponse.waitForResponse(50 + (int)(10.0D * this.meanRTT));
        if ((!bool) && (this.verbose)) {
          System.out.println("Transaction timeout: " + i + ", timeout=" + this.meanRTT);
        }
      } while (!bool);
      long l2 = System.nanoTime();
      double d1 = (l2 - l1) / 1000000000.0D;
      double d2 = 0.995D;
      this.meanRTT = (d2 * this.meanRTT + (1.0D - d2) * d1);
      this.meanRTT = Math.min(Math.max(this.meanRTT, 0.002D), 0.01D);
      return localOrcResponse;
    }
    catch (IOException localIOException)
    {
      this.transactionResponses.remove(Integer.valueOf(i));
      throw localIOException;
    }
  }
  
  public OrcStatus getStatus()
  {
    for (;;)
    {
      try
      {
        return new OrcStatus(doCommand(1, null));
      }
      catch (IOException localIOException) {}
    }
  }
  
  public byte[] i2cTransaction(final int addr, final Object... os)
  {
    final ByteArrayOutputStream bouts = new  ByteArrayOutputStream();
    bouts.write((byte)addr);
    bouts.write(1);
    assert ((os.length & 0x1) == 0x0);
    assert (os.length >= 2);
    final int  ntransactions = os.length / 2;
    for (int j = 0; j < ntransactions; j++)
    {
      final byte[] writebuf = (byte[])os[(2 * j + 0)];
      final int writebuflen = (writebuf == null) ? 0 : writebuf.length;
      final int readlen = (Integer)os[2*j+1];
      bouts.write((byte)writebuflen);
      bouts.write((byte)readlen);
      for (int i = 0; i < writebuflen; i++) {
        bouts.write(writebuf[i]);
      }
    }
    final OrcResponse resp = this.doCommand(20480, bouts.toByteArray());
    assert (resp.responded);
    final ByteArrayOutputStream readData = new ByteArrayOutputStream();
    try
    {
      for (int k = 0; k < ntransactions; k++)
      {
        final int error  = resp.ins.readByte() & 0xFF;
        if (error != 0) {
          System.out.printf("Orc I2C error: code = %d\n", k++);
        }
        int actualreadlen = resp.ins.readByte() & 0xFF;
        for(int j =0;j < actualreadlen; j++){
		readData.write(resp.ins.readByte());
        }
      }
      return readData.toByteArray();
    }
    catch (IOException localIOException) {return null;}
  }
  
  public int[] spiTransaction(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    paramInt1 /= 1000;
    assert (paramInt4 <= 16);
    assert ((paramInt2 == 0) || (paramInt2 == 1));
    assert ((paramInt3 == 0) || (paramInt3 == 1));
    assert (paramArrayOfInt.length <= 16);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(paramInt1 >> 8 & 0xFF);
    localByteArrayOutputStream.write(paramInt1 & 0xFF);
    localByteArrayOutputStream.write(paramInt4 | paramInt2 << 6 | paramInt3 << 7);
    localByteArrayOutputStream.write(paramArrayOfInt.length);
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      localByteArrayOutputStream.write(paramArrayOfInt[i] >> 8 & 0xFF);
      localByteArrayOutputStream.write(paramArrayOfInt[i] & 0xFF);
    }
    OrcResponse localOrcResponse = doCommand(16384, localByteArrayOutputStream.toByteArray());
    assert (localOrcResponse.responded);
    int[] arrayOfInt = null;
    try
    {
      int j = localOrcResponse.ins.readByte();
      assert (j == 0);
      int k = localOrcResponse.ins.readByte() & 0xFF;
      arrayOfInt = new int[k];
      for (int m = 0; m < k; m++) {
        arrayOfInt[m] = (localOrcResponse.ins.readShort() & 0xFFFF);
      }
    }
    catch (IOException localIOException)
    {
      return null;
    }
    return arrayOfInt;
  }
  
  class ReaderThread
    extends Thread
  {
    ReaderThread() {}
    
    public void run()
    {
      for (;;)
      {
        byte[] arrayOfByte = new byte[1600];
        DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
        try
        {
          Orc.this.sock.receive(localDatagramPacket);
          DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte, 0, localDatagramPacket.getLength()));
          int i = localDataInputStream.readInt();
          if (i != 216858625)
          {
            System.out.println("bad signature");
          }
          else
          {
            int j = localDataInputStream.readInt();
            long l = localDataInputStream.readLong();
            int k = localDataInputStream.readInt();
            OrcResponse localOrcResponse;
            synchronized (Orc.this)
            {
              localOrcResponse = (OrcResponse)Orc.this.transactionResponses.remove(Integer.valueOf(j));
            }
            if (localOrcResponse != null)
            {
              localOrcResponse.ins = localDataInputStream;
              localOrcResponse.responseBuffer = arrayOfByte;
              localOrcResponse.responseBufferOffset = 20;
              localOrcResponse.responseBufferLength = localDatagramPacket.getLength();
              localOrcResponse.utimeOrc = l;
              localOrcResponse.responseId = k;
              localOrcResponse.gotResponse();
            }
            else if (Orc.this.verbose)
            {
              System.out.println("Unexpected reply for transId: " + j + " (last issued: " + (Orc.this.nextTransactionId - 1) + ")");
            }
          }
        }
        catch (IOException localIOException)
        {
          System.out.println("Orc.ReaderThread Ex: " + localIOException);
          try
          {
            Thread.sleep(100L);
          }
          catch (InterruptedException localInterruptedException) {}
        }
      }
    }
  }
}

