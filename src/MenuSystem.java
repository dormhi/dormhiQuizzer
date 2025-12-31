import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class MenuSystem {
    private AuthService authService;
    private Scanner scanner;

    public MenuSystem() {
        this.authService = new AuthService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n===========================================");
            System.out.println("   WELCOME TO QUIZ SYSTEM ");
            System.out.println("===========================================");
            System.out.println("   V1.0  CREATED BY DORMHI ");
            System.out.println("===========================================");
            System.out.println("1. I AM A TEACHER");
            System.out.println("2. I AM A STUDENT");
            System.out.println("3. I AM AN ADMINISTRATOR");
            System.out.println("0. EXIT");
            System.out.print("Select option (0-3): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showTeacherMenu();
                    break;
                case "2":
                    showStudentMenu();
                    break;
                case "3":
                    performLogin("ADMIN");
                    break;
                case "0":
                    System.out.println("Quitting system. Goodbye!");
                    return;
                default:
                    System.out.println(">> INVALID SELECTION! Please enter a number between 0-3.");
            }
        }
    }

    private void showStudentMenu() {
        System.out.println("\n--- STUDENT MENU ---");
        System.out.println("1. LOGIN");
        System.out.println("2. REGISTER");
        System.out.println("0. Back");
        System.out.print("Choose (0-2): ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            performLogin("STUDENT");
        } else if (choice.equals("2")) {
            performRegister("STUDENT");
        } else if (!choice.equals("0")) {
            System.out.println("Invalid option.");
        }
    }

    private void showTeacherMenu() {
        System.out.println("\n--- TEACHER MENU ---");
        System.out.println("1. LOGIN");
        System.out.println("2. REGISTER");
        System.out.println("0. Back");
        System.out.print("Choose (0-2): ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            performLogin("TEACHER");
        } else if (choice.equals("2")) {
            performRegister("TEACHER");
        } else if (!choice.equals("0")) {
            System.out.println("Invalid option.");
        }
    }

    private void performLogin(String requiredRole) {
        System.out.println("\n--- " + requiredRole + " LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);

        if (user != null) {
            // Using Locale.ENGLISH to ensure correct conversion of 'i' to 'I'
            String userRole = user.getClass().getSimpleName().toUpperCase(java.util.Locale.ENGLISH);

            if (userRole.equals(requiredRole)) {
                System.out.println(">>> LOGIN SUCCESSFUL! WELCOME BACK, " + user.getFullName());

                if (user instanceof Student) {
                    startQuizForStudent((Student) user);
                } else if (user instanceof Admin) {
                    showAdminPanel((Admin) user);
                } else if (user instanceof Teacher) {
                    showTeacherPanel((Teacher) user);
                }
            } else {
                System.out.println(">>> ERROR! ONLY " + requiredRole + "S CAN LOG IN HERE!");
            }
        } else {
            System.out.println(">>> INVALID USERNAME OR PASSWORD. ACCESS DENIED!");
        }
    }

    private void performRegister(String role) {
        System.out.println("\n--- NEW " + role + " REGISTRATION ---");
        System.out.print("Enter your Authorized ID Number: ");
        String id = scanner.nextLine();
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Choose Username: ");
        String username = scanner.nextLine();
        System.out.print("Choose Password: ");
        String password = scanner.nextLine();

        // The success/fail message is handled inside AuthService
        boolean success = authService.register(id, name, username, password);
    }

    private void startQuizForStudent(Student student) {
        System.out.println("\nLoading quiz system...");
        Quiz quiz = new Quiz();
        QuestionLoader loader = new QuestionLoader();
        ArrayList<Question> questions = loader.loadQuestions("questions.csv");

        if (questions.isEmpty()) {
            System.out.println("ERROR: No questions loaded from database!");
            return;
        }

        for (Question q : questions) {
            quiz.addQuestion(q);
        }
        quiz.start(student);
    }

    // --- ADMIN PANEL ---
    private void showAdminPanel(Admin admin) {
        IDManager adminManager = new IDManager();

        while (true) {
            System.out.println("\n===========================================");
            System.out.println("  ADMIN CONTROL PANEL (" + admin.getFullName() + ")");
            System.out.println("===========================================");
            System.out.println("1. List Authorized IDs");
            System.out.println("2. Add New ID (Authorize)");
            System.out.println("3. Remove ID (Revoke)");
            System.out.println("4. User Management / Change Password");
            System.out.println("0. Log Out");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                adminManager.listAllIds();

            } else if (choice.equals("2")) {
                System.out.print("ID to Add: ");
                String newId = scanner.nextLine();
                System.out.print("Role (STUDENT / TEACHER / ADMIN): ");
                String role = scanner.nextLine().toUpperCase();

                if (role.equals("STUDENT") || role.equals("TEACHER") || role.equals("ADMIN")) {
                    adminManager.addId(newId, role);
                } else {
                    System.out.println("ERROR: Invalid role entered!");
                }

            } else if (choice.equals("3")) {
                System.out.print("ID to Remove: ");
                String delId = scanner.nextLine();
                adminManager.removeId(delId);

            } else if (choice.equals("4")) {
                // --- ADMIN: LIST USERS & CHANGE PASSWORDS ---
                System.out.println("\n--- ALL REGISTERED USERS ---");
                List<String> users = authService.getAllRegisteredUsers();
                for (String u : users) {
                    System.out.println(">> " + u.replace(";", " | "));
                }

                System.out.print("\nEnter User ID to change password (Cancel: 0): ");
                String targetId = scanner.nextLine();

                if (!targetId.equals("0")) {
                    System.out.print("New Password: ");
                    String newPass = scanner.nextLine();
                    boolean result = authService.updateUserPassword(targetId, newPass);

                    if (result) {
                        System.out.println(">>> SUCCESS! User password updated.");
                    } else {
                        System.out.println(">>> ERROR: User not found or operation failed.");
                    }
                }

            } else if (choice.equals("0")) {
                System.out.println("Closing admin session...");
                break;

            } else {
                System.out.println("Invalid selection!");
            }
        }
    }

    // --- TEACHER PANEL ---
    private void showTeacherPanel(Teacher teacher) {
        QuestionLoader loader = new QuestionLoader();

        while (true) {
            System.out.println("\n===========================================");
            System.out.println("  TEACHER PANEL (" + teacher.getFullName() + ")");
            System.out.println("===========================================");
            System.out.println("1. Add New Question");
            System.out.println("2. Delete Question");
            System.out.println("3. List Student Passwords");
            System.out.println("0. Log Out");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("\n--- CREATE NEW QUESTION ---");
                System.out.println("Select Question Type:");
                System.out.println("M - Multiple Choice");
                System.out.println("T - True/False");
                System.out.print("Selection (M/T): ");
                String type = scanner.nextLine().toUpperCase();

                if (type.equals("M")) {
                    System.out.print("Question Text: ");
                    String text = scanner.nextLine();
                    System.out.print("Score Value (e.g., 10): ");
                    String score = scanner.nextLine();

                    System.out.print("How many options? (e.g., 4): ");
                    int numOptions = Integer.parseInt(scanner.nextLine());

                    StringBuilder optionsPart = new StringBuilder();
                    for (int i = 1; i <= numOptions; i++) {
                        System.out.print("Option " + i + " text: ");
                        String option = scanner.nextLine();
                        optionsPart.append(";").append(option);
                    }
                    System.out.print("Which option number is correct? (1-" + numOptions + "): ");
                    String answer = scanner.nextLine();

                    // Constructing CSV line
                    String csvLine = "MC;" + text + ";" + score + ";" + answer + optionsPart.toString();
                    loader.appendQuestion(csvLine);

                } else if (type.equals("T")) {
                    System.out.print("Question Text: ");
                    String text = scanner.nextLine();
                    System.out.print("Score Value (e.g., 5): ");
                    String score = scanner.nextLine();
                    System.out.print("Correct Answer (true/false): ");
                    String answer = scanner.nextLine().toLowerCase();

                    // Constructing CSV line
                    String csvLine = "TF;" + text + ";" + score + ";" + answer;
                    loader.appendQuestion(csvLine);
                } else {
                    System.out.println("Invalid question type!");
                }

            } else if (choice.equals("2")) {
                System.out.println("\n--- QUESTIONS IN DATABASE ---");
                List<String> lines = loader.getAllRawQuestions();
                if (lines.isEmpty()) {
                    System.out.println(">> No questions found.");
                } else {
                    for (int i = 0; i < lines.size(); i++) {
                        String[] parts = lines.get(i).split(";");
                        if (parts.length > 1) {
                            System.out.println((i + 1) + ". " + parts[1] + " (" + parts[2] + " Points)");
                        }
                    }
                    System.out.print("\nQuestion # to delete (0 to Cancel): ");
                    try {
                        int delIndex = Integer.parseInt(scanner.nextLine());
                        if (delIndex > 0 && delIndex <= lines.size()) {
                            loader.deleteQuestionAt(delIndex - 1);
                        } else if (delIndex == 0) {
                            System.out.println("Deletion cancelled.");
                        } else {
                            System.out.println("Invalid number!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter numbers only.");
                    }
                }

            } else if (choice.equals("3")) {
                System.out.println("\n--- REGISTERED STUDENTS ---");
                List<String> users = authService.getAllRegisteredUsers();

                System.out.println("ID  |  ROLE  |  FULL NAME  |  USERNAME  |  PASSWORD");
                System.out.println("-------------------------------------------------------");

                boolean studentFound = false;
                for (String u : users) {
                    String[] parts = u.split(";");
                    if (parts.length > 1 && parts[1].equalsIgnoreCase("STUDENT")) {
                        System.out.println(u.replace(";", "  |  "));
                        studentFound = true;
                    }
                }

                if (!studentFound) {
                    System.out.println(">> No registered students found in the system.");
                }
                System.out.println("-------------------------------------------------------");
                System.out.println("NOTE: Contact Administrator for password changes or Teacher info.");

            } else if (choice.equals("0")) {
                System.out.println("Closing teacher session...");
                break;

            } else {
                System.out.println("Invalid selection!");
            }
        }
    }
}