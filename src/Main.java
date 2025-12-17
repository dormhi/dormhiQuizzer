import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Student currentStudent = new Student("Ahmet Yilmaz", "2023101");

        Quiz myQuiz = new Quiz();

        ArrayList<String> options1 = new ArrayList<>();
        options1.add("Java");
        options1.add("Python");
        options1.add("C++");
        options1.add("HTML");

        MultipleChoiceQuestion q1 = new MultipleChoiceQuestion("Which of the following is an interpreted language?", 10, options1, 2);

        TrueFalseQuestion q2 = new TrueFalseQuestion("In Java, 'int' is a primitive data type.", 5, true);

        ArrayList<String> options2 = new ArrayList<>();
        options2.add("1990");
        options2.add("1995");
        options2.add("2000");
        MultipleChoiceQuestion q3 = new MultipleChoiceQuestion("In which year was Java first released?", 15, options2, 2);


        myQuiz.addQuestion(q1);
        myQuiz.addQuestion(q2);
        myQuiz.addQuestion(q3);


        myQuiz.start(currentStudent);
    }
}