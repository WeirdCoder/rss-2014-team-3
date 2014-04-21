/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ public class Servo
/*  4:   */ {
/*  5:   */   Orc orc;
/*  6:   */   int port;
/*  7:   */   double pos0;
/*  8:   */   double pos1;
/*  9:   */   int usec0;
/* 10:   */   int usec1;
/* 11:   */   
/* 12:   */   public Servo(Orc orc, int port, double pos0, int usec0, double pos1, int usec1)
/* 13:   */   {
/* 14:15 */     this.orc = orc;
/* 15:16 */     this.port = port;
/* 16:17 */     this.pos0 = pos0;
/* 17:18 */     this.usec0 = usec0;
/* 18:19 */     this.pos1 = pos1;
/* 19:20 */     this.usec1 = usec1;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setPulseWidth(int usecs)
/* 23:   */   {
/* 24:25 */     this.orc.doCommand(28672, new byte[] { (byte)this.port, 3, (byte)(usecs >> 24 & 0xFF), (byte)(usecs >> 16 & 0xFF), (byte)(usecs >> 8 & 0xFF), (byte)(usecs >> 0 & 0xFF) });
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void idle()
/* 28:   */   {
/* 29:35 */     int value = 0;
/* 30:36 */     this.orc.doCommand(28672, new byte[] { (byte)this.port, 2, (byte)(value >> 24 & 0xFF), (byte)(value >> 16 & 0xFF), (byte)(value >> 8 & 0xFF), (byte)(value >> 0 & 0xFF) });
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setPosition(double pos)
/* 34:   */   {
/* 35:46 */     if (pos < Math.min(this.pos0, this.pos1)) {
/* 36:47 */       pos = Math.min(this.pos0, this.pos1);
/* 37:   */     }
/* 38:49 */     if (pos > Math.max(this.pos0, this.pos1)) {
/* 39:50 */       pos = Math.max(this.pos0, this.pos1);
/* 40:   */     }
/* 41:52 */     setPulseWidth((int)(this.usec0 + (this.usec1 - this.usec0) * (pos - this.pos0) / (this.pos1 - this.pos0)));
/* 42:   */   }
/* 43:   */   
/* 44:   */   public static Servo makeMPIMX400(Orc orc, int port)
/* 45:   */   {
/* 46:60 */     return new Servo(orc, port, 0.0D, 600, 3.141592653589793D, 2500);
/* 47:   */   }
/* 48:   */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.Servo
 * JD-Core Version:    0.7.0.1
 */