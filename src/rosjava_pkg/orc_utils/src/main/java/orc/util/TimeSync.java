/*   1:    */ package orc.util;
/*   2:    */ 
/*   3:    */ public class TimeSync
/*   4:    */ {
/*   5:    */   long last_device_ticks_wrapping;
/*   6:    */   long device_ticks_offset;
/*   7:    */   long device_ticks_wrap;
/*   8:    */   double device_ticks_per_second;
/*   9:    */   double rate_error;
/*  10:    */   double reset_time;
/*  11:    */   long p_ticks;
/*  12:    */   long q_ticks;
/*  13:    */   public double last_sync_error;
/*  14:    */   public int resync_count;
/*  15:    */   
/*  16:    */   public TimeSync(double device_ticks_per_second, long device_ticks_wrap, double rate_error, double reset_time)
/*  17:    */   {
/*  18: 58 */     this.p_ticks = -1L;
/*  19: 59 */     this.device_ticks_per_second = device_ticks_per_second;
/*  20: 60 */     this.device_ticks_wrap = device_ticks_wrap;
/*  21: 61 */     this.rate_error = rate_error;
/*  22: 62 */     this.reset_time = reset_time;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void update(long host_utime, long device_ticks_wrapping)
/*  26:    */   {
/*  27: 70 */     assert (device_ticks_wrapping >= 0L);
/*  28: 73 */     if (device_ticks_wrapping < this.last_device_ticks_wrapping) {
/*  29: 75 */       this.device_ticks_offset += this.device_ticks_wrap;
/*  30:    */     }
/*  31: 77 */     this.last_device_ticks_wrapping = device_ticks_wrapping;
/*  32:    */     
/*  33: 79 */     long device_ticks = this.device_ticks_offset + device_ticks_wrapping;
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50: 96 */     long pi_ticks = device_ticks;
/*  51: 97 */     long qi_ticks = host_utime;
/*  52:    */     
/*  53: 99 */     double dp = (pi_ticks - this.p_ticks) / this.device_ticks_per_second;
/*  54:100 */     double dq = (qi_ticks - this.q_ticks) / 1000000.0D;
/*  55:    */     
/*  56:102 */     this.last_sync_error = Math.abs(dp - dq);
/*  57:104 */     if ((this.p_ticks == -1L) || (this.last_sync_error >= this.reset_time))
/*  58:    */     {
/*  59:106 */       this.p_ticks = pi_ticks;
/*  60:107 */       this.q_ticks = qi_ticks;
/*  61:    */       
/*  62:    */ 
/*  63:110 */       this.resync_count += 1;
/*  64:111 */       return;
/*  65:    */     }
/*  66:114 */     if (dp >= dq - Math.abs(this.rate_error * dp))
/*  67:    */     {
/*  68:115 */       this.p_ticks = pi_ticks;
/*  69:116 */       this.q_ticks = qi_ticks;
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public long getHostUtime(long device_ticks_wrapping)
/*  74:    */   {
/*  75:126 */     assert (this.p_ticks != -1L);
/*  76:127 */     assert (device_ticks_wrapping >= 0L);
/*  77:    */     long device_ticks;
/*  78:    */     long device_ticks;
/*  79:145 */     if (device_ticks_wrapping <= this.last_device_ticks_wrapping) {
/*  80:147 */       device_ticks = this.device_ticks_offset + device_ticks_wrapping;
/*  81:    */     } else {
/*  82:152 */       device_ticks = this.device_ticks_offset + device_ticks_wrapping - this.device_ticks_wrap;
/*  83:    */     }
/*  84:155 */     long pi_ticks = device_ticks;
/*  85:    */     
/*  86:    */ 
/*  87:158 */     double dp = (pi_ticks - this.p_ticks) / this.device_ticks_per_second;
/*  88:    */     
/*  89:    */ 
/*  90:161 */     return (dp * 1000000.0D) + this.q_ticks + (1000000.0D * Math.abs(this.rate_error * dp));
/*  91:    */   }
/*  92:    */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.util.TimeSync
 * JD-Core Version:    0.7.0.1
 */