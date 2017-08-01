package com.jady.admaster.module.common;

import android.content.Context;
import android.content.Intent;

import com.jady.admaster.module.main.MainActivity;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class NativePageUtils {
    public static final String USER_CENTER = "user_center";

    public static Intent getNativeIntent(Context context, String pageName) {
        Intent intent = new Intent();
        switch (pageName) {
            case USER_CENTER:
                intent.setClass(context, MainActivity.class);
                break;
        }
        if (intent.getComponent() != null) {
            return intent;
        } else {
            return null;
        }
    }
}
