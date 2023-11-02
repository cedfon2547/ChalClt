package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.awt.Color;

public class SceneConfiguration {
    Color backgroundColor = Color.DARK_GRAY;
    Color selectionColor = Color.CYAN;
    boolean showAxis = true;
    boolean showGrid = false; // TODO: Implement
   

    public SceneConfiguration() {

    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public boolean getShowAxis() {
        return showAxis;
    }

    public boolean getShowGrid() {
        return showGrid;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }
}
