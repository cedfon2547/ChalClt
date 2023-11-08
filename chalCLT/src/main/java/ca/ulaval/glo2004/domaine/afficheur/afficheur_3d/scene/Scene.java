package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;

public class Scene {
    private SceneConfiguration configuration = new SceneConfiguration();
    private ArrayList<TriangleMeshGroup> meshes = new ArrayList<TriangleMeshGroup>();
    // private ArrayList<TriangleMeshGroup> meshesGroups = new ArrayList<TriangleMeshGroup>();
    private Light light = new Light();
    private Camera camera = new Camera();

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

    public TriangleMeshGroup getMesh(String id) {
        for (TriangleMeshGroup mesh : meshes) {
            if (mesh.getID().equals(id)) {
                return mesh;
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
        meshes.add(mesh);
    }

    public void addMeshes(List<TriangleMeshGroup> meshes) {
        for (TriangleMeshGroup mesh : meshes) {
            this.meshes.add(mesh);
        }
    }

    public void addMeshes(TriangleMeshGroup[] meshes) {
        for (TriangleMeshGroup mesh : meshes) {
            this.meshes.add(mesh);
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

    public void viewTop() {
        Camera topCamera = new Camera();
        topCamera.setPosition(new Vector3D(0, 0, -1000));
        topCamera.setDirection(new Vector3D(Math.PI / 2, Math.PI, 0));
        setCamera(topCamera);
    }

    public void setSelected(String id, boolean selected) {
        for (TriangleMeshGroup mesh : meshes) {
            if (mesh.getID().equals(id)) {
                mesh.setSelected(selected);
            }
        }
    }
}
