package com.geniusgithub.lookaround.collection;


import com.geniusgithub.lookaround.base.BasePresenter;
import com.geniusgithub.lookaround.base.BaseView;
import com.geniusgithub.lookaround.model.BaseType;

import java.util.List;

public class CollectContract {
    public interface IView extends BaseView<IPresenter> {
        public void updateInfomationView(List<BaseType.InfoItemEx> dataList);
        public void showDeleteDialog();
    }

    public interface IPresenter extends BasePresenter<IView> {
        public void onEnterDetail(BaseType.InfoItemEx item);
        public void clearCollcet();
        public int getCollectCount();
    }
}
