import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Student currentStudent = new Student("Ahmet Yilmaz", "2023101"); //just example. it will be upgraded

        Quiz myQuiz = new Quiz();

        QuestionLoader loader = new QuestionLoader();
        ArrayList<Question> fileQuestions = loader.loadQuestions("questions.csv"); // new question databasse

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