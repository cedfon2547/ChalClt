package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import ca.ulaval.glo2004.domaine.utils.ImperialDimension;

public class AccessoireTableCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener, KeyListener {

    public String valeurCellEditorCourrante;
    public JTextField jTextField;
    public String valeurImperialNoFraction;

    public AccessoireTableCellEditor(){
        jTextField = new JTextField();
        jTextField.addActionListener(this);
        jTextField.addKeyListener(this);
    }

    @Override
    public Object getCellEditorValue() {
        return valeurCellEditorCourrante;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                // System.out.println("-----2-----");
                // valeurCellEditorCourrante = jTextField.getText();
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
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        valeurCellEditorCourrante = jTextField.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(value != null){
        String noFractionValue;
        String stringVal = value.toString();
        String delim = "[\"]";
        String[] parsedString = stringVal.split(delim);
        // System.out.println(value.toString());
        if (column == 1 && row != 0) {
            if (stringVal.contains("0/1")) {
                // System.out.println("in");
                noFractionValue = parsedString[0] + "\"";
                jTextField.setText((String) noFractionValue);
            } else {
                jTextField.setText((String) value);
            }
        }else {
            jTextField.setText((String) value);
        }
    }
        valeurCellEditorCourrante = getValeurCourranteByPattern();
        return jTextField;
    }

    public String getValeurCourranteByPattern() {
        valeurCellEditorCourrante = jTextField.getText().trim();
        if (valeurCellEditorCourrante.matches(".*[a-z].*")) {
            // System.out.println("lettres");
            return valeurCellEditorCourrante;
        }
        Matcher matcheValeurCourrante = ImperialDimension.patternImperial.matcher(valeurCellEditorCourrante);
        String valeurFinale;
        if (matcheValeurCourrante.find()) {
            // System.out.println(matcheValeurCourrante.group("Pied"));
            // System.out.println(matcheValeurCourrante.group("Pouce"));
            // System.out.println(matcheValeurCourrante.group("A"));
            // System.out.println(matcheValeurCourrante.group("B"));
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
}
