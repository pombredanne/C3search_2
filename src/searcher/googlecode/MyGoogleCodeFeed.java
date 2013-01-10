package searcher.googlecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import searcher.searhco.MySearchcoFeed;
import searcher.searhco.SearchcoEntry;

public class MyGoogleCodeFeed {
	String xml;
	int resultNumber;
	ArrayList<GoogleCodeEntry> resultEntries;
	public MyGoogleCodeFeed(String url) {
		resultNumber = 0;
		resultEntries = new ArrayList<GoogleCodeEntry>();
		try {
			xml = getHttpResponse(url);
			//xml = getFileContent("./googlecodetestdata.txt");
			parseGoogleCodeXml(xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getFileContent(String string) {
		File f = new File(string);
		StringBuffer sb = new StringBuffer("");
		try {
			Scanner s = new Scanner(f);
			while(s.hasNext())sb.append(s.nextLine()+"\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private void parseGoogleCodeXml(String xml) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(xml);
		while(scanner.hasNext()){
			String line = scanner.nextLine();
			if(line.trim().equals("Results")){
				for(int i=0;i<9;i++)scanner.nextLine();
				String temp[]=scanner.nextLine().trim().split(",");
				StringBuffer buffer = new StringBuffer("");
				for(int i=0;i<temp.length;i++)buffer.append(temp[i]);
				resultNumber=Integer.parseInt(buffer.toString());
			}
			if(line.trim().equals("<div class=\"GKBKXLXH3\">")){
				
				parsePart(scanner);
			}
		}
		
	}

	private void parsePart(Scanner scanner) {
		// TODO Auto-generated method stub
		scanner.nextLine();
		String path = scanner.nextLine().trim();
		String project="";
		String license="";
		while(scanner.hasNext()){
			String line = scanner.nextLine();
			if(line.trim().equals("<div class=\"GKBKXLXC4\">")){
				scanner.nextLine();
				project = scanner.nextLine().trim();
				scanner.nextLine();
				scanner.nextLine();
				scanner.nextLine();
				scanner.nextLine();
				scanner.nextLine();
				license = scanner.nextLine().trim();
				break;
			}
			
		}
		GoogleCodeEntry entry = new GoogleCodeEntry();
		entry.setProject(project);
		entry.setFileUrl(path);
		entry.setLicense(license);
		entry.setUrl(project+"/"+path);
		entry.setLoc("");
		resultEntries.add(entry);
	}

	public ArrayList<GoogleCodeEntry> getGoogleCodeEntries() {
		

		return resultEntries;

	}

	public int getTotalResultsNum() {
		return resultNumber;
	}

	protected static String getHttpResponse(String request) throws Exception {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		HtmlPage page;
		//System.out.println("start-----------------");
		page = webClient.getPage(request);
		// assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());
		 webClient.waitForBackgroundJavaScript(5000);
		//webClient.getJavaScriptEngine().pumpEventLoop(5000);
		final String pageAsXml = page.asXml();
		// assertTrue(pageAsXml.contains("<body class=\"composite\">"));

		
		webClient.closeAllWindows();
		return pageAsXml;
	}

	public static void main(String[] args) {
		String url = "http://code.google.com/codesearch#search/&q=malloc.c&p=2";
		MyGoogleCodeFeed mgcf = new MyGoogleCodeFeed(url);
		//System.out.println(mgcf.xml);
		//System.out.println("resultNum:"+mgcf.resultNumber);
		for(GoogleCodeEntry g:mgcf.resultEntries){
			System.out.println(g.toString());
		}
	}
}
