package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domaine.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    
    @Test
    public void updatePorteWithHauteur() {
        Chalet chalet = chaletTest();
        double[] pos = {4.0, 5.0};
        double[] dim = {2.0, 3.0};
        Accessoire porte = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Porte, pos, dim);
        porte.setPosition(pos);
        Accessoire.AccessoireDTO porteDTO = porte.toDTO();
        double bottomMargin = porte.getMarginWithRect(porteDTO.position, porteDTO.dimensions)[3];
        Chalet.ChaletDTO test = chalet.toDTO();
        test.hauteur = 16.0;
        chalet.updateChalet(test);
        
        assertEquals(test.hauteur - bottomMargin - porteDTO.dimensions[1], chalet.getAccessoire(porteDTO.accessoireId).getPosition()[1], 0.0);
    }
    
    @Test
    public void updatePorteWithLongueur() {
        Chalet chalet = chaletTest();
        double[] pos = {4.0, 5.0};
        double[] dim = {2.0, 3.0};
        Accessoire porte1 = chalet.ajouterAccessoire(TypeMur.Gauche, TypeAccessoire.Porte, pos, dim);
        porte1.setPosition(pos);
        Accessoire.AccessoireDTO porte1DTO = porte1.toDTO();
        
        Accessoire porte2 = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Porte, pos, dim);
        porte2.setPosition(pos);
        Accessoire.AccessoireDTO porte2DTO = porte2.toDTO();
        
        Chalet.ChaletDTO test = chalet.toDTO();
        double newLongueur = 16.0;
        double expectedNewPosX = (((pos[0] + (dim[0]/2)) * newLongueur) / test.longueur) - (dim[0]/2);
        test.longueur = 16.0;
        chalet.updateChalet(test);
        
        assertEquals(expectedNewPosX, chalet.getAccessoire(porte1DTO.accessoireId).getPosition()[0], 0.0);
        assertEquals(porte2DTO.position[0], chalet.getAccessoire(porte2DTO.accessoireId).getPosition()[0], 0.0);
    }
    
     @Test
    public void updatePorteWithLargeur() {
        Chalet chalet = chaletTest();
        double[] pos = {4.0, 5.0};
        double[] dim = {2.0, 3.0};
        Accessoire porte1 = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Porte, pos, dim);
        porte1.setPosition(pos);
        Accessoire.AccessoireDTO porte1DTO = porte1.toDTO();
        
        Accessoire porte2 = chalet.ajouterAccessoire(TypeMur.Gauche, TypeAccessoire.Porte, pos, dim);
        porte2.setPosition(pos);
        Accessoire.AccessoireDTO porte2DTO = porte2.toDTO();
        
        Chalet.ChaletDTO test = chalet.toDTO();
        double newLargeur = 16.0;
        double expectedNewPosX = (((pos[0] + (dim[0]/2)) * newLargeur) / test.largeur) - (dim[0]/2);
        test.largeur = 16.0;
        chalet.updateChalet(test);
        
        assertEquals(expectedNewPosX, chalet.getAccessoire(porte1DTO.accessoireId).getPosition()[0], 0.0);
        assertEquals(porte2DTO.position[0], chalet.getAccessoire(porte2DTO.accessoireId).getPosition()[0], 0.0);
    }
    
    @Test
    public void updateFenetreWithHauteur() {
        Chalet chalet = chaletTest();
        double[] pos = {2.0, 2.0};
        double[] dim = {2.0, 2.0};
        Accessoire fenetre = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Fenetre, pos, dim);
        fenetre.setPosition(pos);
        Accessoire.AccessoireDTO fenetreDTO = fenetre.toDTO();
        
        Chalet.ChaletDTO test = chalet.toDTO();
        double newHauteur = 16.0;
        double expectedNewPosY = (((pos[1] + (dim[1]/2)) * newHauteur) / test.hauteur) - (dim[1]/2);
        test.hauteur = 16.0;
        chalet.updateChalet(test);
        
        assertEquals(expectedNewPosY, chalet.getAccessoire(fenetreDTO.accessoireId).getPosition()[1], 0.0);
    }
    
    @Test
    public void updateFenetreWithLongueur() {
        Chalet chalet = chaletTest();
        double[] pos = {2.0, 2.0};
        double[] dim = {2.0, 2.0};
        Accessoire fenetre1 = chalet.ajouterAccessoire(TypeMur.Gauche, TypeAccessoire.Fenetre, pos, dim);
        fenetre1.setPosition(pos);
        Accessoire.AccessoireDTO fenetre1DTO = fenetre1.toDTO();
        
        Accessoire fenetre2 = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Fenetre, pos, dim);
        fenetre2.setPosition(pos);
        Accessoire.AccessoireDTO fenetre2DTO = fenetre2.toDTO();
        
        Chalet.ChaletDTO test = chalet.toDTO();
        double newLongueur = 16.0;
        double expectedNewPosX = (((pos[0] + (dim[0]/2)) * newLongueur) / test.longueur) - (dim[0]/2);
        test.longueur = 16.0;
        chalet.updateChalet(test);
        
        assertEquals(expectedNewPosX, chalet.getAccessoire(fenetre1DTO.accessoireId).getPosition()[0], 0.0);
        assertEquals(fenetre2DTO.position[0], chalet.getAccessoire(fenetre2DTO.accessoireId).getPosition()[0], 0.0);
    }
    
    @Test
    public void updateFenetreWithLargeur() {
        Chalet chalet = chaletTest();
        double[] pos = {2.0, 2.0};
        double[] dim = {2.0, 2.0};
        Accessoire fenetre1 = chalet.ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Fenetre, pos, dim);
        fenetre1.setPosition(pos);
        Accessoire.AccessoireDTO fenetre1DTO = fenetre1.toDTO();
        
        Accessoire fenetre2 = chalet.ajouterAccessoire(TypeMur.Gauche, TypeAccessoire.Fenetre, pos, dim);
        fenetre2.setPosition(pos);
        Accessoire.AccessoireDTO fenetre2DTO = fenetre2.toDTO();
        
        Chalet.ChaletDTO test = chalet.toDTO();
        double newLargeur = 16.0;
        double expectedNewPosX = (((pos[0] + (dim[0]/2)) * newLargeur) / test.largeur) - (dim[0]/2);
        test.largeur = 16.0;
        chalet.updateChalet(test);
        
        assertEquals(expectedNewPosX, chalet.getAccessoire(fenetre1DTO.accessoireId).getPosition()[0], 0.0);
        assertEquals(fenetre2DTO.position[0], chalet.getAccessoire(fenetre2DTO.accessoireId).getPosition()[0], 0.0);
    }
    
        // vérifie si les méthodes writeObject et readObject fonctionnent
    @Test
    public void serializableValide() {
        Chalet.ChaletCompletDTO cDTO1 = new Chalet.ChaletCompletDTO(chaletTest());
        
        try {
            String path = System.getProperty("user.dir") + "\\test.txt";
            File fichier = new File(path);
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cDTO1);
            oos.flush();
            oos.close();
            
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Chalet.ChaletCompletDTO cDTO2 = (Chalet.ChaletCompletDTO) ois.readObject();

            ois.close();
            fichier.delete();
            
            assertEquals(cDTO1.nom, cDTO2.nom);
            assertEquals(cDTO1.hauteur, cDTO2.hauteur, 0);
            assertEquals(cDTO1.largeur, cDTO2.largeur, 0);
            assertEquals(cDTO1.longueur, cDTO2.longueur, 0);
            assertEquals(cDTO1.epaisseurMur, cDTO2.epaisseurMur, 0);
            assertEquals(cDTO1.sensToit, cDTO2.sensToit);
            assertEquals(cDTO1.angleToit, cDTO2.angleToit, 0);
            assertEquals(cDTO1.margeAccessoire, cDTO2.margeAccessoire, 0);
            assertEquals(cDTO1.margeSupplementaireRetrait, cDTO2.margeSupplementaireRetrait, 0);
        }
        
        catch (IOException | ClassNotFoundException e) {
            // jsp
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
