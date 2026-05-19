package till.edu.englishlearningapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.QuizPlayActivity;
import till.edu.englishlearningapp.activities.LeaderboardActivity; // Import trang Bảng xếp hạng

public class QuizFragment extends Fragment {

    private TextView tabBeginner, tabIntermediate, tabAdvanced;
    // Khai báo thêm cardLeaderboard ở đây
    private MaterialCardView cardVocab, cardGrammar, cardListening, cardSpeaking, cardReading, cardMixed, cardLeaderboard;

    // Biến lưu độ khó hiện tại
    private String currentDifficulty = "Beginner";

    public QuizFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ ID
        tabBeginner = view.findViewById(R.id.tabBeginner);
        tabIntermediate = view.findViewById(R.id.tabIntermediate);
        tabAdvanced = view.findViewById(R.id.tabAdvanced);

        cardVocab = view.findViewById(R.id.cardQuizVocab);
        cardGrammar = view.findViewById(R.id.cardQuizGrammar);
        cardListening = view.findViewById(R.id.cardQuizListening);
        cardSpeaking = view.findViewById(R.id.cardQuizSpeaking);
        cardReading = view.findViewById(R.id.cardQuizReading);
        cardMixed = view.findViewById(R.id.cardQuizMixed);

        // Ánh xạ ID cho bảng xếp hạng
        cardLeaderboard = view.findViewById(R.id.cardLeaderboard);

        // 2. Thiết lập Tab mặc định là Beginner
        updateTabSelection(tabBeginner, "Beginner");

        // 3. Bắt sự kiện Click chuyển đổi Tab
        tabBeginner.setOnClickListener(v -> updateTabSelection(tabBeginner, "Beginner"));
        tabIntermediate.setOnClickListener(v -> updateTabSelection(tabIntermediate, "Intermediate"));
        tabAdvanced.setOnClickListener(v -> updateTabSelection(tabAdvanced, "Advanced"));

        // 4. Gắn hiệu ứng nhún cho các thẻ
        setClickAnimation(cardVocab);
        setClickAnimation(cardGrammar);
        setClickAnimation(cardListening);
        setClickAnimation(cardSpeaking);
        if(cardReading != null) setClickAnimation(cardReading);
        if(cardMixed != null) setClickAnimation(cardMixed);
        if(cardLeaderboard != null) setClickAnimation(cardLeaderboard); // Hiệu ứng nhún cho Bảng Xếp Hạng

        // 5. Bắt sự kiện Click vào Thẻ Quiz
        cardVocab.setOnClickListener(v -> startQuiz("Vocabulary"));
        cardGrammar.setOnClickListener(v -> startQuiz("Grammar"));
        cardListening.setOnClickListener(v -> startQuiz("Listening"));
        cardSpeaking.setOnClickListener(v -> startQuiz("Speaking"));
        if(cardReading != null) cardReading.setOnClickListener(v -> startQuiz("Reading"));
        if(cardMixed != null) cardMixed.setOnClickListener(v -> startQuiz("Mixed Quiz"));

        // 6. SỰ KIỆN CLICK MỞ BẢNG XẾP HẠNG
        if(cardLeaderboard != null) {
            cardLeaderboard.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
                startActivity(intent);
            });
        }
    }

    private void updateTabSelection(TextView selectedTab, String difficulty) {
        currentDifficulty = difficulty;

        tabBeginner.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabBeginner.setTextColor(getResources().getColor(android.R.color.black));

        tabIntermediate.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabIntermediate.setTextColor(getResources().getColor(android.R.color.black));

        tabAdvanced.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabAdvanced.setTextColor(getResources().getColor(android.R.color.black));

        selectedTab.setBackgroundResource(R.drawable.bg_chip_active);
        selectedTab.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void startQuiz(String category) {
        Intent intent = new Intent(getActivity(), QuizPlayActivity.class);
        intent.putExtra("QUIZ_TYPE", currentDifficulty + " " + category);
        Toast.makeText(getContext(), "Đang mở: " + currentDifficulty + " " + category, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void setClickAnimation(View view) {
        if (view == null) return;
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }
}