package till.edu.englishlearningapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import till.edu.englishlearningapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    // Khai báo công cụ của Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Ánh xạ giao diện CHUẨN KHỚP VỚI ID BÊN XML
        edtEmail = findViewById(R.id.edtRegEmail);
        edtPassword = findViewById(R.id.edtRegPassword);
        edtConfirmPassword = findViewById(R.id.edtRegConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvBackToLogin);

        // 2. Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Sự kiện bấm nút "Already have an account? Login"
        tvLogin.setOnClickListener(v -> finish()); // Đóng trang Đăng ký, quay về Login

        // 4. Sự kiện bấm nút Register
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirm = edtConfirmPassword.getText().toString().trim();

        // Bắt lỗi nhập thiếu
        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bắt lỗi gõ pass 2 lần không giống nhau
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi Firebase tạo tài khoản mới
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Tạo xong thì tự động quay về trang Login
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}