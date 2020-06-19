import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.ArrayList;

public class MainWindow
{
    private ArrayList<String> symbolList = new ArrayList<String>(Arrays.asList("(", ")"));
    private ArrayList<ArrayList<String>> operatorList = new ArrayList<>();

    private JPanel mainPanel;
    private JPanel digitPanel;
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
    private JTextField inputField;
    private JButton button_add;
    private JButton button_minus;
    private JButton button_multiply;
    private JButton button_divide;
    private JButton button_mod;
    private JTextField outputField;
    private JButton button_clear;
    private JPanel outputPanel;
    private JPanel operatorPanel;
    private JPanel radixPanel;
    private JRadioButton hexRadioButton;
    private JRadioButton decRadioButton;
    private JRadioButton octRadioButton;
    private JRadioButton binRadioButton;
    private JButton button_left;
    private JButton button_right;
    private JButton button_pow;
    private JButton button_and;
    private JButton button_or;
    private JButton button_lShift;
    private JButton button_rShift;
    private JPanel inputPanel;

    private int lastRadix = 10;
    private int radix = 10;

    private ActionListener inputListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            inputField.setText(inputField.getText() + ((JButton) (e.getSource())).getText());
            inputField.grabFocus();
        }
    };

    private ItemListener radixListener = new ItemListener()
    {
        int val = 0;

        @Override
        public void itemStateChanged(ItemEvent e)
        {
            Object source = e.getSource();
            if (source == hexRadioButton)
            {
                radix = 16;
                val = Integer.valueOf(outputField.getText(), lastRadix);
                outputField.setText(Integer.toHexString(val));
                lastRadix = 16;
            }
            else if (source == decRadioButton)
            {
                radix = 10;
                val = Integer.valueOf(outputField.getText(), lastRadix);
                outputField.setText(Integer.toString(val));
                lastRadix = 10;
            }
            else if (source == octRadioButton)
            {
                radix = 8;
                val = Integer.valueOf(outputField.getText(), lastRadix);
                outputField.setText(Integer.toOctalString(val));
                lastRadix = 8;
            }
            else if (source == binRadioButton)
            {
                radix = 2;
                val = Integer.valueOf(outputField.getText(), lastRadix);
                outputField.setText(Integer.toBinaryString(val));
                lastRadix = 2;
            }
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
                boolean isFailed = false;
                ArrayList<String> res1;
                ArrayList<String> res2;
                String res3 = "";
                if (inputField.getText().trim().isEmpty())
                {
                    return;
                }
                try
                {
                    res1 = split(inputField.getText().trim());
                    res2 = infix2suffix(res1);
                    res3 = computeSuffix(res2);
                } catch (Exception exception)
                {
                    isFailed = true;
                    JOptionPane.showMessageDialog(mainPanel, "表达式有误！", "错误", JOptionPane.WARNING_MESSAGE);
                }
                if (isFailed)
                {
                    outputField.setText("Error!");
                }
                else
                {
                    outputField.setText(res3);
                }
            }
        });
        button_add.addActionListener(inputListener);
        button_minus.addActionListener(inputListener);
        button_multiply.addActionListener(inputListener);
        button_divide.addActionListener(inputListener);
        button_mod.addActionListener(inputListener);
        button_left.addActionListener(inputListener);
        button_right.addActionListener(inputListener);
        button_lShift.addActionListener(inputListener);
        button_rShift.addActionListener(inputListener);
        button_pow.addActionListener(inputListener);
        button_and.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inputField.setText(inputField.getText() + "&");
                inputField.grabFocus();
            }
        });
        button_or.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inputField.setText(inputField.getText() + "|");
                inputField.grabFocus();
            }
        });

        inputField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    button_equal.doClick();
                }
                else if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
                {
                    button_clear.doClick();
                }
            }
        });

        operatorList.add(new ArrayList<String>(Collections.singletonList("^")));
        operatorList.add(new ArrayList<String>(Arrays.asList("*", "/", "%"))); // 运算符顺序列表可调
        operatorList.add(new ArrayList<String>(Arrays.asList("+", "-")));
        operatorList.add(new ArrayList<String>(Arrays.asList("<<", ">>")));
        operatorList.add(new ArrayList<String>(Collections.singletonList("&")));
        operatorList.add(new ArrayList<String>(Collections.singletonList("|")));
        for (ArrayList<String> list : operatorList)
        {
            symbolList.addAll(list);
        }
        button_clear.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inputField.setText("");
            }
        });
        hexRadioButton.addItemListener(radixListener);
        decRadioButton.addItemListener(radixListener);
        octRadioButton.addItemListener(radixListener);
        binRadioButton.addItemListener(radixListener);
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new MainWindow().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private ArrayList<String> split(String exp)
    {
        String expression = exp.trim();
        ArrayList<String> morphemes = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
        StringBuffer temp = new StringBuffer();
        ArrayList<Integer> symbolPos = new ArrayList<Integer>();
        ArrayList<Integer> symbolLen = new ArrayList<Integer>();

        int offset = 0;
        int firstSymbolPos = expression.length();
        int firstSymbolLen = 0;
        boolean symbolFound = false;

        do
        {
            symbolFound = false;
            firstSymbolPos = expression.length();
            firstSymbolLen = 0;
            for (String symbol : symbolList)
            {
                int curr = expression.indexOf(symbol, offset);
                if (curr != -1 && curr < firstSymbolPos)
                {
                    symbolFound = true;
                    firstSymbolPos = curr;
                    firstSymbolLen = symbol.length();
                }
            }
            if (symbolFound)
            {
                offset = firstSymbolPos + firstSymbolLen;
                symbolPos.add(firstSymbolPos);
                symbolLen.add(firstSymbolLen);
            }
        } while (symbolFound);

        offset = 0;
        String substr = "";
        int symbolCoun = symbolPos.size() - 1;


        substr = symbolPos.size() > 0 ? expression.substring(0, symbolPos.get(0)) : expression;
        if (!substr.isEmpty())
        {
            morphemes.add(substr);
        }

        for (int i = 0; i < symbolCoun; i++)
        {
            substr = expression.substring(symbolPos.get(i), symbolPos.get(i) + symbolLen.get(i));
            morphemes.add(substr);
            substr = expression.substring(symbolPos.get(i) + symbolLen.get(i), symbolPos.get(i + 1));
            if (!substr.isEmpty())
            {
                morphemes.add(substr);
            }
        }

        if (symbolPos.size() > 0)
        {
            substr = expression.substring(symbolPos.get(symbolCoun), symbolPos.get(symbolCoun) + symbolLen.get(symbolCoun));
            morphemes.add(substr);
            substr = expression.substring(symbolPos.get(symbolCoun) + symbolLen.get(symbolCoun));
            if (!substr.isEmpty())
            {
                morphemes.add(substr);
            }
        }

        if (morphemes.get(0).equals("-"))
        {
            result.add("0");
        }

        for (int i = 0; i < morphemes.size(); i++)
        {
            if ((i == 0 || symbolList.contains(morphemes.get(i - 1))) && morphemes.get(i).equals("-"))
            {
                result.add("0");
            }
            result.add(morphemes.get(i));
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
            else if (!item.equals("(") && !item.equals(")")) // 如果是普通运算符，则进行进一步判断
            {
                // 当前操作符优先级小于等于栈顶操作符时，弹出栈顶操作符
                CmpResult cmpResult = stack.isEmpty() ? CmpResult.CMP_ERROR : operatorCmp(item, stack.peek());
                while (cmpResult == CmpResult.CMP_BELOW || cmpResult == CmpResult.CMP_EQUAL)
                {
                    result.add(stack.pop());
                    cmpResult = stack.isEmpty() ? CmpResult.CMP_ERROR : operatorCmp(item, stack.peek());
                }
                stack.push(item);
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
        while (!stack.isEmpty()) // 将栈中内容一次输出至返回列表
        {
            result.add(stack.pop());
        }
        return result;
    }

    private enum CmpResult
    {
        CMP_ERROR, CMP_BELOW, CMP_EQUAL, CMP_ABOVE
    }

    private CmpResult operatorCmp(String a, String b)
    {
        int a_level = -1;
        int b_level = -1;
        for (int i = 0; i < operatorList.size(); i++)
        {
            if (operatorList.get(i).contains(a))
            {
                a_level = i;
                break;
            }
        }
        for (int j = 0; j < operatorList.size(); j++)
        {
            if (operatorList.get(j).contains(b))
            {
                b_level = j;
                break;
            }
        }
        if (a_level == -1 || b_level == -1)
        {
            return CmpResult.CMP_ERROR;
        }
        else if (a_level > b_level) // 从0到n优先级降低，则level值越大优先级越低
        {
            return CmpResult.CMP_BELOW;
        }
        else if (a_level == b_level)
        {
            return CmpResult.CMP_EQUAL;
        }
        else // a_level < b_level
        {
            return CmpResult.CMP_ABOVE;
        }
    }

    private String computeSuffix(ArrayList<String> suffixexp)
    {
        Stack<String> stack = new Stack<String>();
        for (String item : suffixexp)
        {
            if (!symbolList.contains(item))
            {
                stack.push(item);
            }
            else
            {
                String b = stack.pop();
                String a = stack.pop();
                stack.push(compute(a, b, item)); // 逆序特性
            }
        }
        return stack.isEmpty() ? "" : stack.peek();
    }

    private String compute(String a, String b, String operator)
    {

        double a_val = Double.parseDouble(a);
        double b_val = Double.parseDouble(b);
        double result = (double) 0;
        if (operator.equals("+"))
        {
            result = a_val + b_val;
        }
        else if (operator.equals("-"))
        {
            result = a_val - b_val;
        }
        else if (operator.equals("*"))
        {
            result = a_val * b_val;
        }
        else if (operator.equals("/"))
        {
            result = a_val / b_val;
        }
        else if (operator.equals("%"))
        {
            result = a_val % b_val;
        }
        else if (operator.equals("<<"))
        {
            result = (double) ((int) (a_val + 0.5) << (int) (b_val + 0.5));
        }
        else if (operator.equals(">>"))
        {
            result = (double) ((int) (a_val + 0.5) >> (int) (b_val + 0.5));
        }
        else if (operator.equals("&"))
        {
            result = (double) ((int) (a_val + 0.5) & (int) (b_val + 0.5));
        }
        else if (operator.equals("|"))
        {
            result = (double) ((int) (a_val + 0.5) | (int) (b_val + 0.5));
        }
        else if (operator.equals("^"))
        {
            result = Math.pow(a_val, b_val);
        }
        if (Math.floor(result) == Math.ceil(result))
        {
            return String.valueOf((int) result);
        }
        else
        {
            return String.valueOf(result);
        }
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
        mainPanel.setPreferredSize(new Dimension(400, 400));
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        radixPanel = new JPanel();
        radixPanel.setLayout(new GridBagLayout());
        radixPanel.setPreferredSize(new Dimension(350, 45));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(radixPanel, gbc);
        radixPanel.setBorder(BorderFactory.createTitledBorder(null, "Radix", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        hexRadioButton = new JRadioButton();
        hexRadioButton.setFocusable(false);
        hexRadioButton.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        radixPanel.add(hexRadioButton, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radixPanel.add(spacer2, gbc);
        decRadioButton = new JRadioButton();
        decRadioButton.setFocusable(false);
        decRadioButton.setSelected(true);
        decRadioButton.setText("Dec");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        radixPanel.add(decRadioButton, gbc);
        octRadioButton = new JRadioButton();
        octRadioButton.setFocusable(false);
        octRadioButton.setText("Oct");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        radixPanel.add(octRadioButton, gbc);
        binRadioButton = new JRadioButton();
        binRadioButton.setFocusable(false);
        binRadioButton.setText("Bin");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        radixPanel.add(binRadioButton, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radixPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        radixPanel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer5, gbc);
        outputPanel = new JPanel();
        outputPanel.setLayout(new GridBagLayout());
        outputPanel.setPreferredSize(new Dimension(350, 60));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(outputPanel, gbc);
        outputField = new JTextField();
        outputField.setEditable(false);
        outputField.setHorizontalAlignment(4);
        outputField.setOpaque(true);
        outputField.setPreferredSize(new Dimension(250, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        outputPanel.add(outputField, gbc);
        inputField = new JTextField();
        inputField.setHorizontalAlignment(4);
        inputField.setOpaque(true);
        inputField.setPreferredSize(new Dimension(250, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        outputPanel.add(inputField, gbc);
        button_clear = new JButton();
        button_clear.setMaximumSize(new Dimension(75, 30));
        button_clear.setMinimumSize(new Dimension(75, 30));
        button_clear.setOpaque(true);
        button_clear.setPreferredSize(new Dimension(75, 30));
        button_clear.setText("Clear");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        outputPanel.add(button_clear, gbc);
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setMinimumSize(new Dimension(200, 208));
        inputPanel.setPreferredSize(new Dimension(400, 208));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(inputPanel, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(spacer6, gbc);
        operatorPanel = new JPanel();
        operatorPanel.setLayout(new GridBagLayout());
        operatorPanel.setPreferredSize(new Dimension(150, 208));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(operatorPanel, gbc);
        button_add = new JButton();
        button_add.setMaximumSize(new Dimension(100, 30));
        button_add.setMinimumSize(new Dimension(20, 30));
        button_add.setPreferredSize(new Dimension(60, 30));
        button_add.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_add, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(spacer7, gbc);
        button_minus = new JButton();
        button_minus.setMaximumSize(new Dimension(100, 30));
        button_minus.setMinimumSize(new Dimension(20, 30));
        button_minus.setPreferredSize(new Dimension(60, 30));
        button_minus.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_minus, gbc);
        button_left = new JButton();
        button_left.setMaximumSize(new Dimension(100, 30));
        button_left.setMinimumSize(new Dimension(20, 30));
        button_left.setPreferredSize(new Dimension(60, 30));
        button_left.setText("(");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_left, gbc);
        button_right = new JButton();
        button_right.setMaximumSize(new Dimension(100, 30));
        button_right.setMinimumSize(new Dimension(20, 30));
        button_right.setPreferredSize(new Dimension(60, 30));
        button_right.setText(")");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_right, gbc);
        button_multiply = new JButton();
        button_multiply.setMaximumSize(new Dimension(100, 30));
        button_multiply.setMinimumSize(new Dimension(20, 30));
        button_multiply.setPreferredSize(new Dimension(60, 30));
        button_multiply.setText("*");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_multiply, gbc);
        button_divide = new JButton();
        button_divide.setMaximumSize(new Dimension(100, 30));
        button_divide.setMinimumSize(new Dimension(20, 30));
        button_divide.setPreferredSize(new Dimension(60, 30));
        button_divide.setText("/");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_divide, gbc);
        button_mod = new JButton();
        button_mod.setMaximumSize(new Dimension(100, 30));
        button_mod.setMinimumSize(new Dimension(20, 30));
        button_mod.setPreferredSize(new Dimension(60, 30));
        button_mod.setText("%");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_mod, gbc);
        button_pow = new JButton();
        button_pow.setMaximumSize(new Dimension(100, 30));
        button_pow.setMinimumSize(new Dimension(20, 30));
        button_pow.setPreferredSize(new Dimension(60, 30));
        button_pow.setText("^");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_pow, gbc);
        button_and = new JButton();
        button_and.setActionCommand("AND");
        button_and.setLabel("AND");
        button_and.setMaximumSize(new Dimension(100, 30));
        button_and.setMinimumSize(new Dimension(20, 30));
        button_and.setPreferredSize(new Dimension(60, 30));
        button_and.setRequestFocusEnabled(true);
        button_and.setText("AND");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_and, gbc);
        button_or = new JButton();
        button_or.setMaximumSize(new Dimension(100, 30));
        button_or.setMinimumSize(new Dimension(20, 30));
        button_or.setPreferredSize(new Dimension(60, 30));
        button_or.setRequestFocusEnabled(true);
        button_or.setText("OR");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_or, gbc);
        button_lShift = new JButton();
        button_lShift.setMaximumSize(new Dimension(100, 30));
        button_lShift.setMinimumSize(new Dimension(20, 30));
        button_lShift.setPreferredSize(new Dimension(60, 30));
        button_lShift.setText("<<");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_lShift, gbc);
        button_rShift = new JButton();
        button_rShift.setMaximumSize(new Dimension(100, 30));
        button_rShift.setMinimumSize(new Dimension(20, 30));
        button_rShift.setPreferredSize(new Dimension(60, 30));
        button_rShift.setText(">>");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        operatorPanel.add(button_rShift, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        operatorPanel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        operatorPanel.add(spacer9, gbc);
        digitPanel = new JPanel();
        digitPanel.setLayout(new GridBagLayout());
        digitPanel.setPreferredSize(new Dimension(200, 162));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(digitPanel, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        digitPanel.add(spacer11, gbc);
        button3 = new JButton();
        button3.setMaximumSize(new Dimension(100, 30));
        button3.setMinimumSize(new Dimension(20, 30));
        button3.setPreferredSize(new Dimension(50, 30));
        button3.setText("3");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button3, gbc);
        button4 = new JButton();
        button4.setMaximumSize(new Dimension(100, 30));
        button4.setMinimumSize(new Dimension(20, 30));
        button4.setPreferredSize(new Dimension(50, 30));
        button4.setText("4");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button4, gbc);
        button5 = new JButton();
        button5.setMaximumSize(new Dimension(100, 30));
        button5.setMinimumSize(new Dimension(20, 30));
        button5.setPreferredSize(new Dimension(50, 30));
        button5.setText("5");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button5, gbc);
        button6 = new JButton();
        button6.setMaximumSize(new Dimension(100, 30));
        button6.setMinimumSize(new Dimension(20, 30));
        button6.setPreferredSize(new Dimension(50, 30));
        button6.setText("6");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button6, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        digitPanel.add(spacer13, gbc);
        button7 = new JButton();
        button7.setMaximumSize(new Dimension(100, 30));
        button7.setMinimumSize(new Dimension(20, 30));
        button7.setPreferredSize(new Dimension(50, 30));
        button7.setText("7");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button7, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        digitPanel.add(spacer14, gbc);
        button0 = new JButton();
        button0.setMaximumSize(new Dimension(100, 30));
        button0.setMinimumSize(new Dimension(20, 30));
        button0.setPreferredSize(new Dimension(50, 30));
        button0.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button0, gbc);
        button8 = new JButton();
        button8.setMaximumSize(new Dimension(100, 30));
        button8.setMinimumSize(new Dimension(20, 30));
        button8.setPreferredSize(new Dimension(50, 30));
        button8.setText("8");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button8, gbc);
        button9 = new JButton();
        button9.setMaximumSize(new Dimension(100, 30));
        button9.setMinimumSize(new Dimension(20, 30));
        button9.setPreferredSize(new Dimension(50, 30));
        button9.setText("9");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button9, gbc);
        button_dot = new JButton();
        button_dot.setMaximumSize(new Dimension(100, 30));
        button_dot.setMinimumSize(new Dimension(20, 30));
        button_dot.setPreferredSize(new Dimension(50, 30));
        button_dot.setText(".");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button_dot, gbc);
        button_equal = new JButton();
        button_equal.setMaximumSize(new Dimension(100, 30));
        button_equal.setMinimumSize(new Dimension(20, 30));
        button_equal.setPreferredSize(new Dimension(50, 30));
        button_equal.setText("=");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button_equal, gbc);
        button2 = new JButton();
        button2.setMaximumSize(new Dimension(100, 30));
        button2.setMinimumSize(new Dimension(20, 30));
        button2.setPreferredSize(new Dimension(50, 30));
        button2.setText("2");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button2, gbc);
        button1 = new JButton();
        button1.setMaximumSize(new Dimension(100, 30));
        button1.setMinimumSize(new Dimension(20, 30));
        button1.setPreferredSize(new Dimension(50, 30));
        button1.setText("1");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        digitPanel.add(button1, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(hexRadioButton);
        buttonGroup.add(decRadioButton);
        buttonGroup.add(octRadioButton);
        buttonGroup.add(binRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }

}
