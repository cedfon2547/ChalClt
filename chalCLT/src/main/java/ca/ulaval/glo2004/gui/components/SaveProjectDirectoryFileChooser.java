package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.gui.MainWindow;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;



public class SaveProjectDirectoryFileChooser extends javax.swing.JFrame {
    private JFileChooser fileChooser;
    private OpenSaveDirectoryFileChooseListener listener;
    private MainWindow mainWindow;

    public SaveProjectDirectoryFileChooser(MainWindow mainWindow, OpenSaveDirectoryFileChooseListener listener) {
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
        fileChooser.setDialogTitle("Sélectionner le fichier à ouvrir");
        fileChooser.setToolTipText("Sélectionner le fichier à ouvrir");
        fileChooser.setName("OpenSaveFileChooser"); // NOI18N
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setOpaque(true);
        fileChooser.setVisible(true);
        fileChooser.setDialogTitle("Sélectionner le fichier à ouvrir");

        // Will use the current working directory + chalet name
        File defaultSelectedFile = new File(System.getProperty("user.dir") + String.format("\\%s.chalclt", mainWindow.getControleur().getChalet().nom));
        fileChooser.setSelectedFile(defaultSelectedFile);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Sauvegarde ChalCLT", "chalclt");
        fileChooser.setFileFilter(filter);

        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // System.out.println(evt.getActionCommand());
                if (evt.getActionCommand().equals("ApproveSelection")) {
                    // System.out.println("ApproveSelection");
                    boolean isValid = listener.onSelect(fileChooser.getSelectedFile().getAbsolutePath());

                    // if the listener return false, the path is not valid and we should display the
                    // error and let the user choose a different one.
                    if (isValid) {
                        fileChooser.setVisible(false);
                        dispose();
                    } else {

                    }

                } else if (evt.getActionCommand().equals("CancelSelection")) {
                    // System.out.println("CancelSelection");
                    fileChooser.setVisible(false);
                    dispose();
                }
            }
        });

        fileChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // System.out.println("Property Change" + evt.getPropertyName() + " " +
                // evt.getNewValue());
            }
        });

        getContentPane().add(fileChooser, java.awt.BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    @FunctionalInterface
    public static interface OpenSaveDirectoryFileChooseListener {
        public boolean onSelect(String path);
    }
}
