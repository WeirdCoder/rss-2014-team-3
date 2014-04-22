/*   1:    */ package orc.examples;
/*   2:    */ 
/*   3:    */ import java.awt.BorderLayout;
/*   4:    */ import java.awt.GridLayout;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import javax.swing.BorderFactory;
/*   8:    */ import javax.swing.JFrame;
/*   9:    */ import javax.swing.JLabel;
/*  10:    */ import javax.swing.JPanel;
/*  11:    */ import javax.swing.JSlider;
/*  12:    */ import javax.swing.event.ChangeEvent;
/*  13:    */ import javax.swing.event.ChangeListener;
/*  14:    */ import orc.AX12Servo;
/*  15:    */ import orc.AX12Status;
/*  16:    */ import orc.Orc;
/*  17:    */ import orc.OrcStatus;
/*  18:    */ 
/*  19:    */ public class AX12Demo
/*  20:    */ {
/*  21: 13 */   JFrame jf = new JFrame("AX12Demo");
/*  22:    */   ArrayList<AX12Servo> servos;
/*  23:    */   
/*  24:    */   public static void main(String[] args)
/*  25:    */   {
/*  26: 18 */     Orc orc = Orc.makeOrc();
/*  27: 19 */     orc.verbose = true;
/*  28:    */     
/*  29: 21 */     System.out.printf("Checking communication with Orc board...", new Object[0]);
/*  30: 22 */     System.out.flush();
/*  31: 23 */     OrcStatus os = orc.getStatus();
/*  32: 24 */     System.out.println("good!");
/*  33:    */     
/*  34: 26 */     System.out.println("Scanning for AX12 servos...");
/*  35: 27 */     ArrayList<AX12Servo> servos = new ArrayList();
/*  36: 29 */     for (int id = 0; id < 254; id++)
/*  37:    */     {
/*  38: 30 */       System.out.printf("%4d\r", new Object[] { Integer.valueOf(id) });
/*  39: 31 */       System.out.flush();
/*  40: 32 */       AX12Servo servo = new AX12Servo(orc, id);
/*  41: 33 */       if (servo.ping())
/*  42:    */       {
/*  43: 34 */         System.out.printf("%d  found!\n", new Object[] { Integer.valueOf(id) });
/*  44: 35 */         servos.add(servo);
/*  45:    */       }
/*  46:    */     }
/*  47: 39 */     if (servos.size() == 0)
/*  48:    */     {
/*  49: 40 */       System.out.println("No AX12 servos found.");
/*  50: 41 */       System.exit(0);
/*  51:    */     }
/*  52: 44 */     new AX12Demo(servos);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public AX12Demo(ArrayList<AX12Servo> servos)
/*  56:    */   {
/*  57: 50 */     this.servos = servos;
/*  58:    */     
/*  59: 52 */     this.jf.setLayout(new GridLayout(servos.size(), 1));
/*  60: 54 */     for (AX12Servo s : servos) {
/*  61: 55 */       this.jf.add(new AX12Widget(s));
/*  62:    */     }
/*  63: 58 */     this.jf.setSize(800, 400);
/*  64: 59 */     this.jf.setVisible(true);
/*  65:    */   }
/*  66:    */   
/*  67:    */   class AX12Widget
/*  68:    */     extends JPanel
/*  69:    */     implements ChangeListener
/*  70:    */   {
/*  71:    */     AX12Servo servo;
/*  72:    */     JSlider js;
/*  73: 66 */     JLabel positionLabel = new JLabel("");
/*  74: 67 */     JLabel speedLabel = new JLabel("");
/*  75: 68 */     JLabel loadLabel = new JLabel("");
/*  76: 69 */     JLabel voltageLabel = new JLabel("");
/*  77: 70 */     JLabel tempLabel = new JLabel("");
/*  78: 71 */     JLabel errorsLabel = new JLabel("");
/*  79: 73 */     JSlider positionSlider = new JSlider(0, 300, 150);
/*  80:    */     
/*  81:    */     public AX12Widget(AX12Servo servo)
/*  82:    */     {
/*  83: 77 */       this.servo = servo;
/*  84:    */       
/*  85: 79 */       setBorder(BorderFactory.createTitledBorder("AX12 id " + servo.getId()));
/*  86:    */       
/*  87: 81 */       setLayout(new BorderLayout());
/*  88:    */       
/*  89: 83 */       JPanel jp = new JPanel();
/*  90: 84 */       jp.setLayout(new GridLayout(6, 2));
/*  91: 85 */       jp.add(new JLabel("Position"));
/*  92: 86 */       jp.add(this.positionLabel);
/*  93: 87 */       jp.add(new JLabel("Speed"));
/*  94: 88 */       jp.add(this.speedLabel);
/*  95: 89 */       jp.add(new JLabel("Load"));
/*  96: 90 */       jp.add(this.loadLabel);
/*  97: 91 */       jp.add(new JLabel("Voltage"));
/*  98: 92 */       jp.add(this.voltageLabel);
/*  99: 93 */       jp.add(new JLabel("Temp. (C)  "));
/* 100: 94 */       jp.add(this.tempLabel);
/* 101: 95 */       jp.add(new JLabel("Errors"));
/* 102: 96 */       jp.add(this.errorsLabel);
/* 103:    */       
/* 104: 98 */       add(jp, "East");
/* 105: 99 */       add(this.positionSlider, "Center");
/* 106:100 */       this.positionSlider.addChangeListener(this);
/* 107:    */       
/* 108:102 */       new RunThread().start();
/* 109:    */     }
/* 110:    */     
/* 111:    */     public void stateChanged(ChangeEvent e)
/* 112:    */     {
/* 113:107 */       if (e.getSource() == this.positionSlider) {
/* 114:108 */         this.servo.setGoalDegrees(this.positionSlider.getValue(), 0.3D, 0.3D);
/* 115:    */       }
/* 116:    */     }
/* 117:    */     
/* 118:    */     class RunThread
/* 119:    */       extends Thread
/* 120:    */     {
/* 121:    */       RunThread() {}
/* 122:    */       
/* 123:    */       public void run()
/* 124:    */       {
/* 125:    */         for (;;)
/* 126:    */         {
/* 127:117 */           AX12Status status = AX12Demo.AX12Widget.this.servo.getStatus();
/* 128:118 */           AX12Demo.AX12Widget.this.positionLabel.setText(String.format("%.3f", new Object[] { Double.valueOf(status.positionDegrees) }));
/* 129:119 */           AX12Demo.AX12Widget.this.speedLabel.setText(String.format("%.3f", new Object[] { Double.valueOf(status.speed) }));
/* 130:120 */           AX12Demo.AX12Widget.this.loadLabel.setText(String.format("%.3f", new Object[] { Double.valueOf(status.load) }));
/* 131:121 */           AX12Demo.AX12Widget.this.voltageLabel.setText(String.format("%.3f", new Object[] { Double.valueOf(status.voltage) }));
/* 132:122 */           AX12Demo.AX12Widget.this.errorsLabel.setText(String.format("%02x", new Object[] { Integer.valueOf(status.error_flags) }));
/* 133:123 */           AX12Demo.AX12Widget.this.tempLabel.setText(String.format("%.1f", new Object[] { Double.valueOf(status.temperature) }));
/* 134:    */           try
/* 135:    */           {
/* 136:126 */             Thread.sleep(50L);
/* 137:    */           }
/* 138:    */           catch (InterruptedException ex) {}
/* 139:    */         }
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */ }

