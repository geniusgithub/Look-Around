package com.geniusgithub.lookaround.network;


import android.content.Context;

import com.geniusgithub.common.util.AlwaysLog;
import com.geniusgithub.lookaround.model.ServerUrlBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class ClientEngine {


	private static final String TAG = ClientEngine.class.getSimpleName();

	private static ClientEngine mInstance;
	
	private Context mContext;
	private AsyncHttpClient client = new AsyncHttpClient();

	
	public static synchronized ClientEngine getInstance(Context context) {
		if (mInstance == null){
			mInstance  = new ClientEngine(context);
		}
		return mInstance;
	}

	private ClientEngine(Context context)
	{	
		mContext = context;
		client.setTimeout(20 * 1000);
	}
	
	public void cancelTask(Context context){
		client.cancelRequests(context, true);
	}
	
	public boolean httpGetRequestEx(BaseRequestPacket packet, IRequestDataPacketCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			AlwaysLog.e(TAG, "can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			AlwaysLog.e(TAG, "BaseRequestPacket.object = null!!!");
			return false;
		}
		
		AlwaysLog.d(TAG, "httpGetRequest url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());

		HttpResponseHandler handler = new HttpResponseHandler(packet.action, callback, null, packet.extra);	
		client.get(packet.context, url,  param, handler);
		
		return true;
	}
	
	public boolean httpPostRequestEx(BaseRequestPacket packet, IRequestDataPacketCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			AlwaysLog.e(TAG, "can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			AlwaysLog.e(TAG, "BaseRequestPacket.object = null!!!");
			return false;
		}
		
		AlwaysLog.d(TAG, "httpPostRequestEx url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, callback, null, packet.extra);	
		client.post(packet.context, url,  param, handler);
		
		return true;
	}
		

	public boolean httpGetRequest(BaseRequestPacket packet,  IRequestContentCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			AlwaysLog.e(TAG, "can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			AlwaysLog.e(TAG, "BaseRequestPacket.object = null!!!");
			return false;
		}
		
		AlwaysLog.e(TAG, "httpGetRequest url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, null, callback, packet.extra);	
		client.get(packet.context, url,  param, handler);
		
		return true;
	}
	
	public boolean httpPostRequest(BaseRequestPacket packet,  IRequestContentCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			AlwaysLog.e(TAG, "can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			AlwaysLog.e(TAG, "BaseRequestPacket.object = null!!!");
			return false;
		}
		
		AlwaysLog.i(TAG, "httpPostRequestEx url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, null, callback, packet.extra);	
		client.post(packet.context, url,  param, handler);
		
		return true;
	}

}
