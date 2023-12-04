package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SceneConfiguration {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    Color backgroundColor = new Color(75, 75, 75, 255);
    Color gridColor = new Color(114, 114, 114, 255);
    Color selectionColor = new Color(0, 220, 220, 255);
    int SelectionStrokeWidth = 3;
    boolean showAxis = true;
    boolean showGridXY = true;
    boolean showGridXZ = true;
    boolean showGridYZ = true;
    int stepCounts = 24;
    double gridStep = 30;
    int gridStrokeWidth = 2;
    int axisStrokeWidth = 2;
 
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
        this.pcs.firePropertyChange("axisStrokeWidth", this.axisStrokeWidth, axisStrokeWidth);
        this.axisStrokeWidth = axisStrokeWidth;
    }

    public void setGridStrokeWidth(int gridStrokeWidth) {
        this.pcs.firePropertyChange("gridStrokeWidth", this.gridStrokeWidth, gridStrokeWidth);
        this.gridStrokeWidth = gridStrokeWidth;
    }

    public void setGridColor(Color gridColor) {
        this.pcs.firePropertyChange("gridColor", this.gridColor, gridColor);
        this.gridColor = gridColor;
    }
 
    public void setBackgroundColor(Color backgroundColor) {
        this.pcs.firePropertyChange("backgroundColor", this.backgroundColor, backgroundColor);
        this.backgroundColor = backgroundColor;
    }
 
    public void setShowAxis(boolean showAxis) {
        this.pcs.firePropertyChange("showAxis", this.showAxis, showAxis);
        this.showAxis = showAxis;
    }
 
    public void setShowGridXY(boolean showGrid) {
        this.pcs.firePropertyChange("showGridXY", this.showGridXY, showGrid);
        this.showGridXY = showGrid;
    }
 
    public void setShowGridXZ(boolean showGrid) {
        this.pcs.firePropertyChange("showGridXZ", this.showGridXZ, showGrid);
        this.showGridXZ = showGrid;
    }
 
    public void setShowGridYZ(boolean showGrid) {
        this.pcs.firePropertyChange("showGridYZ", this.showGridYZ, showGrid);
        this.showGridYZ = showGrid;
    }
 
    public void setSelectionColor(Color selectionColor) {
        this.pcs.firePropertyChange("selectionColor", this.selectionColor, selectionColor);
        this.selectionColor = selectionColor;
    }
 
    public void setSelectionStrokeWidth(int selectionStrokeWidth) {
        this.pcs.firePropertyChange("selectionStrokeWidth", this.SelectionStrokeWidth, selectionStrokeWidth);
        SelectionStrokeWidth = selectionStrokeWidth;
    }
 
    public void setStepCounts(int axisLength) {
        this.pcs.firePropertyChange("stepCounts", this.stepCounts, axisLength);
        this.stepCounts = axisLength;
    }
 
    public void setGridStep(double gridStep) {
        this.pcs.firePropertyChange("gridStep", this.gridStep, gridStep);
        this.gridStep = Math.max(gridStep,0.1);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
