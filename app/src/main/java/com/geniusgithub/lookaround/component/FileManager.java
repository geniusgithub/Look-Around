package com.geniusgithub.lookaround.component;


import com.geniusgithub.lookaround.util.CommonUtil;

public class FileManager {

	private final String TAG = FileManager.class.getSimpleName();

	public static String getDownloadFileSavePath(String url){
		return getDownloadFileSaveDir() + String.valueOf(url.hashCode()) + getPostfix(url);
	}
	
	public static String getDownloadFileSaveDir() {
		return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/downfiles/";
	}

	public static String getPostfix(String url){
		int index = url.lastIndexOf(".");
		if (index != -1){
			return url.substring(index);
		}
		return "";
	}
}
