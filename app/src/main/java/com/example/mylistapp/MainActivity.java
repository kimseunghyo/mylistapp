package com.example.mylistapp;

import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mylistapp.databinding.ActivityMainBinding;
import com.example.mylistapp.ui.add.AddFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private Fragment addFragment;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragment = new Fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame,new AddFragment()).commitAllowingStateLoss();

        backBtn = (ImageButton) findViewById(R.id.backBtn);
        //backBtn.setOnClickListener(this);
    }
}