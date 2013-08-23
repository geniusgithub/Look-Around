package com.geniusgithub.lookaround.network;


import android.content.Context;

import com.geniusgithub.lookaround.model.IToStringMap;
import com.geniusgithub.lookaround.model.ServerUrlBuilder;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class ClientEngine {


	private static final CommonLog log = LogFactory.createLog();
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
	}
	
	public void cancelTask(Context context){
		client.cancelRequests(context, true);
	}
	
	public boolean httpGetRequestEx(BaseRequestPacket packet, IRequestDataPacketCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			log.e("can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			log.e("BaseRequestPacket.object = null!!!");
			return false;
		}
		
		log.e("httpGetRequest url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, callback, null, packet.extra);	
		client.get(packet.context, url,  param, handler);
		
		return true;
	}
	
	public boolean httpPostRequestEx(BaseRequestPacket packet, IRequestDataPacketCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			log.e("can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			log.e("BaseRequestPacket.object = null!!!");
			return false;
		}
		
		log.e("httpPostRequestEx url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, callback, null, packet.extra);	
		client.post(packet.context, url,  param, handler);
		
		return true;
	}
		

	public boolean httpGetRequest(BaseRequestPacket packet,  IRequestContentCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			log.e("can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			log.e("BaseRequestPacket.object = null!!!");
			return false;
		}
		
		log.e("httpGetRequest url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, null, callback, packet.extra);	
		client.get(packet.context, url,  param, handler);
		
		return true;
	}
	
	public boolean httpPostRequest(BaseRequestPacket packet,  IRequestContentCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(packet.action);
		if (url.equals("")){
			log.e("can't get serverURL by action : " + packet.action);
			return false;
		}
		
		if (packet.object == null){
			log.e("BaseRequestPacket.object = null!!!");
			return false;
		}
		
		log.e("httpPostRequestEx url = " + url);
		RequestParams param = new RequestParams(packet.object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(packet.action, null, callback, packet.extra);	
		client.post(packet.context, url,  param, handler);
		
		return true;
	}

}
