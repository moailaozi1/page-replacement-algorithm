class QueueOfFifo {
    /**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Max size of the queue
     */
    private int maxSize;
    /**
     * The array representing the queue
     */
    private int[] queueArray;
    /**
     * Front of the queue
     */
    private int front;
    /**
     * Rear of the queue
     */
    private int rear;
    /**
     * How many items are in the queue
     */
    private int nItems;

    /**
     * init with DEFAULT_CAPACITY
     */
    public QueueOfFifo() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor
     *
     * @param size Size of the new queue
     */
    public QueueOfFifo(int size) {
        maxSize = size;
        queueArray = new int[size];
        front = 0;
        rear = -1;
        nItems = 0;
    }
    /**
     * 获得队列的最大容量
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Inserts an element at the rear of the queue
     *
     * @param x element to be added
     * @return True if the element was added successfully
     */
    public boolean insert(int x) {
        if (isFull()) {
            return false;
        }
        // If the back of the queue is the end of the array wrap around to the front
        rear = (rear + 1) % maxSize;
        queueArray[rear] = x;
        nItems++;
        return true;
    }

    /**
     * Remove an element from the front of the queue
     *
     * @return the new front of the queue
     */
    public int remove() {
        if (isEmpty()) {
            return -1;
        }
        int temp = queueArray[front];
        front = (front + 1) % maxSize;
        nItems--;
        return temp;
    }

    /**
     * Checks what's at the front of the queue
     *
     * @return element at the front of the queue
     */
    public int peekFront() {
        return queueArray[front];
    }

    /**
     * Checks what's at the rear of the queue
     *
     * @return element at the rear of the queue
     */
    public int peekRear() {
        return queueArray[rear];
    }

    /**
     * Returns true if the queue is empty
     *
     * @return true if the queue is empty
     */
    public boolean isEmpty() {
        return nItems == 0;
    }

    /**
     * Returns true if the queue is full
     *
     * @return true if the queue is full
     */
    public boolean isFull() {
        return nItems == maxSize;
    }

    /**
     * Returns the number of elements in the queue
     *
     * @return number of elements in the queue
     */
    public int getSize() {
        return nItems;
    }

    public boolean isExist(int a) {
        boolean flag = false;
        if (isEmpty()) {
            return false;
        }
        for (int i = 0; i < nItems; i++) {
            if (queueArray[i] == a) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = front; ; i = ++i % maxSize) {
            sb.append(queueArray[i]).append(", ");
            if (i == rear) {
                break;
            }
        }
        sb.replace(sb.length() - 2, sb.length(), "]");
        return sb.toString();
    }
}




