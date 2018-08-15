package xyz.yisa.distressplus.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class AddContactActivity extends BaseAuthenticatedActivity implements View.OnClickListener {
    private EditText email;
    private Button add;
    private View back;

    @Override
    protected void onDplusCreate(Bundle savedState) {
        setContentView(R.layout.activity_add_contact);
        email = findViewById(R.id.activity_add_contact_email);
        add = findViewById(R.id.activity_add_contact_button);
        back = findViewById(R.id.activity_add_contact_back);
        add.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == add && !email.getText().toString().isEmpty()){
            sendRequest();
        } else if (view == back)
            finish();
    }

    private void sendRequest() {
        final AlertDialog progressDialog = Alerts.getInstance().showProgress(this);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        DsClient client = retrofit.create(DsClient.class);
        Call<BasicResponse> call = client.trust(preferences.getString("token",null),
                new User(null,email.getText().toString(),null,null));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().status){
                        LayoutInflater layoutInflater = LayoutInflater.from(AddContactActivity.this);
                        View alertView = layoutInflater.inflate(R.layout.options_dialog, null);
                        Button stay = alertView.findViewById(R.id.options_dialog_yes);
                        Button leave = alertView.findViewById(R.id.options_dialog_no);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddContactActivity.this);
                        alertDialogBuilder.setView(alertView);
                        alertDialogBuilder
                                .setCancelable(false);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        stay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        leave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setResult(RESULT_OK);
                                alertDialog.dismiss();
                                finish();
                            }
                        });
                    } else {
                        Alerts.getInstance().showError(AddContactActivity.this,response.body().message);
                    }
                } else {
                    Alerts.getInstance().showError(AddContactActivity.this,"Something went wrong. Try again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable throwable) {
                progressDialog.dismiss();
                Alerts.getInstance().showError(AddContactActivity.this,"The network connection flaked out...");

            }
        });
    }
}
