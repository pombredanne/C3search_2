package htmlClient;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestHtmlClient {
	public static void main(String[] args) {
		final WebClient webClient = new WebClient(BrowserVersion.CHROME_16);
		webClient.setJavaScriptEnabled(true);
		HtmlPage page;
		try {
			String s =("http://code.google.com/codesearch#search/&q=HttpClient.java%20http%20lang:%5Ejava&p=0");
			// assertEquals("HtmlUnit - Welcome to HtmlUnit",
			// page.getTitleText());
			String c = getHttpResponse(s);
			System.out.println(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected static String getHttpResponse(String request) throws Exception {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		HtmlPage page;
		// System.out.println("start-----------------");
		page = webClient.getPage(request);
		// assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());
		webClient.waitForBackgroundJavaScript(5000);
		// webClient.getJavaScriptEngine().pumpEventLoop(5000);
		final String pageAsXml = page.asXml();
		// assertTrue(pageAsXml.contains("<body class=\"composite\">"));

		webClient.closeAllWindows();
		return pageAsXml;
	}
}
