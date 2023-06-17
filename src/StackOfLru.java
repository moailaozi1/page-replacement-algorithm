/**
 * @author GoldenRetriever
 * 利用数组实现栈的功能，包括栈相关元素定义，初始化方法(初始化一个固定大小的栈)
 * 以及push()和pop()方法、peek()方法
 * 新增isExist()、location()、以及重写toString()方法，在LRU算法中将得到使用。
 */
class StackOfLru {
    /**
     * storage 存放栈中元素的数组
     */
    private int[] storage;
    /**
     * capacity 栈的容量
     */
    private int capacity;
    /**
     * count 栈中元素数量
     */
    private int count;
    /**
     * MyStack() 不带初始容量的构造方法。默认容量为8
     */
    private StackOfLru() {
        this.capacity = 8;
        this.storage=new int[8];
        this.count = 0;
    }
    /**
     *  带初始容量的构造方法
     * @param initialCapacity 初始容量
     */
    StackOfLru(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Capacity too small.");
        }
        this.capacity = initialCapacity;
        this.storage = new int[initialCapacity];
        this.count = 0;
    }
    /**
     * 入栈
     * @param value 入栈值
     */
    void push(int value) {
        if (count == capacity) {
            System.out.println("栈满");
        }else{
            storage[count++] = value;
        }
    }
    /**
     * @return 返回栈顶元素并出栈
     */
    int pop() {
        count--;
        if (count == -1) {
            throw new IllegalArgumentException("Stack is empty.");
        }
        return storage[count];
    }
    /**
     * @return 返回栈顶元素不出栈
     */
    public int peek() {
        if (count == 0){
            throw new IllegalArgumentException("Stack is empty.");
        }else {
            return storage[count-1];
        }
    }
    /**
     * @return 判断栈是否为空
     */
    public boolean isEmpty() {
        return count == 0;
    }
    /**
     * @return 判断栈是否满
     */
    boolean isFull() {
        return count == capacity;
    }
    /**
     * @return 判断栈中是否存在一个数
     */
    boolean isExist(int n, StackOfLru stackOfLru){
        for(int i = 0; i< stackOfLru.getCount(); i++){
            if(n == stackOfLru.storage[i]){
                return true;
            }
        }
        return false;
    }
    /**
     * @return 返回栈中元素的个数
     */
    int getCount() {
        return count;
    }
    /**
     * @return 返回栈的容量
     */
    int getCapacity() {
        return capacity;
    }
    /**
     * @return 如果栈中存在某个元素，则返回该元素所在栈中位置
     */
    int  location(int n, StackOfLru stackOfLru) {
        for (int i = 0; i< stackOfLru.getCount(); i++){
            if ( n == stackOfLru.storage[i]){
                return i;
            }
        }
        return 0;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = count - 1; i >= 0; i--){

            sb.append(storage[i] + ", ");
        }
        sb.replace(sb.length() - 2, sb.length(), "]");
        return sb.toString();
    }
}
