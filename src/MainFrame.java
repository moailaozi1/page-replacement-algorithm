import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author GoldenRetriever
 * @version 1.0
 * @date 2019/12/23 10:46
 */
public class MainFrame extends JFrame implements ActionListener {

    /**
     * 管理按钮的面板jp1, 管理五个独立算法面板的总面板jp2, (jp3、jp4、jp5、jp6，jp7为各算法的分面板），jp8面板用于放置一些标签
     */
    JPanel jp1 = new JPanel();
    JPanel jp2 = new JPanel();
    FifoPanel jp3 = new FifoPanel();
    LruPanel jp4 = new LruPanel();
    LfuPanel jp5 = new LfuPanel();
    ClockPanel jp6 = new ClockPanel();
    OptimaPanel jp7 = new OptimaPanel();
    JPanel jp8 = new JPanel();
    /**
     * 设置标签, 用来表示相关信息
     */
    JLabel labelOfOptimal = new JLabel("Optimal算法的数据结构为数组 + HashMap！时间复杂度O(n^2),理论上不可实现，此为简单模拟！");
    JLabel labelOfFifo = new JLabel("Fifo算法的数据结构为Queue！");
    JLabel labelOfLru = new JLabel("Lru算法的数据结构为Stack！");
    JLabel labelOfLfu = new JLabel("Lfu算法的数据结构为数组!时间复杂度O(n^3)");
    JLabel labelOfClock = new JLabel("Clock算法实现借助外部类!");
    JLabel labelOfPages = new JLabel("所有的默认页面序列为:7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1");
    JLabel labelOfState = new JLabel("Clock算法默认访问位, 修改位均为0，自己输入序列时随机生成修改位");
    /**
     * 五个button, 分别用来注册监听按钮，调用各自的算法。
     */

