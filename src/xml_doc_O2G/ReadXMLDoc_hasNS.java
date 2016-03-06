package xml_doc_O2G;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReadXMLDoc_hasNS {

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(new File("xml_doc/DocEng/region-LESTE-output.xml"));

		FileWriter fw = new FileWriter("xml_doc/DocEng/region-LESTE-output2.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		String bairro = "";
		while (in.hasNextLine()) {

			String line = in.nextLine();
			bw.append(line);
			bw.newLine();

			if (line.contains("<neighborhood>")) {
				line = in.nextLine();
				bw.append(line);
				bw.newLine();

				Scanner in2 = new Scanner(line);
				in2.useDelimiter("[<>]+");
				in2.next();
				in2.next();

				bairro = in2.next();
				System.err.println(bairro);

			}

			if (line.contains("</gml:Polygon>")) {

//				bw.append("<TESTE>");
//				bw.newLine();

				boolean r = false;
				boolean append = false;
				Scanner in3 = new Scanner(new File("xml_doc/DocEng/hasNS-LESTE.xml"));
				while (in3.hasNextLine()) {

					String line3 = in3.nextLine();
					if (line3.contains("<Bairro>"+bairro+"</Bairro>")) {
						bw.append("<hasNS>");
						bw.newLine();
						bw.append("<streetCrossing>");
						bw.newLine();

						append = true;
						bw.append(line3);
						bw.newLine();
						line3 = in3.nextLine();
					}

					if (append) {
						bw.append(line3);
						bw.newLine();
					}
					
					//fim do bloco
					if (line3.contains("</hasNS>")) {
						//r = true;
						append = false;
					}
				}
			}
		}

		bw.flush();
		bw.close();
	}
}
