/*   1:    */ package orc;
/*   2:    */ 
/*   3:    */ import java.io.DataInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ 
/*   7:    */ public class AX12Servo
/*   8:    */ {
/*   9:    */   Orc orc;
/*  10:    */   int id;
/*  11: 10 */   public boolean verbose = false;
/*  12:    */   static final int INST_PING = 1;
/*  13:    */   static final int INST_READ_DATA = 2;
/*  14:    */   static final int INST_WRITE_DATA = 3;
/*  15:    */   static final int INST_REG_WRITE = 4;
/*  16:    */   static final int INST_ACTION = 5;
/*  17:    */   static final int INST_RESET_DATA = 6;
/*  18:    */   static final int INST_SYNC_WRITE = 131;
/*  19:    */   static final int ADDR_ID = 3;
/*  20:    */   public static final int ERROR_INSTRUCTION = 64;
/*  21:    */   public static final int ERROR_OVERLOAD = 32;
/*  22:    */   public static final int ERROR_CHECKSUM = 16;
/*  23:    */   public static final int ERROR_RANGE = 8;
/*  24:    */   public static final int ERROR_OVERHEAT = 4;
/*  25:    */   public static final int ERROR_ANGLE_LIMIT = 2;
/*  26:    */   public static final int ERROR_VOLTAGE = 1;
/*  27:    */   public static final int BROADCAST_ADDRESS = 254;
/*  28: 32 */   double pos0 = 0.0D;
/*  29: 32 */   double val0 = 0.0D;
/*  30: 32 */   double pos1 = 300.0D;
/*  31: 32 */   double val1 = 300.0D;
/*  32:    */   
/*  33:    */   public AX12Servo(Orc orc, int id)
/*  34:    */   {
/*  35: 36 */     this.orc = orc;
/*  36: 37 */     this.id = id;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public AX12Servo(Orc orc, int id, double pos0, double val0, double pos1, double val1)
/*  40:    */   {
/*  41: 42 */     this(orc, id);
/*  42: 43 */     remap(pos0, val0, pos1, val1);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void remap(double pos0, double val0, double pos1, double val1)
/*  46:    */   {
/*  47: 58 */     this.pos0 = pos0;
/*  48: 59 */     this.val0 = val0;
/*  49: 60 */     this.pos1 = pos1;
/*  50: 61 */     this.val1 = val1;
/*  51:    */   }
/*  52:    */   
/*  53:    */   double map(double val)
/*  54:    */   {
/*  55: 67 */     double x = (val - this.val0) / (this.val1 - this.val0);
/*  56: 68 */     return this.pos0 + x * (this.pos1 - this.pos0);
/*  57:    */   }
/*  58:    */   
/*  59:    */   double unmap(double pos)
/*  60:    */   {
/*  61: 73 */     double x = (pos - this.pos0) / (this.pos1 - this.pos0);
/*  62: 74 */     return this.val0 + x * (this.val1 - this.val0);
/*  63:    */   }
/*  64:    */   
/*  65:    */   static byte[] makeCommand(int id, int instruction, byte[] parameters)
/*  66:    */   {
/*  67: 79 */     int parameterlen = parameters == null ? 0 : parameters.length;
/*  68: 80 */     byte[] cmd = new byte[6 + parameterlen];
/*  69:    */     
/*  70: 82 */     cmd[0] = -1;
/*  71: 83 */     cmd[1] = -1;
/*  72: 84 */     cmd[2] = ((byte)id);
/*  73: 85 */     cmd[3] = ((byte)(parameterlen + 2));
/*  74: 86 */     cmd[4] = ((byte)instruction);
/*  75: 88 */     if (parameters != null) {
/*  76: 89 */       for (int i = 0; i < parameters.length; i++) {
/*  77: 90 */         cmd[(5 + i)] = parameters[i];
/*  78:    */       }
/*  79:    */     }
/*  80: 95 */     int checksum = 0;
/*  81: 96 */     for (int i = 2; i < cmd.length - 1; i++) {
/*  82: 97 */       checksum += (cmd[i] & 0xFF);
/*  83:    */     }
/*  84: 99 */     cmd[(5 + parameterlen)] = ((byte)(checksum ^ 0xFF));
/*  85:    */     
/*  86:    */ 
/*  87:102 */     return cmd;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void changeServoID(int oldID, int newID)
/*  91:    */   {
/*  92:114 */     OrcResponse resp = this.orc.doCommand(16808448, makeCommand((byte)(oldID & 0xFF), 3, new byte[] { 3, (byte)(newID & 0xFF) }));
/*  93:118 */     if (this.verbose) {
/*  94:119 */       resp.print();
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public boolean ping()
/*  99:    */   {
/* 100:124 */     OrcResponse resp = this.orc.doCommand(16808448, makeCommand(this.id, 1, null));
/* 101:126 */     if (this.verbose) {
/* 102:127 */       resp.print();
/* 103:    */     }
/* 104:129 */     byte[] tmp = { 6, -1, -1, (byte)this.id, 2, 0 };
/* 105:131 */     for (int i = 0; i < tmp.length; i++) {
/* 106:132 */       if (tmp[i] != resp.responseBuffer[(resp.responseBufferOffset + i)]) {
/* 107:133 */         return false;
/* 108:    */       }
/* 109:    */     }
/* 110:135 */     return true;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setGoalDegrees(double deg, double speedfrac, double torquefrac)
/* 114:    */   {
/* 115:144 */     deg = map(deg);
/* 116:145 */     int posv = (int)(1023.0D * deg / 300.0D);
/* 117:146 */     int speedv = (int)(1023.0D * speedfrac);
/* 118:147 */     int torquev = (int)(1023.0D * torquefrac);
/* 119:    */     
/* 120:149 */     OrcResponse resp = this.orc.doCommand(16808448, makeCommand(this.id, 3, new byte[] { 30, (byte)(posv & 0xFF), (byte)(posv >> 8), (byte)(speedv & 0xFF), (byte)(speedv >> 8), (byte)(torquev & 0xFF), (byte)(torquev >> 8) }));
/* 121:155 */     if (this.verbose) {
/* 122:156 */       resp.print();
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   public AX12Status getStatus()
/* 127:    */   {
/* 128:161 */     AX12Status status = new AX12Status();
/* 129:    */     for (;;)
/* 130:    */     {
/* 131:165 */       OrcResponse resp = this.orc.doCommand(16808448, makeCommand(this.id, 2, new byte[] { 36, 8 }));
/* 132:168 */       if (this.verbose) {
/* 133:169 */         resp.print();
/* 134:    */       }
/* 135:171 */       DataInputStream ins = resp.ins;
/* 136:    */       try
/* 137:    */       {
/* 138:174 */         int len = ins.read();
/* 139:175 */         if (len == 14)
/* 140:    */         {
/* 141:178 */           int ff = ins.read();
/* 142:179 */           if (ff == 255)
/* 143:    */           {
/* 144:181 */             ff = ins.read();
/* 145:182 */             if (ff == 255)
/* 146:    */             {
/* 147:185 */               int id = ins.read();
/* 148:186 */               if (id == this.id)
/* 149:    */               {
/* 150:189 */                 int paramlen = ins.read();
/* 151:190 */                 if (paramlen == 10)
/* 152:    */                 {
/* 153:193 */                   status.error_flags = ins.read();
/* 154:    */                   
/* 155:195 */                   status.positionDegrees = unmap(((ins.read() & 0xFF) + ((ins.read() & 0x3F) << 8)) * 300.0D / 1024.0D);
/* 156:    */                   
/* 157:    */ 
/* 158:    */ 
/* 159:199 */                   int ispeed = (ins.read() & 0xFF) + ((ins.read() & 0xFF) << 8);
/* 160:200 */                   status.speed = ((ispeed & 0x3FF) / 1024.0D);
/* 161:201 */                   if ((ispeed & 0x400) != 0) {
/* 162:202 */                     status.speed *= -1.0D;
/* 163:    */                   }
/* 164:204 */                   int iload = (ins.read() & 0xFF) + ((ins.read() & 0xFF) << 8);
/* 165:205 */                   status.load = ((iload & 0x3FF) / 1024.0D);
/* 166:206 */                   if ((iload & 0x400) != 0) {
/* 167:207 */                     status.load *= -1.0D;
/* 168:    */                   }
/* 169:209 */                   status.utimeOrc = resp.utimeOrc;
/* 170:210 */                   status.utimeHost = resp.utimeHost;
/* 171:211 */                   status.voltage = ((ins.read() & 0xFF) / 10.0D);
/* 172:212 */                   status.temperature = (ins.read() & 0xFF);
/* 173:    */                 }
/* 174:    */               }
/* 175:    */             }
/* 176:    */           }
/* 177:    */         }
/* 178:    */       }
/* 179:    */       catch (IOException ex) {}
/* 180:    */     }
/* 181:218 */     return status;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public int getId()
/* 185:    */   {
/* 186:224 */     return this.id;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static void main(String[] args)
/* 190:    */   {
/* 191:229 */     Orc orc = Orc.makeOrc();
/* 192:    */     
/* 193:231 */     AX12Servo servo = new AX12Servo(orc, 1);
/* 194:232 */     for (int iter = 0;; iter++)
/* 195:    */     {
/* 196:233 */       System.out.printf("%d\n", new Object[] { Integer.valueOf(iter) });
/* 197:    */       
/* 198:235 */       servo.ping();
/* 199:    */       
/* 200:237 */       AX12Status st = servo.getStatus();
/* 201:238 */       st.print();
/* 202:    */       
/* 203:240 */       System.out.println("**");
/* 204:241 */       servo.setGoalDegrees(150.0D, 0.1D, 0.1D);
/* 205:    */       try
/* 206:    */       {
/* 207:243 */         Thread.sleep(500L);
/* 208:    */       }
/* 209:    */       catch (InterruptedException ex) {}
/* 210:247 */       servo.setGoalDegrees(160.0D, 0.1D, 0.1D);
/* 211:    */       try
/* 212:    */       {
/* 213:249 */         Thread.sleep(500L);
/* 214:    */       }
/* 215:    */       catch (InterruptedException ex) {}
/* 216:    */     }
/* 217:    */   }
/* 218:    */ }
