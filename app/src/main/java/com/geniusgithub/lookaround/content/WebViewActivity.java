package com.geniusgithub.lookaround.content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseActivityEx;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;


public class WebViewActivity extends BaseActivityEx {

	private static final CommonLog log = LogFactory.createLog();
	
	public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";


	private Toolbar toolbar;
	private WebView mWebView;
	private View progressBar;

	
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
		initToolBar();


		mWebView = (WebView) findViewById(R.id.webView);
		progressBar = findViewById(R.id.show_request_progress_bar);

	    mWebView.setVerticalScrollBarEnabled(false);
	    mWebView.setHorizontalScrollBarEnabled(false);
	    mWebView.requestFocus();
	    
	    WebSettings webSettings = mWebView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

	}

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.page);
		setSupportActionBar(toolbar);

		final ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
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



	
}
