import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private ArrayList<Question> questions;
    private int totalScore;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.totalScore = 0;
    }

    // Method to add a single question to the list
    public void addQuestion(Question q) {
        questions.add(q);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("WELCOME TO THE AREL VADISI QUİZ CHALLENGE!   ");
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
            } else {
                System.out.println(">>> Oops, that's not quite right.");
            }

            System.out.println("-------------------------------------------");
            questionCount++;
        }

        System.out.println("\n*** YOU SUCCESFULLY FINISHED THE QUIZ ***");
        System.out.println("Let's see how you did...");
        System.out.println("Total Score: " + totalScore);

        if (totalScore > 0) {
            System.out.println("Great job!");
        } else {
            System.out.println("Better luck next time!");
        }
    }
}