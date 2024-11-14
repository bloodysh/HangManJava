import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Hangman extends JFrame implements ActionListener {
    private final Dictionary dictionary;
    private JImageResourceLabel hangmanImage;
    private JLabel hiddenWordLabel;
    private final JButton[] letterButtons;

    private final GameSave gameSave;


    public String getUsername() {
        return gameSave.getUsername();
    }

    public Hangman(GameSave gameSave) {
        setTitle("Hangman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 760);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);

        this.gameSave = gameSave;
        dictionary = new Dictionary();
        letterButtons = new JButton[26];

        if (gameSave.getWord() == null) {
            resetGame();
        }

        addGuiComponents();
    }

    public static void openWindow(GameSave gameSave) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Hangman hangman = new Hangman(gameSave);
                hangman.setVisible(true);
            }
        });
    }

    private Difficulty chooseDifficulty() {
        String[] options = {"1 - Easy", "2 - Medium", "3 - Hard"};
        int choice = JOptionPane.showOptionDialog(this, "Choose difficulty level:", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            System.exit(0); // Exit the application if the dialog is closed
        }

        return switch (choice) {
            case 0 -> Difficulty.Easy;
            case 1 -> Difficulty.Medium;
            case 2 -> Difficulty.Hard;
            default ->
                    throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    private void addGuiComponents() {
        hangmanImage = new JImageResourceLabel("/images/" + (gameSave.getIncorrectGuesses() + 1) + ".png");
        hangmanImage.setBounds(0, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);

        hiddenWordLabel = new JLabel(gameSave.getHiddenValue());
        hiddenWordLabel.setForeground(Color.WHITE);
        hiddenWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hiddenWordLabel.setFont(new Font("Arial", Font.BOLD, 30));
        hiddenWordLabel.setBounds(0, hangmanImage.getHeight(), 540, 50);

        JPanel letterPanel = createLetterPanel();

        getContentPane().add(hangmanImage);
        getContentPane().add(hiddenWordLabel);
        getContentPane().add(letterPanel);
    }

    private Color determineButtonColor(char letter) {
        if (gameSave.isCharacterGuessed(letter)) {
            return gameSave.getWord().containsLetter(letter) ? Color.GREEN:Color.RED;
        }
        return Color.BLACK;
    }

    private JPanel createLetterPanel() {
        GridLayout gridLayout = new GridLayout(5, 6);
        JPanel letterPanel = new JPanel(gridLayout);
        letterPanel.setBounds(-5, hiddenWordLabel.getY() + hiddenWordLabel.getPreferredSize().height + 20, 540, (int) (760 * 0.42));
        letterPanel.setLayout(gridLayout);

        for (char letter = 'A'; letter <= 'Z'; letter++) {
            JButton letterButton = createButton(Character.toString(letter), determineButtonColor(letter), Color.WHITE);
            letterButtons[letter - 'A'] = letterButton;
            letterPanel.add(letterButton);
        }

        letterPanel.add(createButton("Reset", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Save", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Load", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Admin", Color.ORANGE, Color.WHITE));

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
            case "Save":
                gameSave.saveFile();
                break;
            case "Load":
                gameSave.saveFile();
                Program.main(new String[]{});
                dispose();
                break;
            case "Admin":
                gameSave.saveFile();
                AdminOptions.openWindow();
                dispose();
                break;
            default:
                handleLetterButton(actionCommand.charAt(0), (JButton) e.getSource());
                break;
        }
    }

    private void handleLetterButton(char letter, JButton clickedButton) {
        clickedButton.setEnabled(false);
        gameSave.addGuessedCharacter(letter);
        updateHiddenWord();
        if (gameSave.getWord().containsLetter(letter)) {
            clickedButton.setBackground(Color.GREEN);
            if (!hiddenWordLabel.getText().contains("_")) {
                hiddenWordLabel.setForeground(Color.GREEN);
                JOptionPane.showMessageDialog(this, getUsername() + ", You won!");
                resetGame();
            }
        } else {
            clickedButton.setBackground(Color.RED);
            gameSave.incrementIncorrectGuess();
            hangmanImage.setImageResourcePath("/images/" + (gameSave.getIncorrectGuesses() + 1) + ".png");
            if (gameSave.getIncorrectGuesses() == 6) {
                hiddenWordLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "You lost! The word was: " + gameSave.getWord().getRawValue());
                resetGame();
            }
        }
    }

    private void updateHiddenWord() {
        hiddenWordLabel.setText(gameSave.getHiddenValue());
    }

    private void resetGame() {
        Difficulty difficulty = chooseDifficulty();
        gameSave.setWord(dictionary.pickRandomWord(difficulty));
        gameSave.clearGuessedCharacters();
        gameSave.setIncorrectGuesses(0);
        gameSave.saveFile();
        if (hiddenWordLabel != null) {
            updateHiddenWord();
            resetUI();
        }
    }

    private void resetUI() {
        hangmanImage.setImageResourcePath("/images/1.png");
        hiddenWordLabel.setForeground(Color.WHITE);
        for (JButton letterButton : letterButtons) {
            letterButton.setEnabled(true);
            letterButton.setBackground(Color.BLACK);
        }
    }
}