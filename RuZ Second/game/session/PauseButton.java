package game.session;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

import game.session.Session;

// Button for pausing game session
public class PauseButton extends JPanel {
    Session session;
    JButton pauseButton;
    public PauseButton(Session session) {
        this.addKeyListener(session);
        this.session = session;
        this.pauseButton = new JButton("Pause");
        this.pauseButton.setFocusable(false);
        this.add(pauseButton);
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (session.state.paused) {
                    session.resume();
                    pauseButton.setText("Pause");
                } else {
                    session.pause();
                    pauseButton.setText("Resume");
                }
            }
        });
    }
}