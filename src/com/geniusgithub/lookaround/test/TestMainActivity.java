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


public class TestMainActivity extends Activity implements OnClickListener, IRequestDataPacketCallback{

	private static final CommonLog log = LogFactory.createLog();
	

	private Button mBtnGetinfo;
	private ListView mListView;
	private InfoContentAdapter mAdapter;
	
	private List<BaseType.InfoItem> mData = new ArrayList<BaseType.InfoItem>();
	
	private ClientEngine mClientEngine;
	
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
		
		mListView = (ListView) findViewById(R.id.listview);

	}
	
	private void initData(){
		mClientEngine=  ClientEngine.getInstance(this);
		
		mAdapter = new InfoContentAdapter(this, mData);
		mListView.setAdapter(mAdapter);
	}

	
	private void refresh(){
		mAdapter.refreshData(mData);
	}
	
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btnGetInfo:
				getInfo();
		
		}
	}
	
	private void getInfo(){
		log.e("getInfo");
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(this, "1");
		
		mClientEngine.httpGetRequestEx(PublicType.GET_INFO_MSGID, object, this);
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
			mData = object.mDataList;
			refresh();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}