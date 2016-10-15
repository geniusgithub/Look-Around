package com.geniusgithub.lookaround.maincontent.content;


import com.geniusgithub.lookaround.base.BasePresenter;
import com.geniusgithub.lookaround.base.BaseView;
import com.geniusgithub.lookaround.model.BaseType;

import java.util.List;

public class ContentContract {
    public interface IView extends BaseView<IPresenter> {
        public void showFailView(boolean bShow);
        public void showLoadView(boolean bShow);
        public void updateInfomationView(List<BaseType.InfoItem> dataList);
        public void updateLoadMoreViewState(int state);
    }

    public interface IPresenter extends BasePresenter<IView> {
        public void onPullRefresh();
        public void onLoadMore();
        public void onEnterDetail(BaseType.InfoItem item);
    }
}
