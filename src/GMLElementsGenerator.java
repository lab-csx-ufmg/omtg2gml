import java.io.IOException;


public class GMLElementsGenerator {
	
	private final String PATH = "plsql";
	private final String RELATIONSHIP_SCRIPT = "generate-relationship.sql";
	private final String RELATIONSHIPREF_SCRIPT = "generate-relationship-ref.sql";
	
	private String db, password, relationship_path, relationshipref_path;
	
	public GMLElementsGenerator(String db, String password) {
		this.db = db;
		this.password = password;
		this.relationship_path = PATH + "/" + RELATIONSHIP_SCRIPT;
		this.relationshipref_path = PATH + "/" + RELATIONSHIPREF_SCRIPT;
	}
	
	//sqlplus bh/bh @generate-relationship.sql bairro_trecho2 contem2 bairro_trecho geom "nome as n1, nolog as n2" fileName
	public int generateRelationship(String table, String relationship, String element, String geometry, String florest, String xmlFileName) throws IOException {
		return SQLPlusCmdLine.exec(db, password, relationship_path, table, relationship, element, geometry, florest, xmlFileName);
	}
	
	//sqlplus bh/bh @generate-relationship-ref.sql quadra_privado_leste contem "idconstrucao as ref" "idquadra as IdQuadra" fileName
	public int generateRelationshipRef(String table, String relationship, String florest1, String florest2, String xmlFileName) throws IOException {
		return SQLPlusCmdLine.exec(db, password, relationshipref_path, table, relationship, florest1, florest2, xmlFileName);
	}

}
