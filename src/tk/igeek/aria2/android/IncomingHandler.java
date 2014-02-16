package tk.igeek.aria2.android;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class IncomingHandler extends Handler {
    private final WeakReference<IIncomingHandler> _InCome; 

    public IncomingHandler(IIncomingHandler inCome) {
        _InCome = new WeakReference<IIncomingHandler>(inCome);
    }
    
    public IncomingHandler(Looper looper,IIncomingHandler inCome) {
    	super(looper);
    	_InCome = new WeakReference<IIncomingHandler>(inCome);
    	
    }
    @Override
    public void handleMessage(Message msg)
    {
         IIncomingHandler inCome = _InCome.get();
         if (inCome != null) {
              inCome.handleMessage(msg,this);
         }
    }
}