import java.awt.TextArea;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import myUIUtils.CheckNode;

import analyser.Analyser;


import searcher.ResultInfo;
import searcher.Searcher;
import tokenizer.Token;
import tokenizer.Tokenizer;

public class OpenCCFinder {
	private String queryContent;
	private String compareContent;
	private Token[] keywords;

	private Tokenizer tokenizer;
	private Searcher searcher;
	private Analyser analyser;
	private TextArea logText;
	private ArrayList<ResultInfo> finalResults;
	private Object[] resultInfo;

	public OpenCCFinder() {
		tokenizer = new Tokenizer();
		searcher = new Searcher();
		analyser = new Analyser();
		
	}

	public String getQueryContent() {
		return queryContent;
	}

	public void setQueryContent(String queryContent) {
		this.queryContent = queryContent;
		setCompareContent(queryContent);
	}

	public String getCompareContent() {
		return compareContent;
	}

	public void setLogText(TextArea logText) {
		this.logText = logText;
	}

	public void setCompareContent(String compareContent) {
		this.compareContent = compareContent;
	}

	public void tokenize(ConfigDialog config) {

		keywords = tokenizer.tokennize(queryContent, config.getSortType(),
				config.getTargetType(), config.getMinSize(),
				config.getConfigStr());
		logText.append(getAllKeywordsStrs());
	}

	public void shuffleTokens() {
		List<Token> tokenList = (List<Token>) Arrays.asList(keywords);
		Collections.shuffle(tokenList);
		int i = 0;
		for (Token token : tokenList) {
			keywords[i++] = token;
		}
	}

	public String[][] getKeywordsStr() {
		String[][] s = new String[keywords.length][2];
		for (int i = 0; i < keywords.length; i++) {
			s[keywords.length - i - 1] = keywords[i].getStrings();
		}
		return s;
	}

	public String getKeywordsRank(int num) {

		return keywords[keywords.length - num - 1].getName();
	}

	public void doQuery(ConfigDialog config,String filename,boolean cmdMode) {
		// TODO Auto-generated method stub

		boolean selectedSearchEngines[] = new boolean[] {
				config.isGoogleSelected(), config.isKodersSelected(),
				config.isSparsSelected() };
		if(keywords==null||keywords.length==0){
			logText.append("Cannot search without keywords.\n");
			return ;
		}
		searcher.query(logText,config.getMaxResultNum(), config.getMaxKeywordNum(),
				config.getLanguage(), selectedSearchEngines,
				filename, keywords,cmdMode);

		// }
	}
	public HashMap<String, ArrayList<ResultInfo>> getResultsMap() {
		return searcher.getResultsMap();
	}

	public void analyse(String querycode,CheckNode root,ConfigDialog config) {
		// TODO Auto-generated method stub
		finalResults = new ArrayList<ResultInfo> ();
		analyser.setRequestText(queryContent);
		analyser.setUrlTreeRoot(root);
		
		logText.append("downloading...\n");
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		analyser.downloadFiles(logText,finalResults);
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		if(finalResults.size()<=1){
			logText.append("finish!\nNo proper results.");
			return;
		}
		logText.append("finish!\nRunning CCFinder...");
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		analyser.runCCFinder(config.LANGUAGE[config.getLanguage()],10);
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		logText.append("finish!\nAnalyzing CCFinder output file...");
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		analyser.analyseCCFinderResult(logText,finalResults);
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		logText.append("finish!\nChecking copyright and last modified time...");
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		analyser.checkCopyrightAndLastModifiedTime(finalResults,logText);
		logText.append(DateFormat.getTimeInstance().format(new Date())+"\n");
		logText.append("finish!\n");
		resultInfo = finalResults.toArray();
		Arrays.sort(resultInfo);
		
		/*for(int i=ri.length-1;i>=0;i--){
			System.out.println(((ResultInfo)ri[i]).infoStr());
		}*/
	}

	public Object[][] getResultTableStr() {
		// TODO Auto-generated method stub
		if(resultInfo==null){
			return new String[0][9];
		}
		String[][] resultInfoStr = new String[resultInfo.length-1][9];
		//int count = 1;
		for(int i=1;i<resultInfo.length;i++){
			int j = resultInfo.length-i;
			resultInfoStr[i-1][0]=String.valueOf(i);
			resultInfoStr[i-1][1]=((ResultInfo)resultInfo[j]).searchEngine;
			resultInfoStr[i-1][2]=((ResultInfo)resultInfo[j]).url;
			resultInfoStr[i-1][3]=((ResultInfo)resultInfo[j]).toString();
			resultInfoStr[i-1][4]=((ResultInfo)resultInfo[j]).license;
			resultInfoStr[i-1][5]=((ResultInfo)resultInfo[j]).copyright;
			resultInfoStr[i-1][6]=((ResultInfo)resultInfo[j]).lineNumber+"";
			resultInfoStr[i-1][7]=String.valueOf((float)(((ResultInfo)resultInfo[j]).coverToken)/(float)(((ResultInfo)resultInfo[0]).tokenNumber));
			resultInfoStr[i-1][8]=((ResultInfo)resultInfo[j]).lastModifyTime;
		}
		return resultInfoStr;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
		this.searcher.getResultsMap().clear();
		this.tokenizer.clear();
		this.keywords = null;
	}

	public Token[] getKeywords() {
		return keywords;
	}

	public String getResultContent() {
		String content = "rank\tsearchEngine\turl\tPath\tlicense\tcopyRight\tlineNumber\tcoverRatio\tlastModifyTime\n";
		Object[][] rows = getResultTableStr();
		for(int i=0;i < rows.length;i++){
			for(int j = 0;j<rows[i].length;j++){
				if(j>0)content+="\t";
				content+=(String)rows[i][j];
				
			}
			content+="\n";
		}
		return content;
	}

	public void adjustKeywordToTop(int selectedRow) {
		if(selectedRow<0||selectedRow>keywords.length-1){
			return;
		}else{
			Token tempt = keywords[keywords.length-1-selectedRow];
			for(int i=keywords.length-1-selectedRow;i<keywords.length-1;i++){
				keywords[i]=keywords[i+1];
			}
			keywords[keywords.length-1]=tempt;
		}
		
	}

	public void adjustKeywordToBottom(int selectedRow) {
		if(selectedRow<0||selectedRow>keywords.length-1){
			return;
		}else{
			Token tempt = keywords[keywords.length-1-selectedRow];
			for(int i=keywords.length-1-selectedRow;i>0;i--){
				keywords[i]=keywords[i-1];
			}
			keywords[0]=tempt;
		}
		
	}

	public String getTopKeywordsStrs(int top) {
		String s = "";
		for(int i=0;i<Math.min(top, keywords.length);i++){
			s+=keywords[keywords.length-i-1].toString();
		}
		return s;
	}
	public String getAllKeywordsStrs() {
		String s = "";
		for(int i=0;i<keywords.length;i++){
			s+=keywords[keywords.length-i-1].toString();
		}
		return s;
	}


	
}
