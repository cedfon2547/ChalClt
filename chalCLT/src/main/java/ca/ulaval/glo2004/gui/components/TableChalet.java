package ca.ulaval.glo2004.gui.components;

import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;

import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Chalet.ChaletDTO;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.gui.MainWindow;

public class TableChalet extends JTable {
    private MainWindow mainWindow;
    private ChaletDTO dtoChalet;
    private String[] columnNames;
    private Object[][] props;
    private javax.swing.table.DefaultTableModel model;
    private TitledBorder titledBorder;

    public TableChalet(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.dtoChalet = mainWindow.getControleur().getChalet();
        this.columnNames = new String[] {
                "Propriété",
                "Valeur"
        };
        this.props = new Object[][] {
                { "Nom", dtoChalet.nom },
                { "Hauteur", ImperialDimension.convertToImperial(dtoChalet.hauteur) },
                { "Largeur", ImperialDimension.convertToImperial(dtoChalet.largeur) },
                { "Longueur", ImperialDimension.convertToImperial(dtoChalet.longueur) },
                { "Épaisseur panneaux", ImperialDimension.convertToImperial(dtoChalet.epaisseurMur) },
                { "Angle du toit", dtoChalet.angleToit },
                { "Sens du toit", dtoChalet.sensToit },
                {" Marge accessoire", ImperialDimension.convertToImperial(dtoChalet.margeAccessoire)},
        };
        model = new javax.swing.table.DefaultTableModel(props, columnNames);
        this.setModel(model);
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Propriétés du chalet");
        this.setTableHeader(this.getTableHeader());

        this.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                // System.out.println("Table changed" + " " + evt.getFirstRow() + " " + evt.getLastRow() + " "
                //         + evt.getColumn() + " " + getValueAt(evt.getFirstRow(), evt.getColumn()));

                Chalet.ChaletDTO chaletDTO = mainWindow.getControleur().getChalet();
                chaletDTO.nom = (String) getValueAt(0, 1);
                chaletDTO.hauteur = ImperialDimension.parseFromString((String) getValueAt(1, 1).toString()).toInches();
                chaletDTO.largeur = ImperialDimension.parseFromString((String) getValueAt(2, 1).toString()).toInches();
                chaletDTO.longueur = ImperialDimension.parseFromString((String) getValueAt(3, 1).toString()).toInches();
                chaletDTO.epaisseurMur = ImperialDimension.parseFromString((String) getValueAt(4, 1).toString())
                        .toInches();
                chaletDTO.angleToit = Double.parseDouble(getValueAt(5, 1).toString());
                chaletDTO.sensToit = TypeSensToit.valueOf(getValueAt(6, 1).toString());
                chaletDTO.margeAccessoire = ImperialDimension.parseFromString((String) getValueAt(7, 1).toString()).toInches();

                mainWindow.getControleur().setChalet(chaletDTO);
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
            // System.out.println("Dimension: " + dim);
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
