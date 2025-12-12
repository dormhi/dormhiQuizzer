import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private ArrayList<Question> questions; // Soruları tutacak liste
    private int totalScore; // Toplam puan

    // Kurucu Metot (Constructor)
    public Quiz() {
        this.questions = new ArrayList<>();
        this.totalScore = 0;
    }
}