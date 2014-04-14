package VisualServo;

import java.awt.Color;
import java.util.Stack;

/**
 * BlobTracking performs image processing and tracking for the VisualServo
 * module.  BlobTracking filters raw pixels from an image and classifies blobs,
 * generating a higher-level vision image.
 *
 * @author previous TA's, prentice
 */
public class BlobTracking {
	protected int stepCounter = 0;
	protected double lastStepTime = 0.0;

	public int width;
	public int height;


	// Variables used for velocity controller that are available to calling
	// process.  Visual results are valid only if targetDetected==true; motor
	// velocities should do something sane in this case.
	public boolean targetDetected = false; // set in blobPresent()
	public double centroidX = 0.0; // set in blobPresent()
	public double centroidY = 0.0; // set in blobPresent()
	public double targetArea = 0.0; // set in blobPresent()
	public double targetRange = 0.0; // set in blobFix()
	public double targetBearing = 0.0; // set in blobFix()
    

    //Search Parameters
    private double h1 = 100;
    private double h2 = 200;
    private double s1 = 150;
    private double b1 = 100;
	/**
	 * <p>Create a BlobTracking object</p>
	 *
	 * @param width image width
	 * @param height image height
	 */
	public BlobTracking(int width, int height) {

		this.width = width;
		this.height = height;

	}


    private void setParam(double inh1, double inh2, double ins1, double inb1){
        h1 = inh1;
        h2 = inh2;
        s1 = ins1;
        b1 = inb1;
    }


	/**
	 * <p>Computes frame rate of vision processing</p>
	 */
	private void stepTiming() {
		double currTime = System.currentTimeMillis();
		stepCounter++;
		// if it's been a second, compute frames-per-second
		if (currTime - lastStepTime > 1000.0) {
			//double fps = (double) stepCounter * 1000.0
			// / (currTime - lastStepTime);
			//System.err.println("FPS: " + fps);
			stepCounter = 0;
			lastStepTime = currTime;
		}
	}

	/**
	 * <p>Segment out a blob from the src image (if a good candidate exists).</p>
	 *
	 * <p><code>dest</code> is a packed RGB image for a java image drawing
	 * routine. If it's not null, the blob is highlighted.</p>
	 *
	 * @param src the source RGB image, not packed
	 * @param dest the destination RGB image, packed, may be null
	 */
	public void apply(Image src, Image dest) {

		stepTiming(); // monitors the frame rate

		// Begin Student Code

		Image temp=new Image(src.getWidth(),src.getHeight());
		Image temp1=new Image(src.getWidth(),src.getHeight());
		Image temp2=new Image(src.getWidth(),src.getHeight());
		int[][] map=new int[src.getHeight()][src.getWidth()];

		// Apply a median filter to the image
		MedianFilter medFilter = new MedianFilter(3);
		medFilter.filter(src, temp);		
		
                // Apply a filter and generate a map
                redFilter(temp, temp1, map);

		// Find islands
		int num_islands=connectedComponents(map);

		// Analyze all islands (generate BlobData for each)
		BlobData[] bd=analyze(map,num_islands);

		// Find best candidate
		BlobData best=null;
		for(int i=0;i<num_islands;i++)
		{
		    if(bd[i].score > 0 && (best == null || bd[i].score > best.score)) {
			best=bd[i];
		    }
		}

		// Display
		if(best != null) { // If we found a suitable blob
		    annotate_image(temp1,dest,best); // Draw on image
		    targetDetected=true;
                    centroidX=best.centroidX;
                    centroidY=best.centroidY;
		    targetArea=best.radius*best.radius*Math.PI;
		}
		else { // Otherwise,
		    dest.fromArray(temp1.toArray()); // Just copy to output
		    targetDetected=false;
		}

		//Histogram.getHistogram(src,dest,true);

		// End Student Code
	}
	
