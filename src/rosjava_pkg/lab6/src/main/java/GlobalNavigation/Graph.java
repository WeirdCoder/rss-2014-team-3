package GlobalNavigation;

import java.util.*;

class Graph
{
    protected ArrayList<Vertex> vertices;

    public Graph(ArrayList<Vertex> vertices)
    {
        this.vertices=vertices;
    }

    // Input: start and end (node indices)
    public Path search(int start, int end)
    {
        PriorityQueue<Path> toExplore = new PriorityQueue<Path>();
        double[] closedList = new double[vertices.size()];
        for(int i=0;i<closedList.length;i++)
        {
            closedList[i]=-1;
        }
        Path emptyPath=new Path();
        emptyPath.length=0;
        emptyPath.extra=heuristic(start,end);
        toExplore.add(emptyPath);
        // A*
        while(!toExplore.isEmpty())
        {
            Path cur=toExplore.poll();
            int lastNode;
            if(cur.isEmpty())
            {
                lastNode=start;
            }
            else
            {
                lastNode=cur.get(cur.size()-1).other;
            }
            System.err.println("Exploring "+lastNode);
            System.err.println("Length so far "+cur.length);
            System.err.println("Estimated remaining distance "+cur.extra);

            if(lastNode == end)
            {
                return cur;
            }

            if(closedList[lastNode] >= 0 && cur.length+cur.extra >= closedList[lastNode])
            {
                System.err.println("Skipping, already explored");
                continue;
            }

            closedList[lastNode] = cur.length+cur.extra;

            ArrayList<Vertex.Edge> edgesOut=vertices.get(lastNode).connections;
            for(int i=0;i<edgesOut.size();i++)
            {
                Vertex.Edge n=edgesOut.get(i);
                System.err.println("Traversing edge "+n);
                Path newPath=new Path(cur);
                newPath.add(n);
                newPath.length=cur.length+n.weight;
                newPath.extra=heuristic(n.other,end);
                toExplore.offer(newPath);
            }
        }
        return null;
    }

    // Returns the heuristic value for a given vertex
    protected double heuristic(int vertex,int end)
    {
        double x1=vertices.get(vertex).x;
        double y1=vertices.get(vertex).y;
        double x2=vertices.get(end).x;
        double y2=vertices.get(end).y;
        return Math.hypot(x2-x1,y2-y1);
    }

    static class Path extends ArrayList<Vertex.Edge> implements Comparable<Path>
    {
        public double length,extra;

        public Path()
        {
            super();
        }

        public Path(Path p)
        {
            super(p);
        }

        public int compareTo(Path other)
        {
            double value=length+extra;
            double otherValue=other.length+other.extra;
            if(value > otherValue)
            {
                return 1;
            }
            if(value < otherValue)
            {
                return -1;
            }
            return 0;
        }
    }

    static class Vertex
    {
        public double x,y;
        public ArrayList<Edge> connections;

        public Vertex(double x, double y,ArrayList<Edge> connections)
        {
            this.x=x;
            this.y=y;
            this.connections=connections;
        }

        public Vertex(double x, double y)
        {
            this.x=x;
            this.y=y;
            this.connections=new ArrayList<Edge>();
        }


        public String toString()
        {
            return "Vertex("+x+","+y+")";
        }

        static class Edge
        {
            public int other;
            public double weight;

            public Edge(int other, double weight)
            {
                this.other=other;
                this.weight=weight;
            }

            public String toString()
            {
                return "Edge(to "+other+" weight "+weight+")";
            }

            public String expandedString(Graph g)
            {
                Graph.Vertex eother=g.vertices.get(other);
                return "Edge(to "+eother+" weight "+weight+")";
            }
        }
    }
}
