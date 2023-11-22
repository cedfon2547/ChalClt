package ca.ulaval.glo2004.domain.utils.scene;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Light;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class LightTest {

    public Light setUp(){
        Vector3D position = new Vector3D(200, 300, 200);
        Color color = Color.WHITE;
        double intensity = 2.0;
        return new Light(position, color, intensity);
    }

    @Test
    public void InstanceTest(){
        Vector3D position = new Vector3D(200, 300, 200);
        Color color = Color.WHITE;
        double intensity = 2.0;
        Light light = new Light(position, color, intensity);

        assertEquals(position, light.getPosition());
        assertEquals(color, light.getColor());
        assertEquals(intensity, light.getIntensity(), 0);
    }

    @Test
    public void setColorTest(){
        Light light = setUp();
        light.setColor(Color.BLUE);
        assertEquals(Color.BLUE, light.getColor());
    }

    @Test
    public void setIntensityTest(){
        Light light = setUp();
        light.setIntensity(1.0);
        assertEquals(1.0, light.getIntensity(), 0);
    }

    @Test
    public void setAmbientIntensityTest(){
        Light light = setUp();
        //Test si intensity >= 1 ou <= 0
        light.setAmbientIntensity(4.0);
        assertNotEquals(4.0, light.getAmbientIntensity(), 0);

        // Test si ok
        light.setAmbientIntensity(0.8);
        assertEquals(0.8, light.getAmbientIntensity(), 0);
    }

    @Test
    public void setPositionTest(){
        Light light = setUp();
        Vector3D newPosition = new Vector3D(200, 300, 200);
        light.setPosition(newPosition);
        assertEquals(newPosition, light.getPosition());
    }

    @Test
    public void setBlenderFactorTest(){
        Light light = setUp();
        //Test si intensity >= 1 ou <= 0
        light.setBlenderFactor(3.0);
        assertNotEquals(3.0, light.getBlenderFactor(), 0);

        // Test si ok
        light.setBlenderFactor(0.8);
        assertEquals(0.8, light.getBlenderFactor(), 0);
    }

    @Test
    public void setDirectionTest(){
        Light light = setUp();
        Vector3D newDirection = new Vector3D(300, 300, -300);
        light.setDirection(newDirection);
        assertEquals(newDirection, light.getDirection());
    }

    // a voir si ce test est necessaire
    //a + (b - a) * t
    @Test
    public void lerpTest(){
        double result = Light.Lerp(2.0, 4.0, 0.5);
        assertEquals(3.0, result, 0);

        try {
            Light.Lerp(2.0, 4.0, 2.0);
            fail("Devrait lever une IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }
}
