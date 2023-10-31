package ca.ulaval.glo2004.gui;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;

import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.gui.components.ArbreDesComposantesChalet;
import ca.ulaval.glo2004.gui.components.DrawingPanel;

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
    public JTable tableProprietesChalet;
    public DrawingPanel drawingPanel;
    public ArbreDesComposantesChalet arbreDesComposantesChalet;

    public MainWindow(Controleur controleur) {
        super("ChalCLT - Créateur de chalets");
        this.controleur = controleur;
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
        arbreDesComposantesChalet = new ArbreDesComposantesChalet();

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

        initializePropertiesTable();


        sideSectionLayout.setHorizontalGroup(
                sideSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        sideSectionLayout.setVerticalGroup(
                sideSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        sidePanelTopSectionLayout.setHorizontalGroup(
                sidePanelTopSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(100, 100, Short.MAX_VALUE)
                        .addComponent(tableProprietesChalet));

        sidePanelTopSectionLayout.setVerticalGroup(
                sidePanelTopSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(100, 100, Short.MAX_VALUE)
                        .addComponent(tableProprietesChalet));

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
        add(mainWindowSplitPane);
    }

    private void initializePropertiesTable() {
        // TODO: Maybe move this to a separate class
        Chalet.ChaletDTO dtoChalet = controleur.getChalet();
        String[] columnNames = new String[] {
                "Propriété",
                "Valeur"
        };
        Object[][] props = new Object[][] {
                { "Hauteur", ImperialDimension.convertToImperial(dtoChalet.hauteur) },
                { "Largeur", ImperialDimension.convertToImperial(dtoChalet.largeur) },
                { "Longueur", ImperialDimension.convertToImperial(dtoChalet.longueur) },
                { "Épaisseur panneaux", ImperialDimension.convertToImperial(dtoChalet.epaisseurMur) },
                { "Angle du toit", dtoChalet.angleToit },
                { "Sens du toit", dtoChalet.sensToit },
        };

        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(props, columnNames) {
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public int getRowCount() {
                return props.length;
            }

            @Override
            public String getColumnName(int col) {
                return columnNames[col];
            }

            @Override
            public Object getValueAt(int row, int col) {
                return props[row][col];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                System.out.println("Setting value at " + rowIndex + "," + columnIndex + " to " + aValue
                        + " (an instance of " + aValue.getClass() + ")");

                if (rowIndex == 0 || rowIndex == 1 || rowIndex == 2 || rowIndex == 3) {
                    ImperialDimension dim = ImperialDimension.parseFromString((String) aValue);
                    System.out.println("Dimension: " + dim);
                    if (dim == null) {
                        return;
                    }

                    props[rowIndex][columnIndex] = dim.toString();
                    fireTableCellUpdated(rowIndex, columnIndex);
                    return;
                }

                props[rowIndex][columnIndex] = aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        tableProprietesChalet = new javax.swing.JTable(model);
        tableProprietesChalet.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                System.out.println("Table changed" + " " + evt.getFirstRow() + " " + evt.getLastRow() + " "
                        + evt.getColumn() + " " + tableProprietesChalet.getValueAt(evt.getFirstRow(), evt.getColumn()));
            }
        });

    }

    public Controleur getControleur() {
        return controleur;
    }
}