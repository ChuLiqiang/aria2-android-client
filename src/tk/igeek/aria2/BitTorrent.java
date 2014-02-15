package tk.igeek.aria2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Struct which contains information retrieved from .torrent file. BitTorrent
 * only. It contains following keys.
 */
public class BitTorrent extends CommonItem implements Parcelable {
	public boolean isHaveSetData() {
		return haveSetData;
	}

	public boolean haveSetData = false;

	public BitTorrent() {

	}

	public void setData(HashMap<String, Object> data) {
		haveSetData = true;
		init(data);
		infoData.setData(info);
		initAnnounceList(); 
		
	}

	
	public ArrayList<String> getAnnounceList()
	{
		return announceStringList;
	}
	
	
	private void initAnnounceList() {
		if(announceList == null)
		{
			return;
		}
		int announceListSize = announceList.length;
		for(int announceListIndex = 0; announceListIndex  < announceListSize ; announceListIndex++)
		{
			Object[] announceListItem = (Object[]) announceList[announceListIndex];
			int announceListItemSize = announceListItem.length;
			for(int announceListItemIndex = 0; announceListItemIndex  < announceListItemSize ; announceListItemIndex++)
			{
				announceStringList.add((String)(announceListItem[announceListItemIndex]));
			}
		}
		
	}

	/**
	 * List of lists of announce URI. If .torrent file contains announce and no
	 * announce-list, announce is converted to announce-list format.
	 */
	public Object[] announceList = null;
	private ArrayList<String> announceStringList = new ArrayList<String>();
	

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
	 * Struct which contains data from Info dictionary. It contains following
	 * keys.
	 */
	public HashMap<String, Object> info = null;
	private Info infoData = new Info();
	
	public Info getInfoData() {
		return infoData;
	}

	public static final Parcelable.Creator<BitTorrent> CREATOR = new Parcelable.Creator<BitTorrent>() {

		@Override
		public BitTorrent createFromParcel(Parcel source) {
			return new BitTorrent(source);
		}

		@Override
		public BitTorrent[] newArray(int size) {
			return new BitTorrent[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		write(dest,flags);

	}

	private void readFromParcel(Parcel in) {
		read(in);
	}

	public BitTorrent(Parcel source) {
		readFromParcel(source);
	}

	public void write(Parcel dest,int flags) {
		Field[] fields = getClass().getFields();
		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					Object value = field.get(this);
					if (field.getType() == String.class) {
						dest.writeString((String) value);
					} else if (field.getType() == boolean.class) {
						dest.writeByte((byte) ((Boolean) value ? 1 : 0));
					}else if(field.getName().equals("info"))
					{
						dest.writeParcelable(infoData, flags);
					}else if(field.getName().equals("announceList"))
					{
						dest.writeStringList(announceStringList);
					}
					
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void read(Parcel in) {
		Field[] fields = getClass().getFields();

		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					if (field.getType() == String.class) {
						field.set(this, in.readString());
					} else if (field.getType() == boolean.class) {
						field.set(this, in.readByte() != 0);
					}else if(field.getName().equals("info"))
					{
						infoData = in.readParcelable(Info.class.getClassLoader());
					}else if(field.getName().equals("announceList"))
					{
						in.readStringList(announceStringList);
					}
					
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
