package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeSupport;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class Camera {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String id = "camera";
    private Vector3D position = new Vector3D(0, 0, -1000); // The position of the camera in the world
    private Vector3D direction = new Vector3D(0, 0, 0);
    private double scale = 1;


    public static final double _ZOOM_FAC_PER_TICK= 0.03;

    public Camera() {
    }

    public Camera(String id) {
        this(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 1, id);
    }

    public Camera(Vector3D position) {
        this(position, new Vector3D(0, 0, 0), 1, "camera");
    }

    public Camera(Vector3D position, Vector3D direction) {

    }

    public Camera(Vector3D position, Vector3D direction, double scale, String id) {
        this.setId(id);
        this.setPosition(position);
        this.setDirection(direction);
        this.setScale(scale);

    }

    public String getId() {
        return id;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public double getScale() {
        return scale;
    }

    public void setId(String id) {
        this.pcs.firePropertyChange("id", this.id, id);
        this.id = id;
    }

    public void setPosition(Vector3D position) {
        this.pcs.firePropertyChange("position", this.position, position);
        this.position = position;
    }

    public void setDirection(Vector3D direction) {
        this.pcs.firePropertyChange("direction", this.direction, direction);
        this.direction = direction;
    }

    public void setScale(double scale) {
        this.pcs.firePropertyChange("scale", this.scale, scale);
        this.scale = scale;
    }

    private void zoom(Point mousePosition, Dimension viewportDimension, boolean precise, int fac) { // 1 = in, -1 = out
        double zoomFactor = 1 + (_ZOOM_FAC_PER_TICK * (precise?0.1:1)); // Define the zoom factor
        double trueFactor = (fac>0?zoomFactor:1/zoomFactor); // account for the possibility of zooming out

        // reduce clutter during testing and also now I guess
        Vector3D mousePos = new Vector3D(mousePosition.x,mousePosition.y,0);

        // solution: for trueFactor = S', added value to pos = A, this.position = pos, mousePos = mouse
        // A = (S'-1)(mouse-pos); a lot of maths went into that ok @-@

        this.setPosition(position.add(new Vector3D(-(trueFactor-1)*(mousePos.x-position.x), -(trueFactor-1)*(mousePos.y-position.y), 0)));
        this.setScale(this.scale * trueFactor);
    }

    public void zoomInDirection(Point mousePosition, Dimension viewportDimension, boolean precise){
        zoom(mousePosition, viewportDimension, precise, 1);
    }

    public void zoomOutDirection(Point mousePosition, Dimension viewportDimension, boolean precise) {
        zoom(mousePosition, viewportDimension, precise, -1);
    }

    public Camera copy() {
        return new Camera(position.copy(), direction.copy(), scale, id);
    }

    public Matrix getTransformation() {
        Matrix camRotateX = Matrix.rotationXMatrix(direction.x);
        Matrix camRotateY = Matrix.rotationYMatrix(direction.y);
        Matrix camRotateZ = Matrix.rotationZMatrix(direction.z);
        Matrix cameraRotation = camRotateX.multiply(camRotateY).multiply(camRotateZ);

        Matrix camTranslate = Matrix.translationMatrix(position.x, position.y, position.z);
        Matrix camScale = Matrix.scaleMatrix(scale, scale, scale);

        return camTranslate.multiply(camScale).multiply(cameraRotation);
    }

    public Vector3D getUpVector() {
        Vector3D tempUp = new Vector3D(0, 1, 0);
        Vector3D tempLeft = tempUp.cross(direction);
        return tempLeft.normalize();
    } 

    public Vector3D getLeftVector() {
        Vector3D tempUp = new Vector3D(0, 1, 0);
        Vector3D tempLeft = tempUp.cross(direction);
        Vector3D tempUp2 = direction.cross(tempLeft);
        return tempUp2.normalize();
    }

    public Matrix getInverseRotationTransformation() {
        Vector3D cameraDirection = getDirection();
        double cameraScale = getScale();

        Matrix camRotateX = Matrix.rotationXMatrix(-cameraDirection.x);
        Matrix camRotateY = Matrix.rotationYMatrix(-cameraDirection.y);
        Matrix camRotateZ = Matrix.rotationZMatrix(-cameraDirection.z);
        Matrix cameraRotation = camRotateZ.multiply(camRotateY).multiply(camRotateX);

        Matrix camScale = Matrix.scaleMatrix(1/cameraScale, 1/cameraScale, 1/cameraScale);

        return camScale.multiply(cameraRotation); //.multiply(camTranslate);
    }

    public Matrix lookAtMatrix(Vector3D target) {
        Vector3D direction = target.sub(this.position).normalize();
        Vector3D up = new Vector3D(0, 1, 0);
        Vector3D right = direction.cross(up).normalize();
        up = right.cross(direction).normalize();
        Matrix matrix = new Matrix(new double[][] {
            {right.getX(), up.getX(), -direction.getX(), 0},
            {right.getY(), up.getY(), -direction.getY(), 0},
            {right.getZ(), up.getZ(), -direction.getZ(), 0},
            {0, 0, 0, 1}
        });
        // this.direction = direction;
        // this.position = target.sub(new Vector3D(0, 0, -this.position.z).multiplyMatrix(matrix));
        return matrix;
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, java.beans.PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, java.beans.PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
}
