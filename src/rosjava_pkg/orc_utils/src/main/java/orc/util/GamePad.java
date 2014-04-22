/*   1:    */ package orc.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ 
/*   8:    */ public class GamePad
/*   9:    */ {
/*  10:    */   String devicePath;
/*  11: 10 */   int[] axes = new int[16];
/*  12: 11 */   int[] buttons = new int[16];
/*  13:    */   
/*  14:    */   public GamePad()
/*  15:    */   {
/*  16: 16 */     String[] paths = { "/dev/js0", "/dev/input/js0" };
/*  17: 20 */     for (int i = 0; i < paths.length; i++)
/*  18:    */     {
/*  19: 21 */       String path = paths[i];
/*  20: 22 */       File f = new File(path);
/*  21: 23 */       if (f.exists())
/*  22:    */       {
/*  23: 24 */         this.devicePath = path;
/*  24: 25 */         break;
/*  25:    */       }
/*  26:    */     }
/*  27: 29 */     if (this.devicePath == null)
/*  28:    */     {
/*  29: 30 */       System.out.println("Couldn't find a joystick.");
/*  30: 31 */       System.exit(-1);
/*  31:    */     }
/*  32: 34 */     new ReaderThread().start();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public GamePad(String path)
/*  36:    */   {
/*  37: 39 */     this.devicePath = path;
/*  38: 40 */     new ReaderThread().start();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getAxis(int axis)
/*  42:    */   {
/*  43: 46 */     if (axis >= this.axes.length) {
/*  44: 47 */       return 0.0D;
/*  45:    */     }
/*  46: 49 */     return this.axes[axis] / 32767.0D;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean getButton(int button)
/*  50:    */   {
/*  51: 54 */     if (button >= this.buttons.length) {
/*  52: 55 */       return false;
/*  53:    */     }
/*  54: 57 */     return this.buttons[button] > 0;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int waitForAnyButtonPress()
/*  58:    */   {
/*  59: 66 */     boolean[] buttonState = new boolean[16];
/*  60: 67 */     for (int i = 0; i < buttonState.length; i++) {
/*  61: 68 */       buttonState[i] = getButton(i);
/*  62:    */     }
/*  63:    */     for (;;)
/*  64:    */     {
/*  65: 72 */       for (int i = 0; i < buttonState.length; i++) {
/*  66: 73 */         if (getButton(i) != buttonState[i]) {
/*  67: 74 */           return i;
/*  68:    */         }
/*  69:    */       }
/*  70:    */       try
/*  71:    */       {
/*  72: 77 */         Thread.sleep(10L);
/*  73:    */       }
/*  74:    */       catch (InterruptedException ex) {}
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   class ReaderThread
/*  79:    */     extends Thread
/*  80:    */   {
/*  81:    */     ReaderThread()
/*  82:    */     {
/*  83: 87 */       setDaemon(true);
/*  84:    */     }
/*  85:    */     
/*  86:    */     public void run()
/*  87:    */     {
/*  88:    */       try
/*  89:    */       {
/*  90: 93 */         runEx();
/*  91:    */       }
/*  92:    */       catch (IOException ex)
/*  93:    */       {
/*  94: 95 */         System.out.println("GamePad ex: " + ex);
/*  95:    */       }
/*  96:    */     }
/*  97:    */     
/*  98:    */     public void runEx()
/*  99:    */       throws IOException
/* 100:    */     {
/* 101:101 */       FileInputStream fins = new FileInputStream(new File(GamePad.this.devicePath));
/* 102:102 */       byte[] buf = new byte[8];
/* 103:    */       for (;;)
/* 104:    */       {
/* 105:105 */         fins.read(buf);
/* 106:    */         
/* 107:107 */         int mstime = buf[0] & 0xFF | (buf[1] & 0xFF) << 8 | (buf[2] & 0xFF) << 16 | (buf[3] & 0xFF) << 24;
/* 108:108 */         int value = buf[4] & 0xFF | (buf[5] & 0xFF) << 8;
/* 109:110 */         if ((value & 0x8000) > 0) {
/* 110:111 */           value |= 0xFFFF0000;
/* 111:    */         }
/* 112:113 */         int type = buf[6] & 0xFF;
/* 113:114 */         int number = buf[7] & 0xFF;
/* 114:116 */         if ((type & 0x3) == 1) {
/* 115:117 */           if (number < GamePad.this.buttons.length) {
/* 116:118 */             GamePad.this.buttons[number] = value;
/* 117:    */           } else {
/* 118:120 */             System.out.println("GamePad: " + number + " buttons!");
/* 119:    */           }
/* 120:    */         }
/* 121:123 */         if ((type & 0x3) == 2) {
/* 122:124 */           if (number < GamePad.this.axes.length) {
/* 123:125 */             GamePad.this.axes[number] = value;
/* 124:    */           } else {
/* 125:127 */             System.out.println("GamePad: " + number + " axes!");
/* 126:    */           }
/* 127:    */         }
/* 128:    */       }
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static void main(String[] args)
/* 133:    */   {
/* 134:136 */     GamePad gp = new GamePad();
/* 135:    */     
/* 136:138 */     int nAxes = 6;
/* 137:139 */     int nButtons = 16;
/* 138:141 */     for (int i = 0; i < nAxes; i++) {
/* 139:142 */       System.out.printf("%10s ", new Object[] { "Axis " + i });
/* 140:    */     }
/* 141:145 */     for (int i = 0; i < nButtons; i++)
/* 142:    */     {
/* 143:146 */       int v = i & 0xF;
/* 144:147 */       System.out.printf("%c", new Object[] { Integer.valueOf(v >= 10 ? 97 + (v - 10) : 48 + v) });
/* 145:    */     }
/* 146:149 */     System.out.printf("\n", new Object[0]);
/* 147:    */     for (;;)
/* 148:    */     {
/* 149:152 */       String s = "";
/* 150:153 */       for (int i = 0; i < nButtons; i++) {
/* 151:154 */         s = s + (gp.getButton(i) ? 1 : 0);
/* 152:    */       }
/* 153:156 */       System.out.printf("\r", new Object[0]);
/* 154:157 */       for (int i = 0; i < nAxes; i++) {
/* 155:158 */         System.out.printf("%10f ", new Object[] { Double.valueOf(gp.getAxis(i)) });
/* 156:    */       }
/* 157:159 */       System.out.printf("%s", new Object[] { s });
/* 158:    */       try
/* 159:    */       {
/* 160:162 */         Thread.sleep(10L);
/* 161:    */       }
/* 162:    */       catch (InterruptedException ex) {}
/* 163:    */     }
/* 164:    */   }
/* 165:    */ }

