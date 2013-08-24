package com.geniusgithub.lookaround.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentAdapter;
import com.geniusgithub.lookaround.content.ContentActivity;
import com.geniusgithub.lookaround.content.ContentCache;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.InfoItem;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.proxy.InfoRequestProxy;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.widget.RefreshListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public  class CommonFragmentEx extends CommonFragment implements InfoRequestProxy.IRequestResult,
							RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener,
							OnItemClickListener{

	private BaseType.ListItem mTypeData;
	public View mInvalidView;
	public View mLoadView;
	public View mContentView;
	private RefreshListView mListView;
	private InfoContentAdapter mAdapter;	
	
	
	private Context mContext;
	private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();
	private InfoRequestProxy mInfoRequestProxy;
	
	private Handler mHandler;
	public CommonFragmentEx(BaseType.ListItem data){
		mTypeData = data;	
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
		
		mInfoRequestProxy.requestRefreshInfo();
	
	}
	
	private void setupViews(){
		switchToLoadView();
	
	}
	
	private void initData(){

		mContext = getActivity();
	
		mAdapter = new InfoContentAdapter(mContext, mContentData);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadMoreListener(this);
		mListView.setOnItemClickListener(this);
		mInfoRequestProxy = new InfoRequestProxy(mContext, mTypeData, this);
		
		mHandler = new Handler(){
			
		};
	}

	@Override
	public void onDestroy() {
		
		mInfoRequestProxy.cancelRequest();
	
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


	@Override
	public void OnLoadMore() {
		mInfoRequestProxy.requestMoreInfo();
	}


	@Override
	public void OnRefresh() {
		mInfoRequestProxy.requestRefreshInfo();
	}


	@Override
	public void onSuccess(boolean isLoadMore) {

		switchToContentView();
		mContentData = mInfoRequestProxy.getData();
		mAdapter.refreshData(mContentData);
		
		
		if (isLoadMore){
			mListView.onLoadMoreComplete(false);
		}else{
			mListView.onRefreshComplete();
		}
	}


	@Override
	public void onRequestFailure(boolean isLoadMore) {
		CommonUtil.showToast(R.string.toast_getdata_fail, mContext);
		
		if (mContentData.size() == 0){
			switchToFailView();
			return ;
		}
		
		switchToContentView();
		if (isLoadMore){
			mListView.onLoadMoreComplete(false);
		}else{
			mListView.onRefreshComplete();
		}
	}


	@Override
	public void onAnylizeFailure(boolean isLoadMore) {
		CommonUtil.showToast(R.string.toast_anylizedata_fail, mContext);
		
		if (mContentData.size() == 0){
			switchToFailView();
			return ;
		}
		
		switchToContentView();
		if (isLoadMore){
			mListView.onLoadMoreComplete(false);
		}else{
			mListView.onRefreshComplete();
		}
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {

		BaseType.InfoItem item = (InfoItem) adapter.getItemAtPosition(pos);
		ContentCache.getInstance().setTypeItem(mTypeData);
		ContentCache.getInstance().setInfoItem(item);
		
		goContentActivity();
	}
	

	private void goContentActivity(){
		Intent intent = new Intent();
		intent.setClass(mContext, ContentActivity.class);
		startActivity(intent);
	}
	
	
}
