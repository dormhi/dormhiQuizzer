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
            System.out.println("  AREL VADISI QUIZ CHALLENGE - ANA MENU");
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
                    showAdminLogin();
                    break;
                case "0":
                    System.out.println("Sistemden cikiliyor... Iyi gunler!");
                    return;
                default:
                    System.out.println("Hatali secim! Lutfen tekrar deneyin.");
            }
        }
    }

    private void showTeacherMenu() {
        System.out.println("\n--- OGRETMEN PANELI ---");
        System.out.println("1. Giris Yap");
        System.out.println("2. Kayit Ol");
        System.out.println("0. Ana Menuye Don");
        // will be contiune
    }

    private void showStudentMenu() {
        System.out.println("\n--- OGRENCI PANELI ---");
        System.out.println("1. Giris Yap");
        System.out.println("2. Kayit Ol");
        System.out.println("0. Ana Menuye Don");
// will be contiune
    }
    private void showAdminLogin () {
            System.out.println("\n--- YONETICI GIRISI ---");
        }
    }
