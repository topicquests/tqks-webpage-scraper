/**
 * 
 */
package org.topicquests.jsoup;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.topicquests.jsoup.util.PersistentSet;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
/**
 * @author jackpark
 * Engine starts with a seed URL and then creates a collection of
 * JSON files, one for each page, complete with title, url, and list of outbound hyperlinks
 */
public class WebScraperEngine {
	private WebScraper environment;
	private PersistentSet linkData;
	private FileWriter writer;
	private long limit = -1;
	//this is not thread safe
	private long counter = 0;
	/**
	 * 
	 */
	public WebScraperEngine(WebScraper env) {
		environment = env;
		linkData = new PersistentSet(environment);
		writer = new FileWriter(environment);
		counter = 0;
	}

	/**
	 * A recursive walk along links from a given <code>url</code>
	 * @param url
	 * @param maxLinks -1 means do not stop
	 * @return
	 */
	public IResult harvestURL(String url, long maxLinks) {
		limit = maxLinks;
		IResult result = new ResultPojo();
		_harvestURL(url, result);
		return result;
	}
	
	String cleanURL(String url) {
		String result = url;
		int where = result.lastIndexOf('/');
		if (where > -1) {
			int xpath = result.lastIndexOf('#', where);
			if (xpath > -1) {
				result = result.substring(0, ++where);
			}
		}
		return result;
	}
	/**
	 * The recursion engine itself
	 * @param _url
	 * @param result
	 */
	private void _harvestURL(String _url, IResult result) {
		System.out.println("Look "+limit+" "+counter+" "+_url);
		if (limit > -1 && ++counter >= limit) return;
		String url = cleanURL(_url);
		boolean seenThisBefore = linkData.add(url);
		if (!seenThisBefore) return;
		System.out.println("Looking "+url);
		JSONObject pageObject = new JSONObject();
		pageObject.put("url", url);
		JSONArray pageLinks = new JSONArray();
		pageObject.put("links", pageLinks);
		List<String>linkURLs = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla")
		            .timeout(5000)
		            .cookie("cookiename", "val234")
		            .cookie("anothercookie", "ilovejsoup")
		            .referrer("http://google.com")
		            .get();
			Elements title = doc.select("title");
			Element x = title.get(0);
			pageObject.put("title", x.text());
			Elements body = doc.select("body");
			Elements bodyLinks = body.select("a[href]"); // only those with hrefs
			//environment.logDebug("Title "+title);
			//environment.logDebug("Links "+bodyLinks);
			String urx;
			for (Element link : bodyLinks) {
			
				urx = link.absUrl("href");
				linkURLs.add(urx);
				pageLinks.add(urx);
			}
			environment.logDebug(pageObject.toJSONString());
			writer.saveJSON(pageObject);
			//now recurse
			Iterator<String> itr = linkURLs.iterator();
			while (itr.hasNext())
				_harvestURL(itr.next(), result);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError(e.getMessage(), e);
		}
		
	}
}
