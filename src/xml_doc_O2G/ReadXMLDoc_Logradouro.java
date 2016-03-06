package xml_doc_O2G;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReadXMLDoc_Logradouro {

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(new File("xml_doc/DocEng/thoroughfare-cs-o-l-output2.xml"));

		FileWriter fw = new FileWriter("xml_doc/DocEng/thoroughfare-cs-o-l-output3.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		String logradouro = "";
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

				logradouro = in2.next();
				System.err.println(logradouro);

			}

			if (line.contains("</name>")) {

//				bw.append("<TESTE>");
//				bw.newLine();

				boolean r = false;
				boolean append = false;
				Scanner in3 = new Scanner(new File("xml_doc/DocEng/aggregationTS.xml"));
				while (in3.hasNextLine()) {

					String line3 = in3.nextLine();
					if (line3.contains("<Logra>"+logradouro+"</Logra>")) {
						
						bw.append("<aggregationTS>");
						bw.newLine();
						bw.append("<streetSegment>");
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

					if (line3.contains("/aggregationTS")) {
						//r = true;
						append = false;
					}
				}

//				r = false;
//				append = false;
//				Scanner in4 = new Scanner(new File("xml_doc/aggregationTS-OESTE.xml"));
//				while (in4.hasNextLine()) {
//
//					String line3 = in4.nextLine();
//					if (line3.contains("<Logra>"+logradouro+"</Logra>")) {
//						
//						bw.append("<aggregationTS>");
//						bw.newLine();
//						bw.append("<streetSegment>");
//						bw.newLine();
//						
//						append = true;
//						bw.append(line3);
//						bw.newLine();
//						line3 = in4.nextLine();
//					}
//
//					if (append) {
//						bw.append(line3);
//						bw.newLine();
//					}
//
//					if (line3.contains("/aggregationTS")) {
//						//r = true;
//						append = false;
//					}
//				}
				
			}
		}

		bw.flush();
		bw.close();
	}
}
