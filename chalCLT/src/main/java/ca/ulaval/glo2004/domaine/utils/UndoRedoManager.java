package ca.ulaval.glo2004.domaine.utils;

import java.util.Stack;

import ca.ulaval.glo2004.domaine.ChalCLTProjet;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;


public class UndoRedoManager {
    public static class ProjetState {
        private String nom;
        private Chalet.ChaletCompletDTO chalet;
        private PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur;

        public ProjetState(ChalCLTProjet.ChalCLTProjetDTO projet) {
            this.nom = projet.nom;
            this.chalet = projet.chalet;
            this.preferencesUtilisateur = projet.preferencesUtilisateur;

        }

        public String getNom() {
            return nom;
        }

        public Chalet.ChaletCompletDTO getChalet() {
            return chalet;
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

    public void saveState(ChalCLTProjet.ChalCLTProjetDTO projet) {
        undoStack.push(new ProjetState(projet));
        redoStack.clear();
    }

    public void undo(ChalCLTProjet projet) {
        if (undoStack.isEmpty()) {
            return;
        }

        redoStack.push(new ProjetState(new ChalCLTProjet.ChalCLTProjetDTO(projet)));
        ProjetState sauvegardeDTO = undoStack.pop();
        applyChange(projet, sauvegardeDTO);
    }

    public void redo(ChalCLTProjet projet) {
        if (redoStack.isEmpty()) {
            return;
        }

        undoStack.push(new ProjetState(new ChalCLTProjet.ChalCLTProjetDTO(projet)));
        ProjetState sauvegardeDTO = redoStack.pop();
        applyChange(projet, sauvegardeDTO);
    }

    private void applyChange(ChalCLTProjet projet, ProjetState sauvegardeDTO) {
        projet.setNom(sauvegardeDTO.getNom());
        projet.setPreferencesUtilisateur(new PreferencesUtilisateur(sauvegardeDTO.getPreferencesUtilisateur()));

        Chalet newChalet = new Chalet(sauvegardeDTO.getChalet());
        // for (Mur.MurDTO murDTO: sauvegardeDTO.getChalet().murs) {
        //     Mur mur = newChalet.getMur(murDTO.type);
        //     for (Accessoire.AccessoireDTO accessoireDTO: murDTO.accessoires) {
        //         mur.ajouterAccessoire(new Accessoire(accessoireDTO));
        //     }
        // }
        
        projet.setChalet(newChalet);
    }
}
