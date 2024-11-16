import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public abstract class PickSaveWindow extends JFrame implements ActionListener {

    protected JFileChooser chooser;

    abstract void fileSelected(File file);

    abstract void operationCanceled();

    public PickSaveWindow() {
        super();
        build();
    }

    private void build() {
        setSize(500, 500);
        setLocationRelativeTo(null); //the window is centered on
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
