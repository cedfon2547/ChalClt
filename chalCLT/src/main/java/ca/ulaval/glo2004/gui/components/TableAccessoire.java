package ca.ulaval.glo2004.gui.components;

import java.util.UUID;

import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Accessoire.AccessoireDTO;
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

        model = new javax.swing.table.DefaultTableModel(props, columnNames);
        this.setModel(model);
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Propriétés de l'accessoire");

        this.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                System.out.println("Table changed" + " " + evt.getFirstRow() + " " + evt.getLastRow() + " "
                        + evt.getColumn() + " " + getValueAt(evt.getFirstRow(), evt.getColumn()));
            
                Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur().getAccessoire(dtoAcessoire.accessoireId);

                accessoireDTO.accessoireNom = (String)getValueAt(0, 1);

                //todo pour le reste des attributs
                mainWindow.getControleur().setAccessoire(accessoireDTO.typeMur,accessoireDTO);
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

        if (rowIndex == 1 || rowIndex == 2 || rowIndex == 3 || rowIndex == 4) {
            ImperialDimension dim = ImperialDimension.parseFromString((String) aValue);
            System.out.println("Dimension: " + dim);
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
        return column == 1;
    }

    public TitledBorder getTitledBorder() {
        return titledBorder;
    }
}
