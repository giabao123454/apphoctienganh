package till.edu.englishlearningapp.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.FlashcardActivity;
import till.edu.englishlearningapp.activities.GrammarLessonActivity;
import till.edu.englishlearningapp.activities.QuizPlayActivity;

public class LearnFragment extends Fragment {

    private TextView tabBeginner, tabIntermediate, tabAdvanced, tvGrammarProgress, tvListenProgress;
    private MaterialCardView cardVocab, cardGrammar, cardListening, cardSpeaking;
    private EditText etSearch;
    private Button btnStartTense1, btnStartArticles;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Biến lưu trữ độ khó hiện tại (Mặc định là Beginner)
    private String currentDifficulty = "Beginner";

    public LearnFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 1. Ánh xạ ID
        etSearch = view.findViewById(R.id.etSearch);
        tabBeginner = view.findViewById(R.id.tabBeginner);
        tabIntermediate = view.findViewById(R.id.tabIntermediate);
        tabAdvanced = view.findViewById(R.id.tabAdvanced);
        cardVocab = view.findViewById(R.id.cardLearnVocab);
        cardGrammar = view.findViewById(R.id.cardLearnGrammar);
        cardListening = view.findViewById(R.id.cardLearnListening);
        cardSpeaking = view.findViewById(R.id.cardLearnSpeaking);
        tvGrammarProgress = view.findViewById(R.id.tvGrammarProgress);
        btnStartTense1 = view.findViewById(R.id.btnStartTense1);
        btnStartArticles = view.findViewById(R.id.btnStartArticles);

        // Ánh xạ thêm tiến độ Listening vừa tạo ở XML
        tvListenProgress = view.findViewById(R.id.tvListenProgress);

        // 2. Gắn hiệu ứng nhún (Scale Animation) cho các thẻ
        setClickAnimation(cardVocab);
        setClickAnimation(cardGrammar);
        setClickAnimation(cardListening);
        setClickAnimation(cardSpeaking);

