package com.baiiu.gank.runalone;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * author: baiiu
 * date: on 17/10/31 13:26
 * description:
 */
public class GankApplication extends TinkerApplication {

    public GankApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.baiiu.common.BaseApplication",
              "com.tencent.tinker.loader.TinkerLoader", false);
    }

}
