/*   1:    */ package orc.spy;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Container;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.LayoutManager;
/*   7:    */ import java.awt.Point;
/*   8:    */ import java.util.HashMap;
/*   9:    */ 
/*  10:    */ public class WeightedGridLayout
/*  11:    */   implements LayoutManager
/*  12:    */ {
/*  13:    */   int hgap;
/*  14:    */   int vgap;
/*  15:    */   double[] columnWeights;
/*  16:    */   int cols;
/*  17:    */   int rows;
/*  18: 35 */   HashMap<Integer, Double> rowWeightsMap = new HashMap();
/*  19: 36 */   double defaultRowWeight = 0.0D;
/*  20: 38 */   double[] rowWeights = null;
/*  21: 40 */   int minimumWidth = 0;
/*  22: 41 */   int minimumHeight = 0;
/*  23:    */   int[] minimumColumnWidth;
/*  24:    */   int[] minimumRowHeight;
/*  25:    */   
/*  26:    */   public WeightedGridLayout(double[] columnWeights, int hgap, int vgap)
/*  27:    */   {
/*  28: 54 */     this.hgap = hgap;
/*  29: 55 */     this.vgap = vgap;
/*  30: 56 */     this.columnWeights = normalizeSumToOne(columnWeights);
/*  31: 57 */     this.cols = columnWeights.length;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public WeightedGridLayout(double[] columnWeights)
/*  35:    */   {
/*  36: 62 */     this(columnWeights, 4, 4);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setDefaultRowWeight(double v)
/*  40:    */   {
/*  41: 67 */     this.defaultRowWeight = v;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setRowWeight(int row, double v)
/*  45:    */   {
/*  46: 72 */     this.rowWeightsMap.put(Integer.valueOf(row), Double.valueOf(v));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void addLayoutComponent(String name, Component comp) {}
/*  50:    */   
/*  51:    */   Component getComponentXY(Container parent, int x, int y)
/*  52:    */   {
/*  53: 85 */     return parent.getComponents()[(x + y * this.cols)];
/*  54:    */   }
/*  55:    */   
/*  56:    */   int sum(int[] v)
/*  57:    */   {
/*  58: 90 */     int acc = 0;
/*  59: 91 */     for (int i = 0; i < v.length; i++) {
/*  60: 92 */       acc += v[i];
/*  61:    */     }
/*  62: 93 */     return acc;
/*  63:    */   }
/*  64:    */   
/*  65:    */   double[] normalizeSumToOne(double[] v)
/*  66:    */   {
/*  67: 98 */     double acc = 0.0D;
/*  68: 99 */     for (int i = 0; i < v.length; i++) {
/*  69:100 */       acc += v[i];
/*  70:    */     }
/*  71:103 */     double[] res = new double[v.length];
/*  72:105 */     for (int i = 0; i < v.length; i++) {
/*  73:106 */       res[i] = (acc == 0.0D ? 1.0D / v.length : v[i] / acc);
/*  74:    */     }
/*  75:108 */     return res;
/*  76:    */   }
/*  77:    */   
/*  78:    */   void computeGeometry(Container parent)
/*  79:    */   {
/*  80:117 */     this.rows = (parent.getComponents().length / this.cols);
/*  81:    */     
/*  82:    */ 
/*  83:120 */     this.rowWeights = new double[this.rows];
/*  84:121 */     for (int i = 0; i < this.rowWeights.length; i++) {
/*  85:122 */       if (this.rowWeightsMap.get(Integer.valueOf(i)) == null) {
/*  86:123 */         this.rowWeights[i] = this.defaultRowWeight;
/*  87:    */       } else {
/*  88:125 */         this.rowWeights[i] = ((Double)this.rowWeightsMap.get(Integer.valueOf(i))).doubleValue();
/*  89:    */       }
/*  90:    */     }
/*  91:127 */     this.rowWeights = normalizeSumToOne(this.rowWeights);
/*  92:    */     
/*  93:    */ 
/*  94:130 */     this.minimumColumnWidth = new int[this.cols];
/*  95:131 */     this.minimumRowHeight = new int[this.rows];
/*  96:133 */     for (int col = 0; col < this.cols; col++) {
/*  97:134 */       for (int row = 0; row < this.rows; row++)
/*  98:    */       {
/*  99:135 */         Dimension thisdim = getComponentXY(parent, col, row).getMinimumSize();
/* 100:    */         
/* 101:137 */         this.minimumColumnWidth[col] = ((int)Math.max(this.minimumColumnWidth[col], thisdim.getWidth()));
/* 102:    */         
/* 103:    */ 
/* 104:140 */         this.minimumRowHeight[row] = ((int)Math.max(this.minimumRowHeight[row], thisdim.getHeight()));
/* 105:    */       }
/* 106:    */     }
/* 107:145 */     this.minimumWidth = (sum(this.minimumColumnWidth) + this.hgap * (this.cols - 1));
/* 108:146 */     this.minimumHeight = (sum(this.minimumRowHeight) + this.vgap * (this.rows - 1));
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void layoutContainer(Container parent)
/* 112:    */   {
/* 113:152 */     computeGeometry(parent);
/* 114:    */     
/* 115:    */ 
/* 116:155 */     int extraWidth = parent.getWidth() - this.minimumWidth;
/* 117:156 */     int extraHeight = parent.getHeight() - this.minimumHeight;
/* 118:    */     
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:165 */     int[] columnWidth = new int[this.cols];
/* 127:166 */     for (int col = 0; col < this.cols; col++) {
/* 128:167 */       columnWidth[col] = ((int)(this.minimumColumnWidth[col] + this.columnWeights[col] * extraWidth));
/* 129:    */     }
/* 130:169 */     int[] rowHeight = new int[this.rows];
/* 131:170 */     for (int row = 0; row < this.rows; row++) {
/* 132:171 */       rowHeight[row] = ((int)(this.minimumRowHeight[row] + this.rowWeights[row] * extraHeight));
/* 133:    */     }
/* 134:176 */     int[] columnPosition = new int[this.cols];
/* 135:177 */     for (int col = 1; col < this.cols; col++) {
/* 136:178 */       columnPosition[col] += columnPosition[(col - 1)] + columnWidth[(col - 1)];
/* 137:    */     }
/* 138:180 */     int[] rowPosition = new int[this.rows];
/* 139:181 */     for (int row = 1; row < this.rows; row++) {
/* 140:182 */       rowPosition[row] += rowPosition[(row - 1)] + rowHeight[(row - 1)];
/* 141:    */     }
/* 142:185 */     for (int row = 0; row < this.rows; row++) {
/* 143:186 */       for (int col = 0; col < this.cols; col++)
/* 144:    */       {
/* 145:188 */         Component c = getComponentXY(parent, col, row);
/* 146:189 */         c.setSize(new Dimension(columnWidth[col], rowHeight[row]));
/* 147:190 */         c.setLocation(new Point(columnPosition[col], rowPosition[row]));
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public Dimension minimumLayoutSize(Container parent)
/* 153:    */   {
/* 154:197 */     computeGeometry(parent);
/* 155:198 */     return new Dimension(this.minimumWidth, this.minimumHeight);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public Dimension preferredLayoutSize(Container parent)
/* 159:    */   {
/* 160:203 */     return minimumLayoutSize(parent);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void removeLayoutComponent(Component comp) {}
/* 164:    */ }


/* Location:           C:\Users\Aldebaran_\Documents\GitHub\rss-2014-team-3\src\rosjava_pkg\orc_utils\src\main\java\orc-0.0.jar
 * Qualified Name:     orc.spy.WeightedGridLayout
 * JD-Core Version:    0.7.0.1
 */