package com.geniusgithub.lookaround.widget.phoneview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PictureLoader extends AsyncTaskLoader<List<String>> {
	private List<String> dataResult;
	private boolean dataIsReady;
	private static final String PICTURE = "pics";

	public PictureLoader(Context context) {
		super(context);
		if (dataIsReady) {
			deliverResult(dataResult);
		} else {
			forceLoad();
		}
	}

	@Override
	public List<String> loadInBackground() {
		List<String> list = new ArrayList<String>();
		try {
			String[] flLists = getContext().getAssets().list(PICTURE);
			for (String file : flLists) {
				if (file.endsWith(".jpg") || file.endsWith(".png")) {
					list.add(ImageCacheManager.ASSETS_PATH_PREFIX + PICTURE
							+ "/" + file);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
	}

	@Override
	protected void onStartLoading() {
		// 显示加载条

		super.onStartLoading();
	}

	@Override
	protected void onStopLoading() {
		// 隐藏加载条

		super.onStopLoading();
	}

	@Override
	public boolean takeContentChanged() {

		return super.takeContentChanged();
	}

}
