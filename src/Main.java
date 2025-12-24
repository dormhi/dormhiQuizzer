public class Main {
    public static void main(String[] args) {

        System.out.println("--- SISTEM TESTI BASLIYOR ---");

        // 1. Auth Servisini Başlat
        AuthService auth = new AuthService();

        // 2. Admin Olarak Giriş Yapmayı Dene (users.csv'de bu kullanıcı olmalı)
        // Kullanıcı adı: admin, Şifre: admin123
        User loggedUser = auth.login("admin", "admin123");

        // 3. Sonucu Kontrol Et
        if (loggedUser != null) {
            System.out.println(">>> GIRIS BASARILI!");
            System.out.println("Hosgeldin: " + loggedUser.getFullName());
            System.out.println("Sistemdeki Rolun: " + loggedUser.getClass().getSimpleName());

            // Eğer giriş yapan Admin ise ona özel mesaj göster
            if (loggedUser instanceof Admin) {
                System.out.println("(Yonetici paneli yetkisi onaylandi)");
            }
        } else {
            System.out.println(">>> GIRIS BASARISIZ! Kullanici adi veya sifre hatali.");
        }

        System.out.println("--- TEST BITTI ---");
    }
}