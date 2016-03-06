package xml_doc_O2G;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReadXMLDoc_containsBB {

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(new File("xml_doc/DocEng/containsNB-CENTROSUL.xml"));

		FileWriter fw = new FileWriter("xml_doc/DocEng/containsNB-CENTROSUL-output.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		String code = "";
		while (in.hasNextLine()) {

			String line = in.nextLine();
			bw.append(line);
			bw.newLine();

			if (line.contains("<Bairro>")) {
				line = in.nextLine();
				bw.append(line);
				bw.newLine();

				Scanner in2 = new Scanner(line);
				in2.useDelimiter("[<>]+");
				in2.next();
				in2.next();

				code = in2.next();
				System.err.println(code);

			}

			if (line.contains("</gml:Polygon>")) {

				//				bw.append("<TESTE>");
				//				bw.newLine();

				//boolean r = false;
				boolean append = false;

				Scanner in3 = new Scanner(new File("xml_doc/DocEng/containsBB-CENTROSUL-PRIVADO.xml"));

				String lineAux = "";
				String line3 = in3.nextLine();

				while (in3.hasNextLine()) {

					if (line3.contains("<IdQuadra>"+code+"</IdQuadra>")) {
						bw.append(lineAux);
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

					// fim do bloco
					if (line3.contains("</containsBB>")) {
						//r = true;
						append = false;
					}

					lineAux = line3;
					line3 = in3.nextLine();
				}

				append = false;
				Scanner in4 = new Scanner(new File("xml_doc/DocEng/containsBB-CENTROSUL-PUBLICO.xml"));
				
				lineAux = "";
				line3 = in4.nextLine();

				while (in4.hasNextLine()) {

					if (line3.contains("<IdQuadra>"+code+"</IdQuadra>")) {
						bw.append(lineAux);
						bw.newLine();

						append = true;
						bw.append(line3);
						bw.newLine();
						line3 = in4.nextLine();
					}

					if (append) {
						bw.append(line3);
						bw.newLine();
					}

					// fim do bloco
					if (line3.contains("</containsBB>")) {
						//r = true;
						append = false;
					}

					lineAux = line3;
					line3 = in4.nextLine();
				}
			}
		}
		bw.flush();
		bw.close();
	}
}
