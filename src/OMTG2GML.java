import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OMTG2GML {

	private OMTGSchema omtgModel;

	private List<String> fle;
	private List<String> mcnp;
	private List<String> classesToBeTranslated;
	private List<String> translatedClasses;
	private List<String> translatedRelationships;
	private List<String> translatedGeneralizations;

	private Document xmlDocument;
	private Element schemaElement, rootElement;
	private Namespace XMLnameSpace, GMLnameSpace;

	private int classesCounter = 0;

	public OMTG2GML(OMTGSchema omtgModel) {
		this.omtgModel = omtgModel;

		this.fle = new ArrayList<String>();
		this.mcnp = new ArrayList<String>();
		this.translatedClasses = new ArrayList<String>();
		this.classesToBeTranslated = new ArrayList<String>();
		this.translatedRelationships = new ArrayList<String>();
		this.translatedGeneralizations = new ArrayList<String>();

		this.xmlDocument = new Document();
	}

	private void loadFirstLevelEntities() {

		List<String> classNames = omtgModel.getEntityNames();
		for (String className : classNames) {
			if (isFirstLevelEntities(className)) {
				fle.add(className);
				Collections.sort(fle, Collections.reverseOrder());
			}
		}
	}

	private boolean isFirstLevelEntities(String className) {

		OMTGClass classs = omtgModel.getEntity(className);
		if (classs.isSubclass())
			return false;

		List<OMTGRelationship> relationships = omtgModel.getRelationships(className);
		int i = 0;
		boolean isFle = true;

		System.out.println("=================");
		System.out.println(className);

		while (i < relationships.size() && isFle) {
			OMTGRelationship r = relationships.get(i++);

			System.out.println(r.getName());
			System.out.println(r.getCardRelationA());
			System.out.println(r.getCardRelationB());
			System.out.println(r.getCardA());
			System.out.println(r.getCardB());

			isFle =  (r.getParticipationCardA() == OMTGCardinality.PARTIAL) || 
			(r.getParticipationCardA() == OMTGCardinality.TOTAL && r.getCardRelationB().equals("n") &&
					(r.getCardRelationA().equals("n") || r.getCardRelationA().equals("1")));
		}
		return isFle;
	}

	private void loadFirstLevelEntitiesMCNP() {

		Set<String> set = omtgModel.getNestedEntityNames();

		if (set.size() > 0) {

			MCNP mcnp = new MCNP();
			mcnp.addClasses(new ArrayList<String>(set));

			List<OMTGRelationship> relationships = omtgModel.getRelationships();
			for (int i = 0; i < relationships.size(); i++) {
				OMTGRelationship r = relationships.get(i);
				if (r.getCardA().toString().equalsIgnoreCase(OMTGCardinality.ONE_TO_ONE) && 
						r.getCardB().toString().equalsIgnoreCase(OMTGCardinality.ONE_TO_ONE)) {
					mcnp.addRelationship11(r.getEntityA(), r.getEntityB());
					//				System.out.println(r.getName());
					//				System.out.println(r.getCardA());
					//				System.out.println(r.getCardB());
					//				System.out.println("OPT 1");
				}
				else if (r.getCardA().toString().equalsIgnoreCase(OMTGCardinality.ONE_TO_ONE)) {
					mcnp.addRelationship1N(r.getEntityB(), r.getEntityA());
					//				System.out.println(r.getName());
					//				System.out.println(r.getCardA());
					//				System.out.println(r.getCardB());
					//				System.out.println("OPT 2");
				}
				else if (r.getCardB().toString().equalsIgnoreCase(OMTGCardinality.ONE_TO_ONE)) {
					mcnp.addRelationshipN1(r.getEntityA(), r.getEntityB());
					//				System.out.println(r.getName());
					//				System.out.println(r.getEntityA());
					//				System.out.println(r.getCardA());
					//				System.out.println(r.getEntityB());
					//				System.out.println(r.getCardB());
					//				System.out.println("OPT 3");
				}
			}
			this.mcnp = mcnp.getSCCMainRoots();
		}
	}

	private boolean inclusionConditions(OMTGClass classB, OMTGRelationship relationship) {

		return !translatedClasses.contains(classB.getName()) && relationship.isParticipationCardBTotal() &&
		!fle.contains(classB.getName()) && !classB.isSubclass();
	}

	private boolean inclusionConditions2(OMTGClass classB, OMTGRelationship relationship) {

		return relationship.isParticipationCardBTotal() && !fle.contains(classB.getName());
	}

	// load entities in the correct order (1st fle and 2nd others entities)
	private void loadEntitiesToBeTranslated(boolean epn1, boolean epn2) {

		if (epn1)
			loadFirstLevelEntities();
		if (epn2)
			loadFirstLevelEntitiesMCNP();

		for (int i = 0; i < fle.size(); i++) {
			classesToBeTranslated.add(fle.get(i));
		}

		for (int i = 0; i < mcnp.size(); i++) {

			String className = mcnp.get(i);
			if (!classesToBeTranslated.contains(className)) {
				classesToBeTranslated.add(className);
			}
		}

//		classesToBeTranslated.add("Regiao");
//		classesToBeTranslated.add("a7");

		for (int i = 0; i < omtgModel.getEntityNames().size(); i++) {

			String className = omtgModel.getEntityNames().get(i);
			if (!classesToBeTranslated.contains(className)) {
				classesToBeTranslated.add(className);
			}
		}
	}

	public void saveXML(String file) {

		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());

		System.out.println("--------------------------------------------------------");
		try {
			//outp.output(xmlDocument, new FileWriter(new File("output/output2.xml")));
			outp.output(xmlDocument, new FileWriter(new File(file)));
			//outp.output(xmlDocument, System.out);
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}

	public void translateOMTG2GML(boolean epn1, boolean epn2) {

		// load entities in the correct order (1st FLE, 2nd MCNPs and 3nd others entities)
		loadEntitiesToBeTranslated(epn1, epn2);

		// creating the XML root element and the spatial domain
		defineNameSpace();

		schemaElement = new Element("schema", XMLnameSpace);
		schemaElement.addNamespaceDeclaration(GMLnameSpace);
		xmlDocument.addContent(schemaElement);

		Element importElement = new Element("import", XMLnameSpace);
		importElement.setAttribute("namespace", "http://www.opengis.net/gml");
		importElement.setAttribute("schemaLocation", "http://schemas.opengis.net/gml/3.0.0/base/feature.xsd");
		schemaElement.addContent(importElement);

		rootElement = new Element("element", XMLnameSpace);
		rootElement.setAttribute("name", "root");
		schemaElement.addContent(rootElement);

		Element rootComplexType = new Element("complexType", XMLnameSpace);
		rootElement.addContent(rootComplexType);

		Element rootSequence = new Element("sequence", XMLnameSpace);
		rootComplexType.addContent(rootSequence);

		Element spatialDomaninElement = createSpatialDomain();
		rootSequence.addContent(spatialDomaninElement);

		System.out.println("--------------------------------------------------------");
		System.out.println("FLE: " + fle);
		System.out.println("MCNP: " + mcnp);
		System.out.println("Entities to be translated: " + classesToBeTranslated);
		System.out.println("Translated entities: " + translatedClasses);

		for (String className : classesToBeTranslated) {

			OMTGClass classA = omtgModel.getEntity(className);

			if (!translatedClasses.contains(className) && classA != null && !classA.isSubclass()) {
				Element classElement = mapClass(classA, classA.getRelationships(), "root");
				classElement.setAttribute("minOccurs", "1");
				classElement.setAttribute("maxOccurs", "unbounded");
				rootSequence.addContent(classElement);
			}
		}

		System.out.println("--------------------------------------------------------");
		System.out.println("FLE: " + fle);
		System.out.println("MCNP: " + mcnp);
		System.out.println("Entities to be translated: " + classesToBeTranslated);
		System.out.println("Translated entities: " + translatedClasses);

	}

	private void defineNameSpace() {
		XMLnameSpace = Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		GMLnameSpace = Namespace.getNamespace("gml", "http://www.opengis.net/gml");
	}

	private Element createSpatialDomain() {
		Element spatialDomainElement = new Element("element", XMLnameSpace);
		Element spatialDomainComplexType = new Element("complexType", XMLnameSpace);
		Element spatialDomainSequence = new Element("sequence", XMLnameSpace);
		Element boundedByElement = new Element("element", XMLnameSpace);
		Element extentOfElement = new Element("element", XMLnameSpace);

		spatialDomainElement.setAttribute("name", "spatialDomain");
		spatialDomainElement.setAttribute("minOccurs","0");
		spatialDomainElement.setAttribute("maxOccurs","1");

		boundedByElement.setAttribute("ref", "gml:boundedBy");
		boundedByElement.setAttribute("minOccurs","0");
		boundedByElement.setAttribute("maxOccurs","1");

		extentOfElement.setAttribute("ref", "gml:extentOf");
		extentOfElement.setAttribute("minOccurs","0");
		extentOfElement.setAttribute("maxOccurs","1");

		spatialDomainElement.addContent(spatialDomainComplexType);
		spatialDomainComplexType.addContent(spatialDomainSequence);
		spatialDomainSequence.addContent(boundedByElement);
		spatialDomainSequence.addContent(extentOfElement);

		return spatialDomainElement;
	}

	private Element mapClass(OMTGClass classA, List<OMTGRelationship> relationships, String xpath) {

		System.out.println("--------------------------------------------------------");
		System.out.println("Entity: " + classA.getName());
		System.out.println("Attribute KEY: key" + classA.getName());

		//translatedClasses.add(classA.getName());

		// Creating the XML elements
		Element classElement = new Element("element", XMLnameSpace);
		classElement.setAttribute("name", classA.getName());

		Element classComplexType = new Element("complexType", XMLnameSpace);
		classElement.addContent(classComplexType);

		Element classSequence = new Element("sequence", XMLnameSpace);
		classComplexType.addContent(classSequence);

		//		Element classAttribute = new Element("attribute", nameSpaceXML);
		//		classAttribute.setAttribute("name", "id");

		//		classComplexType.addContent(classAttribute);

		classSequence = mapAttributes(classA, classSequence);

		xpath = classA.getName();

		for (OMTGRelationship r : relationships) {

			if (!translatedRelationships.contains(r.getName())) {

				OMTGClass classB = omtgModel.getEntity(r.getEntityB());

				if (r.getType().equalsIgnoreCase("network")) {

					if (classB.getType().equalsIgnoreCase("node"))
						classSequence = mapNetwork(classA, classB, classSequence);
				}
				else {

					String cardinalityAB = r.getCard().toString();

					// Creating the XML elements
					Element relationshipElement = new Element("element", XMLnameSpace);
					relationshipElement.setAttribute("name", r.getName());

					Element relationshipComplexType = new Element("complexType", XMLnameSpace);
					relationshipElement.addContent(relationshipComplexType);

					Element relationshipSequence;// = new Element("sequence");

					Element relationshipAttribute = new Element("attribute", XMLnameSpace);
					//relationshipAttribute.setAttribute("name", "id");
					relationshipComplexType.addContent(relationshipAttribute);

					String relationIdValue = "";

					boolean ic = inclusionConditions(classB, r);
					boolean ic2 = inclusionConditions2(classB, r);

					if (cardinalityAB.equals(OMTGCardinality.ONE_TO_ONE)){
						if ((r.isParticipationCardAPartial() && r.isParticipationCardBTotal()) ||
								(r.isParticipationCardAPartial() && r.isParticipationCardBPartial()) ||
								(r.isParticipationCardATotal() && r.isParticipationCardBTotal() && 
										ic) /*|| true*/) {

							xpath = r.getName();
							System.out.println("Relationship: " + r.getName() + r.getCardA());

							relationshipElement.setAttribute("minOccurs", r.getCardA().getMin());
							relationshipElement.setAttribute("maxOccurs", r.getCardA().getMax().equals("n")?"unbounded":r.getCardA().getMax());

							classSequence.addContent(relationshipElement);

							if (r.getCardB().getMin().equals("0") && r.getCardB().getMax().equals("1")) {
								System.out.println("Attribute KEY (in relationship): key" + r.getName());
								relationIdValue = r.getName();

								createKeyRestriction("key", relationIdValue, xpath, "@ref");
							}
							else {
								System.out.println("Attribute: key" + r.getName());
								relationIdValue = r.getName();
							}
							if (!(r.getCardB().getMin().equals("1") && r.getCardB().getMax().equals("1")) || 
									translatedClasses.contains(classB.getName()) || classB.isSubclass()) {
								System.out.println("KEYREF: key" + r.getEntityB());
								relationIdValue = r.getEntityB();
								xpath = r.getName();
								relationshipAttribute.setAttribute("name", "ref");
								createKeyRestriction("keyref", relationIdValue, xpath, "@ref");
							}
							translatedRelationships.add(r.getName());
						}
					}

					else if (cardinalityAB.equals(OMTGCardinality.ONE_TO_MANY) || cardinalityAB.equals(OMTGCardinality.MANY_TO_ONE)){
						if ((r.getCardRelationA().equals("1") && ic2) || 
								(r.getCardRelationA().equals("n") && r.isParticipationCardAPartial() && r.isParticipationCardBPartial()) ||
								(r.getCardRelationA().equals("1") && r.isParticipationCardATotal())) {

							xpath = r.getName();
							System.out.println("Relationship: " + r.getName() + r.getCardA());

							relationshipElement.setAttribute("minOccurs", r.getCardA().getMin());
							relationshipElement.setAttribute("maxOccurs", r.getCardA().getMax().equals("n")?"unbounded":r.getCardA().getMax());

							classSequence.addContent(relationshipElement);

							if (r.getCardB().getMin().equals("0") && r.getCardB().getMax().equals("1")) {
								System.out.println("Attribute KEY (in relationship): key" + r.getName());
								relationIdValue = r.getName();
								//relationshipAttribute.setAttribute("name", "id");
								createKeyRestriction("key", relationIdValue, xpath, "@ref");
								//relationShipElement.setAttribute("id", "key" + r.getName());
							}
							else {
								System.out.println("Attribute: key" + r.getName());
								relationIdValue = r.getName();
								//relationshipAttribute.setAttribute("name", "id");
								//relationShipElement.setAttribute("id", "key" + r.getName());
							}
							if (!(r.getCardB().getMin().equals("1") && r.getCardB().getMax().equals("1")) || 
									translatedClasses.contains(classB.getName()) || classB.isSubclass()) {
								System.out.println("KEYREF: key" + r.getEntityB());
								relationIdValue = r.getEntityB();
								xpath = r.getName();
								relationshipAttribute.setAttribute("name", "ref");
								createKeyRestriction("keyref", relationIdValue, xpath, "@ref");
								//relationShipElement.setAttribute("id", "keyref" + r.getEntityB());
							} else {
								//							relationshipElement.setAttribute("minOccurs", r.getCardA().getMin());
								//							relationshipElement.setAttribute("maxOccurs", r.getCardA().getMax());
							}

							translatedRelationships.add(r.getName());
							//relationshipElement.setAttribute("id", relationIdValue);
							//classElement.addContent(relationShipElement);
						}
					}
					//MANY_TO_MANY
					else {
						if ((r.isParticipationCardAPartial() && r.isParticipationCardBPartial() ||
								r.isParticipationCardATotal() && r.isParticipationCardBPartial() ||
								r.isParticipationCardATotal() && r.isParticipationCardBTotal())) {

							System.out.println("Relationship: " + r.getName() + r.getCardA());
							System.out.println("KEYREF: key" + r.getEntityB());

							relationshipElement.setAttribute("minOccurs", r.getCardA().getMin());
							relationshipElement.setAttribute("maxOccurs", r.getCardA().getMax().equals("n")?"unbounded":r.getCardA().getMax());

							classSequence.addContent(relationshipElement);

							relationIdValue = r.getEntityB();
							//relationShipElement.setAttribute("id", "key" + r.getEntityB());
							translatedRelationships.add(r.getName());
							relationshipAttribute.setAttribute("name", "ref");
							//xpath += "/"+r.getName();
							xpath = r.getName();
							createKeyRestriction("keyref", relationIdValue, xpath, "@ref");
							//relationshipElement.setAttribute("id", relationIdValue);
						} else {
							//						relationshipElement.setAttribute("minOccurs", r.getCardA().getMin());
							//						relationshipElement.setAttribute("maxOccurs", r.getCardA().getMax());
						}
					}

					//Element x;
					if ((cardinalityAB.equals(OMTGCardinality.ONE_TO_ONE) || cardinalityAB.equals(OMTGCardinality.ONE_TO_MANY)) && ic) {

						System.out.println(" -- SON --");
						List<OMTGRelationship> relationshipsB = classB.getRelationships();

						relationshipSequence = new Element("sequence", XMLnameSpace);

						translatedClasses.add(classA.getName());
						Element newElement = mapClass(classB, relationshipsB, xpath);

						relationshipComplexType.removeChildren("attribute", XMLnameSpace);
						relationshipComplexType.addContent(relationshipSequence);
						relationshipSequence.addContent(newElement);

						System.out.println("-- END SON --");
					}
				}
			}
		}

		if (classA.hasGeneralization() && !classA.isGeneralizationMapped()) {

			OMTGGeneralization g = classA.getGeneralization();
			List<String> children = g.getChildren();

			Element generalizationElement = mapGeneralization(g);

			for (String child : children) {

				OMTGClass classChild = omtgModel.getEntity(child);
				Element newChildElement = mapClass(classChild, classChild.getRelationships(), xpath);

				if (g.isPartial() && g.isOverlapping()) {
					newChildElement.setAttribute("minOccurs", "0");
					newChildElement.setAttribute("maxOccurs", "1");
				}
				generalizationElement.addContent(newChildElement);
			}
			classSequence.addContent(generalizationElement);
			translatedGeneralizations.add(g.getName());

			classA.setIsGeneralizationMapped();
		}

		translatedClasses.add(classA.getName());
		return classElement;
	}

	private void createKeyRestriction(String constraint, String referenceName, String xpath, String field) {

		Element keyElement = new Element(constraint, XMLnameSpace);

		if (constraint.equalsIgnoreCase("keyref")) {

			keyElement.setAttribute("name", constraint + referenceName + ++classesCounter);
			keyElement.setAttribute("refer", "key"+referenceName);
		}
		else {
			keyElement.setAttribute("name", constraint + referenceName);
		}

		Element selectorElement = new Element("selector", XMLnameSpace);
		selectorElement.setAttribute("xpath", ".//"+xpath);
		//selectorElement.setAttribute("xpath", xpath);
		keyElement.addContent(selectorElement);

		Element fieldElement = new Element("field", XMLnameSpace);
		fieldElement.setAttribute("xpath", field);
		keyElement.addContent(fieldElement);

		rootElement.addContent(keyElement);
	}

	//	private String getXPath(Element e) {
	//		return getXPath(e, "");
	//	}
	//
	//	private String getXPath(Element e, String xpath) {
	//
	//		Element parent = e.getParentElement();
	//		//System.out.println("NAME: " + e.getName());
	//
	//		if (parent != null) {
	//			if (parent.getName().equals("sequence") || parent.getName().equals("complexType")) {
	//				e = parent.getParentElement();
	//			}
	//		}
	//		else {
	//			return e.getAttributeValue("name");
	//		}
	//
	//		xpath += getXPath(e.getParentElement(), xpath);
	//		return "/" + xpath + e.getAttributeValue("name", XMLnameSpace);
	//	}

	private String mapXMLType(String type) {

		if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
			return "xs:integer"; 
		}
		if (type.equalsIgnoreCase("dec") || type.equalsIgnoreCase("decimal")) {
			return "xs:decimal"; 
		}
		if (type.equalsIgnoreCase("date")) {
			return "xs:date"; 
		}
		return "xs:string"; 

	}

	private Element mapAttributes(OMTGClass c, Element currentElement) {

		List<OMTGAttribute> attList = c.getAttributes();

		for (int i = 0; i < attList.size(); i++) {

			OMTGAttribute a = attList.get(i);
			Element element = createElementAttribute(a);
			currentElement.addContent(element);

			if (a.isKey()) {
				createKeyRestriction("key", c.getName(), c.getName(), a.getName());
			}
		}

		if (!c.getType().equalsIgnoreCase("conventional")) {

			if (!c.hasGeneralization() || (c.hasGeneralization() && c.getGeneralization().isPartial())) {

				Element feature = createSpatialType(c.getType());
				currentElement.addContent(feature);
			}
		}

		return currentElement;
	}

	private Element createSpatialType(String type) {

		return mapSpatialType(type);
	}

	private Element mapSpatialType(String type) {

		Element e = null;

		if (type.equalsIgnoreCase("point") || type.equalsIgnoreCase("node") || type.equalsIgnoreCase("sample")) {
			e = new Element("element", XMLnameSpace);
			e.setAttribute("ref", "gml:Point");
			return e;
		}
		if (type.equalsIgnoreCase("line") || type.equalsIgnoreCase("un-line") || type.equalsIgnoreCase("bi-line")) {
			e = new Element("element", XMLnameSpace);
			e.setAttribute("ref", "gml:LineString");
			return e;
		}
		if (type.equalsIgnoreCase("polygon") || type.equalsIgnoreCase("planar-subdivision")) {
			e = new Element("element", XMLnameSpace);
			e.setAttribute("ref", "gml:Polygon");
			return e;
		}
		if (type.equalsIgnoreCase("isolines")) {
			//LineString e/ou Polygon
			e = new Element("choice", XMLnameSpace);
			Element lineString = new Element("element", XMLnameSpace);
			Element polygon = new Element("element", XMLnameSpace);

			e.setAttribute("minOccurs", "1");
			e.setAttribute("maxOccurs", "2");
			lineString.setAttribute("ref", "gml:LineString");
			polygon.setAttribute("ref", "gml:Polygon");

			e.addContent(lineString);
			e.addContent(polygon);
			return e;
		}

		if (type.equalsIgnoreCase("TIN")) {
			// TIN - Point (triangle vertices) and Polygon (triangles)
			e = new Element("sequence", XMLnameSpace);
			Element point = new Element("element", XMLnameSpace);
			Element polygon = new Element("element", XMLnameSpace);

			point.setAttribute("ref", "gml:Point");
			polygon.setAttribute("ref", "gml:Polygon");

			e.addContent(point);
			e.addContent(polygon);
			return e;
		}

		// TIN - Point (triangle vertices) and Polygon (triangles)
		//		e = new Element("sequence", XMLnameSpace);
		//		Element point = new Element("element", XMLnameSpace);
		//		Element polygon = new Element("element", XMLnameSpace);
		//
		//		point.setAttribute("ref", "gml:Point");
		//		polygon.setAttribute("ref", "gml:Polygon");
		//
		//		e.addContent(point);
		//		e.addContent(polygon);
		return e;
		// Tesselation is not implemented yet
	}

	private Element createElementAttribute(OMTGAttribute a) {

		String name = a.getName();
		String type = mapXMLType(a.getType());

		Element element = createElementAttribute(name, type);

		if (a.hasSize()) {

			String minSize = a.getMinSize();
			String maxSize = a.getMaxSize();
			element = createSizeRestriction(element, minSize, maxSize);
		}

		if (a.hasDefaultValue()) {

			String defaultValue = a.getDefaultValue();
			element = createDefaultRestriction(element, defaultValue);
		}

		//		if (a.hasLen()) {
		//			String min = a.getMinLen();
		//			String max = a.getMaxLen();
		//			if (type.equals("xs:string"))
		//				element = createLengthRestriction(element, min, max);
		//			else element = createValueRestriction(element, min, max);
		//		}

		if (a.hasDomain()) {

			List<String> enumeration = a.getDomain();
			element = createEnumerationRestriction(element, type, enumeration);
		}
		return element;
	}

	private Element mapNetwork(OMTGClass classA, OMTGClass classB, Element classSequence) {

		Element elementN1 = new Element("element", XMLnameSpace);
		elementN1.setAttribute("name", "n1");
		elementN1.setAttribute("type", "xs:string");

		Element elementN2 = new Element("element", XMLnameSpace);
		elementN2.setAttribute("name", "n2");
		elementN2.setAttribute("type", "xs:string");

		classSequence.addContent(elementN1);
		classSequence.addContent(elementN2);

		createKeyRestriction("keyref", classB.getName(), classA.getName(), "n1");
		createKeyRestriction("keyref", classB.getName(), classA.getName(), "n2");

		return classSequence;
	}

	private Element createElementAttribute(String elementName, String elementType) {

		Element element = new Element("element", XMLnameSpace);
		element.setAttribute("name", elementName);
		element.setAttribute("type", elementType);

		return element;
	}

	private Element createDefaultRestriction(Element element, String defaultt) {

		element.setAttribute("default", defaultt);

		return element;
	}

	private Element createSizeRestriction(Element element, String minSize, String maxSize) {

		element.setAttribute("minOccurs", minSize);
		if (maxSize.equals("*") || maxSize.equalsIgnoreCase("n"))
			maxSize = "unbounded";
		element.setAttribute("maxOccurs", maxSize);

		return element;
	}

	private Element createEnumerationRestriction(Element element, String elementType, List<String> enumerationList) {

		//Element element = new Element("element", nameSpaceXML);
		//element.setAttribute("name", elementName);

		Element simpleType = new Element("simpleType", XMLnameSpace);
		element.addContent(simpleType);

		Element restriction = new Element("restriction", XMLnameSpace);
		restriction.setAttribute("base", elementType);
		simpleType.addContent(restriction);

		Element enumeration;

		for (int i = 0; i < enumerationList.size(); i++) {

			enumeration = new Element("enumeration", XMLnameSpace);
			enumeration.setAttribute("value", enumerationList.get(i));
			restriction.addContent(enumeration);
		}

		element.removeAttribute("type");
		return element;
	}

	// Restrictions on Length for String
	private Element createLengthRestriction(Element element, String minLen, String maxLen) {

		//		Element element = new Element("element", nameSpaceXML);
		//		element.setAttribute("name", elementName);

		Element simpleType = new Element("simpleType", XMLnameSpace);
		element.addContent(simpleType);

		Element restriction = new Element("restriction", XMLnameSpace);
		restriction.setAttribute("base", "xs:string");
		simpleType.addContent(restriction);

		Element minLength = new Element("minLength", XMLnameSpace);
		minLength.setAttribute("value", minLen);
		restriction.addContent(minLength);

		Element maxLength = new Element("maxLength", XMLnameSpace);
		maxLength.setAttribute("value", maxLen);
		restriction.addContent(maxLength);

		element.removeAttribute("type");
		return element; 
	}

	// Restrictions on Values for Integer
	private Element createValueRestriction(Element element, String minIn, String maxIn) {

		//		Element element = new Element("element", nameSpaceXML);
		//		element.setAttribute("name", elementName);

		Element simpleType = new Element("simpleType", XMLnameSpace);
		element.addContent(simpleType);

		Element restriction = new Element("restriction", XMLnameSpace);
		restriction.setAttribute("base", "xs:integer");
		simpleType.addContent(restriction);

		Element minInclusive = new Element("minInclusive", XMLnameSpace);
		minInclusive.setAttribute("value", minIn);
		restriction.addContent(minInclusive);

		Element maxInclusive = new Element("maxInclusive", XMLnameSpace);
		maxInclusive.setAttribute("value", maxIn);
		restriction.addContent(maxInclusive);

		element.removeAttribute("type");
		return element;
	}

	public Element mapGeneralization(OMTGGeneralization g) {

		Element e;

		if (g.isTotal()) {

			e = new Element("choice", XMLnameSpace);
			if (g.isOverlapping()) {
				e.setAttribute("minOccurs", "1");
				e.setAttribute("maxOccurs", Integer.toString(g.getChildren().size()));
			}
			return e;
		}
		else if (g.isPartial() && g.isDisjoint()) {
			e = new Element("choice", XMLnameSpace);
			e.setAttribute("minOccurs", "0");
			e.setAttribute("maxOccurs", "1");
			return e;
		}
		// else partial and overlapping
		return new Element("sequence", XMLnameSpace);
	}

	//	public Element createInheritance(Generalization inheritance) {
	//
	//		return null;
	//	}

	public static void main(String[] args) throws SecurityException, IOException {

		//LoadOmtgModel loadModel = new LoadOmtgModel("models/simpleRelationship/1n-1n.txt");
		//LoadOmtgModel loadModel = new LoadOmtgModel("models/urban3.txt");
		//LoadOmtgModel loadModel = new LoadOmtgModel("models/eer1.txt");
		//LoadOmtgModel loadModel = new LoadOmtgModel("models/generalization/isoline.txt");
		//LoadOmtgModel loadModel = new LoadOmtgModel("models/urban4.txt");
		//LoadOmtgModel loadModel = new LoadOmtgModel("models/relationProblem2.txt");
		//OMTGModelLoader omtgLoader = new OMTGModelLoader("models/country.txt");

		// case-study-1-pt fle = Logradouro e Ponto
		// case-study-2-pt fle = Ponto e Logradouro

		String xmlDoc = "omtg-schemas/case-study-2-pt.xml";
		String output = "omtg-schemas/case-study-2-pt.xsd";

		OMTGSchemaLoader omtgLoader = new OMTGSchemaLoader(xmlDoc);
		OMTGSchema omtgModel;
		try {
			omtgModel = omtgLoader.getModel();
			OMTG2GML o2g = new OMTG2GML(omtgModel);
			
			o2g.translateOMTG2GML(true,true);
			o2g.saveXML(output);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
}