package ca.ulaval.glo2004.domaine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.ulaval.glo2004.domaine.utils.UndoRedoManager;
import java.util.stream.Collectors;

public class Controleur extends ControleurEventSupport {
    private static Controleur instance = null;
    private ChalCLTProjet projectActif;
    // private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private UndoRedoManager undoRedoManager = new UndoRedoManager();

    private Controleur() {
        this.projectActif = new ChalCLTProjet(null);
        // this.undoRedoManager = new UndoRedoManager();
    }

    public static Controleur getInstance() {
        if (instance == null) {
            instance = new Controleur();
        }
        return instance;
    }

    public Chalet.ChaletDTO getChalet() {
        return projectActif.getChalet().toDTO(); /* Return DTO */
    }

    public Accessoire.AccessoireDTO getAccessoire(UUID uuid) {
        for (Mur mur : this.projectActif.getChalet().getMurs()) {
            for (Accessoire accessoire : mur.getAccessoires()) {
                if (accessoire.getAccessoireId().equals(uuid)) {
                    return accessoire.toDTO();
                }
            }
        }
        return null;
    }

    public List<Accessoire.AccessoireDTO> getAccessoires() {
        List<Accessoire.AccessoireDTO> accessoires = new ArrayList<>();
        for (Mur mur : this.projectActif.getChalet().getMurs()) {
            for (Accessoire accessoire : mur.getAccessoires()) {
                accessoires.add(accessoire.toDTO());
            }
        }

        return accessoires;
    }

    public List<Accessoire.AccessoireDTO> getAccessoires(TypeMur typeMur) {
        Mur mur = this.projectActif.getChalet().getMur(typeMur);
        List<Accessoire.AccessoireDTO> accessoires = new ArrayList<>();
        for (Accessoire accessoire : mur.getAccessoires()) {
            accessoires.add(accessoire.toDTO());
        }

        return accessoires;
    }

    public void setChalet(Chalet.ChaletDTO chalet) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));

        projectActif.getChalet().updateChalet(chalet);
        Chalet.ChaletDTO chaletDTO = this.projectActif.getChalet().toDTO();
        // this.pcs.firePropertyChange(EventType.CHALET, chaletDTO, chalet);
        this.dispatchChaletChangeEvent(chaletDTO);
    }

    public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur().toDTO();
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        // this.pcs.firePropertyChange(EventType.PREFERENCES_UTILISATEUR, this.getPreferencesUtilisateur(), preferencesUtilisateur);
        projectActif.getPreferencesUtilisateur().update(preferencesUtilisateur);
        this.dispatchUserPreferencesChangeEvent(preferencesUtilisateur);
    }

    public void setAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().updateAccessoire(accessoireDTO);

        // this.pcs.firePropertyChange(EventType.ACCESSOIRE, accessoireDTO, accessoire.toDTO());
        this.dispatchAccessoireChangeEvent(accessoire.toDTO());

        this.dispatchAccessoirValidityChangeEvent(getAccessoires(accessoire.getTypeMur()));
    }

    public void ajouterAccessoire(TypeMur typeMur, TypeAccessoire typeAcc, double[] position, double[] dimension) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().ajouterAccessoire(typeMur, typeAcc, position, dimension);

        this.dispatchAccessoireAddEvent(accessoire.toDTO());
        this.dispatchAccessoirValidityChangeEvent(getAccessoires(accessoire.getTypeMur()));
        
    }

    public void retirerAccessoire(TypeMur typeMur, UUID uuid) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().retirerAccessoire(typeMur, uuid);
        this.dispatchAccessoireRemoveEvent(accessoire.toDTO());
        // this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoire.toDTO());
    }

    public void retirerAccessoire(UUID uuid) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().retirerAccessoire(uuid);
        this.dispatchAccessoireRemoveEvent(accessoire.toDTO());
        // this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoire.toDTO());
    }

    public void retirerAccessoires(List<Accessoire.AccessoireDTO> accessoires) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        List<Accessoire> removed = this.projectActif.getChalet().retirerAccessoires(accessoires);
        List<Accessoire.AccessoireDTO> removedDTOs = removed.stream().map((accessoire) -> accessoire.toDTO()).collect(Collectors.toList());

        for (Accessoire.AccessoireDTO accessoireDTO : removedDTOs) {
            this.dispatchAccessoireRemoveEvent(accessoireDTO);
            // this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoireDTO);
        }

        // this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRES, null, removedDTOs);
    }

    public void creeProjet() {
        if (this.projectActif != null) {
            fermerProjet();
        }
        
        this.projectActif = new ChalCLTProjet(null, new Chalet(), this.projectActif.getPreferencesUtilisateur());
        // this.pcs.firePropertyChange(EventType.CREE_PROJET, null, this.getChalet());
        this.dispatchProjectCreateEvent(this.projectActif.getChalet().getNom());
    }

    public void fermerProjet() {
        // TODO

    }

    public void undo() {
        this.undoRedoManager.undo(this.projectActif);
        // this.dispatchChaletChangeEvent(getChalet());
        this.dispatchUndoEvent(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
    }

    public void redo() {
        this.undoRedoManager.redo(this.projectActif);
        // this.dispatchChaletChangeEvent(getChalet());
        this.dispatchRedoEvent(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
    }
}

