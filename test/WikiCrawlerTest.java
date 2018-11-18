import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.HashMap;

public class WikiCrawlerTest
{
    URL fileUrl;

    public static void main(String[] args) throws Exception
    {
        String[] topics = {"Japanese"};
        WikiCrawler w = new WikiCrawler("/wiki/Futaba_Channel", topics, 50, "data.txt", true);
        w.crawl();
        Scanner scan = new Scanner(new File("data.txt"));
        scan.nextInt();
        HashMap<String, Boolean> count = new HashMap<String, Boolean>();
        while(scan.hasNext())
        {
            count.put(scan.next(), true);
        }
        System.out.println(count.keySet().size());
//        Robots robot = new Robots();
//        System.out.println(robot.parseRobots());
    }
}
