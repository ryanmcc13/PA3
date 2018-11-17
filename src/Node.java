import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Node
{
    int index;
    int inDegree = 0;
    ArrayList<Node> edges = new ArrayList<Node>();

    public Node(int i)
    {
        this.index = i;
    }
}