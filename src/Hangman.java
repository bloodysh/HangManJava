import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Hangman class represents the main game window for the Hangman game.
 * It handles the game logic, user interactions, and GUI components.
 */
public class Hangman extends JFrame implements ActionListener {
    private final Dictionary dictionary;
    private JImageResourceLabel hangmanImage;
    private JLabel hiddenWordLabel;
    private final JButton[] letterButtons;
    private final GameSave gameSave;

    /**
     * Gets the username of the current game save.
     *
     * @return the username
     */
    public String getUsername() {
        return gameSave.getUsername();
    }

    /**
     * Constructs a Hangman object with the specified game save.
     * Sets up the window properties and initializes the components.
     *
     * @param gameSave the game save object
     */
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
        addMenu();
    }

    /**
     * Opens the Hangman game window with the specified game save.
     *
     * @param gameSave the game save object
     */
    public static void openWindow(GameSave gameSave) {
        SwingUtilities.invokeLater(() -> {
            Hangman hangman = new Hangman(gameSave);
            hangman.setVisible(true);
        });
    }

    /**
     * Adds the menu bar and menu items to the window.
     */
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem adminMenuItem = new JMenuItem("Admin");
        JMenuItem aboutMenuItem = new JMenuItem("About this game");

        // Customize font
        Font font = new Font("Arial", Font.BOLD, 20);
        menu.setFont(font);
        adminMenuItem.setFont(font);
        aboutMenuItem.setFont(font);

        // Customize color
        menu.setForeground(Color.WHITE);
        adminMenuItem.setForeground(Color.DARK_GRAY);
        adminMenuItem.setBackground(Color.GRAY);
        aboutMenuItem.setForeground(Color.DARK_GRAY);
        aboutMenuItem.setBackground(Color.GRAY);

        // Add admin icon
        ImageIcon adminIcon = new ImageIcon(getClass().getResource("/images/Admin.png"));
        Image image = adminIcon.getImage();
        Image scaledImage = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        adminIcon = new ImageIcon(scaledImage);
        adminMenuItem.setIcon(adminIcon);

        adminMenuItem.addActionListener(e -> {
            gameSave.saveFile();
            AdminOptions.openWindow();
            dispose();
        });

        aboutMenuItem.addActionListener(e -> {
            AboutWindow.openWindow();
        });

        menu.add(adminMenuItem);
        menu.add(aboutMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Set menu bar background color
        menuBar.setBackground(Color.DARK_GRAY);
    }

    /**
     * Prompts the user to choose a difficulty level.
     *
     * @return the chosen difficulty level
     */
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
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    /**
     * Adds the GUI components to the window.
     */
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

    /**
     * Determines the color of the letter button based on whether the letter has been guessed.
     *
     * @param letter the letter to check
     * @return the color of the button
     */
    private Color determineButtonColor(char letter) {
        if (gameSave.isCharacterGuessed(letter)) {
            return gameSave.getWord().containsLetter(letter) ? Color.GREEN : Color.RED;
        }
        return Color.BLACK;
    }

    /**
     * Creates the panel containing the letter buttons.
     *
     * @return the letter panel
     */
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
        letterPanel.add(createButton("Save as", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Load", Color.CYAN, Color.WHITE));
        letterPanel.add(createButton("Admin", Color.ORANGE, Color.WHITE));

        return letterPanel;
    }

    /**
     * Creates a button with the specified text, background color, and foreground color.
     *
     * @param text the text of the button
     * @param bgColor the background color of the button
     * @param fgColor the foreground color of the button
     * @return the created button
     */
    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.addActionListener(this);
        return button;
    }

    /**
     * Handles the action performed event for the buttons.
     *
     * @param e the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Reset":
                resetGame();
                break;
            case "Save as":
                SaveAsWindow.openWindow(gameSave);
                break;
            case "Load":
                OpenSaveWindow openSaveWindow = new OpenSaveWindow(this::dispose);
                openSaveWindow.setVisible(true);
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

    /**
     * Handles the letter button click event.
     *
     * @param letter the letter of the clicked button
     * @param clickedButton the clicked button
     */
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

    /**
     * Updates the hidden word label with the current hidden value.
     */
    private void updateHiddenWord() {
        hiddenWordLabel.setText(gameSave.getHiddenValue());
    }

    /**
     * Resets the game with a new word and difficulty level.
     */
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

    /**
     * Resets the UI components to their initial state.
     */
    private void resetUI() {
        hangmanImage.setImageResourcePath("/images/1.png");
        hiddenWordLabel.setForeground(Color.WHITE);
        for (JButton letterButton : letterButtons) {
            letterButton.setEnabled(true);
            letterButton.setBackground(Color.BLACK);
        }
    }
}