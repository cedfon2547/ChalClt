package ca.ulaval.glo2004.gui.components;


import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.gui.MainWindow;

public class TableAccessoire extends JTable {
    private MainWindow mainWindow;
    private Accessoire.AccessoireDTO dtoAcessoire;
    private String[] columnNames;
    private Object[][] props;
    private javax.swing.table.DefaultTableModel model;
    private TitledBorder titledBorder;

    public TableAccessoire(MainWindow mainWindow,Accessoire.AccessoireDTO dtoAcessoire) {
        this.mainWindow = mainWindow;
        this.dtoAcessoire = dtoAcessoire;
        
        columnNames = new String[] {
                "Propriété",
                "Valeur"
        };

        props = new Object[][] {
                { "Nom", dtoAcessoire.accessoireNom},
                { "Hauteur", ImperialDimension.convertToImperial(dtoAcessoire.dimensions[1]) },
                { "Largeur", ImperialDimension.convertToImperial(dtoAcessoire.dimensions[0]) },
                { "Position x", ImperialDimension.convertToImperial(dtoAcessoire.position[0]) },
                { "Position y", ImperialDimension.convertToImperial(dtoAcessoire.position[1]) }
        };

        // if (dtoAcessoire.accessoireType == TypeAccessoire.Fenetre) {
        //     props[4] = new Object[] { "Position y", ImperialDimension.convertToImperial(dtoAcessoire.position[1]) };
        // }

        model = new javax.swing.table.DefaultTableModel(props, columnNames);
        this.setModel(model);
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Propriétés de l'accessoire");

        this.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                System.out.println("Table changed" + " " + evt.getFirstRow() + " " + evt.getLastRow() + " "
                        + evt.getColumn() + " " + getValueAt(evt.getFirstRow(), evt.getColumn()));
            
                Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur().getAccessoire(dtoAcessoire.accessoireId);

                accessoireDTO.accessoireNom = (String) getValueAt(0, 1);
                accessoireDTO.dimensions[1] = ImperialDimension.parseFromString((String) getValueAt(1, 1).toString()).toInches();
                accessoireDTO.dimensions[0] = ImperialDimension.parseFromString((String) getValueAt(2, 1).toString()).toInches();
                accessoireDTO.position[0] = ImperialDimension.parseFromString((String) getValueAt(3, 1).toString()).toInches();
                
                if (dtoAcessoire.accessoireType == TypeAccessoire.Fenetre) {
                    accessoireDTO.position[1] = ImperialDimension.parseFromString((String) getValueAt(4, 1).toString()).toInches();
                }

                mainWindow.getControleur().setAccessoire(accessoireDTO.typeMur, accessoireDTO);
                mainWindow.arbreDesComposantesChalet.rechargerNoeudAccessoire(accessoireDTO);
                // mainWindow.arbreDesComposantesChalet.invalidate();
                // mainWindow.arbreDesComposantesChalet.repaint();
                mainWindow.drawingPanel.rechargerAffichage();
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
        //     props[4] = new Object[] { "Position y", ImperialDimension.convertToImperial(dtoAcessoire.position[1]) };
        // } 
        return props[row][col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // System.out.println("Setting value at " + rowIndex + "," + columnIndex + " to " + aValue
        //         + " (an instance of " + aValue.getClass() + ")");

        if (rowIndex == 1 || rowIndex == 2 || rowIndex == 3 || rowIndex == 4) {
            ImperialDimension dim = ImperialDimension.parseFromString((String) aValue);
            
            if (dim == null) {
                return;
            }
            
            props[rowIndex][columnIndex] = dim.toString();
            model.fireTableCellUpdated(rowIndex, columnIndex);
            return;
        }

        props[rowIndex][columnIndex] = aValue;
        model.fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Si le type est une porte, on restraint la modification de la valeur y puisque la porte est initialement positionnée au bas du mur.
        if (dtoAcessoire.accessoireType == TypeAccessoire.Porte && row == 4 && column == 1) {
            return false;
        }  

        return column == 1;
    }

    public TitledBorder getTitledBorder() {
        return titledBorder;
    }
}
