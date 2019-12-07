package game.session;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import game.session.Session;

// Class for handling a single gameplay session (starting, pausing, resuming, event handling, painting)
public class Screen extends JPanel {
    Session session;
    public Screen(Session session) {
        this.session = session;
    }

    private void initialize() {
        setBackground(Color.cyan);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < session.state.maze.size; i++) {
            for (int j = 0; j < session.state.maze.size; j++) {
                String color = session.state.maze.getTileColor(i, j);
                if (color.equals("player")) {
                    g.setColor(new Color(0, 0, 255));
                } else if (color.equals("enemy")) {
                    g.setColor(new Color(255, 0, 0));
                } else if (color.equals("goal")) {
                    g.setColor(new Color(0, 255, 0));
                } else if (color.equals("floor")) {
                    g.setColor(new Color(255, 255, 255));
                } else if (color.equals("wall")) {
                    g.setColor(new Color(0, 0, 0));
                }
                g.fillRect(j*session.state.maze.tileSize, i*session.state.maze.tileSize, session.state.maze.tileSize, session.state.maze.tileSize);
            }
        }
    }
}