package GlobalNavigation;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
/**
   This class is being implemented last minute because the current grid path-finding cannot find a path through the maze
 */

public class VisibilityGraph{

	//This is the class used for the A* search for the shortest path
	//Each queue element is a path that has the distance traveled and the distance to the goal
	//They are compared by looking at total distance (distFromStart + distToGoal)
	public class QueueElement implements Comparable<QueueElement> {
		public ArrayList<Point2D.Double> path;
		public double distanceToGoal;
		public double distanceFromStart;
		public QueueElement(ArrayList<Point2D.Double> p, double distFromStart, double distToGoal){
			path = new ArrayList<Point2D.Double>();
			path = p;
			distanceToGoal = distToGoal;
			distanceFromStart = distFromStart;
		}

		@Override
		public int compareTo(QueueElement obj){
			if (this.distanceToGoal+this.distanceFromStart < obj.distanceToGoal+obj.distanceFromStart) {
				return -1;
			}
			if (this.distanceToGoal+this.distanceFromStart > obj.distanceToGoal+obj.distanceFromStart) {
				return 1;
			}
			return 0;
		}
	}

	private Map<Point2D.Double,List<Point2D.Double>> graph = new HashMap<Point2D.Double,List<Point2D.Double>>();

	public VisibilityGraph(Point2D.Double start, Point2D.Double goal, Rectangle2D.Double cworldRect, CSpace cspace){
		ArrayList<Point2D.Double> allPoints = new ArrayList<Point2D.Double>();
		allPoints.add(start);
		allPoints.add(goal);
		for(PolygonObstacle po : cspace.getObstacles()){
			for(Point2D.Double point : po.getVertices()){
				allPoints.add(point);
			}
		}


		//Initialize the arraylist
		for(Point2D.Double p : allPoints){
			graph.put(p,new ArrayList<Point2D.Double>());
		}

		//Create the Visibility Graph
		Point2D.Double beginning,end;
		for(int i = 0; i<allPoints.size()-1; i++){
			beginning = allPoints.get(i);
			for(int j=i+1;j<allPoints.size();j++){
				end = allPoints.get(j);
				if(canSee(beginning,end,cspace,cworldRect)){
					graph.get(beginning).add(end);
					graph.get(end).add(beginning);
				}
			}
		}
	}

	//Make 4 checks around the midpoint of a line.
	//If all 4 checks fall into the object, then we return true
	//If only a few checks fall in the object, then the line is probably an obstacle edge, which is fine.
	private boolean isMidpointInObstacle(Line2D.Double line, PolygonObstacle po){
		double midx = (line.getP1().getX() + line.getP2().getX()) / 2.0;
		double midy = (line.getP1().getY() + line.getP2().getY()) / 2.0;
		double delta = 0.01;//1 cm offset
		return (po.contains(midx-delta,midy-delta) &&
				po.contains(midx-delta,midy+delta) &&
				po.contains(midx+delta,midy-delta) &&
				po.contains(midx+delta,midy+delta));
	}

	private boolean canSee(Point2D.Double start, Point2D.Double end, CSpace cspace, Rectangle2D.Double cworldRect){
		Line2D.Double startToEndLine = new Line2D.Double(start,end);
		if(!cworldRect.contains(start) || !cworldRect.contains(end)){
			return false;
		}
		for(PolygonObstacle po : cspace.getObstacles()){
			if(isMidpointInObstacle(startToEndLine,po)){
				return false;
			}
			ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();
			Point2D.Double last = po.getVertices().get(po.getVertices().size()-1);
			for(Point2D.Double vertex : po.getVertices()){
				lines.add(new Line2D.Double(last,vertex));
				last = vertex;
			}
			for(Line2D.Double otherLine : lines){
				if(startToEndLine.intersectsLine(otherLine) && !startToEndLine.equals(otherLine) &&
						!otherLine.getP1().equals(start) && !otherLine.getP2().equals(start) &&
						!otherLine.getP1().equals(end) && !otherLine.getP2().equals(end)){
					return false;
				}
			}
		}
		return true;
	}

	//Needed a quick utility
	private double euclideanDistance(Point2D.Double start,Point2D.Double end){
		return Math.sqrt(Math.pow(start.getX()-end.getX(),2)+Math.pow(start.getY()-end.getY(),2));
	}

	//A*.  visited stores a set of the visited nodes because the first time we visit a node
	//represents the shortest path to that node, so it doesn't make sense to ever visit a node
	//again.
	//The queue stores a list of Queue elements. They are sorted by the A* heuristic (distTraveled+distToGoal)
	//If we pop off the queue and the path ends at the goal, that is our desired path
	//Otherwise, we extend the node at the end of the path, add it to the visited nodes, and add
	//all possible paths from that node to the queue, assuming the next node isn't already in the path or in our visited list
	//If the queue is ever empty, we have no path
	@SuppressWarnings("unchecked")
	public List<Point2D.Double> computeShortestPath(Point2D.Double start, Point2D.Double goal){
		PriorityQueue<QueueElement> queue = new PriorityQueue<QueueElement>();

		Set<Point2D.Double> visited = new HashSet<Point2D.Double>();
		ArrayList<Point2D.Double> initialPath = new ArrayList<Point2D.Double>();
		initialPath.add(start);
		queue.add(new QueueElement(initialPath,0.0,euclideanDistance(start,goal)));

		while(!queue.isEmpty()){
			QueueElement current = queue.poll();
			Point2D.Double currentPoint = current.path.get(current.path.size()-1);
			visited.add(currentPoint);
			if(currentPoint.equals(goal)){
				return current.path;
			}

			for(Point2D.Double neighbor : graph.get(currentPoint)){
				if(visited.contains(neighbor)){
					continue;
				}
				if(current.path.contains(neighbor)){
					continue;
				}
				ArrayList<Point2D.Double> newPath = (ArrayList<Point2D.Double>)current.path.clone();
				newPath.add(neighbor);
				queue.add(new QueueElement(newPath,current.distanceFromStart+euclideanDistance(currentPoint,neighbor),
						euclideanDistance(neighbor,goal)));
			}

		}
		return null;
	}

	//Make the get so we can render the graph from GlobalNavigation.java
	public Map<Point2D.Double,List<Point2D.Double>> getGraph(){
		return graph;
	}


}
