package com.geniusgithub.lookaround.cache;

import java.io.File;

import com.geniusgithub.lookaround.util.FileHelper;



import android.content.Context;
import android.util.Log;



public abstract class AbstractFileCache {

	private String dirString;
	
	public AbstractFileCache(Context context) {
		
		dirString = getCacheDir();
		boolean ret = FileHelper.createDirectory(dirString);
		Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}
	
	public File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}
	
	public abstract String  getSavePath(String url);
	public abstract String  getCacheDir();

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}

}
