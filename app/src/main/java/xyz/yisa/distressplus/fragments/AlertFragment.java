package xyz.yisa.distressplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.activities.DistressActivity;
import xyz.yisa.distressplus.activities.MainActivity;
import xyz.yisa.distressplus.application.AlertAdapter;
import xyz.yisa.distressplus.application.DsClient;
import xyz.yisa.distressplus.models.Alert;

public class AlertFragment extends Fragment implements View.OnClickListener {
    View rootView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AlertAdapter adapter;
    TextView noticeText;
    TextView errorText;
    Button retryButton;
    MainActivity activity;
    ProgressBar progressBar;
    Gson gson = new Gson();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_alert, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        recyclerView = rootView.findViewById(R.id.fragment_alert_recyclerView);
        layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        adapter = new AlertAdapter(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        noticeText = rootView.findViewById(R.id.fragment_alert_notice_text);
        errorText = rootView.findViewById(R.id.fragment_alert_error_text);
        retryButton = rootView.findViewById(R.id.fragment_alert_retry);
        progressBar = rootView.findViewById(R.id.fragment_alert_progressbar);
        retryButton.setOnClickListener(this);
        getAlerts();
        adapter.setOnClick(new AlertAdapter.OnItemClicked() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(activity,DistressActivity.class);
                intent.putExtra("alert",gson.toJson(adapter.getItem(position)));
                startActivity(intent);
            }
        });
    }

    private void getAlerts() {
        errorText.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        noticeText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        DsClient client = retrofit.create(DsClient.class);
        Call<ArrayList<Alert>> call = client.fetchAlerts(activity.preferences.getString("token",null));
        call.enqueue(new Callback<ArrayList<Alert>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Alert>> call, @NonNull Response<ArrayList<Alert>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().size() > 0){
                        adapter.clear();
                        for (Alert alert : response.body()) {
                            adapter.addItem(alert);
                        }
                    }else {
                        noticeText.setVisibility(View.VISIBLE);
                    }
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    retryButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Alert>> call, @NonNull Throwable throwable) {
                Toast.makeText(activity, "The network connection flaked out", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == retryButton){
            getAlerts();
        }
    }
}
