package till.edu.englishlearningapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import till.edu.englishlearningapp.R;

public class FlashcardActivity extends AppCompatActivity {

    private TextView tvTopicName, tvProgressCount, tvWordContent, tvWordMeaning, tvWordExample;
    private MaterialCardView cardFlashcard;
    private ProgressBar pbFlashcard;
    private Button btnPrevious, btnNext, btnTakeQuiz;

    private List<String[]> wordList; // Mảng chứa [Word, Meaning, Example]
    private int currentIndex = 0;
    private boolean isShowingFront = true;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Lấy tên Topic từ Fragment gửi qua
        topicName = getIntent().getStringExtra("TOPIC_NAME");
        if (topicName == null) topicName = "Vocabulary Topic";

        // Ánh xạ
        tvTopicName = findViewById(R.id.tvTopicName);
        tvProgressCount = findViewById(R.id.tvProgressCount);
        tvWordContent = findViewById(R.id.tvWordContent);
        tvWordMeaning = findViewById(R.id.tvWordMeaning);
        tvWordExample = findViewById(R.id.tvWordExample);
        cardFlashcard = findViewById(R.id.cardFlashcard);
        pbFlashcard = findViewById(R.id.pbFlashcard);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnTakeQuiz = findViewById(R.id.btnTakeQuiz);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        tvTopicName.setText(topicName);

        // Tạo danh sách từ vựng giả lập
        generateDummyData();
        updateUI();

        // Xử lý nút Next / Prev
        btnNext.setOnClickListener(v -> {
            if (currentIndex < wordList.size() - 1) {
                currentIndex++;
                isShowingFront = true; // Chuyển từ mới thì luôn úp mặt sau lại
                updateUI();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                isShowingFront = true;
                updateUI();
            }
        });

        // Xử lý LẬT THẺ 3D
        cardFlashcard.setOnClickListener(v -> flipCard());

        // Xử lý chuyển sang Quiz
        btnTakeQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(FlashcardActivity.this, QuizPlayActivity.class);
            intent.putExtra("QUIZ_TYPE", "Quiz: " + topicName);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.imgSpeak).setOnClickListener(v -> Toast.makeText(this, "Đang phát âm: " + wordList.get(currentIndex)[0], Toast.LENGTH_SHORT).show());
    }

    private void updateUI() {
        String[] currentWord = wordList.get(currentIndex);

        // Cập nhật nội dung hiển thị
        tvWordContent.setText(currentWord[0]);
        tvWordMeaning.setText(currentWord[1]);
        tvWordExample.setText(currentWord[2]);

        // Reset thẻ về mặt trước (chỉ hiện tiếng Anh)
        cardFlashcard.setRotationY(0);
        tvWordContent.setVisibility(View.VISIBLE);
        tvWordMeaning.setVisibility(View.GONE);
        tvWordExample.setVisibility(View.GONE);

        // Cập nhật số đếm & thanh Progress
        tvProgressCount.setText((currentIndex + 1) + " / " + wordList.size());
        pbFlashcard.setProgress((int) (((float) (currentIndex + 1) / wordList.size()) * 100));

        // Ẩn/Hiện nút bấm
        btnPrevious.setEnabled(currentIndex > 0);

        if (currentIndex == wordList.size() - 1) {
            btnNext.setVisibility(View.GONE);
            btnTakeQuiz.setVisibility(View.VISIBLE); // Hiện nút thi khi học xong
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnTakeQuiz.setVisibility(View.GONE);
        }
    }

    // Hiệu ứng lật 3D
    private void flipCard() {
        // Tăng chiều sâu của camera để thẻ lật không bị cắt góc
        float scale = getResources().getDisplayMetrics().density;
        cardFlashcard.setCameraDistance(8000 * scale);

        ObjectAnimator flipOut = ObjectAnimator.ofFloat(cardFlashcard, "rotationY", isShowingFront ? 0f : 180f, 90f);
        flipOut.setDuration(150);
        flipOut.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator flipIn = ObjectAnimator.ofFloat(cardFlashcard, "rotationY", -90f, 0f);
        flipIn.setDuration(150);
        flipIn.setInterpolator(new DecelerateInterpolator());

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isShowingFront = !isShowingFront;
                if (isShowingFront) {
                    tvWordMeaning.setVisibility(View.GONE);
                    tvWordExample.setVisibility(View.GONE);
                } else {
                    tvWordMeaning.setVisibility(View.VISIBLE);
                    tvWordExample.setVisibility(View.VISIBLE);
                }
                flipIn.start();
            }
        });
        flipOut.start();
    }

    private void generateDummyData() {
        wordList = new ArrayList<>();
        if (topicName.contains("Animals")) {
            wordList.add(new String[]{"Dog", "Con chó (Noun)", "Eg: The dog is barking."});
            wordList.add(new String[]{"Cat", "Con mèo (Noun)", "Eg: The cat is sleeping."});
            wordList.add(new String[]{"Bird", "Con chim (Noun)", "Eg: The bird is flying in the sky."});
        } else if (topicName.contains("Food")) {
            wordList.add(new String[]{"Apple", "Quả táo (Noun)", "Eg: An apple a day keeps the doctor away."});
            wordList.add(new String[]{"Bread", "Bánh mì (Noun)", "Eg: I eat bread for breakfast."});
        } else {
            wordList.add(new String[]{"Hello", "Xin chào", "Eg: Hello, how are you?"});
            wordList.add(new String[]{"World", "Thế giới", "Eg: The world is beautiful."});
        }
    }
}