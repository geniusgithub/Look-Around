package com.geniusgithub.lookaround.detailcontent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.SatelliteMenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.base.ToolbarFragmentActivity;
import com.geniusgithub.lookaround.cache.SimpleImageLoader;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.google.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public  class DetailFragment extends BaseFragment {

    private static final CommonLog log = LogFactory.createLog();

    private View mRootView;
    private DetailPresenter mDetailPresenter;
    private DetailContract.IView mDetailView;


    public DetailFragment(){
    
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.detail_fragment_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.content_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.menu_collect);
        item.setVisible(!mDetailPresenter.isCollect());

        super.onPrepareOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_collect:
                mDetailPresenter.collect();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        mDetailPresenter.onUiDestroy();
        super.onDestroy();
    }


    private void onUIReady(View view){
        mRootView = view.findViewById(R.id.ll_rootview);

        mDetailPresenter = new DetailPresenter();
        mDetailView = new DetailView(getActivity());
        mDetailView.setupView(mRootView);
        mDetailPresenter.bindView(mDetailView);
        mDetailPresenter.onUiCreate(getActivity());
    }



    private class DetailView implements DetailContract.IView,  View.OnClickListener,
            android.view.ext.SatelliteMenu.SateliteClickedListener{

        private Context mContext;
        private DetailContract.IPresenter mPresenter;
        private View mRootView;

        private final static int SINA_ID = 1;
        private final static int TENCENT_ID = 2;
        private final static int WECHAT_ID = 3;
        private final static int WECHAT_MOM_ID = 4;
        private final static int QZONE = 5;


        private Button mBtnReadOrign;
        private TextView mTVTitle;
        private TextView mTVArtist;
        private TextView mTVContent;
        private TextView mTVTime;
        private TextView mTVSource;
        private ImageView mIVContent;
        private android.view.ext.SatelliteMenu SatelliteMenu;
        private AdView adView;


        private SimpleImageLoader mImageLoader;

        public DetailView(Context context){
            mContext = context;
        }

        @Override
        public void bindPresenter(DetailContract.IPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void setupView(View view) {
            mRootView = view;
            mBtnReadOrign = (Button) view.findViewById(R.id.btn_readorign);
            mTVTitle = (TextView) view.findViewById(R.id.tv_title);
            mTVArtist = (TextView) view.findViewById(R.id.tv_artist);
            mTVContent = (TextView) view.findViewById(R.id.tv_content);
            mTVTime = (TextView) view.findViewById(R.id.tv_time);
            mTVSource = (TextView) view.findViewById(R.id.tv_source);
            mIVContent = (ImageView) view.findViewById(R.id.iv_content);
            SatelliteMenu = (android.view.ext.SatelliteMenu) view.findViewById(R.id.SatelliteMenu);
            adView = (AdView) view.findViewById(R.id.adView);
            mBtnReadOrign.setOnClickListener(this);
            mIVContent.setOnClickListener(this);

            mImageLoader = new SimpleImageLoader(getParentActivity());
            List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
            items.add(new SatelliteMenuItem(SINA_ID, R.drawable.logo_sina));
            items.add(new SatelliteMenuItem(TENCENT_ID, R.drawable.logo_tencentweibo));
            items.add(new SatelliteMenuItem(WECHAT_ID, R.drawable.logo_wechat));
            items.add(new SatelliteMenuItem(WECHAT_MOM_ID, R.drawable.logo_wechatmoments));
            items.add(new SatelliteMenuItem(QZONE, R.drawable.logo_qzone));
            SatelliteMenu.addItems(items);
            SatelliteMenu.setOnItemClickedListener(this);

        }

        @Override
        public  void updateToolTitle(String title){
            if ( getParentActivity() instanceof ToolbarFragmentActivity){
                ((ToolbarFragmentActivity) getParentActivity()).updateToolTitle(title);
            }
        }

        @Override
        public void updateInfoItemEx(BaseType.InfoItemEx object) {
            mTVTitle.setText(object.mTitle);
            mTVArtist.setText(object.mUserName);
            mTVContent.setText(object.mContent);
            mTVTime.setText(object.mTime);
            mTVContent.setText(object.mContent);
            mImageLoader.DisplayImage(object.getImageURL(0), mIVContent);
            if (object.getThumnaiImageCount() == 0){
                mIVContent.setVisibility(View.GONE);
            }
        }

        @Override
        public void onDestroy() {

            if (adView != null){
                adView.destroy();
            }
        }

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.btn_readorign:
                   mPresenter.enterWebView();
                    break;
                case R.id.iv_content:
                    mPresenter.enterPhoneView();
                    break;

            }
        }



        @Override
        public void eventOccured(int id) {
            log.i("Clicked on " + id);

            switch(id){
                case SINA_ID:
                     mPresenter.shareToSina();
                    break;
                case TENCENT_ID:
                    mPresenter.shareToTencent();
                    break;
                case WECHAT_ID:
                    mPresenter.shareToWChat();
                    break;
                case WECHAT_MOM_ID:
                    mPresenter.shareToWFriend();
                    break;
                case QZONE:
                    mPresenter.shareToQZone();
                    break;
            }
        }

    }



}