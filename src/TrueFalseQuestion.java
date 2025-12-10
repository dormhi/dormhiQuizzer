public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String text, int score, boolean correctAnswer) {
        super(text, score);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer(String answer) {

        boolean userAnswer = Boolean.parseBoolean(answer.toLowerCase());


        if (answer.equalsIgnoreCase("T") || answer.equalsIgnoreCase("True")) {
            userAnswer = true;
        } else {
            userAnswer = false;
        }

        return userAnswer == correctAnswer;
    }
}