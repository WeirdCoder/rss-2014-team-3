/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class WiiMotionPlus
/*  6:   */ {
/*  7:   */   Orc orc;
/*  8:   */   static final int I2C_INIT_ADDRESS = 83;
/*  9:   */   static final int I2C_ADDRESS = 82;
/* 10:   */   
/* 11:   */   public WiiMotionPlus(Orc orc)
/* 12:   */   {
/* 13:21 */     this.orc = orc;
/* 14:   */     
/* 15:   */ 
/* 16:   */ 
/* 17:25 */     orc.i2cTransaction(83, new Object[] { { -2, 4 }, Integer.valueOf(0) });
/* 18:   */     
/* 19:   */ 
/* 20:28 */     orc.i2cTransaction(82, new Object[] { { 0 }, Integer.valueOf(0) });
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int[] readAxes()
/* 24:   */   {
/* 25:36 */     byte[] resp = this.orc.i2cTransaction(82, new Object[] { { 0 }, Integer.valueOf(6) });
/* 26:   */     
/* 27:   */ 
/* 28:39 */     int[] data = new int[3];
/* 29:40 */     data[0] = ((resp[0] & 0xFF) + ((resp[3] & 0xFC) << 6));
/* 30:41 */     data[1] = ((resp[1] & 0xFF) + ((resp[4] & 0xFC) << 6));
/* 31:42 */     data[2] = ((resp[2] & 0xFF) + ((resp[5] & 0xFC) << 6));
/* 32:   */     
/* 33:44 */     return data;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static void main(String[] args)
/* 37:   */   {
/* 38:49 */     Orc orc = Orc.makeOrc();
/* 39:   */     
/* 40:51 */     WiiMotionPlus wmp = new WiiMotionPlus(orc);
/* 41:   */     for (;;)
/* 42:   */     {
/* 43:54 */       int[] axes = wmp.readAxes();
/* 44:   */       
/* 45:56 */       System.out.printf("%10d %10d %10d\n", new Object[] { Integer.valueOf(axes[0]), Integer.valueOf(axes[1]), Integer.valueOf(axes[2]) });
/* 46:   */       try
/* 47:   */       {
/* 48:58 */         Thread.sleep(30L);
/* 49:   */       }
/* 50:   */       catch (InterruptedException ex) {}
/* 51:   */     }
/* 52:   */   }
/* 53:   */ }

