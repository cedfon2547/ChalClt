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


public class ChaletTableCellEditor extends AbstractCellEditor implements TableCellEditor,ActionListener,KeyListener {
    
    private class SensToitComboBox extends JComboBox<String>{

        public SensToitComboBox(){
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
    protected static final String COMBO = "combo";
    protected static final String TEXT = "text";
    public ChaletTableCellEditor(JTable chaletTable){
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
        if(row == 6 && column == 1){
            if(isSelected){
            valeurCellEditorCourrante = (String)value;
            }
            
            return sensToitComboBox;
        }
        jTextField.setText((String) value);
        valeurCellEditorCourrante = (String)value;

        return jTextField;
    }

    @Override
    public Object getCellEditorValue() {
        return valeurCellEditorCourrante;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(COMBO.equals(e.getActionCommand())){
            this.fireEditingStopped(); 
        }
        else{
            valeurCellEditorCourrante = jTextField.getText();
        }
        
        
    }

    public String getValeurCourrante(){
        return valeurCellEditorCourrante;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                valeurCellEditorCourrante = jTextField.getText();
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
        //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }
}
