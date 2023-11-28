package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.awt.Color;

public class SceneConfiguration {
    Color backgroundColor = new Color(75, 75, 75, 255);
    Color gridColor = new Color(114, 114, 114, 255);
    Color selectionColor = new Color(0, 225, 255, 255);
    int SelectionStrokeWidth = 3;
    boolean showAxis = true;
    boolean showGridXY = true;
    boolean showGridXZ = true;
    boolean showGridYZ = true;
    int stepCounts = 24;
    double gridStep = 30;
    int gridStrokeWidth = 2;
    int axisStrokeWidth = 2;
    boolean showLight = true;
    boolean showBoundingBox = true;
    boolean showTriangles = false;
    boolean showGround = true;
    boolean highlightVertices = true;
 
    public SceneConfiguration() {
 
    }

    public int getAxisStrokeWidth() {
        return axisStrokeWidth;
    }

    public int getGridStrokeWidth() {
        return gridStrokeWidth;
    }

    public Color getGridColor() {
        return gridColor;
    }
 
    public Color getBackgroundColor() {
        return backgroundColor;
    }
 
    public boolean getShowAxis() {
        return showAxis;
    }
 
    public boolean getShowGridXY() {
        return showGridXY;
    }
 
    public boolean getShowGridXZ() {
        return showGridXZ;
    }
 
    public boolean getShowGridYZ() {
        return showGridYZ;
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
 
    public int getSelectionStrokeWidth() {
        return SelectionStrokeWidth;
    }
 
    public int getStepCounts() {
        return stepCounts;
    }
 
    public double getGridStep() {
        return gridStep;
    }

    public void setAxisStrokeWidth(int axisStrokeWidth) {
        this.axisStrokeWidth = axisStrokeWidth;
    }

    public void setGridStrokeWidth(int gridStrokeWidth) {
        this.gridStrokeWidth = gridStrokeWidth;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }
 
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
 
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
    }
 
    public void setShowGridXY(boolean showGrid) {
        this.showGridXY = showGrid;
    }
 
    public void setShowGridXZ(boolean showGrid) {
        this.showGridXZ = showGrid;
    }
 
    public void setShowGridYZ(boolean showGrid) {
        this.showGridYZ = showGrid;
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
 
    public void setSelectionStrokeWidth(int selectionStrokeWidth) {
        SelectionStrokeWidth = selectionStrokeWidth;
    }
 
    public void setStepCounts(int axisLength) {
        this.stepCounts = axisLength;
    }
 
    public void setGridStep(double gridStep) {
        this.gridStep = Math.max(gridStep,0.1);
    }
}
