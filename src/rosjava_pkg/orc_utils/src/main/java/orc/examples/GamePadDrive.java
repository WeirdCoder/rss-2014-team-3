/*  1:   */ package orc.examples;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import orc.Motor;
/*  5:   */ import orc.Orc;
/*  6:   */ import orc.util.GamePad;
/*  7:   */ 
/*  8:   */ public class GamePadDrive
/*  9:   */ {
/* 10:   */   public static void main(String[] args)
/* 11:   */   {
/* 12:11 */     GamePad gp = new GamePad();
/* 13:12 */     Orc orc = Orc.makeOrc();
/* 14:   */     
/* 15:14 */     boolean flipLeft = args.length > 2 ? Boolean.parseBoolean(args[1]) : false;
/* 16:15 */     boolean flipRight = args.length > 2 ? Boolean.parseBoolean(args[2]) : true;
/* 17:16 */     boolean flipMotors = args.length > 3 ? Boolean.parseBoolean(args[3]) : false;
/* 18:   */     
/* 19:18 */     Motor leftMotor = new Motor(orc, flipMotors ? 1 : 0, flipLeft);
/* 20:19 */     Motor rightMotor = new Motor(orc, flipMotors ? 0 : 1, flipRight);
/* 21:   */     
/* 22:21 */     System.out.println("flipLeft: " + flipLeft + ", flipRight: " + flipRight + ", flipMotors: " + flipMotors);
/* 23:   */     
/* 24:23 */     System.out.println("Hit any gamepad button to begin.");
/* 25:24 */     gp.waitForAnyButtonPress();
/* 26:   */     
/* 27:26 */     System.out.printf("%15s %15s %15s %15s\n", new Object[] { "left", "right", "left current", "right current" });
/* 28:   */     for (;;)
/* 29:   */     {
/* 30:30 */       double left = 0.0D;double right = 0.0D;
/* 31:   */       
/* 32:   */ 
/* 33:   */ 
/* 34:   */ 
/* 35:   */ 
/* 36:   */ 
/* 37:   */ 
/* 38:38 */       double fwd = -gp.getAxis(3);
/* 39:39 */       double lr = gp.getAxis(2);
/* 40:   */       
/* 41:41 */       left = fwd - lr;
/* 42:42 */       right = fwd + lr;
/* 43:   */       
/* 44:44 */       double max = Math.max(Math.abs(left), Math.abs(right));
/* 45:45 */       if (max > 1.0D)
/* 46:   */       {
/* 47:46 */         left /= max;
/* 48:47 */         right /= max;
/* 49:   */       }
/* 50:50 */       System.out.printf("%15f %15f %15f %15f\r", new Object[] { Double.valueOf(left), Double.valueOf(right), Double.valueOf(leftMotor.getCurrent()), Double.valueOf(rightMotor.getCurrent()) });
/* 51:51 */       leftMotor.setPWM(left);
/* 52:52 */       rightMotor.setPWM(right);
/* 53:   */       try
/* 54:   */       {
/* 55:55 */         Thread.sleep(30L);
/* 56:   */       }
/* 57:   */       catch (InterruptedException ex) {}
/* 58:   */     }
/* 59:   */   }
/* 60:   */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.examples.GamePadDrive
 * JD-Core Version:    0.7.0.1
 */