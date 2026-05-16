package till.edu.englishlearningapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import till.edu.englishlearningapp.models.Topic;
import till.edu.englishlearningapp.models.Word;
import till.edu.englishlearningapp.models.Question;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EnglishPro.db";
    private static final int DATABASE_VERSION = 1;

    // Table Constants
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TOPICS = "topics";
    public static final String TABLE_WORDS = "words";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_USER_STATS = "user_stats";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Auth Table (Local Profile Cache)
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT UNIQUE, email TEXT, displayName TEXT)");
        
        // Modules Table
        db.execSQL("CREATE TABLE " + TABLE_TOPICS + " (id TEXT PRIMARY KEY, name TEXT, imageUrl TEXT, progress INTEGER DEFAULT 0, wordCount INTEGER, studyTime TEXT)");
        
        // Vocabulary Table
        db.execSQL("CREATE TABLE " + TABLE_WORDS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, topicId TEXT, english TEXT, phonetic TEXT, vietnamese TEXT, example TEXT, isFavorite INTEGER DEFAULT 0)");
        
        // Quiz Table
        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, topicId TEXT, content TEXT, optionA TEXT, optionB TEXT, optionC TEXT, optionD TEXT, correctAnswer TEXT)");
        
        // User Stats Table
        db.execSQL("CREATE TABLE " + TABLE_USER_STATS + " (id INTEGER PRIMARY KEY, userId TEXT, level TEXT, xp INTEGER, streak INTEGER, quizzesDone INTEGER)");

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        // Professional Learning Units
        insertTopic(db, "1", "Unit 1: Essential Phrasal Verbs", 28, "15m Study");
        insertTopic(db, "2", "Unit 2: Business Negotiation", 35, "22m Study");
        insertTopic(db, "3", "Unit 3: Academic Expressions", 42, "30m Study");

        // Premium Vocabulary Content
        insertWord(db, "1", "Carry on", "/ˈkæri ɒn/", "Tiếp tục", "Please carry on with your work.");
        insertWord(db, "1", "Give up", "/ˈɡɪv ʌp/", "Từ bỏ", "He refused to give up the search.");
        insertWord(db, "2", "Negotiate", "/nəˈɡəʊ.ʃi.eɪt/", "Đàm phán", "We managed to negotiate a better price.");

        // Quiz Bank
        insertQuestion(db, "1", "Synonym of 'Carry on'?", "Stop", "Continue", "Finish", "Pause", "B");

        // Initial Progress Cache
        ContentValues stats = new ContentValues();
        stats.put("userId", "default_user");
        stats.put("level", "B1 Inter");
        stats.put("xp", 2450);
        stats.put("streak", 12);
        stats.put("quizzesDone", 48);
        db.insert(TABLE_USER_STATS, null, stats);
    }

    private void insertTopic(SQLiteDatabase db, String id, String name, int count, String time) {
        ContentValues v = new ContentValues();
        v.put("id", id); v.put("name", name); v.put("wordCount", count); v.put("studyTime", time);
        db.insert(TABLE_TOPICS, null, v);
    }

    private void insertWord(SQLiteDatabase db, String tId, String en, String ph, String vi, String ex) {
        ContentValues v = new ContentValues();
        v.put("topicId", tId); v.put("english", en); v.put("phonetic", ph); v.put("vietnamese", vi); v.put("example", ex);
        db.insert(TABLE_WORDS, null, v);
    }

    private void insertQuestion(SQLiteDatabase db, String tId, String c, String a, String b, String c2, String d, String cor) {
        ContentValues v = new ContentValues();
        v.put("topicId", tId); v.put("content", c); v.put("optionA", a); v.put("optionB", b); v.put("optionC", c2); v.put("optionD", d); v.put("correctAnswer", cor);
        db.insert(TABLE_QUESTIONS, null, v);
    }

    public List<Topic> getAllTopics() {
        List<Topic> list = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TOPICS, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Topic t = new Topic(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    t.setProgress(cursor.getInt(3));
                    t.setWordCount(cursor.getInt(4));
                    t.setStudyTime(cursor.getString(5));
                    list.add(t);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    public List<Word> getFavoriteWords() {
        List<Word> list = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_WORDS + " WHERE isFavorite = 1", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), true));
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_QUESTIONS, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(new Question(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    public Cursor getUserStats(String userId) {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_USER_STATS + " WHERE userId = ?", new String[]{userId});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_STATS);
        onCreate(db);
    }
}
