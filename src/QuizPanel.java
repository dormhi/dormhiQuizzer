import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Quiz paneli - Soru gösterimi ve cevaplama
 */
public class QuizPanel extends JPanel {

    private MainFrame mainFrame;
    private Student currentStudent;
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int totalScore;

    // UI Bileşenleri
    private JLabel questionNumberLabel;
    private JLabel questionTextLabel;
    private JLabel scoreLabel;
    private JPanel optionsPanel;
    private ButtonGroup optionGroup;
    private JButton nextButton;
    private JLabel feedbackLabel;

    public QuizPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.questions = new ArrayList<>();

        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        initComponents();
    }

    private void initComponents() {
        // Üst panel - Soru numarası ve skor
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(MainFrame.BACKGROUND_COLOR);

        questionNumberLabel = new JLabel("Question 1 / 10");
        questionNumberLabel.setForeground(MainFrame.PRIMARY_COLOR);
        questionNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(MainFrame.ACCENT_COLOR);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(questionNumberLabel, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Orta panel - Soru ve seçenekler
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(MainFrame.PANEL_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        questionTextLabel = new JLabel("<html><div style='width: 500px;'>Question text here...</div></html>");
        questionTextLabel.setForeground(MainFrame.TEXT_COLOR);
        questionTextLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(questionTextLabel);

        centerPanel.add(Box.createVerticalStrut(25));

        optionsPanel = new JPanel();
        optionsPanel.setBackground(MainFrame.PANEL_COLOR);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(optionsPanel);

        centerPanel.add(Box.createVerticalStrut(20));

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(feedbackLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Alt panel - Butonlar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(MainFrame.BACKGROUND_COLOR);

        nextButton = MainFrame.createStyledButton("NEXT →");
        nextButton.addActionListener(e -> nextQuestion());
        bottomPanel.add(nextButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void startQuiz(Student student) {
        this.currentStudent = student;
        this.currentQuestionIndex = 0;
        this.totalScore = 0;

        // Soruları yükle
        QuestionLoader loader = new QuestionLoader();
        questions = loader.loadQuestions("questions.csv");

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No questions found in database!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            mainFrame.showPanel(MainFrame.STUDENT_PANEL);
            return;
        }

        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        Question q = questions.get(currentQuestionIndex);

        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " / " + questions.size());
        questionTextLabel.setText("<html><div style='width: 500px;'>" + q.getText() +
                " <span style='color: #2196F3;'>(" + q.getScore() + " pts)</span></div></html>");
        scoreLabel.setText("Score: " + totalScore);
        feedbackLabel.setText(" ");

        // Seçenekleri temizle
        optionsPanel.removeAll();
        optionGroup = new ButtonGroup();

        if (q instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
            String[] options = mcq.getOptions();

            for (int i = 0; i < options.length; i++) {
                JRadioButton radio = createStyledRadioButton((i + 1) + ". " + options[i]);
                radio.setActionCommand(String.valueOf(i + 1));
                optionGroup.add(radio);
                optionsPanel.add(radio);
                optionsPanel.add(Box.createVerticalStrut(10));
            }
        } else if (q instanceof TrueFalseQuestion) {
            JRadioButton trueBtn = createStyledRadioButton("True");
            trueBtn.setActionCommand("true");
            JRadioButton falseBtn = createStyledRadioButton("False");
            falseBtn.setActionCommand("false");

            optionGroup.add(trueBtn);
            optionGroup.add(falseBtn);
            optionsPanel.add(trueBtn);
            optionsPanel.add(Box.createVerticalStrut(10));
            optionsPanel.add(falseBtn);
        }

        nextButton.setText(currentQuestionIndex < questions.size() - 1 ? "NEXT →" : "FINISH");

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setBackground(MainFrame.PANEL_COLOR);
        radio.setForeground(MainFrame.TEXT_COLOR);
        radio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        radio.setFocusPainted(false);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radio;
    }

    private void nextQuestion() {
        ButtonModel selection = optionGroup.getSelection();

        if (selection == null) {
            feedbackLabel.setText("⚠ Please select an answer!");
            feedbackLabel.setForeground(MainFrame.ERROR_COLOR);
            return;
        }

        String answer = selection.getActionCommand();
        Question q = questions.get(currentQuestionIndex);

        if (q.checkAnswer(answer)) {
            totalScore += q.getScore();
            currentStudent.addToScore(q.getScore());
            feedbackLabel.setText("✓ Correct! +" + q.getScore() + " pts");
            feedbackLabel.setForeground(MainFrame.ACCENT_COLOR);
        } else {
            feedbackLabel.setText("✗ Wrong answer");
            feedbackLabel.setForeground(MainFrame.ERROR_COLOR);
        }

        // Kısa bekleme sonra sonraki soruya geç
        Timer timer = new Timer(1000, e -> {
            currentQuestionIndex++;
            showQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showResults() {
        optionsPanel.removeAll();

        questionNumberLabel.setText("QUIZ COMPLETED!");
        questionTextLabel.setText("<html><div style='width: 500px; text-align: center;'>" +
                "<h2 style='color: #4CAF50;'>Congratulations, " + currentStudent.getFullName() + "!</h2>" +
                "<p style='font-size: 18px;'>Your final score:</p>" +
                "<p style='font-size: 36px; color: #2196F3;'>" + totalScore + " points</p>" +
                "</div></html>");

        scoreLabel.setText("");
        feedbackLabel.setText(totalScore > 10 ? "Great job! 🎉" : "Keep practicing! 💪");
        feedbackLabel.setForeground(totalScore > 10 ? MainFrame.ACCENT_COLOR : MainFrame.PRIMARY_COLOR);

        nextButton.setText("← BACK TO MENU");

        // Buton action'ını değiştir
        for (var al : nextButton.getActionListeners()) {
            nextButton.removeActionListener(al);
        }
        nextButton.addActionListener(e -> {
            mainFrame.showPanel(MainFrame.STUDENT_PANEL);
            // Action listener'ı geri yükle
            for (var al : nextButton.getActionListeners()) {
                nextButton.removeActionListener(al);
            }
            nextButton.addActionListener(ev -> nextQuestion());
        });

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }
}
