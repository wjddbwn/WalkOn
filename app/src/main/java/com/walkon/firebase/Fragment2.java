package com.walkon.firebase;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Fragment2 extends Fragment {

    private ImageView sq1Image, sq3Image;
    private Button tutorialButton;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        /*
        try {
            // 뷰 초기화
            sq1Image = view.findViewById(R.id.sq1);
            sq3Image = view.findViewById(R.id.sq3);
            tutorialButton = view.findViewById(R.id.tutorial_button);

            // 모든 뷰 초기 상태를 INVISIBLE로 설정
            sq1Image.setVisibility(View.INVISIBLE);
            sq3Image.setVisibility(View.INVISIBLE);
            tutorialButton.setVisibility(View.INVISIBLE);

            // 딜레이를 주면서 순차적으로 뷰 표시
            showImagesWithDelay();

            // 버튼 클릭 이벤트
            tutorialButton.setOnClickListener(v -> {
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_frame, new Fragment_tutorial_start());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

        } catch (Exception e) {
            Log.e("Fragment2", "Error in onCreateView: ", e);
        }

        return view;
    }

    private void showImagesWithDelay() {
        // Handler 대신 뷰의 postDelayed 사용

        sq1Image.postDelayed(() -> {
            if (isAdded() && getView() != null) sq1Image.setVisibility(View.VISIBLE);
        }, 1000);

        sq3Image.postDelayed(() -> {
            if (isAdded() && getView() != null) sq3Image.setVisibility(View.VISIBLE);
        }, 2000);

        tutorialButton.postDelayed(() -> {
            if (isAdded() && getView() != null) tutorialButton.setVisibility(View.VISIBLE);
        }, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 안전하게 Handler 작업 취소
        if (handler != null) {
            handler.removeCallbacksAndMessages(null); // 작업 취소
        }

         */
        return view;
    }
}
