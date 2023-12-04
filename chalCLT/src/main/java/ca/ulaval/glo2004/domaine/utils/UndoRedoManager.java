package ca.ulaval.glo2004.domaine.utils;

import java.util.List;
import java.util.Stack;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.ChalCLTProjet;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;


public class UndoRedoManager {
    private static class ProjetState {
        private String nom;
        private Chalet.ChaletDTO chalet;
        private List<Accessoire.AccessoireDTO> accessoires;
        private PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur;

        public ProjetState(ChalCLTProjet projet) {
            this.nom = projet.getNom();
            this.chalet = projet.getChalet().toDTO();
            this.accessoires = projet.getChalet().getAccessoireDTOs();
            this.preferencesUtilisateur = projet.getPreferencesUtilisateur().toDTO();
        }

        public String getNom() {
            return nom;
        }

        public Chalet.ChaletDTO getChalet() {
            return chalet;
        }

        public List<Accessoire.AccessoireDTO> getAccessoires() {
            return accessoires;
        }

        public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
            return preferencesUtilisateur;
        }
    }

    private Stack<ProjetState> undoStack = new Stack<>();
    private Stack<ProjetState> redoStack = new Stack<>();

    public int getUndoStackSize() {
        return undoStack.size();
    }

    public int getRedoStackSize() {
        return redoStack.size();
    }

    public void saveState(ChalCLTProjet projet) {
        undoStack.push(new ProjetState(projet));
        redoStack.clear();
    }

    public void undo(ChalCLTProjet projet) {
        if (undoStack.isEmpty()) {
            return;
        }

        redoStack.push(new ProjetState(projet));
        ProjetState sauvegardeDTO = undoStack.pop();
        applyChange(projet, sauvegardeDTO);
    }

    public void redo(ChalCLTProjet projet) {
        if (redoStack.isEmpty()) {
            return;
        }

        undoStack.push(new ProjetState(projet));
        ProjetState sauvegardeDTO = redoStack.pop();
        applyChange(projet, sauvegardeDTO);
    }

    private void applyChange(ChalCLTProjet projet, ProjetState sauvegardeDTO) {
        projet.setNom(sauvegardeDTO.getNom());
        projet.setPreferencesUtilisateur(new PreferencesUtilisateur(sauvegardeDTO.getPreferencesUtilisateur()));

        Chalet newChalet = new Chalet(sauvegardeDTO.getChalet());
        for (Accessoire.AccessoireDTO accessoireDTO : sauvegardeDTO.getAccessoires()) {
            newChalet.getMur(accessoireDTO.typeMur).ajouterAccessoire(new Accessoire(accessoireDTO));
        }
        
        projet.setChalet(newChalet);
    }
}
