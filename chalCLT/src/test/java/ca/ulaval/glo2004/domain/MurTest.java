package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domaine.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;
import java.util.List;

public class MurTest {

    private final double margeAccessoire = 0.1;
    private final double epaisseurMur = 1.0;

   // Mur et accessoire pour test pour eviter redondance
    public Mur setTest(){
        TypeMur typeMur = TypeMur.Facade;
        Chalet chalet = new Chalet("nom", 10.0, 10.0, 10.0, epaisseurMur, TypeSensToit.Est, 30.0, margeAccessoire, 0.1);
        return new Mur(typeMur, chalet);
    }

    // Accessoire de base pour eviter redondance
    public static Accessoire accessoireTrue1(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {3.0, 3.0};
        double[] dim = {1.0,1.0};
        return new Accessoire(typeAccessoire, typeMur, pos, dim, true);
    }

   // Accessoire qui colisionne pour eviter redondance
    public static Accessoire accessoireTrue2(){
        TypeAccessoire typeAccessoire2 = TypeAccessoire.Fenetre;
        TypeMur typeMur2 = TypeMur.Facade;
        double[] pos2 = {2.0, 3.0};
        double[] dim2 = {2.0,1.0};
        return new Accessoire(typeAccessoire2, typeMur2, pos2, dim2, true);
    }

    // Accessoire qui ne colisionne pas pour eviter redondance
    public static Accessoire accessoireFalse(){
        TypeAccessoire typeAccessoire2 = TypeAccessoire.Porte;
        TypeMur typeMur2 = TypeMur.Facade;
        double[] pos2 = {7.0, 8.0};
        double[] dim2 = {1.0,2.0};
        return new Accessoire(typeAccessoire2, typeMur2, pos2, dim2, true);
    }

    // Test
    @Test
    public void InstanceTest(){
        Mur mur = setTest();
        assertEquals(TypeMur.Facade, mur.getType());
        // assertEquals(chalet, mur.getChalet());
    }

    @Test
    public void ajouterAccessoireTest(){

        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;
        double[] dim = {1.0,2.0};
        double[] mauvaise_pos = {-1.0 , -2.0};
        Accessoire accessoire = accessoireTrue1();
        Mur mur = setTest();

        mur.ajouterAccessoire(accessoire);

        assertEquals(accessoire, mur.getAccessoires().get(0));

        Accessoire accessoireMauvaisePosition = new Accessoire(typeAccessoire, typeMur, mauvaise_pos, dim, true);
        try {
            mur.ajouterAccessoire(accessoireMauvaisePosition);
            fail("Devrait lever une IllegalArgumentException pour la mauvaise position");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void RetirerAccessoireTest(){
        Accessoire accessoire = accessoireTrue1();
        Mur mur = setTest();

        mur.ajouterAccessoire(accessoire);
        UUID uuid = accessoire.getAccessoireId();
        Accessoire accessoireretirer = mur.retirerAccessoire(uuid);
        assertEquals(accessoire, accessoireretirer);

        // Test si l'accessoire ne se retrouve pas dans la liste
        UUID mauvais_uuid = UUID.randomUUID();
        assertNull(mur.retirerAccessoire(mauvais_uuid));
    }

    @Test
    public void verifierCollisionTrueTest(){
        //Accessoire 1
        Accessoire accessoire = accessoireTrue1();

        //Accessoire 2
        Accessoire accessoire2 = accessoireTrue2();

        Mur mur = setTest();
        mur.ajouterAccessoire(accessoire);  // Insertion 1 element

        // Comparaison avec tout les accessoires
        assertTrue(mur.verifierCollisionAcc(accessoire2, margeAccessoire));
    }

    @Test
    public void verifierCollisionFalseTest(){
        //Accessoire 1
        Accessoire accessoire = accessoireTrue1();

        //Accessoire 2
        Accessoire accessoire2 = accessoireFalse();

        Mur mur = setTest();
        mur.ajouterAccessoire(accessoire);  // Insertion 1 element

        // Comparaison avec tout les accessoires
        assertFalse(mur.verifierCollisionAcc(accessoire2, margeAccessoire));
    }

    @Test
    public void verifierValiditerAccessoireTrueTest(){
        //DOTO
        Mur mur = setTest();
        Accessoire accessoire = accessoireTrue1();
        mur.ajouterAccessoire(accessoire);
        Accessoire accessoire2 = accessoireFalse();
        boolean validite = mur.verifierValiditeAccessoire(accessoire2, margeAccessoire, epaisseurMur);
        assertTrue(validite);
    }

    @Test
    public void verifierValiditerAccessoireFalseTest(){
        //DOTO
        Mur mur = setTest();

        Accessoire accessoire = accessoireTrue1();
        mur.ajouterAccessoire(accessoire);
        Accessoire accessoire2 = accessoireTrue2();
        boolean validite = mur.verifierValiditeAccessoire(accessoire2, margeAccessoire, epaisseurMur);
        assertFalse(validite);
    }

    @Test
    public void setAccessoireTest(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        Mur mur = setTest();
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, mur.getType(), pos, dim, true);

        List<Accessoire> accessoireList = new ArrayList<>();
        accessoireList.add(accessoire);

        mur.setAccessoires(accessoireList);
        assertEquals(accessoireList, mur.getAccessoires());
    }

}
