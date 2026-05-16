package till.edu.englishlearningapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.fragments.HomeFragment;
import till.edu.englishlearningapp.fragments.ProfileFragment;
import till.edu.englishlearningapp.fragments.QuizFragment;
import till.edu.englishlearningapp.fragments.LearnFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ ID chuẩn với file XML mới
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // 2. Mặc định load trang Home khi vừa mở app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }

        // 3. Bắt sự kiện bấm vào các nút trên thanh Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_learn) {
                // Tab Learn tạm thời gọi tới VocabularyFragment
                selectedFragment = new LearnFragment();
            } else if (id == R.id.nav_quiz) {
                // Tab Quiz gọi tới QuizFragment
                selectedFragment = new QuizFragment();
            } else if (id == R.id.nav_profile) {
                // Tab Profile gọi tới ProfileFragment
                selectedFragment = new ProfileFragment();
            }

            // Thay thế Fragment hiện tại bằng Fragment mới được chọn
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}