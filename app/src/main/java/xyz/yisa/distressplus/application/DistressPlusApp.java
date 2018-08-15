package xyz.yisa.distressplus.application;

import android.app.Application;

import com.onesignal.OneSignal;

public class DistressPlusApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();
    }
}
