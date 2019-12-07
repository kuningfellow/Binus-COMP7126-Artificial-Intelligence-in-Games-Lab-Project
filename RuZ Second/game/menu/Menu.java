package game.menu;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.io.InputStream;

import game.Game;

// Class for game menu
public class Menu extends JPanel {
    Game game;
    Font rogFont;
    Title title;
    JPanel buttons;
    JButton easyButton, mediumButton, hardButton;
    public Menu(Game game) {
        this.game = game;
        title = new Title();
        buttons = new JPanel();
        easyButton = new JButton("Easy");
        mediumButton = new JButton("Medium");
        hardButton = new JButton("Hard");
        buttons.add(easyButton);
        buttons.add(mediumButton);
        buttons.add(hardButton);
        this.easyButton.setFocusable(false);
        this.mediumButton.setFocusable(false);
        this.hardButton.setFocusable(false);
        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized(game) {
                    game.option = 1;
                    game.notify();
                }
            }
        });
        mediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized(game) {
                    game.option = 2;
                    game.notify();
                }
            }
        });
        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized(game) {
                    game.option = 3;
                    game.notify();
                }
            }
        });

        this.game.frame.setSize(800, 600);
        this.setLayout(new GridLayout(2, 1));
        this.add(title);
        this.add(buttons);
        this.game.frame.add(this);
        this.game.frame.setVisible(true);
    }
}