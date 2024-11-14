import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PickSaveWindow extends JFrame implements ActionListener {

    private JFileChooser chooser;
    private JButton createSaveButton;
    private JButton openAdminButton;

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

        JPanel buttons = new JPanel();
        GridLayout buttonsLayout = new GridLayout(2, 1);
        buttonsLayout.setVgap(10);
        buttons.setLayout(buttonsLayout);

        createSaveButton = new JButton("Create save");
        createSaveButton.addActionListener(this);
        buttons.add(createSaveButton);

        openAdminButton = new JButton("Open admin panel");
        openAdminButton.addActionListener(this);
        buttons.add(openAdminButton);

        this.add(buttons);
        this.pack();
    }

    private void createGameSave() {
        String username = JOptionPane.showInputDialog(this, "Please input username");
        if (username != null) {
            try {
                GameSave.createSave(new File("saves/"+username+".hangman.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            chooser.updateUI();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooser) {
            String s = e.getActionCommand();
            if (s.equals("ApproveSelection")) {
                Hangman hangman = new Hangman(GameSave.loadSave(chooser.getSelectedFile()));
                hangman.setVisible(true);
                dispose();
            } else {
                System.exit(0);
            }
        } else if (e.getSource() == createSaveButton) {
            createGameSave();
        } else if (e.getSource() == openAdminButton) {
            AdminOptions.openWindow();
            dispose();
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
