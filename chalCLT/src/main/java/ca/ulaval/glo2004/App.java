package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.gui.components.MainWindowTopBarMenu;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        Controleur controleur = Controleur.getInstance();

        MainWindowTopBarMenu menu = new MainWindowTopBarMenu();
        MainWindow mainWindow = new MainWindow(controleur);

        mainWindow.setJMenuBar(menu); // Add the menu bar to the window
        
        // JFrame configuration
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setSize(800, 600);
        mainWindow.setVisible(true);
    }
}
