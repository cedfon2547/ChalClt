package ca.ulaval.glo2004.gui.components;

import java.awt.event.MouseAdapter;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.gui.MainWindow;

public class ArbreDesComposantesChalet extends javax.swing.JPanel {
    MainWindow mainWindow;
    public ArbreDesComposantesChalet(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    private javax.swing.JTree arbreComposantesChalet;
    private javax.swing.JScrollPane treeScrollPane;
    javax.swing.border.TitledBorder titledBorder;
    javax.swing.tree.DefaultMutableTreeNode chaletNode;
    javax.swing.tree.DefaultMutableTreeNode mursNode;
    javax.swing.tree.DefaultMutableTreeNode toitNode;
    javax.swing.tree.DefaultMutableTreeNode murFacadeNode;
    javax.swing.tree.DefaultMutableTreeNode murArriereNode;
    javax.swing.tree.DefaultMutableTreeNode murDroitNode;
    javax.swing.tree.DefaultMutableTreeNode murGaucheNode;
    javax.swing.tree.DefaultMutableTreeNode toitPanneauSuperieurNode;
    javax.swing.tree.DefaultMutableTreeNode toitRallongeVerticaleNode;
    javax.swing.tree.DefaultMutableTreeNode toitPignonDroitNode;
    javax.swing.tree.DefaultMutableTreeNode toitPignonGaucheNode;

    private void initComponents() {
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Composantes");
        setBorder(titledBorder);

        String nom = this.mainWindow.getControleur().getChalet().nom;
        treeScrollPane = new javax.swing.JScrollPane();
        arbreComposantesChalet = new javax.swing.JTree();

        arbreComposantesChalet.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        chaletNode = new javax.swing.tree.DefaultMutableTreeNode(nom);
        mursNode = new javax.swing.tree.DefaultMutableTreeNode("Murs");
        toitNode = new javax.swing.tree.DefaultMutableTreeNode("Toit");
        murFacadeNode = new javax.swing.tree.DefaultMutableTreeNode("Façade");
        murArriereNode = new javax.swing.tree.DefaultMutableTreeNode("Arrière");
        murDroitNode = new javax.swing.tree.DefaultMutableTreeNode("Mur droit");
        murGaucheNode = new javax.swing.tree.DefaultMutableTreeNode("Mur gauche");
        toitPanneauSuperieurNode = new javax.swing.tree.DefaultMutableTreeNode("Panneau supérieur");
        toitRallongeVerticaleNode = new javax.swing.tree.DefaultMutableTreeNode("Rallonge verticale");
        toitPignonDroitNode = new javax.swing.tree.DefaultMutableTreeNode("Pignon droit");
        toitPignonGaucheNode = new javax.swing.tree.DefaultMutableTreeNode("Pignon gauche");

        this.mainWindow.getControleur().addPropertyChangeListener("chalet", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == "chalet") {
                    Chalet.ChaletDTO chaletDTO = (Chalet.ChaletDTO) evt.getNewValue();
                    chaletNode.setUserObject(chaletDTO.nom);
                    ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeChanged(chaletNode);
                    //chaletNode = new javax.swing.tree.DefaultMutableTreeNode(evt.getNewValue().toString());
                }
            }
        });

        javax.swing.tree.DefaultMutableTreeNode murFacadeAccChild1Node = new javax.swing.tree.DefaultMutableTreeNode(
                "fenêtre_1");
        javax.swing.tree.DefaultMutableTreeNode murFacadeAccChild2Node = new javax.swing.tree.DefaultMutableTreeNode(
                "fenêtre_2");
        javax.swing.tree.DefaultMutableTreeNode murFacadeAccChild3Node = new javax.swing.tree.DefaultMutableTreeNode(
                "porte_1");
        javax.swing.tree.DefaultMutableTreeNode murFacadeAccChild4Node = new javax.swing.tree.DefaultMutableTreeNode(
                "porte_2");

        javax.swing.tree.DefaultMutableTreeNode murArriereAccChild1Node = new javax.swing.tree.DefaultMutableTreeNode(
                "fenêtre_1");
        javax.swing.tree.DefaultMutableTreeNode murArriereAccChild2Node = new javax.swing.tree.DefaultMutableTreeNode(
                "fenêtre_2");
        javax.swing.tree.DefaultMutableTreeNode murArriereAccChild3Node = new javax.swing.tree.DefaultMutableTreeNode(
                "porte_1");

        murFacadeNode.add(murFacadeAccChild1Node);
        murFacadeNode.add(murFacadeAccChild2Node);
        murFacadeNode.add(murFacadeAccChild3Node);
        murFacadeNode.add(murFacadeAccChild4Node);

        murArriereNode.add(murArriereAccChild1Node);
        murArriereNode.add(murArriereAccChild2Node);
        murArriereNode.add(murArriereAccChild3Node);

        // murFacadeNode.add(murFacadeAccNode);
        // murArriereNode.add(murArriereAccNode);

        mursNode.add(murFacadeNode);
        mursNode.add(murArriereNode);
        mursNode.add(murDroitNode);
        mursNode.add(murGaucheNode);

        toitNode.add(toitPanneauSuperieurNode);
        toitNode.add(toitRallongeVerticaleNode);
        toitNode.add(toitPignonDroitNode);
        toitNode.add(toitPignonGaucheNode);

        chaletNode.add(mursNode);
        chaletNode.add(toitNode);

        arbreComposantesChalet.setModel(new javax.swing.tree.DefaultTreeModel(chaletNode));
        arbreComposantesChalet.setToolTipText("Table des composantes");
        arbreComposantesChalet.setEditable(true);
        arbreComposantesChalet.setName("tableDesComposantes"); // NOI18N
        arbreComposantesChalet.setShowsRootHandles(true);
        treeScrollPane.setViewportView(arbreComposantesChalet);
        arbreComposantesChalet.getAccessibleContext().setAccessibleName("ArbreDesComposantes");
        arbreComposantesChalet.getAccessibleContext().setAccessibleDescription("Arbre des composantes");
        arbreComposantesChalet.setToggleClickCount(0);

        arbreComposantesChalet.addMouseListener(getTreeMouseListener());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(treeScrollPane)
                                .addGap(0, 0, 0)));
    }

    public MouseAdapter getTreeMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    System.out.println("double clicked");
                    TreePath path = arbreComposantesChalet.getPathForLocation(evt.getX(), evt.getY());
                    if (path == null)
                        return;

                    if (path.getPath().length == 1) {
                        System.out.println("Projet");

                        return;
                    }

                    switch (path.getLastPathComponent().toString()) {
                        case "Façade":
                            System.out.println("Facade");
                            mainWindow.drawingPanel.vueActive = DrawingPanel.TypeDeVue.Facade;
                            mainWindow.drawingPanel.scene.getCamera().setDirection(DrawingPanel.TypeDeVue.vueFacade());
                            break;
                        case "Arrière":
                            System.out.println("Arriere");
                            mainWindow.drawingPanel.vueActive = DrawingPanel.TypeDeVue.Arriere;
                            mainWindow.drawingPanel.scene.getCamera().setDirection(DrawingPanel.TypeDeVue.vueArriere());
                            break;
                        case "Mur droit":
                            System.out.println("Mur droit");
                            mainWindow.drawingPanel.vueActive = DrawingPanel.TypeDeVue.Droite;
                            mainWindow.drawingPanel.scene.getCamera().setDirection(DrawingPanel.TypeDeVue.vueDroite());
                            break;
                        case "Mur gauche":
                            System.out.println("Mur gauche");
                            mainWindow.drawingPanel.vueActive = DrawingPanel.TypeDeVue.Gauche;
                            mainWindow.drawingPanel.scene.getCamera().setDirection(DrawingPanel.TypeDeVue.vueGauche());
                            break;
                        case "Panneau supérieur":
                            System.out.println("Panneau supérieur");
                            break;
                        case "Rallonge verticale":
                            System.out.println("Rallonge verticale");
                            break;
                        case "Pignon droit":
                            System.out.println("Pignon droit");
                            break;
                        case "Pignon gauche":
                            System.out.println("Pignon gauche");
                            break;
                    }

                    mainWindow.drawingPanel.updateToolbarBtns();
                    mainWindow.drawingPanel.invalidate();
                    mainWindow.drawingPanel.repaint();
                }
            }
        };
    }
}
