package ca.ulaval.glo2004.gui.components;

import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEventListener;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.gui.MainWindow;

public class TableAccessoire extends JTable {
    private MainWindow mainWindow;
    private Accessoire.AccessoireDTO accessoireDTO;
    private String[] columnNames;
    private Object[][] props;
    private javax.swing.table.DefaultTableModel model;
    private AccessoireTableCellEditor accessoireTableCellEditor;
    private AccessoireTableCellRenderer accessoireTableCellRenderer;
    private TitledBorder titledBorder;

    public TableAccessoire(MainWindow mainWindow, Accessoire.AccessoireDTO dtoAcessoire) {
        this.mainWindow = mainWindow;
        this.accessoireDTO = dtoAcessoire;
        this.accessoireTableCellEditor = new AccessoireTableCellEditor();
        this.accessoireTableCellRenderer = new AccessoireTableCellRenderer();

        initComponents();
    }

    private void initComponents() {
        columnNames = new String[] {
                "Propriété",
                "Valeur"
        };

        props = new Object[][] {
                { "Nom", accessoireDTO.accessoireNom },
                { "Hauteur", ImperialDimension.convertToImperial(accessoireDTO.dimensions[1]) },
                { "Largeur", ImperialDimension.convertToImperial(accessoireDTO.dimensions[0]) },
                { "Position x", ImperialDimension.convertToImperial(accessoireDTO.position[0]) },
                { "Position y", ImperialDimension.convertToImperial(accessoireDTO.position[1]) }
        };

        // if (dtoAcessoire.accessoireType == TypeAccessoire.Fenetre) {
        // props[4] = new Object[] { "Position y",
        // ImperialDimension.convertToImperial(dtoAcessoire.position[1]) };
        // }

        model = new javax.swing.table.DefaultTableModel(props, columnNames);
        this.setModel(model);
        this.getColumnModel().getColumn(1).setCellEditor(accessoireTableCellEditor);
        this.getColumnModel().getColumn(1).setCellRenderer(accessoireTableCellRenderer);
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Propriétés de l'accessoire");

        this.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                // System.out.println("Table changed" + " " + evt.getFirstRow() + " " +
                // evt.getLastRow() + " "
                // + evt.getColumn() + " " + getValueAt(evt.getFirstRow(), evt.getColumn()));

                accessoireDTO.accessoireNom = (String) getValueAt(0, 1);
                accessoireDTO.dimensions[1] = ImperialDimension.parseFromString((String) getValueAt(1, 1).toString())
                        .toInches();
                accessoireDTO.dimensions[0] = ImperialDimension.parseFromString((String) getValueAt(2, 1).toString())
                        .toInches();
                accessoireDTO.position[0] = ImperialDimension.parseFromString((String) getValueAt(3, 1).toString())
                        .toInches();

                if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                    accessoireDTO.position[1] = ImperialDimension.parseFromString((String) getValueAt(4, 1).toString())
                            .toInches();
                }

            }
        });

        // mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.ACCESSOIRE, (evt) -> {
        //     System.out.println("Accessoire changed");
        //     Accessoire.AccessoireDTO accessoireDTO = (Accessoire.AccessoireDTO) evt.getNewValue();

        //     if (accessoireDTO.accessoireId == this.accessoireDTO.accessoireId) {
        //         this.accessoireDTO = accessoireDTO;
        //         props[0][1] = accessoireDTO.accessoireNom;
        //         props[1][1] = ImperialDimension.convertToImperial(accessoireDTO.dimensions[1]);
        //         props[2][1] = ImperialDimension.convertToImperial(accessoireDTO.dimensions[0]);
        //         props[3][1] = ImperialDimension.convertToImperial(accessoireDTO.position[0]);
        //         props[4][1] = ImperialDimension.convertToImperial(accessoireDTO.position[1]);
        //         model.fireTableDataChanged();
        //     }
        // });

        mainWindow.getControleur().addAccessoireEventListener(new AccessoireEventListener() {
            @Override
            public void change(AccessoireEvent event) {
                // TODO Auto-generated method stub
                Accessoire.AccessoireDTO _accessoireDTO = event.getAccessoireDTO();

                if (_accessoireDTO.accessoireId == accessoireDTO.accessoireId) {
                    accessoireDTO = _accessoireDTO;
                    props[0][1] = _accessoireDTO.accessoireNom;
                    props[1][1] = ImperialDimension.convertToImperial(_accessoireDTO.dimensions[1]);
                    props[2][1] = ImperialDimension.convertToImperial(_accessoireDTO.dimensions[0]);
                    props[3][1] = ImperialDimension.convertToImperial(_accessoireDTO.position[0]);
                    props[4][1] = ImperialDimension.convertToImperial(_accessoireDTO.position[1]);
                    model.fireTableDataChanged();
                }

            }

            @Override
            public void add(AccessoireEvent event) {
                // TODO Auto-generated method stub

            }

            @Override
            public void remove(AccessoireEvent event) {
                // TODO Auto-generated method stub

            }
        });
    }

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
        // if (dtoAcessoire.accessoireType == TypeAccessoire.Fenetre) {
        // props[4] = new Object[] { "Position y",
        // ImperialDimension.convertToImperial(dtoAcessoire.position[1]) };
        // }
        return props[row][col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // System.out.println("Setting value at " + rowIndex + "," + columnIndex + " to
        // " + aValue
        // + " (an instance of " + aValue.getClass() + ")");

        if (rowIndex == 0) {
            accessoireDTO.accessoireNom = (String) aValue;
            props[rowIndex][columnIndex] = aValue;
            model.fireTableCellUpdated(rowIndex, columnIndex);
            mainWindow.arbreDesComposantesChalet.rechargerNoeudAccessoire(accessoireDTO);
        }

        if (rowIndex == 1 || rowIndex == 2 || rowIndex == 3 || rowIndex == 4) {
            ImperialDimension dim = ImperialDimension.parseFromString((String) aValue.toString().trim());

            if (dim == null) {
                return;
            }

            accessoireDTO.dimensions[1] = ImperialDimension.parseFromString((String) getValueAt(1, 1).toString())
                    .toInches();
            accessoireDTO.dimensions[0] = ImperialDimension.parseFromString((String) getValueAt(2, 1).toString())
                    .toInches();
            accessoireDTO.position[0] = ImperialDimension.parseFromString((String) getValueAt(3, 1).toString())
                    .toInches();

            if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                accessoireDTO.position[1] = ImperialDimension.parseFromString((String) getValueAt(4, 1).toString())
                        .toInches();
            }
            aValue = dim.toString();
        }

        System.out.println(accessoireDTO.accessoireNom);
        mainWindow.getControleur().setAccessoire(accessoireDTO);
        props[rowIndex][columnIndex] = aValue.toString();

        model.fireTableCellUpdated(rowIndex, columnIndex);
        mainWindow.drawingPanel.afficheur.rechargerAffichage();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Si le type est une porte, on restraint la modification de la valeur y puisque
        // la porte est initialement positionnée au bas du mur.
        if (accessoireDTO.accessoireType == TypeAccessoire.Porte && row == 4 && column == 1) {
            return false;
        }

        return column == 1;
    }

    public TitledBorder getTitledBorder() {
        return titledBorder;
    }

    public void recharger() {
        Accessoire.AccessoireDTO _accessoireDTO = mainWindow.getControleur().getAccessoire(accessoireDTO.accessoireId);
        if (_accessoireDTO == null) {
            return;
        }

        accessoireDTO = _accessoireDTO;
        props[0][1] = accessoireDTO.accessoireNom;
        props[1][1] = ImperialDimension.convertToImperial(accessoireDTO.dimensions[1]);
        props[2][1] = ImperialDimension.convertToImperial(accessoireDTO.dimensions[0]);
        props[3][1] = ImperialDimension.convertToImperial(accessoireDTO.position[0]);
        props[4][1] = ImperialDimension.convertToImperial(accessoireDTO.position[1]);
        model.fireTableDataChanged();
    }
}
