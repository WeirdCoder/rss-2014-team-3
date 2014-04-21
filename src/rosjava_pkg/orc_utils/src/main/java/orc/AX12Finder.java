/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ 
/*  6:   */ public class AX12Finder
/*  7:   */ {
/*  8:   */   public static void main(String[] args)
/*  9:   */   {
/* 10:10 */     Orc orc = Orc.makeOrc();
/* 11:   */     
/* 12:12 */     ArrayList<Integer> servoIDs = new ArrayList();
/* 13:   */     
/* 14:14 */     System.out.println("Attempting to find servos: ");
/* 15:15 */     for (int i = 1; i <= 253; i++)
/* 16:   */     {
/* 17:16 */       AX12Servo servo = new AX12Servo(orc, i);
/* 18:   */       
/* 19:18 */       boolean result = servo.ping();
/* 20:20 */       if (result)
/* 21:   */       {
/* 22:21 */         servoIDs.add(Integer.valueOf(i));
/* 23:22 */         System.out.print("o");
/* 24:   */       }
/* 25:   */       else
/* 26:   */       {
/* 27:24 */         System.out.print("x");
/* 28:   */       }
/* 29:27 */       if (i % 64 == 0) {
/* 30:28 */         System.out.println();
/* 31:   */       }
/* 32:   */     }
/* 33:31 */     System.out.print("\nFound servos: [");
/* 34:32 */     for (Integer i : servoIDs) {
/* 35:33 */       System.out.print(i + ",");
/* 36:   */     }
/* 37:35 */     System.out.println("]");
/* 38:   */   }
/* 39:   */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.AX12Finder
 * JD-Core Version:    0.7.0.1
 */