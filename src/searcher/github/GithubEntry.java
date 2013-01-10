package searcher.github;

public class GithubEntry {
	
	String projectName;
	String path;
	String url;
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String toString(){
		return "url:"+url+" path:"+path+" projectName:"+projectName;
	}
}
