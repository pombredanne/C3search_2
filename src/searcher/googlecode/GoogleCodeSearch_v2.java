package searcher.googlecode;

import java.awt.TextArea;
import java.util.ArrayList;

import searcher.ResultInfo;
import searcher.SearchEngines;
import tokenizer.Token;

public class GoogleCodeSearch_v2 extends SearchEngines {
	private static final String baseUrl = "http://code.google.com/codesearch#search/";
	// q=query&start-index=0&max-results=100
	String[] lang = new String[] { "java", "c%2B%2B", "c" };
	private String queryStr = "";
	ArrayList<ResultInfo> results;
	private int count = 0;
	static int itemPerPage = 10;

	@Override
	public int doQuery(TextArea logText, int maxResultNum, int maxKeywordNum,
			int language, String filename, Token[] keywords, boolean cmdMode) {
		// TODO Auto-generated method stub
		results = new ArrayList<ResultInfo>();
		String querykeywords = "";
		

		int tempcount = count;
		int page = 0;
		for (int i = 1; i <= maxKeywordNum; i++) {
			filename = filename.length() > 0 ? filename + "%20" : "";
			int index = (keywords.length - i - (cmdMode ? count : 0))
					% keywords.length;
			querykeywords += keywords[index].getName() + "%20";
			tempcount++;
			logText.append("using keywords:\t"
					+ (filename.trim() + querykeywords.trim()).replaceAll(
							"%20", " ") + "\n");
			queryStr = baseUrl + "&q=" + filename.trim() + querykeywords.trim()
					+ "lang:%5E" + lang[language] + "&p=" + page;
			System.out.println(queryStr);
			MyGoogleCodeFeed resultFeed = new MyGoogleCodeFeed(queryStr);
			int resultNum = resultFeed.getTotalResultsNum();
			logText.append("results from google code:\t" + resultNum + "\n");
			if (resultNum < maxResultNum || i == maxKeywordNum) {

				for (int j = 0; j < Math.min(resultNum, maxResultNum); j++) {
					ResultInfo resultInfo = new ResultInfo();
					GoogleCodeEntry se = resultFeed.getGoogleCodeEntries().get(
							j - page * itemPerPage);
					resultInfo.url = se.getUrl();
					resultInfo.projectName = se.getProject();
					resultInfo.pckg = se.getProject()+"/";
					resultInfo.path = se.getFileUrl();

					resultInfo.license = se.getLicense();
					resultInfo.searchEngine = "Google Code";
					// System.out.println("id:" + resultInfo.pckg);
					results.add(resultInfo);
					logText.append(resultInfo.toString() + "\n");
					if (j - page * itemPerPage == resultFeed.getGoogleCodeEntries().size() - 1
							&& j + 1 < Math.min(resultNum, maxResultNum)) {
						queryStr = baseUrl + "&q=" + filename.trim() + querykeywords.trim()
						+ "lang:%5E" + lang[language] + "&p=" + ++page;
						resultFeed = new MyGoogleCodeFeed(queryStr);

					}
				}

				break;
			} else {

				continue;
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
