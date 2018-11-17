
public class SpamFarmTest
{
    public static void main(String[] args)
    {
        try
        {
            if(args.length < 3)
            {
                System.out.println("you need to supply a source file a target and a target file");
                System.exit(-1);
            }
            int target = Integer.parseInt(args[1]);
            SpamFarm farm = new SpamFarm(args[0], target);
            farm.createSpam(args[2]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}