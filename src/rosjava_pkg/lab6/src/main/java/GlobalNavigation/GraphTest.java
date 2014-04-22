package GlobalNavigation;

import java.util.*;

class GraphTest
{
    public static void main(String[] args)
    {
        ArrayList<Graph.Vertex> v = new ArrayList<Graph.Vertex>();

        ArrayList<Graph.Vertex.Edge> edges_1=new ArrayList<Graph.Vertex.Edge>();
        edges_1.add(new Graph.Vertex.Edge(1,2));
        edges_1.add(new Graph.Vertex.Edge(3,2));
        v.add(new Graph.Vertex(1,1,edges_1));

        ArrayList<Graph.Vertex.Edge> edges_2=new ArrayList<Graph.Vertex.Edge>();
        edges_2.add(new Graph.Vertex.Edge(2,2));
        v.add(new Graph.Vertex(2,2,edges_2));

        ArrayList<Graph.Vertex.Edge> edges_3=new ArrayList<Graph.Vertex.Edge>();
        v.add(new Graph.Vertex(3,3,edges_3));

        ArrayList<Graph.Vertex.Edge> edges_4=new ArrayList<Graph.Vertex.Edge>();
        v.add(new Graph.Vertex(3,2,edges_4));

        Graph g=new Graph(v);
    	Graph.Path p=g.search(0,2);

        System.out.println("Solution:");
        for(int i=0;i<p.size();i++)
    	{
            Graph.Vertex.Edge edge=p.get(i);
            System.out.println(edge.expandedString(g));
        }
	System.out.println("Length: "+p.length);
    }
}
