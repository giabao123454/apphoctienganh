package till.edu.englishlearningapp.models;

public class Topic {
    private String id;
    private String name;
    private String imageUrl;
    private int progress;
    private int wordCount;
    private String studyTime;

    public Topic() {}

    public Topic(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }

    public String getStudyTime() { return studyTime; }
    public void setStudyTime(String studyTime) { this.studyTime = studyTime; }
}
