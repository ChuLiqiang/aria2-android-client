package tk.igeek.aria2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public class Files extends CommonItem implements Parcelable{

	public Files(HashMap<String, Object> data) {
		init(data);
		getUris();
	}
	
	/**
	 * Index of file. Starting with 1. This is the same order with the files in
	 * multi-file torrent.
	 */
	public String index = "";

	/**
	 * File path.
	 */
	public String path = "";

	/**
	 * File size in bytes.
	 */
	public String length = "";

	/**
	 * Completed length of this file in bytes. Please note that it is possible
	 * that sum of completedLength is less than completedLength in
	 * {@link Aria2API#tellStatus(int, String...)} method. This is because completedLength in
	 * aria2.getFiles only calculates completed pieces. On the other hand,
	 * completedLength in {@link Aria2API#tellStatus(int, String...)} takes into account of partially
	 * completed piece.
	 */
	public String completedLength = "";

	/**
	 * "true" if this file is selected by --select-file option. If --select-file
	 * is not specified or this is single torrent or no torrent download, this
	 * value is always "true". Otherwise "false".
	 */
	public String selected = "";

	/**
	 * Returns the list of URI for this file. The element of list is the same
	 * struct used in aria2.getUris method.
	 */
	public Object[] uris = null;
	List<Uri> urisList = new ArrayList<Uri>();
	public void getUris()
	{
		for (Object uri: uris)
		{
			Uri uriTemp = new Uri((HashMap<String, Object>)uri);
			urisList.add(uriTemp);
		}
	}
	
	
	public static final Parcelable.Creator<Files> CREATOR =
			new Parcelable.Creator<Files>(){

	    @Override
	    public Files createFromParcel(Parcel source) {
	     return new Files(source);
	    }
	
	    @Override
	    public Files[] newArray(int size) {
	     return new Files[size];
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
	 
	 public Files(Parcel source) {
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

}
