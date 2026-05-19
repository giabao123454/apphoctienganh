package till.edu.englishlearningapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import till.edu.englishlearningapp.R;

public class QuizResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // 1. Nhận dữ liệu điểm số từ QuizPlayActivity gửi sang
        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);
        int xp = getIntent().getIntExtra("XP", 0);

        // 2. Ánh xạ giao diện
        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        TextView tvFinalXP = findViewById(R.id.tvFinalXP);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        // 3. Hiển thị dữ liệu lên màn hình
        tvFinalScore.setText(score + " / " + total);
        tvFinalXP.setText("+" + xp + " XP");

        // [Tương lai] Ở đây nhóm ông có thể gọi Firebase để cộng thẳng XP này vào tài khoản User

        // 4. Bấm nút thì đóng trang Kết quả, quay về màn hình trước đó
        btnBackHome.setOnClickListener(v -> finish());
    }
}