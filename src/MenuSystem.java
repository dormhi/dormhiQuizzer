import java.util.ArrayList;
import java.util.Scanner;

public class MenuSystem {
    private AuthService authService;
    private Scanner scanner;

    public MenuSystem() {
        this.authService = new AuthService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("   AREL VADISI QUIZ CHALLENGE - ANA MENU");
            System.out.println("1. Ogretmen Girisi");
            System.out.println("2. Ogrenci Girisi");
            System.out.println("3. Yonetici (Admin) Girisi");
            System.out.println("0. Cikis");
            System.out.print("Seciminiz: ");

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
                    System.out.println("Sistemden cikiliyor... Iyi gunler!");
                    return;
                default:
                    System.out.println(">> HATALI SECIM! Lutfen 1-3 arasi bir rakam girin.");
            }
        }
    }

    private void showStudentMenu() {
        System.out.println("\n--- OGRENCI PANELI ---");
        System.out.println("1. Giris Yap (Login)");
        System.out.println("2. Kayit Ol (Register)");
        System.out.println("0. Geri Don");
        System.out.print("Seciminiz: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            performLogin("STUDENT");
        } else if (choice.equals("2")) {
            performRegister("STUDENT");
        } else if (!choice.equals("0")) {
            System.out.println("Hatali secim.");
        }
    }

    private void showTeacherMenu() {
        System.out.println("\n--- OGRETMEN PANELI ---");
        System.out.println("1. Giris Yap (Login)");
        System.out.println("2. Kayit Ol (Register)");
        System.out.println("0. Geri Don");
        System.out.print("Seciminiz: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            performLogin("TEACHER");
        } else if (choice.equals("2")) {
            performRegister("TEACHER");
        } else if (!choice.equals("0")) {
            System.out.println("Hatali secim.");
        }
    }

    private void performLogin(String requiredRole) {
        System.out.println("\n--- " + requiredRole + " GIRISI ---");
        System.out.print("Kullanici Adi: ");
        String username = scanner.nextLine();
        System.out.print("Sifre: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);

        if (user != null) {
            String userRole = user.getClass().getSimpleName().toUpperCase();

            if (userRole.equals(requiredRole)) {
                System.out.println(">>> GIRIS BASARILI! Hosgeldin " + user.getFullName());

                if (user instanceof Student) {
                    startQuizForStudent((Student) user);
                } else if (user instanceof Admin) {
                    showAdminPanel((Admin) user);
                }
            } else {
                System.out.println(">>> HATA: Bu panelden sadece " + requiredRole + " giris yapabilir!");
            }
        } else {
            System.out.println(">>> GIRIS BASARISIZ! Kullanici adi veya sifre yanlis.");
        }
    }

    private void performRegister(String role) {
        System.out.println("\n--- YENI " + role + " KAYDI ---");
        System.out.print("Kurum ID Numaraniz (Ogrenci/Ogretmen No): ");
        String id = scanner.nextLine();
        System.out.print("Adiniz Soyadiniz: ");
        String name = scanner.nextLine();
        System.out.print("Belirleyeceginiz Kullanici Adi: ");
        String username = scanner.nextLine();
        System.out.print("Belirleyeceginiz Sifre: ");
        String password = scanner.nextLine();

        boolean success = authService.register(id, name, username, password);
        if (success) {
            System.out.println("Simdi giris yapabilirsiniz.");
        }
    }

    private void startQuizForStudent(Student student) {
        System.out.println("\nSınav sistemi yükleniyor...");
        Quiz quiz = new Quiz();
        QuestionLoader loader = new QuestionLoader();
        ArrayList<Question> questions = loader.loadQuestions("questions.csv");

        if (questions.isEmpty()) {
            System.out.println("HATA: Hic soru yuklenemedi!");
            return;
        }

        for (Question q : questions) {
            quiz.addQuestion(q);
        }
        quiz.start(student);
    }


}