package com.jady.admaster.module.splash;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.jady.admaster.GlideApp;
import com.jady.admaster.data.LaunchADConfig;
import com.jady.admaster.data.SPConfig;
import com.jady.admaster.support.thread.ThreadCallback;
import com.jady.admaster.support.thread.ThreadUtils;
import com.jady.admaster.support.utils.SPUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class SplashPresenter {

    private ISplashView iSplashView;
    private Context mContext;

    public String SPLASH_AD_DATA = "{\"enabled\":true," +
            "\"img_url\":\"http://img.zcool.cn/community/018992561efb2732f87557013a3583.jpg@2o.jpg\"," +
            "\"enable_skip\":true," +
            "\"jump_target\":\"http://www.baidu.com/\"," +
            "\"is_webview\":true," +
            "\"always_show\":true," +
            "\"show_count\":8," +
            "\"click_count\":3," +
            "\"skip_count\":5," +
            "\"is_first_open\":false," +
            "\"duration\":3000," +
            "\"start_date\":1499842078000," +
            "\"end_date\":1500101278000}";

    public SplashPresenter(Context context, ISplashView iSplashView) {
        this.mContext = context;
        this.iSplashView = iSplashView;
    }

    public boolean shouldShowAD(LaunchADConfig config) {
        if (config == null || !config.isEnabled()) {
            return false;
        }
        if (!config.isAlwaysShow()) {
            if ((System.currentTimeMillis() < config.getStartDate() || System.currentTimeMillis() > config.getEndDate())) {
                return false;
            }
            SPUtils cacheUtility = SPUtils.create(mContext, SPConfig.SPFileName.LOCAL_DATA);
            int showCount = cacheUtility.getInt(SPConfig.SPFileKey.AD_SHOW_COUNT);
            if (showCount > config.getDisabledAfterShowCount()) {
                return false;
            }
            int clickCount = cacheUtility.getInt(SPConfig.SPFileKey.AD_CLICK_COUNT);
            if (clickCount > config.getDisabledAfterClickCount()) {
                return false;
            }
            int skipCount = cacheUtility.getInt(SPConfig.SPFileKey.AD_SKIP_COUNT);
            if (skipCount > config.getDisabledAfterSkipCount()) {
                return false;
            }
        }

        if (!TextUtils.isEmpty(config.getChannel())) {
            return true;
        }

        switch (config.getUserLoginType()) {
            case LaunchADConfig.UserLoginType.ALL_USER:
                return true;
            case LaunchADConfig.UserLoginType.LOGGED_IN_USER:
                return false;
            case LaunchADConfig.UserLoginType.NOT_LOGGED_IN_USER:
                return true;
        }
        switch (config.getUserInvitedType()) {
            case LaunchADConfig.UserInviteType.ALL_USER:
                return true;
            case LaunchADConfig.UserInviteType.INVITED_USER:
                return true;
            case LaunchADConfig.UserInviteType.NOT_INVITED_USER:
                return false;
        }
        return true;
    }

    public void setStartCount() {
        SPUtils spUtils = SPUtils.create(mContext, SPConfig.SPFileName.LOCAL_DATA);
        int startCount = spUtils.getInt(SPConfig.SPFileKey.START_COUNT, 0);
        spUtils.putInt(SPConfig.SPFileKey.START_COUNT, startCount + 1);
    }

    public boolean isGestureSetted() {
        return true;
//        String gestureCode = SPUtils.create(mContext, SPConfig.SPFileName.LOCAL_DATA).getString(SPConfig.SPFileKey.GESTURE_CODE);
//        if (!TextUtils.isEmpty(gestureCode)) {
//            return true;
//        }
//        return false;
    }

    public boolean isPastWelcomePage() {
//        return SPUtils.create(mContext, SPConfig.SPFileName.LOCAL_DATA).getBoolean(SPConfig.SPFileKey.IS_PAST_WELCOME_PAGE);
        return true;
    }

    /**
     * 返回用户是否登录
     */
    public boolean isUserLogined() {
        return true;
    }

    public void getOnlineADConfig(final LaunchADConfigCallback callback) {
        //本地
        if (callback != null) {
            LaunchADConfig launchADConfig = new Gson().fromJson(SPLASH_AD_DATA, LaunchADConfig.class);
            callback.onSuccess(launchADConfig);
        }
        //网络
//        API.getLaunchADConfig(new CommonCallback<LaunchADConfig>() {
//
//            @Override
//            public void onSuccess(LaunchADConfig config) {
//                callback.onSuccess(config);
//            }
//
//            @Override
//            public void onFailure(String error_code, String error_message) {
//                callback.onFailure();
//            }
//        });
    }

    public void initData() {
        ThreadUtils.startChildThread(new ThreadCallback() {
            @Override
            public void runOperate() throws InterruptedException {
                //执行耗时任务，第三方库的初始化等等
                Thread.sleep(2000);
            }

            @Override
            public void onError(Throwable e) {
                iSplashView.onDataInited();
            }

            @Override
            public void onCompleted() {
                iSplashView.onDataInited();
            }
        });
    }

    private static void copyFile(String sourceFile, String targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }

    public void downloadImg(final LaunchADConfig config) {
        ThreadUtils.startChildThread(new ThreadCallback() {
            @Override
            public void runOperate() throws InterruptedException, ExecutionException, IOException {
                File file = GlideApp.with(mContext).download(config.getImgUrl()).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                String imgPath = mContext.getCacheDir() + File.separator + config.getImgUrl().substring(config.getImgUrl().lastIndexOf("/") + 1);
                copyFile(file.getAbsolutePath(), imgPath);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
