/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class TLC3548
/*  6:   */ {
/*  7:   */   Orc orc;
/*  8: 7 */   int maxclk = 2500000;
/*  9: 8 */   int spo = 0;
/* 10: 9 */   int sph = 1;
/* 11:10 */   int nbits = 16;
/* 12:11 */   boolean shortSampling = false;
/* 13:13 */   boolean externalReference = true;
/* 14:   */   
/* 15:   */   public TLC3548(Orc orc)
/* 16:   */   {
/* 17:17 */     this.orc = orc;
/* 18:   */     
/* 19:   */ 
/* 20:20 */     orc.spiTransaction(this.maxclk, this.spo, this.sph, this.nbits, new int[] { 40960 });
/* 21:   */     
/* 22:22 */     int flag = 0;
/* 23:23 */     if (this.externalReference) {
/* 24:24 */       flag |= 0x800;
/* 25:   */     }
/* 26:25 */     if (this.shortSampling) {
/* 27:26 */       flag |= 0x200;
/* 28:   */     }
/* 29:28 */     orc.spiTransaction(this.maxclk, this.spo, this.sph, this.nbits, new int[] { 0xA000 | flag });
/* 30:   */   }
/* 31:   */   
/* 32:   */   public int beginConversion(int port)
/* 33:   */   {
/* 34:34 */     int[] rx = this.orc.spiTransaction(this.maxclk, this.spo, this.sph, this.nbits, new int[] { port << 12, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
/* 35:   */     
/* 36:36 */     return rx[0];
/* 37:   */   }
/* 38:   */   
/* 39:   */   public static void main(String[] args)
/* 40:   */   {
/* 41:41 */     Orc orc = Orc.makeOrc();
/* 42:42 */     TLC3548 tlc = new TLC3548(orc);
/* 43:   */     for (;;)
/* 44:   */     {
/* 45:45 */       for (int i = 0; i < 8; i++) {
/* 46:46 */         System.out.printf("%04x  ", new Object[] { Integer.valueOf(tlc.beginConversion(i)) });
/* 47:   */       }
/* 48:47 */       System.out.printf("\n", new Object[0]);
/* 49:   */       try
/* 50:   */       {
/* 51:51 */         Thread.sleep(20L);
/* 52:   */       }
/* 53:   */       catch (InterruptedException ex) {}
/* 54:   */     }
/* 55:   */   }
/* 56:   */ }

