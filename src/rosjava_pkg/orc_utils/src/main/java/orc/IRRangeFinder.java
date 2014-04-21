/*   1:    */ package orc;
/*   2:    */ 
/*   3:    */ public class IRRangeFinder
/*   4:    */ {
/*   5:    */   AnalogInput ain;
/*   6:    */   double Xd;
/*   7:    */   double Xm;
/*   8:    */   double Xb;
/*   9: 22 */   double arcAngle = 0.08D;
/*  10: 23 */   double voltageStdDev = 0.025D;
/*  11:    */   
/*  12:    */   public IRRangeFinder(Orc orc, int port)
/*  13:    */   {
/*  14: 31 */     this.ain = new AnalogInput(orc, port);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public double[] getRangeAndUncertainty()
/*  18:    */   {
/*  19: 39 */     double v = this.ain.getVoltage();
/*  20:    */     
/*  21: 41 */     double range = this.Xm / (v - this.Xb) + this.Xd;
/*  22: 42 */     if (range < 0.0D) {
/*  23: 43 */       range = 0.0D;
/*  24:    */     }
/*  25: 44 */     if (range > 100.0D) {
/*  26: 45 */       range = 100.0D;
/*  27:    */     }
/*  28: 47 */     return new double[] { range, getRangeUncertainty(v) };
/*  29:    */   }
/*  30:    */   
/*  31:    */   public double getRange()
/*  32:    */   {
/*  33: 52 */     return getRangeAndUncertainty()[0];
/*  34:    */   }
/*  35:    */   
/*  36:    */   double getRangeUncertainty(double v)
/*  37:    */   {
/*  38: 85 */     double dddV = Math.abs(-this.Xm / (v - this.Xb) / (v - this.Xb));
/*  39:    */     
/*  40: 87 */     return this.voltageStdDev * dddV;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setParameters(double Xd, double Xm, double Xb, double voltageStdDev)
/*  44:    */   {
/*  45: 94 */     this.Xd = Xd;
/*  46: 95 */     this.Xm = Xm;
/*  47: 96 */     this.Xb = Xb;
/*  48: 97 */     this.voltageStdDev = voltageStdDev;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static IRRangeFinder make2Y0A02(Orc orc, int port)
/*  52:    */   {
/*  53:104 */     IRRangeFinder s = new IRRangeFinder(orc, port);
/*  54:105 */     s.Xd = -0.0618D;
/*  55:106 */     s.Xm = 0.7389D;
/*  56:107 */     s.Xb = -0.1141D;
/*  57:    */     
/*  58:109 */     return s;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static IRRangeFinder makeGP2D12(Orc orc, int port)
/*  62:    */   {
/*  63:118 */     IRRangeFinder s = new IRRangeFinder(orc, port);
/*  64:119 */     s.Xd = 0.0828D;
/*  65:120 */     s.Xm = 0.1384D;
/*  66:121 */     s.Xb = 0.2448D;
/*  67:    */     
/*  68:123 */     return s;
/*  69:    */   }
/*  70:    */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.IRRangeFinder
 * JD-Core Version:    0.7.0.1
 */