package xml_doc_Related1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReadXMLDoc_neartoTA {

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(new File("xml_doc/Related1/thoroughfare-cs-o-l.xml"));

		FileWriter fw = new FileWriter("xml_doc/Related1/thoroughfare-cs-o-l-output.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		String code = "";
		while (in.hasNextLine()) {

			String line = in.nextLine();
			bw.append(line);
			bw.newLine();

			if (line.contains("<thoroughfare>")) {
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

			if (line.contains("</name>")) {

				//				bw.append("<TESTE>");
				//				bw.newLine();

				//boolean r = false;
				boolean append = false;

				Scanner in3 = new Scanner(new File("xml_doc/Related1/address.xml"));

				//				String lineAux = "";
				//				String line3 = in3.nextLine();

				while (in3.hasNextLine()) {

					String line3 = in3.nextLine();
					if (line3.contains("<Logra>"+code+"</Logra>")) {

						bw.append("<address>");
						bw.newLine();

						//						bw.append(lineAux);
						//						bw.newLine();

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
					if (line3.contains("</address>")) {
						//r = true;
						append = false;
					}

					//					lineAux = line3;
					//					line3 = in3.nextLine();
				}
			}
		}
		bw.flush();
		bw.close();
	}
}
