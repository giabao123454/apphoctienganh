package till.edu.englishlearningapp.activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import till.edu.englishlearningapp.R;
import till.edu.englishlearningapp.database.DatabaseHelper;
import till.edu.englishlearningapp.models.Word;

public class FlashcardActivity extends AppCompatActivity {

    private TextView tvTopicTitle, tvEnglishWord, tvPhonetic, tvVietnameseMeaning, tvExample;
    private ImageView btnBack, btnFav;
    private MaterialCardView cardFlashcard;
    private Button btnPrev, btnNext;
    private FloatingActionButton btnSpeak;

    private DatabaseHelper dbHelper;
    private List<Word> wordList;
    private int currentIndex = 0;
    private boolean isShowingFront = true;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // 1. UI Mapping
        tvTopicTitle = findViewById(R.id.tvTopicTitle);
        tvEnglishWord = findViewById(R.id.tvEnglishWord);
        tvPhonetic = findViewById(R.id.tvPhonetic);
        tvVietnameseMeaning = findViewById(R.id.tvVietnameseMeaning);
        tvExample = findViewById(R.id.tvExample);
        btnBack = findViewById(R.id.btnBack);
        btnFav = findViewById(R.id.btnFav);
        cardFlashcard = findViewById(R.id.cardFlashcard);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnSpeak = findViewById(R.id.btnSpeak);

        // 2. Intent Data
        String topicId = getIntent().getStringExtra("TOPIC_ID");
        String topicName = getIntent().getStringExtra("TOPIC_NAME");
        if (topicName != null) tvTopicTitle.setText(topicName);

        // 3. TTS Initialization
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) tts.setLanguage(Locale.US);
        });

        // 4. SQLite Load
        dbHelper = new DatabaseHelper(this);
        loadWords(topicId);

        // 5. Click Listeners
        btnBack.setOnClickListener(v -> finish());

        btnSpeak.setOnClickListener(v -> {
            if (!wordList.isEmpty()) {
                tts.speak(wordList.get(currentIndex).getEnglishWord(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < wordList.size() - 1) {
                currentIndex++;
                showWordData();
            } else {
                Toast.makeText(this, "End of unit!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showWordData();
            }
        });

        btnFav.setOnClickListener(v -> toggleFavorite());

        cardFlashcard.setOnClickListener(v -> flipCard());
    }

    private void loadWords(String topicId) {
        if (topicId == null) return;
        
        // Fetch from SQLite
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_WORDS + " WHERE topicId = ?", new String[]{topicId});
        
        wordList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6) == 1
                );
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (!wordList.isEmpty()) {
            currentIndex = 0;
            showWordData();
        } else {
            tvEnglishWord.setText("No data");
            tvPhonetic.setText("");
        }
    }

    private void showWordData() {
        if (wordList.isEmpty()) return;
        Word currentWord = wordList.get(currentIndex);

        isShowingFront = true;
        cardFlashcard.setRotationY(0f);

        tvEnglishWord.setText(currentWord.getEnglishWord());
        tvPhonetic.setText(currentWord.getPhonetic());
        tvVietnameseMeaning.setText(currentWord.getVietnameseMeaning());
        tvExample.setText(currentWord.getExampleSentence());

        updateFavIcon(currentWord.isFavorite());

        tvEnglishWord.setVisibility(View.VISIBLE);
        tvPhonetic.setVisibility(View.VISIBLE);
        tvVietnameseMeaning.setVisibility(View.INVISIBLE);
        tvExample.setVisibility(View.INVISIBLE);
    }

    private void toggleFavorite() {
        if (wordList.isEmpty()) return;
        Word currentWord = wordList.get(currentIndex);
        boolean newStatus = !currentWord.isFavorite();
        currentWord.setFavorite(newStatus);
        
        android.database.sqlite.SQLiteDatabase db = dbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("isFavorite", newStatus ? 1 : 0);
        db.update(DatabaseHelper.TABLE_WORDS, values, "id = ?", new String[]{String.valueOf(currentWord.getId())});
        
        updateFavIcon(newStatus);
        Toast.makeText(this, newStatus ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
    }

    private void updateFavIcon(boolean isFav) {
        btnFav.setImageResource(isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }

    private void flipCard() {
        cardFlashcard.animate().rotationY(90f).setDuration(150).withEndAction(() -> {
            isShowingFront = !isShowingFront;
            if (isShowingFront) {
                tvEnglishWord.setVisibility(View.VISIBLE);
                tvPhonetic.setVisibility(View.VISIBLE);
                tvVietnameseMeaning.setVisibility(View.INVISIBLE);
                tvExample.setVisibility(View.INVISIBLE);
            } else {
                tvEnglishWord.setVisibility(View.INVISIBLE);
                tvPhonetic.setVisibility(View.INVISIBLE);
                tvVietnameseMeaning.setVisibility(View.VISIBLE);
                tvExample.setVisibility(View.VISIBLE);
            }
            cardFlashcard.setRotationY(-90f);
            cardFlashcard.animate().rotationY(0f).setDuration(150).start();
        }).start();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }
}
