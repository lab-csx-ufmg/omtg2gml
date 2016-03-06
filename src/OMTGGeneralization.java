import java.util.ArrayList;
import java.util.List;


public class OMTGGeneralization extends Relationship {
	
	private static final String TOTAL = "total";
	private static final String PARTIAL = "partial";
	private static final String DISJOINT = "disjoint";
	private static final String OVERLAPPING = "overlapping";

	/*
	 * completeness = (t)/(p) total, partial
	 * disjointness = (d)/(o) disjoint, overlapping
	 */
	private String father, completeness, disjointness;
	private List<String> children;
	private boolean mapped;

	public OMTGGeneralization(String name, String type, String completeness, String disjointness, String father) {
		super(name, type);
		this.father = father;
//		this.completeness = mapInheritanceType(completeness);
//		this.disjointness = mapInheritanceType(disjointness);
		this.completeness = completeness;
		this.disjointness = disjointness;
		this.children = new ArrayList<String>();
		this.mapped = false;
	}
	
	public void setIsMapped() {
		this.mapped = true;
	}
	
	public boolean isMapped() {
		return mapped;
	}

	public String getFather() {
		return father;
	}

	public String getCompleteness() {
		return completeness;
	}

	public void setCompleteness(String completeness) {
		this.completeness = completeness;
	}

	public String getDisjointness() {
		return disjointness;
	}

	public List<String> getChildren() {
		return children;
	}

	public void addChildren(String child) {
		children.add(child);
	}
	
	public boolean isTotal() {
		return completeness.endsWith(TOTAL);
	}
	
	public boolean isPartial() {
		return completeness.endsWith(PARTIAL);
	}
	
	public boolean isDisjoint() {
		return disjointness.endsWith(DISJOINT);
	}
	
	public boolean isOverlapping() {
		return disjointness.endsWith(OVERLAPPING);
	}
	
	public String mapInheritanceType(String type) {
		
		if (type.equals("(t)"))
			return "total";
		if (type.equals("(p)"))
			return "partial";
		if (type.equals("(d)"))
			return "disjoint";
		return "overlapping";
	}
}
