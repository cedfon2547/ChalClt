package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PreferencesUtilisateurTest {
    
    @Test
    public void ConstructeurPrefUtil() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        
        assertTrue(test.getAfficherGrille() || !(test.getAfficherVoisinSelection()));
        assertEquals(50, test.getGridSpacing(), 0);
    }
    
    @Test
    public void ConstructeurAltPrefUtil() {
        PreferencesUtilisateur test = new PreferencesUtilisateur(true, true);
        
        assertTrue(test.getAfficherGrille() && test.getAfficherVoisinSelection());
        assertEquals(50, test.getGridSpacing(), 0);
    }
    
    @Test
    public void SetAfficherGrilleTrue() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setAfficherGrille(true);
        
        assertTrue(test.getAfficherGrille());
    }
    
    @Test
    public void SetAfficherVoisinTrue() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setAfficherVoisinSelection(true);
        
        assertTrue(test.getAfficherVoisinSelection());
    }
    
    @Test
    public void SetAfficherGrilleFalse() {
        PreferencesUtilisateur test = new PreferencesUtilisateur(true, true);
        test.setAfficherGrille(false);
        
        assertTrue(!(test.getAfficherGrille()));
    }
    
    @Test
    public void SetAfficherVoisinFalse() {
        PreferencesUtilisateur test = new PreferencesUtilisateur(true, true);
        test.setAfficherVoisinSelection(false);
        
        assertTrue(!(test.getAfficherVoisinSelection()));
    }
    
    @Test
    public void SetGridSpace() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setGridSpacing(80);
        
        assertEquals(80, test.getGridSpacing(), 0);
    }
    
    @Test
    public void ConstructeurDTO() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        PreferencesUtilisateur.PreferencesUtilisateurDTO testDTO = new 
        PreferencesUtilisateur.PreferencesUtilisateurDTO(test);
        
        assertEquals(test.getAfficherGrille(), testDTO.afficherGrille);
        assertEquals(test.getAfficherVoisinSelection(), testDTO.afficherVoisinSelection);
        assertEquals(test.getGridSpacing(), testDTO.gridSpacing, 0);      
    }
    
    @Test
    public void ToDTO() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        PreferencesUtilisateur.PreferencesUtilisateurDTO testDTO = test.toDTO();
        
        assertEquals(test.getAfficherGrille(), testDTO.afficherGrille);
        assertEquals(test.getAfficherVoisinSelection(), testDTO.afficherVoisinSelection);
        assertEquals(test.getGridSpacing(), testDTO.gridSpacing, 0);
    }
    
    @Test
    public void Update() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        PreferencesUtilisateur modele = new PreferencesUtilisateur(true, true);
        modele.setGridSpacing(80);
        PreferencesUtilisateur.PreferencesUtilisateurDTO modeleDTO = modele.toDTO();
        test.update(modeleDTO);
        
        assertEquals(modele.getAfficherGrille(), test.getAfficherGrille());
        assertEquals(modele.getAfficherVoisinSelection(), test.getAfficherVoisinSelection());
        assertEquals(modele.getGridSpacing(), test.getGridSpacing(), 0);
    }
    
    // vérifie si les méthodes writeObject et readObject fonctionnent
    @Test
    public void serializableValide() {
        PreferencesUtilisateur test = new PreferencesUtilisateur();
        test.setAfficherGrille(false);
        test.setGridSpacing(25);
        test.setAfficherPlancher(true);
        PreferencesUtilisateur.PreferencesUtilisateurDTO pDTO1 = test.toDTO();
        
        try {
            String path = System.getProperty("user.dir") + "\\test.txt";
            File fichier = new File(path);
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(pDTO1);
            oos.flush();
            oos.close();
            
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            PreferencesUtilisateur.PreferencesUtilisateurDTO pDTO2 = (PreferencesUtilisateur.PreferencesUtilisateurDTO) ois.readObject();

            ois.close();
            fichier.delete();
            
            assertEquals(pDTO1.afficherGrille, pDTO2.afficherGrille);
            assertEquals(pDTO1.afficherPlancher, pDTO2.afficherPlancher);
            assertEquals(pDTO1.afficherVoisinSelection, pDTO2.afficherVoisinSelection);
            assertEquals(pDTO1.gridSpacing, pDTO2.gridSpacing, 0);
        }
        
        catch (IOException | ClassNotFoundException e) {
            // jsp
            e.printStackTrace();
            assertTrue(false);
        }
    }    
}
