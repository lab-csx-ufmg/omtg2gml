package xml_doc_O2G;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadXMLDocRemoveBlocks {

	public static boolean containId(List<String> idList, String id) {

		for (int i = 0; i < idList.size(); i++) {

			if (idList.contains(id)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(new File("xml_doc/completeInstance-output.xml"));

		Scanner ids = new Scanner(new File("xml_doc/ids.txt"));
		List<String> idList = new ArrayList<String>();
		while (ids.hasNextLine()) {
			String id = ids.nextLine();
			idList.add(id);
		}

		FileWriter fw = new FileWriter("xml_doc/completeInstance-output2.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		while (in.hasNextLine()) {

			String line = in.nextLine();

			if (line.contains("<containsNB>")) {

				String line2 = in.nextLine();
				String line3 = in.nextLine();

				Scanner in2 = new Scanner(line3);
				in2.useDelimiter("[<>]+");
				in2.next();
				in2.next();

				String code = in2.next();
				if (containId(idList, code)) {
					System.out.println(code);
					String jump = in.nextLine();
					while (in.hasNextLine() && !jump.contains("</containsNB>")) {
						jump = in.nextLine();
						//System.out.println(jump);
					}
				}
				else {
					bw.append(line);
					bw.newLine();
					bw.append(line2);
					bw.newLine();
					bw.append(line3);
					bw.newLine();
				}
			} else {
				bw.append(line);
				bw.newLine();
			}

		}
		bw.flush();
		bw.close();
	}
}