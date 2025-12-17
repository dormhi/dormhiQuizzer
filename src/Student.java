public class Student {
    private String name;
    private String studentId;
    private int examScore;

    // Constructor to create a new student
    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.examScore = 0; // Starts with 0 points
    }

    // Basic Getter methods
    public String getName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getExamScore() {
        return examScore;
    }

    // Method to update score
    public void addToScore(int points) {
        if (points > 0) {
            this.examScore += points;
        }
    }
}