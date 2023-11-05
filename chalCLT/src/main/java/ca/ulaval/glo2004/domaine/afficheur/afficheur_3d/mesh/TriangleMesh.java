package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class TriangleMesh {
    public String ID = UUID.randomUUID().toString();

    private String componentHandle = ""; // some indicator of what this mesh represents // sorta dupicate of ID
    protected List<Triangle> triangles;
    private Material material = new Material(); // The material of the object
    private boolean isSelected = false;

    public TriangleMesh(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public TriangleMesh(TriangleMesh mesh) {
        this.triangles = mesh.triangles;
    }

    public TriangleMesh(TriangleMesh[] meshes) {
        this.triangles = new ArrayList<Triangle>();

        for (TriangleMesh mesh : meshes) {
            this.triangles.addAll(mesh.triangles);
        }
    }

    public TriangleMesh(List<Triangle> triangles, Material material) {
        this.triangles = triangles;
        this.material = material;
    }

    public TriangleMesh(List<Triangle> triangles, Material material, String ID, String handle) {
        this.triangles = triangles;
        this.material = material;
        this.ID = ID;
        this.componentHandle = handle;
    }

    public String getID() {
        return ID;
    }

    // feel free to fix, this is a bodge
    public String getHandle() {
        return componentHandle;
    }

    public void setHandle(String handle) {
        componentHandle=handle;
    }

    public List<Triangle> getTriangles() {
        return this.triangles;
    }

    public Material getMaterial() {
        return material;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /*
     * Returns the bounding box of the mesh
     * The return valus is a 2D array of Vector3D, where the first element is the
     * minimum point and the second element is the maximum point
     */
    public Vector3D[] getBounding() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Triangle triangle : triangles) {
            Vector3D[] vertices = triangle.getVertices();

            for (Vector3D vertex : vertices) {
                if (vertex.x < minX) {
                    minX = vertex.x;
                }

                if (vertex.x > maxX) {
                    maxX = vertex.x;
                }

                if (vertex.y < minY) {
                    minY = vertex.y;
                }

                if (vertex.y > maxY) {
                    maxY = vertex.y;
                }

                if (vertex.z < minZ) {
                    minZ = vertex.z;
                }

                if (vertex.z > maxZ) {
                    maxZ = vertex.z;
                }
            }
        }

        return new Vector3D[] { new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ) };
    }

    public Vector3D getCenter() {
        Vector3D[] bounding = this.getBounding();
        return new Vector3D((bounding[0].getX() + bounding[1].getX()) / 2, (bounding[0].getY() + bounding[1].getY()) / 2, (bounding[0].getZ() + bounding[1].getZ()) / 2);
    }

    public TriangleMesh copy() {
        ArrayList<Triangle> trianglesCopy = new ArrayList<Triangle>();

        for (Triangle triangle : this.triangles) {
            trianglesCopy.add(triangle.copy());
        }

        TriangleMesh newMesh = new TriangleMesh(trianglesCopy, this.material.copy());
        newMesh.material = this.material;

        return newMesh;
    }

    public TriangleMesh translate(Vector3D translation) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();
        
        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.translate(translation));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh scale(Vector3D scale) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();
        
        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.scale(scale));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh rotateX(double angle) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();

        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.rotateX(angle));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh rotateXOnPlace(double angle) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();
        Vector3D center = getCenter();

        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.translate(center.multiplyScalar(-1)).rotateX(angle).translate(center));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh rotateY(double angle) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();

        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.rotateY(angle));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh rotateYOnPlace(double angle) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();
        Vector3D center = getCenter();

        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.translate(center.multiplyScalar(-1)).rotateY(angle).translate(center));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh rotateZ(double angle) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();

        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.rotateZ(angle));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public TriangleMesh rotateZOnPlace(double angle) {
        List<Triangle> newTriangles = new ArrayList<Triangle>();
        Vector3D center = getCenter();

        for (Triangle triangle : triangles) {
            newTriangles.add(triangle.translate(center.multiplyScalar(-1)).rotateZ(angle).translate(center));
        }

        return new TriangleMesh(newTriangles, this.material.copy(), this.ID, this.componentHandle);
    }

    public double getWidth() {
        Vector3D[] bounding = this.getBounding();
        return bounding[1].getX() - bounding[0].getX();
    }

    public double getHeight() {
        Vector3D[] bounding = this.getBounding();
        return bounding[1].getY() - bounding[0].getY();
    }

    public double getDepth() {
        Vector3D[] bounding = this.getBounding();
        return bounding[1].getZ() - bounding[0].getZ();
    }

    public TriangleMesh subdivideTriangles(int count) {
        List<Triangle> subdividedTriangles = new ArrayList<>();

        for (Triangle triangle : triangles) {
            for (Triangle subTriangle : Triangle.subdivide(triangle, count)) {
                subdividedTriangles.add(subTriangle);
            }
        }

        if (count > 1) {
            TriangleMesh subdividedMesh = new TriangleMesh(subdividedTriangles);
            return subdividedMesh.subdivideTriangles(count - 1);
        } else {
            return new TriangleMesh(subdividedTriangles);
        }
    }

    public static TriangleMesh fromDoubleList(List<double[][]> triangles) {
        List<Triangle> triangleList = new ArrayList<>();

        for (double[][] triangle : triangles) {
            triangleList.add(new Triangle(new Vector3D(triangle[0][0], triangle[0][1], triangle[0][2]), new Vector3D(triangle[1][0], triangle[1][1], triangle[1][2]), new Vector3D(triangle[2][0], triangle[2][1], triangle[2][2])));
        }

        return new TriangleMesh(triangleList);
    }

}
