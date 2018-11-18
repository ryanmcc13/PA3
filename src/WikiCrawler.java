import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WikiCrawler
{

    String seedUrl;
    String[] keywords;
    int max;
    File file;
    File robots;
    Boolean isTopicSensitive, test = false;
    ArrayList<String> visited;
    HashMap<String, Boolean> nodes = new HashMap<String,Boolean>();
    WeightedQ<String> q;
    String root;
    ArrayList<String> nonoList;
    Pattern paragraph = Pattern.compile("<p>(.*)");
    Pattern html = Pattern.compile("<[^/a].*?>(.*?)</[^a].*?>");
    Pattern badAnchor = Pattern.compile("<a href=\"(?!/wiki/).*?</a>");
    Pattern tokens = Pattern.compile("(?:[\\w-]+)|(?:<a.*?/a>)");
    Pattern anchor = Pattern.compile("<a href=\"(.*?)\".*?>(.*?)</a>");
    int count;
    BufferedWriter writer;
    public static final String BASE_URL="https://en.wikipedia.org";


    public WikiCrawler (String seedUrl, String[] keywords, int max, String filename, Boolean isTopicSensitive)
    {
        this.isTopicSensitive=isTopicSensitive;
        this.seedUrl=seedUrl;
        this.keywords=keywords;
        this.max=max;
        file = new File(filename);
        visited = new ArrayList<String>();
        q = new WeightedQ<String>();
        nodes.put(seedUrl, true);
        q.add(seedUrl, 0);
        this.count = 0;
    }



    public void crawl()
    {
        try
        {
            Robots robot = new Robots();
            nonoList = robot.parseRobots();
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(max--+"\n");
            URL url;
            int requestCount = 1;

            while(q.head!=null && max > 0)
            {
                root = q.extract();
                while(visited.contains(root))
                {
                    root = q.extract();
                }
                url = new URL(BASE_URL+root);
                visited.add(root);
                parsePage(url);
                if(requestCount%10 == 0) {
                    Thread.sleep(2000);
                    requestCount = 0;
                }
                requestCount++;
            }
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public void parsePage(URL url) {
        try
        {
            InputStream is = url.openStream();
            ArrayList<String> toks = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String input = "", temp;
            Matcher m, m2, m3;
            int mult;
            double weight = 0;
            boolean found = false;
            ArrayList<String> pageVisited = new ArrayList<String>();
            //reads the input
            while((temp=br.readLine()) != null)
            {
                m = paragraph.matcher(temp);
                while(m.find())
                {
                    input += m.group(1);
                }
            }
            m = html.matcher(input);
            input = m.replaceAll("$1");
            m = badAnchor.matcher(input);
            input = m.replaceAll("");
            if(test)
            {
                System.out.println(input);
                test = false;
            }
            m = tokens.matcher(input);
            while(m.find())
            {
                toks.add(m.group());
            }

            for(int i = 0; i < toks.size() && max > 0; i++)
            {
                weight = 0;
                m = anchor.matcher(toks.get(i));
                if(m.find() && !nonoList.contains(m.group(1)) && !pageVisited.contains(m.group(1)))
                {
                    if(isTopicSensitive)
                    {
                        for(int k = 0; k < keywords.length; k++)
                        {
                            if(m.group(2).toLowerCase().contains(keywords[k].toLowerCase()) || m.group(1).contains(keywords[k]))
                            {
                                weight = 1;
                                break;
                            }
                            found = false;
                            for(int j = 17; j > 0; j--)
                            {
                                m2 = tokens.matcher(keywords[k]);
                                m2.find();
                                if(i + j > 0 && i + j < toks.size() && toks.get(i+j).equalsIgnoreCase(m2.group()))
                                {
                                    found = true;
                                    mult = 0;
                                    while(m2.find() && i+j+mult < toks.size() && i+j+mult > 0)
                                    {
                                        found = toks.get(i+j+mult).equalsIgnoreCase(m2.group());
                                        mult++;
                                    }
                                    if(found)
                                        weight = 1/(j+2) > weight ? 1/(j+2):weight;
                                }
                                m2 = tokens.matcher(keywords[k]);
                                m2.find();
                                if(i - j > 0 && i - j < toks.size() && toks.get(i-j).equalsIgnoreCase(m2.group()))
                                {
                                    found = true;
                                    mult = 0;
                                    while(m2.find() && i-j+mult > 0 && i-j+mult > 0)
                                    {
                                        found = toks.get(i-j+mult).equalsIgnoreCase(m2.group());
                                        mult++;
                                    }
                                    if(found)
                                        weight = 1l/((double)j+2) > weight ? 1l/((double)j+2):weight;
                                }
                            }
                        }
                    }
                    q.add(m.group(1), weight);
                    pageVisited.add(m.group(1));
                    if(nodes.get(m.group(1))==null)
                    {
                        nodes.put(m.group(1), true);
                        max--;
                    }
                    writer.write(root + " " + m.group(1) + "\n");
                }
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
