package philosopher;

public class DiningPhilosopher {
    public static void main(String[] args) {
        int numOfPhils = 5;
        Philosopher[] philosophers = new Philosopher[numOfPhils];
        DiningPhilosopherMonitor monitor = new DiningPhilosopherMonitor(numOfPhils); // 철학자 동시성 문제를 제어하기위한 모니터
        for (int id = 0; id < philosophers.length; id++) {
            new Thread(new Philosopher(id,monitor)).start();
        }

    }
}
