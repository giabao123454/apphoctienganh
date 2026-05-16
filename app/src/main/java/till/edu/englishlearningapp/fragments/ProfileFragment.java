package till.edu.englishlearningapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.LoginActivity;

public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private MaterialButton btnLogout;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Constructor rỗng
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ ID
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Hiển thị thông tin người dùng đang đăng nhập
        loadUserProfile();

        // Xử lý sự kiện Đăng xuất (Logout)
        btnLogout.setOnClickListener(v -> {
            // Đăng xuất khỏi hệ thống Firebase
            mAuth.signOut();
            Toast.makeText(getContext(), "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển hướng về trang LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);

            // Xóa toàn bộ lịch sử các trang trước đó để không bấm Back quay lại app được
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Đóng Activity hiện tại
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                // Hiển thị email
                tvUserEmail.setText(email);

                // Cắt tên từ email (VD: nguyenbao@gmail.com -> Nguyenbao)
                if (email.contains("@")) {
                    String name = email.substring(0, email.indexOf("@"));
                    // Viết hoa chữ cái đầu tiên cho đẹp
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    tvUserName.setText(name);
                }
            }
        }
    }
}