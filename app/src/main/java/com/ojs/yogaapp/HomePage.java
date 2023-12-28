package com.ojs.yogaapp;

import static android.text.TextUtils.replace;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private final BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener =
            item -> {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.menu_course) {
                    selectedFragment = new CourseFragment();
                } else if (item.getItemId() == R.id.menu_class) {
                    selectedFragment = new ClassFragment();
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();

                return true;
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener);

        // Set the default fragment to CourseFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CourseFragment())
                .commit();

        }
    }


