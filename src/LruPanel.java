import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author GoldenRetriever
 * @version 1.0
 * @date 2019/12/23 16:55
 */
class LruPanel extends JPanel{
    /**
     * 设置6个标签,用来显示相关信息, 一个文本域, 用来接受输出信息
     */
    static JLabel label = new JLabel("缺页次数");
    static JLabel label1 = new JLabel("0");
    static JLabel label2 = new JLabel("缺页率");
    static JLabel label3 = new JLabel("0");
    static JLabel label4 = new JLabel("置换次数");
    static JLabel label5 = new JLabel("0");
    static TextArea tA = new TextArea();

    /**
     * 栈容量capacity、页面序列长度length、页面序列pages、flag标志、接受数据字符串s、正则数字表达式regex
     * 栈对象myQueue, 固定的页面序列fixedArray, 是否命中isHit。
     */
    private static int capacity;
    private static int[] pages;
    private static boolean flag = true;
    private static String s;
    private static String regex = "([0-9]|[1-9][0-9]*)";
    private static StackOfLru stack;
    private static int[] fixedArray = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3,
            0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
    private static boolean isHit;

    public LruPanel() {
        //设置尺寸、大小
        Dimension sizeOfTa=new Dimension(250, 200);
        Dimension sizeOfLabel =new Dimension(100, 50);
        //设置文本域、标签的首选大小
        tA.setPreferredSize(sizeOfTa);
        label.setPreferredSize(sizeOfLabel );
        label1.setPreferredSize(sizeOfLabel );
        label2.setPreferredSize(sizeOfLabel );
        label3.setPreferredSize(sizeOfLabel );
        label4.setPreferredSize(sizeOfLabel );
        label5.setPreferredSize(sizeOfLabel );
        //设置标签内字体。
        Font myFont = new Font("宋体",Font.BOLD, 18);
        label.setFont(myFont);
        label1.setFont(myFont);
        label2.setFont(myFont);
        label3.setFont(myFont);
        label4.setFont(myFont);
        label5.setFont(myFont);
        this.add(tA);
        this.add(label);
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
    }

    /**
     * 输入初始化页面号序列
     */
    public static void initPagesByInput(){
        //输入信息，不可直接调用FifoPanel中的同样方法，否则输出信息时将输出到对面面板内。
        do {
            s = JOptionPane.showInputDialog("请输入页面序列(以空格分开): ");
            if (s == null || "".equals(s)) {
                //没有做出选择
                flag = false;
            }else {
                //正则表达式代表多个空格
                String regex2 = " +";
                String [] arr = s.split(regex2);
                pages = new int[arr.length];
                for(int i = 0; i<arr.length;i++){
                    //赋值前先来个判断，分析数据是否是数字。
                    if (!arr[i].matches(regex)) {
                        JOptionPane.showMessageDialog(null, "输入数据错误，其中包含非数字！",
                                "提醒", JOptionPane.ERROR_MESSAGE);
                        flag = false;
                        break;
                    }else {
                        pages[i] = Integer.parseInt(arr[i]);
                        flag = true;
                    }
                }
            }
        }while (!flag);
    }

    public static void initPagesByRandom(){
        Random x = new Random();
        do {
            s = JOptionPane.showInputDialog("请输入页面序列长度: ");
            if (s == null || "".equals(s)) {
                //没有做出选择
                flag = false;
            }else {
                if (!s.matches(regex)) {
                    flag = false;
                    JOptionPane.showMessageDialog(null,"数据格式错误！");
                } else {
                    int length = Integer.parseInt(s);
                    pages = new int[length];
                    for (int i = 0; i < length; i++) {
                        pages[i] = x.nextInt(8);
                    }
                    flag = true;
                }
            }

        }while (!flag);
    }

    /**
     * 初始化物理块个数
     */
    public static void initPhysicalBlock(){
        do {
            s = JOptionPane.showInputDialog("物理块个数: ");
            if (s == null || "".equals(s)) {
                //没有做出选择, 强制重新输入
                flag = false;
            }else {
                if (!s.matches(regex)) {
                    flag = false;
                    JOptionPane.showMessageDialog(null,"数据格式错误！");
                } else {
                    capacity= Integer.parseInt(s);
                    stack = new StackOfLru(capacity);
                    flag = true;
                }
            }
        }while (!flag);
    }

