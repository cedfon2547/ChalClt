package ca.ulaval.glo2004.domaine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import ca.ulaval.glo2004.domaine.utils.UndoRedoManager;
import java.util.stream.Collectors;

/**
 * Contrôleur selon le principe de Larman
 * Suit le patron Singleton
 */
public class Controleur extends ControleurEventSupport {
    private static Controleur instance = null;
    private ChalCLTProjet projectActif;
    // private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private UndoRedoManager undoRedoManager = new UndoRedoManager();

    private Controleur() {
        this.projectActif = new ChalCLTProjet();
        // this.undoRedoManager = new UndoRedoManager();
    }

    /**
    * Retourne l'instance unique du contrôleur
    * @return Instance unique du contrôleur
    */
    public static Controleur getInstance() {
        if (instance == null) {
            instance = new Controleur();
        }
        return instance;
    }

    /**
    * Retourne le chalet associé au projet ouvert
    * @return Chalet associé au projet ouvert en format DTO
    */
    public Chalet.ChaletDTO getChalet() {
        return projectActif.getChalet().toDTO(); /* Return DTO */
    }

    /**
    * Retourne l'accessoire spécifié
    * @param uuid ID de l'accessoire
    * @return Accessoire spécifié en format DTO
    */
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

    /**
    * Retourne la liste de tous les accessoires du chalet
    * @return Liste de tous les accessoires du chalet en format DTO
    */
    public List<Accessoire.AccessoireDTO> getAccessoires() {
        List<Accessoire.AccessoireDTO> accessoires = new ArrayList<>();
        for (Mur mur : this.projectActif.getChalet().getMurs()) {
            for (Accessoire accessoire : mur.getAccessoires()) {
                accessoires.add(accessoire.toDTO());
            }
        }

        return accessoires;
    }

    /**
    * Retourne la liste de tous les accessoires contenus sur le mur spécifié du chalet
    * @param typeMur Mur dont on veut la liste d'accessoires
    * @return Liste de tous les accessoires placés sur le mur en format DTO
    */
    public List<Accessoire.AccessoireDTO> getAccessoires(TypeMur typeMur) {
        Mur mur = this.projectActif.getChalet().getMur(typeMur);
        List<Accessoire.AccessoireDTO> accessoires = new ArrayList<>();
        for (Accessoire accessoire : mur.getAccessoires()) {
            accessoires.add(accessoire.toDTO());
        }

        return accessoires;
    }

