package com.geniusgithub.lookaround.detailcontent.browse;

import com.geniusgithub.lookaround.base.BasePresenter;
import com.geniusgithub.lookaround.base.BaseView;

import java.util.List;

public class PhotoBrowseContact {
    public interface IView extends BaseView<IPresenter> {
        public void initBrowseData(List<String> data, int curIndex);
    }

    public interface IPresenter extends BasePresenter<IView> {
    }
}
