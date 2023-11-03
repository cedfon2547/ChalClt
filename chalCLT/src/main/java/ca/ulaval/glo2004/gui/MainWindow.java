package ca.ulaval.glo2004.gui;

import java.awt.BorderLayout;
import java.util.UUID;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.gui.components.ArbreDesComposantesChalet;
import ca.ulaval.glo2004.gui.components.DrawingPanel;
import ca.ulaval.glo2004.gui.components.MainWindowTopBarMenu;
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

    public JTable tableProprietesAccessoire;
    public JScrollPane tableContainer;

    public DrawingPanel drawingPanel;
    public ArbreDesComposantesChalet arbreDesComposantesChalet;
    public MainWindowTopBarMenu menu;

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
        tableProprietesChalet = new TableChalet(this);

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

        sidePanelTopSection.setBorder(tableProprietesChalet.getTitledBorder());
        
        tableContainer.add(tableProprietesChalet.getTableHeader());
        tableContainer.setViewportView(tableProprietesChalet);

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
        TopButtonPanel topButtonPanel = new TopButtonPanel(this);
        add(mainWindowSplitPane);
        add(topButtonPanel,BorderLayout.BEFORE_FIRST_LINE);

    }

    
    public Controleur getControleur() {
        return controleur;
    }
}