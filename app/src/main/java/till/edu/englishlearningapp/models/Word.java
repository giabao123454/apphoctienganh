package till.edu.englishlearningapp.models;

public class Word {
    private int id;
    private String topicId;
    private String englishWord;
    private String vietnameseMeaning;
    private String phonetic;
    private String exampleSentence;
    private boolean isFavorite;

    public Word() {}

    public Word(int id, String topicId, String englishWord, String phonetic, String vietnameseMeaning, String exampleSentence, boolean isFavorite) {
        this.id = id;
        this.topicId = topicId;
        this.englishWord = englishWord;
        this.phonetic = phonetic;
        this.vietnameseMeaning = vietnameseMeaning;
        this.exampleSentence = exampleSentence;
        this.isFavorite = isFavorite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTopicId() { return topicId; }
    public void setTopicId(String topicId) { this.topicId = topicId; }

    public String getEnglishWord() { return englishWord; }
    public void setEnglishWord(String englishWord) { this.englishWord = englishWord; }

    public String getVietnameseMeaning() { return vietnameseMeaning; }
    public void setVietnameseMeaning(String vietnameseMeaning) { this.vietnameseMeaning = vietnameseMeaning; }

    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }

    public String getExampleSentence() { return exampleSentence; }
    public void setExampleSentence(String exampleSentence) { this.exampleSentence = exampleSentence; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
