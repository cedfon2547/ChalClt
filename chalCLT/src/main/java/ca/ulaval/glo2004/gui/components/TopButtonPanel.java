package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.gui.MainWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private void initComponent() {
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

        ImageIcon iconFenetre = new ImageIcon(
                "chalCLT\\src\\main\\java\\ca\\ulaval\\glo2004\\gui\\ressources\\icons\\fenetre_1.png");
        ImageIcon iconPorte = new ImageIcon(
                "chalCLT\\src\\main\\java\\ca\\ulaval\\glo2004\\gui\\ressources\\icons\\door_1.png");
        ImageIcon iconSupprimer = new ImageIcon(
                "chalCLT\\src\\main\\java\\ca\\ulaval\\glo2004\\gui\\ressources\\icons\\supprimer_1.png");
        ImageIcon iconGrille = new ImageIcon(
                "chalCLT\\src\\main\\java\\ca\\ulaval\\glo2004\\gui\\ressources\\icons\\grille_1.png");
        ImageIcon iconVoisins = new ImageIcon(
                "chalCLT\\src\\main\\java\\ca\\ulaval\\glo2004\\gui\\ressources\\icons\\voisin_1.png");

        iconFenetre.setImage(iconFenetre.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        iconPorte.setImage(iconPorte.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        iconSupprimer.setImage(iconSupprimer.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        iconGrille.setImage(iconGrille.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        iconVoisins.setImage(iconVoisins.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

        creerFenetreBtn.setIcon(iconFenetre);
        creerFenetreBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        creerFenetreBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        creerFenetreBtn.setBorder(null);

        creerPorteBtn.setIcon(iconPorte);
        creerPorteBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        creerPorteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        creerPorteBtn.setBorder(null);

        supprimerAccessoireBtn.setIcon(iconSupprimer);
        supprimerAccessoireBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        supprimerAccessoireBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        supprimerAccessoireBtn.setBorder(null);

        grilleToggleBtn.setIcon(iconGrille);
        grilleToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        grilleToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        grilleToggleBtn.setBorder(null);

        voisinToggleBtn.setIcon(iconVoisins);
        voisinToggleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        voisinToggleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        voisinToggleBtn.setBorder(null);

        creerFenetreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingPanel.TypeDeVue vueActive = mainWindow.drawingPanel.vueActive;
                if (vueActive == DrawingPanel.TypeDeVue.Dessus) {
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
                        new double[] { 50, 50 });

            }
        });

        creerPorteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingPanel.TypeDeVue vueActive = mainWindow.drawingPanel.vueActive;
                if (vueActive == DrawingPanel.TypeDeVue.Dessus) {
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
                        new double[] { 50, 50 });

            }
        });

        supprimerAccessoireBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo
            }
        });

        grilleToggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                        .getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherGrille = !preferencesUtilisateurDTO.afficherGrille;
                mainWindow.drawingPanel.repaint();
            }
        });

        voisinToggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                        .getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherVoisinSelection = !preferencesUtilisateurDTO.afficherVoisinSelection;
                mainWindow.drawingPanel.repaint();

            }
        });

        supprimerAccessoireBtn.setEnabled(false);
        voisinToggleBtn.setEnabled(false);

        add(creerFenetreBtn);
        add(creerPorteBtn);
        add(supprimerAccessoireBtn);
        add(grilleToggleBtn);
        add(voisinToggleBtn);
    }

}
