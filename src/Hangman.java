import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;

public class Hangman extends JFrame implements ActionListener {
    private Dictionnaire dictio;
    private JLabel hangmanImage;
    private JLabel hiddenWordLabel;
    private int incorrectGuesses;
    private String[] gameWord;
    private JButton[] letterButtons;

    public Hangman() {
        setTitle("Hangman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 760);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);

        dictio = new Dictionnaire();
        letterButtons = new JButton[26];
        int difficulty = chooseDifficulty();
        gameWord = dictio.loadChallenge(difficulty);

        addGuiComponents();
    }

    private int chooseDifficulty() {
        String[] options = {"1 - Easy", "2 - Medium", "3 - Hard"};
        int choice = JOptionPane.showOptionDialog(this, "Choose difficulty level:", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return choice + 1;
    }

    private void addGuiComponents() {
        hangmanImage = CustomTools.loadImage("/images/1.png");
        hangmanImage.setBounds(0, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);

        hiddenWordLabel = new JLabel(CustomTools.hideWord(gameWord[0]));
        hiddenWordLabel.setForeground(Color.WHITE);
        hiddenWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hiddenWordLabel.setFont(new Font("Arial", Font.BOLD, 30));
        hiddenWordLabel.setBounds(0, hangmanImage.getHeight(), 540, 50);

        JPanel letterPanel = createLetterPanel();

        getContentPane().add(hangmanImage);
        getContentPane().add(hiddenWordLabel);
        getContentPane().add(letterPanel);
    }

    private JPanel createLetterPanel() {
        GridLayout gridLayout = new GridLayout(5, 6);
        JPanel letterPanel = new JPanel(gridLayout);
        letterPanel.setBounds(-5, hiddenWordLabel.getY() + hiddenWordLabel.getPreferredSize().height + 20, 540, (int) (760 * 0.42));
        letterPanel.setLayout(gridLayout);

        for (char c = 'A'; c <= 'Z'; c++) {
            JButton letterButton = createButton(Character.toString(c), Color.BLACK, Color.WHITE);
            letterButtons[c - 'A'] = letterButton;
            letterPanel.add(letterButton);
        }

        letterPanel.add(createButton("Reset", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Quit", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Save", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Load", Color.CYAN, Color.WHITE));

        return letterPanel;
    }

    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Reset":
                resetGame();
                break;
            case "Quit":
                dispose();
                break;
            case "Save":
                saveGame();
                break;
            case "Load":
                loadGame();
                break;
            default:
                handleLetterButton(actionCommand, (JButton) e.getSource());
                break;
        }
    }

    private void handleLetterButton(String actionCommand, JButton clickedButton) {
        clickedButton.setEnabled(false);
        if (gameWord[0].contains(actionCommand)) {
            clickedButton.setBackground(Color.GREEN);
            updateHiddenWord(actionCommand);
            if (!hiddenWordLabel.getText().contains("_")) {
                hiddenWordLabel.setForeground(Color.GREEN);
                JOptionPane.showMessageDialog(this, "You won!");
                resetGame();
            }
        } else {
            clickedButton.setBackground(Color.RED);
            incorrectGuesses++;
            CustomTools.updateImage(hangmanImage, "/images/" + (incorrectGuesses + 1) + ".png");
            if (incorrectGuesses == 6) {
                hiddenWordLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "You lost! The word was: " + gameWord[0]);
                resetGame();
            }
        }
    }

    private void updateHiddenWord(String actionCommand) {
        char[] hiddenWord = hiddenWordLabel.getText().toCharArray();
        for (int i = 0; i < gameWord[0].length(); i++) {
            if (gameWord[0].charAt(i) == actionCommand.charAt(0)) {
                hiddenWord[i * 2] = actionCommand.charAt(0);
            }
        }
        hiddenWordLabel.setText(new String(hiddenWord));
    }

    private void resetGame() {
        int difficulty = chooseDifficulty();
        gameWord = dictio.loadChallenge(difficulty);
        incorrectGuesses = 0;
        CustomTools.updateImage(hangmanImage, "/images/1.png");
        hiddenWordLabel.setText(CustomTools.hideWord(gameWord[0]));
        hiddenWordLabel.setForeground(Color.WHITE);
        for (JButton letterButton : letterButtons) {
            letterButton.setEnabled(true);
            letterButton.setBackground(Color.BLACK);
        }
    }

    private void saveGame() {
        CustomTools.saveGame(gameWord, hiddenWordLabel, incorrectGuesses, letterButtons);
    }

    private void loadGame() {
        Object[] loadedState = CustomTools.loadGame(hiddenWordLabel, letterButtons);
        if (loadedState != null) {
            gameWord = (String[]) loadedState[0];
            incorrectGuesses = (int) loadedState[1];
        }
    }
}