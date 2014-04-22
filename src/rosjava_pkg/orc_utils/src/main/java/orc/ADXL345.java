/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class ADXL345
/*  6:   */ {
/*  7:   */   Orc orc;
/*  8:   */   static final int I2C_ADDRESS = 29;
/*  9:   */   
/* 10:   */   public ADXL345(Orc orc)
/* 11:   */   {
/* 12:18 */     this.orc = orc;
/* 13:   */     
/* 14:20 */     int deviceId = getDeviceId();
/* 15:21 */     assert (deviceId == 229);
/* 16:   */     
/* 17:23 */     orc.i2cTransaction(29, new Object[] { { 45, 8 }, Integer.valueOf(0) });
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getDeviceId()
/* 21:   */   {
/* 22:28 */     byte[] res = this.orc.i2cTransaction(29, new Object[] { { 0 }, Integer.valueOf(1) });
/* 23:   */     
/* 24:30 */     return res[0] & 0xFF;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int[] readAxes()
/* 28:   */   {
/* 29:35 */     byte[] resp = this.orc.i2cTransaction(29, new Object[] { { 50 }, Integer.valueOf(6) });
/* 30:   */     
/* 31:37 */     int[] v = new int[3];
/* 32:38 */     v[0] = ((resp[0] & 0xFF) + (resp[1] << 8));
/* 33:39 */     v[1] = ((resp[2] & 0xFF) + (resp[3] << 8));
/* 34:40 */     v[2] = ((resp[4] & 0xFF) + (resp[5] << 8));
/* 35:41 */     return v;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public static void main(String[] args)
/* 39:   */   {
/* 40:46 */     Orc orc = Orc.makeOrc();
/* 41:   */     
/* 42:48 */     ADXL345 accel = new ADXL345(orc);
/* 43:   */     for (;;)
/* 44:   */     {
/* 45:51 */       int[] axes = accel.readAxes();
/* 46:   */       
/* 47:53 */       System.out.printf("%10d %10d %10d\n", new Object[] { Integer.valueOf(axes[0]), Integer.valueOf(axes[1]), Integer.valueOf(axes[2]) });
/* 48:   */       try
/* 49:   */       {
/* 50:56 */         Thread.sleep(30L);
/* 51:   */       }
/* 52:   */       catch (InterruptedException ex) {}
/* 53:   */     }
/* 54:   */   }
/* 55:   */ }

