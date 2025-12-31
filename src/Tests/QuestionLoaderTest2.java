import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuestionLoaderTest2 {

    private QuestionLoader loader;

    // Kodun içinde "questions.csv" sabit olduğu için testte de mecburen bunu kullanıyoruz
    private static final Path REAL_FILE = Paths.get("questions.csv");
    private static final Path BACKUP_FILE = Paths.get("questions_backup.csv");

    // loadQuestions metodunu test etmek için güvenli, geçici bir dosya adı
    private static final String TEMP_TEST_FILE = "test_questions_source.csv";

    // --- GÜVENLİK BLOĞU: GERÇEK VERİLERİ KORUMA ---
    @BeforeAll
    static void backupRealData() throws IOException {
        // Eğer gerçek bir questions.csv varsa, testlerden önce yedeğe al
        if (Files.exists(REAL_FILE)) {
            Files.move(REAL_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("--- GÜVENLİK: Gerçek soru dosyası yedeklendi ---");
        }
    }

    @AfterAll
    static void restoreRealData() throws IOException {
        // Test bitti, oluşturduğumuz çöp dosyayı sil
        Files.deleteIfExists(REAL_FILE);
        Files.deleteIfExists(Paths.get(TEMP_TEST_FILE));

        // Yedeği geri getir
        if (Files.exists(BACKUP_FILE)) {
            Files.move(BACKUP_FILE, REAL_FILE, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("--- GÜVENLİK: Gerçek sorular geri yüklendi ---");
        }
    }
    // ------------------------------------------------

    @BeforeEach
    void setUp() throws IOException {
        // Her testten önce ortamı sıfırla
        Files.deleteIfExists(REAL_FILE);
        Files.createFile(REAL_FILE);

        loader = new QuestionLoader();
    }

    @Test
    @Order(1)
    @DisplayName("Soru Ekleme (Append): Dosyaya doğru formatta yazmalı")
    void testAppendQuestion() throws IOException {
        // GIVEN
        // Format: MC;Soru Metni;Puan;DoğruCevapIndex;Şık1;Şık2;Şık3;Şık4
        String newQuestion = "MC;Java hangi yıl çıktı?;10;1;1990;1995;2000;2005";

        // WHEN
        loader.appendQuestion(newQuestion);

        // THEN
        List<String> lines = Files.readAllLines(REAL_FILE);
        assertEquals(1, lines.size(), "Dosyada 1 satır olmalı");
        assertEquals(newQuestion, lines.get(0), "Eklenen satır birebir aynı olmalı");
    }

    @Test
    @Order(2)
    @DisplayName("Soru Silme (Delete): İndeksteki soru silinmeli")
    void testDeleteQuestionAt() throws IOException {
        // GIVEN - Dosyaya manuel olarak 3 tane soru yazalım
        List<String> questions = new ArrayList<>();
        questions.add("MC;Soru1;10;0;A;B"); // Index 0
        questions.add("TF;Soru2;5;true");   // Index 1 (Silinecek olan)
        questions.add("MC;Soru3;20;1;X;Y"); // Index 2
        Files.write(REAL_FILE, questions);

        // WHEN - Ortadaki soruyu (Index 1) sil
        loader.deleteQuestionAt(1);

        // THEN
        List<String> remaining = loader.getAllRawQuestions();
        assertEquals(2, remaining.size(), "3 sorudan 1'i silinince 2 kalmalı");

        // Kalanların sırasını kontrol et: Soru1 durmalı, Soru3 durmalı
        assertTrue(remaining.get(0).contains("Soru1"));
        assertTrue(remaining.get(1).contains("Soru3"));
    }

    @Test
    @Order(3)
    @DisplayName("Soruları Yükleme (Load): CSV'den Nesneye Dönüşüm")
    void testLoadQuestions() throws IOException {
        // GIVEN - Okuma testi için özel bir dosya hazırlayalım
        Path sourcePath = Paths.get(TEMP_TEST_FILE);
        List<String> data = new ArrayList<>();
        // Bir Çoktan Seçmeli (MC), Bir Doğru Yanlış (TF)
        data.add("MC;Baskent neresidir?;10;1;Istanbul;Ankara;Izmir");
        data.add("TF;Java bir meyvedir?;5;false");
        Files.write(sourcePath, data);

        // WHEN
        // loadQuestions metoduna bu özel dosyanın ismini veriyoruz
        ArrayList<Question> questions = loader.loadQuestions(TEMP_TEST_FILE);

        // THEN
        assertNotNull(questions);
        assertEquals(2, questions.size(), "2 adet soru nesnesi yüklenmeli");

        // İlk soru kontrolü (MC)
        Question q1 = questions.get(0);
        assertEquals(10, q1.getScore());
        // Not: Burada 'getText' veya 'getQuestionText' metodun neyse onu kullanmalısın
        //assertTrue(q1 instanceof MultipleChoiceQuestion);

        // İkinci soru kontrolü (TF)
        Question q2 = questions.get(1);
        assertEquals(5, q2.getScore());
        //assertTrue(q2 instanceof TrueFalseQuestion);
    }

    @Test
    @Order(4)
    @DisplayName("Hatalı Silme: Geçersiz indeks verilirse dosya bozulmamalı")
    void testDeleteInvalidIndex() throws IOException {
        // GIVEN - 1 soruluk dosya
        Files.write(REAL_FILE, List.of("TF;Deneme;10;true"));

        // WHEN - Olmayan bir indeksi (örn: 5) silmeye çalış
        loader.deleteQuestionAt(5);

        // THEN
        List<String> lines = loader.getAllRawQuestions();
        assertEquals(1, lines.size(), "Geçersiz indeks silme işlemi yapmamalı");
    }
}