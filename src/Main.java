public class Main
{
    public static void main(String[] args)
    {
        String[] topics = {"tennis", "grand slam"};
        WikiCrawler w = new WikiCrawler("/wiki/Tennis", topics, 100, "WikiTennisGraph.txt", true);
        w.crawl();
    }
}
