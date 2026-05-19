package till.edu.englishlearningapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.adapters.LeaderboardAdapter;
import till.edu.englishlearningapp.models.RankUser;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter adapter;
    private List<RankUser> userList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Bấm nút lùi để thoát

        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        adapter = new LeaderboardAdapter(userList);
        rvLeaderboard.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchLeaderboard();
    }

    private void fetchLeaderboard() {
        // Tạm thời TẮT gọi Firebase đi để test giao diện trước
        /*
        db.collection("users")
                .orderBy("xp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        if(name == null) name = "Unknown Player";

                        Long xp = doc.getLong("xp");
                        if(xp == null) xp = 0L;

                        userList.add(new RankUser(name, xp));
                    }
                    adapter.notifyDataSetChanged(); // Báo adapter cập nhật giao diện
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi lấy dữ liệu xếp hạng!", Toast.LENGTH_SHORT).show();
                    Log.e("Leaderboard", "Error", e);
                });
        */

        // BƠM DỮ LIỆU GIẢ (DUMMY DATA) ĐỂ CHIÊM NGƯỠNG GIAO DIỆN
        userList.clear();

        // Nhét tên 3 anh em vào Top 1, 2, 3 luôn cho cháy
        userList.add(new RankUser("Thiên Coder", 9999));
        userList.add(new RankUser("Phong Đẹp Trai", 8500));
        userList.add(new RankUser("Bảo Chùm Bug", 7200));

        // Thêm vài người qua đường cho danh sách nó dài
        userList.add(new RankUser("John Doe", 5000));
        userList.add(new RankUser("Sarah Smith", 4200));
        userList.add(new RankUser("Alex Nguyen", 3100));
        userList.add(new RankUser("Hacker Lỏ", 150));

        // Báo cho cái Adapter biết là "Ê, có data rồi, vẽ lên màn hình đi!"
        adapter.notifyDataSetChanged();
    }
}