package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.base.Vector3D;

public class TriangleMeshGroup {
    ArrayList<TriangleMesh> meshes = new ArrayList<TriangleMesh>();

    public TriangleMeshGroup() {
    }

    public TriangleMeshGroup(ArrayList<TriangleMesh> meshes) {
        this.meshes = meshes;
    }

    public TriangleMeshGroup(TriangleMesh[] meshes) {
        for (TriangleMesh mesh : meshes) {
            this.meshes.add(mesh);
        }
    }

    public List<TriangleMesh> getMeshes() {
        return meshes;
    }

    public TriangleMesh getMesh(int index) {
        return meshes.get(index);
    }

    public TriangleMesh getMesh(String id) {
        for (TriangleMesh mesh : meshes) {
            if (mesh.ID.equals(id)) {
                return mesh;
            }
        }
        return null;
    }

    public void addMesh(TriangleMesh mesh) {
        meshes.add(mesh);
    }

    public void addMeshes(List<TriangleMesh> meshes) {
        this.meshes.addAll(meshes);
    }

    public void setMeshes(ArrayList<TriangleMesh> meshes) {
        this.meshes = meshes;
    }

    public void clear() {
        meshes.clear();
    }

    public void removeMesh(TriangleMesh mesh) {
        meshes.removeIf(m -> m.ID.equals(mesh.ID));
    }

    public void removeMeshes(List<TriangleMesh> meshes) {
        this.meshes.removeAll(meshes);
    }

    public void removeMeshes(TriangleMesh[] meshes) {
        this.removeMeshes(Arrays.asList(meshes));
    }

    public Vector3D[] getBounding() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (TriangleMesh mesh : meshes) {
            Vector3D[] bounding = mesh.getBounding();

            if (bounding[0].x < minX) {
                minX = bounding[0].x;
            }

            if (bounding[1].x > maxX) {
                maxX = bounding[1].x;
            }

            if (bounding[0].y < minY) {
                minY = bounding[0].y;
            }

            if (bounding[1].y > maxY) {
                maxY = bounding[1].y;
            }

            if (bounding[0].z < minZ) {
                minZ = bounding[0].z;
            }

            if (bounding[1].z > maxZ) {
                maxZ = bounding[1].z;
            }
        }

        return new Vector3D[] { new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ) };
    }

    public Vector3D getCenter() {
        Vector3D[] bounding = this.getBounding();
        return new Vector3D((bounding[0].getX() + bounding[1].getX()) / 2,
                (bounding[0].getY() + bounding[1].getY()) / 2, (bounding[0].getZ() + bounding[1].getZ()) / 2);
    }

    public TriangleMeshGroup rotateX(double angle) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiplyScalar(-1)).rotateX(angle));
        }

        return newGroup;
    }

    public TriangleMeshGroup rotateY(double angle) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiplyScalar(-1)).rotateY(angle));
        }

        return newGroup;
    }

    public TriangleMeshGroup rotateZ(double angle) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiplyScalar(-1)).rotateZ(angle));
        }

        return newGroup;
    }

    public TriangleMeshGroup translate(Vector3D translation) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(translation));
        }

        return newGroup;
    }

    public TriangleMeshGroup scale(Vector3D scale) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.scale(scale));
        }

        return newGroup;
    }
}
