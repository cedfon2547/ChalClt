package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEventListener;
import ca.ulaval.glo2004.domaine.afficheur.TypeDeVue;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.gui.NotificationManager.NotificationType;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class TopButtonPanel extends JPanel {

    private MainWindow mainWindow;
    public JButton creerFenetreBtn;

    public JButton creerPorteBtn;

    public JButton supprimerAccessoireBtn;

    public JToggleButton grilleToggleBtn;

    public JToggleButton voisinToggleBtn;

    public JButton undoToggleBtn;

    public JButton redoToggleBtn;

    public JButton saveToggleBtn;

    public TopButtonPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponent();
        initializeIcons();

        mainWindow.getControleur().addUserPreferencesEventListener(new UserPreferencesEventListener() {
            @Override
            public void change(UserPreferencesEvent event) {
                // PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur = event
                //         .getPreferencesUtilisateurDTO();
                
                // grilleToggleBtn.setSelected(preferencesUtilisateur.afficherGrille);
                // voisinToggleBtn.setSelected(preferencesUtilisateur.afficherVoisinSelection);

                // if (preferencesUtilisateur.afficherVoisinSelection) {
                //     creerFenetreBtn.setEnabled(false);
                //     creerPorteBtn.setEnabled(false);
                // } else {
                //     creerFenetreBtn.setEnabled(true);
                //     creerPorteBtn.setEnabled(true);
                // }
            }
        });
    }

    public void initComponent() {
        FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT);
        fLayout.setAlignOnBaseline(true);
        fLayout.setHgap(0);
        fLayout.setVgap(0);
        setLayout(fLayout);

        creerFenetreBtn = new JButton("Fenêtre");
        creerPorteBtn = new JButton("Porte");
        supprimerAccessoireBtn = new JButton("Supprimer");
        grilleToggleBtn = new JToggleButton("Grille");
        voisinToggleBtn = new JToggleButton("Voisin");
        undoToggleBtn = new JButton("Annuler");
        redoToggleBtn = new JButton("Rétablir");
        saveToggleBtn = new JButton("Sauvegarder");

        creerFenetreBtn.setPreferredSize(new Dimension(75, 50));
        creerPorteBtn.setPreferredSize(new Dimension(75, 50));
        supprimerAccessoireBtn.setPreferredSize(new Dimension(75, 50));
        //grilleToggleBtn.setPreferredSize(new Dimension(75, 50));
        //voisinToggleBtn.setPreferredSize(new Dimension(75, 50));
        undoToggleBtn.setPreferredSize(new Dimension(75, 50));
        redoToggleBtn.setPreferredSize(new Dimension(75, 50));
        saveToggleBtn.setPreferredSize(new Dimension(75, 50));

        creerFenetreBtn.setFocusPainted(false);
        creerFenetreBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        creerFenetreBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        creerFenetreBtn.setBorder(null);

        creerPorteBtn.setFocusPainted(false);
        creerPorteBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        creerPorteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        creerPorteBtn.setBorder(null);

        supprimerAccessoireBtn.setFocusPainted(false);
        supprimerAccessoireBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        supprimerAccessoireBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        supprimerAccessoireBtn.setBorder(null);

//        grilleToggleBtn.setFocusPainted(false);
//        grilleToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
//        grilleToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
//        grilleToggleBtn.setBorder(null);
//        grilleToggleBtn.setSelected(true);

