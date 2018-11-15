import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WikiCrawler
{

    String seedUrl;
    String[] keywords;
    int max;
    String filename;
    Boolean isTopicSensitive;
    public static final String BASE_URL="https://en.wikipedia.org";
    public WikiCrawler (String seedUrl, String[] keywords, int max, String filename, Boolean isTopicSensitive)
    {
        this.isTopicSensitive=isTopicSensitive;
        this.seedUrl=seedUrl;
        this.keywords=keywords;
        this.filename=filename;

    }
    /**
     *
     *
     */
    public void crawl()
    {
        URL url;
        try
        {
            url = new URL(BASE_URL+seedUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String input = findP(br);
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

    public void hasLink(String line)
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

    public void isolateText(String s)
    {
        String trimmed = s.split("\"")[0];
        if(trimmed.contains("#") || trimmed.contains(":"))
        {
            return;
        }
        System.out.println(trimmed);
    }

}
