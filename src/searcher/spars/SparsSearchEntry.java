package searcher.spars;

public class SparsSearchEntry {
	
	 String url;
	 String fileUrl;
	 String score;
	 String componentRank;
	 String lastModifiedTime;
	 String QualifiedName;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getComponentRank() {
		return componentRank;
	}
	public void setComponentRank(String componentRank) {
		this.componentRank = componentRank;
	}
	public String getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public String getQualifiedName() {
		return QualifiedName;
	}
	public void setQualifiedName(String qualifiedName) {
		QualifiedName = qualifiedName;
	}
	public String toString(){
		return fileUrl+" "+lastModifiedTime+" "+QualifiedName;
	}
	 
}
