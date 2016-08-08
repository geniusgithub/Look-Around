package com.geniusgithub.lookaround.splash;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.util.PermissionsUtil;

public class SplashActivity extends Activity implements SplashPresenter.ISplashCallback{

    private static final CommonLog log = LogFactory.createLog();

    private View rootView;
    private SplashPresenter mPresenter;
    private SplashContract.IView mView;

    private static final int SEND_MSG_ID = 0x0001;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        initView();
        initData();
    }

    private void initView(){
        rootView = findViewById(R.id.rl_root);
    }

    private void initData(){
        mPresenter = new SplashPresenter();
        mPresenter.attachActivity(this, this);
        mView = new SplashView();
        mView.setupView(rootView);
        mPresenter.bindView(mView);

        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SEND_MSG_ID:
                        go();
                        break;
                    default:
                        break;
                }
            }

        };

        mHandler.sendEmptyMessageDelayed(SEND_MSG_ID, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        mPresenter.cancelTask(this);
        exit();
    }

    @Override
    public void onExit() {
        exit();
    }


    private void go(){
        if (PermissionsUtil.hasNecessaryRequiredPermissions(this)){
            tryToLogin();
        }else{
            requestNecessaryRequiredPermissions();
        }
    }

    private void exit(){
        finish();
    }



    private void tryToLogin(){
        boolean ret = mPresenter.requestRegister();
        if (!ret){
            finish();
        }
    }


    private final int REQUEST_STORAGE_PERMISSION =  0X0001;
    private final int REQUEST_PHONE_PERMISSION =  0X0002;
    private void requestNecessaryRequiredPermissions(){
        requestSpecialPermissions(PermissionsUtil.STORAGE, REQUEST_STORAGE_PERMISSION);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void requestSpecialPermissions(String permission, int requestCode){
        String []permissions = new String[]{permission};
        requestPermissions(permissions, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode){
            case REQUEST_STORAGE_PERMISSION:
                doStoragePermission(grantResults);
                break;
            case REQUEST_PHONE_PERMISSION:
                doPhonePermission(grantResults);
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }


    }

    private void doStoragePermission(int[] grantResults){
        if (grantResults[0] == PackageManager.PERMISSION_DENIED){
            log.e("doStoragePermission is denied!!!" );
            Dialog dialog = PermissionsUtil.createPermissionSettingDialog(this, "存储权限");
            dialog.show();
        }else if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            log.i("doStoragePermission, is granted!!!" );
            requestSpecialPermissions(PermissionsUtil.PHONE, REQUEST_PHONE_PERMISSION);
        }

    }

    private void doPhonePermission(int[] grantResults){
        if (grantResults[0] == PackageManager.PERMISSION_DENIED){
            log.e("doPhonePermission is denied!!!" );
            Dialog dialog = PermissionsUtil.createPermissionSettingDialog(this, "读电话权限");
            dialog.show();
        }else if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            log.i("doPhonePermission, is granted!!!" );
            tryToLogin();
        }

    }


}
