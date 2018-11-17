

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class SpamFarm
{
    String ogFile;
    int target;

    public SpamFarm(String graphFile, int target)
    {
        this.ogFile = graphFile;
        this.target=target-1;
    }

    /**
     * reads graph from og file then adds n/10 nodes and a bunch of edges
     * writes the altered graph to a file with the route passed in
     * @param fileName  the name of the file to write the altered graph to.
     */
    public void createSpam(String fileName)
    {
        BufferedWriter writer;
        Scanner scan;
        Node[] graph;
        int n, i1, i2;
        Node n1, n2;

        try {
            scan = new Scanner(new File(ogFile));
        } catch (Exception e) {
            System.out.println("file not found");
            return;
        }
        n = scan.nextInt();
        graph = new Node[n + n / 10];
        while (scan.hasNext()) {
            i1 = scan.nextInt() - 1;
            i2 = scan.nextInt() - 1;
            n1 = graph[i1];
            n2 = graph[i2];
            if (n1 == null) {
                n1 = new Node(i1+1);
                graph[i1] = n1;
            }
            if (n2 == null) {
                n2 = new Node(i2+1);
                graph[i2] = n2;
            }
            n2.inDegree++;
            n1.edges.add(n2);
        }
        n1 = graph[target];
        for (int i = n; i < graph.length; i++) {
            graph[i] = new Node(i+1);
            graph[i].edges.add(n1);
        }
        try{
            scan.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        if(n1.edges.size() != 0)
        {
            for(int i = 0; i < graph.length; i++)
            {
                if(i != target && !n1.edges.contains(graph[i]))
                {
                    n1.edges.add(graph[i]);
                }
            }
        }
        try
        {
            writer = new BufferedWriter(new FileWriter(new File(fileName))) ;
            writer.write(graph.length + "\n");
            for(int i = 0; i < graph.length; i++)
            {
                for(int j = 0; j < graph[i].edges.size(); j++)
                {
                    writer.write((i+1) + " " + graph[i].edges.get(j).index + "\n");
                }
            }
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("failed to write to file");
            System.out.println(e.getMessage());
        }
    }
}
