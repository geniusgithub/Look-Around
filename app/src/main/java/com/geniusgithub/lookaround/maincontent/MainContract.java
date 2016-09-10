package com.geniusgithub.lookaround.maincontent;


import com.geniusgithub.lookaround.base.BasePresenter;
import com.geniusgithub.lookaround.base.BaseView;
import com.geniusgithub.lookaround.model.BaseType;

import java.util.List;

public class MainContract {
    public interface IView extends BaseView<IPresenter> {
        public void updateNavView(List<BaseType.ListItem> dataList);
    }

    public interface IPresenter extends BasePresenter<IView> {
    }
}
