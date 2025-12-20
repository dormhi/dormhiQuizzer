//new requirements addded = ıd, name, username, password
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Student currentStudent = new Student("2023101", "Ahmet Yilmaz", "ahmet123", "pass1234");
// Format: ID, Name, Username, Password
        Quiz myQuiz = new Quiz();

        QuestionLoader loader = new QuestionLoader();
        ArrayList<Question> fileQuestions = loader.loadQuestions("questions.csv");

        for (Question q : fileQuestions) {
            myQuiz.addQuestion(q);
        }

        if (fileQuestions.size() > 0) {
            myQuiz.start(currentStudent);
        } else {
            System.out.println("No questions found. Please check questions.csv file.");
        }
    }
}