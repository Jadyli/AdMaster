package com.jady.admaster.module.common;

import com.jady.admaster.module.user.UserCenterActivity;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class NativePageUtils {
    public static final String USER_CENTER = "user_center";

    public static Class getNativeClass(String pageName) {
        switch (pageName) {
            case USER_CENTER:
                return UserCenterActivity.class;
        }
        return null;
    }
}
