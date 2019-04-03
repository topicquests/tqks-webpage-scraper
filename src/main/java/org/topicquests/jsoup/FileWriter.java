/**
 * 
 */
package org.topicquests.jsoup;

import net.minidev.json.JSONObject;
import java.io.*;
/**
 * @author jackpark
 *
 */
public class FileWriter {
	private WebScraper environment;
	private final String BASE_PATH;

	/**
	 * 
	 */
	public FileWriter(WebScraper env) {
		environment = env;
		BASE_PATH = environment.getStringProperty("BASEDatabasePath");
	}

	public void saveJSON(JSONObject jo) throws Exception {
		String url = jo.getAsString("url");
		String path = BASE_PATH+cleanString(url)+".json";
		PrintWriter out = new PrintWriter(path, "UTF-8");
		out.write(jo.toJSONString());
		out.flush();
		out.close();
	}
	
	String cleanString(String url) {
		String result = url;
		result = result.replaceAll(" ", "_");
		result = result.replaceAll("\\\\", "_");
		result = result.replaceAll("/", "_");
		result = result.replaceAll(":", "_");
		return result;
	}
}
