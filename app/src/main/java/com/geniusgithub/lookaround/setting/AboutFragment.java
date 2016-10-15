package com.geniusgithub.lookaround.setting;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.BaseRequestPacket;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends BaseFragment implements View.OnClickListener,
                                                            IRequestDataPacketCallback,
                                                            IDialogInterface{
    private static final CommonLog log = LogFactory.createLog();

    @BindView(R.id.ll_advise)
    public  View mAdviseView;

    @BindView(R.id.ll_attention)
    public View mAttentionWeiboView;

    @BindView(R.id.ll_checkupdate)
    public View mCheckUpdateView;

    @BindView(R.id.iv_updateicon)
    public ImageView mIVUpageIcon;

    @BindView(R.id.iv_logo)
    public ImageView mLogoIcon;

    @BindView(R.id.tv_version)
    public TextView mTVVersion;

    private ClientEngine mClientEngine;
    private PublicType.CheckUpdateResult object = null;

    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abount_fragment_layout, container, false);
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
            case R.id.ll_advise:
                goAdviseActivity();
                break;
            case R.id.ll_attention:
                attention();
                break;
            case R.id.ll_checkupdate:
                checkUpdate();
                break;
        }
    }


    @Override
    public void onSuccess(int requestAction, ResponseDataPacket dataPacket,
                          Object extra) {
        log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());

        switch(requestAction){
            case PublicType.CHECK_UPDATE_MSGID:
                onGetCheckUpdate(dataPacket, extra);
                break;

        }
    }


    @Override
    public void onRequestFailure(int requestAction, String content, Object extra) {
        log.e("onRequestFailure --> requestAction = " + requestAction);

        CommonUtil.showToast(R.string.toast_getdata_fail, getParentActivity());
    }


    @Override
    public void onAnylizeFailure(int requestAction, String content, Object extra) {
        log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);

    }

    @Override
    public void onSure() {
        if (updateDialog != null){
            updateDialog.dismiss();
        }

        log.e("object:" + object);
        if (object != null){
            Intent intents = new Intent(Intent.ACTION_VIEW);
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents.setData(Uri.parse(object.mAppUrl));
            startActivity(intents);
            log.e("jump to url:" + object.mAppUrl);
        }

    }


    @Override
    public void onCancel() {
        if (updateDialog != null){
            updateDialog.dismiss();
        }
    }


    private void onUIReady(View view){
        ButterKnife.bind(this, view);

        mAttentionWeiboView.setOnClickListener(this);
        mCheckUpdateView.setOnClickListener(this);
        mAdviseView.setOnClickListener(this);

        initData();

    }

    private void initData(){
        mClientEngine=  ClientEngine.getInstance(getParentActivity());

        object = LAroundApplication.getInstance().getNewVersion();
        if (object != null && object.mHaveNewVer != 0){
            showUpdateIcon(true);
        }else{
            showUpdateIcon(false);

            PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(getParentActivity());
            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = PublicType.CHECK_UPDATE_MSGID;
            packet.object = object;
            packet.extra = new Object();
            mClientEngine.httpGetRequestEx(packet, this);
        }


        updateView();

        mHandler = new Handler();
    }

    private void showUpdateIcon(boolean flag){
        if (flag){
            mIVUpageIcon.setVisibility(View.VISIBLE);
        }else{
            mIVUpageIcon.setVisibility(View.GONE);
        }
    }

    private void updateView(){

        String value = getResources().getString(R.string.tvt_ver_pre) + CommonUtil.getSoftVersion(getParentActivity());
        mTVVersion.setText(value);
    }

    private void goAdviseActivity(){
        Intent intent = new Intent();
        intent.setClass(getParentActivity(), AdviseActivity.class);
        startActivity(intent);
    }

    private void attention(){
        Toast.makeText(getParentActivity(), "功能暂时屏蔽，敬请谅解", Toast.LENGTH_SHORT).show();
		/*Platform plat = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		plat.setPlatformActionListener(this);
		plat.followFriend("2881812642");*/
    }


    private void checkUpdate(){

        if (object != null && object.mHaveNewVer != 0){
            if (updateDialog != null){
                updateDialog.dismiss();
            }

            updateDialog = getUpdateDialog(object.mContentList);
            updateDialog.show();
        }else{
            PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(getParentActivity());


            BaseRequestPacket packet = new BaseRequestPacket();
            packet.action = PublicType.CHECK_UPDATE_MSGID;
            packet.object = object;

            mClientEngine.httpGetRequestEx(packet, this);
            CommonUtil.showToast(R.string.toast_checking_update, getParentActivity());
        }


    }


    private Dialog updateDialog;
    private void onGetCheckUpdate( ResponseDataPacket dataPacket, Object extra){
        object = new PublicType.CheckUpdateResult();

        try {
            object.parseJson(dataPacket.data);
            //	log.e("mHaveNewVer = " + object.mHaveNewVer +  "\nmVerCode = " + object.mVerCode +
            //			"\nmVerName = " + object.mVerName + "\nmAppUrl = " + object.mAppUrl + "\nmContent.size = " + object.mContentList.size());
        } catch (JSONException e) {
            e.printStackTrace();
            CommonUtil.showToast(R.string.toast_anylizedata_fail, getParentActivity());
            object = null;
            return ;
        }

        if (object.mHaveNewVer != 0){
            showUpdateIcon(true);
            if (extra == null){
                if (updateDialog != null){
                    updateDialog.dismiss();
                }

                updateDialog = getUpdateDialog(object.mContentList);
                updateDialog.show();
            }
            LAroundApplication.getInstance().setNewVersionFlag(object);
        }else{
            if (extra == null){
                CommonUtil.showToast(R.string.toast_no_update, getParentActivity());
            }

        }



    }

    private Dialog getUpdateDialog(List<String > list){
        int size = list.size();
        StringBuffer sBuffer = new StringBuffer();
        for(int i = 0; i < size; i++){
            String value = String.valueOf(i + 1) + "." + list.get(i);
            sBuffer.append(value);
            if (i != size - 1){
                sBuffer.append("\n");
            }
        }
        log.e("msg = " + sBuffer.toString());
        Dialog dialog = DialogBuilder.buildNormalDialog(getParentActivity(), "版本更新" + object.mVerName, sBuffer.toString(), this);
        return dialog;
    }




}
