package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;

public class Scene {
    private SceneConfiguration configuration = new SceneConfiguration();
    private ArrayList<TriangleMeshGroup> meshes = new ArrayList<TriangleMeshGroup>();
    private Light light = new Light();
    private Camera camera = new Camera(new Vector3D(0, 0, 1000), new Vector3D(0, 0, 1000));

    public Scene() {
    }

    public Scene(ArrayList<TriangleMesh> objects, Light light, Camera camera) {
        this.light = light;
        this.camera = camera;
    }

    public SceneConfiguration getConfiguration() {
        return configuration;
    }

    public ArrayList<TriangleMeshGroup> getMeshes() {
        return meshes;
    }

    public TriangleMeshGroup getMesh(int index) {
        return meshes.get(index);
    }

    @FunctionalInterface
    public static interface TraverseMeshesFn {
        public void apply(TriangleMesh mesh);
    }

    public void traverseMeshes(TriangleMesh mesh, TraverseMeshesFn fn) {
        fn.apply(mesh);
        if (mesh instanceof TriangleMeshGroup) {
            for (TriangleMesh child : ((TriangleMeshGroup) mesh).getMeshes()) {
                traverseMeshes(child, fn);
            }
        }
    }

    public TriangleMesh getMesh(String id) {
        for (TriangleMeshGroup group: this.getMeshes()) {
            if (group.getIdentifier().equals(id)) {
                return group;
            }

            for (TriangleMesh mesh: group.getMeshes()) {
                if (mesh.getIdentifier().equals(id)) {
                    return mesh;
                }
            }
        }

        return null;
    }

    public Light getLight() {
        return light;
    }

    public Camera getCamera() {
        return camera;
    }

    public void addMesh(TriangleMeshGroup mesh) {
        this.formatTriangles(mesh);
        meshes.add(mesh);
    }

    public void addMeshes(List<TriangleMeshGroup> meshes) {
        for (TriangleMeshGroup mesh : meshes) {
            this.addMesh(mesh);
        }
    }

    public void addMeshes(TriangleMeshGroup[] meshes) {
        for (TriangleMeshGroup mesh : meshes) {
            this.addMesh(mesh);
        }
    }

    public void removeMesh(TriangleMeshGroup mesh) {
        meshes.remove(mesh);
    }

    public void removeMeshes(List<TriangleMeshGroup> meshes) {
        meshes.removeAll(meshes);
    }

    public void clearMeshes() {
        meshes.clear();
    }

    public void removeMeshes(TriangleMeshGroup[] meshes) {
        this.removeMeshes(Arrays.asList(meshes));
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public void setSelected(String id, boolean selected) {
        for (TriangleMeshGroup mesh : meshes) {
            if (mesh.getIdentifier().equals(id)) {
                mesh.setSelected(selected);
            }
        }
    }

    public void setValid(String id, boolean valid) {
        for (TriangleMeshGroup mesh: meshes) {
            if (mesh.getIdentifier().equals(id)) {
                mesh.setValid(valid);
            }
        }
    }

    public void clearAllSelection() {
        for (TriangleMeshGroup mesh : meshes) {
            mesh.setSelected(false);
        }
    }

    public void clearAllValide() {
        for (TriangleMeshGroup mesh : meshes) {
            mesh.setValid(true);
        }
    }

    public void formatTriangles(TriangleMesh mesh) {
        for (Triangle triangle : mesh.getTriangles()) {
            Vector3D toCamera = getCamera().getPosition().sub(triangle.getVertice(0));
                double dotProduct = toCamera.dot(triangle.getNormal());

                if (dotProduct < 0) {
                    Vector3D temp = triangle.getVertex1();
                    triangle.setVertex1(triangle.getVertex2());
                    triangle.setVertex2(temp);
                }
        }
    }
}
