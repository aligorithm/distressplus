package xyz.yisa.distressplus.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.activities.MainActivity;
import xyz.yisa.distressplus.activities.singles.Alerts;
import xyz.yisa.distressplus.application.DsClient;
import xyz.yisa.distressplus.models.Alert;
import xyz.yisa.distressplus.models.BasicResponse;
import xyz.yisa.distressplus.models.User;

public class HomeFragment extends Fragment implements View.OnClickListener {
    View rootView;
    ImageButton sos;
    ImageView success;
    MainActivity activity;
    User user;
    Gson gson = new Gson();
    ReactiveLocationProvider locationProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        user = gson.fromJson(activity.preferences.getString("user", null), User.class);
        sos = rootView.findViewById(R.id.fragment_home_sos);
        success = rootView.findViewById(R.id.fragment_home_success);
        sos.setOnClickListener(this);
        success.setOnClickListener(this);
        locationProvider = new ReactiveLocationProvider(activity);
    }

    @Override
    public void onClick(View view) {
        if (view == sos) {
            sendAlert();
        } else if (view == success) {
            success.setVisibility(View.GONE);
        }
    }

    private void sendAlert() {
        final AlertDialog progressDialog = Alerts.getInstance().showProgress(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProvider.getLastKnownLocation()
                .subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                        Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl(getString(R.string.base_url))
                                .addConverterFactory(GsonConverterFactory.create());
                        Retrofit retrofit = builder.build();
                        DsClient client = retrofit.create(DsClient.class);
                        Alert alert = new Alert();
                        alert.sender_id = user.id;
                        alert.name = user.name;
                        alert.phone = user.phone;
                        alert.latitude = String.valueOf(location.getLatitude());
                        alert.longitude = String.valueOf(location.getLongitude());
                        Call<BasicResponse> call = client.sendAlert(activity.preferences.getString("token",null),alert);
                        Log.e("Dis1",gson.toJson(alert));
                        Log.e("dis2",gson.toJson(call.request()));
                        call.enqueue(new Callback<BasicResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                                progressDialog.dismiss();
                                if (response.isSuccessful() && response.body() != null){
                                    success.setVisibility(View.VISIBLE);
                                } else {
                                    Alerts.getInstance().showError(activity,"Something went wrong. Please retry.");
                                    Log.e("Dis3",String.valueOf(response.code()));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable throwable) {
                                progressDialog.dismiss();
                                Alerts.getInstance().showError(activity,"Something went wrong. Please retry.");
                                Toast.makeText(activity, "The network connection flaked out", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

    }
}
