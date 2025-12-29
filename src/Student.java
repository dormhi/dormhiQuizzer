/**
 * Üniversite öğrencisini temsil eden sınıf.
 * User sınıfından türetilmiştir ve sınav puanı verisini ek olarak tutar.
 *
 * @author [Senin Adın]
 * @version 1.0
 * @since 2025-12-08
 */
public class Student extends User {
    private int examScore;

    /**
     * Yeni bir öğrenci oluşturur.
     * Başlangıç sınav puanı 0 olarak ayarlanır.
     *
     * @param id       Öğrenci Okul Numarası
     * @param fullName Ad Soyad
     * @param username Sistem Giriş Adı
     * @param password Giriş Şifresi
     */
    public Student(String id, String fullName, String username, String password) {
        super(id, fullName, username, password);
        this.examScore = 0;
    }

    /**
     * Öğrencinin mevcut sınav puanına ekleme yapar.
     * Negatif puan eklenmesini engeller.
     *
     * @param score Eklenecek puan miktarı (pozitif olmalı)
     */
    public void addToScore(int score) {
        if (score > 0) {
            this.examScore += score;
        }
    }

    /**
     * Öğrencinin güncel sınav puanını döndürür.
     *
     * @return Toplam puan
     */
    public int getExamScore() {
        return examScore;
    }
}