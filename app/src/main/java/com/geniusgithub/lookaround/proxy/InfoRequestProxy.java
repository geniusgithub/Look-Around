package com.geniusgithub.lookaround.proxy;

import android.content.Context;

import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.BaseRequestPacket;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class InfoRequestProxy implements IRequestDataPacketCallback{
	
	private static final CommonLog log = LogFactory.createLog();
	
	private final static int FLAG_REFRESH = 0;
	private final static int FLAG_LOADMORE = 1;
	
	public static interface IRequestResult{
		
		public void onSuccess(boolean isLoadMore, boolean isLoadDataComplete);
		public void onRequestFailure(boolean isLoadMore);
		public void onAnylizeFailure(boolean isLoadMore);
	}

	private Context mContext;
	private BaseType.ListItem mTypeData;
	
	private IRequestResult mCallback;
	
	private ClientEngine mClientEngine;
	private int mPage = 0;

	private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();
	
	
	public InfoRequestProxy(Context context, BaseType.ListItem data, IRequestResult callback){
		mContext = context;
		mTypeData = data;
		mCallback = callback;
		mClientEngine=  ClientEngine.getInstance(mContext);
	}
	
	public void requestRefreshInfo(){
		log.i("requestRefreshInfo");
		getInfoByPage(0, FLAG_REFRESH);
	}
	
	public void requestMoreInfo(){
		log.i("requestMoreInfo");
		getInfoByPage(mPage + 1, FLAG_LOADMORE);
	}
	
	public void cancelRequest(){
		mClientEngine.cancelTask(mContext);
	}
	
	public List<BaseType.InfoItem> getData(){
		return mContentData;
	}
	

	private void getInfoByPage(int page,int requestType){
	
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(mContext, mTypeData.mTypeID);
		object.mPage = String.valueOf(page);
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.GET_INFO_MSGID;
		packet.object = object;
		packet.extra = new Integer(requestType);

		
		mClientEngine.httpGetRequestEx(packet, this);
	}

	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket, Object extra) {
		
		switch(requestAction){	
			case PublicType.GET_INFO_MSGID:{
				onGetInfoResult(dataPacket,extra);
			}
			break;
			
		}
	}

	@Override
	public void onRequestFailure(int requestAction, String content, Object extra) {
		int type = (Integer) extra;
		if (type == FLAG_REFRESH){
			mCallback.onRequestFailure(false);
		}else if (type == FLAG_LOADMORE){
			mCallback.onRequestFailure(true);
		}
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content, Object extra) {
		int type = (Integer) extra;
		if (type == FLAG_REFRESH){
			mCallback.onAnylizeFailure(false);
		}else if (type == FLAG_LOADMORE){
			mCallback.onAnylizeFailure(true);
		}
	}
	
	private void onGetInfoResult( ResponseDataPacket dataPacket, Object extra){
		PublicType.GetInfoResult object = new PublicType.GetInfoResult();
		
		int type = (Integer) extra;
	
		
		try {
			object.parseJson(dataPacket.data);
			log.i("mDataList.size = " + object.mDataList.size());
			
			if (type == FLAG_REFRESH){
				mContentData = object.mDataList;
				mPage = 0;
				mCallback.onSuccess(false, false);
			}else if (type == FLAG_LOADMORE){
				mContentData.addAll(object.mDataList);
				mPage++;
				if (object.mDataList.size() == 0){
					mCallback.onSuccess(true, true);
				}else{
					mCallback.onSuccess(true, false);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
