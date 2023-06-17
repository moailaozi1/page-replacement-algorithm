import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.*;

/**
 * @author GoldenRetriever
 * @version 1.0
 * @date 2019/12/27 13:17
 */
public class OptimaPanel extends JPanel {
    /**
     * 设置6个标签,用来显示相关信息, 一个文本域, 用来接受输出信息
     */
    static JLabel label = new JLabel("缺页次数");
    static JLabel label1 = new JLabel("0");
    static JLabel label2 = new JLabel("缺页率");
    static JLabel label3 = new JLabel("0");
    static JLabel label4 = new JLabel("命中次数");
    static JLabel label5 = new JLabel("0");
    static TextArea tA = new TextArea();

    /**
     * 容量capacity、页面序列pages、输入是否正确的flag标志、接受数据字符串s、正则数字表达式regex
     * map对象myLfu, 固定的页面序列fixedArray
     */
    private static int capacity;
    private static int[] pages;
    private static boolean flag = true;
    private static String s;
    private static String regex = "([0-9]|[1-9][0-9]*)";
    private static int[] fixedArray = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3,
            0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
    /**
     * cache的指针
     */
    private static int idx;
    private static int[] cache;
    /**
     * 记录cache中每个数出现的位置
     */
    private static Map<Integer, Integer> map;
    private static boolean isHit = false;

