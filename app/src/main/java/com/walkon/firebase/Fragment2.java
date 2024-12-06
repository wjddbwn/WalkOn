package com.walkon.firebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

public class Fragment2 extends Fragment {

    private ImageView sq1Image, sq2Image, sq3Image;
    private Button tutorialButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        // 뷰 초기화
        sq1Image = view.findViewById(R.id.sq1);
        sq2Image = view.findViewById(R.id.sq2);
        sq3Image = view.findViewById(R.id.sq3);
        tutorialButton = view.findViewById(R.id.tutorial_button);

        // 초기 상태
        sq1Image.setVisibility(View.INVISIBLE);
        sq2Image.setVisibility(View.INVISIBLE);
        sq3Image.setVisibility(View.INVISIBLE);
        tutorialButton.setVisibility(View.INVISIBLE);

        // Glide를 사용해 이미지 로드
        Glide.with(this)
                .load(R.drawable.num1) // 실제 이미지 리소스
                .override(145, 145) // 크기 제한
                .into(sq1Image);

        Glide.with(this)
                .load(R.drawable.num2) // 실제 이미지 리소스
                .override(145, 145) // 크기 제한
                .into(sq2Image);

        Glide.with(this)
                .load(R.drawable.num3) // 실제 이미지 리소스
                .override(100, 100) // 크기 제한
                .into(sq3Image);

        // Jetpack's View LifecycleScope를 사용한 딜레이 처리
        view.postDelayed(() -> sq1Image.setVisibility(View.VISIBLE), 1000);
        view.postDelayed(() -> sq2Image.setVisibility(View.VISIBLE), 2000);
        view.postDelayed(() -> sq3Image.setVisibility(View.VISIBLE), 3000);
        view.postDelayed(() -> tutorialButton.setVisibility(View.VISIBLE), 4000);

        // 버튼 클릭 이벤트
        tutorialButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame, new Fragment_tutorial_start());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}