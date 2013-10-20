package com.geniusgithub.lookaround.activity.set;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.util.CommonUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity implements OnClickListener{

	private Button mBtnBack;
	private TextView mTVBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abount_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	mTVBottom = (TextView) findViewById(R.id.tv_bottom);
    }
    
    private void initData(){
    	updateView();
    }


	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
		}
	}
	
	private void updateView(){
		
		String value = getResources().getString(R.string.tvt_ver_pre) + CommonUtil.getSoftVersion(this);
		mTVBottom.setText(value);
	}

}
