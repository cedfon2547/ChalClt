package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.shapes;

import java.util.ArrayList;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Material;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;

public class RectCuboid extends TriangleMesh {
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
    }

    public RectCuboid(Vector3D position, Vector3D dimension) {
        this(position, dimension, new Material());
    }
}
