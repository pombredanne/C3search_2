package analyser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import searcher.ResultInfo;

import myUIUtils.CheckNode;

public class FileDownloader {

	private String googlecodePattern = "googlecode.com/svn/";
	private String gitPattern = "git://github.com/(.+)/(.+)\\.git/(.+)";
	private String openJDKPattern = "http://hg.openjdk.java.net";
	private String androidGitPattern = "git://android.git.kernel.org";
	private String sparsPattern = "http://demo.spars.info/r/";
	private String searchcoPattern = "http://searchco.de/";

	public String httpdownload(ResultInfo resultInfo) {
		// TODO Auto-generated method stub
		//System.out.println(resultInfo.path);
		if (ContainCompressedFile(resultInfo.toString())) {
			return "Unrecognized url.";
		} else if (resultInfo.pckg.contains(googlecodePattern)) {
			return googledownload(resultInfo.toString());

		} else if (resultInfo.toString().matches(gitPattern)) {
			return gitdownload(resultInfo.toString());
		} else if (resultInfo.path.contains(sparsPattern)) {
			
			return sparsdownload(resultInfo.path);
		} else if (resultInfo.pckg.contains(androidGitPattern)
				|| resultInfo.pckg.contains(openJDKPattern)) {
			return "Unrecognized url.";
		}

		else if (resultInfo.path.contains(searchcoPattern)) {
			
			return searchcodownload(resultInfo.path);
		}else{

			return otherdownload(resultInfo.toString());
		}

		// return "not match!\n";
	}

	private String searchcodownload(String string) {
		try {
			string = string.trim().replace(" ", "%20");
			String filecontenttemp = getHttpResponse(string);
			Scanner scanner = new Scanner(filecontenttemp);
			int count=0;
			StringBuffer buffer = new StringBuffer("");
			scanner.nextLine();
			buffer.append(scanner.nextLine().substring(5));
			while(scanner.hasNextLine()){
				String line=scanner.nextLine();
				if(line.trim().equals("</pre>\n"))continue;
				buffer.append(line);
				buffer.append("\n");
			}
			String filecontent = buffer.toString();
			if (filecontent.startsWith("<")) {
				return "failed!";
			}
			String filename = transfer(string);

			PrintWriter writer = new PrintWriter(
					new File("./cache/" + filename));
			writer.print(filecontent);
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "failed!";
		}
		return "success!";
	}

	private boolean ContainCompressedFile(String string) {
		// TODO Auto-generated method stub
		String[] comp = {"zip","tgz","gz","tbz2"};
		for(int i = 0;i<comp.length;i++){
			if(string.contains(comp[i]))return true;
		}
		return false;
	}

	private String sparsdownload(String path) {
		path = path.replaceAll("&amp;", "&");
		if (googledownload(path).equals("success!")) {
			return "success!";
		} else {
			return "Unrecognized url.";
		}
	}

	private String otherdownload(String string) {

		if (googledownload(string).equals("success!")) {
			return "success!";
		} else {
			return "Unrecognized url.";
		}
	}

	private String gitdownload(String string) {
		Pattern p = Pattern.compile(gitPattern);

		Matcher m = p.matcher(string);

		if (m.matches()) {

			String rawUrl = "https://raw.github.com/" + m.group(1) + "/"
					+ m.group(2) + "/master/" + m.group(3);
			// System.out.println(rawUrl);

			try {

				string = string.replace(" ", "%20");
				String filecontent = getHttpResponse(rawUrl);
				// System.out.println(filecontent);
				if (filecontent.startsWith("<")) {
					return "failed!";
				}
				String filename = transfer(string);

				PrintWriter writer = new PrintWriter(new File("./cache/"
						+ filename));
				writer.print(filecontent);
				writer.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "failed!";
			}
			return "success!";
		}
		return "failed!";
	}

	private String googledownload(String string) {
		// TODO Auto-generated method stub
		try {
			string = string.trim().replace(" ", "%20");
			String filecontent = getHttpResponse(string);
			if (filecontent.startsWith("<")) {
				return "failed!";
			}
			String filename = transfer(string);

			PrintWriter writer = new PrintWriter(
					new File("./cache/" + filename));
			writer.print(filecontent);
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "failed!";
		}
		return "success!";
	}

	public static void main(String[] args) {
		String testStr = "git://github.com/rhomobile/rhodes.git/platform/android/Rhodes/src/com/rhomobile/rhodes/bluetooth/RhoBluetoothSession.java";
		String testStr2 = "http://demo.spars.info/r/spars?sparshook=dl&amp;fid=1531612";
		String test = "http://searchco.de/codesearch/raw/15264254";
		FileDownloader f = new FileDownloader();
		System.out.println(f.searchcodownload(test));
	}

	private String getHttpResponse(String request) throws Exception {
		StringBuffer buffer = new StringBuffer("");
		URL url;
		HttpURLConnection conn;
		
		// url = new URL(urltr.translate(name, file));
		url = new URL(request);
		conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod("GET");
		Scanner scanner = new Scanner(conn.getInputStream());
		boolean flag = false;
		while (scanner.hasNextLine()) {

			String nextline = scanner.nextLine().trim();
			buffer.append(nextline + "\n");
		}
		scanner.close();
		conn.disconnect();
		return buffer.toString();
	}

	public String transfer(String string) {
		string = string.replace(':', '_');
		string = string.replace('/', '_');
		string = string.replace('.', '_');
		string = string.replace('?', '_');
		string = string.replace('=', '_');
		string = string.replace('#', '_');
		string = string.replaceAll(" ", "%20");
		string = string.replaceAll("&amp;", "_");
		if(string.length()>150){
			string = string.substring(0,75)+string.substring(string.length()-76,string.length()-1);
		}
		return string;
	}
}
