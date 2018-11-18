import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class MyWikiRanker
{
    private static String seedUrl = "/wiki/Tennis";
    private static String[] keywords = {"tennis", "grand slam"};
    private static int max = 400;
    private static String filename = "crawl.txt";
    private static String tempfile = "temp.txt";
    private static String directory = "pages";
    private static int topN = 20;
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
                    rmap.put(c, n2);
                    nodes[c] = new Node(c);
                    c++;
                }
                nodes[map.get(n1)].edges.add(nodes[map.get(n2)]);
            }
            scan.close();
            rankPages();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void rankPages()
    {
        int[][] top = new int[5][];
        File f;
        ArrayList<String> pages = new ArrayList<String>();
        MinHashSimilarities minHash;
        File dir = new File(directory);
        PageRank rank = new PageRank(nodes, .01, .85);
        int b;
        String route, temp1, temp2;
        URL url;
        top[0] = rank.topKOutDegree(topN);
        System.out.println("top out degree:");
        printArray(top[0]);
        System.out.println();
        top[1] = rank.topKInDegree(topN);
        System.out.println("top in degree:");
        printArray(top[1]);
        System.out.println();
        top[2] = rank.topKPageRank(topN);
        System.out.println("top rank .01 .85:");
        printArray(top[2]);
        System.out.println();
        rank = new PageRank(nodes, .005, .85);
        top[3] = rank.topKPageRank(topN);
        System.out.println("top rank .005 .85:");
        printArray(top[3]);
        System.out.println();
        rank = new PageRank(nodes, .001, .85);
        top[4] = rank.topKPageRank(topN);
        System.out.println("top rank .001 .85:");
        printArray(top[4]);
        System.out.println();
        try
        {
            if(!dir.exists())
                dir.mkdir();
            for(int i = 0; i < 5; i++)
            {
                for(int j = 0; j < topN; j++)
                {
                    route = rmap.get(top[i][j]);
                    if(!pages.contains(route))
                    {
                        pages.add(route);
                        f = new File(dir, route.replace("/", "_"));
                        f.createNewFile();
                        url = new URL(WikiCrawler.BASE_URL+route);
                        InputStream inStream = url.openStream();
                        FileOutputStream out = new FileOutputStream(f);
                        while((b = inStream.read()) != -1)
                        {
                            out.write(b);
                        }
                        inStream.close();
                        out.close();
                    }
                }
            }
            minHash = new MinHashSimilarities(directory, 1);
            for(int j = 0; j < minHash.filenames.length; j++)
            {
                for(int k = j+1; k < minHash.filenames.length; k++)
                {
                    temp1 = minHash.filenames[j].replace("_wiki_", "/wiki/");
                    temp2 = minHash.filenames[k].replace("_wiki_", "/wiki/");
                    System.out.println(temp1 + " vs " + temp2);
                    System.out.println("\t" + minHash.exactJaccard(minHash.filenames[j], minHash.filenames[k]));
                }
                f = new File(dir, minHash.filenames[j]);
                f.delete();
            }
            dir.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void printArray(int[] top)
    {
        System.out.print("{");
        for(int i = 0; i < top.length; i++)
        {
            System.out.print(" " + rmap.get(top[i]) + ", ");
        }
        System.out.println("}");
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
                    case "--max":
                        i++;
                        max = Integer.parseInt(args[i]);
                        break;
                    case "--sensitive":
                        isTopicSensitive = Boolean.parseBoolean(args[++i]);
                        break;
                    case "--tempfile":
                        tempfile = args[++i];
                        break;
                    case "--directoy":
                        directory = args[++i];
                        break;
                    case "--top":
                        topN = Integer.parseInt(args[++i]);
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
