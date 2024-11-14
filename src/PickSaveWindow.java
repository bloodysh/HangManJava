import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PickSaveWindow extends JFrame implements ActionListener {

    private JFileChooser chooser;
    private JButton createSaveButton;

    public PickSaveWindow() {
        super();
        build();
    }

    private void build() {
        setTitle("Open save");
        setSize(500, 500);
        setLocationRelativeTo(null); //the window is centered on
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

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

        createSaveButton = new JButton("Create save");
        createSaveButton.addActionListener(this);
        this.add(createSaveButton);

        this.pack();
    }

    private void createGameSave() {
        Program.newGame();
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooser) {
            String s = e.getActionCommand();
            if (s.equals("ApproveSelection")) {
                Hangman.openWindow(GameSave.loadSave(chooser.getSelectedFile()));
                dispose();
            } else {
                System.exit(0);
            }
        } else if (e.getSource() == createSaveButton) {
            createGameSave();
        }
    }

    public static void openWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PickSaveWindow window = new PickSaveWindow();
                window.setVisible(true);
            }
        });
    }
}
