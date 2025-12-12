import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private ArrayList<Question> questions;
    private int totalScore;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.totalScore = 0;
    }
}
public void addQuestion(Question q) {
    questions.add(q);
}