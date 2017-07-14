package com.jady.admaster.data;

import com.jady.retrofitclient.HttpManager;
import com.jady.retrofitclient.callback.HttpCallback;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class API {
    public static void getLaunchADConfig(HttpCallback callback) {
        HttpManager.get(UrlConfig.LAUNCH_AD_CONFIG, null, callback);
    }
}
