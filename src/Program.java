import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Program {
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
            case 1 -> PickSaveWindow.openWindow();
            case 2 -> AdminOptions.openWindow();
        }
    }

    public static void main(String[] args) {
        GameSave.createSaveFolder();
        chooseAction();
    }

    public static void newGame() {
        String username = JOptionPane.showInputDialog(null, "Please input username");
        if (username != null) {
            try {
                File file = new File("saves/"+username+".hangman.txt");
                GameSave.createSave(file);
                Hangman.openWindow(GameSave.loadSave(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}