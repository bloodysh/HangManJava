import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * The PickSaveWindow class is an abstract class that provides a window for selecting a save file.
 * It uses a JFileChooser to allow the user to pick a file and handles the file selection and cancellation events.
 */
public abstract class PickSaveWindow extends JFrame implements ActionListener {

    protected JFileChooser chooser;

    /**
     * This method is called when a file is selected.
     *
     * @param file the selected file
     */
    abstract void fileSelected(File file);

    /**
     * This method is called when the operation is canceled.
     */
    abstract void operationCanceled();

    /**
     * Constructs a PickSaveWindow object and builds the window.
     */
    public PickSaveWindow() {
        super();
        build();
    }

    /**
     * Builds the window by setting its properties and adding the file chooser.
     */
    private void build() {
        setSize(500, 500);
        setLocationRelativeTo(null); // the window is centered on the screen
        setResizable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new FlowLayout());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                operationCanceled();
            }
        });

        chooser = new JFileChooser();
        chooser.addActionListener(this);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return (f.getName().endsWith(".hangman.txt"));
            }

            @Override
            public String getDescription() {
                return "Hangman save file";
            }
        });
        chooser.setCurrentDirectory(new File("saves/"));
        this.add(chooser);

        this.pack();
    }

    /**
     * Handles the action performed event for the file chooser.
     *
     * @param e the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooser) {
            String s = e.getActionCommand();
            if (s.equals("ApproveSelection")) {
                fileSelected(chooser.getSelectedFile());
            } else {
                operationCanceled();
            }
            dispose();
        }
    }
}