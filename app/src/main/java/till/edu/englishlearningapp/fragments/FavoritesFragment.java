package till.edu.englishlearningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import till.edu.englishlearningapp.R;

public class FavoritesFragment extends Fragment {

    public FavoritesFragment() {
        // Constructor rỗng bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tạm thời chỉ gọi giao diện ra, chưa xử lý logic gì để tránh lỗi
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Phần logic của màn hình Favorites tụi mình sẽ code sau
    }
}