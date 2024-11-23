package com.example.onfest;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSdk.init(this,"8db1f9e5e0716f7d1e38b0320bb3faf9");
    }
    public static GlobalApplication getInstance() {
        return instance;
    }
}
