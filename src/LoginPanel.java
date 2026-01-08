import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

/**
 * Login paneli - Kullanıcı girişi
 * Üstte Student/Teacher butonları, sağ altta gizli Admin logosu
 */
public class LoginPanel extends JPanel {

    private MainFrame mainFrame;
    private AuthService authService;

    // Bileşenler
    private String selectedRole = "STUDENT";
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton studentButton;
    private JButton teacherButton;
    private JButton registerButton;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authService = new AuthService();

        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // ===== ÜST KISIM - ROL BUTONLARI =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topPanel.setBackground(MainFrame.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        studentButton = createRoleButton("🎓 STUDENT", true);
        studentButton.addActionListener(e -> selectRole("STUDENT"));

        teacherButton = createRoleButton("📚 TEACHER", false);
        teacherButton.addActionListener(e -> selectRole("TEACHER"));

        topPanel.add(studentButton);
        topPanel.add(teacherButton);

        add(topPanel, BorderLayout.NORTH);

        // ===== ORTA KISIM - GİRİŞ FORMU =====
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(MainFrame.BACKGROUND_COLOR);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(MainFrame.PANEL_COLOR);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(8, 10, 8, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.gridx = 0;

        // Logo / Başlık
        JLabel titleLabel = MainFrame.createTitleLabel("QUIZ SYSTEM");
        titleLabel.setForeground(MainFrame.PRIMARY_COLOR);
        formGbc.gridy = 0;
        formGbc.gridwidth = 2;
        formPanel.add(titleLabel, formGbc);

        JLabel subtitleLabel = new JLabel("v2.0 by dormhi", SwingConstants.CENTER);
        subtitleLabel.setForeground(new Color(150, 150, 150));
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        formGbc.gridy = 1;
        formPanel.add(subtitleLabel, formGbc);

        // Boşluk
        formGbc.gridy = 2;
        formPanel.add(Box.createVerticalStrut(25), formGbc);

        // Username
        formGbc.gridy = 3;
        formGbc.gridwidth = 1;
        formPanel.add(MainFrame.createStyledLabel("Username:"), formGbc);

        usernameField = MainFrame.createStyledTextField();
        formGbc.gridx = 1;
        formPanel.add(usernameField, formGbc);

        // Password
        formGbc.gridy = 4;
        formGbc.gridx = 0;
        formPanel.add(MainFrame.createStyledLabel("Password:"), formGbc);

        passwordField = MainFrame.createStyledPasswordField();
        formGbc.gridx = 1;
        formPanel.add(passwordField, formGbc);

        // Boşluk
        formGbc.gridy = 5;
        formGbc.gridx = 0;
        formGbc.gridwidth = 2;
        formPanel.add(Box.createVerticalStrut(15), formGbc);

        // Mesaj label
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        formGbc.gridy = 6;
        formPanel.add(messageLabel, formGbc);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(MainFrame.PANEL_COLOR);

        JButton loginButton = MainFrame.createStyledButton("LOGIN");
        loginButton.addActionListener(e -> performLogin());

        registerButton = MainFrame.createSecondaryButton("REGISTER");
        registerButton.addActionListener(e -> goToRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formGbc.gridy = 7;
        formPanel.add(buttonPanel, formGbc);

        centerWrapper.add(formPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // ===== SAĞ ALT KÖŞE - ADMİN LOGOSU =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(MainFrame.BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 30));

        JPanel adminButtonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        adminButtonWrapper.setBackground(MainFrame.BACKGROUND_COLOR);

        JButton adminButton = createAdminButton();
        adminButtonWrapper.add(adminButton);

        bottomPanel.add(adminButtonWrapper, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createRoleButton(String text, boolean isSelected) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor;
                if (getName() != null && getName().equals("selected")) {
                    bgColor = MainFrame.PRIMARY_COLOR;
                } else if (getModel().isRollover()) {
                    bgColor = new Color(70, 70, 70);
                } else {
                    bgColor = new Color(55, 55, 55);
                }

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                if (getName() != null && getName().equals("selected")) {
                    g2d.setColor(MainFrame.PRIMARY_COLOR.brighter());
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setName(isSelected ? "selected" : "");
        button.setForeground(MainFrame.TEXT_COLOR);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 55));

        return button;
    }

    private JButton createAdminButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Daire arka plan
                Color bgColor = getModel().isRollover()
                        ? new Color(80, 80, 80)
                        : new Color(50, 50, 50);
                g2d.setColor(bgColor);
                g2d.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));

                // ⚙️ Dişli simgesi (Settings/Admin ikonu)
                g2d.setColor(new Color(150, 150, 150));
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 22));
                FontMetrics fm = g2d.getFontMetrics();
                String icon = "⚙";
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(icon, x, y);

                g2d.dispose();
            }
        };

        button.setPreferredSize(new Dimension(45, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Admin Login");

        button.addActionListener(e -> {
            selectedRole = "ADMIN";
            updateRoleButtonStyles();
            showMessage("Admin mode selected", new Color(150, 150, 150));
        });

        return button;
    }

    private void selectRole(String role) {
        selectedRole = role;
        updateRoleButtonStyles();
        messageLabel.setText(" ");
    }

    private void updateRoleButtonStyles() {
        studentButton.setName(selectedRole.equals("STUDENT") ? "selected" : "");
        teacherButton.setName(selectedRole.equals("TEACHER") ? "selected" : "");
        studentButton.repaint();
        teacherButton.repaint();

        // Admin modunda register butonu gizle
        registerButton.setVisible(!selectedRole.equals("ADMIN"));
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields!", MainFrame.ERROR_COLOR);
            return;
        }

        User user = authService.login(username, password);

        if (user != null) {
            String userRole = user.getClass().getSimpleName().toUpperCase(java.util.Locale.ENGLISH);

            if (userRole.equals(selectedRole)) {
                showMessage("Login successful! Welcome, " + user.getFullName(), MainFrame.ACCENT_COLOR);

                // Panele yönlendir
                if (user instanceof Student) {
                    mainFrame.getStudentPanel().setCurrentUser((Student) user);
                    mainFrame.showPanel(MainFrame.STUDENT_PANEL);
                } else if (user instanceof Teacher) {
                    mainFrame.getTeacherPanel().setCurrentUser((Teacher) user);
                    mainFrame.showPanel(MainFrame.TEACHER_PANEL);
                } else if (user instanceof Admin) {
                    mainFrame.getAdminPanel().setCurrentUser((Admin) user);
                    mainFrame.showPanel(MainFrame.ADMIN_PANEL);
                }

                clearFields();
            } else {
                showMessage("ERROR: Only " + selectedRole + "s can login here!", MainFrame.ERROR_COLOR);
            }
        } else {
            showMessage("Invalid username or password!", MainFrame.ERROR_COLOR);
        }
    }

    private void goToRegister() {
        mainFrame.getRegisterPanel().setRole(selectedRole);
        mainFrame.showPanel(MainFrame.REGISTER_PANEL);
        clearFields();
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");
        selectedRole = "STUDENT";
        updateRoleButtonStyles();
    }
}
