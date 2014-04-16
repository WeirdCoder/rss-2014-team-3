package LocalNavigation;
import java.util.ArrayList;

class LineEstimator
{
	private double a;
	private double b;
	private double c;

	private double sum_x;
	private double sum_y;
	private double sum_xx;
	private double sum_yy;
	private double sum_xy;
	private int n;

    private boolean nextIsStart;
    private boolean lastIsEnd;

    private double start_x;
    private double start_y;
    private double end_x;
    private double end_y;

	public LineEstimator()
	{
		clear();
	}

    public double[] getLineParams(){
	   return new double[]{a, b, c};
	}

    public boolean ready()
    {
        return n >= 2;
    }

    public void saveNextPointAsStart()
    {
        nextIsStart=true;
    }

    public void saveLastPointAsEnd()
    {
        lastIsEnd=true;
    }
    
	public void add(double x,double y)
	{
		n++;

		sum_x+=x;
		sum_y+=y;
		sum_xx+=x*x;
		sum_yy+=y*y;
		sum_xy+=x*y;

        if(nextIsStart)
        {
            nextIsStart=false;
            start_x=x;
            start_y=y;
        }

        if(lastIsEnd)
        {
            end_x=x;
            end_y=y;
        }

		if(ready())
		{
			double d=sum_xx*sum_yy-sum_xy*sum_xy;
			double a=(sum_x*sum_yy-sum_y*sum_xy)/d;
			double b=(sum_y*sum_xx-sum_x*sum_xy)/d;
			double h=(double)Math.hypot(a,b);

			synchronized(this)
			{
				this.a=a/h;
				this.b=b/h;
				this.c=-1/h;
			}
		}
	}

	public double distance(double x,double y)
	{
		double d;
		synchronized(this)
		{
			d=Math.abs(a*x+b*y+c);
		}
		return d;
	}

    public double[] getClosestPoint(double x,double y)
    {
        return new double[]{b*(b*x-a*y)-a*c,a*(-b*x+a*y)-b*c};
    }

    public double[] getSegment()
    {
        double[] start=getClosestPoint(start_x,start_y);
        double[] end=getClosestPoint(end_x,end_y);
        return new double[] {start[0],start[1],end[0],end[1]};
    }

	public void clear()
	{
		a=0;
		b=0;
		c=0;
		sum_x=0;
		sum_y=0;
		sum_xx=0;
		sum_yy=0;
		sum_xy=0;
		n=0;
        nextIsStart=false;
        lastIsEnd=false;
        start_x=0;
        start_y=0;
        end_x=0;
        end_y=0;
	}
}
