package com.geniusgithub.lookaround.activity.set;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentExAdapter;
import com.geniusgithub.lookaround.base.BaseActivityEx;
import com.geniusgithub.lookaround.content.ContentActivity;
import com.geniusgithub.lookaround.content.ContentCache;
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoMaster.DevOpenHelper;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.InfoItemEx;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends BaseActivityEx implements OnItemClickListener, IDialogInterface{
	
	private static final CommonLog log = LogFactory.createLog();

	private Toolbar toolbar;
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

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.collect);
		setSupportActionBar(toolbar);


		final ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
	}
    
    
    @Override
	protected void onDestroy() {

    	db.close();
    	
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.collect_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return  super.onPrepareOptionsMenu(menu);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
			case R.id.menu_delete:
				showDeleteDialog();
				break;
		}

		return super.onOptionsItemSelected(item);
	}



	private void setupViews(){
		initToolBar();

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
	
	
	private Dialog deleteDialog;
	
	private void showDeleteDialog(){
		
		long count = infoItemDao.count();
		if (count == 0){
			CommonUtil.showToast(R.string.toast_no_delcollect, this);	
			return ;
		}
		if (deleteDialog != null){
			deleteDialog.show();
			return ;
		}
		
		deleteDialog = DialogBuilder.buildNormalDialog(this,
									getResources().getString(R.string.dia_msg_delcollect_title),
									getResources().getString(R.string.dia_msg_delcollect_msg),
									this);
		deleteDialog.show();
	}

	@Override
	public void onSure() {
		if (deleteDialog != null){
			deleteDialog.dismiss();
		}	
		
		clear();
	}

	
	@Override
	public void onCancel() {
		if (deleteDialog != null){
			deleteDialog.dismiss();
		}
		
		
		
	}


}
