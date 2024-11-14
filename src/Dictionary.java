import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private final List<DictionaryWord> words;

    public Dictionary() {
        words = new ArrayList<>();
        loadWords();
    }

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

    public List<DictionaryWord> getWordsByDifficulty(Difficulty difficulty) {
        return words.stream().filter(word -> difficulty == word.getDifficulty()).toList();
    }

    public void addWord(DictionaryWord word) {
        words.add(word);
    }

    public void removeWord(DictionaryWord word) {
        words.remove(word);
    }

    public DictionaryWord pickRandomWord(Difficulty difficulty) {
        Random rand = new Random();
        List<DictionaryWord> filteredWords = words.stream().filter(word -> difficulty == word.getDifficulty()).toList();
        return filteredWords.get(rand.nextInt(filteredWords.size()));
    }
}