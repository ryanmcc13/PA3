import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class MyWikiRanker
{
    private static String seedUrl = "/wiki/4chan";
    private static String[] keywords = {"memes", "4chan", "pepe", "hack", "linux", "robot", "/b/"};
    private static int max = 400;
    private static String filename = "crawl.txt";
    private static String tempfile = "temp.txt";
    private static Boolean isTopicSensitive = true;
    private static Node[] nodes;
    private static HashMap<Integer, String> rmap;

    public static void main(String[] args)
    {
        WikiCrawler crawl;
        Scanner scan;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        String n1, n2;
        int size, c = 0;

        rmap = new HashMap<Integer, String>();

        if(args.length > 0)
        {
            parseArgs(args);
        }
        try
        {
            crawl = new WikiCrawler(seedUrl, keywords, max, filename, isTopicSensitive);
            crawl.crawl();
            scan = new Scanner(new File(filename));
            size = scan.nextInt();
            nodes = new Node[size];
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
                    rmap.put(c, n1);
                    nodes[c] = new Node(c);
                    c++;
                }
                nodes[map.get(n1)].edges.add(nodes[map.get(n2)]);
            }
            scan.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void rankPages()
    {
        int[][] top = new int[5][];
        PageRank rank = new PageRank(nodes, .01, .85);
        top[0] = rank.topKOutDegree(20);
        top[1] = rank.topKInDegree(20);
        top[2] = rank.topKPageRank(20);
        rank = new PageRank(nodes, .005, .85);
        top[3] = rank.topKPageRank(20);
        rank = new PageRank(nodes, .001, .85);
        top[4] = rank.topKPageRank(20);
    }

    public static void parseArgs(String[] args)
    {
        int i = 0;
        try
        {
            for(; i < args.length; i++)
            {
                switch(args[i])
                {
                    case "--seed":
                        i++;
                        seedUrl = args[i];
                        break;
                    case "--filename":
                        i++;
                        filename = args[i];
                        break;
                    case "max":
                        i++;
                        max = Integer.parseInt(args[i]);
                        break;
                    case "--sensitive":
                        isTopicSensitive = Boolean.parseBoolean(args[++i]);
                        break;
                    case "--tempfile":
                        tempfile = args[++i];
                        break;
                    default:
                        keywords = new String[args.length - i];
                        for(int j = i; j < args.length; i++)
                        {
                            keywords[i-j] = args[i];
                        }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("invalid argument: " + args[i] + "\n" + e.getMessage());
        }
    }
}
