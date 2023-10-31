package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import ca.ulaval.glo2004.gui.MainWindow;

public class DrawingPanel extends javax.swing.JPanel {
    MainWindow mainWindow;
    Afficheur afficheur;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.afficheur = new Afficheur(this);

        initComponents();
    }

    private void initComponents() {
        setBackground(java.awt.Color.BLACK);
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        this.afficheur.drawGrid(g);
        // g.drawImage(this.afficheur.getImage(), 0, 0, null);
    }
}
