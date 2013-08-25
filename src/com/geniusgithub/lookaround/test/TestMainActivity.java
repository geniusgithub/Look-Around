package com.geniusgithub.lookaround.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentAdapter;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.BaseRequestPacket;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestContentCallback;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.proxy.InfoRequestProxy;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.RefreshListView;


public class TestMainActivity extends Activity implements OnClickListener,
											InfoRequestProxy.IRequestResult,
			RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener{

	private static final CommonLog log = LogFactory.createLog();
	

	private Button mBtnGetinfo;
	private RefreshListView mListView;
	private InfoContentAdapter mAdapter;
	
	private List<BaseType.InfoItem> mData = new ArrayList<BaseType.InfoItem>();	
	private InfoRequestProxy mInfoRequestProxy;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setupViews();
		
		initData();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	private void setupViews(){
		setContentView(R.layout.test_main_layout);
		
		mBtnGetinfo = (Button) findViewById(R.id.btnGetInfo);
		mBtnGetinfo.setOnClickListener(this);
		
		mListView = (RefreshListView) findViewById(R.id.listview);
		

	}
	
	private void initData(){
		mAdapter = new InfoContentAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadMoreListener(this);
		
		 BaseType.ListItem item = new BaseType.ListItem();
		 item.mTypeID = "1";
		mInfoRequestProxy = new InfoRequestProxy(this, item, this);
	}

	
	private void updateUI(){
		mAdapter.refreshData(mData);
		
	}
	
	
	@Override
	public void OnRefresh() {
		mInfoRequestProxy.requestRefreshInfo();
	}
	
	@Override
	public void OnLoadMore() {
		mInfoRequestProxy.requestMoreInfo();
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btnGetInfo:
				mInfoRequestProxy.requestRefreshInfo();
		
		}
	}

	@Override
	public void onSuccess(boolean isLoadMore) {
	
		mData = mInfoRequestProxy.getData();
		mAdapter.refreshData(mData);
		
		
		if (isLoadMore){
			mListView.onLoadMoreComplete(false);
		}else{
			mListView.onRefreshComplete();
		}
	}

	@Override
	public void onRequestFailure(boolean isLoadMore) {
	CommonUtil.showToast(R.string.toast_getdata_fail, this);
		
		
		if (isLoadMore){
			mListView.onLoadMoreComplete(false);
		}else{
			mListView.onRefreshComplete();
		}
	}

	@Override
	public void onAnylizeFailure(boolean isLoadMore) {
		CommonUtil.showToast(R.string.toast_getdata_fail, this);

		if (isLoadMore){
			mListView.onLoadMoreComplete(false);
		}else{
			mListView.onRefreshComplete();
		}
	}




}