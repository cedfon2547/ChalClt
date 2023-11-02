package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;

public class Scene {
    private SceneConfiguration configuration = new SceneConfiguration();
    private ArrayList<TriangleMesh> meshes = new ArrayList<TriangleMesh>();
    private ArrayList<TriangleMeshGroup> meshesGroups = new ArrayList<TriangleMeshGroup>();
    private Light light = new Light();
    private Camera camera = new Camera();

    public Scene() {
    }

    public Scene(ArrayList<TriangleMesh> objects, Light light, Camera camera) {
        this.meshes = objects;
        this.light = light;
        this.camera = camera;
    }

    public SceneConfiguration getConfiguration() {
        return configuration;
    }

    public ArrayList<TriangleMesh> getMeshes() {
        return meshes;
    }

    public ArrayList<TriangleMeshGroup> getMeshesGroups() {
        return meshesGroups;
    }

    public Light getLight() {
        return light;
    }

    public Camera getCamera() {
        return camera;
    }

    public void addMesh(TriangleMesh mesh) {
        meshes.add(mesh);
    }

    public void addMeshes(List<TriangleMesh> meshes) {
        meshes.addAll(meshes);
    }

    public void addMeshes(TriangleMesh[] meshes) {
        for (TriangleMesh mesh : meshes) {
            this.meshes.add(mesh);
        }
    }

    public void addMeshesGroup(TriangleMeshGroup meshesGroup) {
        meshesGroups.add(meshesGroup);
    }

    public void removeMesh(TriangleMesh mesh) {
        meshes.remove(mesh);
    }

    public void removeMeshes(List<TriangleMesh> meshes) {
        meshes.removeAll(meshes);
    }

    public void clearMeshes() {
        meshes.clear();
    }

    public void removeMeshes(TriangleMesh[] meshes) {
        this.removeMeshes(Arrays.asList(meshes));
    }

    public void removeMeshesGroup(TriangleMeshGroup meshesGroup) {
        meshesGroups.remove(meshesGroup);
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
