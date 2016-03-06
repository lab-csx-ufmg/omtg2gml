

public class OMTGNetwork {
	
	private String name, class1, class2;

	public OMTGNetwork(String name, String class1, String class2) {

		this.name = name;
		this.class1 = class1;
		this.class2 = class2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}
}