/**
 * 
 */
package tests;

import java.io.*;
import java.util.*;

import org.topicquests.neo4j.WebPageGraphClient;
import org.topicquests.support.api.IResult;
import org.topicquests.support.util.TextFileHandler;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 * This will read some web page JSON files and
 * <li>Create WebPage Nodes</li>
 * <li>Connect them</li>
 * That's a two-pass algorithm
 * Still has te opportunity to fail if a URL leads to a node that doesn't exist
 */
public class ClientTest extends TestRoot {
	private WebPageGraphClient client;
	private TextFileHandler handler;
	/**
	 * 
	 */
	public ClientTest() {
		client = environment.getWebPageClient();
		handler = new TextFileHandler();
		runTest();
		environment.close();
		System.exit(0);
	}
	
	void runTest() {
		//Phase 1: load the WebPages
		loadFiles_1();
		//Phase 2: connect them
		loadFiles_2();
	}
	
	
	void loadFiles_1() {
		String path = environment.getStringProperty("BASEDatabasePath");
		File dir = new File(path);
		File [] files = dir.listFiles();
		int len = files.length;
		File f;
		String fpath;
		for (int i=0;i<len;i++) {
			f = files[i];
			fpath = f.getAbsolutePath();
			if (fpath.endsWith(".json")) {
				doPhase1(f);
			}
		}
	}

	void loadFiles_2() {
		String path = environment.getStringProperty("BASEDatabasePath");
		File dir = new File(path);
		File [] files = dir.listFiles();
		int len = files.length;
		File f;
		String fpath;
		for (int i=0;i<len;i++) {
			f = files[i];
			fpath = f.getAbsolutePath();
			if (fpath.endsWith(".json")) {
				doPhase2(f);
			}
		}
	}

	void doPhase1(File f) {
		try {
			JSONObject page = fileToJSONObject(f);
			String title = page.getAsString("title");
			String url = page.getAsString("url");
			IResult r = client.addWebPage(title, url);
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError(e.getMessage(), e);
		}
		
	}

	void doPhase2(File f) {
		try {
			JSONObject page = fileToJSONObject(f);
			String fromUrl = page.getAsString("url");
			JSONArray ja = (JSONArray)page.get("links");
			Iterator<Object> itr = ja.iterator();
			String toUrl;
			IResult r;
			while (itr.hasNext()) {
				toUrl = (String)itr.next();
				if (!toUrl.equals(fromUrl))
					r = client.linkWebPages(fromUrl, toUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError(e.getMessage(), e);
		}
		
	}

	JSONObject fileToJSONObject(File f) throws Exception {
		JSONObject result = null;
		String json = handler.readFile(f);
		JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		result = (JSONObject)p.parse(json);
		return result;
	}
	
	
}
