package com.jady.admaster.data;

import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class LaunchADConfig {

    @IntDef({UserLoginType.ALL_USER, UserLoginType.LOGGED_IN_USER, UserLoginType.NOT_LOGGED_IN_USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserLoginType {
        int ALL_USER = 0;
        int LOGGED_IN_USER = 1;
        int NOT_LOGGED_IN_USER = 2;
    }

    @IntDef({UserInviteType.ALL_USER, UserInviteType.INVITED_USER, UserInviteType.NOT_INVITED_USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserInviteType {
        int ALL_USER = 0;
        int INVITED_USER = 1;
        int NOT_INVITED_USER = 2;
    }

    /**
     * 是否允许显示广告
     */
    private boolean enabled;

    /**
     * 广告图片url
     */
    @SerializedName("img_url")
    private String imgUrl;

    /**
     * 是否允许显示跳过按钮
     */
    @SerializedName("enable_skip")
    private boolean enableSkip;

    /**
     * 跳转目标
     */
    @SerializedName("jump_target")
    private String jumpTarget;

    /**
     * 是否在网页打开
     * 如果不是，则调到本地的jumpTarget页面
     */
    @SerializedName("is_webview")
    private boolean isWebView;

    /**
     * 广告时长
     */
    private long duration;

    @SerializedName("always_show")
    private boolean alwaysShow;
    /**
     * 广告展示次数
     */
    @SerializedName("show_count")
    private int disabledAfterShowCount;

    /**
     * 点击多少次广告后隐藏广告
     */
    @SerializedName("click_count")
    private int disabledAfterClickCount;

    /**
     * 点击多少次跳过后隐藏广告
     */
    @SerializedName("skip_count")
    private int disabledAfterSkipCount;

    /**
     * 是否针对第一次打开的用户
     */
    @SerializedName("is_first_open")
    private boolean isFirstOpen;

    /**
     * 开始显示广告的日期
     */
    @SerializedName("start_date")
    private long startDate;

    /**
     * 结束显示广告的日期
     */
    @SerializedName("end_date")
    private long endDate;

    /**
     * 用户是否是被邀请的
     * 0：所有用户 1：被邀请的 2：不是被邀请的
     */
    @SerializedName("user_invited_type")
    @UserInviteType
    private int userInvitedType;

    /**
     * 用户是否登录
     *
     * @see UserLoginType#ALL_USER
     * @see UserLoginType#LOGGED_IN_USER
     * @see UserLoginType#NOT_LOGGED_IN_USER
     */
    @SerializedName("user_login_type")
    @UserLoginType
    private int userLoginType;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isEnableSkip() {
        return enableSkip;
    }

    public void setEnableSkip(boolean enableSkip) {
        this.enableSkip = enableSkip;
    }

    public String getJumpTarget() {
        return jumpTarget;
    }

    public void setJumpTarget(String jumpTarget) {
        this.jumpTarget = jumpTarget;
    }

    public boolean isWebView() {
        return isWebView;
    }

    public void setWebView(boolean webView) {
        isWebView = webView;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public int getDisabledAfterShowCount() {
        return disabledAfterShowCount;
    }

    public void setDisabledAfterShowCount(int disabledAfterShowCount) {
        this.disabledAfterShowCount = disabledAfterShowCount;
    }

    public int getDisabledAfterClickCount() {
        return disabledAfterClickCount;
    }

    public void setDisabledAfterClickCount(int disabledAfterClickCount) {
        this.disabledAfterClickCount = disabledAfterClickCount;
    }

    public int getDisabledAfterSkipCount() {
        return disabledAfterSkipCount;
    }

    public void setDisabledAfterSkipCount(int disabledAfterSkipCount) {
        this.disabledAfterSkipCount = disabledAfterSkipCount;
    }

    public boolean isFirstOpen() {
        return isFirstOpen;
    }

    public void setFirstOpen(boolean firstOpen) {
        isFirstOpen = firstOpen;
    }

    @UserInviteType
    public int getUserInvitedType() {
        return userInvitedType;
    }

    public void setUserInvitedType(@UserInviteType int userInvitedType) {
        this.userInvitedType = userInvitedType;
    }

    @UserLoginType
    public int getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(@UserLoginType int userLoginType) {
        this.userLoginType = userLoginType;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "LaunchADConfig{" +
                "enabled=" + enabled +
                ", imgUrl='" + imgUrl + '\'' +
                ", enableSkip=" + enableSkip +
                ", jumpTarget='" + jumpTarget + '\'' +
                ", isWebView=" + isWebView +
                ", duration=" + duration +
                ", alwaysShow=" + alwaysShow +
                ", disabledAfterShowCount=" + disabledAfterShowCount +
                ", disabledAfterClickCount=" + disabledAfterClickCount +
                ", disabledAfterSkipCount=" + disabledAfterSkipCount +
                ", isFirstOpen=" + isFirstOpen +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userInvitedType=" + userInvitedType +
                ", userLoginType=" + userLoginType +
                '}';
    }
}
