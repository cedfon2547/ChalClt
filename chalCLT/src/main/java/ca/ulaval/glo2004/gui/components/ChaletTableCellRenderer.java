package ca.ulaval.glo2004.gui.components;

import java.awt.Color;
import java.awt.Component;


import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;


public class ChaletTableCellRenderer extends DefaultTableCellRenderer {

    private ChaletTableCellEditor chaletTableCellEditor;
    

    public ChaletTableCellRenderer(ChaletTableCellEditor editor) {
        this.chaletTableCellEditor = editor;
        
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        
        if (row == 6 && column == 1) {
            // hasFocus = true;
            //chaletTableCellEditor.sensToitComboBox.setRenderer(comboBoxRenderer);
            
            return chaletTableCellEditor.sensToitComboBox;
        }
        if(column == 1 && (row == 1 || row == 2 || row == 3 || row == 4 || row == 7 || row == 8)){
            String stringVal = value.toString();
            String delim = "[\"]";
            String[] parsedString = stringVal.split(delim);
            // System.out.println(parsedString[1]);
            String noFraction = " 0/1";
            if(parsedString[1].equals(noFraction)){
                //System.out.println("in");
                value = parsedString[0] + "\""; 
            }
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    }

}




