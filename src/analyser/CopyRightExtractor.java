package analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import tokenizer.Tokenizer;

public class CopyRightExtractor {
	public String extractCopyRigh(String filename) {
		StringBuffer sb = new StringBuffer("");
		try {
			Scanner scanner = new Scanner(new File("./cache/"+filename));
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine() + "\n");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String comment = (new Tokenizer()).splitCodeAndComment(sb.toString())[1];
		boolean flag = false;// copyright begin
		boolean flag2 = false;// \n
		String result = "";
		Scanner scanner1 = new Scanner(comment);
		String temp = "";
		String tempResult="";
		while (scanner1.hasNextLine()) {
			String line = scanner1.nextLine();
			Scanner scanner2 = new Scanner(line);
			while (scanner2.hasNext()) {
				temp = scanner2.next();
				if (temp.contains("Copyright")) {
					tempResult=line;
					flag = true;
					
				}
				if (!flag) {
					continue;
				} else {
					if(temp.equalsIgnoreCase("*"))continue;
					if (temp.equalsIgnoreCase("reserved.")) {
						result += " "+temp;
						scanner2.close();
						scanner1.close();
						return result.trim();
					}
					result += " "+temp;

				}
				
			}
			scanner2.close();
			if(tempResult.length()==0)tempResult = result;
			
		}
		scanner1.close();
		return tempResult;

	}
/*	public static void main(String[] args){
		
		CopyRightExtractor c =new CopyRightExtractor();
		System.out.println(c.extractCopyRigh("http___aether-rpg_googlecode_com_svn_trunk_Aether%20RPG_src_com_aether_present_game_InGameWorldWindow_java"));
	}*/
}
