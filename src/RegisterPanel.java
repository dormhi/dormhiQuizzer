import javax.swing.*;
import java.awt.*;

/**
 * Register paneli - Yeni kullanıcı kaydı
 */
public class RegisterPanel extends JPanel {

    private MainFrame mainFrame;
    private AuthService authService;

    // Bileşenler
    private JLabel roleLabel;
    private JTextField idField;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private String currentRole = "STUDENT";

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authService = new AuthService();

        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(new GridBagLayout());

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ana panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(MainFrame.PANEL_COLOR);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(8, 10, 8, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.gridx = 0;

        // Başlık
        JLabel titleLabel = MainFrame.createTitleLabel("REGISTER");
        titleLabel.setForeground(MainFrame.PRIMARY_COLOR);
        formGbc.gridy = 0;
        formGbc.gridwidth = 2;
        formPanel.add(titleLabel, formGbc);

        // Rol gösterimi
        roleLabel = new JLabel("New STUDENT Registration", SwingConstants.CENTER);
        roleLabel.setForeground(MainFrame.ACCENT_COLOR);
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formGbc.gridy = 1;
        formPanel.add(roleLabel, formGbc);

        // Boşluk
        formGbc.gridy = 2;
        formPanel.add(Box.createVerticalStrut(15), formGbc);

        // ID
        formGbc.gridy = 3;
        formGbc.gridwidth = 1;
        formPanel.add(MainFrame.createStyledLabel("Authorized ID:"), formGbc);

        idField = MainFrame.createStyledTextField();
        formGbc.gridx = 1;
        formPanel.add(idField, formGbc);

        // Full Name
        formGbc.gridy = 4;
        formGbc.gridx = 0;
        formPanel.add(MainFrame.createStyledLabel("Full Name:"), formGbc);

        fullNameField = MainFrame.createStyledTextField();
        formGbc.gridx = 1;
        formPanel.add(fullNameField, formGbc);

        // Username
        formGbc.gridy = 5;
        formGbc.gridx = 0;
        formPanel.add(MainFrame.createStyledLabel("Username:"), formGbc);

        usernameField = MainFrame.createStyledTextField();
        formGbc.gridx = 1;
        formPanel.add(usernameField, formGbc);

        // Password
        formGbc.gridy = 6;
        formGbc.gridx = 0;
        formPanel.add(MainFrame.createStyledLabel("Password:"), formGbc);

        passwordField = MainFrame.createStyledPasswordField();
        formGbc.gridx = 1;
        formPanel.add(passwordField, formGbc);

        // Boşluk
        formGbc.gridy = 7;
        formGbc.gridx = 0;
        formGbc.gridwidth = 2;
        formPanel.add(Box.createVerticalStrut(10), formGbc);

        // Mesaj label
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        formGbc.gridy = 8;
        formPanel.add(messageLabel, formGbc);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(MainFrame.PANEL_COLOR);

        JButton registerButton = MainFrame.createStyledButton("REGISTER");
        registerButton.addActionListener(e -> performRegister());

        JButton backButton = MainFrame.createSecondaryButton("← BACK TO LOGIN");
        backButton.addActionListener(e -> goBack());

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        formGbc.gridy = 9;
        formPanel.add(buttonPanel, formGbc);

        // Ana paneli ekle
        add(formPanel, gbc);
    }

    public void setRole(String role) {
        this.currentRole = role;
        roleLabel.setText("New " + role + " Registration");
    }

    private void performRegister() {
        String id = idField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (id.isEmpty() || fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields!", MainFrame.ERROR_COLOR);
            return;
        }

        boolean success = authService.register(id, fullName, username, password);

        if (success) {
            showMessage("Registration successful! You can now login.", MainFrame.ACCENT_COLOR);
            // 2 saniye sonra login'e dön
            Timer timer = new Timer(2000, e -> {
                goBack();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showMessage("Registration failed! Check ID or try different username.", MainFrame.ERROR_COLOR);
        }
    }

    private void goBack() {
        mainFrame.showPanel(MainFrame.LOGIN_PANEL);
        clearFields();
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    private void clearFields() {
        idField.setText("");
        fullNameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");
    }
}
