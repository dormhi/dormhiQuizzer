import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class QuestionLoader {

    public ArrayList<Question> loadQuestions(String fileName) {
        ArrayList<Question> loadedQuestions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                String type = parts[0];     // Multi choise or True false
                String text = parts[1];     // Question text
                int score = Integer.parseInt(parts[2]); // Score

                if (type.equals("MC")) {
                    int correctAnswerIndex = Integer.parseInt(parts[3]);
                    ArrayList<String> options = new ArrayList<>();

                    for (int i = 4; i < parts.length; i++) {
                        options.add(parts[i]);
                    }

                    loadedQuestions.add(new MultipleChoiceQuestion(text, score, options, correctAnswerIndex));

                } else if (type.equals("TF")) {  //True/False
                    boolean correctAnswer = Boolean.parseBoolean(parts[3]);
                    loadedQuestions.add(new TrueFalseQuestion(text, score, correctAnswer));
                }
            }
            System.out.println("Questions loaded successfully from " + fileName);

        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
            e.printStackTrace();
        }

        return loadedQuestions;
    }
}