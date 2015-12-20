package com.geniusgithub.lookaround.util;


public class FileManager {

	public static String getCacheFileSavePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/cache/";
		} else {
			return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/cache/";
		}
	}
	
	public static String getDownloadFileSavePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/files/";
		}
	}
}
