package tk.igeek.aria2;

import java.util.HashMap;

public class Uri extends CommonItem {
	
	/**
	 * URI
	 */
	public String uri = "";

	/**
	 * 'used' if the URI is already used. 'waiting' if the URI is waiting in the queue.
	 */
	public String status = "";
	
	public Uri(HashMap<String, Object> data) {
		init(data);
	}
}
