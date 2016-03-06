package xml_doc_O2G;

public class Teste {
	
	public static void main(String[] args) {
		
		String line = "<containsBB ref=\"9160\">";
		
		line = line.substring(0, line.length()-1) + "/>";
		
		System.out.println(line);
		
	}

}
