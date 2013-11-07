package com.geniusgithub.lookaround.activity.set;

import com.geniusgithub.lookaround.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SupportActivity extends Activity implements OnClickListener{

	private Button mBtnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    }
    
    private void initData(){

    }


	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
		}
	}
	
}
