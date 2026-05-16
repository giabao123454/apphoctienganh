package till.edu.englishlearningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import till.edu.englishlearningapp.R;

public class ProgressFragment extends Fragment {

    public ProgressFragment() {
        // Constructor rỗng bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tạm thời chỉ gọi giao diện ra, dọn sạch code SQLite cũ để hết lỗi
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Chỗ này mốt rảnh anh em mình sẽ viết code kéo điểm số (XP/Streak) từ Firebase về sau
    }
}