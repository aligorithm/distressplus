package xyz.yisa.distressplus.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.models.Alert;

public class DistressActivity extends BaseAuthenticatedActivity implements OnMapReadyCallback {
    Alert alert;
    @Override
    protected void onDplusCreate(Bundle savedState) {
        setContentView(R.layout.activity_distress);
        Gson gson = new Gson();
        alert = gson.fromJson(getIntent().getStringExtra("alert"),Alert.class);
        TextView name = findViewById(R.id.activity_distress_name);
        TextView phone = findViewById(R.id.activity_distress_phone);
        TextView location = findViewById(R.id.activity_distress_location);
        TextView distance = findViewById(R.id.activity_distress_distance);
        name.setText(alert.name);
        phone.setText(alert.phone);
        location.setText("University of Ilorin");
        distance.setText("1km");
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.activity_distress_mapView, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng userPosition = new LatLng(Double.valueOf(alert.latitude), Double.valueOf(alert.longitude));
        googleMap.addMarker(new MarkerOptions().position(userPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition,15));
    }

}
