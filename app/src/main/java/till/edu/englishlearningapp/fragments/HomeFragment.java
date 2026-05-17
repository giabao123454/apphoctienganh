package till.edu.englishlearningapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.QuizPlayActivity;
import till.edu.englishlearningapp.adapters.TopicAdapter;
import till.edu.englishlearningapp.models.Topic;

public class HomeFragment extends Fragment {

    private RecyclerView rvTopics;
    private TopicAdapter topicAdapter;
    private List<Topic> topicList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView tvUserName, tvMotivationalQuote, tvStreakCount, tvLevel, tvTotalXP;
    private ProgressBar pbLevel;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 1. Ánh xạ giao diện
        tvUserName = view.findViewById(R.id.tvUserName);
        tvMotivationalQuote = view.findViewById(R.id.tvMotivationalQuote);
        tvStreakCount = view.findViewById(R.id.tvStreakCount);
        tvLevel = view.findViewById(R.id.tvLevel);
        tvTotalXP = view.findViewById(R.id.tvTotalXP);
        pbLevel = view.findViewById(R.id.pbLevel);

        setupGreeting();
        setupMotivationalQuote();
        loadUserStatsFromFirebase();

        // 2. Tính năng Thông báo (Có hiệu ứng lắc chuông)
        ImageView icNotification = view.findViewById(R.id.icNotification);
        icNotification.setOnClickListener(v -> {
            // Tạo hiệu ứng rung lắc (Shake effect)
            Animation shake = new RotateAnimation(-15, 15, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            shake.setDuration(100);
            shake.setRepeatMode(Animation.REVERSE);
            shake.setRepeatCount(3);
            v.startAnimation(shake);

            Toast.makeText(getContext(), "🔔 Bạn chưa có thông báo mới nào!", Toast.LENGTH_SHORT).show();
        });

        // 3. Tính năng Thanh Tìm Kiếm
        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(getContext(), "🔍 Đang tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
                    // Mốt có trang Search thì Intent qua đây
                }
                return true;
            }
            return false;
        });

        // 4. Tính năng Continue Learning
        View cardContinue = view.findViewById(R.id.cardContinueLearning);
        cardContinue.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đang mở Unit 3...", Toast.LENGTH_SHORT).show();
            // Mốt Intent qua FlashcardActivity hoặc bài học đang dở
        });

        // 5. Explore Categories -> Điều hướng sang Tab Learn
        View.OnClickListener categoryListener = v -> {
            if (getActivity() != null) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_learn); // Chuyển sang Tab Learn
                }
            }
        };
        view.findViewById(R.id.btnCategoryVocab).setOnClickListener(categoryListener);
        view.findViewById(R.id.btnCategoryGrammar).setOnClickListener(categoryListener);
        view.findViewById(R.id.btnCategoryListening).setOnClickListener(categoryListener);
        view.findViewById(R.id.btnCategorySpeaking).setOnClickListener(categoryListener);

        // 6. Daily Challenge -> Mở ngay QuizPlayActivity
        Button btnStartDaily = view.findViewById(R.id.btnStartDaily);
        btnStartDaily.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizPlayActivity.class);
            intent.putExtra("QUIZ_TYPE", "Daily Challenge");
            startActivity(intent);
        });
        // Click vào thẻ Daily Challenge cũng mở luôn cho tiện
        view.findViewById(R.id.cardDailyChallenge).setOnClickListener(v -> btnStartDaily.performClick());

        // 7. Load Recommended Units (Giữ nguyên như cũ)
        rvTopics = view.findViewById(R.id.rvTopics);
        rvTopics.setLayoutManager(new LinearLayoutManager(getContext()));
        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(getContext(), topicList);
        rvTopics.setAdapter(topicAdapter);
        loadTopicsFromFirebase();
    }

    // Lấy thông tin Streak và XP từ Firestore (Bảng users)
    private void loadUserStatsFromFirebase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Nếu Firebase có dữ liệu thì kéo về hiển thị
                    Long streak = documentSnapshot.getLong("streak");
                    Long xp = documentSnapshot.getLong("xp");
                    Long level = documentSnapshot.getLong("level");

                    if (streak != null) tvStreakCount.setText(streak + " Days");
                    if (xp != null) tvTotalXP.setText(xp + " XP");
                    if (level != null) {
                        tvLevel.setText("Level " + level);
                        // Giả sử mỗi level cần 1000 XP, tính phần trăm cho thanh Progress
                        int progress = (int) ((xp != null ? xp : 0) % 1000) / 10;
                        pbLevel.setProgress(progress);
                    }
                } else {
                    // Nếu User mới tinh chưa có data, set mặc định
                    tvStreakCount.setText("1 Days");
                    tvTotalXP.setText("10 XP");
                    tvLevel.setText("Level 1");
                    pbLevel.setProgress(10);
                }
            }).addOnFailureListener(e -> Log.e("Firebase", "Lỗi tải Stats", e));
        }
    }

    private void setupGreeting() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null && email.contains("@")) {
                String name = email.substring(0, email.indexOf("@"));
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                tvUserName.setText(name + " 👋");
            }
        }
    }

    private void setupMotivationalQuote() {
        String[] quotes = {
                "\"Small steps every day lead to big results.\"",
                "\"Practice makes progress.\"",
                "\"Mistakes are proof that you are trying.\""
        };
        Random random = new Random();
        if (tvMotivationalQuote != null) {
            tvMotivationalQuote.setText(quotes[random.nextInt(quotes.length)]);
        }
    }

    private void loadTopicsFromFirebase() {
        db.collection("topics").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                topicList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Topic topic = document.toObject(Topic.class);
                    topicList.add(topic);
                }
                topicAdapter.notifyDataSetChanged();
            }
        });
    }
}