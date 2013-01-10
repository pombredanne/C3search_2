package searcher.spars;

import java.awt.TextArea;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gdata.client.codesearch.CodeSearchService;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.codesearch.CodeSearchEntry;
import com.google.gdata.data.codesearch.CodeSearchFeed;

import searcher.ResultInfo;
import searcher.SearchEngines;
import tokenizer.Token;

public class SparsSearch extends SearchEngines {
	private static final String baseUrl = "http://demo.spars.info/r/spars?sparshook=search&";
	// q=query&start-index=0&max-results=100
	private String queryStr = "";
	ArrayList<ResultInfo> results;
	private int count=0;
	@Override
	public ArrayList<ResultInfo> getResultList() {
		return results;
	}

	@Override
	public int doQuery(TextArea logText,int maxResultNum, int maxKeywordNum, int language,
			String filename, Token[] keywords,boolean cmdMode) {
		// TODO Auto-generated method stub
		results = new ArrayList<ResultInfo>();
		if(language!=0){
			return 0;
		}
		String querykeywords = "";
		MySparsFeed msf;
		ArrayList<SparsSearchEntry> entries = new ArrayList<SparsSearchEntry>();
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
					+ "&start-index=0&max-results=100";
			// System.out.println(queryStr);

			String filecontent;
			try {
				filecontent = getHttpResponse(queryStr);

				msf = new MySparsFeed(filecontent);
				if(msf.getSparsEntries().size()>0){
					entries = msf.getSparsEntries();
				}

				System.out.println(queryStr + "\nnum:" + entries.size());
				logText.append("results from spars api:\t"+entries.size()+"\n");
				if (entries.size() < maxResultNum || i == maxKeywordNum) {

					for (int j = 0; j < Math.min(entries.size(), maxResultNum); j++) {
						ResultInfo resultInfo = new ResultInfo();
						SparsSearchEntry se = entries.get(j);
						resultInfo.path = se.getQualifiedName();
						resultInfo.url = se.getFileUrl();
						resultInfo.pckg ="";
						resultInfo.lastModifyTime = se.getLastModifiedTime();
						resultInfo.searchEngine = "Spars";
						// System.out.println("id:" + resultInfo.pckg);
						results.add(resultInfo);
						logText.append(resultInfo.toString()+"\n");
					}
					break;
				} else {

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
	public String getQueryStr() {
		// TODO Auto-generated method stub
		return queryStr;
	}

	

}
