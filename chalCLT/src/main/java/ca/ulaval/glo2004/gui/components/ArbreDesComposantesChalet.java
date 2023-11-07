package ca.ulaval.glo2004.gui.components;

import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.Constants;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;

public class ArbreDesComposantesChalet extends javax.swing.JPanel {
    MainWindow mainWindow;

    public ArbreDesComposantesChalet(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    public javax.swing.JTree arbreComposantesChalet;
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
    TreeRenderer treeRenderer;

    public List<AccessoireTreeNode> accessoireNodes = new ArrayList<AccessoireTreeNode>();

    private void initComponents() {
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Composantes");
        setBorder(titledBorder);

        String nom = this.mainWindow.getControleur().getChalet().nom;
        treeScrollPane = new javax.swing.JScrollPane();
        arbreComposantesChalet = new javax.swing.JTree();

        arbreComposantesChalet.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        chaletNode = new javax.swing.tree.DefaultMutableTreeNode(nom);
        mursNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_MURS);
        toitNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_TOIT);
        murFacadeNode = new MurTreeNode(Constants._STRING_MUR_FACADE);
        murArriereNode = new MurTreeNode(Constants._STRING_MUR_ARRIERE);
        murDroitNode = new MurTreeNode(Constants._STRING_MUR_DROIT);
        murGaucheNode = new MurTreeNode(Constants._STRING_MUR_GAUCHE);
        toitPanneauSuperieurNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_PANNEAU_SUPERIEUR);
        toitRallongeVerticaleNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_RALLONGE_VERTICALE);
        toitPignonDroitNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_PIGNON_DROIT);
        toitPignonGaucheNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_PIGNON_GAUCHE);
        treeRenderer = new TreeRenderer(mainWindow);
        arbreComposantesChalet.setCellRenderer(treeRenderer);

        this.mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.CHALET, this.getChaletChangeListener());
        this.mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.ACCESSOIRE, this.getAccessoireChangeListener());
        this.mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.SUPPRIMER_ACCESSOIRE, this.getSupprimerAccessoireListener());
        this.mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.AJOUTER_ACCESSOIRE, this.getAjouterAccessoireListener());

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
        arbreComposantesChalet.getAccessibleContext().setAccessibleName("ArbreDesComposantes");
        arbreComposantesChalet.getAccessibleContext().setAccessibleDescription("Arbre des composantes");
        arbreComposantesChalet.setToggleClickCount(0);

        treeScrollPane.setViewportView(arbreComposantesChalet);

        arbreComposantesChalet.addMouseListener(this.getTreeMouseListener());
        arbreComposantesChalet.addFocusListener(this.getFocusListener());
        arbreComposantesChalet.addTreeSelectionListener(this.getTreeSelectionListener());
        
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

    private TreeSelectionListener getTreeSelectionListener() {
        return new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath[] treePaths = arbreComposantesChalet.getSelectionPaths();

                if (treePaths == null)
                    return;

                mainWindow.clearAccessoiresSelectionnees();
                for (TreePath path : treePaths) {
                    if (path.getLastPathComponent() instanceof AccessoireTreeNode) {
                        AccessoireTreeNode nodeAcc = (AccessoireTreeNode) path.getLastPathComponent();
                        mainWindow.ajouterAccessoireSelectionnee(nodeAcc.getAccessoireDTO());
                    }
                }
            }
        };
    }
    private PropertyChangeListener getChaletChangeListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("chalet")) {
                    Chalet.ChaletDTO chaletDTO = (Chalet.ChaletDTO) evt.getNewValue();
                    chaletNode.setUserObject(chaletDTO.nom);
                    ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeChanged(chaletNode);
                }
            }
        };
    }

    private PropertyChangeListener getAccessoireChangeListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("accessoire")) {
                    Accessoire.AccessoireDTO accDto = (Accessoire.AccessoireDTO) evt.getNewValue();

                    // System.out.println(accDto.accessoireNom);

                    AccessoireTreeNode accessoireTreeNode = (AccessoireTreeNode) arbreComposantesChalet
                            .getLastSelectedPathComponent();
                    TreePath treePath = arbreComposantesChalet.getNextMatch(
                            ((Accessoire.AccessoireDTO) evt.getOldValue()).accessoireNom, 0, Position.Bias.Forward);
                    if (treePath != null) {
                        // System.out.println(treePath);
                        accessoireTreeNode.setUserObject(accDto);
                        accessoireTreeNode.setAccessoireDTO(accDto);
                        accessoireTreeNode.setNom(accDto.accessoireNom);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(accessoireTreeNode);
                    }
                }
            }
        };
    }

    private PropertyChangeListener getSupprimerAccessoireListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // System.out.println("Retirer Accessoire " + ((Accessoire.AccessoireDTO)
                // evt.getNewValue()).accessoireNom);
                mainWindow.showChaletTable();
            }
        };
    }

    private PropertyChangeListener getAjouterAccessoireListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // System.out.println("ajouterAccessoire");
                Accessoire.AccessoireDTO accDto = (Accessoire.AccessoireDTO) evt.getNewValue();
                AccessoireTreeNode accessoireNode = new AccessoireTreeNode(accDto);

                accessoireNodes.add(accessoireNode);

                switch (accDto.typeMur) {
                    case Facade:
                        murFacadeNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murFacadeNode);
                        break;
                    case Arriere:
                        murArriereNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murArriereNode);
                        break;
                    case Droit:
                        murDroitNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murDroitNode);
                        break;
                    case Gauche:
                        murGaucheNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murGaucheNode);

                        break;
                    default:
                        return;
                }

                arbreComposantesChalet.invalidate();
                arbreComposantesChalet.repaint();
            }
        };
    }

    private MouseAdapter getTreeMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TreePath path = arbreComposantesChalet.getPathForLocation(evt.getX(), evt.getY());

                if (path == null)
                    return;

                if (path.getPath().length == 1) {
                    // System.out.println("Projet");
                    mainWindow.showChaletTable();
                    return;
                }

                TreePath parent = path.getParentPath();
                // node utilisé pour vérifier si le type du node selectionné est
                // AccessoireTreeNode

                if (parent != null) {
                    String parentStr = parent.getLastPathComponent().toString();

                    if (parentStr != null) {
                        if (path.getLastPathComponent() instanceof AccessoireTreeNode) {
                            AccessoireTreeNode nodeAcc = (AccessoireTreeNode) path.getLastPathComponent();
                            Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                                    .getAccessoire(nodeAcc.getUuid());

                            mainWindow.showAccessoireTable(accDto);
                        } else {
                            mainWindow.showChaletTable();
                        }

                    }
                }

                if (evt.getClickCount() == 2 && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    switch (path.getLastPathComponent().toString()) {
                        case Constants._STRING_MUR_FACADE:
                            // System.out.println("Mur Facade");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Facade);
                            break;
                        case Constants._STRING_MUR_ARRIERE:
                            // System.out.println("Mur Arriere");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Arriere);
                            break;
                        case Constants._STRING_MUR_DROIT:
                            // System.out.println("Mur droit");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Droite);
                            break;
                        case Constants._STRING_MUR_GAUCHE:
                            // System.out.println("Mur gauche");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Gauche);
                            break;
                        case Constants._STRING_PANNEAU_SUPERIEUR:
                            // System.out.println("Panneau supérieur");
                            break;
                        case Constants._STRING_RALLONGE_VERTICALE:
                            // System.out.println("Rallonge verticale");
                            break;
                        case Constants._STRING_PIGNON_DROIT:
                            // System.out.println("Pignon droit");
                            break;
                        case Constants._STRING_PIGNON_GAUCHE:
                            // System.out.println("Pignon gauche");
                            break;
                    }

                    mainWindow.drawingPanel.updateToolbarBtns();
                    mainWindow.drawingPanel.invalidate();
                    mainWindow.drawingPanel.repaint();
                }
            }
        };
    }

    private FocusListener getFocusListener() {
        return new FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                // System.out.println("focusGained");
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                // System.out.println("focusLost");
                // arbreComposantesChalet.clearSelection();
            }
        };
    }

    public void rechargerNoeudAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        for (AccessoireTreeNode accNode : accessoireNodes) {
            if (accNode.getAccessoireDTO().accessoireId == accessoireDTO.accessoireId) {
                // accNode.setUserObject(accessoireDTO);
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(accNode);
                break;
            }
        }
    }
}
