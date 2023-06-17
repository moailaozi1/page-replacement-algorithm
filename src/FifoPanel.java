import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author GoldenRetriever
 * @version 1.0
 * @date 2019/12/23 15:06
 */
class FifoPanel extends JPanel {
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
     * 队列容量capacity、页面序列pages、flag标志、接受数据字符串s、正则数字表达式regex
     * 队列对象myQueue, 固定的页面序列fixedArray, 页面是否命中标志isHit。虽然每个页面都有这个些全局变量，但是不可通用。
     */
    private static int capacity;
    private static int[] pages;
    private static boolean flag = true;
    private static String s;
    private static String regex = "([0-9]|[1-9][0-9]*)";
    private static QueueOfFifo myQueue;
    private static int[] fixedArray = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3,
            0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
    private static boolean isHit;

    /**
     * 构造函数
     */
    public FifoPanel() {
        //设置尺寸、大小
        Dimension sizeOfLabel =new Dimension(100, 50);
        Dimension sizeOfTa=new Dimension(250, 200);
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
        //将相关组件加入到面板中
        this.add(tA);
        this.add(label);
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
    }

    /**
     * 随机初始化页面号序列
     */
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
                        //页面序列范围[0, 8)
                        pages[i] =x.nextInt(8);
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
                    myQueue = new QueueOfFifo(capacity);
                    flag = true;
                }
            }
        }while (!flag);
    }
    /**
     * 输入初始化页面号序列
     */
    public static void initPagesByInput(){
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
    /**
     * FIFO页面置换算法。
     */
    public  static void fifo(QueueOfFifo q, int[] a) {
        //用来计算缺页率
        int hitNumber = 0;
        tA.append("页面序列:\n");
        for (int value : a) {
            tA.append(value + " ");
        }
        tA.append("\n");
        for (int value : a) {
            //设置初始命中情况为false
            isHit = false;
            if (!q.isExist(value) && !q.isFull()) {
                q.insert(value);
                isHit = false;
            } else if (q.isExist(value)) {
                //存在数据，那么统计数加1
                hitNumber++;
                isHit = true;
            } else {
                q.remove();
                q.insert(value);
                isHit = false;
            }
            if (!isHit) {
                tA.append("    页面" + value  + "访问: " + q.toString() + " --> Page fault\n");
            }else {
                tA.append("    页面" + value  + "访问: " + q.toString() + " --> Page hit\n");
            }
        }
        tA.append("\n");
        tA.append("缺页次数: " + (a.length - hitNumber) + "\n");
        tA.append("缺页率: " + ( 1 - hitNumber * 1.0 /a.length) + "\n");
        tA.append("置换次数：" + (a.length - hitNumber - capacity) + "\n\n");
        label1.setText((a.length - hitNumber) + "");
        label3.setText(( 1 - hitNumber * 1.0 /a.length) + "");
        label5.setText((a.length - hitNumber - capacity) + "");
    }
    public static void run1() {
        //比较相同的页面序列
        initPhysicalBlock();
        fifo(myQueue, fixedArray);

    }
    public static void run2() {
        //输入页面序列
        initPagesByInput();
        initPhysicalBlock();
        fifo(myQueue, pages);
    }
    public static void run3() {
        //使用随机生成页面序列
        initPagesByRandom();
        initPhysicalBlock();
        fifo(myQueue, pages);
    }
}
