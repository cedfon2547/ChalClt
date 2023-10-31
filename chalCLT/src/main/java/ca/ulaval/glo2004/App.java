package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.gui.components.MainWindowTopBarMenu;

import javax.swing.*;

public class App {
    // Exemple de creation d'une fenetre et d'un bouton avec swing. Lorsque vous
    // allez creer votre propre GUI
    // Vous n'aurez pas besoin d'ecrire tout ce code, il sera genere automatiquement
    // par intellij ou netbeans
    // Par contre vous aurez a creer les actions listener pour vos boutons et etc.
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
