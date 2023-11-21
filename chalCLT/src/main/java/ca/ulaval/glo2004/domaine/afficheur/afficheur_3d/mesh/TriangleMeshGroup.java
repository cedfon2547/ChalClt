package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class TriangleMeshGroup {
    String identifier = UUID.randomUUID().toString();
    List<TriangleMesh> meshes = new ArrayList<TriangleMesh>();
    private boolean selected = false;
    private boolean selectable = true;
    private boolean visible = true;
    private boolean valid = true;
    private Vector3D position = new Vector3D(0, 0, 0);
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

    public Vector3D getPosition() {
        return position;
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

    public String getIdentifier() {
        return identifier;
    }

    public boolean getSelected() {
        return selected;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void addMesh(TriangleMesh mesh) {
        meshes.add(mesh);
    }

    public void addMeshes(List<TriangleMesh> meshes) {
        this.meshes.addAll(meshes);
    }

    public void setMeshes(List<TriangleMesh> meshes) {
        this.meshes = meshes;
    }

    public void setIdentifier(String ID) {
        this.identifier = ID;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
    public boolean getSelectable() {return this.selectable;}
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean getVisible() {return this.visible;}

    public void setPosition(Vector3D position) {
        this.position = position;
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

    public Vector3D getCenter() {
        Vector3D[] bounding = this.getBounding();
        return new Vector3D((bounding[0].getX() + bounding[1].getX()) / 2,
                (bounding[0].getY() + bounding[1].getY()) / 2, (bounding[0].getZ() + bounding[1].getZ()) / 2);
    }

    public TriangleMeshGroup rotateX(double angle) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiply(-1)).rotateX(angle));
        }

        return newGroup;
    }

    public TriangleMeshGroup rotateY(double angle) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiply(-1)).rotateY(angle));
        }

        return newGroup;
    }

    public TriangleMeshGroup rotateZ(double angle) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiply(-1)).rotateZ(angle));
        }

        return newGroup;
    }

    public TriangleMeshGroup rotate(double angleX, double angleY, double angleZ, Vector3D axis) {
        TriangleMeshGroup newGroup = new TriangleMeshGroup();
        Vector3D groupCenter = this.getCenter();

        for (TriangleMesh mesh : meshes) {
            newGroup.addMesh(mesh.translate(groupCenter.multiply(-1)).rotate(angleX, angleY, angleZ, axis)
                    .translate(groupCenter));
        }

        return newGroup;
    }

    public TriangleMeshGroup rotate(double angleX, double angleY, double angleZ) {
        return this.rotate(angleX, angleY, angleZ, new Vector3D(0, 0, 0));
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
