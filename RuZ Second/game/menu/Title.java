package game.menu;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsEnvironment;

import javax.swing.JPanel;
import java.io.InputStream;

import game.Game;

// Class for menu title
class Title extends JPanel {
    Font rogFont;
    Title() {
        try {
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("game/assets/ROGFontsv1.6-Regular.ttf");
            rogFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(Font.ITALIC, 48f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        repaint();
    }

    private void initialize() {
        setBackground(Color.cyan);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(rogFont);
        g.drawString("RuZ Second", 280, 130);
        g.setFont(new Font("Roboto", Font.BOLD, 14));
        g.drawString("Objective: Reach the goal First!", 280, 180);
    }
}