package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.gui.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


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
        Chalet.ChaletDTO chaletDTO = mainWindow.getControleur().getChalet();

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

        //fileChooser.setSelectedFile(new File(chaletDTO.nom));
        //fileChooser.setFileFilter(new FileNameExtensionFilter("Sauvegardes ChalCLT", "chalclt"));

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
    public static interface ExportationDirectoryFileChooseListener {
        public boolean onSelect(String path);
    }
}
