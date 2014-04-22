/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class SRF02
/*  6:   */ {
/*  7:   */   Orc orc;
/*  8:   */   int i2caddr;
/*  9:   */   static final int DEFAULT_I2C_ADDR = 112;
/* 10:10 */   static double SOUND_METERS_PER_SEC = 343.0D;
/* 11:   */   
/* 12:   */   public SRF02(Orc orc, int i2caddr)
/* 13:   */   {
/* 14:14 */     this.orc = orc;
/* 15:15 */     this.i2caddr = i2caddr;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public SRF02(Orc orc)
/* 19:   */   {
/* 20:20 */     this(orc, 112);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void ping()
/* 24:   */   {
/* 25:26 */     byte[] resp = this.orc.i2cTransaction(this.i2caddr, new Object[] { 0, 82 }, Integer.valueOf(0));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public double readTime()
/* 29:   */   {
/* 30:32 */     byte[] resp = this.orc.i2cTransaction(this.i2caddr, new Object[] { 2 }, Integer.valueOf(4));
/* 31:   */     
/* 32:34 */     int usecs = ((resp[0] & 0xFF) << 8) + (resp[1] & 0xFF);
/* 33:   */     
/* 34:36 */     int minusecs = ((resp[2] & 0xFF) << 8) + (resp[3] & 0xFF);
/* 35:   */     
/* 36:38 */     return usecs / 1000000.0D;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public double readRange()
/* 40:   */   {
/* 41:43 */     return readTime() * SOUND_METERS_PER_SEC / 2.0D;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public double measure()
/* 45:   */   {
/* 46:49 */     ping();
/* 47:   */     try
/* 48:   */     {
/* 49:51 */       Thread.sleep(70L);
/* 50:   */     }
/* 51:   */     catch (InterruptedException ex) {}
/* 52:54 */     return readRange();
/* 53:   */   }
/* 54:   */   
/* 55:   */   public static void main(String[] args)
/* 56:   */   {
/* 57:59 */     Orc orc = Orc.makeOrc();
/* 58:   */     
/* 59:61 */     SRF02 srf = new SRF02(orc);
/* 60:   */     for (;;)
/* 61:   */     {
/* 62:64 */       System.out.printf("%15f m\n", new Object[] { Double.valueOf(srf.measure()) });
/* 63:   */     }
/* 64:   */   }
/* 65:   */ }

