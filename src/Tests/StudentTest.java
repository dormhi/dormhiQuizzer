import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    Student student;

    @BeforeEach
    void setUp() {
        student = new Student("101", "Test Student", "testuser", "1234");
    }

    @Test
    void initialScoreShouldBeZero() {
        assertEquals(0, student.getExamScore(), "Baslangic puani 0 olmali");
    }

    @Test
    void addToScorePositive() {
        student.addToScore(10);
        assertEquals(10, student.getExamScore(), "Puan dogru eklenmeli");

        student.addToScore(5);
        assertEquals(15, student.getExamScore(), "Puanlar kümülatif toplanmali");
    }

    @Test
    void addToScoreNegative() {
        student.addToScore(-50);
        assertEquals(0, student.getExamScore(), "Negatif puan kabul edilmemeli");
    }
}