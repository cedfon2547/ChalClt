package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
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

    public TopButtonPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponent();
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
        voisinToggleBtn = new JToggleButton("Voisins");

        creerFenetreBtn.setPreferredSize(new Dimension(75, 50));
        creerPorteBtn.setPreferredSize(new Dimension(75, 50));
        supprimerAccessoireBtn.setPreferredSize(new Dimension(75, 50));
        grilleToggleBtn.setPreferredSize(new Dimension(75, 50));
        voisinToggleBtn.setPreferredSize(new Dimension(75, 50));

        String fenetreImgName = "/icons/dark/fenetre_1.png";
        String porteImgName = "/icons/dark/door_1.png";
        String supprimerImgName = "/icons/dark/supprimer_1.png";
        String grilleImgName = "/icons/dark/grille_1.png";
        String voisinImgName = "/icons/dark/voisin_1.png";

        URL fenetreImgURL = App.class.getResource(fenetreImgName);
        URL porteImgURL = App.class.getResource(porteImgName);
        URL supprimerImgURL = App.class.getResource(supprimerImgName);
        URL grilleImgURL = App.class.getResource(grilleImgName);
        URL voisinImgURL = App.class.getResource(voisinImgName);
        Toolkit tk = Toolkit.getDefaultToolkit();
        
        Image fenetreImg = tk.getImage(fenetreImgURL);
        Image porteImg = tk.getImage(porteImgURL);
        Image supprimerImg = tk.getImage(supprimerImgURL);
        Image grilleImg = tk.getImage(grilleImgURL);
        Image voisinImg = tk.getImage(voisinImgURL);

        ImageIcon iconFenetre = new ImageIcon(fenetreImg);
        ImageIcon iconPorte = new ImageIcon(porteImg);
        ImageIcon iconSupprimer = new ImageIcon(supprimerImg);
        ImageIcon iconGrille = new ImageIcon(grilleImg);
        ImageIcon iconVoisins = new ImageIcon(voisinImg);

        iconFenetre.setImage(iconFenetre.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconPorte.setImage(iconPorte.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconSupprimer.setImage(iconSupprimer.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconGrille.setImage(iconGrille.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        iconVoisins.setImage(iconVoisins.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));

        creerFenetreBtn.setFocusPainted(false);
        creerFenetreBtn.setIcon(iconFenetre);
        creerFenetreBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        creerFenetreBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        creerFenetreBtn.setBorder(null);

        creerPorteBtn.setFocusPainted(false);
        creerPorteBtn.setIcon(iconPorte);
        creerPorteBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        creerPorteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        creerPorteBtn.setBorder(null);

        supprimerAccessoireBtn.setFocusPainted(false);
        supprimerAccessoireBtn.setIcon(iconSupprimer);
        supprimerAccessoireBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        supprimerAccessoireBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        supprimerAccessoireBtn.setBorder(null);

        grilleToggleBtn.setFocusPainted(false);
        grilleToggleBtn.setIcon(iconGrille);
        grilleToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        grilleToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        grilleToggleBtn.setBorder(null);
        grilleToggleBtn.setSelected(true);

        voisinToggleBtn.setFocusPainted(false);
        voisinToggleBtn.setIcon(iconVoisins);
        voisinToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        voisinToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        voisinToggleBtn.setBorder(null);

        creerFenetreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Afficheur.TypeDeVue vueActive = mainWindow.drawingPanel.afficheur.getVueActive();
                if (vueActive == Afficheur.TypeDeVue.Dessus) {
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
                mainWindow.drawingPanel.rechargerAffichage();

            }
        });

        creerPorteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Afficheur.TypeDeVue vueActive = mainWindow.drawingPanel.afficheur.getVueActive();
                if (vueActive == Afficheur.TypeDeVue.Dessus) {
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
                mainWindow.drawingPanel.rechargerAffichage();
            }
        });

        supprimerAccessoireBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println(mainWindow.getAccessoiresSelectionnees().size());
                mainWindow.deleteAllAccessoiresSelectionnees();
            }
        });

        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur().getPreferencesUtilisateur();
        grilleToggleBtn.setSelected(preferencesUtilisateurDTO.afficherGrille);

        mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.PREFERENCES_UTILISATEUR, (evt) -> {
            PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur = (PreferencesUtilisateur.PreferencesUtilisateurDTO) evt.getNewValue();
            grilleToggleBtn.setSelected(preferencesUtilisateur.afficherGrille);
            voisinToggleBtn.setSelected(preferencesUtilisateur.afficherVoisinSelection);
        });

        grilleToggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                        .getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherGrille = !preferencesUtilisateurDTO.afficherGrille;
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO);
                mainWindow.drawingPanel.updateViewGrid();
                mainWindow.drawingPanel.rechargerAffichage();
            }
        });

        voisinToggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                        .getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherVoisinSelection = !preferencesUtilisateurDTO.afficherVoisinSelection;
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO);
                
                mainWindow.drawingPanel.rechargerAffichage();
            }
        });

        
        if(mainWindow.getAccessoiresSelectionnees().size()==0){
            supprimerAccessoireBtn.setEnabled(false);
        }

        // voisinToggleBtn.setEnabled(false);

        add(creerFenetreBtn);
        add(creerPorteBtn);
        add(supprimerAccessoireBtn);
        add(voisinToggleBtn);
        add(grilleToggleBtn);
        mainWindow.drawingPanel.rechargerAffichage();
    }

}
