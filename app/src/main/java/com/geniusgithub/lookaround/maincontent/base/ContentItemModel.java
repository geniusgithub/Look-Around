package com.geniusgithub.lookaround.maincontent.base;

public interface ContentItemModel {

    public int getBannerType();
    public String getTitle();
    public String getContent();
    public String getUserName();
    public int getThumnaiImageCount();
    public String getThumnaiImageURL(int index);
}
