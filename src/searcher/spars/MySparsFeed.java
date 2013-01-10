package searcher.spars;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySparsFeed {
	String xml;
	String nodePattern = "file url=\"(.+)\" file-url=\"(.+)\" score=\"(.+)\" component-rank=\"(.+)\" last-modified=\"\\w+ (\\w+ \\w+) \\w\\w:\\w\\w:\\w\\w \\w+ (\\w+)\" qualified-name=\"(.+)\"/";
	public MySparsFeed(String xml){
		this.xml = xml;
	}
	/*
	 *example:
<?xml version="1.0" encoding="UTF-8"?>
<target-query result="10">
<file url="PT:C;ET:C;ID:880886" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=880885" score="1.035012E-4" component-rank="28.499776257145506" last-modified="Wed Dec 24 15:09:00 JST 2008" qualified-name="apache.org/commons-digester-2.0:org/apache/commons/digester/Digester.java"/>
<file url="PT:C;ET:C;ID:1278850" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=1278849" score="1.4401182E-4" component-rank="8.49658811140388" last-modified="Sat Nov 08 06:06:24 JST 2008" qualified-name="eclipse.org/org.eclipse.update.core:src/org/eclipse/update/internal/core/UpdateManagerUtils.java"/>
<file url="PT:C;ET:C;ID:365781" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=365780" score="0.0012523019" component-rank="6.301817928277812" last-modified="Sat Jan 03 04:38:06 JST 2009" qualified-name="apache.org/httpcomponents-httpclient:module-client/src/main/java/org/apache/http/conn/scheme/SocketFactory.java"/>
<file url="PT:C;ET:C;ID:1811284" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=1811283" score="2.9842171E-5" component-rank="12.040750855209769" last-modified="Fri Nov 19 03:52:46 JST 2004" qualified-name="sourceforge.net/frostwire:cvs/core-uhc/com/limegroup/gnutella/altlocs/AlternateLocation.java"/>
<file url="PT:C;ET:C;ID:92335" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=92334" score="6.2609934E-6" component-rank="25.97191915536911" last-modified="Sat Jan 03 05:08:06 JST 2009" qualified-name="apache.org/cayenne:main/trunk/framework/cayenne-jdk1.5-unpublished/src/main/java/org/apache/cayenne/access/DataContext.java"/>
<file url="PT:C;ET:C;ID:1832505" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=1832504" score="2.1325073E-5" component-rank="13.302353017253226" last-modified="Tue Aug 24 02:38:36 JST 2004" qualified-name="sourceforge.net/frostwire:cvs/httpclient-118/src/java/org/apache/commons/httpclient/Cookie.java"/>
<file url="PT:C;ET:C;ID:277905" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=277904" score="1.1480627E-4" component-rank="0.4501589971533009" last-modified="Sat Jan 03 04:33:48 JST 2009" qualified-name="apache.org/db-torque-runtime:src/java/org/apache/torque/adapter/DBSapDB.java"/>
<file url="PT:C;ET:C;ID:1798299" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=1798298" score="2.1643336E-5" component-rank="12.014768413025228" last-modified="Fri Dec 23 03:17:10 JST 2005" qualified-name="sourceforge.net/frostwire:cvs/core/com/limegroup/gnutella/altlocs/AlternateLocation.java"/>
<file url="PT:C;ET:C;ID:277780" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=277779" score="1.0315631E-4" component-rank="0.5376856692239421" last-modified="Sat Jan 03 04:33:48 JST 2009" qualified-name="apache.org/db-torque-runtime:src/java/org/apache/torque/adapter/DBHypersonicSQL.java"/>
<file url="PT:C;ET:C;ID:2472130" file-url="http://demo.spars.info/r/spars?sparshook=dl&amp;fid=2472129" score="8.4648957E-5" component-rank="0.18263210348782077" last-modified="Sun Jan 04 08:18:58 JST 2009" qualified-name="sourceforge.net/opennms:svn/opennms/opennms/trunk/opennms-webapp/src/main/java/org/opennms/web/admin/notification/noticeWizard/NotificationWizardServlet.java"/>
</target-query> */
	public 	ArrayList<SparsSearchEntry> getSparsEntries(){
		ArrayList<SparsSearchEntry> sparsSearchEntry = new ArrayList<SparsSearchEntry>();
		String[] nodes = xml.split("><");
		Pattern p = Pattern.compile(nodePattern);
		
		for(String node:nodes){
			Matcher m = p.matcher(node);
			if(m.matches()){
				SparsSearchEntry sse = new SparsSearchEntry();
				sse.setUrl(m.group(1));
				sse.setFileUrl(m.group(2));
				sse.setScore(m.group(3));
				sse.setComponentRank(m.group(4));
				sse.setLastModifiedTime(m.group(5)+","+m.group(6));
				sse.setQualifiedName(m.group(7));
				sparsSearchEntry.add(sse);
			}
			
		}
		return sparsSearchEntry;
	}
	public static void main(String[] args){
		String example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><target-query result=\"10\">" +
				"<file url=\"PT:C;ET:C;ID:880886\" file-url=\"http://demo.spars.info/r/spars?sparshook=dl&amp;fid=880885\" score=\"1.035012E-4\" component-rank=\"28.499776257145506\" last-modified=\"Wed Dec 24 15:09:00 JST 2008\" qualified-name=\"apache.org/commons-digester-2.0:org/apache/commons/digester/Digester.java\"/>" +
				"<file url=\"PT:C;ET:C;ID:1278850\" file-url=\"http://demo.spars.info/r/spars?sparshook=dl&amp;fid=1278849\" score=\"1.4401182E-4\" component-rank=\"8.49658811140388\" last-modified=\"Sat Nov 08 06:06:24 JST 2008\" qualified-name=\"eclipse.org/org.eclipse.update.core:src/org/eclipse/update/internal/core/UpdateManagerUtils.java\"/>" +
				"<file url=\"PT:C;ET:C;ID:365781\" file-url=\"http://demo.spars.info/r/spars?sparshook=dl&amp;fid=365780\" score=\"0.0012523019\" component-rank=\"6.301817928277812\" last-modified=\"Sat Jan 03 04:38:06 JST 2009\" qualified-name=\"apache.org/httpcomponents-httpclient:module-client/src/main/java/org/apache/http/conn/scheme/SocketFactory.java\"/>" +
				"</target-query>>";
		MySparsFeed msf = new MySparsFeed(example);
		ArrayList<SparsSearchEntry> entries = msf.getSparsEntries();
		for(SparsSearchEntry n:entries){
			System.out.println(n.toString());
		}
	}
}
