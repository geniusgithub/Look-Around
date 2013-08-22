package com.geniusgithub.lookaround.util;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.geniusgithub.lookaround/files/";
		}
	}
}
