package com.example.mylistapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mylistapp.databinding.ActivityMainBinding;
import com.example.mylistapp.ui.checked.CheckedFragment;
import com.example.mylistapp.ui.home.HomeFragment;
import com.example.mylistapp.ui.productAdd.ProductAddFragment;
import com.example.mylistapp.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, new HomeFragment()).commitAllowingStateLoss();

        bottomNav = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String tag = String.valueOf(item.getTitle());
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
                //Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainFrame);
                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment);
                }

                fragment = fragmentManager.findFragmentByTag(tag);

                if (fragment == null) {
                    if (item.getItemId() == R.id.home) {
                        fragment = new HomeFragment();
                    }
                    else if (item.getItemId() == R.id.search) {
                        fragment = new SearchFragment();
                    }
                    else if (item.getItemId() == R.id.checked) {
                        fragment = new CheckedFragment();
                    }
                    fragmentTransaction.add(R.id.mainFrame, fragment, tag);
                }
                else {
                    fragmentTransaction.show(fragment);
                }

                fragmentTransaction.setPrimaryNavigationFragment(fragment);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.commitNow();

                return true;
            }
        });

        bottomNav.setSelectedItemId(R.id.home);
    }

}