package searcher;

import java.awt.TextArea;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import tokenizer.Token;

public abstract class SearchEngines {
	public String queryStr;
	public abstract int doQuery(TextArea logText, int maxResultNum,int maxkeyWordNum, int language, String filename,Token[] keywords, boolean cmdMode);
	public abstract ArrayList<ResultInfo> getResultList();
	public abstract String getQueryStr();
	protected String getHttpResponse(String request) throws Exception {
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
}
