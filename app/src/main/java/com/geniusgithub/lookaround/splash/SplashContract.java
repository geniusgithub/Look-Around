package com.geniusgithub.lookaround.splash;

import android.content.Context;

import com.geniusgithub.lookaround.base.BasePresenter;
import com.geniusgithub.lookaround.base.BaseView;
import com.geniusgithub.lookaround.model.PublicType;

public class SplashContract {
    public interface IView extends BaseView<IPresenter>{
        public void showForceUpdateDialog(Context context,  PublicType.UserLoginResult object);
        public void closeForceUpdateDialog();
    }

    public interface IPresenter extends BasePresenter<IView>{
        public void onUpdateSure();
        public void onUpdateCancel();
    }
}
