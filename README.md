# Quiz System for OOP Project 🎓
# My procect title(Arel Vadisi Quiz challence)

introduction= That ia a sample quiz sytem

A comprehensive, console-based Quiz Management System developed in Java. This project demonstrates core Object-Oriented Programming (OOP) principles including Inheritance, Polymorphism, Encapsulation, and Interface implementation.

## 🚀 Features

* **Role-Based Access Control:** Secure login for Admin, Teacher, and Student roles.
* **User Management:**
    * **Admin:** Manage authorized IDs (add/remove/list permissions).
    * **Teacher:** Create questions (Multiple Choice / True-False) and manage question bank.
    * **Student:** Take quizzes and view scores.
* **Dynamic Data Storage:** All data (users, questions, permissions) is persisted in CSV files (No database setup required).
* **Quiz Engine:** Randomly presents questions and calculates scores automatically.

## 🛠️ Technical Details

This project strictly follows OOP standards:
* **Encapsulation:** All data fields are private and accessed via getters/setters.
* **Polymorphism:** `Question` class is extended by `MultipleChoiceQuestion` and `TrueFalseQuestion`.
* **Inheritance:** `User` base class is extended by `Student`, `Teacher`, and `Admin`.
* **Interface:** `ICsvConvertible` interface is used for data serialization.
* **File I/O:** Custom `IDManager` and `QuestionLoader` services handle file operations.
* **Unit Testing:** JUnit 5 is used for testing core logic (`IDManagerTest`, `StudentTest`, etc.).

## 📂 Project Structure

* `src/model` -> Entity classes (User, Student, Question...)
* `src/service` -> Business logic (AuthService, IDManager...)
* `src/ui` -> Console interface (MenuSystem)

##  ▶️ How to Run

1.  Clone this repository.
2.  Open the project in IntelliJ IDEA.
3.  Run `Main.java`.
4.  **Login Credentials:**
    * **Admin:** `admin` / `admin123`
    * **Teacher/Student:** Register via the menu using an allowed ID.

## 🧪 Testing

Run the tests located in the `test` folder to verify system integrity:
* `IDManagerTest`: Checks admin operations.
* `StudentTest`: Verifies score calculation logic.

---
*Developed for Object Oriented Programming Course Final Project.*