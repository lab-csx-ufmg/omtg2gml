
public abstract class Relationship implements Cloneable {

	private String name, type;

	public Relationship(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}