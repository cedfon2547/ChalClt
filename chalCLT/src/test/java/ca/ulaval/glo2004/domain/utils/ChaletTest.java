package ca.ulaval.glo2004.domain.utils;

import ca.ulaval.glo2004.domaine.*;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ChaletTest {


    public static Chalet chaletTest(){
        String nom = "test";
        double hauteur = 8.0;
        double largeur = 12.0;
        double longeur = 10.0;
        double epaisseurMur = 1.0;
        TypeSensToit typeSensToit = TypeSensToit.Est;
        double angletoit = 30.0;
        double margeAccessoire = 0.1;
        double margeSupplementaire = 0.1;
        return new Chalet(nom, hauteur, largeur, longeur,epaisseurMur, typeSensToit, angletoit, margeAccessoire, margeSupplementaire);
    }

    @Test
    public void instanceTest(){
        Chalet chalet = chaletTest();
        assertEquals("test", chalet.getNom());
        assertEquals(8.0, chalet.getHauteur(), 0);
        assertEquals(12.0, chalet.getLargeur(), 0);
        assertEquals(10.0, chalet.getLongueur(), 0);
        assertEquals(1.0, chalet.getEpaisseurMur(), 0);
        assertEquals(TypeSensToit.Est, chalet.getSensToit());
        assertEquals(30.0, chalet.getAngleToit(), 0);
        assertEquals(0.1, chalet.getMargeAccessoire(), 0);
        assertEquals(0.1, chalet.getMargeSupplementaireRetrait(), 0);
    }

    @Test
    public void getAccessoireTest(){
        Chalet chalet = chaletTest();
        double[] position = {2.0, 2.0};
        double[] dimension = {1.0, 1.0};

        Accessoire accessoire = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Fenetre, position , dimension);
        UUID uuid = accessoire.getAccessoireId();
        assertEquals(accessoire, chalet.getAccessoire(uuid));
    }

    @Test
    public void ajouterAccessoireTest(){
        Chalet chalet = chaletTest();
        double[] position = {2.0, 2.0};
        double[] dimension = {1.0, 1.0};
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        TypeMur typeMur = TypeMur.Facade;

        Accessoire result = chalet.ajouterAccessoire(typeMur, typeAccessoire, position , dimension);
        assertNotNull(result);
        assertEquals(typeAccessoire, result.getAccessoireType());
        assertEquals(typeMur, result.getTypeMur());

        Mur mur = chalet.getMur(typeMur);
        assertTrue(mur.getAccessoires().contains(result));
    }

    @Test
    public void retirerAccessoireTest() {
        Chalet chalet = chaletTest();
        TypeMur typeMur = TypeMur.Facade;
        TypeAccessoire typeAccessoire = TypeAccessoire.Fenetre;
        double[] position = {2.0, 2.0};
        double[] dimension = {1.0, 1.0};

        // Juste pour verifier que l'accessoire a bien ete ajouter avant d'etre supprimer
        Accessoire accessoire = chalet.ajouterAccessoire(typeMur, typeAccessoire, position, dimension);
        Mur mur = chalet.getMur(typeMur);
        assertTrue(mur.getAccessoires().contains(accessoire));

        //Suppression
        Accessoire accessoireRetire = chalet.retirerAccessoire(typeMur, accessoire.getAccessoireId());
        assertNotNull(accessoireRetire);
        assertEquals(accessoire, accessoireRetire);
        assertFalse(mur.getAccessoires().contains(accessoire));
    }




}