    /**
     * 栈中不存在该数据，且栈满，则把栈底数据拿出，其他数据下降一层，栈顶元素加入
     * @param s 栈
     * @param a a[i]代表的那个数据
     */
    private static void dataNotExistAndStackFull(StackOfLru s, int a){
        StackOfLru tempStackOfLru = new StackOfLru(s.getCount()-1 );
        //注意，这里的循环控制条件为临时栈的容量而不是栈中元素的个数
        //取tempStack.getCapacity而不是.getCount()，因为这个栈满了，上面的n-1个数据
        //都要pop出来，此时tempStack.getCount()为0,不能循环0次。
        for (int j = 0; j < tempStackOfLru.getCapacity(); j++) {
            tempStackOfLru.push(s.pop());
        }
        //把最后一个数据pop出来
        s.pop();
        for (int k = 0; k < tempStackOfLru.getCapacity(); k++){
            s.push(tempStackOfLru.pop());
        }
        //把最新的栈顶元素push进去
        s.push(a);
    }

    /**
     * 栈中存在该数据，不管栈满不满，则获取该元素所在位置，它上面的元素下降一层，该数据重新加入栈顶
     * @param s 栈
     * @param a a[i]代表的那个数据
     */
    private static void dataExist(StackOfLru s, int a) {
        //如果这个元素就是栈顶元素，那么不需要做任何变化。
        if (a == s.peek()) {
            return;
        }
        int location = s.location(a, s);
        //临时栈大小
        int tempSize = s.getCount() - location - 1 ;
        StackOfLru tempStackOfLru = new StackOfLru(tempSize);
        //数据上面的元素push进新栈
        for(int m = 0; m < tempSize; m++){
            tempStackOfLru.push(s.pop());
        }
        //原来的栈s pop掉找到的数据
        s.pop();
        //新栈中的元素pop出来给原来的栈，一进一出。
        for (int m = 0; m < tempSize; m++){
            s.push(tempStackOfLru.pop());
        }
        //栈s加入存在的数据到栈顶
        s.push(a);
    }
    /**
     *  根据不同的情况更新栈的排列顺序
     */
    private static void updateStack(StackOfLru s, int[] a){
        //用来计算缺页率
        int hitNumber = 0;
        tA.append("页面序列:\n");
        for (int value : a) {
            tA.append(value + " ");
        }
        tA.append("\n");
        for (int value : a) {
            isHit = false;
            if (!s.isExist(value, s) && !s.isFull()) {
                //栈中不存在该数据，且栈未满，把数据加入到栈顶
                s.push(value);
                isHit = false;
            } else if (!s.isExist(value, s) && s.isFull()) {
                dataNotExistAndStackFull(s, value);
                isHit = false;
            } else if (s.isExist(value, s)) {
                dataExist(s, value);
                isHit = true;
                hitNumber++;
            }
            if (!isHit) {
                tA.append("    页面" + value  + "访问: " + s.toString() + " --> Page fault\n");
            }else {
                tA.append("    页面" + value  + "访问: " + s.toString() + " --> Page hit\n");
            }
        }
        tA.append("\n");
        tA.append("缺页次数: " + (a.length - hitNumber) + "\n");
        tA.append("缺页率: " + ( 1 - hitNumber * 1.0 /a.length) + "\n");
        tA.append("置换次数：" + (a.length - hitNumber - capacity) + "\n\n");
        label1.setText((a.length - hitNumber) + "");
        label3.setText(( 1- hitNumber * 1.0 /a.length) + "");
        label5.setText((a.length - hitNumber - capacity) + "");
    }

    public static void run1() {
        //比较相同的页面序列
        initPhysicalBlock();
        updateStack(stack, fixedArray);

    }

    public static void run2() {
        //输入页面序列
        initPagesByInput();
        initPhysicalBlock();
        updateStack(stack, pages);
    }

    public static void run3() {
        //随机生成页面序列
        initPagesByRandom();
        initPhysicalBlock();
        updateStack(stack, pages);
    }

}
