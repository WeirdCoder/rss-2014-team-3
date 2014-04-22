/*   1:    */ package orc.spy;
/*   2:    */ 
/*   3:    */ import java.awt.BorderLayout;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.Font;
/*   7:    */ import java.awt.FontMetrics;
/*   8:    */ import java.awt.Graphics;
/*   9:    */ import java.awt.Graphics2D;
/*  10:    */ import java.awt.GridLayout;
/*  11:    */ import java.awt.event.ActionEvent;
/*  12:    */ import java.awt.event.ActionListener;
/*  13:    */ import java.awt.event.WindowAdapter;
/*  14:    */ import java.awt.event.WindowEvent;
/*  15:    */ import java.io.PrintStream;
/*  16:    */ import javax.swing.JCheckBox;
/*  17:    */ import javax.swing.JComponent;
/*  18:    */ import javax.swing.JDesktopPane;
/*  19:    */ import javax.swing.JFrame;
/*  20:    */ import javax.swing.JInternalFrame;
/*  21:    */ import javax.swing.JLabel;
/*  22:    */ import javax.swing.JPanel;
/*  23:    */ import orc.Motor;
/*  24:    */ import orc.Orc;
/*  25:    */ import orc.OrcStatus;
/*  26:    */ import orc.Servo;
/*  27:    */ 
/*  28:    */ public class Spy
/*  29:    */ {
/*  30:    */   JFrame jf;
/*  31: 15 */   JDesktopPane jdp = new JDesktopPane();
/*  32:    */   TextPanelWidget basicDisplay;
/*  33:    */   TextPanelWidget analogDisplay;
/*  34:    */   TextPanelWidget qeiDisplay;
/*  35:    */   TextPanelWidget digDisplay;
/*  36:    */   MotorPanel[] motorPanels;
/*  37:    */   ServoPanel[] servoPanels;
/*  38:    */   Orc orc;
/*  39:    */   
/*  40:    */   public static void main(String[] args)
/*  41:    */   {
/*  42: 25 */     new Spy(args);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Spy(String[] args)
/*  46:    */   {
/*  47: 30 */     if (args.length > 0) {
/*  48: 31 */       this.orc = Orc.makeOrc(args[0]);
/*  49:    */     } else {
/*  50: 33 */       this.orc = Orc.makeOrc();
/*  51:    */     }
/*  52: 35 */     this.jf = new JFrame("OrcSpy: " + this.orc.getAddress());
/*  53: 36 */     this.jf.setLayout(new BorderLayout());
/*  54: 37 */     this.jf.add(this.jdp, "Center");
/*  55:    */     
/*  56: 39 */     this.basicDisplay = new TextPanelWidget(new String[] { "Parameter", "Value" }, new String[][] { { "uorc time", "0" } }, new double[] { 0.4D, 0.6D });
/*  57:    */     
/*  58:    */ 
/*  59:    */ 
/*  60: 43 */     this.analogDisplay = new TextPanelWidget(new String[] { "Port", "Raw value", "Value", "LPF Value" }, new String[][] { { "0", "0", "0", "0" }, { "1", "0", "0", "0" }, { "2", "0", "0", "0" }, { "3", "0", "0", "0" }, { "4", "0", "0", "0" }, { "5", "0", "0", "0" }, { "6", "0", "0", "0" }, { "7", "0", "0", "0" }, { " 8 (mot0)", "0", "0", "0" }, { " 9 (mot1)", "0", "0", "0" }, { "10 (mot2)", "0", "0", "0" }, { "11 (vbat)", "0", "0", "0" }, { "12 (temp)", "0", "0", "0" } }, new double[] { 0.3D, 0.5D, 0.5D, 0.5D });
/*  61:    */     
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79: 62 */     this.digDisplay = new TextPanelWidget(new String[] { "Port", "Mode", "Value" }, new String[][] { { "0", "--", "0" }, { "1", "--", "0" }, { "2", "--", "0" }, { "3", "--", "0" }, { "4", "--", "0" }, { "5", "--", "0" }, { "6", "--", "0" }, { "7", "--", "0" }, { "8  (mot0 fail)", "--", "0" }, { "9  (mot0 en)  ", "--", "0" }, { "10 (mot1 fail)", "--", "0" }, { "11 (mot1 en)  ", "--", "0" }, { "12 (mot2 fail)", "--", "0" }, { "13 (mot2 en)  ", "--", "0" }, { "14 (estop)    ", "--", "0" }, { "15 (button0)  ", "--", "0" } }, new double[] { 0.3D, 0.5D, 0.5D });
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:    */ 
/* 100: 83 */     this.qeiDisplay = new TextPanelWidget(new String[] { "Port", "Position", "Velocity" }, new String[][] { { "0", "0", "0" }, { "1", "0", "0" } }, new double[] { 0.3D, 0.5D, 0.5D });
/* 101:    */     
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106: 89 */     JPanel motDisplay = new JPanel();
/* 107: 90 */     WeightedGridLayout motorWGL = new WeightedGridLayout(new double[] { 0.0D, 0.0D, 0.8D, 0.2D });
/* 108: 91 */     motorWGL.setDefaultRowWeight(1.0D);
/* 109: 92 */     motorWGL.setRowWeight(0, 0.0D);
/* 110: 93 */     motDisplay.setLayout(motorWGL);
/* 111:    */     
/* 112: 95 */     motDisplay.add(new JLabel("# "));
/* 113: 96 */     motDisplay.add(new JLabel("En "));
/* 114: 97 */     motDisplay.add(new JLabel("PWM "));
/* 115: 98 */     motDisplay.add(new JLabel("Slew"));
/* 116:    */     
/* 117:100 */     this.motorPanels = new MotorPanel[3];
/* 118:101 */     for (int i = 0; i < this.motorPanels.length; i++) {
/* 119:102 */       this.motorPanels[i] = new MotorPanel(motDisplay, i);
/* 120:    */     }
/* 121:105 */     JPanel servoDisplay = new JPanel();
/* 122:106 */     servoDisplay.setLayout(new GridLayout(8, 1));
/* 123:107 */     this.servoPanels = new ServoPanel[8];
/* 124:108 */     for (int i = 0; i < this.servoPanels.length; i++)
/* 125:    */     {
/* 126:109 */       this.servoPanels[i] = new ServoPanel(i);
/* 127:110 */       JPanel jp = new JPanel();
/* 128:111 */       jp.setLayout(new BorderLayout());
/* 129:112 */       jp.add(new JLabel(" " + i), "West");
/* 130:113 */       jp.add(this.servoPanels[i], "Center");
/* 131:114 */       servoDisplay.add(jp);
/* 132:    */     }
/* 133:117 */     this.jf.setSize(906, 550);
/* 134:118 */     this.jf.setVisible(true);
/* 135:119 */     makeInternalFrame("Basic Properties", this.basicDisplay, 300, 120);
/* 136:    */     
/* 137:121 */     makeInternalFrame("Analog Input", this.analogDisplay, 300, 285);
/* 138:122 */     makeInternalFrame("Quadrature Decoders", this.qeiDisplay, 300, 120);
/* 139:123 */     makeInternalFrame("Motors", motDisplay, 300, 200);
/* 140:124 */     makeInternalFrame("Servos", servoDisplay, 300, 300);
/* 141:125 */     makeInternalFrame("DigitalIO", this.digDisplay, 300, 330);
/* 142:    */     
/* 143:127 */     new StatusPollThread().start();
/* 144:    */     
/* 145:129 */     this.jf.addWindowListener(new WindowAdapter()
/* 146:    */     {
/* 147:    */       public void windowClosing(WindowEvent e)
/* 148:    */       {
/* 149:131 */         System.exit(0);
/* 150:    */       }
/* 151:    */     });
/* 152:    */   }
/* 153:    */   
/* 154:135 */   int xpos = 0;
/* 155:135 */   int ypos = 0;
/* 156:    */   
/* 157:    */   void makeInternalFrame(String name, JComponent jc, int width, int height)
/* 158:    */   {
/* 159:138 */     JInternalFrame jif = new JInternalFrame(name, true, true, true, true);
/* 160:139 */     jif.setLayout(new BorderLayout());
/* 161:140 */     jif.add(jc, "Center");
/* 162:141 */     if (this.ypos + height >= this.jf.getHeight())
/* 163:    */     {
/* 164:142 */       this.ypos = 0;
/* 165:143 */       this.xpos += width;
/* 166:    */     }
/* 167:145 */     jif.reshape(this.xpos, this.ypos, width, height);
/* 168:146 */     this.ypos += height;
/* 169:    */     
/* 170:148 */     jif.setVisible(true);
/* 171:149 */     this.jdp.add(jif);
/* 172:150 */     this.jdp.setBackground(Color.blue);
/* 173:    */   }
/* 174:    */   
/* 175:    */   class StatusPollThread
/* 176:    */     extends Thread
/* 177:    */   {
/* 178:    */     StatusPollThread()
/* 179:    */     {
/* 180:157 */       setDaemon(true);
/* 181:    */     }
/* 182:    */     
/* 183:    */     public void run()
/* 184:    */     {
/* 185:    */       try
/* 186:    */       {
/* 187:    */         for (;;)
/* 188:    */         {
/* 189:164 */           OrcStatus status = Spy.this.orc.getStatus();
/* 190:165 */           Spy.this.orcStatus(Spy.this.orc, status);
/* 191:166 */           Thread.sleep(100L);
/* 192:    */         }
/* 193:    */       }
/* 194:    */       catch (Exception ex)
/* 195:    */       {
/* 196:168 */         System.out.println("Spy.StatusPollThread ex: " + ex);
/* 197:    */       }
/* 198:    */     }
/* 199:    */   }
/* 200:    */   
/* 201:174 */   boolean firstStatus = true;
/* 202:    */   
/* 203:    */   public void orcStatus(Orc orc, OrcStatus status)
/* 204:    */   {
/* 205:179 */     if (status.utimeOrc < 2000000L) {
/* 206:180 */       this.firstStatus = true;
/* 207:    */     }
/* 208:182 */     for (int i = 0; i < 2; i++)
/* 209:    */     {
/* 210:183 */       this.qeiDisplay.values[i][1] = String.format("%6d", new Object[] { Integer.valueOf(status.qeiPosition[i]) });
/* 211:184 */       this.qeiDisplay.values[i][2] = String.format("%6d", new Object[] { Integer.valueOf(status.qeiVelocity[i]) });
/* 212:    */     }
/* 213:186 */     this.basicDisplay.values[0][1] = String.format("%.6f", new Object[] { Double.valueOf(status.utimeOrc / 1000000.0D) });
/* 214:188 */     for (int i = 0; i < 13; i++)
/* 215:    */     {
/* 216:189 */       this.analogDisplay.values[i][1] = String.format("%04X", new Object[] { Integer.valueOf(status.analogInput[i]) });
/* 217:192 */       if (i < 8)
/* 218:    */       {
/* 219:193 */         double voltage = status.analogInput[i] / 65535.0D * 5.0D;
/* 220:194 */         this.analogDisplay.values[i][2] = String.format("%8.3f V  ", new Object[] { Double.valueOf(voltage) });
/* 221:    */         
/* 222:196 */         voltage = status.analogInputFiltered[i] / 65535.0D * 5.0D;
/* 223:197 */         this.analogDisplay.values[i][3] = String.format("%8.3f V  ", new Object[] { Double.valueOf(voltage) });
/* 224:    */       }
/* 225:198 */       else if ((i >= 8) && (i <= 10))
/* 226:    */       {
/* 227:200 */         double voltage = status.analogInput[i] / 65535.0D * 3.0D;
/* 228:201 */         double current = voltage * 375.0D / 200.0D * 1000.0D;
/* 229:202 */         this.analogDisplay.values[i][2] = String.format("%8.0f mA ", new Object[] { Double.valueOf(current) });
/* 230:    */         
/* 231:204 */         voltage = status.analogInputFiltered[i] / 65535.0D * 3.0D;
/* 232:205 */         current = voltage * 375.0D / 200.0D * 1000.0D;
/* 233:206 */         this.analogDisplay.values[i][3] = String.format("%8.0f mA ", new Object[] { Double.valueOf(current) });
/* 234:    */       }
/* 235:208 */       else if (i == 11)
/* 236:    */       {
/* 237:210 */         double voltage = status.analogInput[i] / 65535.0D * 3.0D;
/* 238:211 */         double batvoltage = voltage * 10.1D;
/* 239:212 */         this.analogDisplay.values[i][2] = String.format("%8.2f V  ", new Object[] { Double.valueOf(batvoltage) });
/* 240:    */         
/* 241:214 */         voltage = status.analogInputFiltered[i] / 65535.0D * 3.0D;
/* 242:215 */         batvoltage = voltage * 10.1D;
/* 243:216 */         this.analogDisplay.values[i][3] = String.format("%8.2f V  ", new Object[] { Double.valueOf(batvoltage) });
/* 244:    */       }
/* 245:    */       else
/* 246:    */       {
/* 247:220 */         double voltage = status.analogInput[i] / 65535.0D * 3.0D;
/* 248:221 */         double temp = -(voltage - 2.7D) * 75.0D - 55.0D;
/* 249:222 */         this.analogDisplay.values[i][2] = String.format("%8.2f degC", new Object[] { Double.valueOf(temp) });
/* 250:    */         
/* 251:224 */         voltage = status.analogInputFiltered[i] / 65535.0D * 3.0D;
/* 252:225 */         temp = -(voltage - 2.7D) * 75.0D - 55.0D;
/* 253:226 */         this.analogDisplay.values[i][3] = String.format("%8.2f degC", new Object[] { Double.valueOf(temp) });
/* 254:    */       }
/* 255:    */     }
/* 256:230 */     for (int i = 0; i < 16; i++)
/* 257:    */     {
/* 258:231 */       int val = status.simpleDigitalValues >> i & 0x1;
/* 259:232 */       this.digDisplay.values[i][2] = ("" + val);
/* 260:    */     }
/* 261:235 */     for (int i = 0; i < 3; i++)
/* 262:    */     {
/* 263:236 */       this.motorPanels[i].pwmslider.setActualValue(status.motorPWMactual[i]);
/* 264:237 */       this.motorPanels[i].slewslider.setActualValue((int)(status.motorSlewSeconds[i] / this.motorPanels[i].slewslider.formatScale));
/* 265:    */       
/* 266:    */ 
/* 267:240 */       this.motorPanels[i].enabledCheckbox.setSelected(status.motorEnable[i]);
/* 268:242 */       if (this.firstStatus)
/* 269:    */       {
/* 270:243 */         this.motorPanels[i].pwmslider.setGoalValue(status.motorPWMactual[i]);
/* 271:244 */         this.motorPanels[i].slewslider.setGoalValue((int)(status.motorSlewSeconds[i] / this.motorPanels[i].slewslider.formatScale));
/* 272:    */       }
/* 273:    */     }
/* 274:248 */     this.firstStatus = false;
/* 275:    */     
/* 276:250 */     this.digDisplay.repaint();
/* 277:251 */     this.qeiDisplay.repaint();
/* 278:252 */     this.basicDisplay.repaint();
/* 279:253 */     this.analogDisplay.repaint();
/* 280:    */   }
/* 281:    */   
/* 282:    */   class ServoPanel
/* 283:    */     extends JPanel
/* 284:    */     implements SmallSlider.Listener
/* 285:    */   {
/* 286:258 */     SmallSlider slider = new SmallSlider(250, 3750, 1500, 1500, true);
/* 287:    */     Servo servo;
/* 288:    */     
/* 289:    */     ServoPanel(int port)
/* 290:    */     {
/* 291:263 */       this.slider.formatScale = 1.0D;
/* 292:264 */       this.slider.formatString = "%.0f us";
/* 293:    */       
/* 294:    */ 
/* 295:267 */       this.servo = new Servo(Spy.this.orc, port, 0.0D, 0, 0.0D, 0);
/* 296:    */       
/* 297:269 */       setLayout(new BorderLayout());
/* 298:270 */       add(this.slider, "Center");
/* 299:    */       
/* 300:272 */       this.slider.addListener(this);
/* 301:    */     }
/* 302:    */     
/* 303:    */     public void goalValueChanged(SmallSlider slider, int goal)
/* 304:    */     {
/* 305:277 */       this.servo.setPulseWidth(goal);
/* 306:    */     }
/* 307:    */   }
/* 308:    */   
/* 309:    */   class MotorPanel
/* 310:    */     implements SmallSlider.Listener
/* 311:    */   {
/* 312:283 */     SmallSlider pwmslider = new SmallSlider(-255, 255, 0, 0, true);
/* 313:284 */     SmallSlider slewslider = new SmallSlider(0, 65535, 0, 0, true);
/* 314:285 */     JCheckBox enabledCheckbox = new JCheckBox();
/* 315:    */     int port;
/* 316:    */     Motor motor;
/* 317:    */     
/* 318:    */     MotorPanel(JPanel jp, int port)
/* 319:    */     {
/* 320:292 */       this.motor = new Motor(Spy.this.orc, port, false);
/* 321:    */       
/* 322:294 */       this.pwmslider.formatScale = 0.392156862745098D;
/* 323:295 */       this.pwmslider.formatString = "%.0f %%";
/* 324:    */       
/* 325:297 */       this.slewslider.formatString = "%.3fs";
/* 326:298 */       this.slewslider.formatScale = 4.577706569008927E-005D;
/* 327:    */       
/* 328:300 */       jp.add(new JLabel(" " + port));
/* 329:301 */       jp.add(this.enabledCheckbox);
/* 330:302 */       jp.add(this.pwmslider);
/* 331:303 */       jp.add(this.slewslider);
/* 332:    */       
/* 333:305 */       this.pwmslider.addListener(this);
/* 334:306 */       this.slewslider.addListener(this);
/* 335:    */       
/* 336:308 */       this.enabledCheckbox.addActionListener(new ActionListener()
/* 337:    */       {
/* 338:    */         public void actionPerformed(ActionEvent e)
/* 339:    */         {
/* 340:310 */           Spy.MotorPanel.this.enabledClicked();
/* 341:    */         }
/* 342:    */       });
/* 343:    */     }
/* 344:    */     
/* 345:    */     public void goalValueChanged(SmallSlider slider, int goal)
/* 346:    */     {
/* 347:317 */       if (slider == this.pwmslider) {
/* 348:318 */         this.motor.setPWM(goal / 255.0D);
/* 349:    */       }
/* 350:319 */       if (slider == this.slewslider) {
/* 351:320 */         this.motor.setSlewSeconds(goal * this.slewslider.formatScale);
/* 352:    */       }
/* 353:    */     }
/* 354:    */     
/* 355:    */     void enabledClicked()
/* 356:    */     {
/* 357:325 */       if (this.enabledCheckbox.isSelected()) {
/* 358:326 */         this.motor.setPWM(this.pwmslider.getGoalValue() / 255.0D);
/* 359:    */       } else {
/* 360:328 */         this.motor.idle();
/* 361:    */       }
/* 362:    */     }
/* 363:    */   }
/* 364:    */   
/* 365:    */   class TextPanelWidget
/* 366:    */     extends JPanel
/* 367:    */   {
/* 368:    */     String panelName;
/* 369:    */     String[] columnNames;
/* 370:    */     String[][] values;
/* 371:    */     double[] columnWidthWeight;
/* 372:    */     
/* 373:    */     public TextPanelWidget(String[] columnNames, String[][] values, double[] columnWidthWeight)
/* 374:    */     {
/* 375:343 */       this.columnNames = columnNames;
/* 376:344 */       this.columnWidthWeight = columnWidthWeight;
/* 377:345 */       this.values = values;
/* 378:    */     }
/* 379:    */     
/* 380:    */     int getStringWidth(Graphics g, String s)
/* 381:    */     {
/* 382:350 */       FontMetrics fm = g.getFontMetrics(g.getFont());
/* 383:351 */       return fm.stringWidth(s);
/* 384:    */     }
/* 385:    */     
/* 386:    */     int getStringHeight(Graphics g, String s)
/* 387:    */     {
/* 388:356 */       FontMetrics fm = g.getFontMetrics(g.getFont());
/* 389:357 */       return fm.getMaxAscent();
/* 390:    */     }
/* 391:    */     
/* 392:    */     public Dimension getPreferredSize()
/* 393:    */     {
/* 394:362 */       return new Dimension(100, 100);
/* 395:    */     }
/* 396:    */     
/* 397:    */     public Dimension getMinimumSize()
/* 398:    */     {
/* 399:367 */       return getPreferredSize();
/* 400:    */     }
/* 401:    */     
/* 402:    */     public Dimension getMaximumSize()
/* 403:    */     {
/* 404:372 */       return new Dimension(1000, 1000);
/* 405:    */     }
/* 406:    */     
/* 407:    */     public void paint(Graphics _g)
/* 408:    */     {
/* 409:377 */       Graphics2D g = (Graphics2D)_g;
/* 410:378 */       int width = getWidth();int height = getHeight();
/* 411:    */       
/* 412:380 */       Font fb = new Font("Monospaced", 1, 12);
/* 413:381 */       Font fp = new Font("Monospaced", 0, 12);
/* 414:382 */       g.setFont(fb);
/* 415:383 */       int textHeight = getStringHeight(g, "");
/* 416:    */       
/* 417:    */ 
/* 418:    */ 
/* 419:    */ 
/* 420:388 */       g.setColor(getBackground());
/* 421:389 */       g.fillRect(0, 0, width, height);
/* 422:    */       
/* 423:391 */       g.setColor(Color.black);
/* 424:392 */       g.drawRect(0, 0, width - 1, height - 1);
/* 425:    */       
/* 426:394 */       double totalColumnWidthWeight = 0.0D;
/* 427:395 */       for (int i = 0; i < this.columnWidthWeight.length; i++) {
/* 428:396 */         totalColumnWidthWeight += this.columnWidthWeight[i];
/* 429:    */       }
/* 430:398 */       int[] columnPos = new int[this.columnWidthWeight.length];
/* 431:399 */       int[] columnWidth = new int[this.columnWidthWeight.length];
/* 432:400 */       for (int i = 0; i < columnWidth.length; i++) {
/* 433:401 */         columnWidth[i] = ((int)(width * this.columnWidthWeight[i] / totalColumnWidthWeight));
/* 434:    */       }
/* 435:403 */       for (int i = 1; i < columnWidth.length; i++) {
/* 436:404 */         columnPos[i] = (columnPos[(i - 1)] + columnWidth[(i - 1)]);
/* 437:    */       }
/* 438:409 */       g.setColor(Color.black);
/* 439:410 */       for (int i = 0; i < this.columnNames.length; i++)
/* 440:    */       {
/* 441:411 */         String s = this.columnNames[i];
/* 442:412 */         g.drawString(s, columnPos[i] + columnWidth[i] / 2 - getStringWidth(g, s) / 2, textHeight * 3 / 2);
/* 443:    */       }
/* 444:417 */       g.setFont(fp);
/* 445:418 */       for (int i = 0; i < this.values.length; i++) {
/* 446:419 */         for (int j = 0; j < this.columnNames.length; j++)
/* 447:    */         {
/* 448:420 */           String s = this.values[i][j];
/* 449:421 */           if (s == null) {
/* 450:422 */             s = "";
/* 451:    */           }
/* 452:424 */           g.drawString(s, columnPos[j] + columnWidth[j] / 2 - getStringWidth(g, s) / 2, textHeight * (3 + i));
/* 453:    */         }
/* 454:    */       }
/* 455:    */     }
/* 456:    */   }
/* 457:    */ }

