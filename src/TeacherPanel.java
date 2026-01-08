import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Öğretmen paneli - Soru yönetimi
 */
public class TeacherPanel extends JPanel {

    private MainFrame mainFrame;
    private Teacher currentUser;
    private QuestionLoader questionLoader;
    private AuthService authService;

    // UI Bileşenleri
    private JLabel welcomeLabel;
    private JTabbedPane tabbedPane;
    private JTable questionTable;
    private DefaultTableModel tableModel;

    // Soru ekleme formu
    private JComboBox<String> questionTypeCombo;
    private JTextField questionTextField;
    private JTextField scoreField;
    private JTextField option1Field, option2Field, option3Field, option4Field;
    private JTextField correctAnswerField;
    private JPanel optionsPanel;

    public TeacherPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.questionLoader = new QuestionLoader();
        this.authService = new AuthService();

        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        initComponents();
    }

    private void initComponents() {
        // Üst panel - Başlık ve çıkış
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(MainFrame.BACKGROUND_COLOR);

        welcomeLabel = MainFrame.createTitleLabel("TEACHER PANEL");
        welcomeLabel.setForeground(MainFrame.PRIMARY_COLOR);
        welcomeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton logoutButton = MainFrame.createSecondaryButton("← LOGOUT");
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> logout());

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Sekmeli panel
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(MainFrame.PANEL_COLOR);
        tabbedPane.setForeground(MainFrame.TEXT_COLOR);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Sekme 1: Soru Ekle
        tabbedPane.addTab("➕ Add Question", createAddQuestionPanel());

        // Sekme 2: Soru Listesi
        tabbedPane.addTab("📋 Question List", createQuestionListPanel());

        // Sekme 3: Öğrenci Listesi
        tabbedPane.addTab("👥 Students", createStudentListPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAddQuestionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(MainFrame.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Soru tipi
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(MainFrame.createStyledLabel("Question Type:"), gbc);

        questionTypeCombo = new JComboBox<>(new String[] { "Multiple Choice", "True/False" });
        questionTypeCombo.setBackground(new Color(60, 60, 60));
        questionTypeCombo.setForeground(MainFrame.TEXT_COLOR);
        questionTypeCombo.addActionListener(e -> toggleOptionsVisibility());
        gbc.gridx = 1;
        panel.add(questionTypeCombo, gbc);

        // Soru metni
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(MainFrame.createStyledLabel("Question Text:"), gbc);

        questionTextField = MainFrame.createStyledTextField();
        questionTextField.setPreferredSize(new Dimension(400, 45));
        gbc.gridx = 1;
        panel.add(questionTextField, gbc);

        // Puan
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(MainFrame.createStyledLabel("Score:"), gbc);

        scoreField = MainFrame.createStyledTextField();
        scoreField.setPreferredSize(new Dimension(100, 45));
        gbc.gridx = 1;
        panel.add(scoreField, gbc);

        // Seçenekler paneli (MC için)
        optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(MainFrame.PANEL_COLOR);
        GridBagConstraints optGbc = new GridBagConstraints();
        optGbc.insets = new Insets(5, 10, 5, 10);
        optGbc.fill = GridBagConstraints.HORIZONTAL;

        optGbc.gridx = 0;
        optGbc.gridy = 0;
        optionsPanel.add(MainFrame.createStyledLabel("Option 1:"), optGbc);
        option1Field = MainFrame.createStyledTextField();
        optGbc.gridx = 1;
        optionsPanel.add(option1Field, optGbc);

        optGbc.gridx = 0;
        optGbc.gridy = 1;
        optionsPanel.add(MainFrame.createStyledLabel("Option 2:"), optGbc);
        option2Field = MainFrame.createStyledTextField();
        optGbc.gridx = 1;
        optionsPanel.add(option2Field, optGbc);

        optGbc.gridx = 0;
        optGbc.gridy = 2;
        optionsPanel.add(MainFrame.createStyledLabel("Option 3:"), optGbc);
        option3Field = MainFrame.createStyledTextField();
        optGbc.gridx = 1;
        optionsPanel.add(option3Field, optGbc);

        optGbc.gridx = 0;
        optGbc.gridy = 3;
        optionsPanel.add(MainFrame.createStyledLabel("Option 4:"), optGbc);
        option4Field = MainFrame.createStyledTextField();
        optGbc.gridx = 1;
        optionsPanel.add(option4Field, optGbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(optionsPanel, gbc);

        // Doğru cevap
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(MainFrame.createStyledLabel("Correct Answer:"), gbc);

        correctAnswerField = MainFrame.createStyledTextField();
        correctAnswerField.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 1;
        panel.add(correctAnswerField, gbc);

        JLabel hintLabel = new JLabel("(MC: 1-4 | TF: true/false)");
        hintLabel.setForeground(new Color(150, 150, 150));
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        gbc.gridy = 5;
        panel.add(hintLabel, gbc);

        // Ekle butonu
        JButton addButton = MainFrame.createStyledButton("ADD QUESTION");
        addButton.addActionListener(e -> addQuestion());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        return panel;
    }

    private void toggleOptionsVisibility() {
        boolean isMC = questionTypeCombo.getSelectedIndex() == 0;
        optionsPanel.setVisible(isMC);
    }

    private JPanel createQuestionListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(MainFrame.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tablo
        String[] columns = { "#", "Type", "Question", "Score" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        questionTable = new JTable(tableModel);
        questionTable.setBackground(new Color(50, 50, 50));
        questionTable.setForeground(MainFrame.TEXT_COLOR);
        questionTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        questionTable.setRowHeight(30);
        questionTable.getTableHeader().setBackground(MainFrame.PRIMARY_COLOR);
        questionTable.getTableHeader().setForeground(Color.WHITE);
        questionTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(questionTable);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(MainFrame.PANEL_COLOR);

        JButton refreshButton = MainFrame.createSecondaryButton("🔄 REFRESH");
        refreshButton.addActionListener(e -> refreshQuestionList());

        JButton deleteButton = MainFrame.createStyledButton("🗑 DELETE SELECTED");
        deleteButton.setBackground(MainFrame.ERROR_COLOR);
        deleteButton.addActionListener(e -> deleteSelectedQuestion());

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(MainFrame.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Öğrenci tablosu
        String[] columns = { "ID", "Full Name", "Username" };
        DefaultTableModel studentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable studentTable = new JTable(studentModel);
        studentTable.setBackground(new Color(50, 50, 50));
        studentTable.setForeground(MainFrame.TEXT_COLOR);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        studentTable.setRowHeight(30);
        studentTable.getTableHeader().setBackground(MainFrame.ACCENT_COLOR);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        // Öğrencileri yükle
        List<String> users = authService.getAllRegisteredUsers();
        for (String u : users) {
            String[] parts = u.split(";");
            if (parts.length >= 4 && parts[1].equalsIgnoreCase("STUDENT")) {
                studentModel.addRow(new Object[] { parts[0], parts[2], parts[3] });
            }
        }

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void setCurrentUser(Teacher user) {
        this.currentUser = user;
        welcomeLabel.setText("Teacher: " + user.getFullName());
        refreshQuestionList();
    }

    private void addQuestion() {
        String text = questionTextField.getText().trim();
        String scoreStr = scoreField.getText().trim();
        String answer = correctAnswerField.getText().trim();

        if (text.isEmpty() || scoreStr.isEmpty() || answer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String csvLine;
        if (questionTypeCombo.getSelectedIndex() == 0) {
            // Multiple Choice
            String opt1 = option1Field.getText().trim();
            String opt2 = option2Field.getText().trim();
            String opt3 = option3Field.getText().trim();
            String opt4 = option4Field.getText().trim();

            if (opt1.isEmpty() || opt2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "At least 2 options required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            csvLine = "MC;" + text + ";" + scoreStr + ";" + answer + ";" + opt1 + ";" + opt2;
            if (!opt3.isEmpty())
                csvLine += ";" + opt3;
            if (!opt4.isEmpty())
                csvLine += ";" + opt4;
        } else {
            // True/False
            csvLine = "TF;" + text + ";" + scoreStr + ";" + answer;
        }

        questionLoader.appendQuestion(csvLine);
        JOptionPane.showMessageDialog(this, "Question added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        refreshQuestionList();
    }

    private void refreshQuestionList() {
        tableModel.setRowCount(0);
        List<String> lines = questionLoader.getAllRawQuestions();
        int index = 1;
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 3) {
                tableModel.addRow(new Object[] { index++, parts[0], parts[1], parts[2] });
            }
        }
    }

    private void deleteSelectedQuestion() {
        int row = questionTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a question to delete!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            questionLoader.deleteQuestionAt(row);
            refreshQuestionList();
        }
    }

    private void clearForm() {
        questionTextField.setText("");
        scoreField.setText("");
        option1Field.setText("");
        option2Field.setText("");
        option3Field.setText("");
        option4Field.setText("");
        correctAnswerField.setText("");
    }

    private void logout() {
        currentUser = null;
        mainFrame.showPanel(MainFrame.LOGIN_PANEL);
    }
}
