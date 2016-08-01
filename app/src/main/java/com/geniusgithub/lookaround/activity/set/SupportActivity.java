package com.geniusgithub.lookaround.activity.set;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;

import roboguice.inject.InjectView;

public class SupportActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

	@InjectView (R.id.btn_back) Button mBtnBack;  

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_layout);
        
        setupViews();
        initData();
    }
    
    private void setupViews(){
    	mBtnBack.setOnClickListener(this);
    }
    
    private void initData(){
/*    	ExchangeConstants.CONTAINER_AUTOEXPANDED = false;

		ViewGroup fatherLayout = (ViewGroup) findViewById(R.id.ad);
		ListView listView = (ListView) findViewById(R.id.list);

		ExchangeDataService exchangeDataService = new ExchangeDataService("");
		exchangeDataService.setTemplate(1);
		ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this, exchangeDataService);
		exchangeViewManager.setGridTemplateConfig(new GridTemplateConfig().setMaxPsize(9).setNumColumns(3).setVerticalSpacing(13));
		exchangeViewManager.addView(fatherLayout, listView);
		
		listView.setOnItemClickListener(this);*/
    }


	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int pos, long arg3) {
		Toast.makeText(this, "pos = " + pos, Toast.LENGTH_SHORT).show();
	}
	
}
