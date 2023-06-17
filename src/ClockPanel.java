import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * 内存空间类，用于表示一个内存空间单元
 */
class Block{
    /**
     * page页面号，表示存储于该内存页的页号，默认为-1
     * access使用标志位，默认值为0，表示未使用
     * modify修改标志位，默认值为0，表示未修改
     */
    int page = -1;
    int access = 0;
    int modify = 0;
}
/**
 * 页面类，表示要访问的页面号
 */
class Page {

    public Page(int page, int modify) {
        this.page = page;
        this.modify = modify;
    }
    /**
     * page表示该页面的页号
     * modify模拟该页面被装入内存块的时候，是否被修改，0表示未修改，1表示修改，默认值为0
     */
    int page = -1;
    int modify = 0;
}
/**
 * @author GoldenRetriever
 * @version 1.0
 * @date 2019/12/24 8:55
 */
public class ClockPanel extends JPanel{
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
     * 容量capacity、页面序列长度length页面序列pages、flag标志、接受数据字符串s、正则数字表达式regex
     */
    private static int capacity;
    private static int length;
    private static boolean flag = true;
    private static String s;
    private static String regex = "([0-9]|[1-9][0-9]*)";
    private static Random random = new Random();


    /**
     * 用类来创建相关数组，声明一个包含size个页面的内存块, 访问的页面序列
     */
    private static Block[] arr;
    private static Page[] pages;

    public ClockPanel() {
        Dimension sizeOfTa=new Dimension(250, 200);
        tA.setPreferredSize(sizeOfTa);
        this.add(tA);
        Dimension sizeOfLabel =new Dimension(100, 50);
        label.setPreferredSize(sizeOfLabel );
        label1.setPreferredSize(sizeOfLabel );
        label2.setPreferredSize(sizeOfLabel );
        label3.setPreferredSize(sizeOfLabel );
        label4.setPreferredSize(sizeOfLabel );
        label5.setPreferredSize(sizeOfLabel );
        Font myFont = new Font("宋体",Font.BOLD, 18);
        label.setFont(myFont);
        label1.setFont(myFont);
        label2.setFont(myFont);
        label3.setFont(myFont);
        label4.setFont(myFont);
        label5.setFont(myFont);
        this.add(label);
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
    }
    public static void initPagesByRandom(){
        do {
            s = JOptionPane.showInputDialog("请输入页面序列长度: ");
            if (s == null || "".equals(s)) {
                //没有做出选择, 强制重新输入
                flag = false;
            }else {
                if (!s.matches(regex)) {
                    flag = false;
                    JOptionPane.showMessageDialog(null,"数据格式错误！");
                } else {
                    length = Integer.parseInt(s);
                    pages = new Page[length];
                    int x  = JOptionPane.showConfirmDialog(null,"是否使用默认修改位和访问位");
                    if (x == JOptionPane.YES_OPTION) {
                        for (int i = 0; i < length; i++) {
                            pages[i] = new Page( random.nextInt(8),0);
                        }
                    }else if (x == JOptionPane.NO_OPTION){
                        JOptionPane.showMessageDialog(null,"随机生成修改位和访问位");
                        for (int i = 0; i < length; i++) {
                            pages[i] = new Page(random.nextInt(8),random.nextInt(10)%2);
                        }
                    }else {
                        break;
                    }
                    flag = true;
                }
            }

        }while (!flag);
    }
    public static void initPagesByFixNumber() {
        //通过固定的页面序列创建
         int[] fixedArray = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3,
                 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
         pages = new Page[fixedArray.length];
         for (int i =0; i < fixedArray.length; i++) {
             pages[i] = new Page(fixedArray[i],0);
         }
    }
    /**
     * 输入初始化页面号序列
     */
    public static void initPagesByInput(){
        //输入信息，不可直接调用FifoPanel中的同样方法，否则输出信息时将输出到对面面板内。
        outer:
        do {
            s = JOptionPane.showInputDialog("请输入页面序列(以空格分开): ");
            if (s == null || "".equals(s)) {
                //没有做出选择
                flag = false;
            }else {
                //正则表达式代表多个空格
                String regex2 = " +";
                String [] arr = s.split(regex2);
                for (String value : arr) {
                    //赋值前先来个判断，分析数据是否是数字。
                    if (!value.matches(regex)) {
                        JOptionPane.showMessageDialog(null, "输入数据错误，其中包含非数字！",
                                "提醒", JOptionPane.ERROR_MESSAGE);
                        flag = false;
                        break outer;
                    } else {
                        pages = new Page[arr.length];
                        flag = true;
                    }
                }
                int x  = JOptionPane.showConfirmDialog(null,"是否使用默认修改位和访问位");
                if (x == JOptionPane.YES_OPTION) {
                    for (int i =0; i <pages.length; i++ ) {
                        //是数字，那么直接转换为整形赋值
                        pages[i] = new Page(Integer.parseInt(arr[i]),0);
                    }
                }else if (x == JOptionPane.NO_OPTION){
                    JOptionPane.showMessageDialog(null,"随机生成修改位和访问位");
                    for (int i = 0; i < pages.length; i++) {
                        pages[i] = new Page(Integer.parseInt(arr[i]),random.nextInt(10)%2);
                    }
                }else {
                    break;
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
                    arr = new Block[capacity];
                    for(int i = 0;i < capacity;i++){
                        arr[i] = new Block();
                    }
                    flag = true;
                }
            }
        }while (!flag);
    }