//        voisinToggleBtn.setFocusPainted(false);
//        voisinToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
//        voisinToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
//        voisinToggleBtn.setBorder(null);

        undoToggleBtn.setFocusPainted(false);
        undoToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        undoToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        undoToggleBtn.setBorder(null);

        redoToggleBtn.setFocusPainted(false);
        redoToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        redoToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        redoToggleBtn.setBorder(null);

        saveToggleBtn.setFocusPainted(false);
        saveToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        saveToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        saveToggleBtn.setBorder(null);

        creerFenetreBtn.addActionListener(creerFenetreActionListener());
        creerPorteBtn.addActionListener(creerPortActionListener());
        supprimerAccessoireBtn.addActionListener(supprimerAccessoireActionListener());
        grilleToggleBtn.addActionListener(grilleToggleActionListener());
        voisinToggleBtn.addActionListener(voisinToggleActionListener());
        undoToggleBtn.addActionListener(undoToggleActionListener());
        redoToggleBtn.addActionListener(redoToggleActionListener());
        saveToggleBtn.addActionListener(saveToggleActionListener());

        // creerFenetreBtn.addMouseMotionListener(new MouseAdapter() {
        //     @Override
        //     public void mouseDragged(java.awt.event.MouseEvent evt) {
        //         System.out.println("dragged");
        //     }
        // });

        add(creerFenetreBtn);
        add(creerPorteBtn);
        add(supprimerAccessoireBtn);
        add(undoToggleBtn);
        add(redoToggleBtn);
        add(saveToggleBtn);

        recharger();
    }

    private void initializeIcons() {
        String fenetreImgName = "/icons/dark/fenetre_1.png";
        String porteImgName = "/icons/dark/door_1.png";
        String supprimerImgName = "/icons/dark/supprimer_1.png";
        String grilleImgName = "/icons/dark/grille_1.png";
        String voisinImgName = "/icons/dark/voisin_1.png";
        String undoImgName = "/icons/dark/undo_1.png";
        String redoImgName = "/icons/dark/redo_1.png";
        String saveImgSave = "/icons/dark/save_1.png";

        URL fenetreImgURL = App.class.getResource(fenetreImgName);
        URL porteImgURL = App.class.getResource(porteImgName);
        URL supprimerImgURL = App.class.getResource(supprimerImgName);
        URL grilleImgURL = App.class.getResource(grilleImgName);
        URL voisinImgURL = App.class.getResource(voisinImgName);
        URL undoImgURL = App.class.getResource(undoImgName);
        URL redoImgURL = App.class.getResource(redoImgName);
        URL saveImgURL = App.class.getResource(saveImgSave);
        Toolkit tk = Toolkit.getDefaultToolkit();

        Image fenetreImg = tk.getImage(fenetreImgURL);
        Image porteImg = tk.getImage(porteImgURL);
        Image supprimerImg = tk.getImage(supprimerImgURL);
        Image grilleImg = tk.getImage(grilleImgURL);
        Image voisinImg = tk.getImage(voisinImgURL);
        Image undoImg = tk.getImage(undoImgURL);
        Image redoImg = tk.getImage(redoImgURL);
        Image saveImg = tk.getImage(saveImgURL);

        ImageIcon iconFenetre = new ImageIcon(fenetreImg);
        ImageIcon iconPorte = new ImageIcon(porteImg);
        ImageIcon iconSupprimer = new ImageIcon(supprimerImg);
        ImageIcon iconGrille = new ImageIcon(grilleImg);
        ImageIcon iconVoisins = new ImageIcon(voisinImg);
        ImageIcon iconUndo = new ImageIcon(undoImg);
        ImageIcon iconRedo = new ImageIcon(redoImg);
        ImageIcon iconSave = new ImageIcon(saveImg);

        iconFenetre.setImage(iconFenetre.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconPorte.setImage(iconPorte.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconSupprimer.setImage(iconSupprimer.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconGrille.setImage(iconGrille.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconVoisins.setImage(iconVoisins.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconUndo.setImage(iconUndo.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconRedo.setImage(iconRedo.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconSave.setImage(iconSave.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));

        creerFenetreBtn.setIcon(iconFenetre);
        creerPorteBtn.setIcon(iconPorte);
        supprimerAccessoireBtn.setIcon(iconSupprimer);
        grilleToggleBtn.setIcon(iconGrille);
        voisinToggleBtn.setIcon(iconVoisins);
        undoToggleBtn.setIcon(iconUndo);
        redoToggleBtn.setIcon(iconRedo);
        saveToggleBtn.setIcon(iconSave);
    }

    private ActionListener creerFenetreActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TypeDeVue vueActive = mainWindow.drawingPanel.afficheur.getVueActive();
                if (vueActive == TypeDeVue.Dessus) {
                    return;
                }

                // Mapping TypeDeVue -> TypeMur
                TypeMur typeMur = null;

                switch (vueActive) {
                    case Arriere:
                        typeMur = TypeMur.Arriere;
                        break;
                    case Facade:
                        typeMur = TypeMur.Facade;
                        break;
                    case Gauche:
                        typeMur = TypeMur.Gauche;
                        break;
                    case Droite:
                        typeMur = TypeMur.Droit;
                        break;
                    default:
                        return;
                }

                mainWindow.getControleur().ajouterAccessoire(typeMur, TypeAccessoire.Fenetre, new double[] { 0, 0 },
                        new double[] { 20, 20 });
                // mainWindow.drawingPanel.rechargerAffichage();

            }
        };
    }

    private ActionListener creerPortActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TypeDeVue vueActive = mainWindow.drawingPanel.afficheur.getVueActive();
                if (vueActive == TypeDeVue.Dessus) {
                    return;
                }

                // Mapping TypeDeVue -> TypeMur
                TypeMur typeMur = null;

                switch (vueActive) {
                    case Arriere:
                        typeMur = TypeMur.Arriere;
                        break;
                    case Facade:
                        typeMur = TypeMur.Facade;
                        break;
                    case Gauche:
                        typeMur = TypeMur.Gauche;
                        break;
                    case Droite:
                        typeMur = TypeMur.Droit;
                        break;
                    default:
                        return;
                }

                mainWindow.getControleur().ajouterAccessoire(typeMur, TypeAccessoire.Porte, new double[] { 0, 0 },
                        new double[] { 32, 60 });
                // mainWindow.drawingPanel.rechargerAffichage();
            }
        };
    }

    private ActionListener supprimerAccessoireActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println(mainWindow.getAccessoiresSelectionnees().size());
                mainWindow.deleteAllAccessoiresSelectionnees();
            }
        };
    }

    private ActionListener grilleToggleActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                        .getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherGrille = !preferencesUtilisateurDTO.afficherGrille;
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO);
            }
        };
    }

    private ActionListener voisinToggleActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                        .getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherVoisinSelection = !preferencesUtilisateurDTO.afficherVoisinSelection;
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO);

                mainWindow.drawingPanel.afficheur.rechargerAffichage();
            }
        };
    }

    private ActionListener undoToggleActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getControleur().undo();
                mainWindow.recharger();
                mainWindow.drawingPanel.afficheur.rechargerAffichage();
            }
        };
    }

    private ActionListener redoToggleActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getControleur().redo();
                mainWindow.recharger();
                mainWindow.drawingPanel.afficheur.rechargerAffichage();
            }
        };
    }

    private ActionListener saveToggleActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new OpenSaveDirectoryFileChoose(mainWindow, (path) -> {
                //     mainWindow.getControleur().creerSauvegarde(path);
                //     mainWindow.notificationManager.createNotification("Projet sauvegardé", "", NotificationType.SUCCESS).setTimer(2000);

                //     return true;
                // });
                new SaveProjectDirectoryFileChooser(mainWindow, (path) -> {
                    mainWindow.getControleur().creerSauvegarde(path);
                    mainWindow.notificationManager.createNotification("Projet sauvegardé", "", NotificationType.SUCCESS).setTimer(2000);
                    return true;
                });
            }
        };
    }

    public void recharger() {
        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                .getPreferencesUtilisateur();

        grilleToggleBtn.setSelected(preferencesUtilisateurDTO.afficherGrille);
        voisinToggleBtn.setSelected(preferencesUtilisateurDTO.afficherVoisinSelection);
        
        if (mainWindow.getAccessoiresSelectionnees().size() != 0) {
            supprimerAccessoireBtn.setEnabled(true);
        } else {
            supprimerAccessoireBtn.setEnabled(false);
        }
    }
}
