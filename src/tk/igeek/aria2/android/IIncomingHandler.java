package tk.igeek.aria2.android;

import android.os.Handler;
import android.os.Message;

public interface IIncomingHandler
{
	public void handleMessage(Message msg,Handler handler);
}
