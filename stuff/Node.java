package stuff;

public class Node {

	private String data;
	private double weight;
	public Node(String data, double weight) {
		this.data=data;
		this.weight=weight;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
