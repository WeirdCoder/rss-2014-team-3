/*  1:   */ package orc;
/*  2:   */ 
/*  3:   */ public class Nunchuck
/*  4:   */ {
/*  5:   */   Orc orc;
/*  6:35 */   byte[] writeme2 = new byte[1];
/*  7:36 */   byte[] readme2 = new byte[20];
/*  8:37 */   byte addrss = 82;
/*  9:   */   byte writeleng;
/* 10:   */   byte readleng;
/* 11:   */   static final int I2C_ADDRESS = -92;
/* 12:   */   
/* 13:   */   public Nunchuck(Orc orc)
/* 14:   */   {
/* 15:46 */     this.orc = orc;
/* 16:47 */     this.writeme2[0] = 0;
/* 17:   */     
/* 18:49 */     orc.i2cTransaction(-92, new Object[] { { 64, 0 }, Integer.valueOf(0) });
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int[] readState()
/* 22:   */   {
/* 23:57 */     this.orc.i2cTransaction(-92, new Object[] { { 0 }, Integer.valueOf(0) });
/* 24:58 */     byte[] resp = this.orc.i2cTransaction(-92, new Object[] { null, Integer.valueOf(6) });
/* 25:   */     
/* 26:   */ 
/* 27:   */ 
/* 28:   */ 
/* 29:   */ 
/* 30:   */ 
/* 31:   */ 
/* 32:   */ 
/* 33:67 */     int[] state = new int[6];
/* 34:68 */     state[0] = ((resp[0] & 0xFF ^ 0x17) + 23);
/* 35:69 */     state[1] = ((resp[1] & 0xFF ^ 0x17) + 23);
/* 36:   */     
/* 37:71 */     state[2] = ((resp[2] & 0xFF ^ 0x17) + 23);
/* 38:72 */     state[3] = ((resp[3] & 0xFF ^ 0x17) + 23);
/* 39:73 */     state[4] = ((resp[4] & 0xFF ^ 0x17) + 23);
/* 40:   */     
/* 41:75 */     state[5] = ((resp[5] & 0xFF ^ 0x17) + 23 & 0x3);
/* 42:   */     
/* 43:77 */     return state;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public int[] readJoystick()
/* 47:   */   {
/* 48:82 */     int[] v = readState();
/* 49:83 */     return new int[] { v[0], v[1] };
/* 50:   */   }
/* 51:   */   
/* 52:   */   public int[] readAccelerometers()
/* 53:   */   {
/* 54:88 */     int[] v = readState();
/* 55:89 */     return new int[] { v[2], v[3], v[4] };
/* 56:   */   }
/* 57:   */   
/* 58:   */   public int readButtons()
/* 59:   */   {
/* 60:94 */     int[] v = readState();
/* 61:95 */     return v[5];
/* 62:   */   }
/* 63:   */ }

