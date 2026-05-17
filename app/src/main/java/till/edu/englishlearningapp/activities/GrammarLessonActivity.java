package till.edu.englishlearningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import till.edu.englishlearningapp.R;

public class GrammarLessonActivity extends AppCompatActivity {

    private TextView tvGrammarTitle, tvFormula, tvUsage, tvExamples;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_lesson);

        // Nhận tên bài học từ LearnFragment truyền qua
        topicName = getIntent().getStringExtra("TOPIC_NAME");
        if (topicName == null) topicName = "Grammar Lesson";

        // Ánh xạ
        tvGrammarTitle = findViewById(R.id.tvGrammarTitle);
        tvFormula = findViewById(R.id.tvFormula);
        tvUsage = findViewById(R.id.tvUsage);
        tvExamples = findViewById(R.id.tvExamples);
        Button btnPracticeNow = findViewById(R.id.btnPracticeNow);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Cập nhật nội dung bài học theo Chủ đề
        loadLessonContent();

        // Đọc lý thuyết xong thì bấm vào đây để mở Đấu trường Quiz
        btnPracticeNow.setOnClickListener(v -> {
            Intent intent = new Intent(GrammarLessonActivity.this, QuizPlayActivity.class);
            intent.putExtra("QUIZ_TYPE", "Grammar: " + topicName);
            startActivity(intent);
            finish(); // Đóng trang lý thuyết
        });
    }

    private void loadLessonContent() {
        tvGrammarTitle.setText(topicName);

        // Đổ dữ liệu giả lập tùy theo bài học được chọn
        if (topicName.contains("Simple Present")) {
            tvFormula.setText("(+) S + V(s/es) + O\n(-) S + do/does + not + V\n(?) Do/Does + S + V?");
            tvUsage.setText("Diễn tả thói quen, hành động lặp đi lặp lại hoặc sự thật hiển nhiên.");
            tvExamples.setText("1. I go to school every day.\n2. The sun rises in the east.\n3. She doesn't like apples.");
        } else if (topicName.contains("A / An / The")) {
            tvFormula.setText("A / An + Danh từ số ít, đếm được, chưa xác định\nThe + Danh từ đã xác định");
            tvUsage.setText("'A' đứng trước phụ âm, 'An' đứng trước nguyên âm (u, e, o, a, i). 'The' dùng khi cả người nói và người nghe đều biết vật đó là gì.");
            tvExamples.setText("1. I have an apple and a banana.\n2. The apple is red.\n3. He is looking at the moon.");
        } else {
            tvFormula.setText("Đang cập nhật công thức...");
            tvUsage.setText("Đang cập nhật cách sử dụng...");
            tvExamples.setText("Đang cập nhật ví dụ...");
        }
    }
}