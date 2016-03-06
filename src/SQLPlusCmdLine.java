import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SQLPlusCmdLine {

	public static int exec(String db, String password, String script, String table, String relationship, String element, String geometry, String florest, String xmlFileName) throws IOException {

		Process p = Runtime.getRuntime().exec("sqlplus "+db+"/"+password+" @"+script+" "+table+" "+relationship+" "+element+ " "
				+geometry+" \""+florest+"\" "+xmlFileName);
		InputStream stderr = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null)
			System.out.println(line);
		stderr.close();
		isr.close();
		br.close();
		return p.exitValue();
	}
	
	public static int exec(String db, String password, String script, String table, String relationship, String florest1, String florest2, String xmlFileName) throws IOException {

		Process p = Runtime.getRuntime().exec("sqlplus "+db+"/"+password+" @"+script+" "+table+" "+relationship+" \""+florest1+"\" \""+florest2+"\" "+xmlFileName);
		InputStream stderr = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null)
			System.out.println(line);
		stderr.close();
		isr.close();
		br.close();
		return p.exitValue();
	}

	public static void main(String[] args) throws IOException {
		
		//sqlplus bh/bh @generate-relationship.sql regiao contem regiao geom "noreg as regiao" teste
		//sqlplus bh/bh @generate-relationship.sql bairro_trecho2 contem2 bairro_trecho geom "nome as n1, nolog as n2" b_t
		SQLPlusCmdLine.exec("bh", "bh", "plsql/generate-relationship.sql", "bairro_trecho2", "contem2", "bairro_trecho", "geom", "nome as n1, nolog as n2", "teste");
		

	}
}
