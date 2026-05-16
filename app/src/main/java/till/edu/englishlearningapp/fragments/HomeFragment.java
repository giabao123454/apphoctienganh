package till.edu.englishlearningapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.adapters.TopicAdapter;
import till.edu.englishlearningapp.models.Topic;

public class HomeFragment extends Fragment {

    private RecyclerView rvTopics;
    private TopicAdapter topicAdapter;
    private List<Topic> topicList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView tvUserName, tvMotivationalQuote;

    public HomeFragment() {
        // Constructor rỗng bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 1. Ánh xạ giao diện phần Cá nhân hóa (Khớp với giao diện Dribbble mới)
        tvUserName = view.findViewById(R.id.tvUserName);
        tvMotivationalQuote = view.findViewById(R.id.tvMotivationalQuote);

        // 2. Kích hoạt tính năng: Tự động lấy tên User từ Firebase để vẫy chào
        setupGreeting();

        // 3. Kích hoạt tính năng: Random câu Quote truyền cảm hứng
        setupMotivationalQuote();

        // 4. Setup danh sách Topic (Recommended Units)
        rvTopics = view.findViewById(R.id.rvTopics);
        rvTopics.setLayoutManager(new LinearLayoutManager(getContext()));
        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(getContext(), topicList);
        rvTopics.setAdapter(topicAdapter);

        // Kéo dữ liệu từ Firebase về
        loadTopicsFromFirebase();
    }

    // Hàm lấy tên từ Email (VD: bao.nguyen@gmail.com -> Bao.nguyen)
    private void setupGreeting() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null && email.contains("@")) {
                // Lấy phần tên trước chữ @ làm tên hiển thị
                String name = email.substring(0, email.indexOf("@"));
                // Chữ cái đầu viết hoa cho lịch sự
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                tvUserName.setText(name + " 👋");
            }
        }
    }

    // Hàm random câu quote như yêu cầu
    private void setupMotivationalQuote() {
        String[] quotes = {
                "\"Small steps every day lead to big results.\"",
                "\"Practice makes progress.\"",
                "\"Never stop learning English.\"",
                "\"Mistakes are proof that you are trying.\"",
                "\"The secret of getting ahead is getting started.\""
        };
        // Lấy ngẫu nhiên 1 câu
        Random random = new Random();
        int index = random.nextInt(quotes.length);
        if (tvMotivationalQuote != null) {
            tvMotivationalQuote.setText(quotes[index]);
        }
    }

    private void loadTopicsFromFirebase() {
        db.collection("topics")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        topicList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Topic topic = document.toObject(Topic.class);
                            topicList.add(topic);
                        }
                        topicAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firebase", "Lỗi tải danh sách chủ đề: ", task.getException());
                    }
                });
    }
}