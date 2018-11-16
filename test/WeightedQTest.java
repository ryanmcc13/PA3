

public class WeightedQTest
{
    public static WeightedQ<String> q = new WeightedQ<String>();
    public static void main(String[] args)
    {
        q.add("goodbye", 1);
        q.add("blah", 99);
        q.add("hello", 3);
        q.add("dank", 1);
        q.add("asdfasdf", 100);
        q.add("asie", 100);
        q.add("memer", 0);
        System.out.println(q);
        while(q.size()>0)
            System.out.println(q.extract());
    }
}