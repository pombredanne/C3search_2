package searcher.googlecode;

public class GoogleCodeEntry {
	 String url;
	 String fileUrl;
	 String project;
	 String loc;
	 String license;
	 
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
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
