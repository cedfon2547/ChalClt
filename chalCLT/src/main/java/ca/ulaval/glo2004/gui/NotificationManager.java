package ca.ulaval.glo2004.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import ca.ulaval.glo2004.App;

public class NotificationManager {
    JFrame frame;
    List<NotificationDialog> dialogs = new ArrayList<>();
    ImageIcon infoIcon;
    ImageIcon successIcon;
    ImageIcon errorIcon;
    ImageIcon warningIcon;

    public NotificationManager(JFrame frame) {
        this.frame = frame;

        SwingUtilities.invokeLater(() -> {
            updateAllNotificationsLocation();
        });
    }

    public NotificationDialog createNotification(String title, String message, NotificationType type) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(5, 5));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel typeLabel = new JLabel();
        JLabel titlelLabel = new JLabel("<html>" + title + "</html>");
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setHorizontalTextPosition(JLabel.LEFT);
        messageLabel.setVerticalTextPosition(JLabel.TOP);
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.PLAIN, 12.0f));
        
        // Set the height of the message label to 2 lines
        Dimension newDim = new Dimension(messageLabel.getPreferredSize().width, 2 * messageLabel.getFont().getSize());
        messageLabel.setPreferredSize(newDim);

        titlelLabel.setFont(titlelLabel.getFont().deriveFont(Font.BOLD, 14.0f));

        switch(type) {
            case SUCCESS:
                if (successIcon == null) {
                    String iconPath = "/icons/alerts/alert_success_icon.png";
                    URL iconURL = App.class.getResource(iconPath);
                    Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
                    Image scaledIcon = icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    ImageIcon iconImage = new ImageIcon(scaledIcon);
                    successIcon = iconImage;
                }

                typeLabel.setIcon(successIcon);
                break;
            case ERROR:
                if (errorIcon == null) {
                    String iconPath = "/icons/alerts/alert_error_icon.png";
                    URL iconURL = App.class.getResource(iconPath);
                    Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
                    Image scaledIcon = icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    ImageIcon iconImage = new ImageIcon(scaledIcon);
                    errorIcon = iconImage;
                }

                typeLabel.setIcon(errorIcon);
                break;
            case WARNING:
                if (warningIcon == null) {
                    String iconPath = "/icons/alerts/alert_warning_icon.png";
                    URL iconURL = App.class.getResource(iconPath);
                    Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
                    Image scaledIcon = icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    ImageIcon iconImage = new ImageIcon(scaledIcon);
                    warningIcon = iconImage;
                }

                typeLabel.setIcon(warningIcon);
                break;
            case INFO:
                if (infoIcon == null) {
                    String iconPath = "/icons/alerts/alert_info_icon.png";
                    URL iconURL = App.class.getResource(iconPath);
                    Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
                    Image scaledIcon = icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    ImageIcon iconImage = new ImageIcon(scaledIcon);
                    infoIcon = iconImage;
                }

                typeLabel.setIcon(infoIcon);

                break;
            default: 
                break;
        }

        JPanel contentTextPanel = new JPanel();
        JPanel contentLabelPanel = new JPanel();
        contentTextPanel.setLayout(new BorderLayout());
        contentLabelPanel.setLayout(new BorderLayout());

        contentLabelPanel.add(typeLabel, BorderLayout.CENTER);
        contentTextPanel.add(titlelLabel, BorderLayout.PAGE_START);
        contentTextPanel.add(messageLabel, BorderLayout.CENTER);
        
        contentPanel.add(contentTextPanel, BorderLayout.CENTER);
        contentPanel.add(contentLabelPanel, BorderLayout.LINE_START);

        NotificationDialog dialog = new NotificationDialog(frame, contentPanel);
        this.dialogs.add(dialog);

        updateAllNotificationsLocation();
        return dialog;
    }

    public NotificationDialog createNotification(String title, String message) {
        return createNotification(title, message, NotificationType.INFO);
    }

    public void updateAllNotificationsLocation() {
        for (NotificationDialog dialog : dialogs) {
            dialog.updateLocation();
        }
    }

    public void clearAllNotifications() {
        for (NotificationDialog dialog : dialogs) {
            frame.removeComponentListener(dialog.frameComponentAdapter);
            dialog.dialog.dispose();
        }

        dialogs.clear();
    }

    // ==================== NotificationDialog ====================
    public class NotificationDialog {
        JFrame frame;
        JComponent component;
        JDialog dialog;
        JPanel dialogPanel = new JPanel();
        JButton closeBtn = new JButton();
        ComponentAdapter frameComponentAdapter;

        public NotificationDialog(JFrame frame, JComponent component) {
            this.frame = frame;
            this.component = component;
            this.dialog = new JDialog(frame);

            this.dialog.setFocusable(false);
            this.dialog.setFocusableWindowState(false);
            
            initComponents();
        }

        private void initComponents() {
            dialogPanel.setLayout(new BorderLayout());

            closeBtn.setText("X");
            closeBtn.setPreferredSize(new Dimension(20, 20));
            closeBtn.setBorder(null);
            // closeBtn.setContentAreaFilled(false);
            closeBtn.setFocusable(false);
            closeBtn.setOpaque(false);
            closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeBtn.setBackground(new Color(255, 255, 255, 0));

            closeBtn.addActionListener((evt) -> {
                remove();
                updateAllNotificationsLocation();
            });

            closeBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    closeBtn.setBackground(new Color(255, 255, 255, 20));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeBtn.setBackground(new Color(0, 0, 0, 0));
                }
            });

            JPanel closeBtnPanel = new JPanel();
            closeBtnPanel.setOpaque(false);
            closeBtnPanel.setLayout(new BorderLayout());
            closeBtnPanel.add(closeBtn, BorderLayout.NORTH);

            dialogPanel.add(closeBtnPanel, BorderLayout.EAST);
            dialogPanel.add(component, BorderLayout.CENTER);

            dialog.add(dialogPanel);
            dialog.setMinimumSize(new Dimension(200, 70));
            dialog.setLocationRelativeTo(null);
            dialog.setUndecorated(true);
            dialog.setVisible(true);

            frameComponentAdapter = getFrameComponentAdapter();
            frame.addComponentListener(frameComponentAdapter);
        }

        private ComponentAdapter getFrameComponentAdapter() {
            return new ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    updateLocation();
                }

                @Override
                public void componentMoved(java.awt.event.ComponentEvent evt) {
                    // System.out.println("Frame moved");
                    updateLocation();
                }

                @Override
                public void componentShown(java.awt.event.ComponentEvent evt) {
                    // System.out.println("Frame shown");
                    updateLocation();
                }

                @Override
                public void componentHidden(java.awt.event.ComponentEvent evt) {
                    // System.out.println("Frame hidden");
                }
            };
        }

        public void fadeInAnimation() {
            // Fade in animatio
            dialog.setOpacity(0.0f);

            Timer timer1 = new Timer(1, (evt) -> {
                float opacity = dialog.getOpacity();
                if (opacity < 1.0f) {
                    float newOpacity = Math.min(opacity + 0.05f, 1.0f);
                    dialog.setOpacity(newOpacity);
                } else {
                    ((Timer) evt.getSource()).stop();
                }
            });

            timer1.setRepeats(true);
            timer1.start();
        }

        public Point calculatePosition() {
            int thisDialogIndex = dialogs.indexOf(this);
            int dialogsHeight = dialogs.subList(0, thisDialogIndex).stream()
                    .mapToInt((dialog) -> dialog.dialog.getHeight()).sum();

            int x = frame.getLocation().x + frame.getWidth() - this.dialog.getWidth() - 15;
            int y = frame.getLocation().y + frame.getHeight() - dialogsHeight - this.dialog.getHeight()
                    - (10 * thisDialogIndex) - 15;
            return new Point(x, y);
        }

        public void updateLocation() {
            Point position = calculatePosition();
            
            if (dialog.isVisible() && (position.y < frame.getLocation().y + 100 || position.y < 0)) {
                dialog.setVisible(false);
            } else if (!dialog.isVisible() && position.y >= frame.getLocation().y) {
                dialog.setVisible(true);
            }

            dialog.setLocation(position);
        }

        public void remove() {
            dialogs.remove(this);
            frame.removeComponentListener(frameComponentAdapter);
            dialog.dispose();
        }

        public void show() {
            dialog.setVisible(true);
        }

        public void hide() {
            dialog.setVisible(false);
        }

        public void setTimer(int duration) {
            Timer timer = new Timer(duration, (evt) -> {
                remove();
                updateAllNotificationsLocation();
            });

            timer.setRepeats(false);
            timer.start();
        }
    }

    // ==================== NotificationType ====================
    public static enum NotificationType {
        SUCCESS, 
        ERROR, 
        WARNING, 
        INFO,
    }
}
