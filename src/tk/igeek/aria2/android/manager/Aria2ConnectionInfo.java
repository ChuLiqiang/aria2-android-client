package tk.igeek.aria2.android.manager;


import android.os.Parcel;
import android.os.Parcelable;
import tk.igeek.aria2.android.SettingsActivity;

public class Aria2ConnectionInfo implements Parcelable {
	public String _host = null;
	public int _port = 6800;
	public String _username = null;
	public String _password = null;
	
	public Aria2ConnectionInfo(PreferencesManager _preferencesManager)
	{
		_host = _preferencesManager.getHost();
		_port = _preferencesManager.getPort();
		_username = _preferencesManager.getPreferences(SettingsActivity.PREF_KEY_USERNAME);
		_password = _preferencesManager.getPreferences(SettingsActivity.PREF_KEY_PASSWORD);
	}

	
	public boolean canUseAuthentication()
	{
		if(_username.equals("") || _password.equals(""))
		{
			return false;
		}
		return true;
	}
	
	public boolean configIsChange(Aria2ConnectionInfo aria2ConnectionInfo)
	{
		if(!aria2ConnectionInfo._host.equals(_host))
		{
			return true;
		}
		
		if(aria2ConnectionInfo._port != _port)
		{
			return true;
		}
			
		if(!aria2ConnectionInfo._username.equals(_username))
		{
			return true;
		}
		
		if(!aria2ConnectionInfo._password.equals(_password))
		{
			return false;
		}
		
		return false;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_host);
		dest.writeInt(_port);
		dest.writeString(_username);
		dest.writeString(_password);

	}
	
	
	public Aria2ConnectionInfo(){
	
	}
	
	public Aria2ConnectionInfo(Parcel source){
		_host = source.readString();
		_port = source.readInt();
		_username = source.readString();
		_password = source.readString();
	}
	
	public static final Parcelable.Creator<Aria2ConnectionInfo> CREATOR = new Parcelable.Creator<Aria2ConnectionInfo>() {

		@Override
		public Aria2ConnectionInfo createFromParcel(Parcel source) {
			return new Aria2ConnectionInfo(source);
		}

		@Override
		public Aria2ConnectionInfo[] newArray(int size) {
			return new Aria2ConnectionInfo[size];
		}
	};
	
}