    /**
     * 改进的clock算法
     */
    public static void clock(Block[] block, Page[] pages){

        tA.append("页面序列:\n");
        for (Page x : pages) {
            tA.append(x.page+" ");
        }
        tA.append("\n修改位:\n");
        for (Page x : pages) {
            tA.append(x.modify+" ");
        }
        tA.append("\n");
        //用于表示当前页面是否装入内存成功
        boolean flag;
        //缺页数
        int count = 0;
        //模拟访问页面的序列
        for (Page value : pages) {

            for (Block item : block) {
                tA.append("页面号: " + item.page + ", 访问位: " + item.access + ", 修改位: " + item.modify + "\n");
            }

            tA.append("将要访问的页面" + value.page + "\n");
            if (contain(block, value)) {
                //存在于内存块中，则不会产生缺页现象，继续执行外层，
                tA.append("命中！\n");
                for (Block item : block) {
                    tA.append("页面号: " + item.page + ", 访问位：" + item.access + ", 修改位: " + item.modify + "\n");
                }
                tA.append("***********分隔线************************\n");
                continue;
            } else {
                tA.append("...缺页...\n");
                count++;//如果内存块中不存在这个页面则产生一个缺页现象，寻找合适的置换页面
                flag = false;
            }

            //页面没有成功装入内存的时候
            while (!flag) {

                //第一步，寻找内存块中，使用位和修改位都为0的内存块，然后进行置换
                for (Block item : block) {
                    if (item.access == 0 && item.modify == 0) {
                        //找到了可以置换的页面，进行置换。并且修改使用位
                        item.page = value.page;
                        item.access = 1;
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    //成功装入内存,则直接跳出循环，不再执行，否则执行第二步
                    break;
                }

                //第二步，寻找使用位为0，修改位为1的页面进行置换，并且在遍历内存块的过程中，把遍历过的页面的使用位置为0；
                for (Block item : block) {

                    if (item.access == 0 && item.modify == 1) {
                        //找到了可以置换的页面，进行置换。并且修改使用位
                        item.page = value.page;
                        item.access = 1;
                        flag = true;
                        break;
                    } else {
                        //修改使用位为0
                        item.access = 0;
                    }
                }
            }

            for (Block item : block) {
                tA.append("页面号: " + item.page + ", 访问位: " + item.access + ", 修改位: " + item.modify + "\n");
            }
            tA.append("***********分隔线************************\n");
        }
        tA.append("\n");
        tA.append("缺页次数: " + count+ "\n");
        tA.append("缺页率: " + (count * 1.0 /pages.length) + "\n");
        tA.append("置换次数：" + (count- capacity) + "\n\n");
        label1.setText(count + "");
        label3.setText((count*1.0)/pages.length + "");
        label5.setText((count - capacity) + "");
    }

    /**
     * 判断page页面是否存在于block内存块中，如果存在，则将其使用位置为1
     * @param block 内存块数组
     * @param page  页面序列数组
     * @return boolean
     */

    public static boolean contain(Block[] block, Page page){

        for (Block value : block) {
            //如果页面存在于内存块中，则修改使用位和修改位
            if (value.page == page.page) {
                value.page = page.page;
                value.access = 1;
                value.modify = page.modify;
                return true;
            }
        }
        return false;
    }
    public static void run1() {
        //使用固定的页面序列
        initPagesByFixNumber();
        initPhysicalBlock();
        clock(arr,pages);

    }
    public static void run2() {
        //输入页面序列
        initPagesByInput();
        initPhysicalBlock();
        clock(arr,pages);
    }
    public static void run3() {
        //随机生成页面序列
        initPagesByRandom();
        initPhysicalBlock();
        clock(arr,pages);
    }
}