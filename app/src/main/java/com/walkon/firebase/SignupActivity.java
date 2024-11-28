package com.walkon.firebase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.walkon.firebase.R;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

    private EditText emailInput, passwordInput, passwordChckInput, nicknameInput;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        passwordChckInput = findViewById(R.id.password_check);
        nicknameInput = findViewById(R.id.nickname_input);
        signupButton = findViewById(R.id.signup_button);

        // 회원가입 버튼 이벤트
        signupButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String passwordCheck = passwordChckInput.getText().toString().trim();
            String nickname = nicknameInput.getText().toString().trim();

            // 입력값 확인
            if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(SignupActivity.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(passwordCheck)) {
                Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 회원가입 진행
            signupUser(email, password, nickname);
        });
    }

    private void signupUser(String email, String password, String nickname) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // 회원가입 성공
                String userId = task.getResult().getUser().getUid();
                Log.d("FirebaseAuth", "회원가입 성공: " + userId);

                // Firebase Database에 사용자 정보 저장
                saveUserToDatabase(userId, email, nickname);

            } else {
                // 회원가입 실패
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "알 수 없는 오류";
                Toast.makeText(SignupActivity.this, "회원가입 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("FirebaseAuth", "회원가입 실패", task.getException());
            }
        });
    }

    private void saveUserToDatabase(String userId, String email, String nickname) {
        // 사용자 데이터 객체 생성
        User user = new User(email, nickname);

        // Firebase Database에 저장
        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignupActivity.this, "회원가입 성공! 데이터 저장 완료", Toast.LENGTH_SHORT).show();
                Log.d("FirebaseDatabase", "사용자 데이터 저장 성공");
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "알 수 없는 오류";
                Toast.makeText(SignupActivity.this, "데이터 저장 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("FirebaseDatabase", "데이터 저장 실패", task.getException());
            }
        });
    }

    // 사용자 데이터 모델 클래스
    public static class User {
        public String email;
        public String nickname;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String nickname) {
            this.email = email;
            this.nickname = nickname;
        }
    }
}
