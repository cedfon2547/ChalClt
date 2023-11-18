package ca.ulaval.glo2004.domain.utils;

import ca.ulaval.glo2004.domaine.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class MurTest {

    @Test
    public void InstanceTest(){
        TypeMur typeMur = TypeMur.Facade;
        Chalet chalet = new Chalet("nom", 3.0, 3.0, 3.0, 1.0, TypeSensToit.Est, 30.0, 0.1, 0.1);
        Mur mur = new Mur(typeMur, chalet);
        assertEquals(TypeMur.Facade, mur.getType());
    }

    @Test
    public void ajouterAccessoireTest(){

        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        double[] mauvaise_pos = {-1.0 , -2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur, pos, dim);
        Chalet chalet = new Chalet("nom", 3.0, 3.0, 3.0, 1.0, TypeSensToit.Est, 30.0, 0.1, 0.1);
        Mur mur = new Mur(typeMur, chalet);

        mur.ajouterAccessoire(accessoire);

        assertEquals(accessoire, mur.getAccessoires().get(0));

        Accessoire accessoireMauvaisePosition = new Accessoire(typeAccessoire, typeMur, mauvaise_pos, dim);
        try {
            mur.ajouterAccessoire(accessoireMauvaisePosition);
            fail("Devrait lever une IllegalArgumentException pour la mauvaise position");
        } catch (IllegalArgumentException e) {
        }
    }


    @Test
    public void RetirerAccessoireTest(){
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur, pos, dim);
        Chalet chalet = new Chalet("nom", 3.0, 3.0, 3.0, 1.0, TypeSensToit.Est, 30.0, 0.1, 0.1);
        Mur mur = new Mur(typeMur, chalet);

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
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {2.0, 3.0};
        double[] dim = {3.0,2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur, pos, dim);

        //Accessoire 2
        TypeAccessoire typeAccessoire2 = TypeAccessoire.Porte;
        TypeMur typeMur2 = TypeMur.Facade;
        double[] pos2 = {3.0, 4.0};
        double[] dim2 = {1.0,2.0};
        Accessoire accessoire2 = new Accessoire(typeAccessoire2, typeMur2, pos2, dim2);

        Chalet chalet = new Chalet("nom", 10.0, 9.0, 10.0, 1.0, TypeSensToit.Est, 30.0, 0.1, 0.1);
        Mur mur = new Mur(typeMur, chalet);
        mur.ajouterAccessoire(accessoire);  // Insertion 1 element

        // Comparaison avec tout les accessoires
        assertTrue(mur.verifierCollisionAcc(accessoire2, chalet.getMargeAccessoire()));
    }

    @Test
    public void verifierCollisionFalseTest(){
        //Accessoire 1
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {2.0, 3.0};
        double[] dim = {3.0,2.0};
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur, pos, dim);

        //Accessoire 2
        TypeAccessoire typeAccessoire2 = TypeAccessoire.Porte;
        TypeMur typeMur2 = TypeMur.Facade;
        double[] pos2 = {7.0, 8.0};
        double[] dim2 = {1.0,2.0};
        Accessoire accessoire2 = new Accessoire(typeAccessoire2, typeMur2, pos2, dim2);

        Chalet chalet = new Chalet("nom", 10.0, 9.0, 10.0, 1.0, TypeSensToit.Est, 30.0, 0.1, 0.1);
        Mur mur = new Mur(typeMur, chalet);
        mur.ajouterAccessoire(accessoire);  // Insertion 1 element

        // Comparaison avec tout les accessoires
        assertFalse(mur.verifierCollisionAcc(accessoire2, chalet.getMargeAccessoire()));
    }

    @Test
    public void verifierValiditerAccessoireTest(){
        //DOTO
    }
}