    public OptimaPanel() {
        //设置尺寸、大小
        Dimension sizeOfTa = new Dimension(250, 200);
        Dimension sizeOfLabel = new Dimension(100, 50);
        //设置文本域、标签的首选大小
        tA.setPreferredSize(sizeOfTa);
        label.setPreferredSize(sizeOfLabel);
        label1.setPreferredSize(sizeOfLabel);
        label2.setPreferredSize(sizeOfLabel);
        label3.setPreferredSize(sizeOfLabel);
        label4.setPreferredSize(sizeOfLabel);
        label5.setPreferredSize(sizeOfLabel);
        //设置标签内字体。
        Font myFont = new Font("宋体", Font.BOLD, 18);
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

    public static void initPagesByRandom() {
        Random x = new Random();
        do {
            s = JOptionPane.showInputDialog("请输入页面序列长度: ");
            if (s == null || "".equals(s)) {
                //没有做出选择
                flag = false;
            } else {
                if (!s.matches(regex)) {
                    flag = false;
                    JOptionPane.showMessageDialog(null, "数据格式错误！");
                } else {
                    int length = Integer.parseInt(s);
                    pages = new int[length];
                    for (int i = 0; i < length; i++) {
                        pages[i] = x.nextInt(8);
                    }
                    flag = true;
                }
            }

        } while (!flag);
    }

    /**
     * 输入初始化页面号序列
     */
    public static void initPagesByInput() {
        //输入信息，不可直接调用FifoPanel中的同样方法，否则输出信息时将输出到对面面板内。
        do {
            s = JOptionPane.showInputDialog("请输入页面序列(以空格分开): ");
            if (s == null || "".equals(s)) {
                //没有做出选择
                flag = false;
            } else {
                //正则表达式代表多个空格
                String regex2 = " +";
                String[] arr = s.split(regex2);
                pages = new int[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    //赋值前先来个判断，分析数据是否是数字。
                    if (!arr[i].matches(regex)) {
                        JOptionPane.showMessageDialog(null, "输入数据错误，其中包含非数字！",
                                "提醒", JOptionPane.ERROR_MESSAGE);
                        flag = false;
                        break;
                    } else {
                        pages[i] = Integer.parseInt(arr[i]);
                        flag = true;
                    }
                }
            }
        } while (!flag);
    }

    /**
     * 初始化物理块个数
     */
    public static void initPhysicalBlock() {
        do {
            s = JOptionPane.showInputDialog("物理块个数: ");
            if (s == null || "".equals(s)) {
                //没有做出选择, 强制重新输入
                flag = false;
            } else {
                if (!s.matches(regex)) {
                    flag = false;
                    JOptionPane.showMessageDialog(null, "数据格式错误！");
                } else {
                    capacity = Integer.parseInt(s);
                    cache = new int[capacity];
                    //给内存块中的数据赋值，-1代表该位置无数据
                    Arrays.fill(cache, -1);
                    // 将map重置，否则会包含上一个页面的序号，造成数据污染
                    map = new HashMap<>();
                    idx = 0;
                    flag = true;
                }
            }
        } while (!flag);
    }

    /**
     * 当前cache是否满
     */
    public static boolean isFull() {
        return idx == cache.length;
    }

    /**
     * 当前cache中是否存在这个数据
     */
    public static boolean isLocated(int n) {
        boolean isExist = false;
        for (int value : cache) {
            if (value == n) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public static void optimal(int[] arr) {
        tA.append("页面序列:\n");
        StringBuilder sb = new StringBuilder();
        for (int value : arr) {
            sb.append(value + " ");
        }
        tA.append(sb + "\n");
        //hitCounts命中次数，isAppear代表页面是否出现过，
        int hitCounts = 0;
        for (int i = 0; i < arr.length; i++) {
            tA.append("页面" + arr[i] + "访问，");
            // 首先判断cache是否已满
            if (!isFull()) {
                // 不满，判断数据是否存在于当前内存块中
                if (!map.containsKey(arr[i])) {
                    // 记录当前数据出现的位置
                    map.put(arr[i], idx);
                    cache[idx++] = arr[i];
                    isHit = false;
                } else {
                    // 存在该数据，则命中次数 + 1
                    hitCounts++;
                    isHit = true;
                }
            } else {
                // cache满了，判断数据是否存在
                if (map.containsKey(arr[i])) {
                    // 存在
                    hitCounts++;
                    isHit = true;
                } else {
                    // 不存在，需要置换，将后面数据寻找。1: 如果后面出现的数据都不在cache中，那么将第一个位置替换出现。 2：如果后面cache有一个没有命中，那么将他换出，
                    // 3.如果全部命中，那么来的最晚的最先替换（最晚，id靠后）
                    boolean finish = false;
                    // 记录后面的数，第一个出现的位置
                    Map<Integer, Integer> next = new HashMap<>();
                    for (int j = i + 1; j < arr.length; j++) {
                        if (!next.containsKey(arr[j])) {
                            next.put(arr[j], j);
                        }
                    }
                    int maxIndex = -1, number = -1;
                    for (Integer a : map.keySet()) {
                        if (!next.containsKey(a)) {
                            // 目前cache中的数据，不会在后面出现, 将arr[i]放到不会出现的数的位置
                            int position = map.get(a);
                            // 移除该键值对
                            map.remove(a);
                            cache[position] = arr[i];
                            map.put(arr[i], position);
                            finish = true;
                            break;
                        }else {
                            // 目前cache中的数据，将在后面出现，那么记录最晚出现的一个
                            if (next.get(a) > maxIndex) {
                                maxIndex = next.get(a);
                                number = a;
                            }
                        }
                    }
                    if (!finish) {
                        // 最晚出现的那个数，填充
                        int position = map.get(number);
                        map.remove(number);
                        cache[position] = arr[i];
                        map.put(arr[i], position);
                    }
                    isHit = false;
                }
            }
            tA.append("结果为: ");
            for (int value : cache) {
                if (value == -1) {
                    tA.append(" ");
                } else {
                    tA.append(value + " ");
                }
            }
            if (!isHit) {
                tA.append(" --> Page default\n");
            } else {
                tA.append(" --> Page hit\n");
            }
        }
        tA.append("\n缺页次数: " + (arr.length - hitCounts) + "\n");
        double lackRate = (arr.length - hitCounts) * 1.0 / arr.length;
        tA.append("缺页率: " + lackRate + "\n");
        tA.append("命中次数：" + hitCounts + "\n\n");
        label1.setText((arr.length - hitCounts) + "");
        label3.setText(lackRate + "");
        label5.setText(((arr.length - hitCounts) - capacity) + "");
    }

    public static void run1() {
        //使用相同序列
        initPhysicalBlock();
        optimal(fixedArray);
    }

    public static void run2() {
        //自己输入页面
        initPagesByInput();
        initPhysicalBlock();
        optimal(pages);
    }

    public static void run3() {
        //随机生成页面序列
        initPagesByRandom();
        initPhysicalBlock();
        optimal(pages);
    }

}