    /**
    * Modifie le chalet du projet actif pour qu'il soit identique au chalet spécifié
    * @param chalet Nouveau chalet produit en format DTO
    */
    public void setChalet(Chalet.ChaletDTO chalet) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));

        projectActif.getChalet().updateChalet(chalet);
        Chalet.ChaletDTO chaletDTO = this.projectActif.getChalet().toDTO();
        // this.pcs.firePropertyChange(EventType.CHALET, chaletDTO, chalet);
        this.dispatchChaletChangeEvent(chaletDTO);
    }

    /**
    * Retourne les préférences de l'utilisateur
    * @return Préférences de l'utilisateur en format DTO
    */
    public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur().toDTO();
    }

    /**
    * Modifie les préférences de l'utilisateur
    * @param preferencesUtilisateur Nouvelles préférences de l'utilisateur en format DTO
    */
    public void setPreferencesUtilisateur(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        // this.pcs.firePropertyChange(EventType.PREFERENCES_UTILISATEUR, this.getPreferencesUtilisateur(), preferencesUtilisateur);
        projectActif.getPreferencesUtilisateur().update(preferencesUtilisateur);
        this.dispatchUserPreferencesChangeEvent(preferencesUtilisateur);
    }

    /**
    * Modifie un accessoire
    * @param accessoireDTO Nouvel accessoire à modifier en format DTO
    */
    public void setAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().updateAccessoire(accessoireDTO);

        // this.pcs.firePropertyChange(EventType.ACCESSOIRE, accessoireDTO, accessoire.toDTO());
        this.dispatchAccessoireChangeEvent(accessoire.toDTO());

        this.dispatchAccessoirValidityChangeEvent(getAccessoires(accessoire.getTypeMur()));
    }

    /**
    * Ajoute un accessoire au chalet du projet actif
    * @param typeMur Mur sur lequel on souhaite ajouter l'accessoire
    * @param typeAcc Type de l'accessoire à ajouter
    * @param position Position de l'accessoire à ajouter sur le mur
    * @param dimension Dimensions de l'accessoire à ajouter
    */
    public void ajouterAccessoire(TypeMur typeMur, TypeAccessoire typeAcc, double[] position, double[] dimension) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().ajouterAccessoire(typeMur, typeAcc, position, dimension);

        this.dispatchAccessoireAddEvent(accessoire.toDTO());
        this.dispatchAccessoirValidityChangeEvent(getAccessoires(accessoire.getTypeMur()));
        
    }

    /**
    * Retire un accessoire du chalet du projet actif
    * @param typeMur Mur sur lequel est placé l'accessoire à retirer
    * @param uuid ID de l'accessoire à retirer
    */
    public void retirerAccessoire(TypeMur typeMur, UUID uuid) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().retirerAccessoire(typeMur, uuid);
        this.dispatchAccessoireRemoveEvent(accessoire.toDTO());
        // this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoire.toDTO());
    }

    /**
    * Retire un accessoire du chalet du projet actif
    * @param uuid ID de l'accessoire à retirer
    */
    public void retirerAccessoire(UUID uuid) {
        this.undoRedoManager.saveState(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
        Accessoire accessoire = this.projectActif.getChalet().retirerAccessoire(uuid);
        this.dispatchAccessoireRemoveEvent(accessoire.toDTO());
        // this.pcs.firePropertyChange(EventType.RETIRER_ACCESSOIRE, null, accessoire.toDTO());
    }

    /**
    * Retire une liste d'accessoires du chalet du projet actif
    * @param accessoires Liste d'accessoires à retirer en format DTO
    */
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

    /**
    * Créer un nouveau projet par défaut
    */
    public void creeProjet() {
        if (this.projectActif != null) {
            fermerProjet();
        }
        
        this.projectActif = new ChalCLTProjet(null, new Chalet(), this.projectActif.getPreferencesUtilisateur());
        // this.pcs.firePropertyChange(EventType.CREE_PROJET, null, this.getChalet());
        this.dispatchProjectCreateEvent(this.projectActif.getChalet().getNom());
    }

    /**
    * Ferme le projet actif
    */
    public void fermerProjet() {
        // TODO

    }
    
    /**
    * Crée un fichier de sauvegarde pour le projet actif
    * @param path Emplacement du fichier de sauvegarde sur l'ordinateur
    */
    public void creerSauvegarde(String path) {
        try {
            if (!path.endsWith(".chalclt")) {
                path += ".chalclt";
            }
            
            File f = new File(path);
            FileOutputStream fichier = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(this.projectActif);
            oos.flush();
            oos.close();
        }
        catch (IOException e) {
            // jsp
            e.printStackTrace();
        }
    }
    
    /**
    * Ouvre un fichier de sauvegarde existant et le définit comme projet actif
    * @param path Emplacement du fichier de sauvegarde sur l'ordinateur
    */
    public void ouvrirSauvegarde(String path) {
        try {
            File f = new File(path);
            FileInputStream fichier = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fichier);
            ChalCLTProjet aOuvrir = (ChalCLTProjet) ois.readObject();

            this.projectActif = aOuvrir;
            ois.close();

            this.dispatchChaletChangeEvent(this.projectActif.getChalet().toDTO());
        }
        catch (IOException | ClassNotFoundException e) {
            // jsp
            e.printStackTrace();
        }
    }

    /**
    * Annule la dernière action effectuée par l'utilisateur
    */
    public void undo() {
        this.undoRedoManager.undo(this.projectActif);
        // this.dispatchChaletChangeEvent(getChalet());
        this.dispatchUndoEvent(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
    }

    /**
    * Rétablit la dernière action annulée par l'utilisateur
    */
    public void redo() {
        this.undoRedoManager.redo(this.projectActif);
        // this.dispatchChaletChangeEvent(getChalet());
        this.dispatchRedoEvent(new ChalCLTProjet.ChalCLTProjetDTO(projectActif));
    }
}

