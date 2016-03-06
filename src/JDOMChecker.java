import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.IOException;


public class JDOMChecker {

	public static void main(String[] args) {

		String xmlDocument = "output/country.xsd";
		
		SAXBuilder builder = new SAXBuilder();

		// command line should offer URIs or file names
		try {
			builder.build(xmlDocument);
			// If there are no well-formedness errors, 
			// then no exception is thrown
			System.out.println(xmlDocument + " is well-formed.");
		}
		// indicates a well-formedness error
		catch (JDOMException e) { 
			System.out.println(xmlDocument + " is not well-formed.");
			System.out.println(e.getMessage());
		}  
		catch (IOException e) { 
			System.out.println("Could not check " + xmlDocument);
			System.out.println(e.getMessage());
		}
	}
}