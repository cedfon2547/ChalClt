package ca.ulaval.glo2004.domain.utils;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccessoireTest {

    @Test
    public void InstanceTestFenetre(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};

        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim );

        assertEquals(TypeAccessoire.Fenetre, accessoire.getAccessoireType());
        assertEquals(TypeMur.Droit, accessoire.getTypeMur());
        assertEquals(2, accessoire.getPosition()[0], 0);
        assertEquals(3, accessoire.getPosition()[1], 0);
        assertEquals(1, accessoire.getDimension()[0], 0);
        assertEquals(2, accessoire.getDimension()[1], 0);
    }

    @Test
    public void InstanceTestPorte(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};

        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim );

        assertEquals(TypeAccessoire.Porte, accessoire.getAccessoireType());
        assertEquals(TypeMur.Droit, accessoire.getTypeMur());
        assertEquals(2, accessoire.getPosition()[0], 0);
        assertEquals(3, accessoire.getPosition()[1], 0);
        assertEquals(1, accessoire.getDimension()[0], 0);
        assertEquals(2, accessoire.getDimension()[1], 0);
    }

    @Test
    public void SetTest(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {4.0, 3.0};
        double[] dim = {7.0,2.0};
        UUID uuid = UUID.randomUUID();
        String nom = "NomRandom101";
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim);

        accessoire.setAccessoireType(TypeAccessoire.Fenetre);
        assertEquals(TypeAccessoire.Fenetre, accessoire.getAccessoireType());

        accessoire.setAccessoireId(uuid);
        assertEquals(uuid, accessoire.getAccessoireId());

        accessoire.setAccessoireNom(nom);
        assertEquals(nom, accessoire.getAccessoireNom());

        accessoire.setValide(true);
        assertTrue(accessoire.getValide());
    }

    @Test
    public void getMarginWithRectTest(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Droit;
        double[] pos = {4.0, 3.0};
        double[] dim = {7.0,2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur,pos, dim);
        double[] pos2 = {5.0, 2.0};
        double[] dim2 = {3.0, 4.0};
        double[] result = accessoire.getMarginWithRect(pos2, dim2);

        assertEquals(1.0, result[0], 0);
        assertEquals(1.0, result[1], 0);
        assertEquals(3.0, result[2], 0);
        assertEquals(1.0, result[3], 0);
    }

}
