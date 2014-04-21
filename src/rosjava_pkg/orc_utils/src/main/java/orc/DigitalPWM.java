/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class DigitalPWM
/*  6:   */ {
/*  7:   */   Orc orc;
/*  8:   */   int port;
/*  9:   */   
/* 10:   */   public DigitalPWM(Orc orc, int port)
/* 11:   */   {
/* 12:18 */     this.orc = orc;
/* 13:19 */     this.port = port;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void setPWM(int period_usec, double dutyCycle)
/* 17:   */   {
/* 18:32 */     assert ((dutyCycle >= 0.0D) && (dutyCycle <= 1.0D));
/* 19:33 */     assert ((period_usec >= 1000) && (period_usec <= 1000000));
/* 20:   */     
/* 21:35 */     int iduty = (int)(dutyCycle * 4095.0D);
/* 22:36 */     int command = (iduty << 20) + period_usec;
/* 23:   */     
/* 24:38 */     this.orc.doCommand(28672, new byte[] { (byte)(this.port - 8), 2, (byte)(command >> 24 & 0xFF), (byte)(command >> 16 & 0xFF), (byte)(command >> 8 & 0xFF), (byte)(command >> 0 & 0xFF) });
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setValue(boolean v) {}
/* 28:   */   
/* 29:   */   public static void main(String[] args)
/* 30:   */   {
/* 31:53 */     Orc orc = Orc.makeOrc();
/* 32:54 */     DigitalOutput dout = new DigitalOutput(orc, 0);
/* 33:   */     try
/* 34:   */     {
/* 35:   */       for (;;)
/* 36:   */       {
/* 37:58 */         dout.setValue(true);
/* 38:59 */         System.out.println("true");
/* 39:60 */         Thread.sleep(1000L);
/* 40:61 */         dout.setValue(false);
/* 41:62 */         System.out.println("false");
/* 42:63 */         Thread.sleep(1000L);
/* 43:   */       }
/* 44:   */     }
/* 45:   */     catch (InterruptedException ex) {}
/* 46:   */   }
/* 47:   */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.DigitalPWM
 * JD-Core Version:    0.7.0.1
 */