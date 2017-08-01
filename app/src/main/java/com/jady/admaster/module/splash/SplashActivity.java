package com.jady.admaster.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jady.admaster.GlideApp;
import com.jady.admaster.R;
import com.jady.admaster.data.LaunchADConfig;
import com.jady.admaster.module.common.NativePageUtils;
import com.jady.admaster.module.common.WebviewActivity;
import com.jady.admaster.module.main.MainActivity;
import com.jady.admaster.support.utils.CommonUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SplashActivity extends AppCompatActivity implements ISplashView {

    public static final String TAG = SplashActivity.class.getSimpleName();

    protected ImageView ivSplashAd;
    protected Button btnSplashSkip;
    private LinearLayout llSplashRoot;
    private SplashPresenter splashPresenter;
    private boolean isDataInited = false, isADEnd = false, isStartedNewAty = false, isGestureEnd = false, isImgCached = false;
    private Intent adIntent;
    private LaunchADConfig mAdConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashPresenter = new SplashPresenter(this, this);
        splashPresenter.setStartCount();

        splashPresenter.initData();
        initAd();

    }

    private void initAd() {
        if (!CommonUtils.isFirstStart(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    splashPresenter.getOnlineADConfig(new LaunchADConfigCallback() {
                        @Override
                        public void onSuccess(final LaunchADConfig config) {
                            if (!splashPresenter.shouldShowAD(config)) {
                                onADCompleted(null);
                                return;
                            }

                            setContentView(R.layout.splash_aty);
                            llSplashRoot = (LinearLayout) findViewById(R.id.ll_splash_root);
                            ivSplashAd = (ImageView) findViewById(R.id.iv_splash_ad);
                            btnSplashSkip = (Button) findViewById(R.id.btn_splash_skip);

                            //第一次进入缓存图片，不显示广告
                            try {
                                String imgPath = getCacheDir() + File.separator + config.getImgUrl().substring(config.getImgUrl().lastIndexOf("/") + 1);
                                if (!new File(imgPath).exists()) {
                                    splashPresenter.downloadImg(config);
                                    onADCompleted(null);
                                    return;
                                }
                                isImgCached = true;
                                Log.d(TAG, "imagePath:" + imgPath);
                                GlideApp.with(SplashActivity.this).load(imgPath).centerCrop().into(ivSplashAd);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onADCompleted(null);
                                return;
                            }

                            //是否允许跳过
                            if (!config.isEnableSkip()) {
                                btnSplashSkip.setVisibility(View.GONE);
                            }
                            //是否设置了手势密码
                            if (splashPresenter.isGestureSetted() && !isGestureEnd) {
                                llSplashRoot.setVisibility(View.GONE);
                            } else {
                                llSplashRoot.setVisibility(View.VISIBLE);
                            }
                            mAdConfig = config;
                            //初始化倒计时

                            Log.d(TAG, "initTime");
                            initTime(config);
                            ivSplashAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onADCompleted(config);
                                }
                            });


                            btnSplashSkip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onADCompleted(null);
                                }
                            });
                        }

                        @Override
                        public void onFailure() {
                            onADCompleted(null);
                        }
                    });
                }
            }, 1000);
        } else {
            onADCompleted(null);
        }
    }

    /**
     * 初始化倒计时
     *
     * @param config
     */
    private void initTime(LaunchADConfig config) {
        if (config == null) {
            return;
        }
        final long count = config.getDuration() / 1000;
//        使用Schedulers.trampoline()是为了解决有时候interval不起作用，不按时发射
        Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.trampoline())//设置0延迟，每隔一秒发送一条数据
                .take((int) (count)) //设置总共发送的次数
                .map(new Func1<Long, Long>() {//long 值是从小到大，倒计时需要将值倒置
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
//                        btnSplashSkip.setText("跳过");
                        Log.d(TAG, "initTime onCompleted");
                        onADCompleted(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) { //接收到一条就是会操作一次UI
                        Log.d(TAG, "initTime onNext:" + aLong + " btn visibility:" + llSplashRoot.getVisibility());
                        String value = String.valueOf(aLong);
                        btnSplashSkip.setText(value + "S 跳过");
                    }
                });

//        new CountDownTimer(config.getDuration(), 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Log.d(TAG, "initTime onNext:" + millisUntilFinished / 1000);
//                btnSplashSkip.setText(millisUntilFinished / 1000 + "S 跳过");
//            }
//
//            @Override
//            public void onFinish() {
//                onADCompleted(null);
//            }
//        }.start();
    }

    /**
     * 广告加载完成
     *
     * @param config
     */
    private void onADCompleted(LaunchADConfig config) {

        Log.d(TAG, "onADCompleted isAdEnd:" + isADEnd + ",isDataInited:" + isDataInited);

        if (splashPresenter.isGestureSetted() && !isGestureEnd) {
            return;
        }

        if (isADEnd) {
            return;
        }
        isADEnd = true;

        if (config == null) {
            if (isDataInited && !isStartedNewAty) {
                startOtherActivity(new Intent(this, MainActivity.class));
            } else {
                if (llSplashRoot != null) {
                    llSplashRoot.setVisibility(View.GONE);
                }
            }
        } else {

            if (config.isWebView()) {
                adIntent = new Intent(this, WebviewActivity.class);
                adIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                adIntent.putExtra("url", config.getJumpTarget());
            } else {
                adIntent = NativePageUtils.getNativeIntent(this, config.getJumpTarget());
            }
            if (!isDataInited || isStartedNewAty) {
                //数据没有加载完成
                return;
            }
            if (adIntent != null) {
                if (!MainActivity.class.getName().equals(adIntent.getComponent().getClassName())) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                startOtherActivity(adIntent);
            } else {
                startOtherActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    /**
     * 数据加载完成
     */
    public void onDataInited() {

        Log.d(TAG, "onDataInited isImgCached:" + isImgCached + ", isADEnd:" + isADEnd + ",isStartedNewActivity:" + isStartedNewAty);
        isDataInited = true;
        //欢迎页
        if (!splashPresenter.isPastWelcomePage()) {
            startOtherActivity(new Intent(this, WelcomeActivity.class));
            return;
        }
        //手势
        if (splashPresenter.isGestureSetted()) {
            if (isImgCached) {
                Intent intent = new Intent(this, GestureConfirmActivity.class);
                intent.putExtra("should_return", true);
                startActivityForResult(intent, 1000);
            } else {
                startOtherActivity(new Intent(this, GestureConfirmActivity.class));
            }
            return;
        }
        if (!isADEnd || isStartedNewAty) {
            return;
        }
        //广告结束且没有开启新的Activity
        if (adIntent != null) {
            if (!MainActivity.class.getSimpleName().equals(adIntent.getComponent().getClassName())) {
                startActivity(new Intent(this, MainActivity.class));
            }
            startOtherActivity(adIntent);
        } else {
            startOtherActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * 手势密码回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1000) {
            isGestureEnd = true;
            llSplashRoot.setVisibility(View.VISIBLE);
            initTime(mAdConfig);
        }
    }

    private void startOtherActivity(Intent intent) {
        isStartedNewAty = true;
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
