package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeSupport;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class Camera {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String id = "camera";
    protected Vector3D position = new Vector3D(0, 0, -1000); // The position of the camera in the world
    protected Vector3D direction = new Vector3D(0, 0, 0);
    private double scale = 1;
    private double fov = Math.PI / 2;
    private double aspectRatio = 1;
    private double near = 0.1;
    private double far = 1000;
    private double focalLength = 1;
    public Matrix perspectiveMatrix = getPerspectiveMatrix();
    
    public static final double _ZOOM_FAC_PER_TICK = 0.03;

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
        String old = this.id;
        this.id = id;
        this.pcs.firePropertyChange("id", old, id);
    }

    public void setPosition(Vector3D position) {
        Vector3D old = this.position.copy();
        this.position = position;
        this.pcs.firePropertyChange("position", old, position);
    }

    public void setDirection(Vector3D direction) {
        Vector3D old = this.direction.copy();
        this.direction = direction;
        this.pcs.firePropertyChange("direction", old, direction);
    }

    public void setScale(double scale) {
        double old = this.scale;
        this.scale = scale;
        this.pcs.firePropertyChange("scale", old, scale);
    }

    public double getFov() {
        return fov;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public double getNear() {
        return near;
    }

    public double getFar() {
        return far;
    }

    public double getFocalLength() {
        return focalLength;
    }

    public void setFov(double fov) {
        this.fov = fov;
        // this.perspectiveMatrix = getPerspectiveMatrix();
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
        // this.perspectiveMatrix = getPerspectiveMatrix();
    }

    public void setNear(double near) {
        this.near = near;
        // this.perspectiveMatrix = getPerspectiveMatrix();
    }

    public void setFar(double far) {
        this.far = far;
        // this.perspectiveMatrix = getPerspectiveMatrix();
    }

    // The methods to move the camera in world space along its axes
    public void moveForward(double d) {
        // Move the camera forward by a given distance along its z-axis
        // by adding the negative z-axis vector multiplied by the distance
        // to the position vector
        // z-axis vector = |0|
        // |0|
        // |-1|
        Vector3D zAxis = new Vector3D(0, 0, -1);
        position = position.add(zAxis.multiply(d));
    }

    public void moveBackward(double d) {
        // Move the camera backward by a given distance along its z-axis
        // by adding the z-axis vector multiplied by the distance
        // to the position vector
        // z-axis vector = |0|
        // |0|
        // |-1|
        Vector3D zAxis = new Vector3D(0, 0, -1);
        position = position.sub(zAxis.multiply(d));
    }

    public void moveLeft(double d) {
        // Move the camera left by a given distance along its x-axis
        // by adding the negative x-axis vector multiplied by the distance
        // to the position vector
        // x-axis vector = |-1|
        // |0|
        // |0|
        Vector3D xAxis = new Vector3D(-1, 0, 0);
        position = position.add(xAxis.multiply(d));
    }

    public void moveRight(double d) {
        // Move the camera right by a given distance along its x-axis
        // by adding the x-axis vector multiplied by the distance
        // to the position vector
        // x-axis vector = |-1|
        // |0|
        // |0|
        Vector3D xAxis = new Vector3D(-1, 0, 0);
        position = position.sub(xAxis.multiply(d));
    }

    public void moveUp(double d) {
        // Move the camera up by a given distance along its y-axis
        // by adding the y-axis vector multiplied by the distance
        // to the position vector
        // y-axis vector = |0|
        // |1|
        // |0|
        Vector3D yAxis = new Vector3D(0, 1, 0);
        position = position.add(yAxis.multiply(d));
    }

    public void moveDown(double d) {
        // Move the camera down by a given distance along its y-axis
        // by adding the negative y-axis vector multiplied by the distance
        // to the position vector
        // y-axis vector = |0|
        // |1|
        // |0|
        Vector3D yAxis = new Vector3D(0, 1, 0);
        position = position.sub(yAxis.multiply(d));
    }

    // The methods to rotate the camera in world space around its axes
    public void rotateX(double a) {
        // Rotate the camera around its x-axis by a given angle in radians
        // by adding the angle to the x component of the rotation vector
        direction = direction.add(new Vector3D(a, 0, 0));
    }

    public void rotateY(double a) {
        // Rotate the camera around its y-axis by a given angle in radians
        // by adding the angle to the y component of the rotation vector
        direction = direction.add(new Vector3D(0, a, 0));
    }

    public void rotateZ(double a) {
        // Rotate the camera around its z-axis by a given angle in radians
        // by adding the angle to the z component of the rotation vector
        direction = direction.add(new Vector3D(0, 0, a));
    }

    /*
     * The method to transform a vertex from world space to camera space
     * using the position and rotation of the camera
     * 
     * Rotate the vertex by the negative rotation of the camera
     * using the rotation matrices
     * Rx = |1 0 0 |
     * |0 cos(a) -sin(a)|
     * |0 sin(a) cos(a)|
     * Ry = | cos(b) 0 sin(b)|
     * | 0 1 0 |
     * |-sin(b) 0 cos(b)|
     * Rz = |cos(c) -sin(c) 0 |
     * |sin(c) cos(c) 0 |
     * | 0 0 1 |
     * R = Rx * Ry * Rz
     * v' = R * v
     */
    public Matrix transform() {
        // Rotate the vertex by the negative rotation of the camera
        // using the rotation matrices
        // Rx = |1 0 0 |
        // |0 cos(a) -sin(a)|
        // |0 sin(a) cos(a)|
        // Ry = | cos(b) 0 sin(b)|
        // | 0 1 0 |
        // |-sin(b) 0 cos(b)|
        // Rz = |cos(c) -sin(c) 0 |
        // |sin(c) cos(c) 0 |
        // | 0 0 1 |
        // R = Rx * Ry * Rz
        // v' = R * v
        Matrix camRotateX = Matrix.rotationXMatrix(direction.x);
        Matrix camRotateY = Matrix.rotationYMatrix(direction.y);
        Matrix camRotateZ = Matrix.rotationZMatrix(direction.z);
        Matrix cameraRotation = camRotateX.multiply(camRotateY).multiply(camRotateZ);

        // Translate the vertex by the negative position of the camera
        // using the translation matrix
        // T = |1 0 0 -x|
        // |0 1 0 -y|
        // |0 0 1 -z|
        // |0 0 0 1 |
        // v'' = T * v'
        Matrix camTranslate = Matrix.translationMatrix(position.x, position.y, position.z);

        // Scale the vertex by the inverse scale of the camera
        // using the scale matrix
        // S = |1/sx 0 0 0|
        // |0 1/sy 0 0|
        // |0 0 1/sz 0|
        // |0 0 0 1 |
        // v''' = S * v''
        Matrix camScale = Matrix.scaleMatrix(getScale(), getScale(), getScale());

        // Transform the vertex from world space to camera space
        // by multiplying the vertex by the transformation matrix
        // M = T * R * S
        // v'''' = M * v
        Matrix transformation = camTranslate.multiply(cameraRotation).multiply(camScale);

        return transformation;
    }

    public Matrix getPerspectiveMatrix() {
        // Create a perspective projection matrix
        // The perspective projection matrix is used to multiply the
        // vertices of a mesh to project them onto the screen
        // The perspective projection matrix is defined as follows:
        // |cot(fov/2)/aspectRatio 0 0 0|
        // |0 cot(fov/2) 0 0|
        // |0 0 (f+n)/(f-n) -2fn/(f-n)|
        // |0 0 1 0|
        // where fov = field of view, aspectRatio = aspect ratio of the screen,
        // n = near, f = far
        double cot = 1 / Math.tan(fov / 2);
        double fmn = far - near;
        double fpn = far + near;

        return new Matrix(new double[][] {
                { cot / aspectRatio, 0, 0, 0 },
                { 0, cot, 0, 0 },
                { 0, 0, fpn / fmn, -2 * far * near / fmn },
                { 0, 0, 1, 0 }
        });
    }

    private void zoom(Point mousePosition, Dimension viewportDimension, boolean precise, int fac) { // 1 = in, -1 = out
        double zoomFactor = 1 + (_ZOOM_FAC_PER_TICK * (precise ? 0.1 : 1)); // Define the zoom factor
        double trueFactor = (fac > 0 ? zoomFactor : 1 / zoomFactor); // account for the possibility of zooming out

        // reduce clutter during testing and also now I guess
        Vector3D mousePos = new Vector3D(mousePosition.x, mousePosition.y, 0);

        // solution: for trueFactor = S', added value to pos = A, this.position = pos,
        // mousePos = mouse
        // A = (S'-1)(mouse-pos); a lot of maths went into that ok @-@

        this.setPosition(position.add(new Vector3D(-(trueFactor - 1) * (mousePos.x - position.x),
                -(trueFactor - 1) * (mousePos.y - position.y), 0)));
        this.setScale(this.scale * trueFactor);
    }

    public void zoomInDirection(Point mousePosition, Dimension viewportDimension, boolean precise) {
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

    public Matrix getInverseRotationTransformation() {
        Vector3D cameraDirection = getDirection();
        double cameraScale = getScale();

        Matrix camRotateX = Matrix.rotationXMatrix(-cameraDirection.x);
        Matrix camRotateY = Matrix.rotationYMatrix(-cameraDirection.y);
        Matrix camRotateZ = Matrix.rotationZMatrix(-cameraDirection.z);
        Matrix cameraRotation = camRotateZ.multiply(camRotateY).multiply(camRotateX);

        Matrix camScale = Matrix.scaleMatrix(1 / cameraScale, 1 / cameraScale, 1 / cameraScale);

        return camScale.multiply(cameraRotation); // .multiply(camTranslate);
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