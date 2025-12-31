/**
 * Class representing a university student.
 * Derived from the User class and additionally holds exam score data.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025-12-08
 */
public class Student extends User {
    private int examScore;

    /**
     * Creates a new student.
     * Initial exam score is set to 0.
     *
     * @param id       Student School ID
     * @param fullName Full Name
     * @param username System Username
     * @param password Login Password
     */
    public Student(String id, String fullName, String username, String password) {
        super(id, fullName, username, password);
        this.examScore = 0;
    }

    /**
     * Adds to the student's current exam score.
     * Prevents adding negative scores.
     *
     * @param score Amount of points to add (must be positive)
     */
    public void addToScore(int score) {
        if (score > 0) {
            this.examScore += score;
        }
    }

    /**
     * Returns the student's current exam score.
     *
     * @return Total score
     */
    public int getExamScore() {
        return examScore;
    }
}