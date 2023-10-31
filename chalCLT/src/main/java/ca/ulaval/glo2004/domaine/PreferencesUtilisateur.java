package ca.ulaval.glo2004.domaine;


public class PreferencesUtilisateur {
    private boolean afficherGrille = false;
    private boolean afficherVoisinSelection = false;
    private java.awt.Color backgroundColor = java.awt.Color.BLACK;
    private java.awt.Color gridColor = java.awt.Color.GRAY;
    private int gridSpacing = 50;

    public PreferencesUtilisateur() {}

    public PreferencesUtilisateur(boolean afficherGrille, boolean afficherVoisinSelection) {
        this.afficherGrille = afficherGrille;
        this.afficherVoisinSelection = afficherVoisinSelection;
    }

    public boolean getAfficherGrille() {
        return afficherGrille;
    }

    public void setAfficherGrille(boolean afficherGrille) {
        this.afficherGrille = afficherGrille;
    }

    public boolean getAfficherVoisinSelection() {
        return afficherVoisinSelection;
    }

    public void setAfficherVoisinSelection(boolean afficherVoisinSelection) {
        this.afficherVoisinSelection = afficherVoisinSelection;
    }

    public java.awt.Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(java.awt.Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public java.awt.Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(java.awt.Color gridColor) {
        this.gridColor = gridColor;
    }

    public int getGridSpacing() {
        return gridSpacing;
    }

    public void setGridSpacing(int gridSpacing) {
        this.gridSpacing = gridSpacing;
    }
}
