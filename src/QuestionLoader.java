import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;

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

    public void appendQuestion(String csvLine) {
        java.io.File file = new java.io.File("questions.csv");

        boolean fileExistsAndHasData = file.exists() && file.length() > 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            if (fileExistsAndHasData) {
                bw.newLine();
            }
            bw.write(csvLine);

            System.out.println("Soru basariyla veritabani dosyasina eklendi!");

        } catch (IOException e) {
            System.out.println("Soru kaydedilirken hata olustu: " + e.getMessage());
        }
    }
    public List<String> getAllRawQuestions() {
        try {
            return Files.readAllLines(Paths.get("questions.csv"));
        } catch (IOException e) {
            System.out.println("Dosya okunurken hata: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void deleteQuestionAt(int index) {
        try {
            List<String> lines = getAllRawQuestions();

            if (index >= 0 && index < lines.size()) {
                String removed = lines.remove(index);

                Files.write(Paths.get("questions.csv"), lines);
                System.out.println("Soru silindi: " + removed.split(";")[1]);
            } else {
                System.out.println("HATA: Gecersiz soru numarasi!");
            }
        } catch (IOException e) {
            System.out.println("Silme isleminde hata: " + e.getMessage());
        }
    }
}