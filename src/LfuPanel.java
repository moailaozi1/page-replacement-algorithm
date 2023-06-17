import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 * @author GoldenRetriever
 * @version 1.0
 * @date 2019/12/23 22:01
 */
class LfuPanel extends JPanel {
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
     * map容量capacity、页面序列长度length页面序列pages、flag标志、接受数据字符串s、正则数字表达式regex
     * map对象myLfu, 固定的页面序列fixedArray
     */
    private static int capacity;
    private static int[] pages;
    private static boolean flag = true;
    private static String s;
    private static String regex = "([0-9]|[1-9][0-9]*)";
    private static int[] fixedArray = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3,
            0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
    private static int[] tempNumber;
    public LfuPanel() {
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
                        pages[i] =x.nextInt(8);
                    }
                    flag = true;
                }
            }

        }while (!flag);
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
                    flag = true;
                }
            }
        }while (!flag);
    }

    private static void lfu(int[] arr, int n) {
        //用来计算缺页率
        int hit = 0;
        tA.append("页面序列:\n");
        for (int value : arr) {
            tA.append(value + " ");
        }
        tA.append("\n");
        //arr存储初始化的页面序列，n存储物理块个数, number存储每个页面对应的出现次数
        //cache保存当前内存块中的页面序列
        boolean isHit = false;
        int[] number = new int[arr.length];
        int[] cache = new int[n];
        //hit 用来计算命中率 flag作为判断标志
        boolean flag = false;
        //初始化内存块中的页面，-1代表未放置
        Arrays.fill(cache, -1);
        //遍历页面序列，开始Lfu算法
        for (int i = 0; i < arr.length; i++) {
            isHit = false;
            tA.append("    页面" + arr[i] + "访问");
            if (i == 0){
                //当i = 0时，即第一个页面来访问，置其出现次数为0
                number[0] = 0;
            }else {
                //当 i > 0时，备份
                /**
                 * 临时遍历数组，用来给当前arr[i]之前的数据备份。
                 */
                tempNumber = new int[i];
                //出现次数
                int appearTime = 0;
                //数组复制，如arr中为4 7 0 7 1 0, 当i = 3时，备份数据4 7 0到临时数组。
                System.arraycopy(arr, 0, tempNumber, 0, i - 1 + 1);
                //给当前值arr[i]寻找之前出现过的次数，如果出现过一次，那么出现次数就+1
                for (int value : tempNumber) {
                    if (arr[i] == value) {
                        appearTime++;
                    }
                }
                for (int j = 0; j < tempNumber.length; j++) {
                    if (arr[i] == tempNumber[j] && appearTime >=1) {
                        //number[i]保存当前数据出现的次数,当前数据前面出现过，访问次数+1
                        number[j] = number[j] + 1;
                    }else {
                        //没出现过的新数据
                        number[i] = 0;
                    }
                }
            }
            //先判断内存块cache是否满,方法时在cache中找数据，如果有-1存在，代表还没有赋值，未满。
            boolean isFull = true;
            for (int value : cache) {
                if (value == -1) {
                    //找到有-1的数据，代表不满
                    isFull = false;
                    break;
                }
            }
            if (!isFull) {
                //如果不满，判断数据是否存在于当前内存块中
                for (int j =0; j < cache.length; j++) {
                    if (cache[j] == arr[i]) {
                        flag = true;
                        break;
                    }else {
                        flag = false;
                    }
                }
                if (!flag) {
                    //如果内存cache中不存在这个数据，那么遍历cache，将遇到的第一个-1置为该数据
                    for (int x = 0; x < cache.length; x ++) {
                        if (cache[x] == -1) {
                            cache[x] = arr[i];
                            number[i]++;
                            isHit =false;
                            break;
                        }
                    }

                }else{
                    //存在该数据，那么需要将该数据的出现次数+1，同时命中次数也+1
                    hit++;
                    isHit = true;
                    number[i]++;
                }
            }else {
                //如果满，也要判断是否存在数据
                for (int j =0; j < cache.length; j++) {
                    if (cache[j] == arr[i]) {
                        //如果内存块中出现了该数据，那么就说明数据存在
                        flag = true;
                        break;
                    }else {
                        flag = false;
                    }
                }
                //将这个数据的访问次数+1，同时当前相同的数据访问次数也置相同的值
                if (flag) {
                    //存在+1
                    hit++;
                    for (int m =0; m < tempNumber.length; m++) {
                        if (arr[i] == tempNumber[m]) {
                            number[i] = number[m];
                            isHit = true;
                            break;
                        }
                    }
                }else {
                    //如果不存在这个数据,先找到要被替换数据的位置
                    int[] nowNumber = new int[cache.length];
                    for (int m = 0 ; m < cache.length; m++) {
                        for (int q = 0 ; q < arr.length; q++) {
                            if (cache[m] == arr[q]) {
                                nowNumber[m] = number[q];
                                break;
                            }
                        }
                    }
                    isHit = false;
                    int min = 99999;
                    for (int m = 0; m < nowNumber.length; m++) {
                        if (nowNumber[m] < min) {
                            min = nowNumber[m];
                        }
                    }
                    outer:for (int m =0; m < arr.length; m++) {
                        for (int q = 0; q < cache.length; q++) {
                            if (number[m] == min && arr[m] == cache[q] ) {
                                cache[q] = arr[i];
                                number[i]++;
                                break outer;
                            }
                        }
                    }
                    for (int m =0; m < tempNumber.length; m++) {
                        if (arr[i] == tempNumber[m]) {
                            number[i] = number[m];
                            break;
                        }
                    }
                }
            }
            tA.append(" 结果为: ");
            for (int value : cache) {
                if(value == -1) {
                    tA.append(" ");
                }else {
                    tA.append(value + " ");
                }
            }
            if (!isHit) {
                tA.append(" --> Page default\n");
            }else {
                tA.append(" --> Page hit\n");
            }

        }
        tA.append( "\n缺页次数: " + (arr.length - hit)  + "\n" );
        double lackRate = (arr.length - hit) * 1.0 / arr.length;
        tA.append("缺页率: " + lackRate + "\n");
        tA.append("置换次数：" + ((arr.length - hit) - capacity) + "\n\n");
        label1.setText((arr.length - hit) + "");
        label3.setText(lackRate + "");
        label5.setText(((arr.length - hit) - capacity) + "");
    }
    public static void run1() {
        //使用相同序列
        initPhysicalBlock();
        lfu(fixedArray,capacity);
    }

    public static void run2() {
        //自己输入页面
        initPagesByInput();
        initPhysicalBlock();
        lfu(pages, capacity);
    }
    public static void run3() {
        //随机生成页面序列
        initPagesByRandom();
        initPhysicalBlock();
        lfu(pages, capacity);
    }
}
