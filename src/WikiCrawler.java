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
    String[][] words;
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

    
    
    public void crawl() throws IOException, InterruptedException
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
		int requestCount = 1;
		parsePage(url);
    	while(q.head!=null && max > 0) {
    		root = (String) q.extract();
            url = new URL(BASE_URL+root);
    		parsePage(url);
    		requestCount++;
    		if(requestCount >= 10) {
    			Thread.sleep(2000);
    			requestCount = 0;
    		}
    	}
    }
    
    
    public void parsePage(URL url) {
    	try
        { 
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String input; 
            while(!((input=br.readLine()).contains("<p>")))
            {}
            hasLink(input);
            if(max == 0) {
            	return;
            }
            if(isTopicSensitive) {
            	while((input=br.readLine())!=null)
                {
            		
                }
        	}
            while((input=br.readLine())!=null)
            {
                hasLink(input);
                if(max == 0) {
                	return;
                }
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void findP(BufferedReader br) throws IOException
    {
        String input;
        while(!((input=br.readLine()).contains("<p>")))
        {

        }
        hasLink(input);
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
                if(max == 0) {
                	return;
                }
            }
        }
    }

    public void isolateText(String s) throws IOException
    {
    	String[] text = s.split("\"");
    	String trimmed = text[0];
        if(trimmed.contains("#") || trimmed.contains(":") || trimmed.equals(root))
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
        	if(!isTopicSensitive) {
        		q.add(trimmed, 1);
        	}else {
        		q.add(trimmed, weight(trimmed, text[2]));
        	}
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.newLine();
            writer.write(root + " " + trimmed);
            writer.close();
            count++;
            System.out.println(root + " " + trimmed+ " " + count);
            max--;
        }
    }
    
    public double weight(String link, String anchor) {
    	if(keywords != null && keywords.length>0) {
    		for(int i = 0; i< keywords.length; i++) {
    			if(link.contains(keywords[i])||anchor.contains(keywords[i])) {
    				return 1;
    			}
    		}
    		//assuming global variable 2d string array called words
    		int d;
    		for(int i = 0; i < 18; i++) {
    			for(int j = 0; j< keywords.length; j++) {
    				if(words[i][0].contains(keywords[j])){
    					return (1/(i+2));
    					//2nd dimension of 2d array starts with 1 so they don't overlap
    				}else if(words[0][i + 1].contains(keywords[j])){
    					return (1/(i+2));
    				}
    			}
    		}
    	}
    	return 0;
    }

}
