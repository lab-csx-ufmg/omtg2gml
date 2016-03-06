import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OMTGSchema {

	private Map<String, OMTGClass> classes;
	private Map<String, OMTGRelationship> relationships;
	private Map<String, List<OMTGRelationship>> entityRelationships;

	public OMTGSchema(Map<String, OMTGClass> entities, Map<String, OMTGRelationship> relationships) throws CloneNotSupportedException {
		this.classes = entities;
		this.relationships = relationships;
		this.createEntityRelationships();
	}

	public void createEntityRelationships() throws CloneNotSupportedException {

		entityRelationships = new HashMap<String,List<OMTGRelationship>>();
		List<OMTGRelationship> rels;

		Set<String> rSet = relationships.keySet();
		for (String relationshipName : rSet) {

			OMTGRelationship relationship = relationships.get(relationshipName);
			//System.out.println(relationship);

			String entityA = relationship.getEntityA();
			String entityB = relationship.getEntityB();

			// verify A side
			if (!entityRelationships.containsKey(entityA)) {
				rels = new ArrayList<OMTGRelationship>();
				OMTGRelationship r = (OMTGRelationship)relationship.clone();
				r.setSide("A");
				rels.add(r);
				//System.out.println("Rel A:" + r.getCardA() + " " + r.getSide());
				entityRelationships.put(entityA, rels);
			}
			else {
				rels = entityRelationships.get(entityA);
				OMTGRelationship r = (OMTGRelationship)relationship.clone();
				r.setSide("A");
				rels.add(r);
				//System.out.println("Rel A:" + r.getCardA() + " " + r.getSide());
				entityRelationships.put(entityA, rels);
			}

			// verify B side
			if (!entityRelationships.containsKey(entityB)) {
				rels = new ArrayList<OMTGRelationship>();
				OMTGRelationship r = (OMTGRelationship)relationship.clone();
				r.setSide("B");
				rels.add(r);
				//System.out.println("Rel B:" + r.getCardB() + " " + r.getSide());
				entityRelationships.put(entityB, rels);
			}
			else {
				rels = entityRelationships.get(entityB);
				OMTGRelationship r = (OMTGRelationship)relationship.clone();
				r.setSide("B");
				rels.add(r);
				//System.out.println("Rel A:" + r.getCardA() + " " + r.getSide());
				entityRelationships.put(entityB, rels);
			}
		}
	}

	public boolean onlyOccurPartially(String entityName) {
		List<OMTGRelationship> rels = entityRelationships.get(entityName);
		OMTGRelationship r;
		//0 - partial
		//1 - total
		int participation = 0;

		int i = 0;
		while (i++ < rels.size() && participation == 0) {
			r = rels.get(i);

			if (r.getSide().equals("A"))
				participation = r.getParticipationCardA();
			else participation = r.getParticipationCardB();

		}
		//return true if partial
		return participation == 0;
	}

	public boolean occurPartially(String entityName, OMTGCardinality card) {
		List<OMTGRelationship> rels = entityRelationships.get(entityName);
		//0 - partial
		//1 - total
		int participation = 0;
		OMTGRelationship r;
		String relation;
		boolean occurPartially = true;

		int i = 0;
		while (i++ < rels.size() && occurPartially) {

			r = rels.get(i);

			if (r.getSide().equals("A")) {
				participation = r.getParticipationCardA();
				relation = r.getCardRelationA();
			}
			else {
				participation = r.getParticipationCardB();
				relation = r.getCardRelationB();
			}

			if (r.getCard().isEqual(card)) {
				if (participation == 1 && card.getMin().equals(relation)) { //min of card eq A side
					occurPartially = false;
				}
			}
		}
		return occurPartially;
	}

	public boolean occurTotally(String entityName, String cardString) {
		List<OMTGRelationship> rels = entityRelationships.get(entityName);
		//0 - partial
		//1 - total
		int participation = 1;
		OMTGRelationship r;
		String relation;
		boolean occurTotally = true;

		OMTGCardinality card = new OMTGCardinality(cardString);

		int i = 0;
		while (i++ < rels.size() && occurTotally) {

			r = rels.get(i);

			if (r.getSide().equals("A")) {
				participation = r.getParticipationCardA();
				relation = r.getCardRelationA();
			}
			else {
				participation = r.getParticipationCardB();
				relation = r.getCardRelationB();
			}

			if (r.getCard().isEqual(card)) {
				if (participation == 1 && card.getMin().equals(relation)) { //min of card eq A side
					occurTotally = false;
				}
			}
		}
		return occurTotally;
	}

	public OMTGClass getEntity(String entityName) {
		return classes.get(entityName);
	}

	public OMTGRelationship getRelationship(String relationshipName) {
		return relationships.get(relationshipName);
	}

	public List<OMTGClass> getEntitiesAB(String relationshipName) {
		List<OMTGClass> entitiesAB = new ArrayList<OMTGClass>();

		OMTGRelationship rel = relationships.get(relationshipName);

		entitiesAB.add(classes.get(rel.getEntityA()));
		entitiesAB.add(classes.get(rel.getEntityB()));

		return entitiesAB;
	}

	public List<OMTGRelationship> getRelationships() {
		return new ArrayList<OMTGRelationship>(relationships.values());
	}

	public List<OMTGRelationship> getRelationships(String entityName) {
		return classes.get(entityName).getRelationships();
	}

	public List<String> getEntityNames() {
		return new ArrayList<String>(classes.keySet());
	}

	public Set<String> getNestedEntityNames() {

		Set<String> set = new HashSet<String>();

		List<OMTGRelationship> relationships = getRelationships();
		for (int i = 0; i < relationships.size(); i++) {
			OMTGRelationship r = relationships.get(i);
			if (r.getCardA().toString().equalsIgnoreCase(OMTGCardinality.ONE_TO_ONE) || 
					r.getCardB().toString().equalsIgnoreCase(OMTGCardinality.ONE_TO_ONE)) {
				set.add(r.getEntityA());
				set.add(r.getEntityB());
			}
		}
		return set;
	}

	public Set<String> getRelationshipNames() {
		return relationships.keySet();
	}
}