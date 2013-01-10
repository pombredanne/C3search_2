package searcher.github;

import java.awt.TextArea;
import java.util.ArrayList;

import searcher.ResultInfo;
import searcher.SearchEngines;
import searcher.googlecode.GoogleCodeEntry;
import searcher.googlecode.MyGoogleCodeFeed;
import tokenizer.Token;

public class GithubSearch extends SearchEngines {
	private static final String baseUrl = "https://github.com/search?q=texture&repo=&langOverride=&start_value=2&type=Code&language=C";
	// q=query&start-index=0&max-results=100
	String[] lang = new String[] { "java", "c%2B%2B", "C" };
	private String queryStr = "";
	ArrayList<ResultInfo> results;
	private int count = 0;
	static int itemPerPage = 30;
	@Override
	public int doQuery(TextArea logText, int maxResultNum, int maxKeywordNum,
			int language, String filename, Token[] keywords, boolean cmdMode) {
		// TODO Auto-generated method stub
		results = new ArrayList<ResultInfo>();
		String querykeywords = "";
		

		int tempcount = count;
		int page = 0;
		for (int i = 1; i <= maxKeywordNum; i++) {
			filename = filename.length() > 0 ? filename + "+" : "";
			int index = (keywords.length - i - (cmdMode ? count : 0))
					% keywords.length;
			querykeywords += keywords[index].getName() + "+";
			tempcount++;
			logText.append("using keywords:\t"
					+ (filename.trim() + querykeywords.trim()).replaceAll(
							"\\+", " ") + "\n");
			queryStr = generateQuery(filename.trim() + querykeywords.trim(),language,page);
					
			//System.out.println(queryStr);
			MyGithubFeed resultFeed = new MyGithubFeed(queryStr);
			int resultNum = resultFeed.getTotalResultsNum();
			logText.append("results from github:\t" + resultNum + "\n");
			if (resultNum < maxResultNum || i == maxKeywordNum) {

				for (int j = 0; j < Math.min(resultNum, maxResultNum); j++) {
					if(j - page * itemPerPage<0)continue;
					ResultInfo resultInfo = new ResultInfo();
					
					GithubEntry se = resultFeed.getGithubEntries().get(
							j - page * itemPerPage);
					resultInfo.url = se.getPath();
					resultInfo.projectName = se.getProjectName();
					resultInfo.pckg = "";
					resultInfo.path = se.getUrl();

					resultInfo.license = "";
					resultInfo.searchEngine = "Github";
					// System.out.println("id:" + resultInfo.pckg);
					results.add(resultInfo);
					logText.append(resultInfo.toString() + "\n");
					if (j - page * itemPerPage == resultFeed.getGithubEntries().size() - 1
							&& j + 1 < Math.min(resultNum, maxResultNum)) {
						queryStr = generateQuery(filename.trim() + querykeywords.trim(),language,++page);
						resultFeed = new MyGithubFeed(queryStr);

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

	private String generateQuery(String q, int language, int page) {
		// TODO Auto-generated method stub
		return "https://github.com/search?q="+q+"&repo=&langOverride=&start_value="+page+1+"&type=Code&language="+lang[language];
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
