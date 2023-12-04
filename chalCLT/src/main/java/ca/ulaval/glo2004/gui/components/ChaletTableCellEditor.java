package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;

import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;

public class ChaletTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, KeyListener {

    private class SensToitComboBox extends JComboBox<String> {

        public SensToitComboBox() {
            this.setFont(this.getFont().deriveFont(8));
            this.addItem(TypeSensToit.Nord.toString());
            this.addItem(TypeSensToit.Sud.toString());
            this.addItem(TypeSensToit.Est.toString());
            this.addItem(TypeSensToit.Ouest.toString());
            this.setSelectedItem(TypeSensToit.Nord.toString());
        }

    }

    public String valeurCellEditorCourrante;
    public JComboBox<String> sensToitComboBox;
    public JTextField jTextField;
    public String valeurImperialNoFraction;

    protected static final String COMBO = "combo";
    protected static final String TEXT = "text";

    public ChaletTableCellEditor(JTable chaletTable) {
        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("ComboBox.buttonStyle", "button");
        // UIManager.put("ComboBox.buttonArrowColor", Color.CYAN);

        sensToitComboBox = new SensToitComboBox();
        jTextField = new JTextField();
        sensToitComboBox.setActionCommand(COMBO);
        sensToitComboBox.addActionListener(this);
        jTextField.setActionCommand(TEXT);
        jTextField.addActionListener(this);
        jTextField.addKeyListener(this);

        sensToitComboBox.setFocusable(false);
        sensToitComboBox.setRequestFocusEnabled(false);
        sensToitComboBox.setBorder(null);
        sensToitComboBox.setBackground(UIManager.getColor("Table.background"));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String noFractionValue;
        if (row == 6 && column == 1) {
            if (isSelected) {
                valeurCellEditorCourrante = (String) value;
            }
            sensToitComboBox.setSelectedItem(value);
            return sensToitComboBox;
        }

        if (column == 1 && (row == 1 || row == 2 || row == 3 || row == 4 || row == 7 || row == 8)) {
            String stringVal = value.toString();
            String delim = "[\"]";
            String[] parsedString = stringVal.split(delim);

            if (stringVal.contains("0/1")) {
                noFractionValue = parsedString[0] + "\"";
                jTextField.setText((String) noFractionValue);
            } else {
                jTextField.setText((String) value);
            }
        } else {
            jTextField.setText((String) value);
        }
        valeurCellEditorCourrante = getValeurCourranteByPattern();
        return jTextField;
    }

    @Override
    public Object getCellEditorValue() {
        return valeurCellEditorCourrante;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMBO.equals(e.getActionCommand())) {
            this.fireEditingStopped();
        } else {
            valeurCellEditorCourrante = jTextField.getText();
        }

    }

    public String getValeurCourranteByPattern() {
        valeurCellEditorCourrante = jTextField.getText().trim();
        if (valeurCellEditorCourrante.matches(".*[a-z].*")) {
            return valeurCellEditorCourrante;
        }
        Matcher matcheValeurCourrante = ImperialDimension.patternImperial.matcher(valeurCellEditorCourrante);
        String valeurFinale;
        if (matcheValeurCourrante.find()) {
            if (matcheValeurCourrante.group("Pied") == null && matcheValeurCourrante.group("Pouce") == null
                    && matcheValeurCourrante.group("A") == null) {
                return valeurCellEditorCourrante;
            }
            if (matcheValeurCourrante.group("Pied") == null) {
                valeurFinale = " 0'";
            } else {
                valeurFinale = " " + matcheValeurCourrante.group("Pied") + "'";
            }
            if (matcheValeurCourrante.group("Pouce") == null) {
                valeurFinale = valeurFinale + " 0\"";
            } else {
                valeurFinale = valeurFinale + " " + matcheValeurCourrante.group("Pouce") + "\"";
            }
            if (matcheValeurCourrante.group("A") == null) {
                valeurFinale = valeurFinale + " 0/1";
            } else {
                valeurFinale = valeurFinale + " " + matcheValeurCourrante.group("A") + "/"
                        + matcheValeurCourrante.group("B");
            }
            valeurCellEditorCourrante = valeurFinale;
        }
        return valeurCellEditorCourrante;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                valeurCellEditorCourrante = getValeurCourranteByPattern();
                this.fireEditingStopped();
                break;
            case KeyEvent.VK_ESCAPE:
                this.fireEditingCanceled();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
