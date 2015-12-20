package com.geniusgithub.lookaround.content;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.PicGallery;


public class WebViewActivity extends BaseActivity implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";
	
	@InjectView (R.id.btn_back) Button mBtnBack;  
	@InjectView (R.id.webview) WebView mWebView;  
	@InjectView (R.id.show_request_progress_bar) View progressBar;

	
	private ScanWebViewClient mWeiboWebViewClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		
		initView();
		initData(getIntent());
	}

	public void initView()
	{

	    mWebView.setVerticalScrollBarEnabled(false);
	    mWebView.setHorizontalScrollBarEnabled(false);
	    mWebView.requestFocus();
	    
	    WebSettings webSettings = mWebView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		mBtnBack.setOnClickListener(this);
	}

	
	public void initData(Intent intent)
	{
		mWeiboWebViewClient = new ScanWebViewClient();
		mWebView.setWebViewClient(mWeiboWebViewClient);
		
		if (intent != null){
			String url = intent.getStringExtra(INTENT_EXTRA_URL);
			if(url != null){
				mWebView.loadUrl(url);
				log.e("webview url = " + url);
				return ;
			}
		}
		
		log.e("can't find url!!!");
		
	}

	
	private void showProgress()
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	private void hideProgress()
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.INVISIBLE);
			}
		});


	}


	
	
	 private class ScanWebViewClient extends WebViewClient {

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            log.e("shouldOverrideUrlLoading url = " + url);
	            showProgress();
	            view.loadUrl(url);
	            return super.shouldOverrideUrlLoading(view, url);
	        }

	        @Override
	        public void onReceivedError(WebView view, int errorCode, String description,
	                String failingUrl) {
	            
	        	log.e("onReceivedError failingUrl = " + failingUrl);
	            super.onReceivedError(view, errorCode, description, failingUrl);
	        }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        	
	        	log.e("onPageStarted url = " + url);
       	
	            super.onPageStarted(view, url, favicon);

	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	         	log.e("onPageFinished url = " + url);
	        	hideProgress();
	            super.onPageFinished(view, url);
	        }
	        
	    
	 }




	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_back:
				finish();
				break;
		}
	}
	 

	
}
