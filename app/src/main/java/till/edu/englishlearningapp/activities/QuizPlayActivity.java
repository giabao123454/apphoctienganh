package till.edu.englishlearningapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.models.Question;

public class QuizPlayActivity extends AppCompatActivity {

    private TextView tvTimer, tvQuestionCategory, tvQuestion, tvQuestionCount;
    private ImageView ivQuizIcon, btnClose;
    private MaterialButton btnAns1, btnAns2, btnAns3, btnAns4;
    private CountDownTimer countDownTimer;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int xpEarned = 0;
    private String quizType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_play);

        quizType = getIntent().getStringExtra("QUIZ_TYPE");
        if (quizType == null) quizType = "Mixed Quiz";

        // Ánh xạ
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionCategory = findViewById(R.id.tvQuestionCategory);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        ivQuizIcon = findViewById(R.id.ivQuizIcon);

        btnAns1 = findViewById(R.id.btnAns1);
        btnAns2 = findViewById(R.id.btnAns2);
        btnAns3 = findViewById(R.id.btnAns3);
        btnAns4 = findViewById(R.id.btnAns4);
        btnClose = findViewById(R.id.btnClose);

        tvQuestionCategory.setText(quizType);
        btnClose.setOnClickListener(v -> finish());

        // Sinh danh sách câu hỏi dựa vào thể loại
        generateQuestions(quizType);

        // Hiển thị câu hỏi đầu tiên
        loadQuestion();

        btnAns1.setOnClickListener(v -> checkAnswer(btnAns1, 1));
        btnAns2.setOnClickListener(v -> checkAnswer(btnAns2, 2));
        btnAns3.setOnClickListener(v -> checkAnswer(btnAns3, 3));
        btnAns4.setOnClickListener(v -> checkAnswer(btnAns4, 4));

        findViewById(R.id.btnSkip).setOnClickListener(v -> moveToNextQuestion());
    }

    // ĐÃ SỬA: Cập nhật hàm này để dùng Constructor 8 tham số của Question
    private void generateQuestions(String type) {
        questionList = new ArrayList<>();

        // Cấu trúc 8 tham số: id, category, question, optA, optB, optC, optD, correctAns
        if (type.contains("Listening")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            questionList.add(new Question(1, type, "Listen to the audio. Where is the man going?", "A. Airport", "B. Hospital", "C. School", "D. Hotel", "1"));
            questionList.add(new Question(2, type, "What did she order?", "A. Coffee", "B. Tea", "C. Water", "D. Milk", "2"));
        } else if (type.contains("Vocabulary")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_menu_sort_alphabetically);
            questionList.add(new Question(3, type, "Which word means 'very big'?", "A. Tiny", "B. Enormous", "C. Small", "D. Short", "2"));
            questionList.add(new Question(4, type, "Synonym of 'Happy' is?", "A. Sad", "B. Angry", "C. Joyful", "D. Bored", "3"));
        } else { // Grammar & Khác
            ivQuizIcon.setImageResource(android.R.drawable.ic_menu_edit);
            questionList.add(new Question(5, type, "Which is the past tense of 'Go'?", "A. Goes", "B. Went", "C. Gone", "D. Going", "2"));
            questionList.add(new Question(6, type, "I ___ a student.", "A. am", "B. is", "C. are", "D. be", "1"));
            questionList.add(new Question(7, type, "She ___ play tennis yesterday.", "A. doesn't", "B. wasn't", "C. didn't", "D. hasn't", "3"));
        }
    }

    private void loadQuestion() {
        // Hủy timer cũ nếu có
        if (countDownTimer != null) countDownTimer.cancel();

        // Bật lại các nút và reset màu về trắng
        resetButtons();

        Question q = questionList.get(currentQuestionIndex);
        tvQuestion.setText(q.getQuestionText());
        btnAns1.setText(q.getOptionA());
        btnAns2.setText(q.getOptionB());
        btnAns3.setText(q.getOptionC());
        btnAns4.setText(q.getOptionD());

        tvQuestionCount.setText("Question " + (currentQuestionIndex + 1) + " of " + questionList.size());

        startTimer();
    }

    private void checkAnswer(MaterialButton selectedBtn, int selectedIndex) {
        if (countDownTimer != null) countDownTimer.cancel();

        // Khóa các nút không cho bấm nhiều lần
        btnAns1.setEnabled(false); btnAns2.setEnabled(false); btnAns3.setEnabled(false); btnAns4.setEnabled(false);

        Question q = questionList.get(currentQuestionIndex);
        int correctIndex = q.getCorrectAnswerIndex();

        if (selectedIndex == correctIndex) {
            // Đúng: Đổi màu xanh, cộng điểm
            selectedBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
            selectedBtn.setTextColor(Color.WHITE);
            score++;
            xpEarned += 10;
        } else {
            // Sai: Đổi màu đỏ nút đã chọn, và Hướng dẫn nút đúng (Màu xanh)
            selectedBtn.setBackgroundColor(Color.parseColor("#F44336"));
            selectedBtn.setTextColor(Color.WHITE);

            MaterialButton correctBtn = getButtonByIndex(correctIndex);
            if(correctBtn != null) {
                correctBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
                correctBtn.setTextColor(Color.WHITE);
            }
        }

        // Tự động chuyển câu sau 1.5 giây
        new Handler().postDelayed(this::moveToNextQuestion, 1500);
    }

    private void moveToNextQuestion() {
        if (currentQuestionIndex < questionList.size() - 1) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            // Đã làm hết câu hỏi, chuyển sang trang Kết quả
            Intent intent = new Intent(QuizPlayActivity.this, QuizResultActivity.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("TOTAL", questionList.size());
            intent.putExtra("XP", xpEarned);
            startActivity(intent);
            finish(); // Đóng trang này
        }
    }

    private void resetButtons() {
        MaterialButton[] btns = {btnAns1, btnAns2, btnAns3, btnAns4};
        for (MaterialButton btn : btns) {
            btn.setBackgroundColor(Color.WHITE);
            btn.setTextColor(Color.parseColor("#2C3E50"));
            btn.setEnabled(true);
        }
    }

    private MaterialButton getButtonByIndex(int index) {
        if(index == 1) return btnAns1;
        if(index == 2) return btnAns2;
        if(index == 3) return btnAns3;
        if(index == 4) return btnAns4;
        return null;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvTimer.setText(String.format("00:%02d", seconds));
                tvTimer.setTextColor(seconds <= 5 ? Color.parseColor("#E74C3C") : Color.parseColor("#1976D2"));
            }
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(QuizPlayActivity.this, "Hết giờ câu này!", Toast.LENGTH_SHORT).show();
                moveToNextQuestion();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}