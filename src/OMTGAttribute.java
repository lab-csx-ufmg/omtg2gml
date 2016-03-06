import java.util.List;


public class OMTGAttribute {

	private String name;
	private String type;
	private String minLen, maxLen;
	private String minSize, maxSize;
	private String defaultValue;
	private boolean isKey, isNotNull;
	private List<String> domain;

	public OMTGAttribute(String name, String type) {
		this.name = name;
		this.type = type;
		this.isKey = false;
		this.minLen = null;
		this.maxLen = null;
		this.minSize = null;
		this.maxSize = null;
		this.defaultValue = null;
		this.domain = null;
		this.isNotNull = false;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultt) {
		this.defaultValue = defaultt;
	}

	public boolean isNotNull() {
		return isNotNull;
	}

	public void setIsNotNull() {
		this.isNotNull = true;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setIsKey() {
		this.isKey = true;
	}

	public boolean isKey() {
		return isKey;
	}

	public boolean hasLen() {
		return minLen != null;
	}

	public String getMinLen() {
		return minLen;
	}

	public void setMinLen(String minLen) {
		this.minLen = minLen;
	}

	public String getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(String maxLen) {
		this.maxLen = maxLen;
	}

	public boolean hasSize() {
		return minSize != null;
	}

	public String getMinSize() {
		return minSize;
	}

	public void setMinSize(String minSize) {
		this.minSize = minSize;
	}

	public String getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}
	
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}

	public boolean hasDomain() {
		return domain != null;
	}

	public List<String> getDomain() {
		return domain;
	}

	public void setDomain(List<String> enumeration) {
		this.domain = enumeration;
	}
}
