package till.edu.englishlearningapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import till.edu.englishlearningapp.R;

public class QuizPlayActivity extends AppCompatActivity {

    private TextView tvTimer, tvQuestionCategory;
    private MaterialButton btnAns1, btnAns2, btnAns3, btnAns4;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_play);

        // Lấy tên Loại Đề (Vocabulary, Grammar...) từ Tab Quiz truyền qua
        String quizType = getIntent().getStringExtra("QUIZ_TYPE");

        // Ánh xạ giao diện
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionCategory = findViewById(R.id.tvQuestionCategory);
        btnAns1 = findViewById(R.id.btnAns1);
        btnAns2 = findViewById(R.id.btnAns2);
        btnAns3 = findViewById(R.id.btnAns3);
        btnAns4 = findViewById(R.id.btnAns4);

        Button btnHint = findViewById(R.id.btnHint);
        Button btnSkip = findViewById(R.id.btnSkip);
        ImageView btnClose = findViewById(R.id.btnClose);

        // Hiển thị loại đề lên góc phải
        if (quizType != null) {
            tvQuestionCategory.setText(quizType);
        }

        // Bấm nút X là thoát khỏi phòng thi
        btnClose.setOnClickListener(v -> finish());

        // Bắt đầu đếm ngược 30 giây
        startTimer();

        // Xử lý nút đáp án (Giả sử câu B đúng)
        btnAns1.setOnClickListener(v -> checkAnswer(btnAns1, false));
        btnAns2.setOnClickListener(v -> checkAnswer(btnAns2, true));
        btnAns3.setOnClickListener(v -> checkAnswer(btnAns3, false));
        btnAns4.setOnClickListener(v -> checkAnswer(btnAns4, false));

        btnHint.setOnClickListener(v -> Toast.makeText(this, "Hint: Bắt đầu bằng chữ 'W'", Toast.LENGTH_SHORT).show());
        btnSkip.setOnClickListener(v -> Toast.makeText(this, "Bỏ qua! -5 XP", Toast.LENGTH_SHORT).show());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvTimer.setText(String.format("00:%02d", seconds));
                // Còn 5 giây thì chuyển sang chữ màu đỏ cảnh báo
                if (seconds <= 5) tvTimer.setTextColor(Color.parseColor("#E74C3C"));
            }

            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(QuizPlayActivity.this, "Hết giờ!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void checkAnswer(MaterialButton btn, boolean isCorrect) {
        // Tắt đồng hồ ngay lập tức
        if (countDownTimer != null) countDownTimer.cancel();

        if (isCorrect) {
            btn.setBackgroundColor(Color.parseColor("#4CAF50")); // Màu xanh lá
            btn.setTextColor(Color.WHITE);
            Toast.makeText(this, "Chính xác! +10 XP", Toast.LENGTH_SHORT).show();
        } else {
            btn.setBackgroundColor(Color.parseColor("#F44336")); // Màu đỏ
            btn.setTextColor(Color.WHITE);
            // Khoe đáp án đúng ra cho người ta biết
            btnAns2.setBackgroundColor(Color.parseColor("#4CAF50"));
            btnAns2.setTextColor(Color.WHITE);
            Toast.makeText(this, "Sai rồi nha!", Toast.LENGTH_SHORT).show();
        }

        // Khóa sạch các nút không cho bấm nữa
        btnAns1.setEnabled(false);
        btnAns2.setEnabled(false);
        btnAns3.setEnabled(false);
        btnAns4.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Thoát trang thì phải dẹp đồng hồ đi không là Crash máy
        if (countDownTimer != null) countDownTimer.cancel();
    }
}