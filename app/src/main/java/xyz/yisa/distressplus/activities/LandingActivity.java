package xyz.yisa.distressplus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xyz.yisa.distressplus.R;

public class LandingActivity extends BaseActivity implements View.OnClickListener {
    Button login;
    Button register;
    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        setContentView(R.layout.activity_landing);
        login = findViewById(R.id.activity_landing_login);
        register = findViewById(R.id.activity_landing_register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == login){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }
    }
}
