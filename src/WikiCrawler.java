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

public class WikiCrawler
{

    String seedUrl;
    String[] keywords;
    int max;
    File file;
    File robots;
    Boolean isTopicSensitive;
    ArrayList<String> visited;
    WeightedQ q;
    String root;
    ArrayList<String> nonoList;
    int count;
    public static final String BASE_URL="https://en.wikipedia.org";
    public WikiCrawler (String seedUrl, String[] keywords, int max, String filename, Boolean isTopicSensitive)
    {
        this.isTopicSensitive=isTopicSensitive;
        this.seedUrl=seedUrl;
        this.keywords=keywords;
        this.max=max;
        file = new File(filename);
        visited = new ArrayList<String>();
        visited.add(seedUrl);
        q = new WeightedQ();
        q.add(seedUrl, 1);
        this.count =0;
    }

    
    
    public void crawl() throws IOException
    {
    	Robots robot = new Robots();
    	nonoList = robot.parseRobots();
    	//testing
//    	nonoList = new ArrayList<String>();
//    	nonoList.add("/wiki/Tennis_court");
    	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    	writer.write(String.valueOf(max));
    	writer.close();
    	System.out.println(max);
		root = seedUrl;
		URL url = new URL(BASE_URL+seedUrl);
		parsePage(url);
    	while(q.head!=null && max > 0) {
    		root = (String) q.extract();
            url = new URL(BASE_URL+root);
    		parsePage(url);
    		max--;
            
    	}
    }
    
    
    public void parsePage(URL url) {
    	try
        { 
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String input = findP(br);
            //some wikipedia pages apparently don't have this need to identify a new end point
            while(!input.contains("id=\"References\""))
            {
                hasLink(input);
                input=br.readLine();
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String findP(BufferedReader br) throws IOException
    {
        String input=br.readLine();
        while(!input.contains("<p>"))
        {
            input=br.readLine();
        }
        return input;
    }

    public void hasLink(String line) throws IOException
    {
        if(line.contains("<a href="))
        {
            String[] arr = line.split("<a href=\"");
            for(int i=1;i<arr.length;i++)
            {
                if(arr[i].contains("/wiki/"))
                {
                    isolateText(arr[i]);
                }
            }
        }
    }

    public void isolateText(String s) throws IOException
    {
        String trimmed = s.split("\"")[0];
        if(trimmed.contains("#") || trimmed.contains(":"))
        {
            return;
        }
        for(int i = 0; i <nonoList.size(); i++) {
        	if(trimmed.contains(nonoList.get(i))) {
        		return;
        	}
        }
        if(trimmed.contains("/wiki/")) {
        	visited.add(trimmed);
            q.add(trimmed, 0);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.newLine();
            writer.write(root + " " + trimmed);
            writer.close();
            count++;
            System.out.println(root + " " + trimmed+ " " + count);
        }
    }

}
