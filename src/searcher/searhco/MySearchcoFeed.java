package searcher.searhco;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class MySearchcoFeed {
	JSONObject jo;
	public MySearchcoFeed(String url){
		String json;
		try {
			json = getHttpResponse(url);
			jo = (JSONObject) new JSONTokener(json).nextValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ArrayList<SearchcoEntry> getSearchcoEntries(){
		ArrayList<SearchcoEntry> output = new ArrayList<SearchcoEntry>();
		JSONArray results = jo.getJSONArray("results");
		
		for(int i=0;i<results.size();i++){
			JSONObject result = results.getJSONObject(i);
			SearchcoEntry entry = new SearchcoEntry();
			entry.setLoc(result.getString("linecount"));
			entry.setUrl(result.getString("location")+"/"+result.getString("filename"));
			entry.setProject(result.getString("name"));
			entry.setFileUrl("http://searchco.de/codesearch/raw/"+result.getString("id"));
			output.add(entry);
		}
		return output;
		
	}
	public int getTotalResultsNum(){
		return Integer.parseInt(jo.getString("total"));
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
	public static void main(String[] args){
		String url = "http://searchco.de/api/codesearch_I/?q=texture%20lang:java";
		
		try {
			//String json = MySearchcoFeed.getHttpResponse(url);
			//System.out.println(json);
			MySearchcoFeed mf = new MySearchcoFeed(url);
			ArrayList<SearchcoEntry> a = mf.getSearchcoEntries();
			for(SearchcoEntry s:a){
				System.out.println(s.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
