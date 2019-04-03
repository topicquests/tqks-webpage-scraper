/**
 * 
 */
package org.topicquests.jsoup;

import org.topicquests.neo4j.BaseNeoClient;
import org.topicquests.neo4j.WebPageGraphClient;
import org.topicquests.support.RootEnvironment;

/**
 * @author jackpark
 *
 */
public class WebScraper extends RootEnvironment {
	private WebScraperEngine engine;
	//private BaseNeoClient neo4j;
	private WebPageGraphClient webpageClient;
	/**
	 */
	public WebScraper() {
		super("config-props.xml", "logger.properties");
		//neo4j = new BaseNeoClient(this);
		engine = new WebScraperEngine(this);
		webpageClient = new WebPageGraphClient(this);
	}
	
	//public BaseNeoClient getBaseNeoClient() {
	//	return neo4j;
	//}
	
	public WebPageGraphClient getWebPageClient () {
		return webpageClient;
	}
	
	public WebScraperEngine getEngine() {
		return engine;
	}

	public void close() {
		webpageClient.close();
	}
}
