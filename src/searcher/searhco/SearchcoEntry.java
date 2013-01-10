package searcher.searhco;

public class SearchcoEntry {
	 String url;
	 String fileUrl;
	 String project;
	 String loc;
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
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String toString(){
		return "url:"+url+" fileUrl:"+fileUrl+" project:"+project+" loc:"+loc;
	}
}
