package till.edu.englishlearningapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.models.RankUser; // Import từ models

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.RankViewHolder> {

    private List<RankUser> userList;

    public LeaderboardAdapter(List<RankUser> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankUser user = userList.get(position);

        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvUserName.setText(user.getName());
        holder.tvUserXP.setText(user.getXp() + " XP");

        // Tô màu Top 1 (Vàng), Top 2 (Bạc), Top 3 (Đồng)
        if (position == 0) holder.tvRank.setTextColor(Color.parseColor("#FFD700"));
        else if (position == 1) holder.tvRank.setTextColor(Color.parseColor("#C0C0C0"));
        else if (position == 2) holder.tvRank.setTextColor(Color.parseColor("#CD7F32"));
        else holder.tvRank.setTextColor(Color.parseColor("#2C3E50"));
    }

    @Override
    public int getItemCount() { return userList.size(); }

    public static class RankViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUserName, tvUserXP;
        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserXP = itemView.findViewById(R.id.tvUserXP);
        }
    }
}