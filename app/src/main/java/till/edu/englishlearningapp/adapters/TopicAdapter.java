package till.edu.englishlearningapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.FlashcardActivity;
import till.edu.englishlearningapp.models.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private Context context;
    private List<Topic> topicList;

    public TopicAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);

        // Gắn tên chủ đề (tùy theo model Topic của ông dùng getName hay getTitle)
        // Nếu đỏ chỗ getName() thì ông đổi thành getTitle() hoặc thuộc tính tương ứng nha
        holder.tvTopicName.setText(topic.getName());

        // Tạm thời gắn cứng text tiến độ cho đẹp giao diện, mốt có dữ liệu thật mình truyền vào sau
        holder.tvTopicStats.setText("30 Words | Đang học (40%)");

        // Sự kiện click vào thẻ chủ đề sẽ mở Flashcard
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardActivity.class);
            // Truyền ID chủ đề sang trang Flashcard
            intent.putExtra("topicId", topic.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicName;
        TextView tvTopicStats; // Đã đổi ID từ tvWordCount thành tvTopicStats
        ImageView imgTopic;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
            // KHỚP LỆNH ID VỚI FILE XML MỚI Ở ĐÂY NÈ
            tvTopicStats = itemView.findViewById(R.id.tvTopicStats);
            imgTopic = itemView.findViewById(R.id.imgTopic);
        }
    }
}