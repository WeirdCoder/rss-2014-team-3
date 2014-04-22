/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class AX12Status
/*  6:   */ {
/*  7:   */   public static final int ERROR_INSTRUCTION = 64;
/*  8:   */   public static final int ERROR_OVERLOAD = 32;
/*  9:   */   public static final int ERROR_CHECKSUM = 16;
/* 10:   */   public static final int ERROR_RANGE = 8;
/* 11:   */   public static final int ERROR_OVERHEAT = 4;
/* 12:   */   public static final int ERROR_ANGLE_LIMIT = 2;
/* 13:   */   public static final int ERROR_VOLTAGE = 1;
/* 14:   */   public long utimeOrc;
/* 15:   */   public long utimeHost;
/* 16:   */   public double positionDegrees;
/* 17:   */   public double speed;
/* 18:   */   public double load;
/* 19:   */   public double voltage;
/* 20:   */   public double temperature;
/* 21:   */   public int error_flags;
/* 22:   */   
/* 23:   */   public void print()
/* 24:   */   {
/* 25:38 */     System.out.printf("position:    %f\n", new Object[] { Double.valueOf(this.positionDegrees) });
/* 26:39 */     System.out.printf("speed:       %f\n", new Object[] { Double.valueOf(this.speed) });
/* 27:40 */     System.out.printf("load:         %f\n", new Object[] { Double.valueOf(this.load) });
/* 28:41 */     System.out.printf("voltage:     %f\n", new Object[] { Double.valueOf(this.voltage) });
/* 29:42 */     System.out.printf("temperature: %f\n", new Object[] { Double.valueOf(this.temperature) });
/* 30:43 */     System.out.printf("error flags:  %d\n", new Object[] { Integer.valueOf(this.error_flags) });
/* 31:44 */     System.out.printf("utime (orc):  %d\n", new Object[] { Long.valueOf(this.utimeOrc) });
/* 32:45 */     System.out.printf("utime (host): %d\n", new Object[] { Long.valueOf(this.utimeHost) });
/* 33:   */   }
/* 34:   */ }
