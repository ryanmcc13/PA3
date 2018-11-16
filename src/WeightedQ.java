import java.util.ArrayList;

public class WeightedQ<T>
{

    Node<T> head;
    Node<T> tail;
    int highest;


    public WeightedQ()
    {}

    //right now it replaces but keeps the same spot in the list
    public void add(T tuple, double weight)
    {
    	 Node new_node = new Node(tuple, weight); 
    	 new_node.next = null;
    	 if (head == null) { 
    	     head = new_node;
    	     tail = head;
    	     head.prev = null;
    	     return; 
    	 }else {
    		 tail.next =new_node;
    		 tail = new_node;
    	 }
    }

    public T extract()
    {
        if(this.head == null)
            return null;
        T ans = this.head.data;
        this.head = this.head.next;
        this.head.prev = null;
        return ans;
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
