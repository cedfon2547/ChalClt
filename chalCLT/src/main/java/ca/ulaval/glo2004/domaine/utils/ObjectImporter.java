package ca.ulaval.glo2004.domaine.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;

public class ObjectImporter {
    static public class Face {
        List<Vector3D> vertices = new ArrayList<Vector3D>();

        public Face(List<Vector3D> vertices) {
            this.vertices = vertices;
        }

        public Face(Face face) {
            this.vertices = face.vertices;
        }

        public Vector3D[] getVertices() {
            return vertices.toArray(new Vector3D[vertices.size()]);
        }

        public static List<Triangle> triangulation(List<Vector3D> vertices) {
            List<Triangle> triangles = new ArrayList<Triangle>();

            for (int i = 1; i < vertices.size() - 1; i++) {
                triangles.add(new Triangle(vertices.get(0), vertices.get(i), vertices.get(i + 1)));
            }

            return triangles;
        }
    }

    public static TriangleMesh importObject(String pathname) {
        List<Vector3D> vertices = new ArrayList<Vector3D>();
        List<Face> faces = new ArrayList<Face>();

        try {
            File file = new File(pathname);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("v ")) {
                    String[] lineSplit = line.split(" ");
                    vertices.add(new Vector3D(Double.parseDouble(lineSplit[1]), Double.parseDouble(lineSplit[2]),
                            Double.parseDouble(lineSplit[3])));
                } else if (line.startsWith("f ")) {
                    String[] lineSplit = line.split(" ");
                    List<Vector3D> faceVertices = new ArrayList<Vector3D>();

                    for (int i = 1; i < lineSplit.length; i++) {
                        String[] vertexSplit = lineSplit[i].split("/");
                        faceVertices.add(vertices.get(Integer.parseInt(vertexSplit[0]) - 1));
                    }

                    faces.add(new Face(faceVertices));
                }
            }

            scanner.close();
        } catch (Exception err) {
            System.out.println(err);
        }

        List<Triangle> triangles = new ArrayList<Triangle>();

        for (Face face : faces) {
            triangles.addAll(Face.triangulation(face.vertices));
        }

        return new TriangleMesh(triangles);
    }
}
