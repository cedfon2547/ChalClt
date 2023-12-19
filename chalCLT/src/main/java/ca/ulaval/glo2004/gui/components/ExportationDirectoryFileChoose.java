package ca.ulaval.glo2004.gui.components;

// import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.gui.MainWindow;

import java.io.File;

import javax.swing.*;


public class ExportationDirectoryFileChoose extends javax.swing.JFrame {
    private JFileChooser fileChooser;

    private ExportationDirectoryFileChooseListener listener;

    private MainWindow mainWindow;

    public ExportationDirectoryFileChoose(MainWindow mainWindow, ExportationDirectoryFileChooseListener listener) {
        this.mainWindow = mainWindow;
        this.listener = listener;
        initComponents();
    }

    private void initComponents() {
        fileChooser = new JFileChooser();

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fileChooser.setApproveButtonText("Sélectionner");
        fileChooser.setApproveButtonToolTipText("sélectionner");
        fileChooser.setDialogTitle("Sélectionner le dossier de destination");
        fileChooser.setToolTipText("Sélectionner le dossier de destination");
        fileChooser.setName("ExportDestionationFileChooser"); // NOI18N
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setOpaque(true);
        fileChooser.setVisible(true);
        fileChooser.setDialogTitle("Sélectionner le dossier de destination");


        fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));

        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (evt.getActionCommand().equals("ApproveSelection")) {
                    System.out.println("ApproveSelection " + fileChooser.getSelectedFile().getAbsolutePath());
                    boolean isValid = listener.onSelect(fileChooser.getSelectedFile().getAbsolutePath());

                    // if the listener return false, the path is not valid and we should display the
                    // error and let the user choose a different one.
                    if (isValid) {
                        fileChooser.setVisible(false);
                        dispose();
                    } else {

                    }

                } else if (evt.getActionCommand().equals("CancelSelection")) {
                    fileChooser.setVisible(false);
                    dispose();
                }
            }
        });

        getContentPane().add(fileChooser, java.awt.BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    @FunctionalInterface
    public static interface ExportationDirectoryFileChooseListener {
        public boolean onSelect(String path);
    }
}
