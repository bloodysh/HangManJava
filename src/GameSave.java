import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GameSave {
    public static final String DELIMITER = "\u0000";

    private File file;
    private String username;
    private DictionaryWord word;
    private Collection<Character> guessedCharacters;
    private int incorrectGuesses;

    public GameSave(File file, String username) {
        this.file = file;
        this.username = username;
        this.guessedCharacters = new ArrayList<>();
        this.incorrectGuesses = 0;
    }

    public GameSave(File file, String username, DictionaryWord word, Collection<Character> guessedCharacters, int incorrectGuesses) {
        this.file = file;
        this.username = username;
        this.word = word;
        this.guessedCharacters = guessedCharacters;
        this.incorrectGuesses = incorrectGuesses;
    }

    public static boolean createSave(File file) throws IOException {
        return file.createNewFile();
    }

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

    public static void createSaveFolder() {
        File savesFolder = new File("saves");
        savesFolder.mkdirs();
    }

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

    public String getUsername() {
        return username;
    }

    public void reset(DictionaryWord word) {
        this.word = word;
        clearGuessedCharacters();
        setIncorrectGuesses(0);
    }

    public DictionaryWord getWord() {
        return word;
    }

    public Difficulty getDifficulty() {
        return word.getDifficulty();
    }

    public void setWord(DictionaryWord word) {
        this.word = word;
    }

    public Collection<Character> getGuessedCharacters() {
        return guessedCharacters;
    }

    public String getHiddenValue() {
        return word.getHiddenValue(guessedCharacters);
    }

    public void clearGuessedCharacters() {
        guessedCharacters.clear();
    }

    public boolean addGuessedCharacter(Character character) {
        return guessedCharacters.add(character);
    }

    public boolean isCharacterGuessed(Character character) {
        return guessedCharacters.contains(character);
    }

    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public void incrementIncorrectGuess() {
        this.incorrectGuesses++;
    }

    public void setIncorrectGuesses(int incorrectGuesses) {
        this.incorrectGuesses = incorrectGuesses;
    }
}
