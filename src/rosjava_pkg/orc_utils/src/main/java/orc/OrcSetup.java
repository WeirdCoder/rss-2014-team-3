/*   1:    */ package orc;
/*   2:    */ 
/*   3:    */ import java.awt.BorderLayout;
/*   4:    */ import java.awt.FlowLayout;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.event.ActionEvent;
/*   7:    */ import java.awt.event.ActionListener;
/*   8:    */ import java.io.ByteArrayInputStream;
/*   9:    */ import java.io.ByteArrayOutputStream;
/*  10:    */ import java.io.DataInputStream;
/*  11:    */ import java.io.DataOutputStream;
/*  12:    */ import java.io.IOException;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.net.DatagramPacket;
/*  15:    */ import java.net.DatagramSocket;
/*  16:    */ import java.net.Inet4Address;
/*  17:    */ import java.net.InetAddress;
/*  18:    */ import java.net.UnknownHostException;
/*  19:    */ import java.util.ArrayList;
/*  20:    */ import java.util.Collections;
/*  21:    */ import java.util.HashMap;
/*  22:    */ import java.util.Random;
/*  23:    */ import javax.swing.Box;
/*  24:    */ import javax.swing.JButton;
/*  25:    */ import javax.swing.JCheckBox;
/*  26:    */ import javax.swing.JComponent;
/*  27:    */ import javax.swing.JFrame;
/*  28:    */ import javax.swing.JLabel;
/*  29:    */ import javax.swing.JList;
/*  30:    */ import javax.swing.JPanel;
/*  31:    */ import javax.swing.JScrollPane;
/*  32:    */ import javax.swing.JSplitPane;
/*  33:    */ import javax.swing.JTextArea;
/*  34:    */ import javax.swing.JTextField;
/*  35:    */ import javax.swing.ListModel;
/*  36:    */ import javax.swing.event.ListDataEvent;
/*  37:    */ import javax.swing.event.ListDataListener;
/*  38:    */ import javax.swing.event.ListSelectionEvent;
/*  39:    */ import javax.swing.event.ListSelectionListener;
/*  40:    */ import orc.spy.WeightedGridLayout;
/*  41:    */ 
/*  42:    */ public class OrcSetup
/*  43:    */ {
/*  44:    */   InetAddress broadcastInetAddr;
/*  45:    */   
/*  46:    */   class OrcDetection
/*  47:    */     implements Comparable<OrcDetection>
/*  48:    */   {
/*  49:    */     InetAddress addr;
/*  50:    */     long mstime;
/*  51:    */     int bootNonce;
/*  52:    */     int magic;
/*  53:    */     int version;
/*  54:    */     int length;
/*  55:    */     int ip4addr;
/*  56:    */     int ip4mask;
/*  57:    */     int ip4gw;
/*  58:    */     long macaddr;
/*  59:    */     boolean dhcpd_enable;
/*  60:    */     int magic2;
/*  61:    */     
/*  62:    */     OrcDetection() {}
/*  63:    */     
/*  64:    */     public int compareTo(OrcDetection od)
/*  65:    */     {
/*  66: 38 */       if (od.ip4addr == this.ip4addr) {
/*  67: 39 */         return od.bootNonce - this.bootNonce;
/*  68:    */       }
/*  69: 41 */       return od.ip4addr - this.ip4addr;
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73: 47 */   OrcDetection selectedOrcDetection = null;
/*  74: 49 */   ArrayList<OrcDetection> detections = new ArrayList();
/*  75:    */   JFrame jf;
/*  76: 52 */   HashMap<String, Param> paramsMap = new HashMap();
/*  77:    */   JPanel paramsPanel;
/*  78: 55 */   JButton revertButton = new JButton("Revert");
/*  79: 56 */   JButton randomMACButton = new JButton("Randomize MAC");
/*  80: 57 */   JButton writeButton = new JButton("Write to uOrc");
/*  81: 59 */   JTextArea log = new JTextArea();
/*  82: 61 */   MyListModel mdl = new MyListModel();
/*  83: 62 */   JList orcList = new JList(this.mdl);
/*  84: 63 */   ArrayList<String> orcIps = new ArrayList();
/*  85: 65 */   Random rand = new Random();
/*  86:    */   FindOrcThread finder;
/*  87:    */   DatagramSocket sock;
/*  88:    */   static final int FLASH_PARAM_SIGNATURE = -311321921;
/*  89:    */   static final int FLASH_PARAM_VERSION = 2;
/*  90:    */   static final int FLASH_PARAM_LENGTH = 37;
/*  91:    */   
/*  92:    */   public static void main(String[] args)
/*  93:    */   {
/*  94: 77 */     new OrcSetup();
/*  95:    */   }
/*  96:    */   
/*  97:    */   static abstract interface Param
/*  98:    */   {
/*  99:    */     public abstract String getValue();
/* 100:    */     
/* 101:    */     public abstract void setValue(String paramString);
/* 102:    */     
/* 103:    */     public abstract JComponent getComponent();
/* 104:    */   }
/* 105:    */   
/* 106:    */   class BooleanParam
/* 107:    */     implements OrcSetup.Param
/* 108:    */   {
/* 109: 89 */     JCheckBox checkBox = new JCheckBox();
/* 110:    */     
/* 111:    */     BooleanParam() {}
/* 112:    */     
/* 113:    */     public String getValue()
/* 114:    */     {
/* 115: 93 */       return this.checkBox.isSelected() ? "true" : "false";
/* 116:    */     }
/* 117:    */     
/* 118:    */     public void setValue(String v)
/* 119:    */     {
/* 120: 98 */       this.checkBox.setSelected(v.equals("true"));
/* 121:    */     }
/* 122:    */     
/* 123:    */     public JComponent getComponent()
/* 124:    */     {
/* 125:103 */       return this.checkBox;
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   class StringParam
/* 130:    */     implements OrcSetup.Param
/* 131:    */   {
/* 132:109 */     JTextField textField = new JTextField();
/* 133:    */     
/* 134:    */     StringParam() {}
/* 135:    */     
/* 136:    */     public String getValue()
/* 137:    */     {
/* 138:113 */       return this.textField.getText();
/* 139:    */     }
/* 140:    */     
/* 141:    */     public void setValue(String v)
/* 142:    */     {
/* 143:118 */       this.textField.setText(v);
/* 144:    */     }
/* 145:    */     
/* 146:    */     public JComponent getComponent()
/* 147:    */     {
/* 148:123 */       return this.textField;
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   void addParamIPAddress(String name)
/* 153:    */   {
/* 154:129 */     this.paramsPanel.add(new JLabel(name));
/* 155:130 */     Param p = new StringParam();
/* 156:131 */     this.paramsPanel.add(p.getComponent());
/* 157:132 */     this.paramsMap.put(name, p);
/* 158:    */   }
/* 159:    */   
/* 160:    */   void addParamMACAddress(String name)
/* 161:    */   {
/* 162:137 */     this.paramsPanel.add(new JLabel(name));
/* 163:138 */     Param p = new StringParam();
/* 164:139 */     this.paramsPanel.add(p.getComponent());
/* 165:140 */     this.paramsMap.put(name, p);
/* 166:    */   }
/* 167:    */   
/* 168:    */   void addParamBoolean(String name)
/* 169:    */   {
/* 170:145 */     this.paramsPanel.add(new JLabel(name));
/* 171:146 */     Param p = new BooleanParam();
/* 172:147 */     this.paramsPanel.add(p.getComponent());
/* 173:148 */     this.paramsMap.put(name, p);
/* 174:    */   }
/* 175:    */   
/* 176:    */   public OrcSetup()
/* 177:    */   {
/* 178:    */     try
/* 179:    */     {
/* 180:155 */       this.broadcastInetAddr = Inet4Address.getByName("192.168.237.255");
/* 181:    */     }
/* 182:    */     catch (UnknownHostException ex)
/* 183:    */     {
/* 184:157 */       System.out.println("ex: " + ex);
/* 185:158 */       System.exit(-1);
/* 186:    */     }
/* 187:161 */     this.finder = new FindOrcThread();
/* 188:162 */     this.finder.start();
/* 189:    */     
/* 190:164 */     this.orcList.addListSelectionListener(new ListSelectionListener()
/* 191:    */     {
/* 192:    */       public void valueChanged(ListSelectionEvent e)
/* 193:    */       {
/* 194:167 */         OrcSetup.this.listChanged();
/* 195:    */       }
/* 196:170 */     });
/* 197:171 */     this.jf = new JFrame("OrcSetup");
/* 198:172 */     this.jf.setLayout(new BorderLayout());
/* 199:    */     
/* 200:174 */     this.paramsPanel = new JPanel();
/* 201:175 */     WeightedGridLayout wgl = new WeightedGridLayout(new double[] { 0.1D, 0.9D });
/* 202:176 */     wgl.setDefaultRowWeight(0.0D);
/* 203:177 */     this.paramsPanel.setLayout(wgl);
/* 204:    */     
/* 205:179 */     addParamIPAddress("ipaddr");
/* 206:180 */     addParamIPAddress("ipmask");
/* 207:181 */     addParamIPAddress("ipgw");
/* 208:182 */     addParamMACAddress("macaddr");
/* 209:183 */     addParamBoolean("dhcpd_enable");
/* 210:    */     
/* 211:185 */     JPanel buttonPanel = new JPanel();
/* 212:186 */     buttonPanel.setLayout(new FlowLayout());
/* 213:187 */     buttonPanel.add(this.revertButton);
/* 214:188 */     buttonPanel.add(this.randomMACButton);
/* 215:189 */     buttonPanel.add(this.writeButton);
/* 216:    */     
/* 217:191 */     JPanel mainPanel = new JPanel();
/* 218:192 */     mainPanel.setLayout(new BorderLayout());
/* 219:193 */     mainPanel.add(this.paramsPanel, "Center");
/* 220:194 */     mainPanel.add(buttonPanel, "South");
/* 221:    */     
/* 222:196 */     JPanel orcListPanel = new JPanel();
/* 223:197 */     orcListPanel.setLayout(new BorderLayout());
/* 224:198 */     orcListPanel.add(new JLabel("uOrcs found:"), "North");
/* 225:199 */     orcListPanel.add(new JScrollPane(this.orcList), "Center");
/* 226:200 */     orcListPanel.add(Box.createHorizontalStrut(10), "East");
/* 227:    */     
/* 228:202 */     mainPanel.add(orcListPanel, "West");
/* 229:    */     
/* 230:204 */     JSplitPane jsp = new JSplitPane(0, mainPanel, new JScrollPane(this.log));
/* 231:205 */     this.jf.add(jsp, "Center");
/* 232:206 */     jsp.setDividerLocation(0.65D);
/* 233:207 */     jsp.setResizeWeight(0.65D);
/* 234:    */     
/* 235:209 */     this.revertButton.addActionListener(new ActionListener()
/* 236:    */     {
/* 237:    */       public void actionPerformed(ActionEvent e)
/* 238:    */       {
/* 239:211 */         OrcSetup.this.revert();
/* 240:    */       }
/* 241:214 */     });
/* 242:215 */     this.randomMACButton.addActionListener(new ActionListener()
/* 243:    */     {
/* 244:    */       public void actionPerformed(ActionEvent e)
/* 245:    */       {
/* 246:217 */         OrcSetup.this.randomMAC();
/* 247:    */       }
/* 248:220 */     });
/* 249:221 */     this.writeButton.addActionListener(new ActionListener()
/* 250:    */     {
/* 251:    */       public void actionPerformed(ActionEvent e)
/* 252:    */       {
/* 253:223 */         OrcSetup.this.write();
/* 254:    */       }
/* 255:226 */     });
/* 256:227 */     this.jf.setSize(600, 500);
/* 257:228 */     this.jf.setVisible(true);
/* 258:    */     
/* 259:230 */     this.log.setEditable(false);
/* 260:231 */     this.log.setFont(new Font("Monospaced", 0, 12));
/* 261:    */     
/* 262:233 */     logAppend("Started.");
/* 263:    */     
/* 264:235 */     listChanged();
/* 265:    */   }
/* 266:    */   
/* 267:    */   void listChanged()
/* 268:    */   {
/* 269:241 */     int idx = this.orcList.getSelectedIndex();
/* 270:243 */     if (idx < 0)
/* 271:    */     {
/* 272:244 */       ((Param)this.paramsMap.get("ipaddr")).setValue("");
/* 273:245 */       ((Param)this.paramsMap.get("ipmask")).setValue("");
/* 274:246 */       ((Param)this.paramsMap.get("ipgw")).setValue("");
/* 275:247 */       ((Param)this.paramsMap.get("macaddr")).setValue("");
/* 276:    */       
/* 277:249 */       this.revertButton.setEnabled(false);
/* 278:250 */       this.randomMACButton.setEnabled(false);
/* 279:251 */       this.writeButton.setEnabled(false);
/* 280:252 */       return;
/* 281:    */     }
/* 282:255 */     this.selectedOrcDetection = ((OrcDetection)this.detections.get(idx));
/* 283:256 */     ((Param)this.paramsMap.get("ipaddr")).setValue(ip2string(this.selectedOrcDetection.ip4addr));
/* 284:257 */     ((Param)this.paramsMap.get("ipmask")).setValue(ip2string(this.selectedOrcDetection.ip4mask));
/* 285:258 */     ((Param)this.paramsMap.get("ipgw")).setValue(ip2string(this.selectedOrcDetection.ip4gw));
/* 286:259 */     ((Param)this.paramsMap.get("macaddr")).setValue(mac2string(this.selectedOrcDetection.macaddr));
/* 287:260 */     ((Param)this.paramsMap.get("dhcpd_enable")).setValue(boolean2string(this.selectedOrcDetection.dhcpd_enable));
/* 288:261 */     this.revertButton.setEnabled(true);
/* 289:262 */     this.randomMACButton.setEnabled(true);
/* 290:263 */     this.writeButton.setEnabled(true);
/* 291:    */   }
/* 292:    */   
/* 293:    */   void revert()
/* 294:    */   {
/* 295:268 */     listChanged();
/* 296:    */   }
/* 297:    */   
/* 298:    */   void randomMAC()
/* 299:    */   {
/* 300:273 */     long r = this.rand.nextLong();
/* 301:    */     
/* 302:275 */     Param p = (Param)this.paramsMap.get("macaddr");
/* 303:276 */     p.setValue(String.format("%02x:%02x:%02x:%02x:%02x:%02x", new Object[] { Integer.valueOf(2), Integer.valueOf(0), Long.valueOf(r >> 24 & 0xFF), Long.valueOf(r >> 16 & 0xFF), Long.valueOf(r >> 8 & 0xFF), Long.valueOf(r & 0xFF) }));
/* 304:    */   }
/* 305:    */   
/* 306:    */   void logAppend(String s)
/* 307:    */   {
/* 308:282 */     this.log.append(s);
/* 309:283 */     this.log.setCaretPosition(this.log.getText().length() - 1);
/* 310:    */   }
/* 311:    */   
/* 312:    */   int swap(int v)
/* 313:    */   {
/* 314:288 */     return ((v >> 24 & 0xFF) << 0) + ((v >> 16 & 0xFF) << 8) + ((v >> 8 & 0xFF) << 16) + ((v & 0xFF) << 24);
/* 315:    */   }
/* 316:    */   
/* 317:    */   long swap(long v)
/* 318:    */   {
/* 319:296 */     return ((v >> 56 & 0xFF) << 0) + ((v >> 48 & 0xFF) << 8) + ((v >> 40 & 0xFF) << 16) + ((v >> 32 & 0xFF) << 24) + ((v >> 24 & 0xFF) << 32) + ((v >> 16 & 0xFF) << 40) + ((v >> 8 & 0xFF) << 48) + ((v & 0xFF) << 56);
/* 320:    */   }
/* 321:    */   
/* 322:    */   int string2ip(String s)
/* 323:    */   {
/* 324:309 */     String[] toks = s.split("\\.");
/* 325:310 */     if (toks.length != 4) {
/* 326:311 */       throw new RuntimeException("Invalid IP format");
/* 327:    */     }
/* 328:313 */     return (Integer.parseInt(toks[0]) << 24) + (Integer.parseInt(toks[1]) << 16) + (Integer.parseInt(toks[2]) << 8) + Integer.parseInt(toks[3]);
/* 329:    */   }
/* 330:    */   
/* 331:    */   String ip2string(int ip)
/* 332:    */   {
/* 333:322 */     return String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(ip >> 24 & 0xFF), Integer.valueOf(ip >> 16 & 0xFF), Integer.valueOf(ip >> 8 & 0xFF), Integer.valueOf(ip & 0xFF) });
/* 334:    */   }
/* 335:    */   
/* 336:    */   long string2mac(String s)
/* 337:    */   {
/* 338:331 */     String[] toks = s.split(":");
/* 339:332 */     if (toks.length != 6) {
/* 340:333 */       throw new RuntimeException("Invalid MAC address format");
/* 341:    */     }
/* 342:335 */     return (Integer.parseInt(toks[0], 16) << 40) + (Integer.parseInt(toks[1], 16) << 32) + (Integer.parseInt(toks[2], 16) << 24) + (Integer.parseInt(toks[3], 16) << 16) + (Integer.parseInt(toks[4], 16) << 8) + (Integer.parseInt(toks[5], 16) << 0);
/* 343:    */   }
/* 344:    */   
/* 345:    */   boolean string2boolean(String s)
/* 346:    */   {
/* 347:345 */     s = s.toLowerCase();
/* 348:346 */     return s.equals("true");
/* 349:    */   }
/* 350:    */   
/* 351:    */   String boolean2string(boolean b)
/* 352:    */   {
/* 353:351 */     if (b) {
/* 354:352 */       return "true";
/* 355:    */     }
/* 356:353 */     return "false";
/* 357:    */   }
/* 358:    */   
/* 359:    */   String mac2string(long mac)
/* 360:    */   {
/* 361:358 */     return String.format("%02x:%02x:%02x:%02x:%02x:%02x", new Object[] { Long.valueOf(mac >> 40 & 0xFF), Long.valueOf(mac >> 32 & 0xFF), Long.valueOf(mac >> 24 & 0xFF), Long.valueOf(mac >> 16 & 0xFF), Long.valueOf(mac >> 8 & 0xFF), Long.valueOf(mac & 0xFF) });
/* 362:    */   }
/* 363:    */   
/* 364:    */   void logArray(byte[] buf, int offset, int len)
/* 365:    */   {
/* 366:369 */     for (int i = 0; i < len - offset; i++)
/* 367:    */     {
/* 368:370 */       if (i % 16 == 0) {
/* 369:371 */         logAppend(String.format("%04x : ", new Object[] { Integer.valueOf(i) }));
/* 370:    */       }
/* 371:372 */       logAppend(String.format("%02x ", new Object[] { Byte.valueOf(buf[(offset + i)]) }));
/* 372:373 */       if (i % 16 == 15) {
/* 373:374 */         logAppend("\n");
/* 374:    */       }
/* 375:    */     }
/* 376:376 */     logAppend("\n");
/* 377:    */   }
/* 378:    */   
/* 379:    */   void write()
/* 380:    */   {
/* 381:    */     try
/* 382:    */     {
/* 383:382 */       ByteArrayOutputStream bouts = new ByteArrayOutputStream();
/* 384:383 */       DataOutputStream outs = new DataOutputStream(bouts);
/* 385:    */       
/* 386:385 */       OrcDetection od = (OrcDetection)this.detections.get(this.orcList.getSelectedIndex());
/* 387:    */       
/* 388:387 */       outs.writeInt(-307579079);
/* 389:388 */       outs.writeInt(65281);
/* 390:389 */       outs.writeInt(od.bootNonce);
/* 391:390 */       outs.writeInt(128);
/* 392:    */       
/* 393:    */ 
/* 394:393 */       outs.writeInt(swap(-311321921));
/* 395:394 */       outs.writeInt(swap(2));
/* 396:395 */       outs.writeInt(swap(37));
/* 397:    */       
/* 398:397 */       outs.writeInt(swap(string2ip(((Param)this.paramsMap.get("ipaddr")).getValue())));
/* 399:398 */       outs.writeInt(swap(string2ip(((Param)this.paramsMap.get("ipmask")).getValue())));
/* 400:399 */       outs.writeInt(swap(string2ip(((Param)this.paramsMap.get("ipgw")).getValue())));
/* 401:400 */       outs.writeLong(swap(string2mac(((Param)this.paramsMap.get("macaddr")).getValue())));
/* 402:401 */       outs.writeByte(string2boolean(((Param)this.paramsMap.get("dhcpd_enable")).getValue()) ? 1 : 0);
/* 403:    */       
/* 404:403 */       outs.writeInt(swap(-311321921));
/* 405:    */       
/* 406:405 */       logAppend("Writing parameter block...\n");
/* 407:    */       
/* 408:407 */       byte[] p = bouts.toByteArray();
/* 409:    */       
/* 410:409 */       System.out.println(od.addr);
/* 411:    */       
/* 412:    */ 
/* 413:    */ 
/* 414:    */ 
/* 415:    */ 
/* 416:    */ 
/* 417:    */ 
/* 418:417 */       DatagramPacket packet = new DatagramPacket(p, p.length, this.broadcastInetAddr, 2377);
/* 419:418 */       this.sock.send(packet);
/* 420:    */       
/* 421:420 */       logAppend("...finished. Reset uORC for settings to take effect.\n\n");
/* 422:    */     }
/* 423:    */     catch (IOException ex)
/* 424:    */     {
/* 425:423 */       System.out.println("ex: " + ex);
/* 426:    */     }
/* 427:    */   }
/* 428:    */   
/* 429:    */   class MyListModel
/* 430:    */     implements ListModel
/* 431:    */   {
/* 432:429 */     ArrayList<ListDataListener> listeners = new ArrayList();
/* 433:    */     
/* 434:    */     MyListModel() {}
/* 435:    */     
/* 436:    */     public void addListDataListener(ListDataListener listener)
/* 437:    */     {
/* 438:433 */       this.listeners.add(listener);
/* 439:    */     }
/* 440:    */     
/* 441:    */     public Object getElementAt(int index)
/* 442:    */     {
/* 443:438 */       OrcSetup.OrcDetection od = (OrcSetup.OrcDetection)OrcSetup.this.detections.get(index);
/* 444:439 */       return String.format("%08x : %s", new Object[] { Integer.valueOf(od.bootNonce), OrcSetup.this.ip2string(od.ip4addr) });
/* 445:    */     }
/* 446:    */     
/* 447:    */     public int getSize()
/* 448:    */     {
/* 449:444 */       return OrcSetup.this.detections.size();
/* 450:    */     }
/* 451:    */     
/* 452:    */     public void removeListDataListener(ListDataListener listener)
/* 453:    */     {
/* 454:449 */       this.listeners.remove(listener);
/* 455:    */     }
/* 456:    */     
/* 457:    */     public void changed()
/* 458:    */     {
/* 459:454 */       for (ListDataListener listener : this.listeners) {
/* 460:455 */         listener.contentsChanged(new ListDataEvent(this, 0, 0, getSize()));
/* 461:    */       }
/* 462:    */     }
/* 463:    */   }
/* 464:    */   
/* 465:    */   class FindOrcThread
/* 466:    */     extends Thread
/* 467:    */   {
/* 468:465 */     HashMap<Integer, OrcSetup.OrcDetection> detectionsMap = new HashMap();
/* 469:    */     ReaderThread reader;
/* 470:    */     
/* 471:    */     public FindOrcThread()
/* 472:    */     {
/* 473:471 */       setDaemon(true);
/* 474:    */     }
/* 475:    */     
/* 476:    */     public void run()
/* 477:    */     {
/* 478:    */       try
/* 479:    */       {
/* 480:477 */         runEx();
/* 481:    */       }
/* 482:    */       catch (IOException ex)
/* 483:    */       {
/* 484:479 */         System.out.println("Ex: " + ex);
/* 485:    */       }
/* 486:    */       catch (InterruptedException ex)
/* 487:    */       {
/* 488:481 */         System.out.println("Ex: " + ex);
/* 489:    */       }
/* 490:    */     }
/* 491:    */     
/* 492:    */     void runEx()
/* 493:    */       throws IOException, InterruptedException
/* 494:    */     {
/* 495:487 */       OrcSetup.this.sock = new DatagramSocket(2377);
/* 496:    */       
/* 497:489 */       this.reader = new ReaderThread();
/* 498:490 */       this.reader.setDaemon(true);
/* 499:491 */       this.reader.start();
/* 500:    */       for (;;)
/* 501:    */       {
/* 502:495 */         ByteArrayOutputStream bouts = new ByteArrayOutputStream();
/* 503:496 */         DataOutputStream outs = new DataOutputStream(bouts);
/* 504:497 */         outs.writeInt(-307579079);
/* 505:498 */         outs.writeInt(0);
/* 506:    */         
/* 507:500 */         byte[] p = bouts.toByteArray();
/* 508:501 */         DatagramPacket packet = new DatagramPacket(p, p.length, OrcSetup.this.broadcastInetAddr, 2377);
/* 509:502 */         OrcSetup.this.sock.send(packet);
/* 510:    */         
/* 511:504 */         Thread.sleep(500L);
/* 512:    */         
/* 513:506 */         ArrayList<OrcSetup.OrcDetection> goodDetections = new ArrayList();
/* 514:507 */         synchronized (this.detectionsMap)
/* 515:    */         {
/* 516:509 */           for (OrcSetup.OrcDetection od : this.detectionsMap.values())
/* 517:    */           {
/* 518:510 */             double age = (System.currentTimeMillis() - od.mstime) / 1000.0D;
/* 519:511 */             if (age < 1.2D) {
/* 520:512 */               goodDetections.add(od);
/* 521:    */             }
/* 522:    */           }
/* 523:515 */           this.detectionsMap.clear();
/* 524:516 */           for (OrcSetup.OrcDetection od : goodDetections) {
/* 525:517 */             this.detectionsMap.put(Integer.valueOf(od.bootNonce), od);
/* 526:    */           }
/* 527:    */         }
/* 528:520 */         Collections.sort(goodDetections);
/* 529:521 */         boolean changed = false;
/* 530:522 */         if (goodDetections.size() != OrcSetup.this.detections.size()) {
/* 531:523 */           changed = true;
/* 532:    */         }
/* 533:525 */         if (!changed) {
/* 534:526 */           for (int i = 0; i < goodDetections.size(); i++) {
/* 535:527 */             if (goodDetections.get(i) != OrcSetup.this.detections.get(i)) {
/* 536:528 */               changed = true;
/* 537:    */             }
/* 538:    */           }
/* 539:    */         }
/* 540:532 */         if (changed)
/* 541:    */         {
/* 542:533 */           OrcSetup.this.detections = goodDetections;
/* 543:535 */           if ((OrcSetup.this.selectedOrcDetection != null) && (!OrcSetup.this.detections.contains(OrcSetup.this.selectedOrcDetection)))
/* 544:    */           {
/* 545:536 */             OrcSetup.this.orcList.clearSelection();
/* 546:537 */             OrcSetup.this.listChanged();
/* 547:    */           }
/* 548:540 */           OrcSetup.this.mdl.changed();
/* 549:    */         }
/* 550:    */       }
/* 551:    */     }
/* 552:    */     
/* 553:    */     class ReaderThread
/* 554:    */       extends Thread
/* 555:    */     {
/* 556:    */       ReaderThread() {}
/* 557:    */       
/* 558:    */       public void run()
/* 559:    */       {
/* 560:    */         try
/* 561:    */         {
/* 562:550 */           runEx();
/* 563:    */         }
/* 564:    */         catch (IOException ex)
/* 565:    */         {
/* 566:552 */           System.out.println("Ex: " + ex);
/* 567:    */         }
/* 568:    */       }
/* 569:    */       
/* 570:    */       void runEx()
/* 571:    */         throws IOException
/* 572:    */       {
/* 573:    */         for (;;)
/* 574:    */         {
/* 575:559 */           byte[] buf = new byte[1600];
/* 576:560 */           DatagramPacket packet = new DatagramPacket(buf, buf.length);
/* 577:    */           
/* 578:562 */           OrcSetup.this.sock.receive(packet);
/* 579:563 */           DataInputStream ins = new DataInputStream(new ByteArrayInputStream(buf, 0, packet.getLength()));
/* 580:564 */           if (ins.available() >= 4)
/* 581:    */           {
/* 582:567 */             int magic = ins.readInt();
/* 583:569 */             if (magic == 1968132675)
/* 584:    */             {
/* 585:572 */               int bootNonce = ins.readInt();
/* 586:    */               
/* 587:574 */               OrcSetup.OrcDetection od = (OrcSetup.OrcDetection)OrcSetup.FindOrcThread.this.detectionsMap.get(Integer.valueOf(bootNonce));
/* 588:575 */               if (od == null)
/* 589:    */               {
/* 590:576 */                 od = new OrcSetup.OrcDetection(OrcSetup.this);
/* 591:577 */                 od.bootNonce = bootNonce;
/* 592:578 */                 OrcSetup.FindOrcThread.this.detectionsMap.put(Integer.valueOf(bootNonce), od);
/* 593:    */               }
/* 594:581 */               od.addr = packet.getAddress();
/* 595:582 */               od.mstime = System.currentTimeMillis();
/* 596:    */               
/* 597:584 */               od.magic = OrcSetup.this.swap(ins.readInt());
/* 598:585 */               od.version = OrcSetup.this.swap(ins.readInt());
/* 599:586 */               od.length = OrcSetup.this.swap(ins.readInt());
/* 600:    */               
/* 601:588 */               od.ip4addr = OrcSetup.this.swap(ins.readInt());
/* 602:589 */               od.ip4mask = OrcSetup.this.swap(ins.readInt());
/* 603:590 */               od.ip4gw = OrcSetup.this.swap(ins.readInt());
/* 604:591 */               od.macaddr = OrcSetup.this.swap(ins.readLong());
/* 605:592 */               od.dhcpd_enable = (ins.readByte() != 0);
/* 606:    */               
/* 607:594 */               od.magic2 = OrcSetup.this.swap(ins.readInt());
/* 608:    */             }
/* 609:    */           }
/* 610:    */         }
/* 611:    */       }
/* 612:    */     }
/* 613:    */   }
/* 614:    */ }

