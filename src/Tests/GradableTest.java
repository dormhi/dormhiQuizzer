package Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Gradable interface testi.
 * Bu testler, Gradable interface'ini implement eden sınıflar
 * (MultipleChoiceQuestion, TrueFalseQuestion) üzerinden yapılır.
 */
class GradableTest {

    private MultipleChoiceQuestion mcQuestion;
    private TrueFalseQuestion tfQuestion;

    @BeforeEach
    void setUp() {
        // MultipleChoiceQuestion örneği
        ArrayList<String> options = new ArrayList<>();
        options.add("Option A");
        options.add("Option B");
        options.add("Option C");
        mcQuestion = new MultipleChoiceQuestion("What is 2+2?", 10, options, 2);

        // TrueFalseQuestion örneği
        tfQuestion = new TrueFalseQuestion("Java is an OOP language", 5, true);
    }

    // ==================== getScore() Testleri ====================

    @Test
    void testGetScore_MultipleChoiceQuestion_ReturnsCorrectScore() {
        // Arrange & Act & Assert
        assertEquals(10, mcQuestion.getScore(), "MultipleChoiceQuestion score should be 10");
    }

    @Test
    void testGetScore_TrueFalseQuestion_ReturnsCorrectScore() {
        // Arrange & Act & Assert
        assertEquals(5, tfQuestion.getScore(), "TrueFalseQuestion score should be 5");
    }

    @Test
    void testGetScore_ZeroScore() {
        // Arrange
        TrueFalseQuestion zeroScoreQuestion = new TrueFalseQuestion("Test question", 0, false);

        // Act & Assert
        assertEquals(0, zeroScoreQuestion.getScore(), "Score should be 0");
    }

    @Test
    void testGetScore_NegativeScore() {
        // Arrange - Negatif skor senaryosu (eğer izin veriliyorsa)
        TrueFalseQuestion negativeScoreQuestion = new TrueFalseQuestion("Test question", -5, true);

        // Act & Assert
        assertEquals(-5, negativeScoreQuestion.getScore(), "Negative score should be returned");
    }

    @Test
    void testGetScore_LargeScore() {
        // Arrange
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        MultipleChoiceQuestion largeScoreQuestion = new MultipleChoiceQuestion("Large score test", 1000, options, 1);

        // Act & Assert
        assertEquals(1000, largeScoreQuestion.getScore(), "Large score should be returned correctly");
    }

    // ==================== Gradable Polymorphism Testleri ====================

    @Test
    void testGradable_Polymorphism_MultipleChoiceAsGradable() {
        // Arrange
        Gradable gradable = mcQuestion;

        // Act & Assert
        assertEquals(10, gradable.getScore(), "Polymorphic access to getScore should work");
    }

    @Test
    void testGradable_Polymorphism_TrueFalseAsGradable() {
        // Arrange
        Gradable gradable = tfQuestion;

        // Act & Assert
        assertEquals(5, gradable.getScore(), "Polymorphic access to getScore should work");
    }

    @Test
    void testGradable_ArrayProcessing() {
        // Arrange - Farklı Gradable implementasyonlarını bir dizide toplama
        Gradable[] gradables = new Gradable[] { mcQuestion, tfQuestion };

        // Act
        int totalScore = 0;
        for (Gradable g : gradables) {
            totalScore += g.getScore();
        }

        // Assert
        assertEquals(15, totalScore, "Total score of all gradables should be 15");
    }

    // ==================== Instance Check Testleri ====================

    @Test
    void testMultipleChoiceQuestion_IsInstanceOfGradable() {
        assertTrue(mcQuestion instanceof Gradable, "MultipleChoiceQuestion should implement Gradable");
    }

    @Test
    void testTrueFalseQuestion_IsInstanceOfGradable() {
        assertTrue(tfQuestion instanceof Gradable, "TrueFalseQuestion should implement Gradable");
    }
}
