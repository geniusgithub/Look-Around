package com.geniusgithub.lookaround.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentAdapter;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.widget.RefreshListView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CommonFragmentEx extends CommonFragment implements IRequestDataPacketCallback,
							RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener{

	private BaseType.ListItem mTypeData;
	public View mInvalidView;
	public View mLoadView;
	public View mContentView;
	private RefreshListView mListView;
	
	
	private InfoContentAdapter mAdapter;	
	
	private Context mContext;
	private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();
	
	
	public CommonFragmentEx(BaseType.ListItem data){
		mTypeData = data;
		mContext = getActivity();
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.common_layout, null);
		mInvalidView = view.findViewById(R.id.invalid_view);
		mLoadView = view.findViewById(R.id.load_view);
		mContentView = view.findViewById(R.id.content_view);
		mListView = (RefreshListView) view.findViewById(R.id.listview);
		
		
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		setupViews();
		
		initData();
		
		requestFirstInfo();
	}
	
	private void setupViews(){
		switchToLoadView();
	}
	
	private void initData(){
		mAdapter = new InfoContentAdapter(mContext, mContentData);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadMoreListener(this);
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	
	public BaseType.ListItem getData(){
		return mTypeData;
	}
	
	private void switchToFailView(){
		mInvalidView.setVisibility(View.VISIBLE);
		mLoadView.setVisibility(View.GONE);
		mContentView.setVisibility(View.GONE);
	}
	
	private void switchToLoadView(){
		mInvalidView.setVisibility(View.GONE);
		mLoadView.setVisibility(View.VISIBLE);
		mContentView.setVisibility(View.GONE);
	}
	
	private void switchToContentView(){
		mInvalidView.setVisibility(View.GONE);
		mLoadView.setVisibility(View.GONE);
		mContentView.setVisibility(View.VISIBLE);
	}

	
	private void requestFirstInfo(){
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(mContext, mTypeData.mTypeID);
		
		mClientEngine.httpGetRequestEx(PublicType.GET_INFO_MSGID, object, this);
	}


	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		if (mContentData.size() == 0){
			switchToContentView();
			onGetInfoResult(dataPacket);
			return ;
		}
		
		onGetInfoResult(dataPacket);
		
	}


	@Override
	public void onRequestFailure(int requestAction, String content) {
		log.e("onRequestFailure! requestAction = " + requestAction + "\ncontent = " + content);
		CommonUtil.showToast(R.string.toast_getdata_fail, mContext);
		
		if (mContentData.size() == 0){
			switchToFailView();
			return ;
		}
	}


	@Override
	public void onAnylizeFailure(int requestAction, String content) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		CommonUtil.showToast(R.string.toast_anylizedata_fail, mContext);
		
		if (mContentData.size() == 0){
			switchToFailView();
			return ;
		}
	}


	@Override
	public void OnLoadMore() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void OnRefresh() {
		// TODO Auto-generated method stub
		
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
