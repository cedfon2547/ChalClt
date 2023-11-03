package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.awt.Color;

public class SceneConfiguration {
    Color backgroundColor = Color.DARK_GRAY;
    Color selectionColor = Color.CYAN;
    boolean showAxis = true;
    boolean showGrid = false; // TODO: Implement
    boolean showLight = true;
    boolean showBoundingBox = true;
    boolean showTriangles = false;
    boolean showGround = true;
    boolean highlightVertices = true;

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

    public boolean getShowLight() {
        return showLight;
    }

    public boolean getShowBoundingBox() {
        return showBoundingBox;
    }

    public boolean getShowTriangles() {
        return showTriangles;
    }

    public boolean getShowGround() {
        return showGround;
    }

    public boolean getHighlightVertices() {
        return highlightVertices;
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

    public void setShowLight(boolean showLight) {
        this.showLight = showLight;
    }

    public void setShowBoundingBox(boolean showBoundingBox) {
        this.showBoundingBox = showBoundingBox;
    }

    public void setShowTriangles(boolean showTriangles) {
        this.showTriangles = showTriangles;
    }

    public void setShowGround(boolean showGround) {
        this.showGround = showGround;
    }

    public void setHighlightVertices(boolean highlightVertices) {
        this.highlightVertices = highlightVertices;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }
}
