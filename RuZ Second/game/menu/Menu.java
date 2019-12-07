package game.menu;

import java.util.Scanner;
import javax.swing.JPanel;

import game.Game;

public class Menu extends JPanel implements Runnable {
    Game game;
    Scanner in = new Scanner(System.in);
    public Menu(Game game) {
        this.game = game;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            synchronized(game) {
                System.out.println("choose game difficulty");
                game.option = in.nextInt();
                game.notify();
                break;
            }
        }
    }
}