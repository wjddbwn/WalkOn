package com.walkon.firebase;

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

    private TextView nicknameTextView;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout inflate
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // Initialize FirebaseAuth and DatabaseReference
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Find the TextView in the layout
        nicknameTextView = view.findViewById(R.id.nicknameTextView);

        // Get the current user's UID
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Query the database for the nickname
            databaseReference.child(uid).child("nickname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the nickname from the database
                        String nickname = dataSnapshot.getValue(String.class);
                        nicknameTextView.setText(nickname); // Display the nickname
                    } else {
                        nicknameTextView.setText("닉네임 없음"); // Fallback if no nickname found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors
                    nicknameTextView.setText("에러 발생: " + databaseError.getMessage());
                }
            });
        } else {
            // If the user is not logged in, set default text
            nicknameTextView.setText("로그인 필요");
        }

        return view;
    }
}