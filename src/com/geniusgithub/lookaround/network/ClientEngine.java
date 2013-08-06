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

	public boolean httpGetRequest(int action, IToStringMap object, IRequestCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(action);
		if (url.equals("")){
			log.e("can't get serverURL by action : " + action);
			return false;
		}
		
		RequestParams param = new RequestParams(object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(action, callback);	
		client.get(url,  param, handler);
		
		return true;
	}
	
	public boolean httpPostRequest(int action, IToStringMap object, IRequestCallback callback)
	{
		String url = ServerUrlBuilder.getServerURL(action);
		if (url.equals("")){
			return false;
		}
		
		RequestParams param = new RequestParams(object.toStringMap());
		
		HttpResponseHandler handler = new HttpResponseHandler(action, callback);	
		client.post(url,  param, handler);
		
		return true;
	}
	
}
