package till.edu.englishlearningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import till.edu.englishlearningapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Ánh xạ giao diện
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // 2. Khởi tạo Firebase Auth (Thay vì dùng DatabaseHelper cũ)
        mAuth = FirebaseAuth.getInstance();

        // 3. Sự kiện chuyển sang trang Đăng ký
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // 4. Sự kiện bấm nút Đăng nhập
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra rỗng
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi Firebase để kiểm tra đăng nhập
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập đúng -> Chuyển sang MainActivity (Trang chủ)
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Đóng trang Login lại
                    } else {
                        // Đăng nhập sai
                        Toast.makeText(LoginActivity.this, "Sai Email hoặc Mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Hàm này sẽ tự động chạy lên đầu tiên khi mở trang Login
    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra xem đã có user nào đăng nhập trước đó chưa
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Có người đăng nhập rồi -> Chuyển thẳng sang trang MainActivity luôn
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng luôn trang Login để không bấm Back quay lại được
        }
    }
}