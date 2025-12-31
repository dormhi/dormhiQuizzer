import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

public class QuestionLoader {

    public ArrayList<Question> loadQuestions(String fileName) {
        ArrayList<Question> loadedQuestions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                String type = parts[0];     // Multiple Choice (MC) or True/False (TF)
                String text = parts[1];     // Question text
                int score = Integer.parseInt(parts[2]); // Score

                if (type.equals("MC")) {
                    int correctAnswerIndex = Integer.parseInt(parts[3]);
                    ArrayList<String> options = new ArrayList<>();

                    for (int i = 4; i < parts.length; i++) {
                        options.add(parts[i]);
                    }

                    loadedQuestions.add(new MultipleChoiceQuestion(text, score, options, correctAnswerIndex));

                } else if (type.equals("TF")) {  // True/False
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
        File file = new File("questions.csv");

        // Check if file exists and has content to manage new lines correctly
        boolean fileExistsAndHasData = file.exists() && file.length() > 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            // If file has data, move to next line before writing to prevent appending to the last line
            if (fileExistsAndHasData) {
                bw.newLine();
            }
            bw.write(csvLine);

            System.out.println("Question successfully added to the database file!");

        } catch (IOException e) {
            System.out.println("Error occurred while saving the question: " + e.getMessage());
        }
    }

    public List<String> getAllRawQuestions() {
        try {
            return Files.readAllLines(Paths.get("questions.csv"));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    public void deleteQuestionAt(int index) {
        try {
            List<String> lines = getAllRawQuestions();

            if (index >= 0 && index < lines.size()) {
                String removed = lines.remove(index); // Remove from list

                Files.write(Paths.get("questions.csv"), lines);
                // Extract question text for the log (assuming format is TYPE;TEXT;...)
                System.out.println("Question deleted: " + removed.split(";")[1]);
            } else {
                System.out.println("ERROR: Invalid question number!");
            }
        } catch (IOException e) {
            System.out.println("Error during deletion: " + e.getMessage());
        }
    }
}