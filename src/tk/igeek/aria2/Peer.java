package tk.igeek.aria2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class Peer extends CommonItem implements Parcelable {
	
	public Peer(HashMap<String, Object> data) {
		init(data);
	}
	
	public String peerId = "";
	public String ip = "";
	public String port = "";
	public String bitfield = "";
	public String amChoking = "";
	public String peerChoking = "";
	public String downloadSpeed = "";
	public String uploadSpeed = "";
	public String seeder = "";
	
	
	
	public Peer(Parcel source) {
		 readFromParcel(source);
	 }
	
	private void readFromParcel(Parcel in) 
	 {    
		read(in); 
     }
	
	public static final Parcelable.Creator<Peer> CREATOR = new Parcelable.Creator<Peer>() {

		@Override
		public Peer createFromParcel(Parcel source) {
			return new Peer(source);
		}

		@Override
		public Peer[] newArray(int size) {
			return new Peer[size];
		}
	};
	
	
	public void write(Parcel dest) {
		Field[] fields = getClass().getFields();
		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					Object value = field.get(this);
					if(field.getType() == String.class)
					{
						dest.writeString((String) value);
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
					if(field.getType() == String.class)
					{
						field.set(this,in.readString());
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		write(dest);
	}
}
