package com.geniusgithub.lookaround.share;

import cn.sharesdk.framework.Platform;

public interface ShareContentCustomizeCallback {

	public void onShare(Platform platform, Platform.ShareParams paramsToShare);

}
