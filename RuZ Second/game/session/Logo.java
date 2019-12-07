package game.session;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game.session.Session;

// Class to display logo
public class Logo extends JPanel {
    Session session;
    int scaleFactor;
    BufferedImage image;
    int width, height;
    public Logo(Session session, int scaleFactor) {
        this.session = session;
        this.scaleFactor = scaleFactor;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("../assets/logo.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        width = image.getWidth() * scaleFactor / 100;
        height = image.getHeight() * scaleFactor / 100;
        repaint();
    }
    int getLogoHeight() {
        return height + 20;
    }
    int getLogoWidht() {
        return width;
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 20, width, height, null);
    }
}