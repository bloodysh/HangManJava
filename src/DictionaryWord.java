import java.util.ArrayList;
import java.util.Collection;

public class DictionaryWord {
    String value;
    Difficulty difficulty;

    public DictionaryWord(String value, Difficulty difficulty) {
        this.value = value;
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getHiddenValue(Collection<Character> visibleLetters) {
        StringBuilder output = new StringBuilder();
        for (char letter: getValue().toCharArray()) {
            output.append(visibleLetters.contains(letter) ? letter + " " : "_ ");
        }
        return output.toString().trim();
    }

    public String getHiddenValue() {
        return getHiddenValue(new ArrayList<>());
    }

    public boolean containsLetter(char letter) {
        return getValue().contains(String.valueOf(letter));
    }

    public String getValue() {
        return value.toUpperCase();
    }

    public String getRawValue() {
        return value;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
