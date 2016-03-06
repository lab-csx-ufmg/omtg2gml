import java.io.IOException;
import java.util.Map;


public class OMTGSchemaLoader {
	
//	private OMTGModelParser in;
	private XMLParser xmlParser;
	
	public OMTGSchemaLoader(String filePath) throws SecurityException, IOException {
//		this.in = new OMTGModelParser(filePath);
//		this.in.createModel();
		xmlParser = new XMLParser(filePath);
		xmlParser.createOMTGSchema(filePath);
	}
	
	public OMTGSchema getModel() throws CloneNotSupportedException {
//		Map<String, OMTGClass> entities = in.getClasses();
//		Map<String, OMTGRelationship> relationships = in.getRelationships();
		
		Map<String, OMTGClass> classes = xmlParser.getClasses();
		Map<String, OMTGRelationship> relationships = xmlParser.getRelationships();
		
		return new OMTGSchema(classes, relationships);
	}
}