        // 3. Logic Tìm kiếm (Search)
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(getContext(), "🔍 Đang tìm bài học: " + query, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        // 4. Logic chuyển đổi Tab (Filter Difficulty)
        updateTabSelection(tabBeginner, "Beginner");

        tabBeginner.setOnClickListener(v -> updateTabSelection(tabBeginner, "Beginner"));
        tabIntermediate.setOnClickListener(v -> updateTabSelection(tabIntermediate, "Intermediate"));
        tabAdvanced.setOnClickListener(v -> updateTabSelection(tabAdvanced, "Advanced"));

        // 5. Logic click vào các Thẻ Category bung Menu Danh Sách Bài Học
        cardVocab.setOnClickListener(v -> showLessonDialog("Vocabulary"));
        cardGrammar.setOnClickListener(v -> showLessonDialog("Grammar"));
        cardListening.setOnClickListener(v -> showLessonDialog("Listening"));
        cardSpeaking.setOnClickListener(v -> showLessonDialog("Speaking"));

        // 6. Logic Bắt đầu bài học Ngữ Pháp tĩnh (Nằm ở dưới cùng UI)
        btnStartTense1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GrammarLessonActivity.class);
            intent.putExtra("TOPIC_NAME", "Simple Present");
            startActivity(intent);
        });

        btnStartArticles.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GrammarLessonActivity.class);
            intent.putExtra("TOPIC_NAME", "A / An / The");
            startActivity(intent);
        });

        // 7. Tải tiến độ Học tập chung từ Firebase
        loadLearningProgress();
    }

    // Hàm đổi màu sắc và Cập nhật Biến Độ Khó
    private void updateTabSelection(TextView selectedTab, String difficulty) {
        currentDifficulty = difficulty;

        // Trả tất cả về Inactive
        tabBeginner.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabBeginner.setTextColor(getResources().getColor(android.R.color.black));

        tabIntermediate.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabIntermediate.setTextColor(getResources().getColor(android.R.color.black));

        tabAdvanced.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabAdvanced.setTextColor(getResources().getColor(android.R.color.black));

        // Set Active cho tab được chọn
        selectedTab.setBackgroundResource(R.drawable.bg_chip_active);
        selectedTab.setTextColor(getResources().getColor(android.R.color.white));
    }

    // Hiển thị danh sách bài học dựa theo Category và Độ khó
    private void showLessonDialog(String category) {
        String[] lessons = new String[0];

        if (category.equals("Vocabulary")) {
            if (currentDifficulty.equals("Beginner")) {
                lessons = new String[]{"Animals & Pets", "Food & Drinks", "Family Members", "Daily Routines", "Colors & Shapes"};
            } else if (currentDifficulty.equals("Intermediate")) {
                lessons = new String[]{"Travel & Airport", "Job Interviews", "Health & Body", "Shopping & Money", "Technology"};
            } else {
                lessons = new String[]{"Advanced Idioms", "Business Jargon", "Academic Words (IELTS)", "Phrasal Verbs", "Slang & Street Talk"};
            }
        }
        else if (category.equals("Grammar")) {
            if (currentDifficulty.equals("Beginner")) {
                lessons = new String[]{"Simple Present", "A / An / The", "Subject Pronouns", "To Be Verbs"};
            } else if (currentDifficulty.equals("Intermediate")) {
                lessons = new String[]{"Present Perfect", "Conditionals (Type 1, 2)", "Relative Clauses", "Passive Voice"};
            } else {
                lessons = new String[]{"Mixed Conditionals", "Inversion", "Reported Speech (Advanced)", "Subjunctive Mood"};
            }
        }
        else if (category.equals("Listening")) {
            if (currentDifficulty.equals("Beginner")) {
                lessons = new String[]{"Basic Greetings Audio", "Numbers & Dates", "Short Conversations"};
            } else if (currentDifficulty.equals("Intermediate")) {
                lessons = new String[]{"Podcast: Travel Stories", "News Broadcast (Slow)", "Ordering at a Restaurant"};
            } else {
                lessons = new String[]{"TED Talks Snippets", "Fast Native Conversations", "Debates & Arguments"};
            }
        }
        else if (category.equals("Speaking")) {
            if (currentDifficulty.equals("Beginner")) {
                lessons = new String[]{"Pronounce Alphabet", "Basic Intro: Who am I?", "Common Questions"};
            } else if (currentDifficulty.equals("Intermediate")) {
                lessons = new String[]{"Describe a Picture", "Roleplay: At the Hotel", "Expressing Opinions"};
            } else {
                lessons = new String[]{"Impromptu Speech", "Debate Topics", "Tongue Twisters (Hard)"};
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(category + " - " + currentDifficulty);

        String[] finalLessons = lessons;
        builder.setItems(lessons, (dialog, which) -> {
            String selectedLesson = finalLessons[which];
            Intent intent = null;

            if (category.equals("Vocabulary")) {
                Toast.makeText(getContext(), "Đang mở Flashcards: " + selectedLesson, Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), FlashcardActivity.class);
            } else if (category.equals("Grammar")) {
                Toast.makeText(getContext(), "Đang mở Bài học: " + selectedLesson, Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), GrammarLessonActivity.class);
            } else if (category.equals("Listening")) {
                Toast.makeText(getContext(), "Đang tải Audio: " + selectedLesson, Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), QuizPlayActivity.class);
            } else if (category.equals("Speaking")) {
                Toast.makeText(getContext(), "Mở phòng thu âm: " + selectedLesson, Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), QuizPlayActivity.class);
            }

            if (intent != null) {
                if (category.equals("Listening") || category.equals("Speaking")) {
                    intent.putExtra("QUIZ_TYPE", category + ": " + selectedLesson);
                } else {
                    intent.putExtra("TOPIC_NAME", selectedLesson);
                }
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Đóng", null);
        builder.show();
    }

    // Hiệu ứng "nhún" mượt mà
    private void setClickAnimation(View view) {
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

    // Lấy tiến độ từ Firebase cho cả Grammar và Listening
    private void loadLearningProgress() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Long grammarProgress = documentSnapshot.getLong("grammarProgress");
                    if (grammarProgress != null && tvGrammarProgress != null) {
                        tvGrammarProgress.setText(grammarProgress + "%");
                    }

                    Long listeningProgress = documentSnapshot.getLong("listeningProgress");
                    if (listeningProgress != null && tvListenProgress != null) {
                        tvListenProgress.setText(listeningProgress + "%");
                    }
                }
            }).addOnFailureListener(e -> Log.e("Firebase", "Lỗi tải tiến độ học tập", e));
        }
    }
}