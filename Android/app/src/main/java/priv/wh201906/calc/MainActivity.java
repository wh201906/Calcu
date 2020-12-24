package priv.wh201906.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity
{

    Button.OnClickListener contentOnClickListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            inputView.getText().insert(inputView.getSelectionStart(),((Button) view).getText());
        }
    };

    EditText inputView;
    TextView outputView;

    ScriptEngine jsEngine=new ScriptEngineManager().getEngineByName("js");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputView = (EditText) findViewById(R.id.inputView);
        outputView = (TextView) findViewById(R.id.outputView);

        findViewById(R.id.Button_0).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_1).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_2).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_3).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_4).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_5).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_6).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_7).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_8).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_9).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_dot).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_plus).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_minus).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_multiply).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_divide).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_Lbrace).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_Rbrace).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_pow).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_mod).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_Lshift).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_Rshift).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_and).setOnClickListener(contentOnClickListener);
        findViewById(R.id.Button_or).setOnClickListener(contentOnClickListener);

        findViewById(R.id.Button_clear).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                inputView.setText("");
                outputView.setText("");
            }
        });

        findViewById(R.id.Button_backspace).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int index=inputView.getSelectionStart();
                if(index>0)
                {
                    inputView.getText().delete(index-1,index);
                }
            }
        });

        findViewById(R.id.Button_equal).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String input=inputView.getText().toString().trim();
                if (input.isEmpty())
                {
                    return;
                }
                if(((RadioButton)findViewById(R.id.useEvalButton)).isChecked())
                {
                    try
                    {
                        input=input.replace("÷","/");
                        input=input.replace("×","*");
                        outputView.setText(jsEngine.eval(input).toString());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        outputView.setText("Error!");
                    }
                    return;
                }
                boolean isFailed = false;
                ArrayList<String> res1;
                ArrayList<String> res2;
                String res3 = "";
                try
                {
                    res1 = split(input);
                    res2 = infix2suffix(res1);
                    res3 = computeSuffix(res2);
                } catch (Exception e)
                {
                    isFailed = true;
                }
                if (isFailed)
                {
                    outputView.setText("Error!");
                }
                else
                {
                    outputView.setText(res3);
                }
            }
        });

        initSymbolList();

    }

    private ArrayList<String> symbolList = new ArrayList<String>(Arrays.asList("(", ")"));
    private ArrayList<ArrayList<String>> operatorList = new ArrayList<>();

    private void initSymbolList()
    {
        operatorList.add(new ArrayList<String>(Collections.singletonList("^")));
        operatorList.add(new ArrayList<String>(Arrays.asList("×", "÷", "%"))); // 运算符顺序列表可调
        operatorList.add(new ArrayList<String>(Arrays.asList("+", "-")));
        operatorList.add(new ArrayList<String>(Arrays.asList("<<", ">>")));
        operatorList.add(new ArrayList<String>(Collections.singletonList("&")));
        operatorList.add(new ArrayList<String>(Collections.singletonList("|")));
        for (ArrayList<String> list : operatorList)
        {
            symbolList.addAll(list);
        }
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
        else if (operator.equals("×"))
        {
            result = a_val * b_val;
        }
        else if (operator.equals("÷"))
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
}
