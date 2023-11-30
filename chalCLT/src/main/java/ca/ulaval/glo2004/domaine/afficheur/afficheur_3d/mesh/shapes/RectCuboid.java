package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.shapes;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Material;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;

public class RectCuboid extends TriangleMesh {
    public static class Face {
        public Vector3D[] vertices = new Vector3D[4];
        public String label = null;

        public Face(Vector3D[] vertices, String label) {
            this.vertices = vertices;
            this.label = label;
        }

        public Vector3D getCenter() {
            Vector3D center = new Vector3D(0, 0, 0);
            for (Vector3D vertex : vertices) {
                center = center.add(vertex);
            }
            return center.multiply(1.0 / vertices.length);
        }

        public double getDistanceFromCenter(Vector3D point) {
            return getCenter().sub(point).length();
        }

        public List<Triangle> getTriangles() {
            List<Triangle> triangles = new ArrayList<Triangle>();
            triangles.add(new Triangle(vertices[0], vertices[1], vertices[2]));
            triangles.add(new Triangle(vertices[0], vertices[2], vertices[3]));
            return triangles;
        }

        public Vector3D getNormal() {
            Vector3D v1 = vertices[1].sub(vertices[0]);
            Vector3D v2 = vertices[2].sub(vertices[0]);
            return v1.cross(v2).normalize();
        }
    }
    
    public Face front, back, left, right, top, bottom;

    public RectCuboid(Vector3D position, Vector3D dimension, Material material) {
        super(new ArrayList<Triangle>(), material);

        Vector3D p1 = new Vector3D(position.x, position.y, position.z);
        Vector3D p2 = new Vector3D(position.x + dimension.x, position.y, position.z);
        Vector3D p3 = new Vector3D(position.x + dimension.x, position.y + dimension.y, position.z);
        Vector3D p4 = new Vector3D(position.x, position.y + dimension.y, position.z);
        Vector3D p5 = new Vector3D(position.x, position.y, position.z + dimension.z);
        Vector3D p6 = new Vector3D(position.x + dimension.x, position.y, position.z + dimension.z);
        Vector3D p7 = new Vector3D(position.x + dimension.x, position.y + dimension.y, position.z + dimension.z);
        Vector3D p8 = new Vector3D(position.x, position.y + dimension.y, position.z + dimension.z);

        // Front
        triangles.add(new Triangle(p1, p2, p3));
        triangles.add(new Triangle(p1, p3, p4));

        // Back
        triangles.add(new Triangle(p5, p7, p6));
        triangles.add(new Triangle(p5, p8, p7));

        // Left
        triangles.add(new Triangle(p1, p5, p8));
        triangles.add(new Triangle(p1, p8, p4));

        // Right
        triangles.add(new Triangle(p2, p6, p7));
        triangles.add(new Triangle(p2, p7, p3));

        // Top
        triangles.add(new Triangle(p4, p8, p7));
        triangles.add(new Triangle(p4, p7, p3));

        // Bottom
        triangles.add(new Triangle(p1, p2, p6));
        triangles.add(new Triangle(p1, p6, p5));

        front = new Face(new Vector3D[] { p1, p2, p3, p4 }, "front");
        back = new Face(new Vector3D[] { p5, p7, p6, p8 }, "back");
        left = new Face(new Vector3D[] { p1, p5, p8, p4 }, "left");
        right = new Face(new Vector3D[] { p2, p6, p7, p3 }, "right");
        top = new Face(new Vector3D[] { p4, p8, p7, p3 }, "top");
        bottom = new Face(new Vector3D[] { p1, p2, p6, p5 }, "bottom");
    }

    public RectCuboid(Vector3D position, Vector3D dimension) {
        this(position, dimension, new Material());
    }

    public Vector3D[] getVertices() {
        ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
        for (Triangle triangle : triangles) {
            for (Vector3D vertex : triangle.getVertices()) {
                if (!vertices.contains(vertex)) {
                    vertices.add(vertex);
                }
            }
        }
        return vertices.toArray(new Vector3D[0]);
    }

    // public List<Triangle> getTriangles() {
    //     List<Triangle> triangles = new ArrayList<Triangle>();
    //     triangles.addAll(front.getTriangles());
    //     triangles.addAll(back.getTriangles());
    //     triangles.addAll(left.getTriangles());
    //     triangles.addAll(right.getTriangles());
    //     triangles.addAll(top.getTriangles());
    //     triangles.addAll(bottom.getTriangles());
    //     return triangles;
    // }
}
