package com.geniusgithub.lookaround.network;

import com.geniusgithub.common.util.AlwaysLog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpResponseHandler extends AsyncHttpResponseHandler{

	private static final String TAG = HttpResponseHandler.class.getSimpleName();
	
	private int mAction = 0;
	private IRequestDataPacketCallback mDataPacketCallback;
	private IRequestContentCallback mContentCallback;
	
	private Object mExtra;
	
	public HttpResponseHandler(int action, IRequestDataPacketCallback callback1, IRequestContentCallback callback2, Object extra){
		mDataPacketCallback = callback1;
		mAction = action;
		mContentCallback = callback2;
		mExtra = extra;
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
		
	//	log.d("mAction = " + mAction + ", onSuccess! statusCode = " + statusCode + "\ncontent = " + content);
		if (isDataPacketAction(mAction)){
			if (mDataPacketCallback == null){
				return ;
			}
			ResponseDataPacket dataPacket = new ResponseDataPacket();
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(content);
				dataPacket.parseJson(jsonObject);
				mDataPacketCallback.onSuccess(mAction, dataPacket, mExtra);
			} catch (JSONException e) {
				e.printStackTrace();
				mDataPacketCallback.onAnylizeFailure(mAction, content, mExtra);
			}
		}else{
			if (mContentCallback == null){
				return ;
			}
			mContentCallback.onResult(mAction, true, content, mExtra);
		}
		
	
	}

	@Override
	public void onFailure(Throwable error, String content) {
		
		AlwaysLog.d(TAG, "mAction = " + mAction + ", onFailure! error = " + error.getMessage() + "\ncontent = " + content);
		if (isDataPacketAction(mAction)){
			if (mDataPacketCallback == null){
				return ;
			}
			mDataPacketCallback.onRequestFailure(mAction, content, mExtra);
		}else{
			if (mContentCallback == null){
				return ;
			}
			mContentCallback.onResult(mAction, false, content, mExtra);
		}
		
		
	}
	
	
	
	
	private boolean isDataPacketAction(int action){
		return true;
	}

	
}
