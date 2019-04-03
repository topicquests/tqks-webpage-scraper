/**
 * 
 */
package tests;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class FirstTest extends TestRoot {
	private final String URL = "http://wordpress.liquid.info/liquid-view-modes/";
	//"https://www.laetusinpraesens.org/"; 
	private final int NUM_LINKS = 1000;
	/**
	 * 
	 */
	public FirstTest() {
		IResult r = engine.harvestURL(URL, NUM_LINKS);
		System.out.println("DID");
		environment.close();
		System.exit(0);
	}

}
