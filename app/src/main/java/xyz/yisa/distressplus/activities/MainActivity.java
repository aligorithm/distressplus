package xyz.yisa.distressplus.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.application.HomePagerAdapter;

public class MainActivity extends BaseAuthenticatedActivity{
    private static final int PERMISSIONS_REQUEST_LOCATION_ACCESS = 1;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onDplusCreate(Bundle savedState) {
        setContentView(R.layout.activity_main);
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder
                    .setMessage("To use Favourmates, please grant the app permission to use your location.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSIONS_REQUEST_LOCATION_ACCESS);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION_ACCESS);
        }
        viewPager = findViewById(R.id.activity_main_viewPager);
        tabLayout = findViewById(R.id.activity_main_tabLayout);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(0).setIcon(R.drawable.home_selected);
                tabLayout.getTabAt(1).setIcon(R.drawable.alert);
                tabLayout.getTabAt(2).setIcon(R.drawable.contacts);
                tabLayout.getTabAt(3).setIcon(R.drawable.user);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_selected);
                        tabLayout.getTabAt(1).setIcon(R.drawable.alert);
                        tabLayout.getTabAt(2).setIcon(R.drawable.contacts);
                        tabLayout.getTabAt(3).setIcon(R.drawable.user);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        tabLayout.getTabAt(1).setIcon(R.drawable.alert_selected);
                        tabLayout.getTabAt(2).setIcon(R.drawable.contacts);
                        tabLayout.getTabAt(3).setIcon(R.drawable.user);
                        break;
                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        tabLayout.getTabAt(1).setIcon(R.drawable.alert);
                        tabLayout.getTabAt(2).setIcon(R.drawable.contacts_selected);
                        tabLayout.getTabAt(3).setIcon(R.drawable.user);
                        break;
                    case 3:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        tabLayout.getTabAt(1).setIcon(R.drawable.alert);
                        tabLayout.getTabAt(2).setIcon(R.drawable.contacts);
                        tabLayout.getTabAt(3).setIcon(R.drawable.user_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION_ACCESS){
        }
    }
}
