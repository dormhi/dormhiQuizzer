import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin paneli - ID ve kullanıcı yönetimi
 */
public class AdminPanel extends JPanel {

    private MainFrame mainFrame;
    private Admin currentUser;
    private IDManager idManager;
    private AuthService authService;

    // UI Bileşenleri
    private JLabel welcomeLabel;
    private JTabbedPane tabbedPane;

    // ID Yönetimi
    private JTable idTable;
    private DefaultTableModel idTableModel;
    private JTextField newIdField;
    private JComboBox<String> roleComboBox;

    // Kullanıcı Yönetimi
    private JTable userTable;
    private DefaultTableModel userTableModel;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.idManager = new IDManager();
        this.authService = new AuthService();

        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        initComponents();
    }

    private void initComponents() {
        // Üst panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(MainFrame.BACKGROUND_COLOR);

        welcomeLabel = MainFrame.createTitleLabel("ADMIN PANEL");
        welcomeLabel.setForeground(MainFrame.ERROR_COLOR);
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

        tabbedPane.addTab("🔑 ID Management", createIdManagementPanel());
        tabbedPane.addTab("👥 User Management", createUserManagementPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createIdManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(MainFrame.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tablo
        String[] columns = { "ID", "Role" };
        idTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        idTable = new JTable(idTableModel);
        idTable.setBackground(new Color(50, 50, 50));
        idTable.setForeground(MainFrame.TEXT_COLOR);
        idTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        idTable.setRowHeight(30);
        idTable.getTableHeader().setBackground(MainFrame.ERROR_COLOR);
        idTable.getTableHeader().setForeground(Color.WHITE);
        idTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(idTable);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Alt panel - Ekleme formu
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(MainFrame.PANEL_COLOR);

        bottomPanel.add(MainFrame.createStyledLabel("New ID:"));
        newIdField = MainFrame.createStyledTextField();
        newIdField.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(newIdField);

        bottomPanel.add(MainFrame.createStyledLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[] { "STUDENT", "TEACHER", "ADMIN" });
        roleComboBox.setBackground(new Color(60, 60, 60));
        roleComboBox.setForeground(MainFrame.TEXT_COLOR);
        bottomPanel.add(roleComboBox);

        JButton addButton = MainFrame.createStyledButton("➕ ADD");
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.addActionListener(e -> addId());
        bottomPanel.add(addButton);

        JButton deleteButton = MainFrame.createSecondaryButton("🗑 DELETE");
        deleteButton.setPreferredSize(new Dimension(100, 40));
        deleteButton.addActionListener(e -> deleteId());
        bottomPanel.add(deleteButton);

        JButton refreshButton = MainFrame.createSecondaryButton("🔄 REFRESH");
        refreshButton.setPreferredSize(new Dimension(110, 40));
        refreshButton.addActionListener(e -> refreshIdList());
        bottomPanel.add(refreshButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(MainFrame.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tablo
        String[] columns = { "ID", "Role", "Full Name", "Username" };
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(userTableModel);
        userTable.setBackground(new Color(50, 50, 50));
        userTable.setForeground(MainFrame.TEXT_COLOR);
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userTable.setRowHeight(30);
        userTable.getTableHeader().setBackground(MainFrame.PRIMARY_COLOR);
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Alt panel - Şifre değiştirme
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(MainFrame.PANEL_COLOR);

        JButton changePassButton = MainFrame.createStyledButton("🔐 CHANGE PASSWORD");
        changePassButton.addActionListener(e -> changePassword());
        bottomPanel.add(changePassButton);

        JButton refreshButton = MainFrame.createSecondaryButton("🔄 REFRESH");
        refreshButton.addActionListener(e -> refreshUserList());
        bottomPanel.add(refreshButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void setCurrentUser(Admin user) {
        this.currentUser = user;
        welcomeLabel.setText("Admin: " + user.getFullName());
        refreshIdList();
        refreshUserList();
    }

    private void refreshIdList() {
        idTableModel.setRowCount(0);
        List<String[]> ids = idManager.getAllIds();
        for (String[] idData : ids) {
            if (idData.length >= 2) {
                idTableModel.addRow(new Object[] { idData[0], idData[1] });
            }
        }
    }

    private void refreshUserList() {
        userTableModel.setRowCount(0);
        List<String> users = authService.getAllRegisteredUsers();
        for (String u : users) {
            String[] parts = u.split(";");
            if (parts.length >= 4) {
                userTableModel.addRow(new Object[] { parts[0], parts[1], parts[2], parts[3] });
            }
        }
    }

    private void addId() {
        String id = newIdField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        idManager.addId(id, role);
        JOptionPane.showMessageDialog(this, "ID added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        newIdField.setText("");
        refreshIdList();
    }

    private void deleteId() {
        int row = idTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an ID to delete!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) idTableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            idManager.removeId(id);
            refreshIdList();
        }
    }

    private void changePassword() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) userTableModel.getValueAt(row, 0);
        String userName = (String) userTableModel.getValueAt(row, 2);

        String newPassword = JOptionPane.showInputDialog(this,
                "Enter new password for " + userName + ":",
                "Change Password",
                JOptionPane.QUESTION_MESSAGE);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            boolean success = authService.updateUserPassword(userId, newPassword.trim());
            if (success) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to change password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logout() {
        currentUser = null;
        mainFrame.showPanel(MainFrame.LOGIN_PANEL);
    }
}
