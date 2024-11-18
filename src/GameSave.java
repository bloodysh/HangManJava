import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The GameSave class handles saving and loading game state for the Hangman game.
 * It manages the save file, username, word, guessed characters, and incorrect guesses.
 */
public class GameSave {
    public static final String DELIMITER = "\u0000";

    private File file;
    private String username;
    private DictionaryWord word;
    private Collection<Character> guessedCharacters;
    private int incorrectGuesses;

    /**
     * Constructs a GameSave object with the specified file and username.
     *
     * @param file the save file
     * @param username the username
     */
    public GameSave(File file, String username) {
        this.file = file;
        this.username = username;
        this.guessedCharacters = new ArrayList<>();
        this.incorrectGuesses = 0;
    }

    /**
     * Constructs a GameSave object with the specified parameters.
     *
     * @param file the save file
     * @param username the username
     * @param word the dictionary word
     * @param guessedCharacters the collection of guessed characters
     * @param incorrectGuesses the number of incorrect guesses
     */
    public GameSave(File file, String username, DictionaryWord word, Collection<Character> guessedCharacters, int incorrectGuesses) {
        this.file = file;
        this.username = username;
        this.word = word;
        this.guessedCharacters = guessedCharacters;
        this.incorrectGuesses = incorrectGuesses;
    }

    /**
     * Creates a new save file.
     *
     * @param file the save file
     * @return true if the file was created, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean createSave(File file) throws IOException {
        return file.createNewFile();
    }

    /**
     * Loads a game save from the specified file.
     *
     * @param file the save file
     * @return the loaded GameSave object
     */
    public static GameSave loadSave(File file) {
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(DELIMITER);
            String username = scanner.next();
            String wordValue = scanner.next();
            String difficulty = scanner.next();
            String guessedCharactersValue = scanner.next();
            int incorrectGuesses = scanner.nextInt();
            return new GameSave(
                    file,
                    username,
                    new DictionaryWord(wordValue, Difficulty.valueOf(difficulty)),
                    guessedCharactersValue.isEmpty() ?
                            new ArrayList<>() :
                            Arrays.stream(guessedCharactersValue.split(":"))
                                    .map(str -> str.charAt(0)).collect(Collectors.toList()),
                    incorrectGuesses
            );
        } catch (NoSuchElementException e) {
            return new GameSave(file, file.getName().substring(0, file.getName().length() - 12));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the save folder if it does not exist.
     */
    public static void createSaveFolder() {
        File savesFolder = new File("saves");
        savesFolder.mkdirs();
    }

    /**
     * Saves the current game state to the file.
     */
    public void saveFile() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(username);
        stringBuilder.append(DELIMITER);
        stringBuilder.append(word.toString());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(word.getDifficulty());
        stringBuilder.append(DELIMITER);
        stringBuilder.append(guessedCharacters.stream().map(String::valueOf).collect(Collectors.joining(":")));
        stringBuilder.append(DELIMITER);
        stringBuilder.append(incorrectGuesses);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(stringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Resets the game state with a new word.
     *
     * @param word the new dictionary word
     */
    public void reset(DictionaryWord word) {
        this.word = word;
        clearGuessedCharacters();
        setIncorrectGuesses(0);
    }

    /**
     * Gets the dictionary word.
     *
     * @return the dictionary word
     */
    public DictionaryWord getWord() {
        return word;
    }

    /**
     * Gets the difficulty level of the word.
     *
     * @return the difficulty level
     */
    public Difficulty getDifficulty() {
        return word.getDifficulty();
    }

    /**
     * Sets the dictionary word.
     *
     * @param word the dictionary word
     */
    public void setWord(DictionaryWord word) {
        this.word = word;
    }

    /**
     * Gets the collection of guessed characters.
     *
     * @return the collection of guessed characters
     */
    public Collection<Character> getGuessedCharacters() {
        return guessedCharacters;
    }

    /**
     * Gets the hidden value of the word with guessed characters revealed.
     *
     * @return the hidden value of the word
     */
    public String getHiddenValue() {
        return word.getHiddenValue(guessedCharacters);
    }

    /**
     * Clears the collection of guessed characters.
     */
    public void clearGuessedCharacters() {
        guessedCharacters.clear();
    }

    /**
     * Adds a guessed character to the collection.
     *
     * @param character the guessed character
     * @return true if the character was added, false otherwise
     */
    public boolean addGuessedCharacter(Character character) {
        return guessedCharacters.add(character);
    }

    /**
     * Checks if a character has been guessed.
     *
     * @param character the character to check
     * @return true if the character has been guessed, false otherwise
     */
    public boolean isCharacterGuessed(Character character) {
        return guessedCharacters.contains(character);
    }

    /**
     * Gets the number of incorrect guesses.
     *
     * @return the number of incorrect guesses
     */
    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    /**
     * Increments the number of incorrect guesses by one.
     */
    public void incrementIncorrectGuess() {
        this.incorrectGuesses++;
    }

    /**
     * Sets the number of incorrect guesses.
     *
     * @param incorrectGuesses the number of incorrect guesses
     */
    public void setIncorrectGuesses(int incorrectGuesses) {
        this.incorrectGuesses = incorrectGuesses;
    }

    /**
     * Gets the save file.
     *
     * @return the save file
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the save file.
     *
     * @param file the save file
     */
    public void setFile(File file) {
        this.file = file;
    }
}