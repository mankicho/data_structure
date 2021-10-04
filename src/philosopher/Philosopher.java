package philosopher;

public class Philosopher implements Runnable {
    private int id;
    private DiningPhilosopherMonitor monitor;


    public Philosopher(int id, DiningPhilosopherMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            think();
            monitor.pickUp(id);
            eating();
            monitor.putDown(id);
        }
    }

    private void think() {
        try {
            System.out.println(id + " : Now I'm thinking");
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
        }
    }

    private void eating() {
        try {
            System.out.println(id + " : Now I'm eating");
            Thread.sleep((long) (Math.random() * 50));
        } catch (InterruptedException e) {
        }
    }
}
