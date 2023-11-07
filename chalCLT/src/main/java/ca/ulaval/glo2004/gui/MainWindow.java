package ca.ulaval.glo2004.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.components.AccessoireTreeNode;
import ca.ulaval.glo2004.gui.components.ArbreDesComposantesChalet;
import ca.ulaval.glo2004.gui.components.DrawingPanel;
import ca.ulaval.glo2004.gui.components.MainWindowTopBarMenu;
import ca.ulaval.glo2004.gui.components.TableAccessoire;
import ca.ulaval.glo2004.gui.components.TopButtonPanel;
import ca.ulaval.glo2004.gui.components.TableChalet;

/*
 * UI main layout
            MainWindowSplitPane

             _________________________________
            |       |                         |
            | Sider |                         |
            | Top   |                         |
            |       |                         |
Sider       |       |         Main            |
SplitPane   ---------         Section         |
            |       |                         |
            | Sider |                         |
            | Bottom|                         |
            |       |                         |
            |_______|_________________________|
 */
public class MainWindow extends javax.swing.JFrame {
    private Controleur controleur;

    public javax.swing.JPanel mainSection;
    public javax.swing.JPanel sideSection;
    public javax.swing.JSplitPane mainWindowSplitPane;
    public javax.swing.JSplitPane sidePanelSplitPane;
    public javax.swing.JPanel sidePanelTopSection;
    public javax.swing.JPanel sidePanelBottomSection;

    public TableChalet tableProprietesChalet;
    public TableAccessoire tableProprietesAccessoire;
    public JScrollPane tableContainer;
    public DrawingPanel drawingPanel;
    public ArbreDesComposantesChalet arbreDesComposantesChalet;
    public MainWindowTopBarMenu menu;
    public TopButtonPanel topButtonPanel; 

    public List<Accessoire.AccessoireDTO> accessoiresSelectionnees = new ArrayList<Accessoire.AccessoireDTO>();

    public MainWindow(Controleur controleur) {
        super("ChalCLT - Créateur de chalets");
        this.controleur = controleur;
        this.menu = new MainWindowTopBarMenu(this);

        setJMenuBar(menu); // Add the menu bar to the window
        initComponents();
    }

