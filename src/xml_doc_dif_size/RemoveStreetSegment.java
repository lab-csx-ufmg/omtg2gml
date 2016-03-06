package xml_doc_dif_size;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RemoveStreetSegment {

	public static boolean containId(List<String> idList, String id) {

		for (int i = 0; i < idList.size(); i++) {

			if (idList.contains(id)) {
				//idList.remove(i);
				//System.out.println(idList.size());
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {

		Scanner xmlDoc = new Scanner(new File("xml_doc/Dif_size/instance-O2G-3.xml"));

		Scanner ids = new Scanner(new File("xml_doc/Dif_size/streetSegment_id_ordenado.xml"));
		List<String> idList = new ArrayList<String>();

		Map<String,String> idMap = new HashMap<String,String>();

		int cont = 1;
		Scanner s = null;
		while (ids.hasNextLine()) {

			String line = ids.nextLine();
			if (cont++ % 2 == 0) {
				s = new Scanner(line).useDelimiter("<code>|</code>");
				//idList.add(s.next());
				idMap.put(s.next(), "");
			}
//			else {
//				cont = 1;
//			}
			//cont++;
		}
		//System.out.println(idList.size());

		System.out.println("OK");

		FileWriter fw = new FileWriter("xml_doc/Dif_size/instance-O2G-4.xml");
		BufferedWriter bw = new BufferedWriter(fw);
		int x = 0;

		while (xmlDoc.hasNextLine()) {

			String line = xmlDoc.nextLine();
			System.out.println(x++);

			if (line.contains("<aggregationTS>")) {

				String line2 = xmlDoc.nextLine();
				String line3 = xmlDoc.nextLine(); // linha com o id

				Scanner sLine = new Scanner(line3);
				sLine.useDelimiter("[<>]+");
				sLine.next();
				sLine.next();
				String code = sLine.next();
				//if (containId(idList, code)) {
				if (idMap.containsKey(code)) {
					//System.out.println("-->"+code);
					String jump = xmlDoc.nextLine();
					while (xmlDoc.hasNextLine() && !jump.contains("</aggregationTS>")) {
						jump = xmlDoc.nextLine();
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