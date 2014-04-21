/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class AX12ChangeID
/*  6:   */ {
/*  7:   */   public static void main(String[] args)
/*  8:   */   {
/*  9:13 */     int oldID = 1;int newID = 2;
/* 10:14 */     Orc orc = Orc.makeOrc();
/* 11:17 */     if (args != null)
/* 12:   */     {
/* 13:19 */       System.out.println("length = " + args.length);
/* 14:20 */       if (args.length == 1)
/* 15:   */       {
/* 16:22 */         Integer i = new Integer(args[0]);
/* 17:23 */         newID = i.intValue();
/* 18:   */       }
/* 19:25 */       else if (args.length == 2)
/* 20:   */       {
/* 21:27 */         System.out.println(args[0]);
/* 22:28 */         Integer i = new Integer(args[0]);
/* 23:29 */         oldID = i.intValue();
/* 24:30 */         i = new Integer(args[1]);
/* 25:31 */         newID = i.intValue();
/* 26:   */       }
/* 27:   */     }
/* 28:34 */     System.out.println("oldID=" + oldID + "\t\tnewID=" + newID);
/* 29:35 */     AX12Servo servo = new AX12Servo(orc, oldID);
/* 30:36 */     if (servo.ping())
/* 31:   */     {
/* 32:38 */       System.out.println("GOOD Ping");
/* 33:39 */       servo.changeServoID(oldID, newID);
/* 34:   */     }
/* 35:   */     else
/* 36:   */     {
/* 37:42 */       System.out.println("BAD Ping");
/* 38:   */     }
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.AX12ChangeID
 * JD-Core Version:    0.7.0.1
 */