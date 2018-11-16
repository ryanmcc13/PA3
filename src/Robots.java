import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Robots {

	ArrayList<String> list;
	public static final String ROBOT_URL="https://en.wikipedia.org/robots.txt";
	public Robots() {
		this.list = new ArrayList<String>();
	}
	
	public ArrayList<String> parseRobots() throws IOException {
		URL url = new URL(ROBOT_URL);
		InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        findStart(br);
    	String line;
    	while((line=br.readLine())!=null)
        {
    		hasRule(line);
        }
		br.close();
    	return list;
    }
    
    public void findStart(BufferedReader br) throws IOException {
    	String input=br.readLine();
        while(!input.contains("User-agent: *"))
        {
            input=br.readLine();
        }
    }
    
    public void hasRule(String line) throws IOException
    {
    	if(line.contains("Disallow:"))
        {
    		String trimmed = line.split(": ")[1];
    		list.add(trimmed);
        }
    }
    
}
    
