package searcher.googlecode;

import java.awt.TextArea;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import searcher.ResultInfo;
import searcher.SearchEngines;
import tokenizer.Token;

import com.google.gdata.client.codesearch.CodeSearchService;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.codesearch.CodeSearchEntry;
import com.google.gdata.data.codesearch.CodeSearchFeed;
import com.google.common.collect.Maps;

public class googleCodeSearch extends SearchEngines {
	String[] lang = new String[] { "java", "c%2B%2B", "c" };
	private static final String baseUrl = "https://www.google.com/codesearch/feeds/search?";
	private String queryStr="";
	ArrayList<ResultInfo> results ;
	private int count=0;

	@Override
	public int doQuery(TextArea logText,int maxResultNum, int maxKeywordNum, int language,
			String filename, Token[] keywords,boolean cmdMode) {
		// TODO Auto-generated method stub
		results = new ArrayList<ResultInfo>();
		String querykeywords = "";
		CodeSearchService myService = new CodeSearchService("c3search");
		
		URL feedUrl;
		int tempcount=count;
		for (int i = 1; i <= maxKeywordNum; i++) {
			//System.out.println(i);
			
			filename = filename.length() > 0 ? filename + "%20" : "";
			int index = (keywords.length - i -(cmdMode?count:0))%keywords.length;
			querykeywords += keywords[index].getName()
					+ "%20";
			tempcount++;
			logText.append("using keywords:\t"+(filename.trim()+querykeywords.trim()).replaceAll("%20", " ")+"\n");
			queryStr = baseUrl + "q=" + filename.trim() + querykeywords.trim()
					+ "lang:^" + lang[language] + "$" + "&max-results=100";

			// System.out.println(queryStr);

			
			try {
				feedUrl = new URL(queryStr);

				CodeSearchFeed resultFeed = myService.getFeed(feedUrl,
						CodeSearchFeed.class);

				List<CodeSearchEntry> entries = resultFeed.getEntries();
				System.out.println(queryStr+"\nnum:"+entries.size());
				logText.append("results from googlesearch api:\t"+entries.size()+"\n");
				if (entries.size() < maxResultNum || i==maxKeywordNum) {
					
					for (int j = 0; j < Math.min(entries.size(),maxResultNum); j++) {
						ResultInfo resultInfo = new ResultInfo();
						CodeSearchEntry ce = entries.get(j);
						resultInfo.url = ce.getId();
						resultInfo.projectName = ce.getPackage().getName();
						resultInfo.pckg = ce.getPackage().getUri()+"/";
						resultInfo.path = ce.getFile().getName();
						TextConstruct right = ce.getRights();
						resultInfo.license = (right==null)?resultInfo.license:right.getPlainText();
						resultInfo.searchEngine = "GoogleCodeSearch";
						//System.out.println("id:" + resultInfo.pckg);
						results.add(resultInfo);
						logText.append(resultInfo.toString()+"\n");
					}
					break;
				}else{
					
					continue;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Send the request and receive the response:
		}
		count = tempcount;
		
		return results.size();
			
	}

	@Override
	public ArrayList<ResultInfo> getResultList() {
		// TODO Auto-generated method stub
		return results;
	}

	@Override
	public String getQueryStr() {
		// TODO Auto-generated method stub
		return queryStr;
	}

}
