package com.geniusgithub.lookaround.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.base.ui.DialogBuilder;
import com.geniusgithub.lookaround.base.ui.IDialogInterface;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.widget.SwitchButton;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

public class BindFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        Handler.Callback, IDialogInterface {

    private SwitchButton mCBSina;
    private SwitchButton mCBTencent;
    private SwitchButton mCBQZone;
    private TextView mTVSina;
    private TextView mTVTencent;
    private TextView mTVQZone;


    private Platform mPlatformSina;
    private Platform mPlatformTencent;
    private Platform mPlatformQZone;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bind_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.sb_sina:
                onSinalCheck(isChecked);
                break;
            case R.id.sb_tencent:
                onTencentCheck(isChecked);
                break;
            case R.id.sb_qzone:
                onQZoneCheck(isChecked);
                break;
        }

    }

    private static final int MSG_ACTION_SUCCESS = 1;
    private static final int MSG_ACTION_FAIL = 2;
    private static final int MSG_ACTION_CANCEL = 3;

    @Override
    public boolean handleMessage(Message msg) {
        Platform platform = (Platform) msg.obj;
        String platName = platform.getName();
        switch(msg.what) {
            case MSG_ACTION_SUCCESS:
                CommonUtil.showToast(R.string.toast_bind_success, getParentActivity());
                updateWeiboStatus(platName);
                break;
            case MSG_ACTION_FAIL:
                updateWeiboStatus(platName);
                break;
            case MSG_ACTION_CANCEL:
                updateWeiboStatus(platName);
                break;
            default:
                break;
        }

        return false;
    }

    private Platform unbindPlatform;
    private Dialog unbindDialog;
    private Dialog getUnbindDialog(){
        Dialog dialog = DialogBuilder.buildNormalDialog(getParentActivity(),
                getResources().getString(R.string.dia_msg_unbind_title),
                getResources().getString(R.string.dia_msg_unbind_msg),
                this);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onSure() {
        if (unbindDialog != null){
            unbindDialog.dismiss();
        }

        if (unbindPlatform == mPlatformSina){
            mPlatformSina.removeAccount();
            updateSinaStatus();
        }else if (unbindPlatform == mPlatformTencent){
            mPlatformTencent.removeAccount();
            updateTencentStatus();
        }else if (unbindPlatform == mPlatformQZone){
            mPlatformQZone.removeAccount();
            updateQZoneStatus();
        }

    }


    @Override
    public void onCancel() {
        if (unbindDialog != null){
            unbindDialog.dismiss();
        }

        if (unbindPlatform == mPlatformSina){
            mCBSina.setChecked(true);
        }else if (unbindPlatform == mPlatformTencent){
            mCBTencent.setChecked(true);
        }else if (unbindPlatform == mPlatformQZone){
            mCBQZone.setChecked(true);
        }
    }

    private void onUIReady(View view){


        mCBSina = (SwitchButton)view.findViewById(R.id.sb_sina);
        mCBTencent = (SwitchButton)view.findViewById(R.id.sb_tencent);
        mCBQZone = (SwitchButton)view.findViewById(R.id.sb_qzone);
        mTVSina = (TextView) view.findViewById(R.id.tv_sina_owner);
        mTVTencent = (TextView)view.findViewById(R.id.tv_tencent_owner);
        mTVQZone = (TextView)view.findViewById(R.id.tv_qzone_owner);


        mCBSina.setOnCheckedChangeListener(this);
        mCBTencent.setOnCheckedChangeListener(this);
        mCBQZone.setOnCheckedChangeListener(this);

        initData();

    }

    private void initData(){
        mPlatformSina = ShareSDK.getPlatform(getParentActivity(), SinaWeibo.NAME);
        mPlatformTencent = ShareSDK.getPlatform(getParentActivity(), TencentWeibo.NAME);
        mPlatformQZone = ShareSDK.getPlatform(getParentActivity(), QZone.NAME);

        updateSinaStatus();
        updateTencentStatus();
        updateQZoneStatus();

    }


    private void updateWeiboStatus(String platName){
        if (platName.equals(SinaWeibo.NAME)){
            updateSinaStatus();
        }else if (platName.equals(TencentWeibo.NAME)){
            updateTencentStatus();
        }else if (platName.equals(QZone.NAME)){
            updateQZoneStatus();
        }
    }


    private void updateSinaStatus(){
        PlatformDb db = mPlatformSina.getDb();
        String nickname = db.get("nickname");
        if (nickname != null && nickname.length() != 0){
            mTVSina.setText(nickname);
            mCBSina.setChecked(true);
        }else{
            mTVSina.setText(getResources().getString(R.string.unbind));
            mCBSina.setChecked(false);
        }
    }


    private void updateTencentStatus(){
        PlatformDb db = mPlatformTencent.getDb();
        String nickname = db.get("nickname");
        if (nickname != null && nickname.length() != 0){
            mTVTencent.setText(nickname);
            mCBTencent.setChecked(true);
        }else{
            mTVTencent.setText(getResources().getString(R.string.unbind));
            mCBTencent.setChecked(false);
        }
    }



    private void updateQZoneStatus(){
        PlatformDb db = mPlatformQZone.getDb();
        String nickname = db.get("nickname");
        if (nickname != null && nickname.length() != 0){
            mTVQZone.setText(nickname);
            mCBQZone.setChecked(true);
        }else{
            mTVQZone.setText(getResources().getString(R.string.unbind));
            mCBQZone.setChecked(false);
        }
    }

    private void onSinalCheck(boolean isChecked){
        if (isChecked){
            if (!mPlatformSina.isValid()){
                mPlatformSina.authorize();
            }
        }else{
            if (mPlatformSina.isValid()){
                unbindPlatform = mPlatformSina;
                getUnbindDialog().show();
            }
        }
    }

    private void onTencentCheck(boolean isChecked){
        if (isChecked){
            if (!mPlatformTencent.isValid()){
                mPlatformTencent.authorize();
            }
        }else{
            if (mPlatformTencent.isValid()){
                unbindPlatform = mPlatformTencent;
                getUnbindDialog().show();
            }
        }
    }

    private void onQZoneCheck(boolean isChecked){
        if (isChecked){
            if (!mPlatformQZone.isValid()){
                mPlatformQZone.authorize();
            }
        }else{
            if (mPlatformQZone.isValid()){
                unbindPlatform = mPlatformQZone;
                getUnbindDialog().show();
            }
        }
    }



}
