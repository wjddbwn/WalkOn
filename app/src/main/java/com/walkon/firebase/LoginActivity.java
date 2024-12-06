package com.walkon.firebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import  com.google.firebase.auth.FirebaseAuth;

import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText id_text, password_text;
    private TextView btn_signup, btn_findpw;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance();

        // View 연결
        id_text = findViewById(R.id.id_text);
        password_text = findViewById(R.id.password_text);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        btn_findpw = findViewById(R.id.btn_findpw);

        // 로그인 버튼 클릭 이벤트
        btn_login.setOnClickListener(v -> login());

        // 회원가입 버튼 클릭 이벤트
        btn_signup.setOnClickListener(v -> {Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        });

        // 비밀번호 찾기 버튼 클릭 이벤트
        btn_findpw.setOnClickListener(v -> {Intent intent = new Intent(LoginActivity.this, FindpwActivity.class);
            startActivity(intent);
        });
    }


    private void login(){
        String email = id_text.getText().toString().trim();
        String password = password_text.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()){
            Toast toast = Toast.makeText(this, "아이디 또는 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0 ,0);
            toast.show();
            System.err.println("아이디 또는 비밀번호를 입력해 주세요.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                //로그인 성공 : MainActivity로 이동
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            } else{
                Toast toast = Toast.makeText(this, "아이디 혹은 이메일이 틀렸습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }
}