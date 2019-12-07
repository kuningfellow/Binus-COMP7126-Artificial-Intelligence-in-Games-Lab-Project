package game.session;

import game.session.Session;

// A thread to paint session on a timer
class Painter implements Runnable {
    Session session;
    Painter(Session session) {
        this.session = session;
    }
    public void run() {
        while (true) {
            // Repaint session JPanel
            session.screen.repaint();
            session.info.repaint();
            session.logo.repaint();

            if (session.state.gameOver()) {
                // Kill thread if game over
                break;
            }
            try {
                // Screen refresh rate
                Thread.sleep(1000/60);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}