public class Student extends User {
    private int examScore;

    public Student(String id, String fullName, String username, String password) {
        super(id, fullName, username, password);
        this.examScore = 0;
    }

    public int getExamScore() {
        return examScore;
    }

    public void addToScore(int points) {
        if (points > 0) {
            this.examScore += points;
        }
    }
}