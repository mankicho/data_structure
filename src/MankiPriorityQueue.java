import java.util.Comparator;

public class MankiPriorityQueue<E> {
    private Object[] elements; // 우선순위 큐에 담을 요소들
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private final Comparator<? super E> comparator;

    public MankiPriorityQueue(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.size = 0;
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    public MankiPriorityQueue(int capacity) {
        this.comparator = null;
        this.size = 0;
        this.elements = new Object[capacity];
    }

    public MankiPriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    public void add(E element) {
        // 배열이 꽉차면 resize()를 통해 배열의 크기를 늘린다.
        if (size + 1 == elements.length) {
            resize(elements.length * 2);
        }
//        elements[size + 1] = element; // 배열의 마지막에 원소를 넣고
        siftUp(size + 1, element); // heap 의 구조를 유지한다
        size++; // size를 늘려준다.
    }

    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            throw new RuntimeException("원소가 하나도 없어요!!");
        }
        E root = (E) elements[1]; // 응답할 요소를 미리 꺼내놓는다.
        E target = (E) elements[size]; // 가장 마지막에 있는 요소를 꺼내놓는다.
        elements[size] = null;
        size--; // 배열의 가장 마지막에있는 요소를 null 처리하고 크기를 1 감소 시킨다.
        siftDown(target); // 미리 꺼내놓았던 가장 마지막에있던 요소를 root 자리에 놓고, heapify 를 통해 힙 구조를 유지한다.

        if (size <= elements.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, elements.length / 2));
        }
        return root;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return this.size;
    }

    private void siftDown(E target) {
        if (comparator == null) {
            siftDownUsingDefaultComparable(target);
        } else {
            siftDownUsingComparator(target);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(E target) {
        if (size < 1) { // size 가 1개면 heapify 의 과정이 필요없다.
            return;
        }
        int parent = 1;
        int child = 1;
        while (getLeftChild(parent) <= size) {

            child = getLeftChild(parent);
            int right = getRightChild(parent);
            E childObject = (E) elements[child];

            if (right <= size && comparator.compare(childObject, (E) elements[right]) < 0) {
                child = right;
                childObject = (E) elements[right];
            }

            // target 은 root 에 있는상태이다. target 이 더 크면 heapify 를 중단한다.
            if (comparator.compare(target, childObject) >= 0) {
                break;
            }

            elements[parent] = childObject; // 요소를 swap 하고
            parent = child; // 아래로 내려가면서 순회한다.
        }

        elements[parent] = target;
    }

    @SuppressWarnings("unchecked")
    private void siftDownUsingDefaultComparable(E target) {
        Comparable<? super E> comparable = (Comparable<? super E>) target;

        if (size < 1) {
            return;
        }

        int parent = 1;
        int child = 1;
        while (getLeftChild(parent) <= size) {

            child = getLeftChild(parent);
            int right = getRightChild(parent);
            E childObject = (E) elements[child];

            Comparable<? super E> childComparable = (Comparable<? super E>) childObject;

            if (right <= size && childComparable.compareTo((E) elements[right]) < 0) {
                child = right;
                childObject = (E) elements[right];
            }

            // target 은 root 에 있는상태이다. target 이 더 크면 heapify 를 중단한다.
            if (comparable.compareTo(childObject) >= 0) {
                break;
            }

            elements[parent] = childObject; // 요소를 swap 하고
            parent = child; // 아래로 내려가면서 순회한다.
        }

        elements[parent] = target;

    }

    private void siftUp(int size, E target) {
        if (comparator == null) {
            siftUpUsingDefaultComparable(size, target);
        } else {
            siftUpUsingComparator(size, target);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUpUsingComparator(int size, E target) {
        int parent = getParent(size);
        int child = size;
        while (parent >= 1) {
            E criteria = (E) elements[parent]; // 비교대상 원소(부모 요소와 비교한다)

            if (comparator.compare(target, criteria) <= 0) { // 자식요소가 부모요소보다 작거나 같으면
                break; // 더이상 heapify 할 필요가없으므로 break;
            }
            elements[child] = criteria; // 원소의 위치를 바꿔주고
            child = parent; // 계속 순회
            parent = getParent(parent);
        }

        elements[child] = target;
    }

    public void print() {
        for (int i = 1; i <= size; i++) {
            System.out.println(elements[i]);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUpUsingDefaultComparable(int size, E target) {
        int parent = getParent(size);
        int child = size;
        Comparable<? super E> comparable = (Comparable<? super E>) target;
        while (parent >= 1) {
            E criteria = (E) elements[parent]; // 비교대상 원소(부모 요소와 비교한다)

            if (comparable.compareTo(criteria) <= 0) { // 자식요소가 부모요소보다 작거나 같으면
                break; // 더이상 heapify 할 필요가없으므로 break;
            }
            elements[child] = criteria; // 원소의 위치를 바꿔주고
            child = parent; // 계속 순회
            parent = getParent(parent);
        }

        elements[child] = target;
    }

    private void resize(int capacity) {
        Object[] newArray = new Object[capacity];

        for (int i = 1; i < elements.length; i++) {
            newArray[i] = elements[i];
        }

        elements = null;
        elements = newArray;
    }

    private int getParent(int idx) {
        return idx / 2;
    }

    private int getLeftChild(int idx) {
        return idx * 2;
    }

    private int getRightChild(int idx) {
        return idx * 2 + 1;
    }

}
