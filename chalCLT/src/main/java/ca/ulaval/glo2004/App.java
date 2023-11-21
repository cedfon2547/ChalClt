package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.gui.MainWindow;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

class Line {
    double x1 = 0;
    double y1 = 0;
    double x2 = 0;
    double y2 = 0;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    public boolean intersects(Line line) {
        if (line.x1 == line.x2) {
            return (x1 <= line.x1 && x2 >= line.x1) || (x1 >= line.x1 && x2 <= line.x1);
        } else {
            return (y1 <= line.y1 && y2 >= line.y1) || (y1 >= line.y1 && y2 <= line.y1);
        }
    }

    public Point intersectionPoint(Line line) {
        // First, check if lines intersect each other
        if (!intersects(line)) {
            return null;
        }

        // If they do, calculate the intersection point
        double x = 0;
        double y = 0;

        if (line.x1 == line.x2) {
            x = line.x1;
            y = y1;
        } else {
            x = x1;
            y = line.y1;
        }

        return new Point((int)x, (int)y);
    }
}

class CustomRect {
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;

    List<Acc> accs = new ArrayList<Acc>();

    public CustomRect(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        List<Line> lines = new ArrayList<Line>();

        for (Acc acc : accs) {
            acc.draw(g);
            Line line1 = new Line(acc.x - 1000, acc.y, acc.x + 1000, acc.y);
            Line line2 = new Line(acc.x - 1000, acc.y + acc.height, acc.x + 1000, acc.y + acc.height);
            Line line3 = new Line(acc.x, acc.y - 1000, acc.x, acc.y + 1000);
            Line line4 = new Line(acc.x + acc.width, acc.y - 1000, acc.x + acc.width, acc.y + 1000);

            lines.add(line1);
            lines.add(line2);
            lines.add(line3);
            lines.add(line4);

            for (Line _line: lines) {
                // Draw oval at intersection
                // if (line1.intersects(_line)) {
                //     g.setColor(Color.RED);
                //     g.fillOval((int)line1.x1 - 5, (int)line1.y1 - 5, 10, 10);
                // }
                g.setColor(Color.CYAN);

                Point p = line1.intersectionPoint(_line);
                if (p != null) {
                    g.fillOval((int)p.getX() - 5, (int)p.getY() - 5, 10, 10);
                }

                Point p2 = line2.intersectionPoint(_line);
                if (p2 != null) {
                    g.fillOval((int)p2.getX() - 5, (int)p2.getY() - 5, 10, 10);
                }

                Point p3 = line3.intersectionPoint(_line);
                if (p3 != null) {
                    g.fillOval((int)p3.getX() - 5, (int)p3.getY() - 5, 10, 10);
                }

                Point p4 = line4.intersectionPoint(_line);
                if (p4 != null) {
                    g.fillOval((int)p4.getX() - 5, (int)p4.getY() - 5, 10, 10);
                }

                // Draw line
                // g.setColor(Color.WHITE);

            }
        }
    }

    public void addAcc(Acc acc) {
        accs.add(acc);
    }
}

class Acc {
    double x = 0;
    double y = 0;
    double width = 0;
    double height = 0;

    public Acc(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        System.out.println("Acc created");
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.drawRect((int)x, (int)y, (int)width, (int)height);

        // Draw circle at each corner
        g2.setColor(Color.CYAN);
        // g2.fillOval((int)x - 5, (int)y -5, 10, 10);
        // g2.fillOval((int)x - 5 + (int)width, (int)y - 5, 10, 10);
        // g2.fillOval((int)x - 5, (int)y + (int)height - 5, 10, 10);
        // g2.fillOval((int)x - 5 + (int)width, (int)y + (int)height - 5, 10, 10);

        // float[] dashingPattern1 = {2f, 2f};
        // Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,
        //         BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
        
        // g2.setStroke(stroke1);

        // Draw line on x axis for each corner
        // g.setColor(Color.RED);
        // g.drawLine((int)x - 1000, (int)y, (int)x + 1000, (int)y);
        // g.drawLine((int)x - 1000, (int)y + (int)height, (int)x + 1000, (int)y + (int)height);

        // // Draw line on y axis for each corner
        // g.setColor(Color.GREEN);
        // g.drawLine((int)x, (int)y - 1000, (int)x, (int)y + 1000);
        // g.drawLine((int)x + (int)width, (int)y - 1000, (int)x + (int)width, (int)y + 1000);
    }
}

public class App {
    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        Controleur controleur = Controleur.getInstance();

        MainWindow mainWindow = new MainWindow(controleur);
        
        // // JFrame configuration
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setSize(800, 600);
        mainWindow.setVisible(true);

        // double rectWidth = 500;
        // double rectHeight = 350;
        // double rectX = 50;
        // double rectY = 50;

        // CustomRect rect = new CustomRect((int)rectX, (int)rectY, (int)rectWidth, (int)rectHeight);

        // Acc acc1 = new Acc(100, 100, 100, 100);
        // Acc acc2 = new Acc(280, 100, 100, 100);
        // Acc acc3 = new Acc(150, 250, 150, 100);
        // Acc acc4 = new Acc(450, 150, 50, 200);

        // rect.addAcc(acc1);
        // rect.addAcc(acc2);
        // rect.addAcc(acc3);
        // rect.addAcc(acc4);

        // JFrame frame = new JFrame("JFrame Example");
        // JPanel panel = new JPanel() {
        //     @Override
        //     public void paintComponent(Graphics g) {
        //         super.paintComponent(g);

        //         g.setColor(Color.BLACK);
        //         g.fillRect(0, 0, getWidth(), getHeight());

        //         rect.draw(g);
        //     }
        // };
    
        // frame.getContentPane().add(panel);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(800, 600);
        // frame.setVisible(true);
    }
}
