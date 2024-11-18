import java.util.ArrayList;
import java.util.Collection;

/**
 * The DictionaryWord class represents a word in the dictionary with an associated difficulty level.
 */
public class DictionaryWord {
    String value;
    Difficulty difficulty;

    /**
     * Constructs a DictionaryWord object with the specified value and difficulty.
     *
     * @param value the word value
     * @param difficulty the difficulty level of the word
     */
    public DictionaryWord(String value, Difficulty difficulty) {
        this.value = value;
        this.difficulty = difficulty;
    }

    /**
     * Returns the string representation of the word.
     *
     * @return the word value
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the word with hidden letters, showing only the letters in the visibleLetters collection.
     *
     * @param visibleLetters the collection of letters to be shown
     * @return the word with hidden letters
     */
    public String getHiddenValue(Collection<Character> visibleLetters) {
        StringBuilder output = new StringBuilder();
        for (char letter : getValue().toCharArray()) {
            output.append(visibleLetters.contains(letter) ? letter + " " : "_ ");
        }
        return output.toString().trim();
    }

    /**
     * Returns the word with all letters hidden.
     *
     * @return the word with all letters hidden
     */
    public String getHiddenValue() {
        return getHiddenValue(new ArrayList<>());
    }

    /**
     * Checks if the word contains the specified letter.
     *
     * @param letter the letter to check
     * @return true if the word contains the letter, false otherwise
     */
    public boolean containsLetter(char letter) {
        return getValue().contains(String.valueOf(letter));
    }

    /**
     * Returns the word value in uppercase.
     *
     * @return the word value in uppercase
     */
    public String getValue() {
        return value.toUpperCase();
    }

    /**
     * Returns the raw word value.
     *
     * @return the raw word value
     */
    public String getRawValue() {
        return value;
    }

    /**
     * Returns the difficulty level of the word.
     *
     * @return the difficulty level of the word
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
}