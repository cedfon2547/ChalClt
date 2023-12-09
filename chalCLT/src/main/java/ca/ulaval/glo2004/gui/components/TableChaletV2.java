package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;

public class TableChaletV2 extends JTable {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    String[] columnNames = new String[] { "Propriété", "Valeur" };
    JComboBox<TypeSensToit> sensToitComboBox;
    DefaultTableModel model;
    DefaultTableCellRenderer cellRenderer;
    DefaultCellEditor cellEditor;
    DefaultTableCellRenderer propertyNameRenderer;

    Chalet.ChaletDTO chaletDTO;

    public TableChaletV2(Chalet.ChaletDTO chaletDTO) {
        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("ComboBox.buttonStyle", "button");

        this.sensToitComboBox = new JComboBox<TypeSensToit>(TypeSensToit.values());
        this.chaletDTO = chaletDTO;

        this.sensToitComboBox.setBorder(null);
        this.sensToitComboBox.setBackground(UIManager.getColor("Table.background"));

        this.putClientProperty("terminateEditOnFocusLost", true);

        initComponents();
    }

    public PropertyChangeSupport getPcs() {
        return pcs;
    }

    Component[][] components = new Component[9][2];

    private void initComponents() {
        setTableHeader(getTableHeader());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        model = new DefaultTableModel(this.getProps(), columnNames);
        cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                if (row == 8) {
                    return sensToitComboBox;
                }

                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };

        cellEditor = new DefaultCellEditor(new javax.swing.JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                    int column) {
                if (row == 8) {
                    return sensToitComboBox;
                }

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
        this.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        this.getColumnModel().getColumn(1).setCellEditor(cellEditor);
        this.getColumnModel().getColumn(0).setCellRenderer(propertyNameRenderer);

        sensToitComboBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                sensToitComboBox.showPopup();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                System.out.println("comboBox focus lost");
                cellEditor.stopCellEditing();
            }
        });

        sensToitComboBox.addActionListener((evt) -> {
            // System.out.println("comboBox action");
            cellEditor.stopCellEditing();
        });
    }

    public String[][] getProps() {
        String nom = chaletDTO.nom;
        String hauteur = ImperialDimension.convertToImperial(chaletDTO.hauteur).toString();
        String largeur = ImperialDimension.convertToImperial(chaletDTO.largeur).toString();
        String longueur = ImperialDimension.convertToImperial(chaletDTO.longueur).toString();
        String epaisseurPanneaux = ImperialDimension.convertToImperial(chaletDTO.epaisseurMur).toString();
        String margeAccessoire = ImperialDimension.convertToImperial(chaletDTO.margeAccessoire).toString();
        String margeSupp = ImperialDimension.convertToImperial(chaletDTO.margeSupplementaireRetrait).toString();
        String angleToit = String.format(Locale.CANADA, "%.2f", chaletDTO.angleToit);
        String sensToit = chaletDTO.sensToit.name();

        return new String[][] {
                { "Nom", nom },
                { "Hauteur", hauteur },
                { "Largeur", largeur },
                { "Longueur", longueur },
                { "Épaisseur des murs", epaisseurPanneaux },
                { "Marge accessoires", margeAccessoire },
                { "Marge supplémentaire", margeSupp },
                { "Angle du toit", angleToit },
                { "Sens du toit", sensToit },
        };
    }

    public Chalet.ChaletDTO getChaletDTO() {
        return chaletDTO;
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
                    String oldName = chaletDTO.nom;
                    chaletDTO.nom = value;
                    this.pcs.firePropertyChange("nom", oldName, value);

                    break;
                case 1:
                    double oldHauteur = chaletDTO.hauteur;
                    ImperialDimension hauteur = ImperialDimension.parseFromString(value.toString());
                    if (hauteur == null) {
                        return;
                    }

                    chaletDTO.hauteur = hauteur.toInches();
                    this.pcs.firePropertyChange("hauteur", oldHauteur, hauteur.toInches());
                    break;
                case 2:
                    double oldLargeur = chaletDTO.largeur;
                    ImperialDimension largeur = ImperialDimension.parseFromString(value.toString());
                    if (largeur == null) {
                        return;
                    }
                    
                    

                    chaletDTO.largeur = largeur.toInches();
                    this.pcs.firePropertyChange("largeur", oldLargeur, largeur.toInches());
                    break;
                case 3:
                    double oldLongueur = chaletDTO.longueur;
                    ImperialDimension longueur = ImperialDimension.parseFromString(value.toString());
                    if (longueur == null) {
                        return;
                    }
                    

                    chaletDTO.longueur = longueur.toInches();
                    this.pcs.firePropertyChange("longueur", oldLongueur, longueur.toInches());
                    break;
                case 4:
                    double oldEpaisseur = chaletDTO.epaisseurMur;
                    ImperialDimension epaisseurPanneaux = ImperialDimension.parseFromString(value.toString());
                    if (epaisseurPanneaux == null) {
                        return;
                    }
                    chaletDTO.epaisseurMur = epaisseurPanneaux.toInches();
                    this.pcs.firePropertyChange("epaisseurMur", oldEpaisseur, epaisseurPanneaux.toInches());
                    break;
                case 5:
                    double oldMargeAccessoire = chaletDTO.margeAccessoire;
                    ImperialDimension margeAccessoire = ImperialDimension.parseFromString(value.toString());
                    if (margeAccessoire == null) {
                        return;
                    }

                    chaletDTO.margeAccessoire = margeAccessoire.toInches();
                    this.pcs.firePropertyChange("margeAccessoire", oldMargeAccessoire, margeAccessoire.toInches());
                    break;
                case 6:
                    double oldMargeSupp = chaletDTO.margeSupplementaireRetrait;
                    ImperialDimension margeSupp = ImperialDimension.parseFromString(value.toString());
                    if (margeSupp == null) {
                        return;
                    }

                    chaletDTO.margeSupplementaireRetrait = margeSupp.toInches();
                    this.pcs.firePropertyChange("margeSupplementaireRetrait", oldMargeSupp, margeSupp.toInches());
                    break;
                case 7:
                    double oldAngleToit = chaletDTO.angleToit;
                    chaletDTO.angleToit = Double.parseDouble(value);
                    this.pcs.firePropertyChange("angleToit", oldAngleToit, Double.parseDouble(value));
                    break;
                case 8:
                    TypeSensToit oldSensToit = chaletDTO.sensToit;
                    TypeSensToit sensToit = TypeSensToit.valueOf(sensToitComboBox.getSelectedItem().toString());
                    chaletDTO.sensToit = sensToit;
                    this.pcs.firePropertyChange("sensToit", oldSensToit, sensToit);
                    break;

            }
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1;
    }

    public void updateTable(Chalet.ChaletDTO chaletDTO) {
        this.chaletDTO = chaletDTO;
        this.sensToitComboBox.setSelectedItem(chaletDTO.sensToit);
        this.model.setDataVector(this.getProps(), columnNames);
        this.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        this.getColumnModel().getColumn(1).setCellEditor(cellEditor);
        this.getColumnModel().getColumn(0).setCellRenderer(propertyNameRenderer);
        model.fireTableDataChanged();
    }
}
