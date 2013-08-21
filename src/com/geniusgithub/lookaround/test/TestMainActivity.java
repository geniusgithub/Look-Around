package com.geniusgithub.lookaround.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentAdapter;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestContentCallback;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.RefreshListView;


public class TestMainActivity extends Activity implements OnClickListener, IRequestDataPacketCallback,
			RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener{

	private static final CommonLog log = LogFactory.createLog();
	

	private Button mBtnGetinfo;
	private RefreshListView mListView;
	private InfoContentAdapter mAdapter;
	
	private List<BaseType.InfoItem> mData = new ArrayList<BaseType.InfoItem>();
	
	private ClientEngine mClientEngine;
	
	private int curPage = 0;
	private int tmpPage = 0;
	private boolean isLoadMore = false;
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
		mClientEngine=  ClientEngine.getInstance(this);
		
		mAdapter = new InfoContentAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadMoreListener(this);
	}

	
	private void updateUI(){
		mAdapter.refreshData(mData);
		
	}
	
	
	@Override
	public void OnRefresh() {
		isLoadMore = false;
		getInfo(0);
	}
	
	@Override
	public void OnLoadMore() {
		isLoadMore = true;
		getInfo(curPage + 1);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btnGetInfo:
				getInfo(0);
		
		}
	}
	
	private void getInfo(int page){
		log.e("getInfo");
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(this, "1");
		object.mPage = String.valueOf(page);
		mClientEngine.httpGetRequestEx(PublicType.GET_INFO_MSGID, object, this);
		tmpPage = page;
	}
	
	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		switch(requestAction){	
			case PublicType.GET_INFO_MSGID:{
				onGetInfoResult(dataPacket);
			}
				break;
				
		}
	}

	@Override
	public void onRequestFailure(int requestAction, String content) {
	//	log.e("onRequestFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
		
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
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