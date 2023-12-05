package ca.ulaval.glo2004.gui.components;

import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.*;

import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Chalet.ChaletDTO;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.gui.MainWindow;

class TableComboBoxRenderer implements TableCellRenderer {
    private JComboBox<TypeSensToit> comboBox;

    public TableComboBoxRenderer(JComboBox<TypeSensToit> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (Arrays.asList(TypeSensToit.getNames()).contains(value)) {
            if (isSelected) {
                comboBox.setForeground(table.getSelectionForeground());
                comboBox.setBackground(table.getSelectionBackground());
            } else {
                comboBox.setForeground(table.getForeground());
                comboBox.setBackground(table.getBackground());
            }
            comboBox.setSelectedItem(value);

            return comboBox;
        }

        return new JLabel(value.toString());
    }
}

class TableComboBoxEditor extends DefaultCellEditor {
    public TableComboBoxEditor(JComboBox<TypeSensToit> comboBox) {
        super(comboBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}

public class TableChalet extends JTable {
    private MainWindow mainWindow;
    private ChaletDTO dtoChalet;
    private String[] columnNames;
    private Object[][] props;
    private javax.swing.table.DefaultTableModel model;
    private TitledBorder titledBorder;
    private ChaletTableCellEditor chaletTableCellEditor;
    private ChaletTableCellRenderer chaletTableCellRenderer;
    JComboBox<TypeSensToit> comboBox;

    public TableChalet(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.dtoChalet = mainWindow.getControleur().getChalet();
        this.chaletTableCellEditor = new ChaletTableCellEditor(this);
        this.chaletTableCellRenderer = new ChaletTableCellRenderer(chaletTableCellEditor);

        // this.chaletTableCellEditor.sensToitComboBox.addActionListener((evt) -> {
        // // System.out.println("Combobox Selected " +
        // chaletTableCellEditor.sensToitComboBox.getSelectedItem().toString());
        // setValueAt(chaletTableCellEditor.sensToitComboBox.getSelectedItem().toString(),
        // 6, 1);
        // });
        // this.setCellEditor(new DefaultCellEditor(sensToitComboBox));

        this.columnNames = new String[] {
                "Propriété",
                "Valeur"
        };

        this.props = new Object[][] {
                { "Nom", dtoChalet.nom },
                { "Hauteur", ImperialDimension.convertToImperial(dtoChalet.hauteur).toString() },
                { "Largeur", ImperialDimension.convertToImperial(dtoChalet.largeur).toString() },
                { "Longueur", ImperialDimension.convertToImperial(dtoChalet.longueur).toString() },
                { "Épaisseur panneaux", ImperialDimension.convertToImperial(dtoChalet.epaisseurMur).toString() },
                { "Angle du toit", String.format("%s", dtoChalet.angleToit) },
                { "Sens du toit", String.format("%s", dtoChalet.sensToit) },
                { "Marge accessoire", ImperialDimension.convertToImperial(dtoChalet.margeAccessoire).toString() },
                { "Marge supplémentaire",
                        ImperialDimension.convertToImperial(dtoChalet.margeSupplementaireRetrait).toString() },
        };

        model = new javax.swing.table.DefaultTableModel(props, columnNames);

        this.setModel(model);
        // this.getColumnModel().getColumn(1).setCellEditor(chaletTableCellEditor);
        // this.getColumnModel().getColumn(1).setCellRenderer(chaletTableCellRenderer);
        TableColumn col = this.getColumnModel().getColumn(1);
        comboBox = new JComboBox<TypeSensToit>(TypeSensToit.values());
        col.setCellEditor(new TableComboBoxEditor(comboBox));
        col.setCellRenderer(new TableComboBoxRenderer(comboBox));

        comboBox.addActionListener((evt) -> {
            // System.out.println("Combobox Selected " +
            // chaletTableCellEditor.sensToitComboBox.getSelectedItem().toString());
            setValueAt(comboBox.getSelectedItem().toString(), 6, 1);
            comboBox.setSelectedItem(comboBox.getSelectedItem().toString());
        });

        titledBorder = javax.swing.BorderFactory.createTitledBorder("Propriétés du chalet");
        this.setTableHeader(this.getTableHeader());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent evt) {
                // System.out.println("Table changed" + " " + evt.getFirstRow() + " " +
                // evt.getLastRow() + " "
                // + evt.getColumn() + " " + getValueAt(evt.getFirstRow(), evt.getColumn()));

                Chalet.ChaletDTO chaletDTO = dtoChalet; // mainWindow.getControleur().getChalet();

                chaletDTO.nom = (String) getValueAt(0, 1);
                chaletDTO.hauteur = ImperialDimension.parseFromString((String) getValueAt(1, 1).toString()).toInches();
                chaletDTO.largeur = ImperialDimension.parseFromString((String) getValueAt(2, 1).toString()).toInches();
                chaletDTO.longueur = ImperialDimension.parseFromString((String) getValueAt(3, 1).toString()).toInches();
                chaletDTO.epaisseurMur = ImperialDimension.parseFromString((String) getValueAt(4, 1).toString())
                        .toInches();
                chaletDTO.angleToit = Double.parseDouble(getValueAt(5, 1).toString());
                chaletDTO.sensToit = TypeSensToit.valueOf(getValueAt(6, 1).toString());
                chaletDTO.margeAccessoire = ImperialDimension.parseFromString((String) getValueAt(7, 1).toString())
                        .toInches();
                chaletDTO.margeSupplementaireRetrait = ImperialDimension
                        .parseFromString((String) getValueAt(8, 1).toString()).toInches();

                // System.out.println("Sens Toit 1 " + chaletDTO.sensToit + " " + props[6][1] +
                // " " + getValueAt(6, 1));
                // mainWindow.getControleur().setChalet(chaletDTO);

                // mainWindow.recharger();
                mainWindow.drawingPanel.afficheur.rechargerAffichage();
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
        // System.out.println("Table changed" + " " + rowIndex + " " + columnIndex + " "
        // + aValue.toString() + " " + getValueAt(rowIndex, columnIndex));
        if (rowIndex == 1 || rowIndex == 2 || rowIndex == 3 || rowIndex == 4) {
            ImperialDimension dim = ImperialDimension.parseFromString((String) aValue.toString());
            if (dim == null) {
                return;
            }

            props[rowIndex][columnIndex] = dim.toString();
            updateChalet();
            model.fireTableCellUpdated(rowIndex, columnIndex);
            return;
        }

        if (rowIndex == 6) {
            props[rowIndex][columnIndex] = chaletTableCellEditor.sensToitComboBox.getSelectedItem().toString();
            updateChalet();
            model.fireTableCellUpdated(rowIndex, columnIndex);
            return;
        }

        props[rowIndex][columnIndex] = (String) aValue.toString();
        updateChalet();
        model.fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1; // && row != 6;
    }

    public TitledBorder getTitledBorder() {
        return titledBorder;
    }

    private void updateChalet() {
        dtoChalet.nom = (String) getValueAt(0, 1);
        dtoChalet.hauteur = ImperialDimension.parseFromString((String) getValueAt(1, 1).toString()).toInches();
        dtoChalet.largeur = ImperialDimension.parseFromString((String) getValueAt(2, 1).toString()).toInches();
        dtoChalet.longueur = ImperialDimension.parseFromString((String) getValueAt(3, 1).toString()).toInches();
        dtoChalet.epaisseurMur = ImperialDimension.parseFromString((String) getValueAt(4, 1).toString()).toInches();
        dtoChalet.angleToit = Double.parseDouble(getValueAt(5, 1).toString());
        dtoChalet.sensToit = TypeSensToit.valueOf(getValueAt(6, 1).toString());
        dtoChalet.margeAccessoire = ImperialDimension.parseFromString((String) getValueAt(7, 1).toString()).toInches();
        dtoChalet.margeSupplementaireRetrait = ImperialDimension.parseFromString((String) getValueAt(8, 1).toString())
                .toInches();

        mainWindow.getControleur().setChalet(dtoChalet);
    }

    public void recharger() {
        Chalet.ChaletDTO chaletDTO = mainWindow.getControleur().getChalet();
        this.dtoChalet = chaletDTO;

        // setValueAt(chaletDTO.nom, 0, 1);
        // setValueAt(ImperialDimension.convertToImperial(chaletDTO.hauteur).toString(),
        // 1, 1);
        // setValueAt(ImperialDimension.convertToImperial(chaletDTO.largeur).toString(),
        // 2, 1);
        // setValueAt(ImperialDimension.convertToImperial(chaletDTO.longueur).toString(),
        // 3, 1);
        // setValueAt(ImperialDimension.convertToImperial(chaletDTO.epaisseurMur).toString(),
        // 4, 1);
        // setValueAt(String.format("%s", chaletDTO.angleToit), 5, 1);
        // setValueAt(String.format("%s", chaletDTO.sensToit), 6, 1);
        // setValueAt(ImperialDimension.convertToImperial(chaletDTO.margeAccessoire).toString(),
        // 7, 1);
        // setValueAt(ImperialDimension.convertToImperial(chaletDTO.margeSupplementaireRetrait).toString(),
        // 8, 1);
        // model.fireTableChanged(null);
        model.fireTableDataChanged();
    }
}
