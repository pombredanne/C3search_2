package searcher;

public class ResultInfo implements Comparable{
	public String url;
	public String pckg;
	public String path;
	public String projectName;
	public String license;
	public String copyright;
	public String searchEngine;
	public String lastModifyTime;
	public int coverToken;
	public int lineNumber;
	public int tokenNumber;
	
	public ResultInfo(){
		url=pckg=path=projectName=license=copyright=searchEngine=lastModifyTime="NotSure";
		coverToken=lineNumber=tokenNumber=1;
	}
	public String toString(){
		return pckg+path;
	}

	@Override
	public int compareTo(Object arg0) {
		
		float temp =  (this.coverToken-((ResultInfo)arg0).coverToken);
		return temp>0?1:(temp==0?0:-1);
	}
	public String infoStr(){
		return pckg+path+projectName+"\t"+coverToken+"/"+tokenNumber+"\t"+lineNumber;
	}
}
