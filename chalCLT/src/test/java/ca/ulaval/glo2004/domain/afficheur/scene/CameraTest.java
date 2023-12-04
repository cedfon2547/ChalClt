package ca.ulaval.glo2004.domain.afficheur.scene;
// package ca.ulaval.glo2004.domain.utils.scene;

// import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
// import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
// import org.junit.Test;

// import java.awt.*;

// import static org.junit.Assert.assertEquals;

// public class CameraTest {

//     public static Camera setUp(){
//         String id = "cameratest";
//         Vector3D position = new Vector3D(0, 0, -1000);
//         Vector3D direction = new Vector3D(0, 0, 0);
//         return new Camera(position, direction, id);
//     }

//     @Test
//     public void InstanceTest(){
//         String id = "cameratest";
//         Vector3D position = new Vector3D(0, 0, -1000);
//         Vector3D direction = new Vector3D(0, 0, 0);
//         Camera camera = new Camera(position, direction, id);
//         assertEquals(position, camera.getPosition());
//         assertEquals(direction, camera.getDirection());
//         assertEquals(id, camera.getId());
//         assertEquals(1.0, camera.getScale(),0);

//     }

//     @Test
//     public void setIdTest(){
//         Camera camera = setUp();
//         String id = "cameraChangetest";
//         camera.setId(id);
//         assertEquals(id, camera.getId());
//     }

//     @Test
//     public void setPositionTest(){
//         Camera camera = setUp();
//         Vector3D position = new Vector3D(100, 100, -1000);
//         camera.setPosition(position);
//         assertEquals(position, camera.getPosition());
//     }

//     @Test
//     public void setDimensionTest(){
//         Camera camera = setUp();
//         Vector3D direction = new Vector3D(2, 3, 10);
//         camera.setDirection(direction);
//         assertEquals(direction, camera.getDirection());
//     }

//     @Test
//     public void setScaleTest(){
//         Camera camera = setUp();
//         double scale = 2.0;
//         camera.setScale(scale);
//         assertEquals(scale, camera.getScale(),0);
//     }

//     // Cas si precise est a true
//     @Test
//     public void zoomInTest1(){
//         Camera camera = setUp();
//         Point point = new Point(3,3);
//         Dimension dimension = new Dimension(1, 1);
//         camera.zoomInDirection(point,dimension, true );
//         Vector3D result = new Vector3D(-0.0075, -0.0075,-1000);

//         // pas oublier de mettre delta a 0.0001 car a cause coversion le resultat peut varier
//         assertEquals(result.x, camera.getPosition().x, 0.0001);
//         assertEquals(result.y, camera.getPosition().y, 0.0001);
//         assertEquals(result.z, camera.getPosition().z, 0.0001);
//         assertEquals(1.003, camera.getScale(),0);
//     }

//     //Cas si precise est a false
//     @Test
//     public void zoomInTest2(){

//         Camera camera = setUp();
//         Point point = new Point(3,3);
//         Dimension dimension = new Dimension(1, 1);
//         camera.zoomInDirection(point,dimension, false);
//         Vector3D result = new Vector3D(-0.075, -0.075,-1000);

//         // A finir
//         assertEquals(result.x, camera.getPosition().x, 0.0001);
//         assertEquals(result.y, camera.getPosition().y, 0.0001);
//         assertEquals(result.z, camera.getPosition().z, 0.0001);
//         assertEquals(1.03, camera.getScale(),0);
//     }

//     @Test
//     public void zoomoutTest1(){
//         Camera camera = setUp();
//         Point point = new Point(3,3);
//         Dimension dimension = new Dimension(1, 1);
//         camera.zoomOutDirection(point,dimension, true );
//         Vector3D result = new Vector3D(0.0075, 0.0075,-1000);

//         assertEquals(result.x, camera.getPosition().x, 0.0001);
//         assertEquals(result.y, camera.getPosition().y, 0.0001);
//         assertEquals(result.z, camera.getPosition().z, 0.0001);
//         assertEquals(0.997, camera.getScale(),0.0001);

//     }

//     @Test
//     public void zoomoutTest2(){
//         Camera camera = setUp();
//         Point point = new Point(3,3);
//         Dimension dimension = new Dimension(1, 1);
//         camera.zoomOutDirection(point,dimension, false );
//         Vector3D result = new Vector3D(0.0728, 0.0728,-1000);

//         assertEquals(result.x, camera.getPosition().x, 0.0001);
//         assertEquals(result.y, camera.getPosition().y, 0.0001);
//         assertEquals(result.z, camera.getPosition().z, 0.0001);
//         assertEquals(0.97, camera.getScale(),0.001);

//     }

//     @Test
//     public void copyTest(){
//         Camera camera = setUp();
//         Camera cameraCopy = camera.copy();
//         assertEquals(camera.getId(), cameraCopy.getId());
//         assertEquals(camera.getPosition(), cameraCopy.getPosition());
//         assertEquals(camera.getDirection(), cameraCopy.getDirection());
//     }

//     // @Test
//     // public void moveLeft(){
//     //     Camera camera = setUp();
//     //     double distance = 2.0;
//     //     double resultat = camera.getPosition().x;
//     //     camera.moveLeft(distance);
//     //     assertEquals(resultat-2.0, camera.getPosition().x, 0);
//     // }

//     // @Test
//     // public void moveUpTest(){
//     //     Camera camera = setUp();
//     //     double distance = 2.0;
//     //     double resultat = camera.getPosition().y;
//     //     camera.moveUp(distance);
//     //     assertEquals(resultat+2.0, camera.getPosition().y, 0);
//     // }

//     // @Test
//     // public void moveForwardTest(){
//     //     Camera camera = setUp();
//     //     double distance = 2.0;
//     //     double resultat = camera.getPosition().z;
//     //     camera.moveForward(distance);
//     //     assertEquals(resultat+2.0, camera.getPosition().z, 0);
//     // }

//     // @Test
//     // public void rotateXTest(){
//     //     Camera camera = setUp();
//     //     double angle = 30.0;
//     //     double resultat = camera.getDirection().x;
//     //     camera.rotateX(angle);
//     //     assertEquals(resultat+angle, camera.getDirection().x, 0);
//     // }

//     // @Test
//     // public void rotateYTest(){
//     //     Camera camera = setUp();
//     //     double angle = 35.0;
//     //     double resultat = camera.getDirection().y;
//     //     camera.rotateY(angle);
//     //     assertEquals(resultat+angle, camera.getDirection().y, 0);
//     // }

//     // @Test
//     // public void rotateZTest(){
//     //     Camera camera = setUp();
//     //     double angle = 45.0;
//     //     double resultat = camera.getDirection().z;
//     //     camera.rotateZ(angle);
//     //     assertEquals(resultat+angle, camera.getDirection().z, 0);
//     // }


//     // Completer les dernier tests si il sont necessaire
// }
