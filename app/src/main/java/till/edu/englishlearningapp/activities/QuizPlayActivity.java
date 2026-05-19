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

    // ĐÃ CẬP NHẬT: Ma trận câu hỏi 6 môn x 3 cấp độ
    private void generateQuestions(String type) {
        questionList = new ArrayList<>();

        // Nhận diện cấp độ khó
        boolean isBeginner = type.contains("Beginner");
        boolean isIntermediate = type.contains("Intermediate");
        boolean isAdvanced = type.contains("Advanced");

        // Nhận diện môn học và nạp đạn (câu hỏi)
        if (type.contains("Reading")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_menu_agenda); // Icon cuốn sách/sổ
            if (isBeginner) {
                questionList.add(new Question(1, type, "Read the sign: 'NO ENTRY'. What does it mean?", "A. Come in", "B. Do not go in", "C. Exit here", "D. Push the door", "2"));
                questionList.add(new Question(2, type, "Read: 'The sky is blue'. What color is the sky?", "A. Red", "B. Green", "C. Blue", "D. Yellow", "3"));
            } else if (isIntermediate) {
                questionList.add(new Question(3, type, "Read: 'The flight was delayed due to bad weather'. Why?", "A. No pilot", "B. Broken plane", "C. Bad weather", "D. Lost luggage", "3"));
                questionList.add(new Question(4, type, "Synonym of 'Essential' in the text is:", "A. Unnecessary", "B. Crucial", "C. Optional", "D. Trivial", "2"));
            } else { // Advanced
                questionList.add(new Question(5, type, "What can be inferred from the author's cynical tone?", "A. He is hopeful", "B. He strongly doubts the outcome", "C. He is extremely angry", "D. He is indifferent", "2"));
                questionList.add(new Question(6, type, "The word 'ubiquitous' in paragraph 2 means:", "A. Rare", "B. Expensive", "C. Found everywhere", "D. Hidden", "3"));
            }
        }
        else if (type.contains("Speaking")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_btn_speak_now); // Icon Micro
            if (isBeginner) {
                questionList.add(new Question(7, type, "Choose the correct polite response to 'How are you?'", "A. I am 10 years old", "B. I am fine, thank you", "C. My name is John", "D. I live in Vietnam", "2"));
                questionList.add(new Question(8, type, "How do you pronounce the 'th' in 'Think'?", "A. /t/", "B. /d/", "C. /θ/", "D. /ð/", "3"));
            } else if (isIntermediate) {
                questionList.add(new Question(9, type, "Which word has a different stress pattern?", "A. HO-tel", "B. WA-ter", "C. PA-per", "D. MU-sic", "1"));
                questionList.add(new Question(10, type, "Respond to: 'Would you mind if I opened the window?'", "A. Yes, please do.", "B. No, not at all.", "C. Yes, I am.", "D. No, I mind.", "2"));
            } else { // Advanced
                questionList.add(new Question(11, type, "Which phrase is used to politely disagree in a debate?", "A. You are completely wrong.", "B. I see your point, but...", "C. Shut up.", "D. That's a stupid idea.", "2"));
                questionList.add(new Question(12, type, "What does 'Hit the nail on the head' mean in speech?", "A. To be exactly right", "B. To build a house", "C. To get angry", "D. To have a headache", "1"));
            }
        }
        else if (type.contains("Mixed")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_menu_compass); // Icon la bàn (Trộn lẫn)
            if (isBeginner) {
                questionList.add(new Question(13, type, "[Vocab] The opposite of 'Hot' is:", "A. Warm", "B. Cold", "C. Cool", "D. Freezing", "2"));
                questionList.add(new Question(14, type, "[Grammar] I ___ an apple every day.", "A. eat", "B. eats", "C. eating", "D. ate", "1"));
            } else if (isIntermediate) {
                questionList.add(new Question(15, type, "[Reading] Read: 'He was exhausted'. He was...", "A. Very happy", "B. Very hungry", "C. Very tired", "D. Very angry", "3"));
                questionList.add(new Question(16, type, "[Grammar] If I ___ you, I wouldn't do that.", "A. am", "B. was", "C. were", "D. have been", "3"));
            } else { // Advanced
                questionList.add(new Question(17, type, "[Reading] The protagonist's dilemma illustrates...", "A. Moral ambiguity", "B. Pure joy", "C. A simple choice", "D. Ignorance", "1"));
                questionList.add(new Question(18, type, "[Vocab] Choose the synonym for 'Ephemeral':", "A. Permanent", "B. Fleeting", "C. Bright", "D. Heavy", "2"));
            }
        }
        else if (type.contains("Listening")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            if (isBeginner) {
                questionList.add(new Question(19, type, "Listen: 'Hello, my name is Sarah.' What is her name?", "A. Susan", "B. Sarah", "C. Sally", "D. Samantha", "2"));
            } else if (isIntermediate) {
                questionList.add(new Question(20, type, "Listen to the airport announcement. Where is flight 204 going?", "A. London", "B. Paris", "C. Tokyo", "D. New York", "1"));
            } else {
                questionList.add(new Question(21, type, "Listen to the TED Talk excerpt. What is the main thesis?", "A. Technology isolates us.", "B. AI will replace jobs.", "C. Human connection is vital.", "D. Climate change is a myth.", "3"));
            }
        }
        else if (type.contains("Vocabulary")) {
            ivQuizIcon.setImageResource(android.R.drawable.ic_menu_sort_alphabetically);
            if (isBeginner) {
                questionList.add(new Question(22, type, "Which animal says 'Meow'?", "A. Dog", "B. Cat", "C. Cow", "D. Pig", "2"));
            } else if (isIntermediate) {
                questionList.add(new Question(23, type, "A place where you borrow books is a:", "A. Bookstore", "B. Museum", "C. Library", "D. Supermarket", "3"));
            } else {
                questionList.add(new Question(24, type, "What is a 'Philanthropist'?", "A. Stamp collector", "B. Generous donator", "C. Ancient philosopher", "D. Type of doctor", "2"));
            }
        }
        else { // Mặc định là Grammar
            ivQuizIcon.setImageResource(android.R.drawable.ic_menu_edit);
            if (isBeginner) {
                questionList.add(new Question(25, type, "She ___ a student.", "A. am", "B. is", "C. are", "D. be", "2"));
            } else if (isIntermediate) {
                questionList.add(new Question(26, type, "I have been living here ___ 2010.", "A. since", "B. for", "C. in", "D. at", "1"));
            } else {
                questionList.add(new Question(27, type, "Scarcely ___ the room when the phone rang.", "A. he had entered", "B. had he entered", "C. he entered", "D. entered he", "2"));
            }
        }

        // Nếu lỡ không có câu hỏi nào thì nhét 1 câu chống cháy để không bị văng app
        if (questionList.isEmpty()) {
            questionList.add(new Question(99, type, "Chưa có dữ liệu cho phần này!", "A. Test 1", "B. Test 2", "C. Test 3", "D. Test 4", "1"));
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