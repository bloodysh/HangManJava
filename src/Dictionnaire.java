import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dictionnaire {
    private List<String> easyWords;
    private List<String> mediumWords;
    private List<String> hardWords;

    public Dictionnaire() {
        easyWords = new ArrayList<>();
        mediumWords = new ArrayList<>();
        hardWords = new ArrayList<>();
        loadWords();
    }

    private void loadWords() {
        try {
            String filePath = getClass().getClassLoader().getResource("data.txt").getFile();
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                for (String word : words) {
                    categorizeWord(word);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private void categorizeWord(String word) {
        if (word.length() <= 4) {
            easyWords.add(word);
        } else if (word.length() <= 7) {
            mediumWords.add(word);
        } else {
            hardWords.add(word);
        }
    }

    public String[] loadChallenge(int difficulty) {
        Random rand = new Random();
        String word;
        switch (difficulty) {
            case 1:
                word = easyWords.get(rand.nextInt(easyWords.size()));
                break;
            case 2:
                word = mediumWords.get(rand.nextInt(mediumWords.size()));
                break;
            case 3:
                word = hardWords.get(rand.nextInt(hardWords.size()));
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficulty);
        }
        return new String[]{word.toUpperCase()};
    }
}