package tk.igeek.aria2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Struct which contains information retrieved from .torrent file.
 * BitTorrent only. It contains following keys.
 */
public class BitTorrent extends CommonItem 
{
		public BitTorrent(HashMap<String, Object> data) {
			init(data);
		}

		/**
		 * Struct which contains data from Info dictionary. It contains
		 * following keys.
		 */
		public class Info extends CommonItem {
			public Info(HashMap<String, Object> data) {
				init(data);
			}
			/**
			 * name in info dictionary. name.utf-8 is used if available.
			 */
			public String name = "";
		}

		/**
		 * List of lists of announce URI. If .torrent file contains announce and
		 * no announce-list, announce is converted to announce-list format.
		 */
		public Object[] announceList = null;

		/**
		 * The comment for the torrent. comment.utf-8 is used if available.
		 */
		public String comment = "";

		/**
		 * The creation time of the torrent. The value is an integer since the
		 * Epoch, measured in seconds.
		 */
		public String creationDate = "";

		/**
		 * File mode of the torrent. The value is either single or multi.
		 */
		public String mode = "";
		
		/**
		 * Struct which contains data from Info dictionary. It contains following keys.
		 */
		public HashMap<String, Object>  info = null;
}
