/*   1:    */ package orc;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ 
/*   5:    */ public class Gyro
/*   6:    */ {
/*   7:    */   Orc orc;
/*   8:    */   int port;
/*   9:    */   double SAMPLE_HZ;
/*  10:    */   double v0;
/*  11:    */   double mvPerDegPerSec;
/*  12: 23 */   double radPerSecPerLSB = 5.32638E-005D;
/*  13:    */   long integratorOffset;
/*  14:    */   int integratorCountOffset;
/*  15:    */   boolean calibrated;
/*  16: 30 */   double voltsPerLSB = 6.103515625E-005D;
/*  17: 32 */   double theta = 0.0D;
/*  18:    */   
/*  19:    */   public Gyro(Orc orc, int port)
/*  20:    */   {
/*  21: 37 */     this(orc, port, 0.005D);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Gyro(Orc orc, int port, double voltsPerDegPerSec)
/*  25:    */   {
/*  26: 42 */     assert ((port >= 0) && (port <= 2));
/*  27: 43 */     this.orc = orc;
/*  28: 44 */     this.port = port;
/*  29:    */     
/*  30: 46 */     double lsbPerDegPerSec = 1.0D / this.voltsPerLSB * voltsPerDegPerSec;
/*  31: 47 */     double degPerSecPerLSB = 1.0D / lsbPerDegPerSec;
/*  32: 48 */     this.radPerSecPerLSB = Math.toRadians(degPerSecPerLSB);
/*  33:    */     
/*  34: 50 */     reset();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void reset()
/*  38:    */   {
/*  39: 56 */     OrcStatus status = this.orc.getStatus();
/*  40: 57 */     this.integratorOffset = status.gyroIntegrator[this.port];
/*  41: 58 */     this.integratorCountOffset = status.gyroIntegratorCount[this.port];
/*  42: 59 */     this.theta = 0.0D;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public synchronized double getTheta()
/*  46:    */   {
/*  47: 64 */     if (!this.calibrated)
/*  48:    */     {
/*  49: 65 */       System.out.println("orc.Gyro: Must calibrate before calling getTheta!()");
/*  50: 66 */       this.calibrated = true;
/*  51: 67 */       return this.theta;
/*  52:    */     }
/*  53: 70 */     OrcStatus s = this.orc.getStatus();
/*  54:    */     
/*  55: 72 */     double integrator = s.gyroIntegrator[this.port] - this.integratorOffset;
/*  56: 73 */     double integratorCount = s.gyroIntegratorCount[this.port] - this.integratorCountOffset;
/*  57: 75 */     if (integratorCount == 0.0D) {
/*  58: 78 */       return this.theta;
/*  59:    */     }
/*  60: 81 */     double dt = integratorCount / this.SAMPLE_HZ;
/*  61: 82 */     double averageIntegrator = integrator / integratorCount - this.v0;
/*  62:    */     
/*  63:    */ 
/*  64:    */ 
/*  65: 86 */     this.theta += averageIntegrator * dt * this.radPerSecPerLSB;
/*  66:    */     
/*  67: 88 */     this.integratorOffset = s.gyroIntegrator[this.port];
/*  68: 89 */     this.integratorCountOffset = s.gyroIntegratorCount[this.port];
/*  69:    */     
/*  70: 91 */     return this.theta;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void calibrate(double seconds)
/*  74:    */   {
/*  75:100 */     OrcStatus s0 = this.orc.getStatus();
/*  76:    */     try
/*  77:    */     {
/*  78:103 */       Thread.sleep((int)(seconds * 1000.0D));
/*  79:    */     }
/*  80:    */     catch (InterruptedException ex) {}
/*  81:107 */     OrcStatus s1 = this.orc.getStatus();
/*  82:    */     
/*  83:109 */     double dt = (s1.utimeOrc - s0.utimeOrc) / 1000000.0D;
/*  84:110 */     double dv = s1.gyroIntegrator[this.port] - s0.gyroIntegrator[this.port];
/*  85:111 */     double ds = s1.gyroIntegratorCount[this.port] - s0.gyroIntegratorCount[this.port];
/*  86:112 */     this.SAMPLE_HZ = (ds / dt);
/*  87:113 */     this.v0 = (dv / ds);
/*  88:    */     
/*  89:115 */     System.out.printf("Requested calib t: %15f seconds\n", new Object[] { Double.valueOf(seconds) });
/*  90:116 */     System.out.printf("Actual calib t:    %15f seconds\n", new Object[] { Double.valueOf(dt) });
/*  91:117 */     System.out.printf("Integrator change: %15.1f ADC LSBs\n", new Object[] { Double.valueOf(dv) });
/*  92:118 */     System.out.printf("Integrator counts: %15.1f counts\n", new Object[] { Double.valueOf(ds) });
/*  93:119 */     System.out.printf("Sample rate:       %15f Hz\n", new Object[] { Double.valueOf(this.SAMPLE_HZ) });
/*  94:120 */     System.out.printf("calibrated at:     %15f ADC LSBs (about %f V)\n", new Object[] { Double.valueOf(this.v0), Double.valueOf(this.v0 / 65535.0D * 5.0D) });
/*  95:    */     
/*  96:122 */     this.calibrated = true;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static void main(String[] args)
/* 100:    */   {
/* 101:129 */     int port = 0;
/* 102:130 */     Orc orc = Orc.makeOrc();
/* 103:131 */     Gyro gyro = new Gyro(orc, port);
/* 104:132 */     AnalogInput ain = new AnalogInput(orc, port);
/* 105:    */     
/* 106:134 */     double calibrateTime = 3.0D;
/* 107:135 */     System.out.println("Calibrating for " + calibrateTime + " seconds...");
/* 108:136 */     gyro.calibrate(calibrateTime);
/* 109:    */     
/* 110:138 */     double starttime = System.currentTimeMillis() / 1000.0D;
/* 111:    */     for (;;)
/* 112:    */     {
/* 113:141 */       double rad = gyro.getTheta();
/* 114:142 */       double dt = System.currentTimeMillis() / 1000.0D - starttime;
/* 115:    */       
/* 116:144 */       System.out.printf("\r t=%15f V=%15f theta=%15f rad (%15f deg)", new Object[] { Double.valueOf(dt), Double.valueOf(ain.getVoltage()), Double.valueOf(rad), Double.valueOf(Math.toDegrees(rad)) });
/* 117:    */       try
/* 118:    */       {
/* 119:146 */         Thread.sleep(100L);
/* 120:    */       }
/* 121:    */       catch (InterruptedException ex) {}
/* 122:    */     }
/* 123:    */   }
/* 124:    */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.Gyro
 * JD-Core Version:    0.7.0.1
 */