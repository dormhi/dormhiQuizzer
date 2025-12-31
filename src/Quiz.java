//student.getName() changed to student.getFullName()
import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private ArrayList<Question> questions;
    private int totalScore;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.totalScore = 0;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public void start(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("WELCOME THE QUIZ CHALLENCE:  " + student.getFullName().toUpperCase() + " (" + student.getFullName() + ")");
        System.out.println("Get ready! Here come the questions...\n");

        int questionCount = 1;

        for (Question q : questions) {

            System.out.println("Question " + questionCount + ": " + q.getText());
            System.out.println("[Points: " + q.getScore() + "]");

            if (q instanceof MultipleChoiceQuestion) {
                ((MultipleChoiceQuestion) q).displayOptions();
                System.out.print("Your choice (1, 2, 3...): ");
            } else {
                System.out.print("Your answer (True/False): ");
            }

            String userAnswer = scanner.nextLine();

            if (q.checkAnswer(userAnswer)) {
                System.out.println(">>> Spot on! That's correct. (+" + q.getScore() + " pts)");
                totalScore += q.getScore();
                student.addToScore(q.getScore());
            } else {
                System.out.println(">>> Oops, that's not quite right.");
            }

            System.out.println("-");
            questionCount++;
        }

        System.out.println("\n*** QUIZ FINISHED ***");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Final Score: " + student.getExamScore());

        if (student.getExamScore() > 10) {
            System.out.println("Great job!");
        } else {
            System.out.println("Better luck next time!");
        }
    }
}