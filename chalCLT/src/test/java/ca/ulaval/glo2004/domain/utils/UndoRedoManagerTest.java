package ca.ulaval.glo2004.domain.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo2004.domaine.ChalCLTProjet;
import ca.ulaval.glo2004.domaine.utils.UndoRedoManager;

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
    public void basicTest() {
        assertEquals(0, manager.getUndoStackSize());
        assertEquals(0, manager.getRedoStackSize());

        // Make some changes to projet
        projet.getChalet().setHauteur(80);
        projet.getChalet().setLargeur(80);

        assertEquals(80, projet.getChalet().getHauteur(), 0);
        assertEquals(80, projet.getChalet().getLargeur(), 0);

        // Save the state
        manager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projet));

        // Check that the state was saved
        assertEquals(1, manager.getUndoStackSize());
        assertEquals(0, manager.getRedoStackSize());

        // Make some more changes to projet
        projet.getChalet().setHauteur(70);
        projet.getChalet().setLargeur(70);

        // Check that the changes were made
        assertEquals(70, projet.getChalet().getHauteur(), 0);
        assertEquals(70, projet.getChalet().getLargeur(), 0);
        
        // Undo the changes
        manager.undo(projet);

        // Check that the state was popped from the undo stack and pushed to the redo stack
        assertEquals(0, manager.getUndoStackSize());
        assertEquals(1, manager.getRedoStackSize());

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
}