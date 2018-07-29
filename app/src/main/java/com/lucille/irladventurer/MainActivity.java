package com.lucille.irladventurer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.lucille.irladventurer.db.AppDatabase;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnFragmentInteractionListener{

    private static Integer mapColorSetting = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.navigation_home:
                transaction.replace(R.id.content, new HomeFragment()).commit();
                return true;
            case R.id.navigation_map:
                transaction.replace(R.id.content,
                        MapFragment.newInstance(mapColorSetting)).commit();
                return true;
            case R.id.navigation_settings:
                transaction.replace(R.id.content, new SettingsFragment()).commit();
                return true;
        }
        return false;
    }

    @Override
    public void updateMapColor(Integer color) {
        mapColorSetting = color;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
    }
}