    // filters image so red pixels are bright red and others are average hue
    // also puts -1 in map where the pixels are red
    public void redFilter(Image src, Image dest, int[][] map){
	int width = src.getWidth();
	int height = src.getHeight();

	/*
	GaussianBlur gaussB = new GaussianBlur();
	byte[] src_array=src.toArray();
	byte[] blurB = new byte[src_array.length];
	byte[] blurB2 = new byte[src_array.length];
	gaussB.apply(src_array, blurB, width, height);
	gaussB.apply(blurB, blurB2, width, height);
	dest.fromArray(blurB2);
	*/
	dest.fromArray(src.toArray());

	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		int pixel = dest.getPixel(x,y);
		int redPixel = dest.pixelRed(pixel) & ~-256;
		int greenPixel = dest.pixelGreen(pixel) & ~-256;
		int bluePixel = dest.pixelBlue(pixel) & ~-256;
		int avePixel = (redPixel + greenPixel + bluePixel) / 3;
		float[] hsb = Color.RGBtoHSB(redPixel, greenPixel, bluePixel, null);

		hsb[0] = Math.round(hsb[0]*255); // hue
		//System.out.println(hsb[0]);
		hsb[1] = Math.round(hsb[1]*255); // saturation
		hsb[2] = Math.round(hsb[2]*255); // brightness
		//System.out.println(hsb[0] + ": " + hsb[1]+ ": " + hsb[2]);

		if ( hsb[0] > h1 && hsb[0] < h2 && hsb[1] > s1 &&  hsb[2] > b1) {   // hsb[1] >188, hsb[2] > 194
		    dest.setPixel(x,y,(byte) 0xff,(byte) 0x00,(byte) 0x00);
                    map[y][x]=-1;
		}
		else{
		    dest.setPixel(x,y, (byte) avePixel, (byte) avePixel, (byte) avePixel);
                    map[y][x]=0;
		}
	    }
	}
    }

    // input: a map with 0 = water, negative number = land
    // output: number of islands
    // side-effect: map now has islands coded by positive numbers
    public int connectedComponents(int[][] map)
    {
	int height=map.length;
	int width=map[0].length;
        int c=0; // Number of islands found

	for(int x=0;x<width;x++) {
	    for(int y=0;y<height;y++) {
		if(map[y][x] < 0) { // negative numbers represent new islands
		    c++;
                    // Flood fill implementation
                    Stack<Integer> xs = new Stack<Integer>();
                    Stack<Integer> ys = new Stack<Integer>(); // Parallel stacks for DFS
		    xs.push(x);
		    ys.push(y);
                    while(!xs.empty()) {
			int cur_x=xs.pop();
			int cur_y=ys.pop();
			if(cur_x < 0 || cur_x >= width || cur_y < 0 || cur_y >= height || map[cur_y][cur_x] >= 0) continue;
                        map[cur_y][cur_x] = c;
                        xs.push(cur_x-1); ys.push(cur_y);
                        xs.push(cur_x+1); ys.push(cur_y);
                        xs.push(cur_x); ys.push(cur_y-1);
                        xs.push(cur_x); ys.push(cur_y+1);
		    }
                }
            }
        }
        return c;
    }

    public void gausDiff ( Image src, Image dest, int width, int height) {
	
	GaussianBlur gauss = new GaussianBlur();
	byte blurbyte[] = src.toArray();
	byte blurbyte2[] = src.toArray();
	byte blurbyte3[] = src.toArray();
	byte subblur[] = src.toArray();
	gauss.apply(src.toArray(), blurbyte, width, height);
	//Image blur = new Image(blurbyte, width, height);
	gauss.apply(blurbyte, blurbyte2, width, height);
	//Image blur2 = new Image(blurbyte, width, height);
	for (int i = 0; i < blurbyte.length; i ++) {
	    subblur[i] = (byte)(blurbyte2[i] - blurbyte[i]);
	    //st.setPixel( i % width, i / width, 
	}
	//blur = new Image(subblur, width, height);
	
	dest.fromArray(subblur);
    }

    // set centroidX, centroidY, targetArea, target detected
    // if ball is on screen, draw a small x at the center of percieved blob and a box around it's body
    public BlobData[] analyze( int[][] map, int num_islands ) {
	int height = map.length;
	int width = map[0].length;

	float[] integral = new float[num_islands];
	float[] first_moment_x = new float[num_islands];
	float[] first_moment_y = new float[num_islands];
	float[] second_moment_x = new float[num_islands];
	float[] second_moment_y = new float[num_islands];
	BlobData[] bd=new BlobData[num_islands];

	// Initialize all arrays
	for(int i=0;i<num_islands;i++) {
	    integral[i]=0;
	    first_moment_x[i]=0;
	    first_moment_y[i]=0;
	    second_moment_x[i]=0;
	    second_moment_y[i]=0;
	}

	// Scan image
	for (int x = 0; x < width; x++) {
	    for (int y =0; y < height; y++) { 
                int island_no=map[y][x]-1;
		if(island_no >= 0) {
		    integral[island_no]+=1;
		    first_moment_x[island_no]+=x;
		    first_moment_y[island_no]+=y;
		    second_moment_x[island_no]+=x*x;
		    second_moment_y[island_no]+=y*y;
                }
	    }
	}

	// Calulate parameters for each island
       	for(int i=0;i<num_islands;i++) {
	    float centroid_x=first_moment_x[i] / integral[i];
	    float centroid_y=first_moment_y[i] / integral[i];
	    float variance_x=(second_moment_x[i] / integral[i] - centroid_x*centroid_x);
	    float variance_y=(second_moment_y[i] / integral[i] - centroid_y*centroid_y);
	    float radius=2*(float)Math.sqrt((variance_x+variance_y)/2);
            float score;
            if(integral[i] < width*height*0.001 || // If ball takes up less than 0.5% of screen
	       integral[i] > width*height*0.7 || // or more than 70% of screen
               variance_x/variance_y > 2 || variance_y/variance_x > 2) { // or ball is squished by a factor of more than two
		score=0;
	    } else {
		score=integral[i];
            }
	    bd[i]=new BlobData(centroid_x,centroid_y,radius,score);
	}
	return bd;
    }

    // Takes a blobdata and draws relevant info onto the image (centroid, radius)
    public void annotate_image(Image src, Image dest, BlobData bd)
    {
        dest.fromArray(src.toArray());

        int centerx=(int)bd.centroidX;
        int centery=(int)bd.centroidY;
        int radius=(int)bd.radius;
	for (int i = -4; i < 4; i++) {
	    dest.setPixel((centerx+i), centery, (byte) 0x00, (byte) 0xFF, (byte) 0x00);
	    dest.setPixel(centerx, centery+i, (byte) 0x00, (byte) 0xFF, (byte) 0x00);
	}

	for (int j = -radius; j < radius; j++) {
	    dest.setPixel( (centerx+j), (centery + radius), (byte) 0x00, (byte) 0xFF, (byte) 0x00);
	    dest.setPixel( (centerx+j), (centery - radius), (byte) 0x00, (byte) 0xFF, (byte) 0x00);
	}
	
	for (int k = -radius; k < radius; k++) {
	    dest.setPixel( (centerx+radius), (centery + k), (byte) 0x00, (byte) 0xFF, (byte) 0x00);
	    dest.setPixel( (centerx-radius), (centery + k), (byte) 0x00, (byte) 0xFF, (byte) 0x00);
	}
    }

    //return centroidX, centroidY
    public double[] getCentroid() {
	return new double[] {centroidX, centroidY};
    }

    //return targetDetected
    public boolean getTargetDetected(){
	return targetDetected;
    }

    public double getTargetArea(){
	return targetArea;
    }

    private class BlobData
    {
        public float centroidX;
        public float centroidY;
        public float radius;
        public float score;

        public BlobData(float centroidX,float centroidY,float radius,float score)
	{
		this.centroidX=centroidX;
		this.centroidY=centroidY;
		this.radius=radius;
		this.score=score;
	}
    }
}




