package com.geniusgithub.lookaround;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NavigationViewEx extends LinearLayout implements View.OnClickListener{

    public static interface INavClickListener{
        public void onSettingClick();
    }

    private Context mContext;
    private View mRootView;

    private ListView mNavListview;
    private View mSettingView;

    private INavClickListener mNavListener;

    public NavigationViewEx(Context context) {
        super(context);
    }

    public NavigationViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        mRootView = LayoutInflater.from(context).inflate(R.layout.navigation_layout, this,true);
    }

    public void setmNavListener(INavClickListener listener){
        mNavListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    private void initView(){
        mNavListview = (ListView) mRootView.findViewById(R.id.nav_listview);
        mSettingView  = mRootView.findViewById(R.id.ll_bottom);
        mSettingView.setOnClickListener(this);
    }


    public ListView getmNavListview(){
        return mNavListview;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_bottom:
                if (mNavListener != null){
                    mNavListener.onSettingClick();
                }
                break;
        }
    }
}
