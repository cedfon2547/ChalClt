package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.gui.components.DrawingPanel;

public class Afficheur {
    private BufferedImage bufImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
    private DrawingPanel drawingPanel;

    public Afficheur(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public Image getImage() {
        return bufImage.getScaledInstance(drawingPanel.getWidth(), drawingPanel.getHeight(), Image.SCALE_SMOOTH);
    }

    public void drawGrid(Graphics g) {
        Dimension panelDimension = this.drawingPanel.getSize();
        PreferencesUtilisateur preferencesUtilisateur = this.drawingPanel.getMainWindow().getControleur().getPreferencesUtilisateur();
        int gridSpacing = preferencesUtilisateur.getGridSpacing();
        Color gridColor = preferencesUtilisateur.getGridColor();
        Color backgroundColor = preferencesUtilisateur.getBackgroundColor();

        g.setColor(backgroundColor);
        g.fillRect(0, 0, (int) panelDimension.getWidth(), (int) panelDimension.getHeight());
        
        g.setColor(gridColor);
        double gridSpacingHeightNormalized = gridSpacing / panelDimension.getHeight();
        double gridSpacingWidthNormalized = gridSpacing / panelDimension.getWidth();

        for (double x = 0; x <= 1; x += gridSpacingWidthNormalized) {
            int x1 = (int) (x * panelDimension.getWidth());
            g.drawLine(x1, 0, x1, (int) panelDimension.getHeight());
        }

        for (double y = 0; y <= 1; y += gridSpacingHeightNormalized) {
            int y1 = (int) (y * panelDimension.getHeight());
            g.drawLine(0, y1, (int) panelDimension.getWidth(), y1);
        }
    }

    public void drawMur(Graphics g) {
        
        throw new UnsupportedOperationException();
    }

    public void drawToit(Graphics g) {
        throw new UnsupportedOperationException();
    }
}
