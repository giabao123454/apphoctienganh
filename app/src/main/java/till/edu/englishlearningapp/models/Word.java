package till.edu.englishlearningapp.models;

public class Word {
    private String id;
    private String english;
    private String vietnamese;
    private String phonetic;
    private String imageUrl;
    private String topicId; // Dùng để biết từ này thuộc chủ đề nào (Animals, Food...)
    private boolean isFavorite; // Trạng thái từ yêu thích

    // 1. Constructor rỗng - CỰC KỲ QUAN TRỌNG để Firebase có thể đổ dữ liệu vào
    public Word() {
    }

    // 2. Constructor đầy đủ để mình tạo đối tượng mới khi cần
    public Word(String id, String english, String vietnamese, String phonetic, String imageUrl, String topicId) {
        this.id = id;
        this.english = english;
        this.vietnamese = vietnamese;
        this.phonetic = phonetic;
        this.imageUrl = imageUrl;
        this.topicId = topicId;
        this.isFavorite = false; // Mặc định từ mới chưa phải là từ yêu thích
    }

    // 3. Các hàm Getter và Setter (Tính đóng gói trong OOP)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEnglish() { return english; }
    public void setEnglish(String english) { this.english = english; }

    public String getVietnamese() { return vietnamese; }
    public void setVietnamese(String vietnamese) { this.vietnamese = vietnamese; }

    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTopicId() { return topicId; }
    public void setTopicId(String topicId) { this.topicId = topicId; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
