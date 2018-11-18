import java.util.ArrayList;

public class WeightedQ<T>
{

    Node<T> head;
    Node<T> tail;
    int size=0;

    public WeightedQ()
    {}

    //right now it replaces but keeps the same spot in the list
    public void add(T data, double weight)
    {
        Node<T> newNode = new Node<T>(data, weight);
        Node<T> cur = this.tail;

        while(cur != null)
        {
            if(cur.weight >= weight)
            {
                newNode.next = cur.next;
                if(cur.next != null)
                    cur.next.prev = newNode;
                newNode.prev = cur;
                cur.next = newNode;
                break;
            }
            cur = cur.prev;
        }

        if(cur == null)
        {
            newNode.next = this.head;
            if(this.head != null)
                this.head.prev = newNode;
            this.head = newNode;
        }

        if(cur == this.tail)
        {
            this.tail = newNode;
        }
        size++;
    }

    public int size()
    {
        return this.size;
    }

    public T extract()
    {
        if(this.head == null)
            return null;
        T ans = this.head.data;
        this.head = this.head.next;
        if(this.head != null)
            this.head.prev = null;
        else
            this.tail = null;
        this.size--;
        return ans;
    }

    public String toString()
    {
        String ans = "{";
        Node<T> cur = this.head;

        while(cur != null)
        {
            ans += cur.data + ", ";
            cur = cur.next;
        }
        return ans.substring(0, ans.length()-2)+"}";
    }

    private class Node<T>
    {
        public T data;
        public double weight;
        public Node<T> prev;
        public Node<T> next;
        public Node(T data, double weight)
        {
            this.data = data;
            this.weight = weight;
        }
    }
}
