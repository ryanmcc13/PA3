import java.util.ArrayList;
import java.util.Arrays;

public class PageRankTest
{
    public static void main(String[] args)
    {
        PageRank p;
        double d=0;
        if(args.length < 1)
        {
            System.out.println("you need to pass a file");
            return;
        }
        for(int i = 0; i < args.length; i++)
        {
            p = new PageRank(args[i], .01, .25);
            System.out.println(Arrays.toString(p.topKInDegree(10)));
            System.out.println(Arrays.toString(p.topKOutDegree(10)));
            System.out.println(Arrays.toString(p.topKPageRank(10)));
            for(int j = 0; j < p.numNodes(); j++)
            {
                d+=p.pageRankOf(j+1);
                System.out.println("node: " + (1+j) + " in: " + p.inDegreeOf(j+1) + " out: " + p.outDegreeOf(j+1) + " Rank: " + p.pageRankOf(j+1));
            }
            System.out.println("sum of ranks: " + d);
        }
    }
}
