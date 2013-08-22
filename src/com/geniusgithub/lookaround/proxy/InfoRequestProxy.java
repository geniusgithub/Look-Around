package com.geniusgithub.lookaround.proxy;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;

import android.content.Context;

public class InfoRequestProxy implements IRequestDataPacketCallback{
	
	public static interface IRequestResult{
		public void refreshOnComplete(boolean isSuccess);
		public void loadMoreOnComplete(boolean isSuccess);
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
		getInfoByPage(0);
	}
	
	public void requestMoreInfo(){
		getInfoByPage(mPage + 1);
	}
	

	private void getInfoByPage(int page){
	
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(mContext, mTypeData.mTypeID);
		object.mPage = String.valueOf(page);
		mClientEngine.httpGetRequestEx(PublicType.GET_INFO_MSGID, object, this);
	}

	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket) {
		
		switch(requestAction){	
			case PublicType.GET_INFO_MSGID:{
				onGetInfoResult(dataPacket);
			}
			break;
			
		}
	}

	@Override
	public void onRequestFailure(int requestAction, String content) {
		
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content) {
		
	}
	
	private void onGetInfoResult( ResponseDataPacket dataPacket){
		PublicType.GetInfoResult object = new PublicType.GetInfoResult();
		
		try {
			object.parseJson(dataPacket.data);
			log.e("mDataList.size = " + object.mDataList.size());
			if (isLoadMore){
				mData.addAll(object.mDataList);
				mListView.onLoadMoreComplete(false);

			}else{
				mData = object.mDataList;
				mListView.onRefreshComplete();
			}
		
			curPage = tmpPage;
			updateUI();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
