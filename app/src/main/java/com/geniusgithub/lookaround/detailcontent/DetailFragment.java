package com.geniusgithub.lookaround.detailcontent;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.geniusgithub.lookaround.component.ImageLoader;
import com.geniusgithub.lookaround.model.BaseType;
import com.google.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class DetailFragment extends BaseFragment {


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



    public class DetailView implements DetailContract.IView,  View.OnClickListener,
            android.view.ext.SatelliteMenu.SateliteClickedListener{

        private Context mContext;
        private DetailContract.IPresenter mPresenter;
        private View mRootView;

        @BindView(R.id.btn_readorign)
        public Button mBtnReadOrign;

        @BindView(R.id.tv_title)
        public TextView mTVTitle;

        @BindView(R.id.tv_artist)
        public TextView mTVArtist;

        @BindView(R.id.tv_content)
        public TextView mTVContent;

        @BindView(R.id.tv_time)
        public TextView mTVTime;

        @BindView(R.id.tv_source)
        public TextView mTVSource;

        @BindView(R.id.iv_content)
        public ImageView mIVContent;

        @BindView(R.id.SatelliteMenu)
        public android.view.ext.SatelliteMenu SatelliteMenu;

        @BindView(R.id.adView)
        public AdView adView;

        private final static int SINA_ID = 1;
        private final static int TENCENT_ID = 2;
        private final static int WECHAT_ID = 3;
        private final static int WECHAT_MOM_ID = 4;
        private final static int QZONE = 5;

        private Drawable mPlaceHolder;

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
            ButterKnife.bind(this, view);

            mBtnReadOrign.setOnClickListener(this);
            mIVContent.setOnClickListener(this);

            List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
            items.add(new SatelliteMenuItem(SINA_ID, R.drawable.logo_sina));
            items.add(new SatelliteMenuItem(TENCENT_ID, R.drawable.logo_tencentweibo));
            items.add(new SatelliteMenuItem(WECHAT_ID, R.drawable.logo_wechat));
            items.add(new SatelliteMenuItem(WECHAT_MOM_ID, R.drawable.logo_wechatmoments));
            items.add(new SatelliteMenuItem(QZONE, R.drawable.logo_qzone));
            SatelliteMenu.addItems(items);
            SatelliteMenu.setOnItemClickedListener(this);

         //   mPlaceHolder = mContext.getResources().getDrawable(R.drawable.load_img);
            mPlaceHolder = null;
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
            ImageLoader.loadSource(mContext, object.getImageURL(0), mIVContent, mPlaceHolder);
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