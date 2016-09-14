package com.geniusgithub.lookaround.detailcontent;


import com.geniusgithub.lookaround.base.BasePresenter;
import com.geniusgithub.lookaround.base.BaseView;
import com.geniusgithub.lookaround.model.BaseType;

public class DetailContract {
    public interface IView extends BaseView<IPresenter> {
        public  void updateToolTitle(String title);
        public void updateInfoItemEx(BaseType.InfoItemEx object);
        public void onDestroy();
    }

    public interface IPresenter extends BasePresenter<IView>{
        public void   shareToSina();
        public void   shareToTencent();
        public void   shareToWChat();
        public void   shareToWFriend();
        public void   shareToQZone();
        public void   enterWebView();
        public void   enterPhoneView();
    }

}
