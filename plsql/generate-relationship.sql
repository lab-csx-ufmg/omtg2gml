--SET verify OFF;
DECLARE

	doc	   	   	    DBMS_XMLDOM.DOMDocument;
	xdata  			XMLTYPE;
	xroot  		   	XMLTYPE;
	parent_document DBMS_XMLDOM.DOMDOCUMENT;
	parent_rootnode DBMS_XMLDOM.DOMNODE;
	child_document 	DBMS_XMLDOM.DOMDOCUMENT;
	child_rootnode 	DBMS_XMLDOM.DOMNODE;
	
	-- $5 = nobaipop as "Bairro", id as "id", nuimv as "number"
	
	CURSOR xmlcur IS
		   SELECT xmlelement("&2", xmlelement("&3", xmlforest(&5), xmltype(sdo_util.to_gmlgeometry(&4))))
		   INTO xdata
		   FROM &1;

BEGIN

	 SELECT xmlelement("root") INTO xroot FROM dual;

	 parent_document := DBMS_XMLDOM.newDOMDocument(xroot);
	 parent_rootnode := DBMS_XMLDOM.makeNode(DBMS_XMLDOM.getDocumentElement(parent_document));

	 OPEN xmlcur;
	 LOOP
	 	 FETCH xmlcur INTO xdata;
	 	 EXIT WHEN xmlcur%NOTFOUND;
		 
		 child_document := dbms_xmldom.newDOMDocument(xdata);
		 child_rootnode := dbms_xmldom.makeNode(DBMS_XMLDOM.getDocumentElement(child_document));
		 
		 child_rootnode := dbms_xmldom.importNode(parent_document,child_rootnode,TRUE);
		 child_rootnode := dbms_xmldom.appendChild(parent_rootnode,child_rootnode);
		 
		 DBMS_XMLDOM.freedocument(child_document);
		 	 
	 END LOOP;
	 CLOSE xmlcur;
	 
	 doc := DBMS_XMLDOM.NewDOMDocument(xroot);
	 DBMS_XMLDOM.WRITETOFILE(doc,'XML_DIR/&6'||'.xml');
END;
/
exit;

--CREATE OR REPLACE DIRECTORY XML_DIR AS 'C:\Users\Owner\Documents\UFMG\Mestrado\Generating_GML_Doc\PL-SQL';
