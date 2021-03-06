package com.geniusgithub.lookaround.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.collection.CollectionActivity;
import com.geniusgithub.lookaround.component.CacheManager;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.ll_bindaccount)
    public  View mBindView;

    @BindView(R.id.ll_mypush)
    public View mMyPushView;

    @BindView(R.id.ll_mycollect)
    public View mMyCollectView;

    @BindView(R.id.ll_clearcache)
    public View mClieaCacheView;

    @BindView(R.id.ll_about)
    public View mAboutView;

    @BindView(R.id.iv_updateicon)
    public ImageView mIVUpageIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_bindaccount:
                goBindActivity();
                break;
            case R.id.ll_mypush:
                CommonUtil.showToast(R.string.toast_no_push, getParentActivity());
                break;
            case R.id.ll_mycollect:
                goCollectActivity();
                break;
            case R.id.ll_clearcache:
                clearCache();
                break;
            case R.id.ll_about:
                goAboutActivity();
                break;
        }
    }

    private void onUIReady(View view){
        ButterKnife.bind(this, view);

        mBindView.setOnClickListener(this);
        mMyPushView.setOnClickListener(this);
        mMyCollectView.setOnClickListener(this);
        mClieaCacheView.setOnClickListener(this);
        mAboutView.setOnClickListener(this);

        initData();

    }

    private void initData(){
        PublicType.UserLoginResult object = LAroundApplication.getInstance().getUserLoginResult();
        if (object.mHaveNewVer != 0){
            mIVUpageIcon.setImageResource(R.drawable.app_new);
        }
    }


    private void goBindActivity(){
        Toast.makeText(getParentActivity(), "功能暂时屏蔽，敬请谅解", Toast.LENGTH_SHORT).show();
     /*   Intent intent = new Intent();
        intent.setClass(getParentActivity(), BindActivity.class);
        startActivity(intent);*/
    }

    private void goCollectActivity(){
        Intent intent = new Intent();
        intent.setClass(getParentActivity(), CollectionActivity.class);
        startActivity(intent);
    }

    private void goAboutActivity(){
        Intent intent = new Intent();
        intent.setClass(getParentActivity(), AboutActivity.class);
        startActivity(intent);
    }

    private void clearCache(){
        LAroundApplication.getInstance().onEvent("UM004");
        CommonUtil.showToast(R.string.toast_clear_success, getParentActivity());

        CacheManager.getInstance().clearCache();
    }


}
