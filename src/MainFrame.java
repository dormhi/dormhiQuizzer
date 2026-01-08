import javax.swing.*;
import java.awt.*;

/**
 * Ana pencere sınıfı - Tüm panelleri yönetir
 */
public class MainFrame extends JFrame {

    // Panel isimleri
    public static final String LOGIN_PANEL = "LOGIN";
    public static final String REGISTER_PANEL = "REGISTER";
    public static final String STUDENT_PANEL = "STUDENT";
    public static final String QUIZ_PANEL = "QUIZ";
    public static final String TEACHER_PANEL = "TEACHER";
    public static final String ADMIN_PANEL = "ADMIN";

    // Ana container ve layout
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Paneller
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private StudentPanel studentPanel;
    private QuizPanel quizPanel;
    private TeacherPanel teacherPanel;
    private AdminPanel adminPanel;

    // Renkler
    public static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Mavi
    public static final Color BACKGROUND_COLOR = new Color(30, 30, 30); // Koyu gri
    public static final Color PANEL_COLOR = new Color(45, 45, 45); // Panel arka plan
    public static final Color TEXT_COLOR = new Color(255, 255, 255); // Beyaz
    public static final Color ACCENT_COLOR = new Color(76, 175, 80); // Yeşil
    public static final Color ERROR_COLOR = new Color(244, 67, 54); // Kırmızı

    public MainFrame() {
        setTitle("Quiz System - dormhi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Ana container
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(BACKGROUND_COLOR);

        // Panelleri oluştur
        initializePanels();

        // Panelleri ekle
        mainContainer.add(loginPanel, LOGIN_PANEL);
        mainContainer.add(registerPanel, REGISTER_PANEL);
        mainContainer.add(studentPanel, STUDENT_PANEL);
        mainContainer.add(quizPanel, QUIZ_PANEL);
        mainContainer.add(teacherPanel, TEACHER_PANEL);
        mainContainer.add(adminPanel, ADMIN_PANEL);

        add(mainContainer);

        // İlk panel login
        showPanel(LOGIN_PANEL);
    }

    private void initializePanels() {
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        studentPanel = new StudentPanel(this);
        quizPanel = new QuizPanel(this);
        teacherPanel = new TeacherPanel(this);
        adminPanel = new AdminPanel(this);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainContainer, panelName);
    }

    // Panel getter'ları
    public StudentPanel getStudentPanel() {
        return studentPanel;
    }

    public QuizPanel getQuizPanel() {
        return quizPanel;
    }

    public TeacherPanel getTeacherPanel() {
        return teacherPanel;
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }

    public RegisterPanel getRegisterPanel() {
        return registerPanel;
    }

    // Yardımcı UI metotları
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(PRIMARY_COLOR.brighter());
                } else {
                    g2d.setColor(PRIMARY_COLOR);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));

        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(PANEL_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(PANEL_COLOR.brighter());
                } else {
                    g2d.setColor(PANEL_COLOR);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(PRIMARY_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setForeground(PRIMARY_COLOR);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));

        return button;
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        field.setBackground(new Color(60, 60, 60));
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(300, 45));

        return field;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        field.setBackground(new Color(60, 60, 60));
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(300, 45));

        return field;
    }

    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}
