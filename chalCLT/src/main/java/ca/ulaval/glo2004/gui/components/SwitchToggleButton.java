package ca.ulaval.glo2004.gui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class SwitchToggleButton extends Component {
    public static enum Size {
        XSMALL(15),
        SMALL(20),
        MEDIUM(25),
        LARGE(30),
        XLARGE(35);

        private final int height;

        Size(int height) {
            this.height = height;
        }

        public int getWidth() { return height * 2; }
        public int getHeight() { return height; }
    }

    private Size size = Size.SMALL;
    private boolean selected = false;
    private boolean mouseOver;
    private List<EventSwitchSelected> events;

    private Color defaultBgColor = Color.LIGHT_GRAY;
    private Color selectedBgColor = new Color(0, 174, 255);
    private Color thumbColor = Color.WHITE;
    private Color disabledBgColor = new Color(55, 58, 64);
    private Color disabledThumbColor = new Color(92, 95, 102);

    public SwitchToggleButton() {
        this(Size.MEDIUM, false);
    }

    public SwitchToggleButton(Size size) {
        this(size, false);
    }

    public SwitchToggleButton(Size size, boolean initialSelected) {
        this.selected = initialSelected;
        this.size = size;

        setPreferredSize(new Dimension(size.getWidth(), size.getHeight()));
        setSize(new Dimension(size.getWidth(), size.getHeight()));
        setForeground(Color.WHITE);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        events = new ArrayList<>();

        this.repaint();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                mouseOver = true;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                mouseOver = false;
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (SwingUtilities.isLeftMouseButton(evt)) {
                    if (mouseOver && isEnabled()) {
                        selected = !selected;
                        repaint();
                        runEvent();
                    }
                }
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        // runEvent();
    }

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = size.getWidth() - 2;
        int height = size.getHeight();
        float alpha = getAlpha();
        int location = selected ? height : 2;
        int size = height;

        g2.setColor(!isEnabled() ? disabledBgColor : defaultBgColor);
        g2.fillRoundRect(0, 0, width, height, size, size);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(!isEnabled() ? disabledBgColor : selectedBgColor);
        
        g2.fillRoundRect(0, 0, width, height, size, size);
        g2.setColor(!isEnabled() ? disabledThumbColor : thumbColor);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.fillOval((int) location, 2, height - 4, height - 4);

        super.paint(grphcs);
    }

    private float getAlpha() {
        float width = size.getHeight();
        float height = size.getWidth() - 2;

        float location = selected ? height : 2;
        float alpha = (location - 2) / width;
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        return alpha;
    }

    private void runEvent() {
        for (EventSwitchSelected event : events) {
            event.onSelected(selected);
        }
    }

    public void addEventSelected(EventSwitchSelected event) {
        events.add(event);
    }

    public static interface EventSwitchSelected {
        public void onSelected(boolean selected);
    }
}
