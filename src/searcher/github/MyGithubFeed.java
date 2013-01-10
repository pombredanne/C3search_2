package searcher.github;



import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MyGithubFeed {
	String html;
	int resultNumber;
	ArrayList<GithubEntry> resultEntries;
	private Pattern githubInfoPattern = Pattern.compile("<a href=\"(.*)\">(.*) &raquo; (.*)</a>");
	private Pattern githubNumPattern = Pattern.compile("<div class=\"title\">Code \\((.*)\\)</div>");
	String rawUrl = "https://raw.github.com";
	
	public MyGithubFeed(String url) {
		resultNumber = 0;
		resultEntries = new ArrayList<GithubEntry>();
		try {
			html = getHttpResponse(url);
			//xml = getFileContent("./googlecodetestdata.txt");
			parseGithubHtml(html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseGithubHtml(String html) {
		// TODO Auto-generated method stub
		//System.out.println(html);
		Scanner scanner = new Scanner(html);
		while(scanner.hasNext()){
			String line = scanner.nextLine();
			if(line.trim().equals("<div class=\"header\">")){
				Matcher matcher = githubNumPattern.matcher(scanner.nextLine());
				if(matcher.matches())resultNumber=Integer.parseInt(matcher.group(1));
			}
			if(line.trim().equals("<div class=\"result\">")){
				scanner.nextLine();
				String l = scanner.nextLine();
				System.out.println(l);
				Matcher matcher = githubInfoPattern.matcher(l);
				if(matcher.matches()){
					GithubEntry entry = new GithubEntry();
					entry.setProjectName(matcher.group(2));
					entry.setPath(matcher.group(3));
					entry.setUrl(rawUrl+matcher.group(1).replaceAll("/tree/\\w*/", "/master/"));
					System.out.println(entry.getUrl());
					resultEntries.add(entry);
				}
			}
		}
		System.out.println(resultNumber);
	}


	public ArrayList<GithubEntry> getGithubEntries() {
		

		return resultEntries;

	}

	public int getTotalResultsNum() {
		return resultNumber;
	}

	protected static String getHttpResponse(String request) throws Exception {
		StringBuffer buffer = new StringBuffer("");
		URL url;
		HttpURLConnection conn;

		// url = new URL(urltr.translate(name, file));
		url = new URL(request);
		conn = (HttpURLConnection) url.openConnection();
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

	public static void main(String[] args) {
		String url = "https://github.com/search?q=capsule+&repo=&langOverride=&start_value=1&type=Code&language=Java";
		MyGithubFeed mgcf = new MyGithubFeed(url);
		System.out.println(mgcf.resultNumber);
		//System.out.println("resultNum:"+mgcf.resultNumber);
//		for(GithubEntry g:mgcf.resultEntries){
//			System.out.println(g.toString());
//		}
	}
}
