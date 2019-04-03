/**
 * 
 */
package org.topicquests.jsoup.util;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.topicquests.jsoup.WebScraper;

import java.io.File;
import java.util.Iterator;
import java.util.NavigableSet;

/**
 * @author jackpark
 * Jars required
 * kotlin-runtime.jar
 * eclipse-collections-7.1.0.jar
 * eclipse-collections-api-7.1.0.jar
 * lz4-1.3.0.jar
 * elsa-3.0.0-M5.jar
 * guava-19.0.jar
 */
public class PersistentSet {
	private WebScraper environment;
	private DB database;
	private NavigableSet<String> treeSet;
	private boolean isClosed = true;
	
	public PersistentSet(WebScraper env) {
		environment = env;
		String databasePath = environment.getStringProperty("MyDatabasePath");
		String storeName = environment.getStringProperty("MyDatabaseName");
		System.out.println("PS-0 "+databasePath+" | "+storeName);
		try {
			File f = new File(databasePath);
			System.out.println("PS-0a "+f);
			database = DBMaker.fileDB(f)
					.closeOnJvmShutdown()
					//.transactionDisable()  defaults to that
					//.compressionEnable()
					.make();
			System.out.println("PS-1 "+database);
			treeSet = (NavigableSet<String>)database.treeSet(storeName).createOrOpen();
			System.out.println("PS-2 "+treeSet.size());
			isClosed = false;
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		}
		
	}
	
	/**
	 * Returns <code>true</code> if <code>val</code> is added
	 * @param val
	 * @return
	 */
	public boolean add(String val) {
		return treeSet.add(val);
	}
	
	public boolean contains(String val) {
		return treeSet.contains(val);
	}
	
	public Iterator<String> iterator() {
		return treeSet.iterator();
	}
	
	public int size() {
		return treeSet.size();
	}
	/**
	 * Returns <code>true</code> if <code>val</code> is removed
	 * @param val
	 * @return
	 */
	public boolean remove(String val) {
		return treeSet.remove(val);
	}
	
	public void shutDown() {
		if (!isClosed) {
			System.out.println("PersistentMap shutting down");
			database.commit();
			//database.compact();
			database.close();
			System.out.println("PersistentMap closed");
			isClosed = true;
		}
	}


}
