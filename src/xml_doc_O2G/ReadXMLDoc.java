package xml_doc_O2G;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReadXMLDoc {

	public static String trataHas(String bairro) {



		return null;
	}

	public static void main(String[] args) throws IOException {

		//Scanner in = new Scanner(new File("xml_doc/DocEng/containsNB-LESTE-output.xml"));
		Scanner in = new Scanner(new File("xml_doc/DocEng/thoroughfare-cs-o-l-output.xml"));
		
		//FileWriter fw = new FileWriter("xml_doc/DocEng/containsNB-LESTE-output2.xml");
		FileWriter fw = new FileWriter("xml_doc/DocEng/thoroughfare-cs-o-l-output2.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		int cont = 0;

		while (in.hasNextLine()) {

			String line = in.nextLine();

			if (line.contains("<containsBB") || line.contains("<interceptNT") || line.contains("<neartoTA")) {
				line = line.substring(0, line.length()-1) + "/>";
			}

			bw.append(line);
			bw.newLine();
			
		}
		bw.flush();
		bw.close();
		System.out.println(cont);
	}
}
