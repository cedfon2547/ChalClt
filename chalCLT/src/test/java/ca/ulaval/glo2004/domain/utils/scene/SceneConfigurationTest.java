// package ca.ulaval.glo2004.domain.utils.scene;

// import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.SceneConfiguration;
// import org.junit.Test;

// import java.awt.*;

// import static org.junit.Assert.*;

// public class SceneConfigurationTest {

//     @Test
//     public void setAxisStrokeWidthTest(){
//         int axisStrokeWidth = 3;
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setAxisStrokeWidth(axisStrokeWidth);
//         assertEquals(axisStrokeWidth, sceneConfiguration.getAxisStrokeWidth());
//     }

//     @Test
//     public void setGridStrokeWidthTest(){
//         int gridStrokeWidth = 4;
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setGridStrokeWidth(gridStrokeWidth);
//         assertEquals(gridStrokeWidth, sceneConfiguration.getGridStrokeWidth());
//     }

//     @Test
//     public void setGridColorTest(){
//         Color gridColor = new Color(75, 75, 75, 255);
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setGridColor(gridColor);
//         assertEquals(gridColor, sceneConfiguration.getGridColor());
//     }

//     @Test
//     public void setBackgroundColorTest(){
//         Color backgroundColor = new Color(30, 75, 100, 255);
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setBackgroundColor(backgroundColor);
//         assertEquals(backgroundColor, sceneConfiguration.getBackgroundColor());
//     }

//     @Test
//     public void setShowAxisTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowAxis(true);
//         assertTrue(sceneConfiguration.getShowAxis());
//     }

//     @Test
//     public void setShowGridXYTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowGridXY(true);
//         assertTrue(sceneConfiguration.getShowGridXY());
//     }

//     @Test
//     public void setShowGridXZTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowGridXZ(true);
//         assertTrue(sceneConfiguration.getShowGridXZ());
//     }

//     @Test
//     public void setShowGridYZTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowGridYZ(true);
//         assertTrue(sceneConfiguration.getShowGridYZ());
//     }

//     @Test
//     public void setShowLightTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowLight(false);
//         assertFalse(sceneConfiguration.getShowLight());
//     }

//     @Test
//     public void setShowBoundingBoxTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowBoundingBox(true);
//         assertTrue(sceneConfiguration.getShowBoundingBox());
//     }

//     @Test
//     public void setShowTrianglesTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowTriangles(true);
//         assertTrue(sceneConfiguration.getShowTriangles());
//     }

//     @Test
//     public void setShowGroundTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setShowGround(false);
//         assertFalse(sceneConfiguration.getShowGround());
//     }

//     @Test
//     public void setHighlightVerticesTest(){
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setHighlightVertices(false);
//         assertFalse(sceneConfiguration.getHighlightVertices());
//     }

//     @Test
//     public void setSelectionColorTest(){
//         Color selectionColor = new Color(35, 85, 140, 205);
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setSelectionColor(selectionColor);
//         assertEquals(selectionColor, sceneConfiguration.getSelectionColor());
//     }

//     @Test
//     public void setSelectionStrokeWidthTest(){
//         int selectionStrokeWidth = 6;
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setSelectionStrokeWidth(selectionStrokeWidth);
//         assertEquals(selectionStrokeWidth, sceneConfiguration.getSelectionStrokeWidth());
//     }
//     @Test
//     public void setStepCountsTest(){
//         int stepCounts = 4;
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setStepCounts(stepCounts);
//         assertEquals(stepCounts, sceneConfiguration.getStepCounts());
//     }
//     @Test
//     public void setGridStepTest(){
//         int gridStep = 8;
//         SceneConfiguration sceneConfiguration = new SceneConfiguration();
//         sceneConfiguration.setGridStep(gridStep);
//         assertEquals(gridStep, sceneConfiguration.getGridStep());
//     }
// }
