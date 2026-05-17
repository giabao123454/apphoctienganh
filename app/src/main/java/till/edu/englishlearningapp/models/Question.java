package till.edu.englishlearningapp.models;

public class Question {
    private int id;
    private String category; // Có thể database của ông lưu Thể loại ở cột 1
    private String questionText;
    private String optionA, optionB, optionC, optionD;
    private String correctAnswer; // Database trả về String (cột 7)

    // Constructor mới mở đủ 8 "lỗ cắm" để nhận đúng dữ liệu từ cursor của ông
    public Question(int id, String category, String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.id = id;
        this.category = category;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    // Các hàm Getters cơ bản
    public int getId() { return id; }
    public String getCategory() { return category; }
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }

    // 🔥 HÀM MA THUẬT: Tự động dịch đáp án từ DB (String) sang vị trí nút (int) cho QuizPlayActivity
    public int getCorrectAnswerIndex() {
        if (correctAnswer == null) return 1; // Mặc định tránh crash

        // Trường hợp 1: DB của ông lưu là "1", "2", "3", "4"
        if (correctAnswer.equals("1")) return 1;
        if (correctAnswer.equals("2")) return 2;
        if (correctAnswer.equals("3")) return 3;
        if (correctAnswer.equals("4")) return 4;

        // Trường hợp 2: DB của ông lưu là "A", "B", "C", "D"
        if (correctAnswer.equalsIgnoreCase("A")) return 1;
        if (correctAnswer.equalsIgnoreCase("B")) return 2;
        if (correctAnswer.equalsIgnoreCase("C")) return 3;
        if (correctAnswer.equalsIgnoreCase("D")) return 4;

        // Trường hợp 3: DB của ông lưu chính xác chữ của đáp án (VD: "Went")
        if (correctAnswer.equalsIgnoreCase(optionA)) return 1;
        if (correctAnswer.equalsIgnoreCase(optionB)) return 2;
        if (correctAnswer.equalsIgnoreCase(optionC)) return 3;
        if (correctAnswer.equalsIgnoreCase(optionD)) return 4;

        // Nếu ép kiểu trực tiếp được thì ép
        try {
            return Integer.parseInt(correctAnswer);
        } catch (NumberFormatException e) {
            return 1; // Mặc định nếu có lỗi data
        }
    }
}