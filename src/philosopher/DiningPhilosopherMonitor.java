package philosopher;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopherMonitor {
    private int numOfPhils;
    private State[] states;
    private Condition[] self;
    private Lock lock;

    public DiningPhilosopherMonitor(int num) {
        numOfPhils = num; // 철학자 수
        states = new State[num]; // 철학자 상태
        self = new Condition[num]; //
        lock = new ReentrantLock(); // 재 진입가능한 lock 객체
        for (int i = 0; i < num; i++) {
            states[i] = State.THINKING;
            self[i] = lock.newCondition(); // Condition 하나씩 부여한다. 철학자에 접근할때 이 lock 을 통해 접근
        }
    }

    private int leftOf(int i) { // 왼쪽 철학자
        return (i + numOfPhils - 1) % numOfPhils;
    }

    private int rightOf(int i) { // 오른쪽 철학자
        return (i + 1) % numOfPhils;
    }

    private void test(int i) {
        // 내가 배고픈상태고 나의 양쪽이 밥먹고있는 상태가 아니라면
        if (states[i] == State.HUNGRY
                && states[leftOf(i)] != State.EATING
                && states[rightOf(i)] != State.EATING) {
            // 나의 상태를 EATING 으로 바꿔주고
            states[i] = State.EATING;
            // signal() 을 통해 나를 깨운다. ==> 나는 밥을먹기위해 wait()를 통해 기다리고있기 때문.
            self[i].signal();
        }
    }

    public void pickUp(int id) {
        lock.lock(); // critical section 에 진입하기전에 lock 을 건다.
        try {
            states[id] = State.HUNGRY;
            test(id);
            if (states[id] != State.EATING) {
                // 내가 밥먹고있는 상태가 아니라면 기다린다.
                /* test() 함수를 통해 나의 양쪽 철학자가 밥을먹고있는지 검사했다. 만약 나의 상태가 EATING
                아니라면 누군가는 밥을먹고있다는것이다. 그래서 await()를 통해 기다린다.
                */
                self[id].await();
            }
        } catch (InterruptedException e) {
        } finally {
            lock.unlock(); // exit section 에서는 lock 을 해제한다.
        }
    }

    public void putDown(int id) {
        lock.lock();

        try {
            states[id] = State.THINKING; // 젓가락을 놓는것이므로 THINKING 으로 바꿔준다.
            /* 내가 젓가락을 놓았기때문에 나의 양쪽사람들에게 알린다.
            나의 양쪽 철학자중 누군가가 배고픈상태라면 test() 메서드를통해 밥을 먹게될것이다.
            */
            test(leftOf(id));
            test(rightOf(id));
        } finally {
            lock.unlock();
        }

    }
}
