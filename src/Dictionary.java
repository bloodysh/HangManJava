import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Dictionary class represents a collection of words categorized by difficulty levels.
 * It provides methods to load, save, add, remove, and pick random words from the dictionary.
 */
public class Dictionary {
    private final List<DictionaryWord> words;

    /**
     * Constructs a Dictionary object and loads the words from the file.
     */
    public Dictionary() {
        words = new ArrayList<>();
        loadWords();
    }

    /**
     * Gets the file containing the dictionary words.
     * If the file does not exist, it creates a new file with default words.
     *
     * @return the File object representing the dictionary file
     */
    private static File getFile() {
        File file = new File("words.txt");
        if (!file.isFile()) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write("Easy|POO|coucou|test\nMedium|Projet|Object\nHard|Polymorphisme|Constitution");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    /**
     * Loads the words from the dictionary file into the words list.
     */
    private void loadWords() {
        try {
            File file = getFile();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parsed = line.split("\\|");
                String difficultyWord = parsed[0];
                Difficulty difficulty = Difficulty.valueOf(difficultyWord);
                words.addAll(Arrays.stream(parsed).skip(1).map(s -> new DictionaryWord(s, difficulty)).toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current state of the dictionary to the file.
     */
    public void saveDictionary() {
        File file = getFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            StringBuilder output = new StringBuilder();
            for (Difficulty difficulty : Difficulty.values()) {
                output
                        .append(difficulty.toString())
                        .append('|')
                        .append(
                                getWordsByDifficulty(difficulty)
                                        .stream()
                                        .map(DictionaryWord::getRawValue)
                                        .collect(Collectors.joining("|"))
                        ).append('\n');
            }
            fileWriter.write(output.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the list of words filtered by the specified difficulty.
     *
     * @param difficulty the difficulty level to filter the words
     * @return the list of words with the specified difficulty
     */
    public List<DictionaryWord> getWordsByDifficulty(Difficulty difficulty) {
        return words.stream().filter(word -> difficulty == word.getDifficulty()).toList();
    }

    /**
     * Adds a new word to the dictionary.
     *
     * @param word the DictionaryWord object to add
     */
    public void addWord(DictionaryWord word) {
        words.add(word);
    }

    /**
     * Removes a word from the dictionary.
     *
     * @param word the DictionaryWord object to remove
     */
    public void removeWord(DictionaryWord word) {
        words.remove(word);
    }

    /**
     * Picks a random word from the dictionary with the specified difficulty.
     *
     * @param difficulty the difficulty level to filter the words
     * @return a randomly picked DictionaryWord object
     */
    public DictionaryWord pickRandomWord(Difficulty difficulty) {
        Random rand = new Random();
        List<DictionaryWord> filteredWords = words.stream().filter(word -> difficulty == word.getDifficulty()).toList();
        return filteredWords.get(rand.nextInt(filteredWords.size()));
    }
}