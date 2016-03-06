//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//
//public class OMTGModelParser {
//
//	private String filePath;
//
//	private Map<String, OMTGClass> classes;
//	private Map<String, OMTGRelationship> relationships;
////	private Map<String, Generalization> generalizations;
//
//	public OMTGModelParser(String filePath) {
//		this.filePath = filePath;
//		this.classes = new HashMap<String, OMTGClass>();
//		this.relationships = new HashMap<String, OMTGRelationship>();
////		this.generalizations = new HashMap<String, Generalization>();
//	}
//
//	private String[] getLenSize(String lsString) {
//
//		Scanner in = new Scanner(lsString);
//		in.useDelimiter("[\\[\\]..]+");
//		in.next(); //jump LEN or SIZE
//
//		return new String[] {in.next(), in.next()};
//	}
//
//	private List<String> getEnumeration(String enumString) {
//
//		Scanner in = new Scanner(enumString);
//		in.useDelimiter("[;\\[\\]]+");
//		in.next(); //jump ENUM
//
//		List<String> enume = new ArrayList<String>();
//
//		while (in.hasNext()) {
//			enume.add(in.next());
//		}
//
//		in.close();
//		return enume;
//	}
//
//	private OMTGAttribute createAttribute(String attString) {
//
//		Scanner in = new Scanner(attString);
//
//		String type = in.next();
//		String attName = in.next();
//
//		OMTGAttribute att = new OMTGAttribute(attName, type);
//
//		while (in.hasNext()) {
//
//			String next = in.next();
//			if (next.equalsIgnoreCase("KEY")) {
//				att.setIsKey();
//			}
////			else if (next.contains("LEN")) {
////				String minLen = getLenSize(next)[0];
////				att.setMinLen(minLen);
////
////				String maxLen = getLenSize(next)[1];
////				att.setMaxLen(maxLen);
////			}
//			else if (next.contains("SIZE")) {
//				String minSize = getLenSize(next)[0];
//				att.setMinSize(minSize);
//
//				String maxSize = getLenSize(next)[1];
//				att.setMaxSize(maxSize);
//			}
//
//			else if (next.contains("ENUM")) {
//				List<String> enumeration = getEnumeration(next);
//				att.setDomain(enumeration);
//			}
//		}
//		in.close();
//		return att;
//	}
//
//	private void createEntity(String entLine) {
//
//		Scanner in = new Scanner(entLine);
//		in.useDelimiter("[\\-(,)]");
//
//		String entityName = in.next();
//		OMTGClass newClass = new OMTGClass(entityName);
//
//		String classType = in.next();
//		newClass.setType(classType);
//
//		//		System.out.println("-------------------------------------");
//		//		System.out.println("Name: " + entityName);
//		//		System.out.println("Type: " + entityType);
//
//		OMTGAttribute att = null;
//		while (in.hasNext()) {
//			att = createAttribute(in.next());
//			newClass.addAttribute(att);
//
//			//			System.out.println("---------------------");
//			//			System.out.println("NAME: " + att.getName());
//			//			System.out.println("TYPE: " + att.getType());
//			//			System.out.println("IS KEY: " + att.isKey());
//			//			System.out.println("MIN LEN: " + att.getMinLen());
//			//			System.out.println("MAX LEN: " + att.getMaxLen());
//			//			System.out.println("MIN SIZE: " + att.getMinSize());
//			//			System.out.println("MAX SIZE: " + att.getMaxSize());
//			//			System.out.println("ENUM: " + att.getEnumeration());
//
//		}
//		classes.put(entityName, newClass);
//		in.close();
//	}
//
//	private void createRelationship(String relLine) {
//
//		Scanner in = new Scanner(relLine);
//		in.useDelimiter("[#,]");
//
//		String a = in.next();
//		String b = in.next();
//
//		/*
//		 * a = (t)/(p) total, partial
//		 * b = (d)/(o) disjoint, overlapping
//		 */
//		if (a.equals("(t)") || a.equals("(p)")) {
//
//			String father = in.next();
//			OMTGGeneralization generalization = new OMTGGeneralization(father, a, b, father);
//
//			while (in.hasNext()) {
//				String child = in.next();
//				generalization.addChildren(child);
//				classes.get(child).setIsSubclass();
//			}
//
////			generalizations.put(father, generalization);
//			classes.get(father).setGeneralization(generalization);
//		}
//		else {
//
//			String relName = in.next();
//
//			OMTGCardinality cardA, cardB;
//
//			// aggregation
//			if (relName.equals("aggregation")) {
//				cardA = new OMTGCardinality("1","n");
//				cardB = new OMTGCardinality("1","1");
//			}
//			else {
//				cardA = new OMTGCardinality(in.next(),in.next());
//				cardB = new OMTGCardinality(in.next(),in.next());
//			}
//
//			OMTGRelationship r = new OMTGRelationship(relName, a, b, cardA, cardB);
//
//			OMTGRelationship rA = new OMTGRelationship(relName, a, b, cardA, cardB);
//			classes.get(a).addRelationship(rA);
//
//			OMTGRelationship rB = new OMTGRelationship(relName, b, a, cardB, cardA);
//			classes.get(b).addRelationship(rB);
//
//			relationships.put(relName, r);
//		}
//	}
//
//	public void createModel() throws FileNotFoundException {
//		Scanner in = new Scanner(new File(filePath));
//		System.out.println("File: " + filePath + "\n");
//		String line;
//
//		boolean isRelationships = false;
//
//		while (in.hasNextLine()) {
//
//			line = in.nextLine();
//
//			if (line.equals(".")) {
//				line = in.nextLine(); //jump
//				isRelationships = true;
//			}
//
//			if (!isRelationships) {
//				createEntity(line);
//			}
//			else {
//				createRelationship(line);
//			}
//		}
//		in.close();
//	}
//
//	public Map<String, OMTGClass> getClasses() {
//		return classes;
//	}
//
//	public Map<String, OMTGRelationship> getRelationships() {
//		return relationships;
//	}
//
//	public static void main(String[] args) throws FileNotFoundException {
//
//		OMTGModelParser in = new OMTGModelParser("models/urban.txt");
//		in.createModel();
//
//	}
//}
