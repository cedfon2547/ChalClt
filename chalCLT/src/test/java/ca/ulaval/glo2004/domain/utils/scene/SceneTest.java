package ca.ulaval.glo2004.domain.utils.scene;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Light;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.SceneConfiguration;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SceneTest {


    public static Scene setup(){

        return new Scene();
    }

    public static Camera setUpCamera(){
        String id = "cameratest";
        Vector3D position = new Vector3D(0, 0, -1000);
        Vector3D direction = new Vector3D(0, 0, 0);
        return new Camera(position, direction, id);
    }

    public static Light setUpLight(){
        Vector3D position = new Vector3D(200, 300, 200);
        Color color = Color.WHITE;
        double intensity = 2.0;
        return new Light(position, color, intensity);
    }

    @Test
    public void InstanceTest(){
        // voir si le constructeur avec l'objet de type triangleMesh est vraiment utile
        //sinon faire constructeur de base
    }

    @Test
    public void addMeshTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        scene.addMesh(testMesh);
        assertTrue(scene.getMeshes().contains(testMesh));
    }

    @Test
    public void removeMeshTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        scene.addMesh(testMesh);
        //Verifie si il la mesh est a l'interieur avant de l'enlever
        assertTrue(scene.getMeshes().contains(testMesh));

        scene.removeMesh(testMesh);
        assertFalse(scene.getMeshes().contains(testMesh));
    }

    @Test
    public void clearMeshesTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        TriangleMeshGroup testMesh2 = new TriangleMeshGroup();
        TriangleMeshGroup testMesh3 = new TriangleMeshGroup();
        List<TriangleMeshGroup> listeMesh = new ArrayList<TriangleMeshGroup>();
        listeMesh.add(testMesh);
        listeMesh.add(testMesh2);
        listeMesh.add(testMesh3);
        scene.addMeshes(listeMesh);

        //Verifie si il la mesh est a l'interieur avant de l'enlever
        assertTrue(scene.getMeshes().contains(testMesh));
        assertTrue(scene.getMeshes().contains(testMesh2));
        assertTrue(scene.getMeshes().contains(testMesh3));


        scene.clearMeshes();
        assertFalse(scene.getMeshes().contains(testMesh));
        assertFalse(scene.getMeshes().contains(testMesh2));
        assertFalse(scene.getMeshes().contains(testMesh3));
    }

    @Test
    public void setLight(){
        Light light = setUpLight();
        Scene scene = setup();
        scene.setLight(light);
        assertEquals(light, scene.getLight());
    }

    @Test
    public void setCamera(){
        Camera camera = setUpCamera();
        Scene scene = setup();
        scene.setCamera(camera);
        assertEquals(camera, scene.getCamera());
    }

    @Test
    public void setSelectedTrueTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        scene.addMesh(testMesh);

        String id = testMesh.getIdentifier();
        scene.setSelected(id, true);
        assertTrue(testMesh.getSelected());
    }

    @Test
    public void setSelectedFlaseTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        scene.addMesh(testMesh);

        String id = testMesh.getIdentifier();
        scene.setSelected(id, false);
        assertFalse(testMesh.getSelected());
    }

    @Test
    public void setValideTrueTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        scene.addMesh(testMesh);

        String id = testMesh.getIdentifier();
        scene.setValid(id, true);
        assertTrue(testMesh.getValid());
    }

    @Test
    public void setValideFlaseTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        scene.addMesh(testMesh);

        String id = testMesh.getIdentifier();
        scene.setValid(id, false);
        assertFalse(testMesh.getValid());
    }

    @Test
    public void clearAllselectionTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        TriangleMeshGroup testMesh2 = new TriangleMeshGroup();
        TriangleMeshGroup testMesh3 = new TriangleMeshGroup();
        List<TriangleMeshGroup> listeMesh = new ArrayList<TriangleMeshGroup>();
        listeMesh.add(testMesh);
        listeMesh.add(testMesh2);
        listeMesh.add(testMesh3);
        scene.addMeshes(listeMesh);

        scene.clearAllSelection();
        assertFalse(testMesh.getSelected());
        assertFalse(testMesh2.getSelected());
        assertFalse(testMesh3.getSelected());
    }

    @Test
    public void clearAllValideTest(){
        Scene scene = setup();
        TriangleMeshGroup testMesh = new TriangleMeshGroup();
        TriangleMeshGroup testMesh2 = new TriangleMeshGroup();
        TriangleMeshGroup testMesh3 = new TriangleMeshGroup();
        List<TriangleMeshGroup> listeMesh = new ArrayList<TriangleMeshGroup>();
        listeMesh.add(testMesh);
        listeMesh.add(testMesh2);
        listeMesh.add(testMesh3);
        scene.addMeshes(listeMesh);

        scene.clearAllValide();
        assertTrue(testMesh.getValid());
        assertTrue(testMesh2.getValid());
        assertTrue(testMesh3.getValid());
    }

    // Verifier la necessite des autres methodes et faire un tri si necessaire
}
