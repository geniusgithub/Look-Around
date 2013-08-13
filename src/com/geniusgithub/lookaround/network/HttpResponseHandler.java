package com.geniusgithub.lookaround.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpResponseHandler extends AsyncHttpResponseHandler{

	private static final CommonLog log = LogFactory.createLog();
	
	private int mAction = 0;
	private IRequestDataPacketCallback mDataPacketCallback;
	private IRequestContentCallback mContentCallback;
	
	public HttpResponseHandler(int action, IRequestDataPacketCallback callback1, IRequestContentCallback callback2){
		mDataPacketCallback = callback1;
		mAction = action;
		mContentCallback = callback2;
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
		if (isDataPacketAction(mAction)){
			if (mDataPacketCallback == null){
				return ;
			}
			ResponseDataPacket dataPacket = new ResponseDataPacket();
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(content);
				dataPacket.parseJson(jsonObject);
				mDataPacketCallback.onSuccess(mAction, dataPacket);
			} catch (JSONException e) {
				e.printStackTrace();
				mDataPacketCallback.onAnylizeFailure(mAction, content);
			}
		}else{
			if (mContentCallback == null){
				return ;
			}
			mContentCallback.onResult(mAction, true, content);
		}
		
	
	}

	@Override
	public void onFailure(Throwable error, String content) {
		
		log.d("mAction = " + mAction + ", onFailure! error = " + error.getMessage() + "\ncontent = " + content);
		if (isDataPacketAction(mAction)){
			if (mDataPacketCallback == null){
				return ;
			}
			mDataPacketCallback.onRequestFailure(mAction, content);
		}else{
			if (mContentCallback == null){
				return ;
			}
			mContentCallback.onResult(mAction, false, content);
		}
		
		
	}
	
	
	
	
	private boolean isDataPacketAction(int action){
		return true;
	}

	
}
