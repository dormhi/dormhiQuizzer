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
public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("BEGIN THE QUIZ");

    for (Question q : questions) {
        System.out.println("QUESTION: " + q.getText());

        if (q instanceof MultipleChoiceQuestion) {
            ((MultipleChoiceQuestion) q).displayOptions();
        }

        System.out.print("ANSWER: ");
        String answer = scanner.nextLine();

        if (q.checkAnswer(answer)) {
            System.out.println("TRUE!");
            totalScore += q.getScore();
        } else {
            System.out.println("FALSE!");
        }
        System.out.println("---------------------");
    }
    System.out.println("TOTAL GRADE: " + totalScore);
}