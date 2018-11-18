

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;

public class PageRank
{
    Node[] graph;

    double[] pageRank;

    S[] pageRankOrder;

    S[] degreeInOrder;

    S[] degreeOutOrder;

    int numEdges;

    int n;

    public PageRank(String graphFile, double approximation, double teleportation)
    {
        readGraph(graphFile);
        makeRank(approximation, teleportation);
    }

    public PageRank(Node[] graph, double approximation, double teleportation)
    {
        this.graph = graph;
        this.n = graph.length;
        makeRank(approximation, teleportation);
    }

    private void makeRank(double approximation, double teleportation)
    {
        double[] p1 = new double[n];
        double[] p2;
        for(int i = 0; i < n; i++)
        {
            p1[i] = 1/n;
        }
        p2 = randomWalk(p1, teleportation);
        while(norm(p1, p2) > approximation)
        {
            p1 = p2;
            p2 = randomWalk(p1, teleportation);
        }
        pageRank = p1;
    }

    private void readGraph(String graphFile)
    {
        Scanner scan;
        int i1, i2;
        Node n1, n2;
        try
        {
            scan = new Scanner(new File(graphFile));

            this.n = scan.nextInt();
            this.graph = new Node[n];
            while(scan.hasNext())
            {
                i1 = scan.nextInt()-1;
                i2 = scan.nextInt()-1;
                n1 = this.graph[i1];
                n2 = this.graph[i2];
                if(n1 == null)
                {
                    n1 = new Node(i1);
                    this.graph[i1] = n1;
                }
                if(n2 == null)
                {
                    n2 = new Node(i2);
                    this.graph[i2] = n2;
                }
                numEdges++;
                n2.inDegree++;
                n1.edges.add(n2);
            }
            scan.close();
        }
        catch(Exception e)
        {
            System.out.println("file not found");
            return;
        }
    }

    public double[] randomWalk(double[] pn1, double teleportation)
    {
        double[] pn2 = new double[pn1.length];
        Node n;
        for(int i = 0; i < pn2.length; i++)
        {
            pn2[i] = (1-teleportation)/ this.n;
        }
        for(int i = 0; i < this.graph.length; i++)
        {
            n = this.graph[i];
            if(n==null)
                this.graph[i] = (n = new Node(i));
            if(n.edges.size() == 0)
            {
                for(int j = 0; j < this.graph.length; j++)
                {
                    pn2[j] = pn2[j] + teleportation * pn1[i]/this.n;
                }
            }
            else
            {
                for (int j = 0; j < n.edges.size(); j++)
                {
                    pn2[n.edges.get(j).index] = pn2[n.edges.get(j).index] + teleportation * pn1[i] / n.edges.size();
                }
            }
        }
        return pn2;
    }

    public double norm(double[] p1, double[] p2)
    {
        double ans = 0;
        for(int i = 0; i < p1.length; i++)
        {
            ans += Math.abs(p1[i]-p2[i]);
        }
        return ans;
    }

    public double pageRankOf(int vertex)
    {
        return this.pageRank[vertex-1];
    }

    public int outDegreeOf(int vertex)
    {
        vertex--;
        if(vertex < 0 || vertex >= graph.length)
            return -1;
        if(graph[vertex] == null)
            return 0;
        return graph[vertex].edges.size();
    }

    public int inDegreeOf(int vertex)
    {
        vertex--;
        if (vertex < 0 || vertex >= graph.length)
            return -1;
        if (graph[vertex] == null)
            return 0;
        return graph[vertex].inDegree;
    }

    public int numNodes()
    {
        return n;
    }

    public int numEdges()
    {
        return numEdges;
    }

    public int[] topKPageRank(int k)
    {
        int[] ans = new int[k];
        if (this.pageRankOrder == null)
        {
            this.pageRankOrder = new S[this.n];
            for (int i = 0; i < n; i++)
            {
                this.pageRankOrder[i] = new S(i+1, this.pageRank[i]);
            }
            Arrays.sort(this.pageRankOrder, new Comp());
        }
        for (int i = 0; i < k; i++)
        {
            ans[i] = this.pageRankOrder[i].index;
        }
        return ans;
    }

    public int[] topKInDegree(int k)
    {
        int[] ans = new int[k];
        if(this.degreeInOrder == null)
        {
            this.degreeInOrder = new S[this.n];
            for (int i = 0; i < n; i++)
            {
                this.degreeInOrder[i] = new S(i+1, this.graph[i].inDegree);
            }
            Arrays.sort(this.degreeInOrder, new Comp());
        }
        for(int i = 0; i < k; i++)
        {
            ans[i] = this.degreeInOrder[i].index;
        }
        return ans;
    }

    public int[] topKOutDegree(int k)
    {
        int[] ans = new int[k];
        if (this.degreeOutOrder == null)
        {
            this.degreeOutOrder = new S[this.n];
            for (int i = 0; i < n; i++)
            {
                this.degreeOutOrder[i] = new S(i+1, this.graph[i].edges.size());
            }
            Arrays.sort(this.degreeOutOrder, new Comp());
        }
        for (int i = 0; i < k; i++)
        {
            ans[i] = this.degreeOutOrder[i].index;
        }
        return ans;
    }

    private class S
    {
        int index;
        double a;
        public S(int index, double a)
        {
            this.index = index;
            this.a = a;
        }
    }

    private class Comp implements Comparator<S>
    {
        public int compare(S a, S b)
        {
            if(b.a - a.a > 0)
                return 1;
            if(b.a - a.a < 0)
                return -1;
            return 0;
        }

        // public boolean equals(Object a)
        // {
        //     return a instanceof Comp;
        // }
    }
}
