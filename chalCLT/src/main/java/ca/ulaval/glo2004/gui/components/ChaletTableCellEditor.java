package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import ca.ulaval.glo2004.domaine.TypeSensToit;

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
        sensToitComboBox = new SensToitComboBox();
        jTextField = new JTextField();
        sensToitComboBox.setActionCommand(COMBO);
        sensToitComboBox.addActionListener(this);
        jTextField.setActionCommand(TEXT);
        jTextField.addActionListener(this);
        jTextField.addKeyListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String noFractionValue;
        if (row == 6 && column == 1) {
            if (isSelected) {
                valeurCellEditorCourrante = (String) value;
            }

            return sensToitComboBox;
        }

        if (column == 1 && (row == 1 || row == 2 || row == 3 || row == 4 || row == 7 || row == 8)) {
            String stringVal = value.toString();
            String delim = "[\"]";
            String[] parsedString = stringVal.split(delim);
            // System.out.println(value.toString());

            if (stringVal.contains("0/1")) {
                // System.out.println("in");
                noFractionValue = parsedString[0] + "\"";
                jTextField.setText((String) noFractionValue);
            } else {
                jTextField.setText((String) value);
            }
        } else {
            jTextField.setText((String) value);
        }
        valeurCellEditorCourrante = getValeurCourrante();
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
            // String valeurRetourne = jTextField.getText();
            System.out.println("-----3-----");
            valeurCellEditorCourrante = jTextField.getText();
        }

    }

    public String getValeurCourrante() {
        valeurCellEditorCourrante = jTextField.getText();
        // si lettre en entrée
        if (valeurCellEditorCourrante.matches(".*[a-z].*")) {
            System.out.println("lettres");
            return valeurCellEditorCourrante;
        }
        String valeurFinale;
        String[] parsedString;
        String delim;
        // lorsqu'une entrée contient un ' pour pieds mais pas de "
        // pour pouces.
        if (valeurCellEditorCourrante.contains("\'") && !valeurCellEditorCourrante.contains("\"")) {
            delim = "[\']";
            parsedString = valeurCellEditorCourrante.split(delim);
            // lorsqu'une entrée contient EXACTEMENT QUE ' pour pieds.
            if (valeurCellEditorCourrante.equals("'")) {
                System.out.println("seulement pieds no spaces");
                return valeurCellEditorCourrante;
            }
            // lorsqu'il y a des pieds et une fraction de pouce
            if (valeurCellEditorCourrante.contains("/")) {
                if (parsedString[1].contains("/")) {
                    System.out.println("pieds et fraction");
                    valeurFinale = " " + parsedString[0] + "' 0\" " + parsedString[1];
                    valeurCellEditorCourrante = valeurFinale;
                    return valeurCellEditorCourrante;
                }
            }

            System.out.println("chiffre(s) et pieds");
            valeurFinale = " " + parsedString[0] + "' 0\" 0/1";
            valeurCellEditorCourrante = valeurFinale;
            return valeurCellEditorCourrante;
        }
        // lorsqu'une entrée contient un " pour pouces mais pas de ' pour pieds
        if (valeurCellEditorCourrante.contains("\"") && !valeurCellEditorCourrante.contains("'")) {
            delim = "[\"]";
            parsedString = valeurCellEditorCourrante.split(delim);

            // lorsqu'une entrée contient EXACTEMENT QUE " pour pouces.
            if (valeurCellEditorCourrante.equals("\"")) {
                System.out.println("seulement pouces no spaces");
                return valeurCellEditorCourrante;
            }

            // lorsqu'il y a des pouces et une fraction de pouce
            if (valeurCellEditorCourrante.contains("/")) {
                if (parsedString[1].contains("/")) {
                    System.out.println("pouces et fraction");
                    valeurFinale = " 0' " + parsedString[0] + "\" " + parsedString[1];
                    valeurCellEditorCourrante = valeurFinale;
                    return valeurCellEditorCourrante;
                }
            }
            System.out.println("chiffre(s) et pouces");
            valeurFinale = " 0' " + parsedString[0] + "\" 0/1";
            valeurCellEditorCourrante = valeurFinale;
            return valeurCellEditorCourrante;
        }

        if ((valeurCellEditorCourrante.contains("\'") || valeurCellEditorCourrante.contains("\""))
                && !valeurCellEditorCourrante.contains("/")) {
            valeurFinale = valeurCellEditorCourrante + " 0/1";
            valeurCellEditorCourrante = valeurFinale;
        }

        if ((valeurCellEditorCourrante.contains("\'") && valeurCellEditorCourrante.contains("\""))) {
            delim = "['\"]+";
            parsedString = valeurCellEditorCourrante.split(delim);

            if (valeurCellEditorCourrante.contains("/")) {
                if (parsedString[2].contains("/")) {
                    valeurFinale = " " + parsedString[0] + "' " + parsedString[1] + "\" " + parsedString[2];
                    valeurCellEditorCourrante = valeurFinale;
                    return valeurCellEditorCourrante;
                }
            }
            valeurFinale = " " + parsedString[0] + "' " + parsedString[1] + "\"";
            valeurCellEditorCourrante = valeurFinale;
            return valeurCellEditorCourrante;
        }
        if (valeurCellEditorCourrante.matches("^[0-9]+\\/[0-9]+[ ?]*$")) {
            valeurFinale = " 0' 0\" " + valeurCellEditorCourrante;
            valeurCellEditorCourrante = valeurFinale;
            return valeurCellEditorCourrante;
        }
        return valeurCellEditorCourrante;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                // System.out.println("-----2-----");
                // valeurCellEditorCourrante = jTextField.getText();
                valeurCellEditorCourrante = getValeurCourrante();
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
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'keyReleased'");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }
}
