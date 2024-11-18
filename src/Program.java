import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * The Program class is the entry point for the Hangman game application.
 * It provides methods to start a new game, load a saved game, or open the admin options.
 */
public class Program {

    /**
     * Displays a dialog to choose an action: start a new game, load a saved game, or open admin options.
     */
    public static void chooseAction() {
        String[] options = {"New Game", "Load save", "Admin"};
        int chosenOption = JOptionPane.showOptionDialog(
                null,
                "Welcome to Hangman!",
                "Hangman",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        switch (chosenOption) {
            case 0 -> newGame();
            case 1 -> OpenSaveWindow.openWindow();
            case 2 -> AdminOptions.openWindow();
        }
    }

    /**
     * The main method, which is the entry point of the application.
     * It creates the save folder and displays the action choice dialog.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameSave.createSaveFolder();
        chooseAction();
    }

    /**
     * Starts a new game by prompting the user for a username and creating a new save file.
     * Opens the Hangman game window with the new save file.
     */
    public static void newGame() {
        String username = JOptionPane.showInputDialog(null, "Please input username");
        if (username != null) {
            try {
                File file = new File("saves/" + username + ".hangman.txt");
                GameSave.createSave(file);
                Hangman.openWindow(GameSave.loadSave(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}