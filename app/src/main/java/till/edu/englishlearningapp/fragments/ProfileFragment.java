package till.edu.englishlearningapp.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.activities.LoginActivity;

public class ProfileFragment extends Fragment {

    // --- UI COMPONENTS ---
    private ImageView ivAvatar;
    private TextView tvUsername, tvStreak, tvTotalXP;
    private TextView tvVocabPct, tvGrammarPct, tvSpeakPct;
    private ProgressBar pbVocab, pbGrammar, pbSpeaking;
    private MaterialCardView cardPremium;

    private TextView btnLogout, btnEditProfile, btnNotifications;

    // --- FIREBASE ---
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    // --- IMAGE PICKER ---
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        initViews(view);
        setupAnimations();
        setupClickListeners();
        setupImagePicker();

        // Thay vì load 1 lần, ta dùng Real-time Listener để data nhảy liên tục
        if (currentUser != null) {
            setupRealtimeSync();
        }
    }

    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvStreak = view.findViewById(R.id.tvStreak);
        tvTotalXP = view.findViewById(R.id.tvTotalXP);

        tvVocabPct = view.findViewById(R.id.tvVocabPct);
        tvGrammarPct = view.findViewById(R.id.tvGrammarPct);
        tvSpeakPct = view.findViewById(R.id.tvSpeakPct);

        pbVocab = view.findViewById(R.id.pbVocab);
        pbGrammar = view.findViewById(R.id.pbGrammar);
        pbSpeaking = view.findViewById(R.id.pbSpeaking);

        cardPremium = view.findViewById(R.id.cardPremium);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnNotifications = view.findViewById(R.id.btnNotifications);
    }

    // --- 1. FIREBASE REAL-TIME SYNC ---
    private void setupRealtimeSync() {
        String uid = currentUser.getUid();

        // Lắng nghe thay đổi liên tục từ Firestore
        db.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ProfileFragment", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // 1. LẤY TÊN CHUẨN XÁC
                    String firestoreName = snapshot.getString("name");
                    String authName = currentUser.getDisplayName();

                    // Ưu tiên lấy tên từ Firestore, nếu không có thì lấy từ Auth, bí quá mới lấy Email
                    if (firestoreName != null && !firestoreName.isEmpty()) {
                        tvUsername.setText(firestoreName);
                    } else if (authName != null && !authName.isEmpty()) {
                        tvUsername.setText(authName);
                    } else {
                        // Cắt bỏ phần @gmail.com để tên hiển thị đẹp hơn
                        String email = currentUser.getEmail();
                        if (email != null && email.contains("@")) {
                            tvUsername.setText(email.substring(0, email.indexOf("@")));
                        } else {
                            tvUsername.setText("Learner");
                        }
                    }

                    // 2. Hiệu ứng nhảy số cho XP và Streak
                    Long xp = snapshot.getLong("xp");
                    Long streak = snapshot.getLong("streak");
                    if (xp != null) animateNumberCounting(tvTotalXP, xp, " XP");
                    if (streak != null) animateNumberCounting(tvStreak, streak, " Days");

                    // 3. Hiệu ứng trượt Progress Bar mượt mà
                    Long vocabProgress = snapshot.getLong("vocab_progress");
                    Long grammarProgress = snapshot.getLong("grammar_progress");
                    Long speakingProgress = snapshot.getLong("speaking_progress");

                    if (vocabProgress != null) animateProgressBar(pbVocab, tvVocabPct, vocabProgress.intValue());
                    if (grammarProgress != null) animateProgressBar(pbGrammar, tvGrammarPct, grammarProgress.intValue());
                    if (speakingProgress != null) animateProgressBar(pbSpeaking, tvSpeakPct, speakingProgress.intValue());
                }
            }
        });
    }

    // --- 2. SMOOTH ANIMATIONS (Counting & Progress) ---
    private void animateNumberCounting(TextView textView, long targetValue, String suffix) {
        // Lấy giá trị hiện tại để chạy hiệu ứng (bỏ chữ cái đi)
        String currentText = textView.getText().toString().replaceAll("[^0-9]", "");
        long startValue = currentText.isEmpty() ? 0 : Long.parseLong(currentText);

        if (startValue == targetValue) return; // Không cần chạy nếu data không đổi

        ValueAnimator animator = ValueAnimator.ofFloat(startValue, targetValue);
        animator.setDuration(1000); // Chạy trong 1 giây
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            textView.setText(Math.round(animatedValue) + suffix);
        });
        animator.start();
    }

    private void animateProgressBar(ProgressBar progressBar, TextView percentageText, int targetProgress) {
        int currentProgress = progressBar.getProgress();
        if (currentProgress == targetProgress) return;

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", currentProgress, targetProgress);
        animation.setDuration(1200); // Trượt mượt trong 1.2s
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        // Cập nhật text % song song
        ValueAnimator textAnimator = ValueAnimator.ofInt(currentProgress, targetProgress);
        textAnimator.setDuration(1200);
        textAnimator.addUpdateListener(anim -> {
            percentageText.setText(anim.getAnimatedValue() + "%");
        });
        textAnimator.start();
    }

    // --- 3. TOUCH & SCALING UX INTERACTION ---
    private void setupAnimations() {
        setClickScalingAnimation(ivAvatar);
        setClickScalingAnimation(cardPremium);
        setClickScalingAnimation(btnEditProfile);
        setClickScalingAnimation(btnNotifications);
        setClickScalingAnimation(btnLogout);
    }

    private void setClickScalingAnimation(View view) {
        if (view == null) return;
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }

    // --- 4. FUNCTIONAL CLICK LISTENERS ---
    private void setupClickListeners() {
        // Mở thư viện đổi Avatar
        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Mở màn hình Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Opening Edit Profile...", Toast.LENGTH_SHORT).show();
        });

        // Mở thẻ Premium
        cardPremium.setOnClickListener(v -> {
            Toast.makeText(getContext(), "🚀 Upgrade to Premium!", Toast.LENGTH_SHORT).show();
        });

        // Log out với Animation
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // Thêm hiệu ứng trượt màn hình thoát app
            if (getActivity() != null) {
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
            }
        });
    }

    // --- 5. IMAGE PICKER HANDLER ---
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ivAvatar.setImageURI(selectedImage);
                        Toast.makeText(getContext(), "Avatar updated! Syncing to Firebase...", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}