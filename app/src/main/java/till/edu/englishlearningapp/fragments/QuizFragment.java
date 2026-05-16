package till.edu.englishlearningapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.QuizPlayActivity;

public class QuizFragment extends Fragment {

    public QuizFragment() {
        // Constructor rỗng
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ 6 thẻ Quiz
        MaterialCardView cardVocab = view.findViewById(R.id.cardVocabQuiz);
        MaterialCardView cardGrammar = view.findViewById(R.id.cardGrammarQuiz);
        MaterialCardView cardListening = view.findViewById(R.id.cardListeningQuiz);
        MaterialCardView cardSpeaking = view.findViewById(R.id.cardSpeakingQuiz);
        MaterialCardView cardReading = view.findViewById(R.id.cardReadingQuiz);
        MaterialCardView cardMixed = view.findViewById(R.id.cardMixedQuiz);

        // Gắn chung 1 hàm chuyển trang, chỉ thay đổi tham số truyền đi
        cardVocab.setOnClickListener(v -> openQuizPlay("Vocabulary"));
        cardGrammar.setOnClickListener(v -> openQuizPlay("Grammar"));
        cardListening.setOnClickListener(v -> openQuizPlay("Listening"));
        cardSpeaking.setOnClickListener(v -> openQuizPlay("Speaking"));
        cardReading.setOnClickListener(v -> openQuizPlay("Reading"));
        cardMixed.setOnClickListener(v -> openQuizPlay("Mixed Challenge"));
    }

    private void openQuizPlay(String quizType) {
        Intent intent = new Intent(getActivity(), QuizPlayActivity.class);
        intent.putExtra("QUIZ_TYPE", quizType); // Truyền loại bài test sang màn hình chơi
        startActivity(intent);
    }
}