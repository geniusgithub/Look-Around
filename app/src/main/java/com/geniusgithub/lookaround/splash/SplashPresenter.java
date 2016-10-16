package com.geniusgithub.lookaround.splash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.maincontent.main.MainActivity;
import com.geniusgithub.lookaround.datastore.LocalConfigSharePreference;
import com.geniusgithub.lookaround.base.ui.IDialogInterface;
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

public class SplashPresenter implements SplashContract.IPresenter, IRequestDataPacketCallback, IDialogInterface{

    private static final CommonLog log = LogFactory.createLog();

    public static interface ISplashCallback{
        public void onExit();
    }

    private Context mParentActivity;
    private SplashContract.IView mView;
    private ISplashCallback mSplashCallback;


    private LAroundApplication mApplication;
    private ClientEngine mClientEngine;
    private PublicType.UserLoginResult object = null;

    public SplashPresenter(){
        mApplication = LAroundApplication.getInstance();
        mClientEngine = ClientEngine.getInstance(mApplication);
    }

    @Override
    public void bindView(SplashContract.IView view) {
        mView = view;
        mView.bindPresenter(this);
    }

    @Override
    public void unBindView() {
        mView = null;
    }

    @Override
    public void onUpdateSure() {
        mView.closeForceUpdateDialog();

        log.e("object:" + object);
        if (object != null){
            Intent intents = new Intent(Intent.ACTION_VIEW);
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents.setData(Uri.parse(object.mAppUrl));
            mParentActivity.startActivity(intents);
            log.e("jump to url:" + object.mAppUrl);
        }

    }

    @Override
    public void onUpdateCancel() {
        mView.closeForceUpdateDialog();
        mSplashCallback.onExit();
    }

    @Override
    public void onSuccess(int requestAction, ResponseDataPacket dataPacket, Object extra) {
        switch (requestAction) {
            case PublicType.USER_REGISTER_MASID:
                onTransdelRegister(dataPacket);
                break;
            case PublicType.USER_LOGIN_MASID:
                onTransdelLogin(dataPacket);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestFailure(int requestAction, String content, Object extra) {
        log.e("SplashPresenter --> onRequestFailure \nrequestAction = " + requestAction + "\ncontent = " + content);

        switch (requestAction) {
            case PublicType.USER_REGISTER_MASID:
                CommonUtil.showToast(R.string.toast_register_fail, mParentActivity);
                exit();
                break;
            case PublicType.USER_LOGIN_MASID:
                CommonUtil.showToast(R.string.toast_login_fail, mParentActivity);
                exit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnylizeFailure(int requestAction, String content, Object extra) {
        log.e("SplashPresenter --> onAnylizeFailure \nrequestAction = " + requestAction + "\ncontent = " + content);

        switch (requestAction) {
            case PublicType.USER_REGISTER_MASID:
                CommonUtil.showToast(R.string.toast_register_fail, mParentActivity);
                exit();
                break;
            case PublicType.USER_LOGIN_MASID:
                CommonUtil.showToast(R.string.toast_login_fail, mParentActivity);
                exit();
                break;
            default:
                break;
        }
    }


    @Override
    public void onSure() {
        mView.closeForceUpdateDialog();

        log.i("object:" + object);
        if (object != null){
            Intent intents = new Intent(Intent.ACTION_VIEW);
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents.setData(Uri.parse(object.mAppUrl));
            mParentActivity.startActivity(intents);
            log.i("jump to url:" + object.mAppUrl);
        }
    }

    @Override
    public void onCancel() {
        mView.closeForceUpdateDialog();

        exit();
    }


    public void attachActivity(Context activity, ISplashCallback callback){
        mParentActivity = activity;
        mSplashCallback = callback;
    }

    public boolean requestRegister(){
        if (!CommonUtil.isNetworkConnect(mParentActivity)){
            CommonUtil.showToast(R.string.toast_connet_fail, mParentActivity);
            return false;
        }

        String keys = LocalConfigSharePreference.getKeys(mParentActivity);
        if (keys.equals("")){
            PublicType.UserRegister object = PublicTypeBuilder.buildUserRegister(mParentActivity);


            BaseRequestPacket packet = new BaseRequestPacket();
            packet.context = mParentActivity;
            packet.action = PublicType.USER_REGISTER_MASID;
            packet.object = object;

            mClientEngine.httpGetRequestEx(packet, this);
        }else{
            return requestLogin(keys);
        }

        return true;
    }

    public void cancelTask(Context context){
        mClientEngine.cancelTask(context);
    }

    private boolean requestLogin(String keys){
        if (!CommonUtil.isNetworkConnect(mParentActivity)){
            CommonUtil.showToast(R.string.toast_connet_fail, mParentActivity);
            return false;
        }
        PublicType.UserLogin object = PublicTypeBuilder.buildUserLogin(mParentActivity, keys);

        BaseRequestPacket packet = new BaseRequestPacket();
        packet.context = mParentActivity;
        packet.action = PublicType.USER_LOGIN_MASID;
        packet.object = object;


        mClientEngine.httpGetRequestEx(packet, this);

        return true;
    }

    private void onTransdelRegister(ResponseDataPacket dataPacket){
        log.e("Register success...dataPacket = " + dataPacket.toString());
        PublicType.UserRegisterResult object = new PublicType.UserRegisterResult();
        try {
            object.parseJson(dataPacket.data);
            LocalConfigSharePreference.commintKeys(mParentActivity, object.mKeys);
            requestLogin(object.mKeys);
        } catch (JSONException e) {
            e.printStackTrace();
            CommonUtil.showToast(R.string.toast_register_fail, mParentActivity);
            exit();
        }
    }

    private void onTransdelLogin(ResponseDataPacket dataPacket) {
        log.i("Login success...");
        object = new PublicType.UserLoginResult();
        try {
            object.parseJson(dataPacket.data);


            log.i("mForceUpdate = " + object.mForceUpdate + "\n" +
                    "mHaveNewVer = " + object.mHaveNewVer + "\n" +
                    "mVerCode = " + object.mVerCode + "\n" +
                    "mVerName = " + object.mVerName + "\n" +
                    "mAppUrl = " + object.mAppUrl + "\n" +
                    "mVerDescribre = " + object.mVerDescribre +
                    "\nmDataList.size = " + object.mDataList.size());

            if (object.mForceUpdate != 0) {
                mView.showForceUpdateDialog(mParentActivity, object);
                return;
            }

            ;
            mApplication.setUserLoginResult(object);
            mApplication.setLoginStatus(true);
            goMainActivity();
        } catch (JSONException e) {
            e.printStackTrace();
            CommonUtil.showToast(R.string.toast_login_fail, mParentActivity);
            exit();
        }
    }

    private void goMainActivity(){
        Intent intent = new Intent();
        intent.setClass(mParentActivity, MainActivity.class);
        mParentActivity.startActivity(intent);
        exit();
    }


    private void exit(){
        if (mSplashCallback != null){
            mSplashCallback.onExit();
        }
    }

}
