package com.walkon.firebase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 화면의 레이아웃을 설정
        setContentView(R.layout.activity_spalsh);

        // 1초 뒤에 LoginActivity로 이동
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 1000); // 1초
    }
}