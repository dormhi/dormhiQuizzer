import javax.swing.SwingUtilities;

/**
 * Main class - Starts the Quiz System with Swing UI
 */
public class Main {
    public static void main(String[] args) {
        // Run Swing UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}