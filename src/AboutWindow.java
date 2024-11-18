import javax.swing.*;
import java.awt.*;

/**
 * The AboutWindow class represents a window that displays information about the Hangman game.
 * It provides details on how to play the game and some tips.
 */
public class AboutWindow extends JFrame {

    /**
     * Constructs an AboutWindow object.
     * Sets up the window properties and adds the text area with game information.
     */
    public AboutWindow() {
        setTitle("About this game");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea aboutText = new JTextArea();
        aboutText.setText("Hangman Game\n\n" +
                "How to play:\n" +
                "1. Guess the word by selecting letters.\n" +
                "2. Each incorrect guess adds a part to the hangman.\n" +
                "3. The game ends when the word is guessed or the hangman is complete.\n\n" +
                "Tips:\n" +
                "- Start with common vowels (A, E, I, O, U).\n" +
                "- Look for common consonants (R, S, T, L, N).\n" +
                "- Use the process of elimination.\n" +
                " Developed By: Anas Benmsatef & Leon Lozahic\n"
        );
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutText.setMargin(new Insets(10, 10, 10, 10));

        add(new JScrollPane(aboutText), BorderLayout.CENTER);
    }

    /**
     * Opens the AboutWindow.
     * This method is called to display the AboutWindow.
     */
    public static void openWindow() {
        SwingUtilities.invokeLater(() -> {
            AboutWindow aboutWindow = new AboutWindow();
            aboutWindow.setVisible(true);
        });
    }
}