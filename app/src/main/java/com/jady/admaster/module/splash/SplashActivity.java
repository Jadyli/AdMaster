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

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity implements ISplashView {

    public static final String TAG = SplashActivity.class.getSimpleName();

    protected ImageView ivSplashAd;
    protected Button btnSplashSkip;
    private LinearLayout llSplashRoot;
    private SplashPresenter splashPresenter;
    private boolean isDataInited = false, isADEnd = false, isStartedNewAty = false, isGestureEnd = false, isImgCached = false;
    private Class nativeClass;
    private LaunchADConfig mAdConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashPresenter = new SplashPresenter(this, this);

        if (splashPresenter.shouldShowAD(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.splash_aty);
                    initView();
                    initAd();
                }
            }, 2000);
        } else {
            isADEnd = true;
        }

        splashPresenter.setStartCount();
        splashPresenter.initData();
    }

    private void initAd() {
        splashPresenter.getOnlineADConfig(new LaunchADConfigCallback() {
            @Override
            public void onSuccess(final LaunchADConfig config) {
                if (config == null
                        || !config.isEnabled()
                        || System.currentTimeMillis() < config.getStartDate()
                        || System.currentTimeMillis() > config.getEndDate()) {
                    onADCompleted(null);
                    return;
                }

                try {
                    String imgPath = getCacheDir() + File.separator + config.getImgUrl().substring(config.getImgUrl().lastIndexOf("/") + 1);
                    if (!new File(imgPath).exists()) {
                        splashPresenter.downloadImg(SplashActivity.this, config);
                        onADCompleted(null);
                        return;
                    }
                    isImgCached = true;
                    GlideApp.with(SplashActivity.this).load(imgPath).centerCrop().into(ivSplashAd);
                } catch (Exception e) {
                    e.printStackTrace();
                    onADCompleted(null);
                    return;
                }
                if (!isGestureEnd && splashPresenter.isGestureSetted()) {
                    llSplashRoot.setVisibility(View.GONE);
                } else {
                    llSplashRoot.setVisibility(View.VISIBLE);
                }
                mAdConfig = config;
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

            }
        });
    }

    private void initTime(LaunchADConfig config) {
        if (config == null) {
            return;
        }
        final long count = config.getDuration() / 1000;
        //使用Schedulers.trampoline()是为了解决有时候interval不起作用，不按时发射
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
                        btnSplashSkip.setText("跳过");
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
    }

    private void onADCompleted(LaunchADConfig config) {

        isADEnd = true;
        if (!isGestureEnd && splashPresenter.isGestureSetted()) {
            return;
        }

        if (config == null) {
            if (isDataInited) {
                startOtherActivity(new Intent(this, MainActivity.class));
            } else {
                llSplashRoot.setVisibility(View.GONE);
            }
        } else {
            if (config.isWebView()) {
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("url", config.getJumpTarget());
                startOtherActivity(intent);
            } else {
                nativeClass = NativePageUtils.getNativeClass(config.getJumpTarget());
                if (nativeClass == null) {
                    return;
                }

                if (isDataInited) {
                    //数据加载完成
                    Intent intent = new Intent(this, nativeClass);
                    startOtherActivity(intent);
                } else {
                    //数据加载未完成
                    Log.d(TAG, "点击广告时数据未加载完成");
                }
            }
        }
    }

    @Override
    public void onDataInited() {
        isDataInited = true;
        if (!splashPresenter.isPastWelcomePage()) {
            startOtherActivity(new Intent(this, WelcomeActivity.class));
            return;
        }
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
        if (isADEnd && !isStartedNewAty) {
            //广告结束且没有开启新的Activity
            if (nativeClass != null) {
                startOtherActivity(new Intent(this, nativeClass));
            } else {
                startOtherActivity(new Intent(this, MainActivity.class));
            }
        }
    }

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

    private void initView() {
        llSplashRoot = (LinearLayout) findViewById(R.id.ll_splash_root);
        ivSplashAd = (ImageView) findViewById(R.id.iv_splash_ad);
        btnSplashSkip = (Button) findViewById(R.id.btn_splash_skip);
    }

    private void startOtherActivity(Intent intent) {
        isStartedNewAty = true;
        startActivity(intent);
        finish();
    }
}
