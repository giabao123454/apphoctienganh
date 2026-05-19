package till.edu.englishlearningapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import till.edu.englishlearningapp.R;

public class RegisterActivity extends AppCompatActivity {

    // LƯU Ý 1: Tui thêm ô nhập tên (edtName)
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    // Khai báo công cụ của Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Thêm Firestore để lưu Data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Ánh xạ giao diện
        // LƯU Ý 2: Ông nhớ vào file activity_register.xml kiểm tra xem đã có ô nhập tên với ID là `edtRegName` chưa nha!
        edtName = findViewById(R.id.edtRegName);
        edtEmail = findViewById(R.id.edtRegEmail);
        edtPassword = findViewById(R.id.edtRegPassword);
        edtConfirmPassword = findViewById(R.id.edtRegConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvBackToLogin);

        // 2. Khởi tạo Firebase Auth & Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 3. Sự kiện bấm nút "Already have an account? Login"
        tvLogin.setOnClickListener(v -> finish()); // Đóng trang Đăng ký, quay về Login

        // 4. Sự kiện bấm nút Register
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirm = edtConfirmPassword.getText().toString().trim();

        // Bắt lỗi nhập thiếu
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bắt lỗi gõ pass 2 lần không giống nhau
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi Firebase tạo tài khoản mới (Auth)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {

                            // 1. Cập nhật DisplayName gốc của Auth (Rất quan trọng)
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            currentUser.updateProfile(profileUpdates);

                            // 2. Tạo "Hồ sơ" cho người dùng mới lên Firestore (Bảng users)
                            Map<String, Object> userParams = new HashMap<>();
                            userParams.put("name", name);
                            userParams.put("email", email);
                            userParams.put("xp", 0);
                            userParams.put("streak", 0);
                            userParams.put("vocab_progress", 0);
                            userParams.put("grammar_progress", 0);
                            userParams.put("speaking_progress", 0);

                            // Đẩy data lên Firestore với ID chính là UID của người dùng
                            db.collection("users").document(currentUser.getUid())
                                    .set(userParams)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        finish(); // Lưu data xong xuôi mới cho lùi về Login
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Lỗi tạo hồ sơ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}