package xyz.yisa.distressplus.activities;

import android.content.Intent;
import android.os.Bundle;

public abstract class BaseAuthenticatedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        if (preferences.getString("token",null) == null){
            Intent intent = new Intent(this,LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        onDplusCreate(savedState);
    }
    protected abstract void onDplusCreate(Bundle savedState);
}
