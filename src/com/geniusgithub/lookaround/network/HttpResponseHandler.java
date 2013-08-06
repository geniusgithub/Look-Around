package com.geniusgithub.lookaround.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpResponseHandler extends AsyncHttpResponseHandler{

	private static final CommonLog log = LogFactory.createLog();
	
	private int mAction = 0;
	private IRequestCallback mCallback;
	
	public HttpResponseHandler(int action, IRequestCallback callback){
		mCallback = callback;
		mAction = action;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	//	log.e("mAction = " + mAction + ", onStart!");
	}

	@Override
	public void onFinish() {
		super.onFinish();
	//	log.e("mAction = " + mAction + ", onFinish!");
	}

	@Override
	public void onSuccess(int statusCode, String content) {
		
		log.d("mAction = " + mAction + ", onSuccess! statusCode = " + statusCode + "\ncontent = " + content);
		ResponseDataPacket dataPacket = new ResponseDataPacket();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(content);
			dataPacket.parseJson(jsonObject);
			mCallback.onSuccess(mAction, dataPacket);
		} catch (JSONException e) {
			e.printStackTrace();
			mCallback.onAnylizeFailure(mAction, content);
		}
	
	}

	@Override
	public void onFailure(Throwable error, String content) {
		
		log.d("mAction = " + mAction + ", onFailure! error = " + error.getMessage() + "\ncontent = " + content);
		mCallback.onRequestFailure(mAction, content);
	}
	
	
	

	
}
