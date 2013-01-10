package analyser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import searcher.ResultInfo;

public class TimeExtractor {
	//static String testname = "http://footballmanagerdesia.googlecode.com/svn";
	//static String testfile = "trunk/JME2_0_1/src/com/jme/image/Texture.java";
	//private Pattern infoPattern = Pattern.compile("<div id=\"changelog\">.*(>r\\d+</a>\n)by (\\w+)\non (\\w+ \\d+, \\d+)\n");
	private Pattern googleInfoPattern = Pattern.compile(".*>(r\\d+)</a>.*by (.+)on (\\w+ \\d+, \\d+).*");
	private Pattern gitInfoPattern = Pattern.compile(".*<time .*>(\\w+ \\d+, \\d+).*");
	URLTranslator urltr = new URLTranslator();
	URL url ;
	Matcher m;
	private String googlecodePattern = "googlecode.com/svn/";
	private String gitPattern = "git://github.com/(.+)/(.+)\\.git/(.+)";
	//
	public String ExtractTime(ResultInfo resultInfo){
		if (resultInfo.pckg.contains(googlecodePattern)) {
			return getLastModifiedTimeOfGoogle(resultInfo.pckg,resultInfo.path);

		}else if(resultInfo.toString().matches(gitPattern)){
			return getLastModifiedTimeOfGit(resultInfo.pckg,resultInfo.path);
		}
		return "NotSure"; 
	}
	private String getLastModifiedTimeOfGit(String name, String file) {
		   StringBuffer buffer = new StringBuffer("");
			String[] temp = file.split("/");
			
			 HttpURLConnection conn;
			try {
				String timeurl = urltr.translateGit(name, file);
				//System.out.println(timeurl);
				if(timeurl!=null){
				url = new URL(timeurl);
				}else{
					return "Not Sure";
				}
			
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET"); 
				
				//Thread.sleep(500);
			    Scanner scanner = new Scanner(conn.getInputStream());
			    boolean flag=false;
			    while ( scanner.hasNextLine() )
			    {
			    	
			    	String nextline = scanner.nextLine().trim();
			    	
			    	if(nextline.contains(temp[temp.length-1])){
			    		flag=true;
			    		continue;
			    	}
			    	if(flag){
			    		if(nextline.contains("</time>")){
			    			flag=false;
			    			buffer.append(nextline);
				    		//System.out.println(nextline);
				    		break;
			    		}
			    	
			    		//buffer.append("\n");
			    	}
			    	
			    }
			    scanner.close();
			    conn.disconnect();
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return "SearchFailed";
			} 
			
			Matcher matcher = gitInfoPattern.matcher(buffer.toString());
			if(matcher.matches()){
				//infoElement.setReversion(matcher.group(1));
				//infoElement.setAuthor(matcher.group(2));
				//infoElement.setTime(matcher.group(3));
				//System.out.println(matcher.group(1));
				return matcher.group(1);
			}else{
			
				return "NotSure";
			}
	}
	public String getLastModifiedTimeOfGoogle(String name,String file){
		 
		  
		   StringBuffer buffer = new StringBuffer("");
			
			 HttpURLConnection conn;
			try {
				String timeurl = urltr.translateGoogle(name, file);
				if(timeurl!=null){
				url = new URL(timeurl);
				}else{
					return "Not Sure";
				}
			
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");  
			    Scanner scanner = new Scanner(conn.getInputStream());
			    boolean flag=false;
			    while ( scanner.hasNextLine() )
			    {
			    	
			    	String nextline = scanner.nextLine().trim();
			    	
			    	if(nextline.contains("<p>Change log</p>")){
			    		flag=true;
			    	}
			    	if(flag){
			    		if(nextline.contains("</div>")){
			    			flag=false;
			    		}
			    		buffer.append(nextline);
			    		//buffer.append("\n");
			    	}
			    	//System.out.println(nextline);
			    }
			    scanner.close();
			    conn.disconnect();
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return "SearchFailed";
			}  
			Matcher matcher = googleInfoPattern.matcher(buffer.toString());
			if(matcher.matches()){
				//infoElement.setReversion(matcher.group(1));
				//infoElement.setAuthor(matcher.group(2));
				//infoElement.setTime(matcher.group(3));
				return matcher.group(3);
			}else{
			
				return "NotSure";
			}
		  
	}

	
	public static void main(String args[]){
		String testStr = "git://github.com/rhomobile/rhodes.git";
		String testStr2 = "/platform/android/Rhodes/src/com/rhomobile/rhodes/bluetooth/RhoBluetoothSession.java";
		TimeExtractor tog = new TimeExtractor();
		String s = tog.getLastModifiedTimeOfGit(testStr, testStr2);
		System.out.println("start...");
			//String content = test;
		//	String time = tog.getLastModifiedTimeOfGoogle(testname,testfile);
			//System.out.println(content);
			//InfoElement info = tog.getInfo(content);
			
			/*if(info!=null){
				System.out.println(tog.getInfo(content).getTime());
			}else{
				System.out.println("not match."); 
			}*/
			System.out.println(s); 
			System.out.println("end"); 
		
	}
	
}
