package com.geniusgithub.lookaround.fragment;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommonFragmentEx extends CommonFragment implements IRequestDataPacketCallback{

	private BaseType.ListItem mData;
	public View mLoadView;
	
	private ClientEngine mClientEngine;
	private Context mContext;
	
	public CommonFragmentEx(BaseType.ListItem data){
		mData = data;
		mContext = getActivity();
		mClientEngine=  ClientEngine.getInstance(mContext);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.common_layout, null);
		mLoadView = view.findViewById(R.id.load_view);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		setupViews();
		
		initData();
	}
	
	private void setupViews(){
	
	}
	
	private void initData(){
		requestInfo();
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	
	public BaseType.ListItem getData(){
		return mData;
	}
	
	
	
	private void showLoadView(boolean show){
		mLoadView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	

	
	private void requestInfo(){
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(mContext, mData.mTypeID);
		
		mClientEngine.httpGetRequestEx(PublicType.GET_INFO_MSGID, object, this);
	}


	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		showLoadView(false);
	}


	@Override
	public void onRequestFailure(int requestAction, String content) {
		log.e("onRequestFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
		showLoadView(false);
	}


	@Override
	public void onAnylizeFailure(int requestAction, String content) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
		showLoadView(false);
	}
	
	
	
	
}
