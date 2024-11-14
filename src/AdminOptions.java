import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AdminOptions extends JFrame implements ListSelectionListener, ActionListener {
    Dictionary dictionary;
    Difficulty currentDifficulty;
    List<DictionaryWord> selectedWords;
    JList<DictionaryWord> wordsList;
    JComboBox<String> difficultyComboBox;
    JButton addWordButton;
    JButton removeWordButton;
    JButton goBackToGameButton;

    public AdminOptions() {
        dictionary = new Dictionary();
        currentDifficulty = Difficulty.Easy;
        selectedWords = new ArrayList<>();
        setTitle("Hangman Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 760);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        build();
    }

    private void build() {
        wordsList = new JList<>(new Vector<>(dictionary.getWordsByDifficulty(currentDifficulty)));
        wordsList.addListSelectionListener(this);
        JScrollPane scrollList = new JScrollPane(wordsList);
        scrollList.setPreferredSize(new Dimension(200, 400));

        JPanel buttons = new JPanel();
        GridLayout buttonsLayout = new GridLayout(4, 1);
        buttonsLayout.setVgap(10);
        buttons.setLayout(buttonsLayout);

        difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyComboBox.addActionListener(this);
        buttons.add(difficultyComboBox);

        addWordButton = new JButton("Add word");
        addWordButton.addActionListener(this);
        buttons.add(addWordButton);

        removeWordButton = new JButton("Remove word");
        removeWordButton.addActionListener(this);
        removeWordButton.setEnabled(false);
        buttons.add(removeWordButton);

        goBackToGameButton = new JButton("Back to game");
        goBackToGameButton.addActionListener(this);
        buttons.add(goBackToGameButton);

        add(scrollList);
        add(buttons);
        pack();
    }

    public void setCurrentDifficulty(Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
        reloadWordList();
    }

    private void reloadWordList() {
        wordsList.setListData(new Vector<>(dictionary.getWordsByDifficulty(currentDifficulty)));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        selectedWords = wordsList.getSelectedValuesList();
        removeWordButton.setEnabled(!selectedWords.isEmpty());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == difficultyComboBox && e.getActionCommand().equals("comboBoxChanged")) {
            switch (difficultyComboBox.getSelectedIndex()) {
                case 0 -> setCurrentDifficulty(Difficulty.Easy);
                case 1 -> setCurrentDifficulty(Difficulty.Medium);
                case 2 -> setCurrentDifficulty(Difficulty.Hard);
            }
        } else if (e.getSource() == removeWordButton) {
            deleteSelectedWords();
        } else if (e.getSource() == addWordButton) {
            promptForNewWord();
        } else if (e.getSource() == goBackToGameButton) {
            Program.main(new String[]{});
            dispose();
        }
    }

    private void deleteSelectedWords() {
        for (DictionaryWord word: selectedWords) {
            dictionary.removeWord(word);
        }
        dictionary.saveDictionary();
        reloadWordList();
    }

    private void promptForNewWord() {
        String wordValue = JOptionPane.showInputDialog(this, "Enter new word (Difficulty " + currentDifficulty.toString() + ") :");
        if (wordValue == null || wordValue.isBlank()) return;
        DictionaryWord word = new DictionaryWord(wordValue, currentDifficulty);
        dictionary.addWord(word);
        dictionary.saveDictionary();
        reloadWordList();
    }

    public static void main(String[] args) {
        openWindow();
    }

    public static void openWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminOptions window = new AdminOptions();
                window.setVisible(true);
            }
        });
    }
}
