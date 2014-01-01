package tk.igeek.aria2;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Struct which contains data from Info dictionary. It contains
 * following keys.
 */
public class Info extends CommonItem implements Parcelable{
	
	public Info() {
		
	}

	public boolean haveSetData = false;
	
	public void setData(HashMap<String, Object> data) {
		haveSetData = true;
		init(data);
	}
	
	/**
	 * name in info dictionary. name.utf-8 is used if available.
	 */
	public String name = "";
	
	public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {

		@Override
		public Info createFromParcel(Parcel source) {
			return new Info(source);
		}

		@Override
		public Info[] newArray(int size) {
			return new Info[size];
		}
	};
	
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		write(dest); 
		
	}
	
	 private void readFromParcel(Parcel in) 
	 {    
		read(in); 
     }  
	 
	 public Info(Parcel source) {
		 readFromParcel(source);
	 }
	 
	 public void write(Parcel dest) {
		Field[] fields = getClass().getFields();
		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					
					Object value = field.get(this);
					if(field.getType() == String.class)
					{
						dest.writeString((String) value);
					}else if (field.getType() == boolean.class) {
						dest.writeByte((byte) ((Boolean) value ? 1 : 0));
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
					}else if (field.getType() == boolean.class) {
						field.set(this, in.readByte() != 0);
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