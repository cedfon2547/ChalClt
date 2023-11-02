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
    public TopButtonPanel(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        initComponent();
    }

    private void initComponent(){
        GridLayout layout = new GridLayout(1,5);
        setLayout(layout);

        creerFenetreBtn = new JButton("Fenêtre");
        creerPorteBtn = new JButton("Porte");
        supprimerAccessoireBtn = new JButton("Supprimer");
        grilleToggleBtn = new JToggleButton("Grille");
        voisinToggleBtn = new JToggleButton("Voisins");

        creerFenetreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingPanel.TypeDeVue vueActive = mainWindow.drawingPanel.vueActive;
                if(vueActive == DrawingPanel.TypeDeVue.Dessus) {
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


                mainWindow.getControleur().ajouterAccessoire(typeMur,  TypeAccessoire.Fenetre, new double[] { 0, 0 }, new double[] { 50, 50 });

            }
        });
        
        creerPorteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingPanel.TypeDeVue vueActive = mainWindow.drawingPanel.vueActive;
                if(vueActive == DrawingPanel.TypeDeVue.Dessus) {
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


                mainWindow.getControleur().ajouterAccessoire(typeMur,  TypeAccessoire.Porte, new double[] { 0, 0 }, new double[] { 50, 50 });

            }
        });

        supprimerAccessoireBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            //todo
            }
        });

        grilleToggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur().getPreferencesUtilisateur();
                preferencesUtilisateurDTO.afficherGrille = !preferencesUtilisateurDTO.afficherGrille;
                mainWindow.drawingPanel.repaint();
            }
        });

        voisinToggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur().getPreferencesUtilisateur();
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
