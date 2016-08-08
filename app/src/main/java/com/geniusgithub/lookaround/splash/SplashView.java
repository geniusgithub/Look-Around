package com.geniusgithub.lookaround.splash;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.PublicType;

public class SplashView implements SplashContract.IView, IDialogInterface {


    private SplashContract.IPresenter mPresenter;

    @Override
    public void bindPresenter(SplashContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupView(View rootView) {

    }

    private Dialog mDialog;
    @Override
    public void showForceUpdateDialog(Context context, PublicType.UserLoginResult object) {
        if (mDialog == null){
            mDialog = DialogBuilder.buildNormalDialog(context,
                    "版本更新" + object.mVerName, "您当前的版本过低，请升级至最新版本！", this);
        }

        mDialog.show();
    }

    @Override
    public void closeForceUpdateDialog() {
        if (mDialog != null){
            mDialog.dismiss();
        }
    }


    @Override
    public void onSure() {
        mPresenter.onUpdateSure();
    }

    @Override
    public void onCancel() {
        mPresenter.onUpdateCancel();
    }
}
