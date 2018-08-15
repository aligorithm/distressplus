package xyz.yisa.distressplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.activities.AddContactActivity;
import xyz.yisa.distressplus.activities.MainActivity;
import xyz.yisa.distressplus.application.ContactAdapter;
import xyz.yisa.distressplus.application.DsClient;
import xyz.yisa.distressplus.models.User;

import static android.app.Activity.RESULT_OK;

public class ContactFragment extends Fragment implements View.OnClickListener {
    private static final int ADD_CONTACT_REQUEST = 1;
    View rootView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContactAdapter adapter;
    TextView noticeText;
    Button noticeButton;
    TextView errorText;
    Button retryButton;
    FloatingActionButton floatingActionButton;
    MainActivity activity;
    Gson gson = new Gson();
    ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        recyclerView = rootView.findViewById(R.id.fragment_contact_recyclerView);
        layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        adapter = new ContactAdapter(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        noticeText = rootView.findViewById(R.id.fragment_contact_notice_text);
        noticeButton = rootView.findViewById(R.id.fragment_contact_notice_button);
        errorText = rootView.findViewById(R.id.fragment_contact_error_text);
        retryButton = rootView.findViewById(R.id.fragment_contact_retry);
        floatingActionButton = rootView.findViewById(R.id.fragment_contact_add);
        progressBar = rootView.findViewById(R.id.fragment_contact_progressBar);
        noticeButton.setOnClickListener(this);
        retryButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        getContacts();
        adapter.setOnClick(new ContactAdapter.OnItemClicked() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    private void getContacts(){
        errorText.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        noticeText.setVisibility(View.GONE);
        noticeButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        DsClient client = retrofit.create(DsClient.class);
        Call<ArrayList<User>> call = client.trustedList(activity.preferences.getString("token",null));
        Log.e("Contfrag",gson.toJson(call.request().url()));
        Log.e("Contfrag token",activity.preferences.getString("token",null));
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<User>> call, @NonNull Response<ArrayList<User>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body() != null && response.body().size() > 0){
                        noticeText.setVisibility(View.GONE);
                        noticeButton.setVisibility(View.GONE);
                        for (User contact : response.body()) {
                            adapter.addItem(contact);
                        }
                    } else {
                        noticeText.setVisibility(View.VISIBLE);
                        noticeButton.setVisibility(View.VISIBLE);
                    }
                } else{
                    Log.e("contfrag",String.valueOf(response.code()));
                    errorText.setVisibility(View.VISIBLE);
                    retryButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<User>> call, @NonNull Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Log.e("contfrag2",throwable.getMessage());
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == floatingActionButton || view == noticeButton)
            startActivityForResult(new Intent(activity, AddContactActivity.class),ADD_CONTACT_REQUEST);
        else if (view == retryButton)
            getContacts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_CONTACT_REQUEST && resultCode == RESULT_OK){
            adapter.clear();
            getContacts();
        }
    }
}
