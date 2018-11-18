import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class Converter
{
    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            System.out.println("must pass and input and output file");
            System.exit(-1);
        }
        convert(args[0], args[1]);
    }

    public static void convert(String in, String out)
    {
        try
        {
            File f = new File(in);
            Scanner scan = new Scanner(f);
            int size = scan.nextInt();
            HashMap<String, Integer> map = new HashMap<String,Integer>();
            HashMap<Integer,String> rmap = new HashMap<Integer,String>();
            int c = 0;
            String n1, n2;
            Node[] nodes = new Node[size];
            while(scan.hasNext())
            {
                n1 = scan.next();
                n2 = scan.next();
                if(map.get(n1) == null)
                {
                    map.put(n1, c);
                    rmap.put(c, n1);
                    nodes[c] = new Node(c);
                    c++;
                }
                if(map.get(n2) == null)
                {
                    map.put(n2, c);
                    rmap.put(c, n2);
                    nodes[c] = new Node(c);
                    c++;
                }
                nodes[map.get(n1)].edges.add(nodes[map.get(n2)]);
            }
            scan.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(out)));
            writer.write(nodes.length + "\n");
            for(int i = 0; i < nodes.length; i++)
            {
                for(int j = 0; j < nodes[i].edges.size(); j++)
                {
                    writer.write((nodes[i].index+1) + " " + (nodes[i].edges.get(j).index + 1) + "\n");
                }
            }
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
