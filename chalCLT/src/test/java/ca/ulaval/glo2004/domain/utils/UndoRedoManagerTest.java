package ca.ulaval.glo2004.domain.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.ChalCLTProjet;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.utils.UndoRedoManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class UndoRedoManagerTest {
    private UndoRedoManager manager;
    private ChalCLTProjet projet;

    @Before
    public void setup() {
        // Initialize a project and undo/redo manager
        manager = new UndoRedoManager();
        projet = new ChalCLTProjet("Test");
    }

    @Test
    public void undoRedoHauteurLargeurChaletValide() {
        assertEquals(0, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);

        // Make some changes to projet
        projet.getChalet().setHauteur(80);
        projet.getChalet().setLargeur(80);

        assertEquals(80, projet.getChalet().getHauteur(), 0);
        assertEquals(80, projet.getChalet().getLargeur(), 0);

        // Save the state
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));

        // Check that the state was saved
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);

        // Make some more changes to projet
        projet.getChalet().setHauteur(70);
        projet.getChalet().setLargeur(70);

        // Check that the changes were made
        assertEquals(70, projet.getChalet().getHauteur(), 0);
        assertEquals(70, projet.getChalet().getLargeur(), 0);
        
        // Undo the changes
        manager.undo(projet);

        // Check that the state was popped from the undo stack and pushed to the redo stack
        assertEquals(0, manager.getUndoStackSize(), 0);
        assertEquals(1, manager.getRedoStackSize(), 0);

        // Check that the changes were undone
        assertEquals(80, projet.getChalet().getHauteur(), 0);
        assertEquals(80, projet.getChalet().getLargeur(), 0);

        // Redo the changes
        manager.redo(projet);

        // Check that the state was popped from the redo stack and pushed to the undo stack
        assertEquals(1, manager.getUndoStackSize());
        assertEquals(0, manager.getRedoStackSize());

        // Check that the changes were redone
        assertEquals(70, projet.getChalet().getHauteur(), 0);
        assertEquals(70, projet.getChalet().getLargeur(), 0);
    }
    
    @Test
    public void changeNomValide() {
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        projet.setNom("LeNomAChange");
        
        assertEquals(1, manager.getUndoStackSize());
        assertEquals(0, manager.getRedoStackSize());
        
        manager.undo(projet);
        
        assertEquals("Test", projet.getChalet().getNom());
        assertEquals(0, manager.getUndoStackSize());
        assertEquals(1, manager.getRedoStackSize());
    }
    
    @Test
    public void undoRedoAccessoireValide() {
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {2.0, 3.0};
        double[] dim = {1.0,2.0};
        
        projet.getChalet().ajouterAccessoire(typeMur, typeAccessoire, pos, dim);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(1, projet.getChalet().getAccessoires().size(), 0);
        
        manager.undo(projet);
        
        assertEquals(0, manager.getUndoStackSize(), 0);
        assertEquals(1, manager.getRedoStackSize(), 0);
        assertTrue(projet.getChalet().getAccessoires().isEmpty());
        
        manager.redo(projet);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(1, projet.getChalet().getAccessoires().size(), 0);
        
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        typeAccessoire = TypeAccessoire.Fenetre;
        typeMur = TypeMur.Droit;
        
        projet.getChalet().ajouterAccessoire(typeMur, typeAccessoire, pos, dim);
        assertEquals(2, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(2, projet.getChalet().getAccessoires().size(), 0);
        assertEquals(1, projet.getChalet().getAccessoires(TypeMur.Facade).size(), 0);
        assertEquals(1, projet.getChalet().getAccessoires(TypeMur.Droit).size(), 0);
        
        manager.undo(projet);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(1, manager.getRedoStackSize(), 0);
        assertEquals(1, projet.getChalet().getAccessoires().size(), 0);
        assertEquals(1, projet.getChalet().getAccessoires(TypeMur.Facade).size(), 0);
        assertTrue(projet.getChalet().getAccessoires(TypeMur.Droit).isEmpty());
    }
    
    @Test
    public void undoRedoToitEtEpaisseurValide() {
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        projet.getChalet().setAngleToit(60.0);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(60.0, projet.getChalet().getAngleToit(), 0);
        
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        projet.getChalet().setSensToit(TypeSensToit.Est);
        
        assertEquals(2, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(TypeSensToit.Est, projet.getChalet().getSensToit());
        
        manager.undo(projet);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(1, manager.getRedoStackSize(), 0);
        assertEquals(TypeSensToit.Nord, projet.getChalet().getSensToit());
        
        manager.redo(projet);
        
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        projet.getChalet().setEpaisseurMur(10.0);
        
        assertEquals(3, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(10.0, projet.getChalet().getEpaisseurMur(), 0);
        
        manager.undo(projet);
        manager.undo(projet);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(2, manager.getRedoStackSize(), 0);
        assertEquals(60.0, projet.getChalet().getAngleToit(), 0);
        assertEquals(TypeSensToit.Nord, projet.getChalet().getSensToit());
        assertEquals(12.0, projet.getChalet().getEpaisseurMur(), 0);
    }
    
    @Test
    public void undoRedoLargeurChaletAvecAccessoires() {
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {32.0, 3.0};
        double[] dim = {32.0, 60.0};
        
        Accessoire porte = projet.getChalet().ajouterAccessoire(typeMur, typeAccessoire, pos, dim);
        porte.setPosition(pos);
        UUID porteId = porte.getAccessoireId();
        projet.getChalet().getMur(typeMur).updateValiditeAccessoires(projet.getChalet().getMargeAccessoire(),
                projet.getChalet().getEpaisseurMur());
        double posPorteExpected = projet.getChalet().getAccessoire(porteId).getPosition()[0];
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(1, projet.getChalet().getAccessoires().size(), 0);
        
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        projet.getChalet().setLargeur(144.0);
        projet.getChalet().updateChalet(projet.getChalet().toDTO());
        
        assertEquals(2, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(posPorteExpected, projet.getChalet().getAccessoire(porteId).getPosition()[0], 0);
        
        manager.undo(projet);
        
        assertEquals(1, manager.getUndoStackSize(), 0);
        assertEquals(1, manager.getRedoStackSize(), 0);
        assertEquals(posPorteExpected, projet.getChalet().getAccessoire(porteId).getPosition()[0], 0);
    }
    
    @Test
    public void undoRedoHauteurChaletAvecAccessoires() {
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        TypeAccessoire typeAccessoire = TypeAccessoire.Porte;
        TypeMur typeMur = TypeMur.Facade;
        double[] pos = {15.0, 3.0};
        double[] dim = {32.0, 60.0};
        
        Accessoire porte = projet.getChalet().ajouterAccessoire(typeMur, typeAccessoire, pos, dim);
        porte.setPosition(pos);
        UUID porteId = porte.getAccessoireId();
        
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        typeAccessoire = TypeAccessoire.Fenetre;
        double[] posF = {36.0, 36.0};
        double[] dimF = {20.0, 20.0};
        
        Accessoire fenetre = projet.getChalet().ajouterAccessoire(typeMur, typeAccessoire, posF, dimF);
        fenetre.setPosition(posF);
        UUID fenetreId = fenetre.getAccessoireId();
        
        projet.getChalet().getMur(typeMur).updateValiditeAccessoires(projet.getChalet().getMargeAccessoire(),
                projet.getChalet().getEpaisseurMur());
        double posPorteExpected = projet.getChalet().getAccessoire(porteId).getPosition()[1];
        double posFenetreExpected = projet.getChalet().getAccessoire(fenetreId).getPosition()[1];
        
        assertEquals(2, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertEquals(2, projet.getChalet().getAccessoires().size(), 0);
        
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
        
        projet.getChalet().setHauteur(144.0);
        projet.getChalet().updateChalet(projet.getChalet().toDTO());
        
        assertEquals(3, manager.getUndoStackSize(), 0);
        assertEquals(0, manager.getRedoStackSize(), 0);
        assertNotEquals(posPorteExpected, projet.getChalet().getAccessoire(porteId).getPosition()[1], 0);
        assertEquals(posFenetreExpected, projet.getChalet().getAccessoire(fenetreId).getPosition()[1], 0);
        
        manager.undo(projet);
        
        assertEquals(2, manager.getUndoStackSize(), 0);
        assertEquals(1, manager.getRedoStackSize(), 0);
        assertEquals(posPorteExpected, projet.getChalet().getAccessoire(porteId).getPosition()[1], 0);
        assertEquals(posFenetreExpected, projet.getChalet().getAccessoire(fenetreId).getPosition()[1], 0);
    }
    
    @Test
    public void sauvegardeUndoRedoValide() {
        Chalet.ChaletCompletDTO cDTO1 = new Chalet.ChaletCompletDTO(projet.getChalet());
        
        try {            
            String path = System.getProperty("user.dir") + "\\test.txt";
            File fichier = new File(path);
            FileOutputStream fos = new FileOutputStream(fichier);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cDTO1);
            oos.flush();
            oos.close();
            
            projet.getChalet().setLargeur(192.0);
            projet.getChalet().setLongueur(144.0);
            projet.getChalet().setHauteur(120.0);
            projet.getChalet().setNom("Sauvegarde");
        
            double[] pos = {2.0, 3.0};
            double[] dim = {10.0, 12.0};
            projet.getChalet().ajouterAccessoire(TypeMur.Facade, TypeAccessoire.Porte, pos, dim);
            projet.getChalet().ajouterAccessoire(TypeMur.Droit, TypeAccessoire.Fenetre, pos, dim);
            projet.getChalet().setAngleToit(20.0);
            projet.getChalet().setSensToit(TypeSensToit.Est);
        
            manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));
            
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Chalet.ChaletCompletDTO cDTO2 = (Chalet.ChaletCompletDTO) ois.readObject();
            projet.setChalet(new Chalet(cDTO2));
            ois.close();
            fichier.delete();
            
            assertEquals(120.0, projet.getChalet().getLargeur(), 0);
            assertEquals(120.0, projet.getChalet().getLongueur(), 0);
            assertEquals(96.0, projet.getChalet().getHauteur(), 0);
            assertEquals(15.0, projet.getChalet().getAngleToit(), 0);
            assertEquals(TypeSensToit.Nord, projet.getChalet().getSensToit());
            assertEquals(0, projet.getChalet().getAccessoires().size(), 0);
            assertEquals("Test", projet.getChalet().getNom());
            
            manager.undo(projet);
            
            assertEquals(192.0, projet.getChalet().getLargeur(), 0);
            assertEquals(144.0, projet.getChalet().getLongueur(), 0);
            assertEquals(120.0, projet.getChalet().getHauteur(), 0);
            assertEquals(20.0, projet.getChalet().getAngleToit(), 0);
            assertEquals(TypeSensToit.Est, projet.getChalet().getSensToit());
            assertEquals(2, projet.getChalet().getAccessoires().size(), 0);
            assertEquals("Sauvegarde", projet.getChalet().getNom());
            
            manager.redo(projet);
            
            assertEquals(120.0, projet.getChalet().getLargeur(), 0);
            assertEquals(120.0, projet.getChalet().getLongueur(), 0);
            assertEquals(96.0, projet.getChalet().getHauteur(), 0);
            assertEquals(15.0, projet.getChalet().getAngleToit(), 0);
            assertEquals(TypeSensToit.Nord, projet.getChalet().getSensToit());
            assertEquals(0, projet.getChalet().getAccessoires().size(), 0);
            assertEquals("Test", projet.getChalet().getNom());
        }
        catch (IOException | ClassNotFoundException e) {
            // jsp
            e.printStackTrace();
            assertTrue(false);
        }   
    }
}