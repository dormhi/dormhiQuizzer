import javax.swing.*;
import java.awt.*;

/**
 * Öğrenci ana paneli
 */
public class StudentPanel extends JPanel {

    private MainFrame mainFrame;
    private Student currentUser;

    private JLabel welcomeLabel;
    private JLabel infoLabel;

    public StudentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(new GridBagLayout());

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ana panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(MainFrame.PANEL_COLOR);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(15, 10, 15, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.gridx = 0;

        // Başlık
        JLabel titleLabel = MainFrame.createTitleLabel("STUDENT PANEL");
        titleLabel.setForeground(MainFrame.PRIMARY_COLOR);
        formGbc.gridy = 0;
        contentPanel.add(titleLabel, formGbc);

        // Hoşgeldin mesajı
        welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setForeground(MainFrame.TEXT_COLOR);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        formGbc.gridy = 1;
        contentPanel.add(welcomeLabel, formGbc);

        // Bilgi
        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setForeground(new Color(150, 150, 150));
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formGbc.gridy = 2;
        contentPanel.add(infoLabel, formGbc);

        // Boşluk
        formGbc.gridy = 3;
        contentPanel.add(Box.createVerticalStrut(30), formGbc);

        // Quiz Başlat butonu
        JButton startQuizButton = MainFrame.createStyledButton("🎯 START QUIZ");
        startQuizButton.setPreferredSize(new Dimension(250, 55));
        startQuizButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startQuizButton.addActionListener(e -> startQuiz());
        formGbc.gridy = 4;
        contentPanel.add(startQuizButton, formGbc);

        // Boşluk
        formGbc.gridy = 5;
        contentPanel.add(Box.createVerticalStrut(15), formGbc);

        // Çıkış butonu
        JButton logoutButton = MainFrame.createSecondaryButton("← LOGOUT");
        logoutButton.addActionListener(e -> logout());
        formGbc.gridy = 6;
        contentPanel.add(logoutButton, formGbc);

        add(contentPanel, gbc);
    }

    public void setCurrentUser(Student user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName() + "!");
        infoLabel.setText("ID: " + user.getId() + " | Ready to take the quiz?");
    }

    private void startQuiz() {
        if (currentUser != null) {
            mainFrame.getQuizPanel().startQuiz(currentUser);
            mainFrame.showPanel(MainFrame.QUIZ_PANEL);
        }
    }

    private void logout() {
        currentUser = null;
        mainFrame.showPanel(MainFrame.LOGIN_PANEL);
    }
}
