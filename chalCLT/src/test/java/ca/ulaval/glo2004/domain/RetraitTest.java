package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domaine.Retrait;
import ca.ulaval.glo2004.domaine.TypeRetrait;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RetraitTest {

    @Test
    public void testInstanceAcessoire(){
        double[] pos = {4.0, 8.0};
        double[] dim = {3.0, 6.0};
        Retrait retrait = new Retrait(TypeRetrait.Accessoire, pos , dim);

        assertEquals(TypeRetrait.Accessoire, retrait.getType());
        assertEquals(4.0, retrait.getPosition()[0], 0);
        assertEquals(8.0, retrait.getPosition()[1], 0);
        assertEquals(3.0, retrait.getDimension()[0], 0);
        assertEquals(6.0, retrait.getDimension()[1], 0);
    }

    @Test
    public void testInstanceMur(){
        double[] pos = {2.0, 3.0};
        double[] dim = {5.0, 2.0};
        Retrait retrait = new Retrait(TypeRetrait.Rainure, pos , dim);

        assertEquals(TypeRetrait.Rainure, retrait.getType());
        assertEquals(2.0, retrait.getPosition()[0], 0);
        assertEquals(3.0, retrait.getPosition()[1], 0);
        assertEquals(5.0, retrait.getDimension()[0], 0);
        assertEquals(2.0, retrait.getDimension()[1], 0);
    }

    @Test
    public void testsetType(){
        double[] pos = {2.0, 3.0};
        double[] dim = {5.0, 2.0};
        Retrait retrait = new Retrait(TypeRetrait.Rainure, pos , dim);
        retrait.setType(TypeRetrait.Accessoire);
        assertEquals(TypeRetrait.Accessoire, retrait.getType());
    }
}
