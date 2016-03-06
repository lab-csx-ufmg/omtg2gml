import java.util.ArrayList;
import java.util.List;


public class OMTGClass {

	private String name;

	private List<OMTGRelationship> relationships;
	private List<OMTGAttribute> attributes;
	private OMTGGeneralization generalization;
	private boolean isSubclass;
	private String type;

	public OMTGClass(String name, String type, List<OMTGAttribute> attributes) {
		this.name = name;
		this.type = type;
		this.attributes = attributes;
		this.relationships = new ArrayList<OMTGRelationship>();
		this.isSubclass = false;
	}

	public String getName() {
		return name;
	}

	public List<OMTGAttribute> getAttributes() {
		return attributes;
	}

	public void addRelationship(OMTGRelationship r) {
		relationships.add(r);
	}

	public List<OMTGRelationship> getRelationships() {
		return relationships;
	}

	public void addAttribute(OMTGAttribute a) {
		attributes.add(a);
	}

	public String toString() {
		return name;
	}

	public OMTGGeneralization getGeneralization() {
		return generalization;
	}

	public boolean hasGeneralization() {
		return generalization != null;
	}

	public void setGeneralization(OMTGGeneralization i) {
		generalization = i;
	}

	public boolean isSubclass() {
		return isSubclass;
	}

	public void setIsSubclass() {
		this.isSubclass = true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isGeneralizationMapped() {
		return generalization.isMapped();
	}
	
	public void setIsGeneralizationMapped() {
		generalization.setIsMapped();
	}
}
