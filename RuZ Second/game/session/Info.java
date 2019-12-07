package game.session;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import game.session.Session;

// Class for handling a single gameplay session (starting, pausing, resuming, event handling, painting)
public class Info extends JPanel {
    Session session;
    public Info(Session session) {
        this.session = session;
    }

    private void initialize() {
        setBackground(Color.cyan);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Font font = new Font ("Roboto", Font.PLAIN, 22);
        int paddingT = session.game.frame.getInsets().top;
        int paddingL = 20;
        int dist = 50;
        g.setFont(font);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(paddingL, paddingT, 30, 30);
        g.drawString("- Wall", paddingL + 35, paddingT + 22);

        g.setColor(new Color(0, 0, 255));
        g.fillRect(paddingL, paddingT + dist, 30, 30);
        g.drawString("- Player", paddingL + 35, paddingT + dist + 22);

        g.setColor(new Color(255, 0, 0));
        g.fillRect(paddingL, paddingT + 2*dist, 30, 30);
        g.drawString("- Enemy", paddingL + 35, paddingT + 2*dist + 22);

        g.setColor(new Color(0, 255, 0));
        g.fillRect(paddingL, paddingT + 3*dist, 30, 30);
        g.drawString("- Goal", paddingL + 35, paddingT + 3*dist + 22);

        g.setColor(Color.BLACK);
        g.drawString("Score : " + session.state.scorer.getScore(), paddingL, paddingT + 4*dist + 22);
        g.drawString("Treasure value:", paddingL, paddingT + 7*dist);
        g.drawString("" + session.state.scorer.treasureValue(), paddingL, paddingT + 7*dist + 30);
    }
}