package GlobalNavigation;

import java.util.*;
import java.awt.geom.*;

/**
 * <p>Simple configuration space.</p>
 *
 * <p>Each real obstacle is expanded to a CS obstacle by computing the convex
 * hull of the Minkowski sum of the real obstacle and a square circumscribed on
 * the robot bounding disc.</p>
 *
 * @author vona
 **/
public class CSpace {

  /**
   * <p>The CS obstacles.</p>
   **/
  protected LinkedList<PolygonObstacle> obstacles =
    new LinkedList<PolygonObstacle>();
 
  /**
   * <p>Compute a new CSpace.</p>
   *
   * @param realObstacles the set of real obstacles
   * @param robotRadius the robot disc radius
   **/
  public CSpace(List<PolygonObstacle> realObstacles, double robotRadius) {
      for (PolygonObstacle realObstacle : realObstacles){
      obstacles.add(makeCSObstacleFromCircle(realObstacle, robotRadius));
      }
  }

    /**
     * Compute a new CSpace
     * @param realObstacles the set of real obstacles
     * @param realRobotVerts the vertices of an arbitrarily shaped robot
     **/
    
  public CSpace(List<PolygonObstacle> realObstacles, List<Point2D.Double> realRobotVerts) {
      for (PolygonObstacle realObstacle : realObstacles){
          obstacles.add(makeCSObstacle(realObstacle, realRobotVerts));
      }
  }

    
    
  /**
   * <p>Get {@link #obstacles}.</p>
   *
   * @return a reference to <code>obstacles</code>
   **/
  public List<PolygonObstacle> getObstacles() {
    return obstacles;
  }

  /**
   * <p>Make a CS obstacle.</p>
   *
   * @param realObstacle the corresp real obstacle
   * @param robotRadius the robot bounding disc radius (m)
   **/
    protected PolygonObstacle makeCSObstacleFromCircle(PolygonObstacle realObstacle, double robotRadius) {

	List<Point2D.Double> csoPoints = new LinkedList<Point2D.Double>();
	List<Point2D.Double> roVertices = realObstacle.getVertices();

	for (Point2D.Double roVertex : roVertices) {
	    csoPoints.add(new Point2D.Double(roVertex.x + robotRadius, roVertex.y + robotRadius));
	    csoPoints.add(new Point2D.Double(roVertex.x - robotRadius, roVertex.y + robotRadius));
	    csoPoints.add(new Point2D.Double(roVertex.x - robotRadius, roVertex.y - robotRadius));
	    csoPoints.add(new Point2D.Double(roVertex.x + robotRadius, roVertex.y - robotRadius));
	}

	PolygonObstacle ret = GeomUtils.convexHull(csoPoints);
	ret.color = realObstacle.color;
	return ret;
  }

    /**
     * Make a CSObstacle from an arbitrarily shaped convex robot and convex obstacle
     * */

    protected PolygonObstacle makeCSObstacle(PolygonObstacle realObstacle, List<Point2D.Double>  realRobotVerts){
	List<Point2D.Double> realObstacleVerts = realObstacle.getVertices();
	List<Point2D.Double> difference = minkowskiDifference(realObstacleVerts, realRobotVerts);
	PolygonObstacle hull = GeomUtils.convexHull(difference);
	return hull;
    }


    public java.util.List<Point2D.Double> minkowskiSum ( java.util.List<Point2D.Double> object , java.util.List<Point2D.Double> obstacle) {
	List<Point2D.Double> sum = new LinkedList<Point2D.Double>();
	
	for (int i=0; i < object.size(); i++){
	    for (int j=0; j < obstacle.size(); j++){
		double xCoor = object.get(i).getX() + obstacle.get(j).getX();
		double yCoor = object.get(i).getY() + obstacle.get(j).getY();
		sum.add(new Point2D.Double(xCoor, yCoor));
	    }
	}
	return sum;
    }


    public java.util.List<Point2D.Double> minkowskiDifference ( java.util.List<Point2D.Double> object1 , java.util.List<Point2D.Double> object2) {
        List<Point2D.Double> difference = new LinkedList<Point2D.Double>();
        
        for (int i=0; i < object1.size(); i++){
            for (int j=0; j < object2.size(); j++){
		double xCoor = object1.get(i).getX() - object2.get(j).getX();
		double yCoor = object1.get(i).getY() - object2.get(j).getY();
		difference.add(new Point2D.Double(xCoor, yCoor));
            }
        }
	return difference;
    }
    
//    public something compressPolygons (){    
//        }
}
     
