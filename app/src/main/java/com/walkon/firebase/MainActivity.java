package com.walkon.firebase;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 처음 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Fragment1()).commit();

        // 바텀 네비게이션 화면 전환
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment1()).commit();
                        break;

                    case R.id.nav_tutorial:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment2()).commit();
                        break;

                    case R.id.nav_analysis:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment3()).commit();
                        break;

                    case R.id.nav_my:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment4()).commit();
                        break;
                }
                return false;
            }
        });
    }

}