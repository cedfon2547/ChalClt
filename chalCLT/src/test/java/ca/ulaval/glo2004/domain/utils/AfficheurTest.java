package ca.ulaval.glo2004.domain.utils;

import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AfficheurTest {

     public static Afficheur setUp() {
        // Initialisation des objets nécessaires avant chaque test
       Controleur controleur = Controleur.getInstance();
       Dimension dimension = new Dimension(1, 1);
       return new Afficheur(controleur, dimension);
    }


    @Test
    public void InstanceTest() {
        Controleur controleur = Controleur.getInstance();
        Dimension dimension = new Dimension(1, 1);
        Afficheur afficheur = new Afficheur(controleur, dimension);

        // Vérifiez que les membres de la classe Afficheur sont correctement initialisés
        assertNotNull(afficheur.getScene());
        assertNotNull(afficheur.getRasterizer());
        assertNotNull(afficheur.getScene().getCamera());

        //Verifier Instance
        assertEquals(controleur, afficheur.getControleur());
        assertEquals(dimension, afficheur.getDimension());
    }


    @Test
    public void setControleurTest(){
         Afficheur afficheur = setUp();
        Controleur controleur = Controleur.getInstance();
        afficheur.setControleur(controleur);
        assertEquals(controleur, afficheur.getControleur());
    }

    @Test
    public void setDimensionTest(){
        Afficheur afficheur = setUp();
        Dimension dimension = new Dimension(2, 3);
        afficheur.setDimension(dimension);
        assertEquals(dimension, afficheur.getDimension());
    }

    @Test
    public void setRasterizerTest(){
        Afficheur afficheur = setUp();
        //! DOTO apres test de light et scene et camera et rasterizer
    }



}
