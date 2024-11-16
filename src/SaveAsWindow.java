import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Window to choose a File to save the game state.
 *
 * @see GameSave
 */
public class SaveAsWindow extends PickSaveWindow {
    /**
     * The game save that will be mutated (by setFile) when the file is selected by user.
     */
    private GameSave gameSave;

    @Override
    void fileSelected(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameSave.setFile(file);
        gameSave.saveFile();
    }

    @Override
    void operationCanceled() {
        // no-op
    }

    /**
     * Construct the window, it will not be made visible.
     * @param gameSave The game save that will be mutated (by setFile) when the file is selected by user.
     */
    public SaveAsWindow(GameSave gameSave) {
        super();
        this.gameSave = gameSave;
        setTitle("Save as");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chooser.setApproveButtonText("Save");
        chooser.setSelectedFile(gameSave.getFile());
    }

    /**
     * Helper to open the window instantly if you do not need the class instance.
     * @param gameSave The game save that will be mutated (by setFile) when the file is selected by user.
     */
    public static void openWindow(GameSave gameSave) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SaveAsWindow saveAsWindow = new SaveAsWindow(gameSave);
                saveAsWindow.setVisible(true);
            }
        });
    }
}
