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
            System.out.println(" \n AREL VADISI QUIZ CHALLENGE A HOSGELDINIZ");
            System.out.println("===========================================");
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
                    System.out.println(">> HATALI SECIM! Lutfen 0-3 arasi bir rakam girin.");
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
            String userRole = user.getClass().getSimpleName().toUpperCase(java.util.Locale.ENGLISH);

            if (userRole.equals(requiredRole)) {
                System.out.println(">>> GIRIS BASARILI! Hosgeldin " + user.getFullName());

                if (user instanceof Student) {
                    startQuizForStudent((Student) user);
                } else if (user instanceof Admin) {
                    showAdminPanel((Admin) user);
                } else if (user instanceof Teacher) {
                    showTeacherPanel((Teacher) user);
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
        // Başarılı/Başarısız mesajı AuthService içinde veriliyor
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

    // --- ADMIN PANEL ---
    private void showAdminPanel(Admin admin) {
        IDManager adminManager = new IDManager();

        while (true) {
            System.out.println("\n===========================================");
            System.out.println("  YONETICI KONTROL PANELI (" + admin.getFullName() + ")");
            System.out.println("===========================================");
            System.out.println("1. Izinli Numaralari Listele");
            System.out.println("2. Yeni Numara Ekle (Izin Ver)");
            System.out.println("3. Numara Sil (Izni Kaldir)");
            System.out.println("4. Kullanici Yonetimi / Sifre Degistir (YENI)");
            System.out.println("0. Cikis Yap");
            System.out.print("Seciminiz: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                adminManager.listAllIds();

            } else if (choice.equals("2")) {
                System.out.print("Eklenecek Numara (ID): ");
                String newId = scanner.nextLine();
                System.out.print("Rol (STUDENT / TEACHER / ADMIN): ");
                String role = scanner.nextLine().toUpperCase();

                if (role.equals("STUDENT") || role.equals("TEACHER") || role.equals("ADMIN")) {
                    adminManager.addId(newId, role);
                } else {
                    System.out.println("HATA: Gecersiz rol!");
                }

            } else if (choice.equals("3")) {
                System.out.print("Silinecek Numara (ID): ");
                String delId = scanner.nextLine();
                adminManager.removeId(delId);

            } else if (choice.equals("4")) {
                // --- ADMIN İÇİN: LİSTELEME VE DEĞİŞTİRME ---
                System.out.println("\n--- TUM KULLANICILAR ---");
                List<String> users = authService.getAllRegisteredUsers();
                for (String u : users) {
                    System.out.println(">> " + u.replace(";", " | "));
                }

                System.out.print("\nSifresini degistirmek istediginiz kullanicinin ID'si (Iptal: 0): ");
                String targetId = scanner.nextLine();

                if (!targetId.equals("0")) {
                    System.out.print("Yeni Sifre: ");
                    String newPass = scanner.nextLine();
                    boolean result = authService.updateUserPassword(targetId, newPass);

                    if (result) {
                        System.out.println(">>> BASARILI! Kullanicinin sifresi guncellendi.");
                    } else {
                        System.out.println(">>> HATA: Kullanici bulunamadi veya islem basarisiz.");
                    }
                }

            } else if (choice.equals("0")) {
                System.out.println("Yonetici oturumu kapatiliyor...");
                break;

            } else {
                System.out.println("Hatali secim!");
            }
        }
    }
    // --- TEACHER PANEL ---
    private void showTeacherPanel(Teacher teacher) {
        QuestionLoader loader = new QuestionLoader();

        while (true) {
            System.out.println("\n===========================================");
            System.out.println("  OGRETMEN PANEL (" + teacher.getFullName() + ")");
            System.out.println("===========================================");
            System.out.println("1. Yeni Soru Ekle");
            System.out.println("2. Soru Sil");
            System.out.println("3. Ogrenci Sifre Listesi");
            System.out.println("0. Cikis Yap");
            System.out.print("Seciminiz: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("\n--- YENI SORU OLUSTURMA ---");
                System.out.println("Soru Tipi Secin:");
                System.out.println("M - Coktan Secmeli (Multiple Choice)");
                System.out.println("T - Dogru/Yanlis (True/False)");
                System.out.print("Secim (M/T): ");
                String type = scanner.nextLine().toUpperCase();

                if (type.equals("M")) {
                    System.out.print("Soru Metni: ");
                    String text = scanner.nextLine();
                    System.out.print("Puan Degeri (Orn: 10): ");
                    String score = scanner.nextLine();

                    System.out.print("Kac secenek olacak? (Orn: 4): ");
                    int numOptions = Integer.parseInt(scanner.nextLine());

                    StringBuilder optionsPart = new StringBuilder();
                    for (int i = 1; i <= numOptions; i++) {
                        System.out.print(i + ". secenek Metni: ");
                        String option = scanner.nextLine();
                        optionsPart.append(";").append(option);
                    }
                    System.out.print("Dogru seceneğin numarasi hangisi? (1-" + numOptions + "): ");
                    String answer = scanner.nextLine();
                    String csvLine = "MC;" + text + ";" + score + ";" + answer + optionsPart.toString();
                    loader.appendQuestion(csvLine);

                } else if (type.equals("T")) {
                    System.out.print("Soru Metni: ");
                    String text = scanner.nextLine();
                    System.out.print("Puan Degeri (Orn: 5): ");
                    String score = scanner.nextLine();
                    System.out.print("Dogru Cevap (true/false): ");
                    String answer = scanner.nextLine().toLowerCase();
                    String csvLine = "TF;" + text + ";" + score + ";" + answer;
                    loader.appendQuestion(csvLine);
                } else {
                    System.out.println("Gecersiz soru tipi!");
                }

            } else if (choice.equals("2")) {
                System.out.println("\n--- VERITABANINDAKI SORULAR ---");
                List<String> lines = loader.getAllRawQuestions();
                if (lines.isEmpty()) {
                    System.out.println(">> Listelenecek soru bulunamadi.");
                } else {
                    for (int i = 0; i < lines.size(); i++) {
                        String[] parts = lines.get(i).split(";");
                        if (parts.length > 1) {
                            System.out.println((i + 1) + ". " + parts[1] + " (" + parts[2] + " Puan)");
                        }
                    }
                    System.out.print("\nSilmek istediginiz soru no (Iptal icin 0): ");
                    try {
                        int delIndex = Integer.parseInt(scanner.nextLine());
                        if (delIndex > 0 && delIndex <= lines.size()) {
                            loader.deleteQuestionAt(delIndex - 1);
                        } else if (delIndex == 0) {
                            System.out.println("Silme islemi iptal edildi.");
                        } else {
                            System.out.println("Gecersiz numara!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Lutfen sadece rakam giriniz.");
                    }
                }

            } else if (choice.equals("3")) {
                System.out.println("\n--- KAYITLI OGRENCILER ---");
                List<String> users = authService.getAllRegisteredUsers();

                System.out.println("ID  |  ROL  |  AD SOYAD  |  KULLANICI ADI  |  SIFRE");
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
                    System.out.println(">> Sistemde kayitli ogrenci bulunmamaktadir.");
                }
                System.out.println("-------------------------------------------------------");
                System.out.println("NOT: Sifre degisikligi veya Ogretmen bilgileri icin Yoneticiye basvurunuz.");

            } else if (choice.equals("0")) {
                System.out.println("Ogretmen oturumu kapatiliyor...");
                break;

            } else {
                System.out.println("Hatali secim!");
            }
        }
    }
}