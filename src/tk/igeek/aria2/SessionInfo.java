package tk.igeek.aria2;

import java.util.HashMap;

public class SessionInfo extends CommonItem {

	public String sessionId = null;

	public SessionInfo() {
	}

	public SessionInfo(HashMap<String, Object> data) {
		init(data);
	}
}