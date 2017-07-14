package com.jady.admaster.support.utils;

import android.content.Context;

import com.jady.admaster.data.SPConfig;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class CommonUtils {

    public static boolean isFirstStart(Context context) {
        return SPUtils.create(context, SPConfig.SPFileName.LOCAL_DATA).getInt(SPConfig.SPFileKey.START_COUNT, 0) == 1;
    }

    public static int getStartCount(Context context) {
        return SPUtils.create(context, SPConfig.SPFileName.LOCAL_DATA).getInt(SPConfig.SPFileKey.START_COUNT, 0);
    }
}
