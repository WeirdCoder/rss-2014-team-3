package orc; 
import  java.io.PrintStream;
public class ADIS16350{
/*  7:   */   Orc orc;
/*  8: 7 */   int maxclk = 100000;
/*  9: 8 */   int spo = 1;
/* 10: 9 */   int sph = 1;
/* 11:10 */   int nbits = 16;
/* 12:   */   
/* 13:   */   public ADIS16350(final Orc paramOrc){
/* 14:   */   
/* 15:14 */   orc = paramOrc;
/* 16:   */   }
/* 17:   */   
/* 18:   */   int readRegister(final int addr){
/* 19:   */   
/* 20:19 */    int[] v  = orc.spiTransaction(this.maxclk, this.spo, this.sph, this.nbits, new int[] { addr << 8 });
/* 21:20 */     v = this.orc.spiTransaction(this.maxclk, this.spo, this.sph, this.nbits, new int[] { addr << 8 });
/* 22:   */     
/* 23:22 */     return v[(v.length - 1)];
/* 24:   */   }
/* 25:   */   
/* 26:   */   static int convert14(int v)
/* 27:   */   {
/* 28:27 */     v &= 0x3FFF;
/* 29:28 */     if ((v & 0x2000) > 0) {
/* 30:29 */       v |= 0xFFFFC000;
/* 31:   */     }
/* 32:30 */     return v;
/* 33:   */   }
/* 34:   */   
/* 35:   */   static int convert12(int v)
/* 36:   */   {
/* 37:35 */     v &= 0xFFF;
/* 38:36 */     if ((v & 0x800) > 0) {
/* 39:37 */       v |= 0xFFFFF000;
/* 40:   */     }
/* 41:38 */     return v;
/* 42:   */   }
/* 43:   */   
/* 44:   */   void writeRegister(int addr)
/* 45:   */   {
/* 46:43 */     int[] v = this.orc.spiTransaction(this.maxclk, this.spo, this.sph, this.nbits, new int[] { (addr | 0x80) << 8, 0 });
/* 47:   */   }
/* 48:   */   
/* 49:   */   public double[] readState()
/* 50:   */   {
/* 51:49 */     double[] x = new double[11];
/* 52:50 */     x[0] = (convert12(readRegister(2)) * 0.0018315D);
/* 53:51 */     x[1] = Math.toRadians(convert14(readRegister(4)) * 0.07326000000000001D);
/* 54:52 */     x[2] = Math.toRadians(convert14(readRegister(6)) * 0.07326000000000001D);
/* 55:53 */     x[3] = Math.toRadians(convert14(readRegister(8)) * 0.07326000000000001D);
/* 56:54 */     x[4] = (convert14(readRegister(10)) * 0.002522D);
/* 57:55 */     x[5] = (convert14(readRegister(12)) * 0.002522D);
/* 58:56 */     x[6] = (convert14(readRegister(14)) * 0.002522D);
/* 59:57 */     x[7] = (convert12(readRegister(16)) * 0.1453D + 25.0D);
/* 60:58 */     x[8] = (convert12(readRegister(18)) * 0.1453D + 25.0D);
/* 61:59 */     x[9] = (convert12(readRegister(20)) * 0.1453D + 25.0D);
/* 62:60 */     x[10] = (convert12(readRegister(22)) * 0.0006105D);
/* 63:   */     
/* 64:62 */     return x;
/* 65:   */   }
/* 66:   */   
/* 67:   */   public static void main(String[] args)
/* 68:   */   {
/* 69:67 */     Orc orc = Orc.makeOrc();
/* 70:68 */     ADIS16350 adis = new ADIS16350(orc);
/* 71:   */     
/* 72:70 */     double zpos = 0.0D;
/* 73:   */     
/* 74:72 */     long lastTime = System.currentTimeMillis();
/* 75:   */     for (;;)
/* 76:   */     {
/* 77:75 */       System.out.printf("%20.5f ", new Object[] { Double.valueOf(System.currentTimeMillis() / 1000.0D) });
/* 78:   */       
/* 79:   */ 
/* 80:78 */       double[] state = adis.readState();
/* 81:80 */       for (int i = 0; i < state.length; i++) {
/* 82:81 */         System.out.printf("%15f", new Object[] { Double.valueOf(state[i]) });
/* 83:   */       }
/* 84:85 */       System.out.printf("\n", new Object[0]);
/* 85:   */       try
/* 86:   */       {
/* 87:88 */         Thread.sleep(5L);
/* 88:   */       }
/* 89:   */       catch (InterruptedException ex) {}
/* 90:   */     }
/* 91:   */   }
/* 92:   */ }

