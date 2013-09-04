package com.geniusgithub.lookaround.activity.set;

import java.util.ArrayList;
import java.util.List;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentExAdapter;
import com.geniusgithub.lookaround.content.ContentActivity;
import com.geniusgithub.lookaround.content.ContentCache;
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.datastore.DaoMaster.DevOpenHelper;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.InfoItemEx;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CollectActivity extends Activity implements OnClickListener, OnItemClickListener{
	
	private static final CommonLog log = LogFactory.createLog();
	private Button mBtnBack;
	private Button mBtnClear;
	private TextView mTVTitle;
	private ListView mListView;

	private InfoContentExAdapter mAdapter;	
	private List<BaseType.InfoItemEx> mContentData = new ArrayList<BaseType.InfoItemEx>();
	
	
	
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private InfoItemDao infoItemDao;
    private SQLiteDatabase db;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);
        
        setupViews();
        initData();
    }
    
    
    
    
    @Override
	protected void onDestroy() {

    	db.close();
    	
		super.onDestroy();
	}




	private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	mBtnClear = (Button) findViewById(R.id.btn_right);
    	mBtnClear.setOnClickListener(this);
    	
    	mTVTitle = (TextView) findViewById(R.id.tv_bartitle);
    	
    	mListView = (ListView) findViewById(R.id.listview);
    	mListView.setOnItemClickListener(this);
    }
    
    private void initData(){
    	
    	mAdapter = new InfoContentExAdapter(this, mContentData);
    	mListView.setAdapter(mAdapter);
    	
    	inidDataBase();
    	
    	refreshData();
    }
    
    private void inidDataBase(){
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lookaround-db", null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        infoItemDao = daoSession.getInfoItemDao();


	}

	private void refreshData(){

		mContentData = infoItemDao.loadAll();
		log.e("load all size = " + mContentData.size());
//		int size = mContentData.size();
//		for(int i = 0; i < size; i++){
//			log.e("index = " + i + ", mContentData[0] = \n" + mContentData.get(i).toString());
//		}
		
		mAdapter.refreshData(mContentData);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_right:
				clear();
				break;
		}
	}

	

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {

		BaseType.InfoItemEx item = (InfoItemEx) adapter.getItemAtPosition(pos);
		ContentCache.getInstance().setTypeItem(item.mType);
		ContentCache.getInstance().setInfoItem(item);
		
		goContentActivity();
	}

	private void clear(){
		infoItemDao.deleteAll();
		refreshData();
	}

	private void goContentActivity(){
		Intent intent = new Intent();
		intent.setClass(this, ContentActivity.class);
		startActivity(intent);
	}
}
