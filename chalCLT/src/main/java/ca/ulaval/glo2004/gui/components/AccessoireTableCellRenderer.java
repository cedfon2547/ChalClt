package ca.ulaval.glo2004.gui.components;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class AccessoireTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (column == 1 && row != 0) {

            String stringVal = value.toString();
            String delim = "[\"]";
            String[] parsedString = stringVal.split(delim);
            // System.out.println(parsedString[1]);
            String noFraction = " 0/1";
            if (parsedString[1].equals(noFraction)) {
                // System.out.println("in");
                value = parsedString[0] + "\"";
            }
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
