package ca.ulaval.glo2004.domaine;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.ulaval.glo2004.domaine.utils.UndoRedoManager;
import java.util.stream.Collectors;

public class Controleur {
    private static Controleur instance = null;
    private ChalCLTProjet projectActif;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
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
        Chalet.ChaletDTO chaletDTO = this.projectActif.getChalet().toDTO();
        this.pcs.firePropertyChange(EventType.CHALET, chaletDTO, chalet);

        projectActif.getChalet().updateChalet(chalet);
    }

    public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur().toDTO();
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur) {
        this.pcs.firePropertyChange(EventType.PREFERENCES_UTILISATEUR, this.getPreferencesUtilisateur(), preferencesUtilisateur);
        projectActif.getPreferencesUtilisateur().update(preferencesUtilisateur);
    }

    public void setAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        Accessoire accessoire = this.projectActif.getChalet().updateAccessoire(accessoireDTO);

        this.pcs.firePropertyChange(EventType.ACCESSOIRE, accessoireDTO, accessoire.toDTO());

        if (!accessoire.isValide()) {
            this.pcs.firePropertyChange(EventType.ACCESSOIRE_INVALIDE, accessoireDTO.valide, accessoire.getValide());
        }
    }

    public void ajouterAccessoire(TypeMur typeMur, TypeAccessoire typeAcc, double[] position, double[] dimension) {
        Accessoire accessoire = this.projectActif.getChalet().ajouterAccessoire(typeMur, typeAcc, position, dimension);

        this.pcs.firePropertyChange(EventType.AJOUTER_ACCESSOIRE, null, accessoire.toDTO());

        if (!accessoire.isValide()) {
            this.pcs.firePropertyChange(EventType.ACCESSOIRE_INVALIDE, false, true);
        }
    }

    public void retirerAccessoire(TypeMur typeMur, UUID uuid) {
        Accessoire accessoire = this.projectActif.getChalet().retirerAccessoire(typeMur, uuid);
        this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoire.toDTO());
    }

    public void retirerAccessoire(UUID uuid) {
        Accessoire accessoire = this.projectActif.getChalet().retirerAccessoire(uuid);
        this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoire.toDTO());
    }

    public void retirerAccessoires(List<Accessoire.AccessoireDTO> accessoires) {
        List<Accessoire> removed = this.projectActif.getChalet().retirerAccessoires(accessoires);
        List<Accessoire.AccessoireDTO> removedDTOs = removed.stream().map((accessoire) -> accessoire.toDTO()).collect(Collectors.toList());

        this.pcs.firePropertyChange(EventType.SUPPRIMER_ACCESSOIRES, null, removedDTOs);
    }

    public void creeProjet() {
        if (this.projectActif != null) {
            fermerProjet();
        }
        
        this.projectActif = new ChalCLTProjet(null, new Chalet(), this.projectActif.getPreferencesUtilisateur());
        this.pcs.firePropertyChange(EventType.CREE_PROJET, null, this.getChalet());
    }

    public void fermerProjet() {
        // TODO

    }

    public void undo() {
        this.undoRedoManager.undo();
    }

    public void redo() {
        this.undoRedoManager.redo();
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(property, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public static class EventType {
        public static final String CHALET = "chalet";
        public static final String ACCESSOIRE = "accessoire";
        public static final String AJOUTER_ACCESSOIRE = "ajouterAccessoire";
        public static final String RETIRER_ACCESSOIRE = "supprimerAccessoire";
        public static final String SUPPRIMER_ACCESSOIRES = "supprimerAccessoires";
        public static final String CREE_PROJET = "creeProjet";
        public static final String FERMER_PROJET = "fermerProjet";
        public static final String ACCESSOIRE_INVALIDE = "accessoireInvalide";
        public static final String PREFERENCES_UTILISATEUR = "preferencesUtilisateur";
    }
}
