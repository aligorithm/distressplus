package xyz.yisa.distressplus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.onesignal.OneSignal;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.activities.singles.Alerts;
import xyz.yisa.distressplus.application.DsClient;
import xyz.yisa.distressplus.models.BasicResponse;
import xyz.yisa.distressplus.models.User;

public class LoginActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {
    @Email
    private EditText email;
    @Password
    private EditText password;
    private Button login;
    private Validator validator;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.activity_login_email);
        password = findViewById(R.id.activity_login_password);
        login = findViewById(R.id.activity_login_button);
        login.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == login){
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        sendRequest();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    private void sendRequest() {
        final AlertDialog progressDialog = Alerts.getInstance().showProgress(this);
        login.setAlpha(0.6f);
        login.setEnabled(false);
        final User user = new User(
                null,
                email.getText().toString(),
                null,
                password.getText().toString()
        );
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                user.player = userId;
            }
        });
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        DsClient client = retrofit.create(DsClient.class);
        Call<BasicResponse> call = client.login(user);
        Log.e("LOG",call.request().url().toString());
        Log.e("LOG",gson.toJson(user));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                login.setEnabled(true);
                login.setAlpha(1f);
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null){
                    Log.e("REG",gson.toJson(response.body()));
                    if (response.body().status){
                        User newUser = response.body().user;
                        String serialUser = gson.toJson(newUser);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user",serialUser);
                        editor.putString("token",response.body().token);
                        editor.apply();
                        OneSignal.setEmail(user.email);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Alerts.getInstance().showError(LoginActivity.this,response.body().message);
                    }
                } else {
                    Alerts.getInstance().showError(LoginActivity.this,"Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable throwable) {
                progressDialog.dismiss();
                login.setEnabled(true);
                login.setAlpha(1f);
                Log.e("LOG",throwable.getMessage());
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
