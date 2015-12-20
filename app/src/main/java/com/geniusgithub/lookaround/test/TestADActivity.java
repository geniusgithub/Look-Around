package com.geniusgithub.lookaround.test;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.ListView;

import com.geniusgithub.lookaround.R;
import com.umeng.newxp.common.ExchangeConstants;
import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;
import com.umeng.newxp.view.GridTemplateConfig;

public class TestADActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_ad_layout);
		
		setupViews();
		
		initData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
	private void setupViews(){
		ExchangeConstants.CONTAINER_AUTOEXPANDED = false;

		ViewGroup fatherLayout = (ViewGroup) findViewById(R.id.ad);
		ListView listView = (ListView) findViewById(R.id.list);

		ExchangeDataService exchangeDataService = new ExchangeDataService("");
		exchangeDataService.setTemplate(1);
		ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this, exchangeDataService);
		exchangeViewManager.setGridTemplateConfig(new GridTemplateConfig().setMaxPsize(9).setNumColumns(3).setVerticalSpacing(13));
		exchangeViewManager.addView(fatherLayout, listView);
		
	}
	
	private void initData(){
		
	}

}
