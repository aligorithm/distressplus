package xyz.yisa.distressplus.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xyz.yisa.distressplus.application.DistressPlusApp;

public abstract class BaseActivity extends AppCompatActivity {
    public DistressPlusApp application;
    public SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        application = (DistressPlusApp) getApplication();
        preferences = application.getSharedPreferences("user",MODE_PRIVATE);

    }
}
