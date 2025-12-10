// Gradable interface
public abstract class Question implements Gradable {
    private String text;
    private int score;

    public Question(String text, int score) {
        this.text = text;
        this.score = score;
    }

    public abstract boolean checkAnswer(String answer);

    public String getText() {
        return text;
    }

    @Override
    public int getScore() {
        return score;
    }
}