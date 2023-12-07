package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeSupport;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;

public class TableAccessoireV2 extends JTable {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    String[] columnNames = new String[] { "Propriété", "Valeur" };
    JComboBox<TypeSensToit> sensToitComboBox;
    DefaultTableModel model;
    DefaultTableCellRenderer cellRenderer;
    DefaultCellEditor cellEditor;
    DefaultTableCellRenderer propertyNameRenderer;

    Accessoire.AccessoireDTO accessoireDTO;

    public TableAccessoireV2(Accessoire.AccessoireDTO accessoireDTO) {
        this.accessoireDTO = accessoireDTO;

        this.putClientProperty("terminateEditOnFocusLost", true);

        initComponents();
    }

    public PropertyChangeSupport getPcs() {
        return pcs;
    }

    private void initComponents() {
        setTableHeader(getTableHeader());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        model = new DefaultTableModel(getProps(), columnNames) {
            @Override
            public Object getValueAt(int row, int column) {
                return getValueAt(row, column);
            }
        };
        cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };

        cellEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                    int column) {
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }
        };

        propertyNameRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
                label.setHorizontalAlignment(JLabel.LEFT);
                return label;
            }
        };

        cellEditor.setClickCountToStart(1);

        this.setModel(model);
        this.getColumnModel().getColumn(0).setCellRenderer(propertyNameRenderer);
        this.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        this.getColumnModel().getColumn(1).setCellEditor(cellEditor);
    }

    public String[][] getProps() {
        String nom = accessoireDTO.accessoireNom;
        String hauteur = ImperialDimension.convertToImperial(accessoireDTO.dimensions[1]).toString();
        String largeur = ImperialDimension.convertToImperial(accessoireDTO.dimensions[0]).toString();
        String positionX = ImperialDimension.convertToImperial(accessoireDTO.position[0]).toString();
        String positionY = ImperialDimension.convertToImperial(accessoireDTO.position[1]).toString();

        return new String[][] {
                { "Nom", nom },
                { "Hauteur", hauteur },
                { "Largeur", largeur },
                { "Position x", positionX },
                { "Position y", positionY }
        };
    }

    public Accessoire.AccessoireDTO getAccessoireDTO() {
        return accessoireDTO;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return getProps()[row][column];
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 1) {
            String value = (String) aValue;

            switch (row) {
                case 0:
                    String oldName = accessoireDTO.accessoireNom;
                    accessoireDTO.accessoireNom = value;
                    pcs.firePropertyChange("accessoireNom", oldName, value);
                    break;
                case 1:
                    double oldHauteur = accessoireDTO.dimensions[1];
                    accessoireDTO.dimensions[1] = ImperialDimension.parseFromString(value).toInches();
                    pcs.firePropertyChange("hauteur", oldHauteur, accessoireDTO.dimensions[1]);
                    break;
                case 2:
                    double oldLargeur = accessoireDTO.dimensions[0];
                    accessoireDTO.dimensions[0] = ImperialDimension.parseFromString(value).toInches();
                    pcs.firePropertyChange("largeur", oldLargeur, accessoireDTO.dimensions[0]);
                    break;
                case 3:
                    double oldPositionX = accessoireDTO.position[0];
                    accessoireDTO.position[0] = ImperialDimension.parseFromString(value).toInches();
                    System.out.println(accessoireDTO.position[0]);
                    pcs.firePropertyChange("positionX", oldPositionX, accessoireDTO.position[0]);
                    break;
                case 4:
                    double oldPositionY = accessoireDTO.position[1];
                    accessoireDTO.position[1] = ImperialDimension.parseFromString(value).toInches();
                    pcs.firePropertyChange("positionY", oldPositionY, accessoireDTO.position[1]);
                    break;
            }
        }
        model.fireTableCellUpdated(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (accessoireDTO.accessoireType == TypeAccessoire.Porte && this.getProps()[row][0].equals("Position y")) {
            return false;
        }
        
        return column == 1;
    }

    public void updateTable(Accessoire.AccessoireDTO accessoireDTO) {
        this.accessoireDTO = accessoireDTO;
        this.model = new DefaultTableModel(getProps(), columnNames);
        setModel(this.model);
        this.getColumnModel().getColumn(0).setCellRenderer(propertyNameRenderer);
        this.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        this.getColumnModel().getColumn(1).setCellEditor(cellEditor);
        this.model.fireTableDataChanged();
    }
}
