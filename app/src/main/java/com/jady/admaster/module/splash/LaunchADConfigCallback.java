package com.jady.admaster.module.splash;

import com.jady.admaster.data.LaunchADConfig;

/**
 * Created by lipingfa on 2017/7/13.
 */
public interface LaunchADConfigCallback {
    void onSuccess(LaunchADConfig config);

    void onFailure();
}
