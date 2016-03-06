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

public class RemoveBlock_ContainsBB_Building {

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

		Scanner xmlDoc = new Scanner(new File("xml_doc/Dif_size/instance-O2G-1.xml"));

		Scanner ids = new Scanner(new File("xml_doc/Dif_size/block_building_ordenado.xml"));

		Map<String,String> idBlocks = new HashMap<String,String>();
		Map<String,String> idBuildings = new HashMap<String,String>();

		int cont = 1;
		int contAux = 0;
		Scanner sLine = null;

		while (ids.hasNextLine()) {

			String line = ids.nextLine();

			if (line.contains("<code>")) {
				if (cont++ % 2 == 0) {
					sLine = new Scanner(line).useDelimiter("<code>|</code>");
					sLine.next();
					String code = sLine.next();
					idBlocks.put(code, "");
					System.out.println("code");
					System.out.println(code);
					contAux++;

					line = ids.nextLine();
					while (line.contains("containsBB")) {
						sLine = new Scanner(line);
						sLine.useDelimiter("\"");
						sLine.next();
						code = sLine.next();
						idBuildings.put(code, "");
						line = ids.nextLine();

						System.out.println("containsBB");
						System.out.println(code);
					}
				}
			}
			//			else {
			//				cont = 1;
			//			}
		}

		System.out.println(contAux);
		System.out.println("OK");

		FileWriter fw = new FileWriter("xml_doc/Dif_size/instance-O2G-2.xml");
		BufferedWriter bw = new BufferedWriter(fw);
		int x = 0;

		while (xmlDoc.hasNextLine()) {

			String line = xmlDoc.nextLine();
			//System.out.println(x++);

			if (line.contains("<building>")) {

				String line3 = xmlDoc.nextLine(); // linha com o id

				sLine = new Scanner(line3);
				sLine.useDelimiter("[<>]+");
				sLine.next();
				sLine.next();
				String code = sLine.next();
				//if (containId(idList, code)) {
				if (idBuildings.containsKey(code)) {
					//System.out.println("-->"+code);
					String jump = xmlDoc.nextLine();
					while (xmlDoc.hasNextLine() && !jump.contains("</building>")) {
						jump = xmlDoc.nextLine();
					}
				}
				else {
					bw.append(line);
					bw.newLine();
					bw.append(line3);
					bw.newLine();
				}
			}

			else if (line.contains("<containsNB>")) {

				String line2 = xmlDoc.nextLine();
				String line3 = xmlDoc.nextLine(); // linha com o id

				sLine = new Scanner(line3);
				sLine.useDelimiter("[<>]+");
				sLine.next();
				sLine.next();
				String code = sLine.next();
				//if (containId(idList, code)) {
				if (idBlocks.containsKey(code)) {
					//System.out.println("-->"+code);
					String jump = xmlDoc.nextLine();
					while (xmlDoc.hasNextLine() && !jump.contains("</containsNB>")) {
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