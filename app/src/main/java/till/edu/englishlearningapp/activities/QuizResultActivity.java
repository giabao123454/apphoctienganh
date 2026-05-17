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

        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);
        int xp = getIntent().getIntExtra("XP", 0);

        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        TextView tvFinalXP = findViewById(R.id.tvFinalXP);

        tvFinalScore.setText(score + " / " + total);
        tvFinalXP.setText("+" + xp + " XP");

        // Ở đây mốt nhóm ông viết lệnh update Firebase cộng XP cho User nha

        findViewById(R.id.btnBackHome).setOnClickListener(v -> finish());
    }
}