    private void initComponents() {
        mainSection = new javax.swing.JPanel();
        sideSection = new javax.swing.JPanel();
        mainWindowSplitPane = new javax.swing.JSplitPane();
        sidePanelSplitPane = new javax.swing.JSplitPane();
        sidePanelTopSection = new javax.swing.JPanel();
        sidePanelBottomSection = new javax.swing.JPanel();
        drawingPanel = new DrawingPanel(this);
        arbreDesComposantesChalet = new ArbreDesComposantesChalet(this);
        tableContainer = new JScrollPane();

        javax.swing.GroupLayout sideSectionLayout = new javax.swing.GroupLayout(sideSection);
        javax.swing.GroupLayout sidePanelTopSectionLayout = new javax.swing.GroupLayout(sidePanelTopSection);
        javax.swing.GroupLayout sidePanelBottomSectionLayout = new javax.swing.GroupLayout(sidePanelBottomSection);

        mainSection.setLayout(new BorderLayout(10, 10));
        mainSection.add(drawingPanel, BorderLayout.CENTER);

        mainWindowSplitPane.setResizeWeight(0.25);
        sidePanelSplitPane.setResizeWeight(0.5);

        sideSection.setLayout(sideSectionLayout);
        sidePanelTopSection.setLayout(sidePanelTopSectionLayout);
        sidePanelBottomSection.setLayout(sidePanelBottomSectionLayout);

        sideSectionLayout.setHorizontalGroup(
                sideSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        sideSectionLayout.setVerticalGroup(
                sideSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        sidePanelTopSectionLayout.setHorizontalGroup(
                sidePanelTopSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(100, 100, Short.MAX_VALUE)
                        .addComponent(tableContainer));

        sidePanelTopSectionLayout.setVerticalGroup(
                sidePanelTopSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(100, 100, Short.MAX_VALUE)
                        .addComponent(tableContainer));

        sidePanelBottomSectionLayout.setHorizontalGroup(
                sidePanelBottomSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(arbreDesComposantesChalet));

        sidePanelBottomSectionLayout.setVerticalGroup(
                sidePanelBottomSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(arbreDesComposantesChalet));

        sidePanelSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        sidePanelSplitPane.setTopComponent(sidePanelTopSection);
        sidePanelSplitPane.setRightComponent(sidePanelBottomSection);

        mainWindowSplitPane.setRightComponent(mainSection);
        mainWindowSplitPane.setLeftComponent(sidePanelSplitPane);

        mainWindowSplitPane.setDividerLocation(200);
        sidePanelSplitPane.setDividerLocation(200);

        setLayout(new BorderLayout());
        topButtonPanel = new TopButtonPanel(this);
        add(mainWindowSplitPane);
        add(topButtonPanel, BorderLayout.BEFORE_FIRST_LINE);

        showChaletTable();
    }

    public Controleur getControleur() {
        return controleur;
    }

    /**
     * Affiche la table des propriétés du chalet.
     */
    public void showChaletTable() {
        tableProprietesChalet = new TableChalet(this);
        tableContainer.setBorder(tableProprietesChalet.getTitledBorder());
        tableContainer.add(tableProprietesChalet.getTableHeader());
        tableContainer.setViewportView(tableProprietesChalet);
    }

    /**
     * Affiche la table des propriétés d'un accessoire.
     * 
     * @param dtoAccessoire le DTO de l'accessoire à afficher
     */
    public void showAccessoireTable(Accessoire.AccessoireDTO dtoAccessoire) {
        if (dtoAccessoire == null) {
            return;
        }
        tableProprietesAccessoire = new TableAccessoire(this, dtoAccessoire);
        tableContainer.setBorder(tableProprietesAccessoire.getTitledBorder());
        tableContainer.add(tableProprietesAccessoire.getTableHeader());
        tableContainer.setViewportView(tableProprietesAccessoire);
    }

    public List<Accessoire.AccessoireDTO> getAccessoiresSelectionnees() {
        return accessoiresSelectionnees;
    }

    public void ajouterAccessoireSelectionnee(Accessoire.AccessoireDTO accessoireDTO) {
        accessoiresSelectionnees.add(accessoireDTO);
        if(accessoiresSelectionnees.size() == 0){
             topButtonPanel.supprimerAccessoireBtn.setEnabled(false);
        }else{
             topButtonPanel.supprimerAccessoireBtn.setEnabled(true);
        }
    }

    public void retirerAccessoireSelectionnee(Accessoire.AccessoireDTO accessoireDTO) {
        accessoiresSelectionnees.remove(accessoireDTO);
        if(accessoiresSelectionnees.size() == 0){
             topButtonPanel.supprimerAccessoireBtn.setEnabled(false);
        }else{
             topButtonPanel.supprimerAccessoireBtn.setEnabled(true);
        }

    
    }

    public void clearAccessoiresSelectionnees() {
        accessoiresSelectionnees.clear();
        
        topButtonPanel.supprimerAccessoireBtn.setEnabled(false);
    }

    /**
     * Supprime tous les accessoires sélectionnés dans l'arbre des composantes du chalet.
     * Cette méthode supprime les accessoires sélectionnés de la liste des accessoires sélectionnés,
     * supprime les nœuds correspondants de l'arbre des composantes du chalet et recharge l'affichage.
     */
    public void deleteAllAccessoiresSelectionnees() {
        DefaultTreeModel model = (DefaultTreeModel) arbreDesComposantesChalet.arbreComposantesChalet.getModel();

        for (Accessoire.AccessoireDTO accessoireDTO: accessoiresSelectionnees) {
            controleur.supprimerAccessoire(accessoireDTO.typeMur, accessoireDTO.accessoireId);
        }

        List<AccessoireTreeNode> toRemove = new ArrayList<AccessoireTreeNode>();
        for (AccessoireTreeNode accNode: arbreDesComposantesChalet.accessoireNodes) {
            if (accessoiresSelectionnees.contains(accNode.getAccessoireDTO())) {
                model.removeNodeFromParent(accNode);
                toRemove.add(accNode);
            }
        }

        for (AccessoireTreeNode accNode: toRemove) {
            arbreDesComposantesChalet.accessoireNodes.remove(accNode);
        }

        this.clearAccessoiresSelectionnees();

        drawingPanel.rechargerAffichage();
    }
}