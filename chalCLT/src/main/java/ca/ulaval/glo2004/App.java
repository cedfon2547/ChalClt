package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.MainWindow;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

class AppSplashScreen extends JFrame {
    public AppSplashScreen() {
        super("ChalCLT");

        String appImgPath = "/icons/dark/chalclt_logo.png";
        URL appImgURL = App.class.getResource(appImgPath);
        Image appImg = Toolkit.getDefaultToolkit().getImage(appImgURL).getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon appIcon = new ImageIcon(appImg);

        JPanel splashScreenPanel = new JPanel(new BorderLayout());
        JLabel splashScreenimgLabel = new JLabel();
        JLabel spashScreenTitleLabel = new JLabel("ChalCLT");
        
        splashScreenimgLabel.setHorizontalAlignment(JLabel.CENTER);
        splashScreenimgLabel.setVerticalAlignment(JLabel.CENTER);

        spashScreenTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        spashScreenTitleLabel.setVerticalAlignment(JLabel.BOTTOM);
        spashScreenTitleLabel.setForeground(java.awt.Color.WHITE);
        spashScreenTitleLabel.setFont(new java.awt.Font("Serif Bold", java.awt.Font.BOLD, 50));
        
        splashScreenimgLabel.setIcon(appIcon);
        splashScreenPanel.add(splashScreenimgLabel, BorderLayout.CENTER);
        splashScreenPanel.add(spashScreenTitleLabel, BorderLayout.SOUTH);

        add(splashScreenPanel);

        // get screen size
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        // Set frame position at the center of the screen
        // setLocationRelativeTo(null);
        setUndecorated(true);
        setSize(500, 500);
        setLocation(new Point((int) (screenSize.getWidth() / 2) - 250, (int) (screenSize.getHeight() / 2) - 250));
        setVisible(true);
    }
}

public class App {
    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        Controleur controleur = Controleur.getInstance();


        MainWindow mainWindow = new MainWindow(controleur);

        // If we want to display the splash screen for a certain amount of time

        AppSplashScreen splashScreen = new AppSplashScreen();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashScreen.dispose();

        String appImgPath = "/icons/dark/chalclt_logo.png";
        URL appImgURL = App.class.getResource(appImgPath);
        Image appImg = Toolkit.getDefaultToolkit().getImage(appImgURL).getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon appIcon = new ImageIcon(appImg);

        mainWindow.setIconImage(appIcon.getImage());
        
        // JFrame configuration
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setSize(1000, 800);
        mainWindow.setVisible(true);
    }
}
