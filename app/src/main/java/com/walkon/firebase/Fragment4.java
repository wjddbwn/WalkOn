package com.walkon.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment4 extends Fragment {

    private TextView nicknameTextView, logoutBtn;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 레이아웃을 inflate
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // FirebaseAuth 및 DatabaseReference 초기화
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // 레이아웃에서 텍스트뷰와 로그아웃 버튼 찾기
        nicknameTextView = view.findViewById(R.id.nicknameTextView);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        // 현재 사용자의 UID 가져오기
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // 데이터베이스에서 닉네임 가져오기
            databaseReference.child(uid).child("Nickname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 데이터베이스에서 닉네임을 가져와 텍스트뷰에 설정
                        String nickname = dataSnapshot.getValue(String.class);
                        nicknameTextView.setText(nickname); // 닉네임 표시
                    } else {
                        nicknameTextView.setText("닉네임 없음"); // 닉네임이 없을 경우 기본 메시지 설정
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터베이스 에러 처리
                    nicknameTextView.setText("에러 발생: " + databaseError.getMessage());
                }
            });
        } else {
            // 사용자가 로그인하지 않은 경우 기본 메시지 설정
            nicknameTextView.setText("로그인 필요");
        }

        // 로그아웃 버튼 클릭 이벤트 설정
        logoutBtn.setOnClickListener(v -> {
            // Firebase 인증에서 로그아웃
            auth.signOut();

            // 로그인 화면으로 이동 (예: LoginActivity로 이동)
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 백 스택 제거
            startActivity(intent);
            getActivity().finish(); // 현재 액티비티 종료
        });

        return view;
    }
}
