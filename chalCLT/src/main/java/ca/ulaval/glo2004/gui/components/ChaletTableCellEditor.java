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
            // String valeurRetourne = jTextField.getText();
            // System.out.println("-----3-----");
            valeurCellEditorCourrante = jTextField.getText();
        }

    }

    // public String getValeurCourrante() {
    //     valeurCellEditorCourrante = jTextField.getText();
    //     // si lettre en entrée
    //     if (valeurCellEditorCourrante.matches(".*[a-z].*")) {
    //         // System.out.println("lettres");
    //         return valeurCellEditorCourrante;
    //     }
    //     String valeurFinale;
    //     String[] parsedString;
    //     String delim;
    //     // lorsqu'une entrée contient un ' pour pieds mais pas de "
    //     // pour pouces.
    //     if (valeurCellEditorCourrante.contains("\'") && !valeurCellEditorCourrante.contains("\"")) {
    //         delim = "[\']";
    //         parsedString = valeurCellEditorCourrante.split(delim);
    //         // lorsqu'une entrée contient EXACTEMENT QUE ' pour pieds.
    //         if (valeurCellEditorCourrante.equals("'")) {
    //             // System.out.println("seulement pieds no spaces");
    //             return valeurCellEditorCourrante;
    //         }
    //         // lorsqu'il y a des pieds et une fraction de pouce
    //         if (valeurCellEditorCourrante.contains("/")) {
    //             if (parsedString[1].contains("/")) {
    //                 // System.out.println("pieds et fraction");
    //                 valeurFinale = " " + parsedString[0] + "' 0\" " + parsedString[1];
    //                 valeurCellEditorCourrante = valeurFinale;
    //                 return valeurCellEditorCourrante;
    //             }
    //         }

    //         // System.out.println("chiffre(s) et pieds");
    //         valeurFinale = " " + parsedString[0] + "' 0\" 0/1";
    //         valeurCellEditorCourrante = valeurFinale;
    //         return valeurCellEditorCourrante;
    //     }
    //     // lorsqu'une entrée contient un " pour pouces mais pas de ' pour pieds
    //     if (valeurCellEditorCourrante.contains("\"") && !valeurCellEditorCourrante.contains("'")) {
    //         delim = "[\"]";
    //         parsedString = valeurCellEditorCourrante.split(delim);

    //         // lorsqu'une entrée contient EXACTEMENT QUE " pour pouces.
    //         if (valeurCellEditorCourrante.equals("\"")) {
    //             // System.out.println("seulement pouces no spaces");
    //             return valeurCellEditorCourrante;
    //         }

    //         // lorsqu'il y a des pouces et une fraction de pouce
    //         if (valeurCellEditorCourrante.contains("/")) {
    //             if (parsedString[1].contains("/")) {
    //                 // System.out.println("pouces et fraction");
    //                 valeurFinale = " 0' " + parsedString[0] + "\" " + parsedString[1];
    //                 valeurCellEditorCourrante = valeurFinale;
    //                 return valeurCellEditorCourrante;
    //             }
    //         }
    //         // System.out.println("chiffre(s) et pouces");
    //         valeurFinale = " 0' " + parsedString[0] + "\" 0/1";
    //         valeurCellEditorCourrante = valeurFinale;
    //         return valeurCellEditorCourrante;
    //     }

    //     if ((valeurCellEditorCourrante.contains("\'") || valeurCellEditorCourrante.contains("\""))
    //             && !valeurCellEditorCourrante.contains("/")) {
    //         valeurFinale = valeurCellEditorCourrante + " 0/1";
    //         valeurCellEditorCourrante = valeurFinale;
    //     }

    //     if ((valeurCellEditorCourrante.contains("\'") && valeurCellEditorCourrante.contains("\""))) {
    //         delim = "['\"]+";
    //         parsedString = valeurCellEditorCourrante.split(delim);

    //         if (valeurCellEditorCourrante.contains("/")) {
    //             if (parsedString[2].contains("/")) {
    //                 valeurFinale = " " + parsedString[0] + "' " + parsedString[1] + "\" " + parsedString[2];
    //                 valeurCellEditorCourrante = valeurFinale;
    //                 return valeurCellEditorCourrante;
    //             }
    //         }
    //         valeurFinale = " " + parsedString[0] + "' " + parsedString[1] + "\"";
    //         valeurCellEditorCourrante = valeurFinale;
    //         return valeurCellEditorCourrante;
    //     }
    //     if (valeurCellEditorCourrante.matches("^[0-9]+\\/[0-9]+[ ?]*$")) {
    //         valeurFinale = " 0' 0\" " + valeurCellEditorCourrante;
    //         valeurCellEditorCourrante = valeurFinale;
    //         return valeurCellEditorCourrante;
    //     }
    //     return valeurCellEditorCourrante;
    // }

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
            }else{
                valeurFinale = valeurFinale + " " + matcheValeurCourrante.group("A") + "/" + matcheValeurCourrante.group("B");
            }
            valeurCellEditorCourrante = valeurFinale;
        }
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
        // throw new UnsupportedOperationException("Unimplemented method
        // 'keyReleased'");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    // Pattern r = Pattern.compile("((?<Pied>[0-9]+)?[ ?]*'[
    // ?]*)?((?<Pouce>[0-9]+)?[ ?]*\"[ ?]*)?((?<A>[0-9]+)/(?<B>[0-9]+)[ ?]*$)?");

    // Matcher matches1 = r.matcher("1' 2\" 3/4");
    // Matcher matches2 = r.matcher("2\" 3/4");
    // Matcher matches3 = r.matcher("3/4");
    // Matcher matches4 = r.matcher("1' 2\"");
    // Matcher matches5 = r.matcher("1' 2");

    // if (matches1.find()) {
    // System.out.println(matches1.group("Pied"));
    // System.out.println(matches1.group("Pouce"));
    // System.out.println(matches1.group("A"));
    // System.out.println(matches1.group("B"));
    // }
    // if (matches2.find()) {
    // System.out.println(matches2.group("Pied"));
    // System.out.println(matches2.group("Pouce"));
    // System.out.println(matches2.group("A"));
    // System.out.println(matches2.group("B"));
    // }
    // if (matches3.find()) {
    // System.out.println(matches3.group("Pied"));
    // System.out.println(matches3.group("Pouce"));
    // System.out.println(matches3.group("A"));
    // System.out.println(matches3.group("B"));
    // }
    // if (matches4.find()) {
    // System.out.println(matches4.group("Pied"));
    // System.out.println(matches4.group("Pouce"));
    // System.out.println(matches4.group("A"));
    // System.out.println(matches4.group("B"));
    // }
    // if (matches5.find()) {
    // System.out.println(matches5.group("Pied"));
    // System.out.println(matches5.group("Pouce"));
    // System.out.println(matches5.group("A"));
    // System.out.println(matches5.group("B"));
    // }
}
