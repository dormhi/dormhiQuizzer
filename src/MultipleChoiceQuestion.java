import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {
    private ArrayList<String> options;
    private int correctOptionIndex;

    public MultipleChoiceQuestion(String text, int score, ArrayList<String> options, int correctOptionIndex) {
        super(text, score);
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public void displayOptions() {
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

    @Override
    public boolean checkAnswer(String answer) {
        try {
            int answerIndex = Integer.parseInt(answer);
            return answerIndex == correctOptionIndex;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}