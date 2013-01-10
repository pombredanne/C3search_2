package analyser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLTranslator {
	

	
	private String repUrl;
	private String filename;
	private static int SOURCEFORGE = 1;
	private static int GOOGLECODE = 2;
	private static int FTPCODE =3;
	
	private Pattern googlecodePattern = Pattern.compile("http://(.+).googlecode.com/svn/");
	private Pattern gitPattern = Pattern.compile("git://github.com/(.+)/(.+)\\.git/(.+)");
	
	public String translateGoogle(String name,String file){
		
		Matcher gcmatcher = googlecodePattern.matcher(name);
		//Matcher sfmatcher = sourceforgePattern.matcher(name);
		if(gcmatcher.matches()){
			String projectName = gcmatcher.group(1);
			return "http://code.google.com/p/"+projectName+"/source/browse/"+file;
		}
		return null;
	}
	
	public static void main(String[] args){
		URLTranslator urltr = new URLTranslator();
		//System.out.println(urltr.translate(testname, testfile));
	}

	public String translateGit(String name, String file) {
		// TODO Auto-generated method stub
		Matcher gitmatcher = gitPattern.matcher(name+file);
		//System.out.println(name+file);
		if(gitmatcher.matches()){
			String ownerName = gitmatcher.group(1);
			String projectName = gitmatcher.group(2);
			String path = gitmatcher.group(3);
			return "https://github.com/"+ownerName+"/"+projectName+"/commits/master/"+path;
		}
		return null;
	}
}
