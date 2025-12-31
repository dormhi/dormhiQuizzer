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
                // 1. Check if line is empty
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                // 2. SAFETY CHECK: Do we have enough data?
                // A valid line needs at least: Type;Text;Score (3 parts)
                if (parts.length < 3) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String type = parts[0];
                String text = parts[1];

                // Safety for number parsing
                int score = 0;
                try {
                    score = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid score format in line: " + line);
                    continue;
                }

                if (type.equals("MC")) {
                    // MC needs at least 5 parts: Type;Text;Score;AnswerIndex;Option1...
                    if (parts.length < 5) {
                        System.out.println("Skipping incomplete MC question: " + line);
                        continue;
                    }

                    int correctAnswerIndex = Integer.parseInt(parts[3]);
                    ArrayList<String> options = new ArrayList<>();

                    for (int i = 4; i < parts.length; i++) {
                        options.add(parts[i]);
                    }

                    loadedQuestions.add(new MultipleChoiceQuestion(text, score, options, correctAnswerIndex));

                } else if (type.equals("TF")) {
                    // TF needs 4 parts: Type;Text;Score;Answer
                    if (parts.length < 4) {
                        System.out.println("Skipping incomplete TF question: " + line);
                        continue;
                    }
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