package GlobalNavigation;

import java.util.*;
import java.awt.geom.*;

class MotionPlanner
{
    protected Graph g;

    public MotionPlanner(CSpace c,Point2D.Double start,Point2D.Double end)
    {
        ArrayList<Graph.Vertex> vertices=new ArrayList<Graph.Vertex>();
        vertices.add(new Graph.Vertex(start.getX(),start.getY()));
        vertices.add(new Graph.Vertex(end.getX(),end.getY()));

        for(Iterator<PolygonObstacle> iterp = c.getObstacles().iterator(); iterp.hasNext();)
        {
            PolygonObstacle p = iterp.next();
            for(Iterator<Point2D.Double> iterv = p.getVertices().iterator(); iterv.hasNext();)
            {
                Point2D.Double v=iterv.next();
                vertices.add(new Graph.Vertex(v.getX(),v.getY()));
            }
        }

        for(int v1=0;v1<vertices.size();v1++)
        {
            for(int v2=0;v2<vertices.size();v2++)
            {
                if(v1==v2)
                {
                    continue;
                }
                double x1=vertices.get(v1).x;
                double y1=vertices.get(v1).y;
                double x2=vertices.get(v2).x;
                double y2=vertices.get(v2).y;

                boolean skip=false;
                for(Iterator<PolygonObstacle> iterp = c.getObstacles().iterator(); iterp.hasNext();)
                {
                    PolygonObstacle p = iterp.next();
                    if(p.contains((x1+x2)/2,(y1+y2)/2)) // If shape contains midpoint, discard
                    {
                        skip=true;
                        break;
                    }

                    Object[] pts=p.getVertices().toArray();
                    for(int i=0;i<pts.length;i++)
                    {
                        Point2D.Double pt1=(Point2D.Double)pts[i];
                        Point2D.Double pt2=(Point2D.Double)pts[(i+1)%pts.length];
                        if(intersects(x1,y1,x2,y2,pt1.getX(),pt1.getY(),pt2.getX(),pt2.getY()))
                        {
                            skip=true;
                            break;
                        }
                    }
                    if(skip)
                    {
                        break;
                    }
                }
                if(!skip)
                {
                    vertices.get(v1).connections.add(new Graph.Vertex.Edge(v2,Math.hypot(x2-x1,y2-y1)));
                }
            }
        }
        g = new Graph(vertices);
    }

    public static boolean intersects(double x1,double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {
        double denom=(x2-x1)*(y4-y3)-(y2-y1)*(x4-x3);
        if(denom == 0)
        {
            return false;
        }
        double t=((x3-x1)*(y4-y3)-(y3-y1)*(x4-x3))/denom;
        double u=((x3-x1)*(y2-y1)-(y3-y1)*(x2-x1))/denom;
        if(t>0 && t<1 && u>0 && u<1)
        {
            return true;
        }
        return false;
    }

    public ArrayList<Point2D.Double> search()
    {
        Graph.Path p = g.search(0,1);
        ArrayList<java.awt.geom.Point2D.Double> out = new ArrayList<java.awt.geom.Point2D.Double>();

        for(int i=0;i<p.size();i++)
        {
            Graph.Vertex e=g.vertices.get(p.get(i).other);
            out.add(new Point2D.Double(e.x,e.y));
        }
        return out;
    }
}
