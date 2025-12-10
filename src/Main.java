import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("TEST MAIN CLASS\n");

        ArrayList<String> options = new ArrayList<>();
        options.add("Mercury");
        options.add("Venus");
        options.add("Earth");
        options.add("Mars");

        MultipleChoiceQuestion q1 = new MultipleChoiceQuestion(
                "Which planet is known as the Red Planet?",
                10,
                options,
                4
        );

        System.out.println("MULTIPLE CHOICE QUESTION: " + q1.getText());
        q1.displayOptions();

        String answer1 = "4";
        boolean result1 = q1.checkAnswer(answer1);
        System.out.println("Given Answer: " + answer1 + " -> Result: " + (result1 ? "CORRECT" : "WRONG"));
        System.out.println("Score Value: " + q1.getScore());

        System.out.println("\n--------------------------------\n");

        TrueFalseQuestion q2 = new TrueFalseQuestion(
                " The speed of light is approximately 300,000 km per second.",
                5,
                true
        );

        System.out.println("TRUE FALSE QUESTION: " + q2.getText());

        String answer2 = "False";
        boolean result2 = q2.checkAnswer(answer2);
        System.out.println("Answer: " + answer2 + " -> Result: " + (result2 ? "CORRECT" : "WRONG"));

        System.out.println("\n TEST FINISH ");
    }
}
