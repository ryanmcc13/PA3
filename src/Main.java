import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class Main
{
	URL fileUrl;
	public Main() {
		//fileUrl = getClass().getResource("data.txt");
	}
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String[] topics = {"tennis", "grand slam"};
        WikiCrawler w = new WikiCrawler("/wiki/Tropical_Depression_Ten_(2005)", topics, 400, "src/data.txt", false);
        try {
			w.crawl();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	Robots robot = new Robots();
//    	System.out.println(robot.parseRobots());
    }
}
