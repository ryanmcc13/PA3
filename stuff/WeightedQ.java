package stuff;

import java.util.ArrayList;

public class WeightedQ {

	ArrayList<Node> nodes;
	int highest;
	public WeightedQ() {
		nodes = new ArrayList<Node>();
	}
	
	//right now it replaces but keeps the same spot in the list
	public void add(Node tuple) {
		int index = -1;
		for(int i = 0; i<nodes.size();i++) {
			if(nodes.get(i).getData() == tuple.getData()) {
				if(nodes.get(i).getWeight() < tuple.getWeight()) {
					nodes.get(i).setWeight(tuple.getWeight());
					index = i;
					break;
				}
				return;
			}
		}
		if(index == -1) {
			index = nodes.size();
		}
		nodes.add(tuple);
		if(nodes.size()==1) {
			highest = 0;
		}else if(tuple.getWeight() > nodes.get(highest).getWeight()) {
			highest = index;
		}
	}
	
	public Node extract() {
		Node toReturn = nodes.get(highest);
		nodes.remove(highest);
		nodes.trimToSize();
		highest = 0;
		for(int i = 1; i < nodes.size(); i++) {
			if(nodes.get(i).getWeight()> nodes.get(highest).getWeight()) {
				highest = i;  
			}
		}
		return toReturn;
	}
}
