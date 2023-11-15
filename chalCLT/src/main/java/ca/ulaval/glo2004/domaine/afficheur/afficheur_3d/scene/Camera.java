package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;
import java.awt.Dimension;
import java.awt.Point;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class Camera {
    private String id = "camera";
    private Vector3D position = new Vector3D(0, 0, -1000); // The position of the camera in the world
    private Vector3D direction = new Vector3D(0, 0, 0);
    public double scale = 1;


    public static final double _ZOOM_FAC_PER_TICK= 0.03;

    public Camera() {
    }

    public Camera(String id) {
        this.id = id;
    }

    public Camera(Vector3D position) {
        this.position = position;
        this.direction = new Vector3D(0, 0, 0);
    }

    public Camera(Vector3D position, Vector3D direction, String id) {
        this.id = id;
        this.direction = direction;
        this.position = position;
        this.direction = new Vector3D(0, 0, 0);
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
        this.id = id;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    private void zoom(Point mousePosition, Dimension viewportDimension, boolean precise, int fac) { // 1 = in, -1 = out
        double zoomFactor = 1 + (_ZOOM_FAC_PER_TICK * (precise?0.1:1)); // Define the zoom factor
        double trueFactor = (fac>0?zoomFactor:1/zoomFactor); // account for the possibility of zooming out

        // reduce clutter during testing and also now I guess
        Vector3D mousePos = new Vector3D(mousePosition.x-(double)viewportDimension.width/2,mousePosition.y-(double)viewportDimension.height/2,0);

        // solution: for trueFactor = S', added value to pos = A, this.position = pos, mousePos = mouse
        // A = (S'-1)(mouse-pos); a lot of maths went into that ok @-@

        this.position = position.add(new Vector3D(-(trueFactor-1)*(mousePos.x-position.x), -(trueFactor-1)*(mousePos.y-position.y), 0));

        this.scale = this.scale * trueFactor;
    }

    public void zoomInDirection(Point mousePosition, Dimension viewportDimension, boolean precise){
        zoom(mousePosition, viewportDimension, precise, 1);
    }

    public void zoomOutDirection(Point mousePosition, Dimension viewportDimension, boolean precise) {
        zoom(mousePosition, viewportDimension, precise, -1);
    }

    public Camera copy() {
        return new Camera(position.copy(), direction.copy(), id);
    }

    public Matrix getTransformation() {
        Vector3D cameraDirection = getDirection();
        Vector3D cameraPosition = getPosition();
        double cameraScale = getScale();

        Matrix camRotateX = Matrix.rotationXMatrix(cameraDirection.x);
        Matrix camRotateY = Matrix.rotationYMatrix(cameraDirection.y);
        Matrix camRotateZ = Matrix.rotationZMatrix(cameraDirection.z);
        Matrix cameraRotation = camRotateX.multiply(camRotateY).multiply(camRotateZ);

        Matrix camTranslate = Matrix.translationMatrix(cameraPosition.x, cameraPosition.y, cameraPosition.z);
        Matrix camScale = Matrix.scaleMatrix(cameraScale, cameraScale, cameraScale);

        return camTranslate.multiply(cameraRotation).multiply(camScale);
    }

    public void moveLeft(double distance) {
        Vector3D position = this.position;

        Vector3D movement = new Vector3D(-1, 0, 0).multiply(distance);

        this.position = position.add(movement);
    }
    
    public void moveUp(double distance) {
        Vector3D position = this.position;

        Vector3D movement = new Vector3D(0, 1, 0).multiply(distance);

        this.position = position.add(movement);
    }

    public void moveForward(double distance) {
        Vector3D position = this.position;

        Vector3D movement = new Vector3D(0, 0, 1).multiply(distance);

        this.position = position.add(movement);
    }

    public void rotateX(double angle) {
        Vector3D direction = this.direction;

        Vector3D rotation = new Vector3D(angle, 0, 0);

        this.direction = direction.add(rotation);
    }

    public void rotateY(double angle) {
        Vector3D direction = this.direction;

        Vector3D rotation = new Vector3D(0, angle, 0);

        this.direction = direction.add(rotation);
    }

    public void rotateZ(double angle) {
        Vector3D direction = this.direction;

        Vector3D rotation = new Vector3D(0, 0, angle);
        this.direction = direction.add(rotation);
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

}
