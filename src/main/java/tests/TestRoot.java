/**
 * 
 */
package tests;

import org.topicquests.jsoup.WebScraperEngine;
import org.topicquests.jsoup.WebScraper;

/**
 * @author jackpark
 *
 */
public class TestRoot {
	protected WebScraper environment;
	protected WebScraperEngine engine;
	/**
	 * 
	 */
	public TestRoot() {
		environment = new WebScraper();
		engine = environment.getEngine();
	}

}
