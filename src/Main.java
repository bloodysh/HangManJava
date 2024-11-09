import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String username = JOptionPane.showInputDialog(null, "Enter your username:");
                if (username == null || username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username is required to start the game.");
                    System.exit(0);
                } else {
                    CustomTools.saveUsername(username);
                    Hangman hangman = new Hangman(username);
                    hangman.setVisible(true);
                }
            }
        });
    }
}