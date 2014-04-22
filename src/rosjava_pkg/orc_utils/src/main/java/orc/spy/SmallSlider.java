/*   1:    */ package orc.spy;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Container;
/*   6:    */ import java.awt.Dimension;
/*   7:    */ import java.awt.Font;
/*   8:    */ import java.awt.Graphics;
/*   9:    */ import java.awt.Graphics2D;
/*  10:    */ import java.awt.RenderingHints;
/*  11:    */ import java.awt.event.MouseEvent;
/*  12:    */ import java.awt.event.MouseListener;
/*  13:    */ import java.awt.event.MouseMotionListener;
/*  14:    */ import java.awt.geom.Ellipse2D;
/*  15:    */ import java.awt.geom.RoundRectangle2D;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import javax.swing.JComponent;
/*  18:    */ 
/*  19:    */ public class SmallSlider
/*  20:    */   extends JComponent
/*  21:    */ {
/*  22: 17 */   int barheight = 8;
/*  23: 19 */   int minvalue = 0;
/*  24: 20 */   int maxvalue = 100;
/*  25:    */   int goalvalue;
/*  26:    */   int actualvalue;
/*  27: 24 */   int goalknobsize = 6;
/*  28: 25 */   int actualknobsize = 10;
/*  29: 27 */   int totalheight = this.actualknobsize + 4;
/*  30: 29 */   int marginx = 6;
/*  31: 31 */   ArrayList<Listener> gsls = new ArrayList();
/*  32: 33 */   boolean copyactual = false;
/*  33: 35 */   boolean showactual = true;
/*  34: 37 */   public String formatString = "%.0f";
/*  35: 38 */   public double formatScale = 1.0D;
/*  36:    */   
/*  37:    */   public SmallSlider(int min, int max, boolean showactual)
/*  38:    */   {
/*  39: 49 */     this.minvalue = min;
/*  40: 50 */     this.maxvalue = max;
/*  41: 51 */     this.goalvalue = this.minvalue;
/*  42: 52 */     this.actualvalue = this.minvalue;
/*  43: 53 */     this.showactual = showactual;
/*  44:    */     
/*  45: 55 */     addMouseMotionListener(new SmallSliderMouseMotionListener());
/*  46: 56 */     addMouseListener(new SmallSliderMouseMotionListener());
/*  47:    */     
/*  48: 58 */     this.copyactual = true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public SmallSlider(int min, int max, int goalvalue, int actualvalue, boolean showactual)
/*  52:    */   {
/*  53: 63 */     this.minvalue = min;
/*  54: 64 */     this.maxvalue = max;
/*  55:    */     
/*  56: 66 */     this.goalvalue = goalvalue;
/*  57: 67 */     this.actualvalue = actualvalue;
/*  58: 68 */     this.showactual = showactual;
/*  59:    */     
/*  60: 70 */     addMouseMotionListener(new SmallSliderMouseMotionListener());
/*  61: 71 */     addMouseListener(new SmallSliderMouseMotionListener());
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void addListener(Listener gsl)
/*  65:    */   {
/*  66: 76 */     this.gsls.add(gsl);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setMaximum(int i)
/*  70:    */   {
/*  71: 81 */     this.maxvalue = i;
/*  72: 82 */     repaint();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setMinimum(int i)
/*  76:    */   {
/*  77: 87 */     this.minvalue = i;
/*  78: 88 */     repaint();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public synchronized void setActualValue(int i)
/*  82:    */   {
/*  83: 93 */     if (this.copyactual)
/*  84:    */     {
/*  85: 95 */       this.goalvalue = i;
/*  86: 96 */       this.copyactual = false;
/*  87:    */     }
/*  88: 99 */     this.actualvalue = i;
/*  89:100 */     repaint();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setGoalValue(int i)
/*  93:    */   {
/*  94:106 */     this.goalvalue = i;
/*  95:107 */     repaint();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int getGoalValue()
/*  99:    */   {
/* 100:112 */     return this.goalvalue;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int getActualValue()
/* 104:    */   {
/* 105:117 */     return this.actualvalue;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Dimension getMinimumSize()
/* 109:    */   {
/* 110:122 */     return new Dimension(40, this.totalheight);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public Dimension getPreferredSize()
/* 114:    */   {
/* 115:127 */     return getMinimumSize();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public synchronized void paint(Graphics gin)
/* 119:    */   {
/* 120:132 */     Graphics2D g = (Graphics2D)gin;
/* 121:133 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 122:    */     
/* 123:135 */     int height = getHeight();
/* 124:136 */     int width = getWidth() - 2 * this.marginx;
/* 125:137 */     int cy = height / 2;
/* 126:138 */     int cx = width / 2;
/* 127:    */     
/* 128:    */ 
/* 129:141 */     g.translate(this.marginx, 0);
/* 130:    */     
/* 131:143 */     g.setColor(getParent().getBackground());
/* 132:144 */     g.fillRect(0, 0, width, height);
/* 133:    */     
/* 134:    */ 
/* 135:147 */     RoundRectangle2D.Double barr = new RoundRectangle2D.Double(0.0D, cy - this.barheight / 2, width, this.barheight, this.barheight, this.barheight);
/* 136:    */     
/* 137:    */ 
/* 138:    */ 
/* 139:151 */     g.setColor(Color.white);
/* 140:152 */     g.fill(barr);
/* 141:153 */     g.setColor(Color.black);
/* 142:154 */     g.draw(barr);
/* 143:    */     
/* 144:    */ 
/* 145:157 */     int x = width * (this.goalvalue - this.minvalue) / (this.maxvalue - this.minvalue);
/* 146:158 */     Ellipse2D.Double goalknob = new Ellipse2D.Double(x - this.goalknobsize / 2, cy - this.goalknobsize / 2, this.goalknobsize, this.goalknobsize);
/* 147:    */     
/* 148:    */ 
/* 149:161 */     g.setColor(Color.green);
/* 150:162 */     g.fill(goalknob);
/* 151:163 */     g.setStroke(new BasicStroke(1.0F));
/* 152:164 */     g.setColor(Color.black);
/* 153:165 */     g.draw(goalknob);
/* 154:166 */     g.setFont(new Font("Monospaced", 0, 11));
/* 155:168 */     if (this.showactual)
/* 156:    */     {
/* 157:171 */       x = width * (this.actualvalue - this.minvalue) / (this.maxvalue - this.minvalue);
/* 158:172 */       g.setColor(Color.black);
/* 159:173 */       g.setStroke(new BasicStroke(1.0F));
/* 160:174 */       Ellipse2D.Double actualknob = new Ellipse2D.Double(x - this.actualknobsize / 2, cy - this.actualknobsize / 2, this.actualknobsize, this.actualknobsize);
/* 161:    */       
/* 162:    */ 
/* 163:    */ 
/* 164:178 */       g.draw(actualknob);
/* 165:    */     }
/* 166:183 */     g.setColor(Color.black);
/* 167:184 */     String s = String.format(this.formatString, new Object[] { Double.valueOf(this.formatScale * this.goalvalue) });
/* 168:185 */     g.drawString(s, width - s.length() * 8, cy + 16);
/* 169:    */   }
/* 170:    */   
/* 171:    */   void handleClick(int x)
/* 172:    */   {
/* 173:191 */     this.goalvalue = (this.minvalue + (this.maxvalue - this.minvalue) * (x - this.marginx) / (getWidth() - 2 * this.marginx));
/* 174:192 */     if (this.goalvalue < this.minvalue) {
/* 175:193 */       this.goalvalue = this.minvalue;
/* 176:    */     }
/* 177:194 */     if (this.goalvalue > this.maxvalue) {
/* 178:195 */       this.goalvalue = this.maxvalue;
/* 179:    */     }
/* 180:197 */     for (Listener gsl : this.gsls) {
/* 181:198 */       gsl.goalValueChanged(this, this.goalvalue);
/* 182:    */     }
/* 183:200 */     repaint();
/* 184:    */   }
/* 185:    */   
/* 186:    */   class SmallSliderMouseMotionListener
/* 187:    */     implements MouseMotionListener, MouseListener
/* 188:    */   {
/* 189:    */     SmallSliderMouseMotionListener() {}
/* 190:    */     
/* 191:    */     public void mouseDragged(MouseEvent e)
/* 192:    */     {
/* 193:207 */       SmallSlider.this.handleClick(e.getX());
/* 194:    */     }
/* 195:    */     
/* 196:    */     public void mouseMoved(MouseEvent e) {}
/* 197:    */     
/* 198:    */     public void mouseClicked(MouseEvent e)
/* 199:    */     {
/* 200:216 */       SmallSlider.this.handleClick(e.getX());
/* 201:    */     }
/* 202:    */     
/* 203:    */     public void mouseEntered(MouseEvent e) {}
/* 204:    */     
/* 205:    */     public void mouseExited(MouseEvent e) {}
/* 206:    */     
/* 207:    */     public void mousePressed(MouseEvent e)
/* 208:    */     {
/* 209:229 */       SmallSlider.this.handleClick(e.getX());
/* 210:    */     }
/* 211:    */     
/* 212:    */     public void mouseReleased(MouseEvent e) {}
/* 213:    */   }
/* 214:    */   
/* 215:    */   public static abstract interface Listener
/* 216:    */   {
/* 217:    */     public abstract void goalValueChanged(SmallSlider paramSmallSlider, int paramInt);
/* 218:    */   }
/* 219:    */ }

