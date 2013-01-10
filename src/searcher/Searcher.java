package searcher;

import java.awt.TextArea;
import java.util.ArrayList;
import java.util.HashMap;

import searcher.github.GithubSearch;
import searcher.googlecode.GoogleCodeSearch_v2;
import searcher.searhco.SearchcoSearch;
import searcher.spars.SparsSearch;
import tokenizer.Token;

public class Searcher {
	private HashMap<String,ArrayList<ResultInfo>> resultsMap = new HashMap<String,ArrayList<ResultInfo>>();
	private SearchEngines[] searchEngine = new SearchEngines[3];
	public Searcher(){
		searchEngine[0]= new GoogleCodeSearch_v2();
		searchEngine[1]= new SearchcoSearch();
		searchEngine[2]= new GithubSearch();
		//searchEngine[3]= new GithubSearch(); 
	}
	public void query(TextArea logText, int maxResultNum,int maxKeywordNum, int language,
			boolean[] selectedSearchEngines, String filename,Token[] keywords,boolean cmdMode) {
		// TODO Auto-generated method stub
		for(int i=0;i<searchEngine.length;i++){
			if(selectedSearchEngines[i]){
				int resultNum = searchEngine[i].doQuery(logText,maxResultNum,maxKeywordNum,language,filename,keywords,cmdMode);
				if(resultNum>0){
					String name = searchEngine[i].getClass().getSimpleName();
					//logText.append("Results count from "+name+": ");
					String queryStr = searchEngine[i].getQueryStr();
					if(!resultsMap.containsKey(queryStr)){
						resultsMap.put(queryStr, searchEngine[i].getResultList());
					}
				}		
				logText.append(resultNum>0?resultNum+"\n":"No proper results.\n");
				
			}
		}
		
	}
	public HashMap<String, ArrayList<ResultInfo>> getResultsMap() {
		return resultsMap;
	}

}
