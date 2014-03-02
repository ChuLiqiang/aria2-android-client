package tk.igeek.aria2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class Server extends CommonItem implements Parcelable{

	public String index = "";
	public Object[] servers = null;
	
	public Server(HashMap<String, Object> data) {
		init(data);
	}
	
	public Server(Parcel source) {
		 readFromParcel(source);
	 }
	
	private void readFromParcel(Parcel in) 
	{    
		read(in); 
	}

	public static final Parcelable.Creator<Server> CREATOR = new Parcelable.Creator<Server>() {

		@Override
		public Server createFromParcel(Parcel source) {
			return new Server(source);
		}

		@Override
		public Server[] newArray(int size) {
			return new Server[size];
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		write(dest);
		
	}

}