    JButton btn1 = new JButton("FIFO算法");
    JButton btn2 = new JButton("LRU算法");
    JButton btn3 = new JButton("LFU算法");
    JButton btn4 = new JButton("CLOCK算法");
    JButton btn5 = new JButton("OPT算法");
    /**
     * 主窗体构造函数
     */
    public MainFrame() {
        new JFrame("主窗体");
        //设置主窗体的布局方式为方位布局
        this.setLayout(new BorderLayout());
        //面板jp2, jp7设置为盒式布局，BoxLayout.X_AXIS表示水平方向排列，.Y_AXIS代表垂直方向排列
        BoxLayout box = new BoxLayout(jp2, BoxLayout.X_AXIS);
        BoxLayout box1 = new BoxLayout(jp8, BoxLayout.Y_AXIS);
        jp2.setLayout(box);
        jp8.setLayout(box1);
        //设置尺寸、大小
        Dimension sizeOfBtn=new Dimension(100, 40);
        Dimension sizeofPanel = new Dimension(120,200);
        Dimension sizeOfDown = new Dimension(1200, 180);
        //设置字体大小
        Font myFont = new Font("宋体",Font.BOLD, 18);
        labelOfOptimal.setFont(myFont);
        labelOfFifo.setFont(myFont);
        labelOfLru.setFont(myFont);
        labelOfLfu.setFont(myFont);
        labelOfClock.setFont(myFont);
        labelOfPages.setFont(myFont);
        labelOfState.setFont(myFont);
        //标签的对齐方式为居中对齐
        labelOfOptimal.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOfFifo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOfLru.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOfLfu.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOfClock.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOfPages.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOfState.setAlignmentX(Component.CENTER_ALIGNMENT);
        //按钮的首选大小
        btn1.setPreferredSize(sizeOfBtn);
        btn2.setPreferredSize(sizeOfBtn);
        btn3.setPreferredSize(sizeOfBtn);
        btn4.setPreferredSize(sizeOfBtn);
        btn5.setPreferredSize(sizeOfBtn);
        //面板的首选大小
        jp3.setPreferredSize(sizeofPanel);
        jp4.setPreferredSize(sizeofPanel);
        jp5.setPreferredSize(sizeofPanel);
        jp6.setPreferredSize(sizeofPanel);
        jp7.setPreferredSize(sizeofPanel);
        jp8.setPreferredSize(sizeOfDown);
        //给按钮注册相关的监听事件, Lambda表达式
        btn1.addActionListener(e -> {
            String[] chooses = {"使用固定的页面序列", "我要自己输入页面序列", "使用随机生成页面序列"};
            String choose= (String) JOptionPane.showInputDialog(null, "make a choose",
            "Fifo Choose", JOptionPane.QUESTION_MESSAGE, null, chooses, chooses[0]);
            if (choose == null || "".equals(choose)) {
                //没有做选择
                repaint();
            }else {
                if (choose.equals(chooses[0])) {
                    FifoPanel.run1();
                }else if (choose.equals(chooses[1])) {
                    FifoPanel.run2();
                }else {
                    FifoPanel.run3();
                }
            }
        });
        btn2.addActionListener(e -> {
            String[] chooses = {"使用固定的页面序列", "我要自己输入页面序列", "使用随机生成页面序列"};
            String choose= (String) JOptionPane.showInputDialog(null, "make a choose",
                    "Lru Choose", JOptionPane.QUESTION_MESSAGE, null, chooses, chooses[0]);
            if (choose == null || "".equals(choose)) {
                //没有做选择
                repaint();
            }else {
                if (choose.equals(chooses[0])) {
                    LruPanel.run1();
                }else if (choose.equals(chooses[1])) {
                    LruPanel.run2();
                }else {
                    LruPanel.run3();
                }
            }
        });
        btn3.addActionListener(e -> {
            String[] chooses = {"使用固定的页面序列", "我要自己输入页面序列", "使用随机生成页面序列"};
            String choose= (String) JOptionPane.showInputDialog(null, "make a choose",
                    "Lfu Choose", JOptionPane.QUESTION_MESSAGE, null, chooses, chooses[0]);
            if (choose == null || "".equals(choose)) {
                //没有做选择
                repaint();
            }else {
                if (choose.equals(chooses[0])) {
                    LfuPanel.run1();
                }else if (choose.equals(chooses[1])) {
                    LfuPanel.run2();
                }else {
                    LfuPanel.run3();
                }
            }
        });
        btn4.addActionListener(e -> {
            String[] chooses = {"使用默认的页面序列和修改位", "我要自己输入页面序列和随机修改位", "使用随机生成页面序列和修改位"};
            String choose= (String) JOptionPane.showInputDialog(null, "make a choose",
                    "Clock Choose", JOptionPane.QUESTION_MESSAGE, null, chooses, chooses[0]);
            if (choose == null || "".equals(choose)) {
                //没有做选择
                repaint();
            }else {
                if (choose.equals(chooses[0])) {
                    ClockPanel.run1();
                }else if (choose.equals(chooses[1])) {
                    ClockPanel.run2();
                }else {
                    ClockPanel.run3();
                }
            }
        });
        btn5.addActionListener(e -> {
            String[] chooses = {"使用固定的页面序列", "我要自己输入页面序列", "使用随机生成页面序列"};
            String choose= (String) JOptionPane.showInputDialog(null, "make a choose",
                    "Optimal Choose", JOptionPane.QUESTION_MESSAGE, null, chooses, chooses[0]);
            if (choose == null || "".equals(choose)) {
                //没有做选择
                repaint();
            }else {
                if (choose.equals(chooses[0])) {
                    OptimaPanel.run1();
                }else if (choose.equals(chooses[1])) {
                    OptimaPanel.run2();
                }else {
                    OptimaPanel.run3();
                }
            }
        });
        //设置面板的边框标题
        jp1.setBorder(new TitledBorder("菜单栏"));
        jp2.setBorder(new TitledBorder("算法显示栏"));
        jp3.setBorder(new TitledBorder("FIFO算法"));
        jp4.setBorder(new TitledBorder("LRU算法"));
        jp5.setBorder(new TitledBorder("LFU算法"));
        jp6.setBorder(new TitledBorder("CLOCK算法"));
        jp7.setBorder(new TitledBorder("Optimal算法"));
        jp8.setBorder(new TitledBorder("显示一些东西"));
        //将按钮加入到面板jp1
        jp1.add(btn5);
        jp1.add(btn1);
        jp1.add(btn2);
        jp1.add(btn3);
        jp1.add(btn4);
        //将分面板加入到面板jp2
        jp2.add(jp7);
        jp2.add(jp3);
        jp2.add(jp4);
        jp2.add(jp5);
        jp2.add(jp6);
        //将显示信息的标签加入到面板jp8
        jp8.add(labelOfOptimal);
        jp8.add(labelOfFifo);
        jp8.add(labelOfLru);
        jp8.add(labelOfLfu);
        jp8.add(labelOfClock);
        jp8.add(labelOfState);
        jp8.add(labelOfPages);
        //主窗体添加面板，并设置相关位置，采用方位布局
        this.add(jp1, BorderLayout.NORTH);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp8, BorderLayout.SOUTH);
        this.setTitle("主窗体");
        //不可更改窗体大小
        this.setResizable(false);
        this.setSize(1500, 800);
        this.setLocation(200,100);
        //关闭窗体, 结束程序运行
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args){
        new MainFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

