package till.edu.englishlearningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import till.edu.englishlearningapp.R;

public class LearnFragment extends Fragment {

    public LearnFragment() {
        // Constructor rỗng bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nơi đây sẽ dùng để xử lý logic:
        // - Bắt sự kiện bấm vào Thẻ Vocab -> mở trang Flashcard
        // - Bắt sự kiện bấm vào Thẻ Grammar -> mở danh sách bài học
        // - Lọc nội dung theo Beginner / Intermediate / Advanced
    }
}