import javax.swing.*;
import java.io.File;

/**
 * Window to pick a file which will be used to open a new Hangman window.
 *
 * @see GameSave
 * @see Hangman
 */
public class OpenSaveWindow extends PickSaveWindow {
    /**
     * A runnable run after the file has been selected, validated and the game window is opened.
     */
    private Runnable fileOpenedCallback;
    /**
     * A runnable run after clicking the cancel button or closing the window.
     */
    private Runnable operationCanceledCallback;

    @Override
    void fileSelected(File file) {
        if (file.isFile()) {
            Hangman.openWindow(GameSave.loadSave(file));
        }
        fileOpenedCallback.run();
    }

    @Override
    void operationCanceled() {
        operationCanceledCallback.run();
    }

    /**
     * Helper to open the window instantly if you do not need the class instance.
     */
    public static void openWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OpenSaveWindow window = new OpenSaveWindow(() -> {}, () -> System.exit(0));
                window.setVisible(true);
            }
        });
    }

    public OpenSaveWindow(Runnable fileOpened) {
        this(fileOpened, () -> {});
    }

    public OpenSaveWindow(Runnable fileOpened, Runnable operationCanceled) {
        super();
        this.fileOpenedCallback = fileOpened;
        this.operationCanceledCallback = operationCanceled;
        setTitle("Open save");
    }
}
