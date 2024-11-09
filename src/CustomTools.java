import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

public class CustomTools {
    //creating jlabel with custom image

    public static JLabel loadImage(String resourcePath){
        BufferedImage image;
        try{
            InputStream inputStream = CustomTools.class.getResourceAsStream(resourcePath);
            image = ImageIO.read(inputStream);
            return new JLabel(new ImageIcon(image));
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
        return null;
    }

    public static String hideWord(String word){
        StringBuilder hiddenWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++){
            hiddenWord.append("_ ");
        }
        return hiddenWord.toString();
    }

    public static void updateImage(JLabel label, String resourcePath) {
        BufferedImage image;
        try {
            InputStream inputStream = CustomTools.class.getResourceAsStream(resourcePath);
            image = ImageIO.read(inputStream);
            label.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void saveGame(String username, String[] gameWord, JLabel hiddenWordLabel, int incorrectGuesses, JButton[] letterButtons) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_game.dat"))) {
            oos.writeObject(username);
            oos.writeObject(gameWord);
            oos.writeObject(hiddenWordLabel.getText());
            oos.writeInt(incorrectGuesses);
            oos.writeObject(Arrays.stream(letterButtons).map(JButton::isEnabled).toArray(Boolean[]::new));
            oos.writeObject(Arrays.stream(letterButtons).map(button -> button.getBackground().getRGB()).toArray(Integer[]::new));

            JOptionPane.showMessageDialog(null, "Game saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save the game.");
        }
    }

    public static Object[] loadGame(JLabel hiddenWordLabel, JButton[] letterButtons) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved_game.dat"))) {
            String username = (String) ois.readObject();
            if (username == null || username.isEmpty()) {
                username = JOptionPane.showInputDialog(null, "Enter your username:");
                if (username == null || username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username is required to load the game.");
                    return null;
                }
                saveUsername(username);
            }
            String[] gameWord = (String[]) ois.readObject();
            hiddenWordLabel.setText((String) ois.readObject());
            int incorrectGuesses = ois.readInt();
            Boolean[] buttonStates = (Boolean[]) ois.readObject();
            Integer[] buttonColors = (Integer[]) ois.readObject();

            for (int i = 0; i < letterButtons.length; i++) {
                letterButtons[i].setEnabled(buttonStates[i]);
                letterButtons[i].setBackground(new Color(buttonColors[i]));
            }

            JOptionPane.showMessageDialog(null, "Game loaded successfully!");
            return new Object[]{username, gameWord, incorrectGuesses};
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load the game.");
            return null;
        }
    }

    public static void saveUsername(String username) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("username.dat"))) {
            oos.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save the username.");
        }
    }

    public static String loadUsername() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("username.dat"))) {
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void askForUsername() {
        String username = loadUsername();
        if (username == null || username.isEmpty()) {
            username = JOptionPane.showInputDialog(null, "Enter your username:");
            if (username == null || username.isEmpty()) {
                System.exit(0);
            } else {
                saveUsername(username);
            }
        }
    }
}
