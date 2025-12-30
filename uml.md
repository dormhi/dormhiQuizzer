## UML DİAGRAM
```mermaid
classDiagram
    class ICsvConvertible {
        <<interface>>
        +toCsvString() String
    }

    class User {
        <<abstract>>
        -String id
        -String fullName
        -String username
        -String password
        +toCsvString() String
    }

    class Student {
        -int examScore
        +addToScore(int)
        +getExamScore() int
    }

    class Teacher {
        +addQuestion()
    }

    class Admin {
        +manageIDs()
    }

    class Question {
        <<abstract>>
        -String text
        -int score
        +checkAnswer(String) boolean
    }

    class MultipleChoiceQuestion {
        -List~String~ options
        -int correctOptionIndex
        +checkAnswer(String) boolean
    }

    class TrueFalseQuestion {
        -String correctAnswer
        +checkAnswer(String) boolean
    }

    class AuthService {
        +login(String, String) User
        +register(String, String, String, String) boolean
    }

    User ..|> ICsvConvertible
    Student --|> User
    Teacher --|> User
    Admin --|> User
    MultipleChoiceQuestion --|> Question
    TrueFalseQuestion --|> Question
    AuthService ..> User : Creates