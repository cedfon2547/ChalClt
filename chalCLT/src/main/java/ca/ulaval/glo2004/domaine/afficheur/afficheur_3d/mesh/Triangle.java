package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class Triangle {
    private Vector3D vertex1, vertex2, vertex3; // The vertices of the triangle
    
    public Triangle(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    public Triangle(Triangle triangle) {
        this.vertex1 = triangle.vertex1;
        this.vertex2 = triangle.vertex2;
        this.vertex3 = triangle.vertex3;
    }

    public Vector3D getVertice(int index) {
        switch (index) {
            case 0:
                return vertex1;
            case 1:
                return vertex2;
            case 2:
                return vertex3;
            default:
                throw new IllegalArgumentException("index must be between 0 and 2");
        }
    }

    public Vector3D[] getVertices() {
        return new Vector3D[] { this.vertex1, this.vertex2, this.vertex3 };
    }

    public double getArea() {
        return getArea(this);
    }

    public Vector3D getCenter() {
        return getCenter(this);
    }

    public Vector3D getNormal() {
        return getNormal(this);
    }

    public Triangle transform(Matrix transformMatrix) {
        return transform(this, transformMatrix);
    }

    public Triangle translate(Vector3D translation) {
        Matrix translationMatrix = Matrix.translationMatrix(translation.x, translation.y, translation.z);
        return transform(translationMatrix);
    }

    public Triangle scale(Vector3D scale) {
        Matrix scaleMatrix = Matrix.scaleMatrix(scale.x, scale.y, scale.z);
        return transform(scaleMatrix);
    }

    public Triangle rotateX(double angle) {
        Matrix rotationMatrix = Matrix.rotationXMatrix(angle);
        return transform(rotationMatrix);
    }

    public Triangle rotateY(double angle) {
        Matrix rotationMatrix = Matrix.rotationYMatrix(angle);
        return transform(rotationMatrix);
    }

    public Triangle rotateZ(double angle) {
        Matrix rotationMatrix = Matrix.rotationZMatrix(angle);
        return transform(rotationMatrix);
    }

    public Triangle copy() {
        return new Triangle(vertex1.copy(), vertex2.copy(), vertex3.copy());
    }

    public String toString() {
        return String.format("Triangle(%s, %s, %s)", vertex1, vertex2, vertex3);
    }

    /* ================= Static Methods ================= */
    public static Vector3D getNormal(Triangle triangle) {
        Vector3D v1v2 = triangle.vertex2.sub(triangle.vertex1);
        Vector3D v1v3 = triangle.vertex3.sub(triangle.vertex1);
        return v1v2.crossProduct(v1v3).normalize();
    }

    public static double getArea(Triangle triangle) {
        Vector3D vertex1 = triangle.vertex1;
        Vector3D vertex2 = triangle.vertex2;
        Vector3D vertex3 = triangle.vertex3;

        return (vertex1.y - vertex3.y) * (vertex2.x - vertex3.x)
        + (vertex2.y - vertex3.y) * (vertex3.x - vertex1.x);
    }

    public static Vector3D getCenter(Triangle triangle) {
        return new Vector3D(
                (triangle.vertex1.x + triangle.vertex2.x + triangle.vertex3.x) / 3,
                (triangle.vertex1.y + triangle.vertex2.y + triangle.vertex3.y) / 3,
                (triangle.vertex1.z + triangle.vertex2.z + triangle.vertex3.z) / 3);
    }

    public static double getDistance(Triangle triangle, Vector3D point) {
        Vector3D normal = getNormal(triangle);
        return Math.abs(Vector3D.dotProduct(normal, point.sub(triangle.vertex1)));
    }

    public static Triangle transform(Triangle triangle, Matrix transformMatrix) {
        return new Triangle(
                triangle.vertex1.multiplyMatrix(transformMatrix),
                triangle.vertex2.multiplyMatrix(transformMatrix),
                triangle.vertex3.multiplyMatrix(transformMatrix));
    }

    public static Triangle[] subdivide(Triangle triangle) {
        Vector3D a = triangle.vertex1;
        Vector3D b = triangle.vertex2;
        Vector3D c = triangle.vertex3;

        Vector3D ab = new Vector3D((a.x + b.x) / 2, (a.y + b.y) / 2, (a.z + b.z) / 2);
        Vector3D bc = new Vector3D((b.x + c.x) / 2, (b.y + c.y) / 2, (b.z + c.z) / 2);
        Vector3D ca = new Vector3D((c.x + a.x) / 2, (c.y + a.y) / 2, (c.z + a.z) / 2);

        return new Triangle[] {
                new Triangle(a, ab, ca),
                new Triangle(ab, b, bc),
                new Triangle(ca, bc, c),
                new Triangle(ab, bc, ca),
        };
    }

    public static Triangle[] subdivide(Triangle triangle, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }

        Triangle[] triangles = new Triangle[] { triangle };
        for (int i = 0; i < count; i++) {
            List<Triangle> newTriangles = new ArrayList<>();
            for (Triangle t : triangles) {
                for (Triangle t2 : subdivide(t)) {
                    newTriangles.add(t2);
                }
            }
            triangles = newTriangles.toArray(new Triangle[0]);
        }

        return triangles;
    }
}
