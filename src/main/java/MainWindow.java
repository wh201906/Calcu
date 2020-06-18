import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Stack;
import java.util.ArrayList;

public class MainWindow
{
    private ArrayList<String> symbolList = new ArrayList<String>(Arrays.asList("(", ")"));
    private ArrayList<ArrayList<String>> operatorList = new ArrayList<>();

    private JPanel mainPanel;
    private JPanel inputPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button0;
    private JButton button_dot;
    private JButton button_equal;
    private JTextField textField;
    private JButton button_add;
    private JButton button_minus;
    private JButton button_multiply;
    private JButton button_divide;
    private JButton button_mod;

    private ActionListener inputListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            textField.setText(textField.getText() + ((JButton) (e.getSource())).getText());
        }
    };

    public MainWindow()
    {
        button0.addActionListener(inputListener);
        button1.addActionListener(inputListener);
        button2.addActionListener(inputListener);
        button3.addActionListener(inputListener);
        button4.addActionListener(inputListener);
        button5.addActionListener(inputListener);
        button6.addActionListener(inputListener);
        button7.addActionListener(inputListener);
        button8.addActionListener(inputListener);
        button9.addActionListener(inputListener);
        button_dot.addActionListener(inputListener);
        button_equal.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ArrayList<String> res1 = split(textField.getText());
                ArrayList<String> res2 = infix2suffix(res1);
            }
        });
        button_add.addActionListener(inputListener);
        button_minus.addActionListener(inputListener);
        button_multiply.addActionListener(inputListener);
        button_divide.addActionListener(inputListener);
        button_mod.addActionListener(inputListener);

        operatorList.add(new ArrayList<String>(Arrays.asList("*", "/", "%"))); // 运算符顺序列表可调
        operatorList.add(new ArrayList<String>(Arrays.asList("+", "-")));
//        operatorList.add(new ArrayList<String>(Arrays.asList("<<",">>")));
        for (ArrayList<String> list : operatorList)
        {
            symbolList.addAll(list);
        }
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private ArrayList<String> split(String expression)
    {
        ArrayList<String> result = new ArrayList<String>();
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < expression.length(); i++)
        {
            String sub = expression.substring(i, i + 1);
            if (symbolList.contains(sub))
            {
                if (!temp.toString().equals(""))
                {
                    result.add(temp.toString());
                }
                result.add(sub);
                temp.delete(0, temp.length());
            }
            else
            {
                temp.append(sub);
            }
        }
        if (!temp.toString().equals(""))
        {
            result.add(temp.toString());
        }
        return result;
    }

    private ArrayList<String> infix2suffix(ArrayList<String> infixexp)
    {

        Stack<String> stack = new Stack<String>();
        ArrayList<String> result = new ArrayList<String>();
        for (String item : infixexp) // 从左到右遍历中缀表达式
        {
            if (!symbolList.contains(item)) // 如果不是运算符，就直接加入返回列表
            {
                result.add(item);
            }
            else
            {
                if (!item.equals("(") && !item.equals(")")) // 如果是普通运算符，则进行进一步判断
                {
                    for (int i = 0; i < operatorList.size(); i++)
                    {
                        if (operatorList.get(i).contains(item)) // 在运算符列表中查找当前运算符
                        {
                            String topOperator = "";
                            int j = 0;
                            do
                            {
                                if (stack.isEmpty())
                                {
                                    break;
                                }
                                topOperator = stack.peek(); // 获取栈顶运算符
                                for (j = 0; j <= i; j++) // 在优先级大于等于当前运算符的列表中进行查找
                                {
                                    if (operatorList.get(j).contains(topOperator))
                                    {
                                        break;
                                    }
                                }
                                if (j <= i) // 如果栈顶运算符优先级大于等于当前运算符，则将其弹出并加入返回列表，循环这一过程
                                {
                                    result.add(stack.pop()); // 当中暗含了同运算级时的从左到右运算法则
                                }
                            } while (j <= i);
                            stack.add(item);
                            break;
                        }
                    }
                }
                else if (item.equals("(")) // 如果是左括号，则直接入栈，作为标记
                {
                    stack.push(item);
                }
                else // 如果是右括号，则将栈顶运算符直接弹出直至遇到左括号
                {
                    while (!stack.peek().equals("("))
                    {
                        result.add(stack.pop());
                    }
                    stack.pop(); // 处理完括号内运算符后丢弃左括号
                }
            }
        }
        while (!stack.isEmpty()) // 将栈中内容一次输出至返回列表
        {
            result.add(stack.pop());
        }
        return result;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(inputPanel, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        inputPanel.add(spacer3, gbc);
        button3 = new JButton();
        button3.setText("3");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button3, gbc);
        button4 = new JButton();
        button4.setText("4");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button4, gbc);
        button5 = new JButton();
        button5.setText("5");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button5, gbc);
        button6 = new JButton();
        button6.setText("6");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button6, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        inputPanel.add(spacer5, gbc);
        button7 = new JButton();
        button7.setText("7");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button7, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        inputPanel.add(spacer6, gbc);
        button0 = new JButton();
        button0.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button0, gbc);
        button8 = new JButton();
        button8.setText("8");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button8, gbc);
        button9 = new JButton();
        button9.setText("9");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button9, gbc);
        button_dot = new JButton();
        button_dot.setText(".");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button_dot, gbc);
        button_equal = new JButton();
        button_equal.setText("=");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button_equal, gbc);
        button2 = new JButton();
        button2.setText("2");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button2, gbc);
        button1 = new JButton();
        button1.setText("1");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(button1, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel1, gbc);
        button_add = new JButton();
        button_add.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button_add, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer7, gbc);
        button_minus = new JButton();
        button_minus.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button_minus, gbc);
        button_multiply = new JButton();
        button_multiply.setText("*");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button_multiply, gbc);
        button_divide = new JButton();
        button_divide.setText("/");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button_divide, gbc);
        button_mod = new JButton();
        button_mod.setText("%");
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button_mod, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer11, gbc);
        textField = new JTextField();
        textField.setHorizontalAlignment(4);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 2.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(textField, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }

}
