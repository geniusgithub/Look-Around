package com.geniusgithub.lookaround.detailcontent.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewFragment extends BaseFragment {

    public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";

    @BindView(R.id.webView)
    public WebView mWebView;

    @BindView(R.id.show_request_progress_bar)
    public View progressBar;

    private ScanWebViewClient mWeiboWebViewClient;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
    }




    private void onUIReady(View view){
        ButterKnife.bind(this, view);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.requestFocus();

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        initData(getParentActivity().getIntent());

    }

    private void initData(Intent intent){
        mWeiboWebViewClient = new ScanWebViewClient();
        mWebView.setWebViewClient(mWeiboWebViewClient);

        if (intent != null){
            String url = intent.getStringExtra(INTENT_EXTRA_URL);
            if(url != null){
                mWebView.loadUrl(url);
                return ;
            }
        }


    }

    private void showProgress()
    {
        getParentActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void hideProgress()
    {
        getParentActivity().runOnUiThread(new Runnable() {

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

            showProgress();
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            hideProgress();
            super.onPageFinished(view, url);
        }


    }

}
