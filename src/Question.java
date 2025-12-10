// Gradable interface'ini implemente ediyoruz
public abstract class Question implements Gradable {
    private String text; // Sorunun metni
    private int score;   // Sorunun puan değeri

    public Question(String text, int score) {
        this.text = text;
        this.score = score;
    }

    // Her soru tipinin cevabı kontrol etme şekli farklıdır.
    // O yüzden bu metodu abstract yapıyoruz (Polymorphism hazırlığı).
    public abstract boolean checkAnswer(String answer);

    public String getText() {
        return text;
    }

    // Interface'den gelen metodu dolduruyoruz
    @Override
    public int getScore() {
        return score;
    }
}