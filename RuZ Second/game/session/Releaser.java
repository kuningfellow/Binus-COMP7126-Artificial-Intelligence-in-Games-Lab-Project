package game.session;

import game.session.Session;

// A thread to ensure locks are released regularly
class Releaser implements Runnable {
    Session session;
    Releaser(Session session) {
        this.session = session;
    }
    public void run() {
        while (true) {
            synchronized(session.state) {
                // Release any locks
                session.state.notify();
                if (session.state.gameOver()) {
                    // Kill thread if game over
                    break;
                }
                try {
                    session.state.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}