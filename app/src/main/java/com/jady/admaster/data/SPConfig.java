package com.jady.admaster.data;

/**
 * Created by lipingfa on 2017/7/13.
 */
public class SPConfig {
    public static class SPFileName {
        public static final String LOCAL_DATA = "local_data";
        public static final String AD_DATA = "ad_data";
    }

    public static class SPFileKey {
        //local_data
        public static final String IS_PAST_WELCOME_PAGE = "is_past_welcome_page";
        public static final String START_COUNT = "start_count";
        public static final String GESTURE_CODE = "GESTURE_CODE";

        //ad_data
        public static final String AD_IMG_PATH = "ad_img_path";
        public static final String AD_SHOW_COUNT = "ad_show_count";
        public static final String AD_SKIP_COUNT = "ad_skip_click_count";
        public static final String AD_CLICK_COUNT = "ad_click_count";
    }
}
