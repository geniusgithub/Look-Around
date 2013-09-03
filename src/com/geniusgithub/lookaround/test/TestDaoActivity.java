package com.geniusgithub.lookaround.test;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoMaster.DevOpenHelper;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.InfoItem;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TestDaoActivity extends Activity implements OnClickListener, OnItemClickListener{

private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnAdd;
	private Button mBtnReset;
	private ListView listView;
	
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private InfoItemDao infoItemDao;
    private SQLiteDatabase db;
    
    private Cursor cursor;
    
    
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
		setContentView(R.layout.test_dao_layout);
		
		mBtnAdd = (Button) findViewById(R.id.btnAdd);
		mBtnReset = (Button) findViewById(R.id.btnReset);

		
		mBtnAdd.setOnClickListener(this);
		mBtnReset.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listview);
	}
	
	private void initData(){

        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lookaround-db", null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        infoItemDao = daoSession.getInfoItemDao();

        String idColumn = InfoItemDao.Properties.KEY_ID.columnName;
        String titleColumn = InfoItemDao.Properties.KEY_TITLE.columnName;
        String contentColumn = InfoItemDao.Properties.KEY_CONTENT.columnName;
        String timeColumn = InfoItemDao.Properties.KEY_TIME.columnName;
        String usernameColumn = InfoItemDao.Properties.KEY_USERNAME.columnName;
        log.e("idColumn = " + idColumn + 
        		",titleColumn = " + titleColumn + 
        		",contentColumn = " + contentColumn + 
        		",timeColumn = " + timeColumn + 
        		",usernameColumn = " + usernameColumn);

        String columns[] = infoItemDao.getAllColumns();
        int size = columns.length;
        String newColumns[] = new String[size + 1];
        newColumns[0] = "_id";
        for(int i = 0; i < size; i++){
        	newColumns[i + 1] = columns[i];
        }
   
        cursor = db.query(infoItemDao.getTablename(), newColumns, null, null, null, null, null);
        String[] from = {idColumn, titleColumn};
        int[] to = { android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,
                to);
        
        listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnAdd:
				add();
				break;
			case R.id.btnReset:
				reset();
				break;
		}
	}
	
	private void add(){
		
	}
	
	private void reset(){
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
		BaseType.InfoItem item = (InfoItem) adapterView.getItemAtPosition(pos);
		
		infoItemDao.deleteByKey(item.mKeyID);
	    Log.d("TestDaoActivity", "Deleted note, ID: " + item.mKeyID);
	    cursor.requery();
	}

}
