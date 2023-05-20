package jisuanqi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;

public class Main extends JFrame implements ActionListener {
    private JTextField displayField;

    public Main() {
        setTitle("计算器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //美化
        
        displayField = new JTextField();
        displayField.setEditable(false);
        
        
        displayField.setFont(new Font("Arial", Font.PLAIN, 24)); // 设置文本框字体和大小
        displayField.setHorizontalAlignment(JTextField.RIGHT); // 文本右对齐
        displayField.setPreferredSize(new Dimension(50, 70)); // 设置文本框大小
        displayField.setBackground(Color.PINK); // 设置文本框背景色
        displayField.setForeground(Color.BLACK); // 设置文本框前景色
        
        //
        displayField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    displayField.setEditable(true);
                } else {
                    displayField.setEditable(false);
                }
            }
        });
        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4,5,5));
        add(buttonPanel, BorderLayout.CENTER);

        String[] buttonLabels = {
                "%", "CE", "C","revoke",	
                "1/x","x^2","√x","÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
        };
        
        //按钮
        Color buttonColor = new Color(255, 250,130); // 自定义按钮背景色
       // float alpha = 1.0f; // 透明度值（取值范围为0.0-1.0）
        
        
        for (int i = 0; i < buttonLabels.length; i++) {
        	String label=buttonLabels[i];
        
            JButton button = new JButton(label);
            button.addActionListener(this);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setBackground(buttonColor);
            button.setForeground(Color.BLACK);
            
            button.setOpaque(true);
            button.setBackground(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue()));

            
            // 添加鼠标事件监听器
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                   button.setBackground(buttonColor.brighter());
                }

                @Override
               public void mouseExited(MouseEvent e) {
                   button.setBackground(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue()));
                }
            });

            buttonPanel.add(button);
        

            // 设置按钮透明度
            //button.setOpaque(true); // 绘制按钮背景
            //button.setBackground(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), (int) (alpha * 255)));

           // buttonPanel.add(button);
        }
        pack();
        setLocationRelativeTo(null);
    }


        

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("=")) {
            calculate();
        } else if (command.equals("CE")) {
            clearEntry();
        } else if (command.equals("C")) {
            clear();
        } else if (command.equals("±")) {
            toggleSign();
        } else if (command.equals("x^2")) {
            square();
        } else if (command.equals("1/x")) {
            reciprocal();
        } else if (command.equals("√x")) {
            squareRoot();
        } else if (command.equals("revoke")) {
        	clearEntry();
        }else {
            displayField.setText(displayField.getText() + command);
        }
    }

    private void calculate() {
        String expression = displayField.getText();

        try {
            double result = eval(expression);
            displayField.setText(Double.toString(result));
        } catch (Exception e) {
            displayField.setText("Error");
        }
    }

    private void clearEntry() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            expression = expression.substring(0, expression.length() - 1);
            displayField.setText(expression);
        }
    }

    private void clear() {
        displayField.setText("");
    }

    private void toggleSign() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            double value = Double.parseDouble(expression);
            value *= -1;
            displayField.setText(Double.toString(value));
        }
    }

    private void square() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            double value = Double.parseDouble(expression);
            value *= value;
            displayField.setText(Double.toString(value));
        }
    }

    private void reciprocal() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            double value = Double.parseDouble(expression);
            if (value != 0) {
                value = 1 / value;
                displayField.setText(Double.toString(value));
            } else {
                displayField.setText("Error");
            }
        }
    }

    private void squareRoot() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            double value = Double.parseDouble(expression);
            if (value >= 0) {
                value = Math.sqrt(value);
                displayField.setText(Double.toString(value));
            } else {
                displayField.setText("Error");
            }
        }
    }
  

    private double eval(final String str) {
    	    return new Object() {
    	        int pos = -1, ch;

    	        void nextChar() {
    	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    	        }

    	        boolean eat(int charToEat) {
    	            while (ch == ' ') nextChar();
    	            if (ch == charToEat) {
    	                nextChar();
    	                return true;
    	            }
    	            return false;
    	        }

    	        double parse() {
    	            nextChar();
    	            double x = parseExpression();
    	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
    	            return x;
    	        }

    	        double parseExpression() {
    	            double x = parseTerm();
    	            for (; ; ) {
    	                if (eat('+')) x += parseTerm(); // +
    	                else if (eat('-')) x -= parseTerm(); // -
    	                else return x;
    	            }
    	        }

    	        double parseTerm() {
    	            double x = parseFactor();
    	            for (; ; ) {
    	                if (eat('×')) x *= parseFactor(); // *
    	                else if (eat('÷')) x /= parseFactor(); // /
    	                else return x;
    	            }
    	        }

    	        double parseFactor() {
    	            if (eat('+')) return parseFactor(); // 正
    	            if (eat('-')) return -parseFactor(); // 负

    	            double x;
    	            int startPos = this.pos;
    	            if (eat('(')) { //
    	                x = parseExpression();
    	                eat(')');
    	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { 
    	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
    	                x = Double.parseDouble(str.substring(startPos, this.pos));
    	            } else {
    	                throw new RuntimeException("Unexpected: " + (char) ch);
    	            }

    	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

    	            return x;
    	        }
    	    }.parse();
    	}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main calculator = new Main();
                calculator.setVisible(true);
            }
        });
    }
    private void appendToDisplayField(String text) {
        displayField.setText(displayField.getText() + text);
    }

    private void handleKeyboardInput(char keyChar) {
        if (Character.isDigit(keyChar) || keyChar == '.') {
            appendToDisplayField(Character.toString(keyChar));
        } else if (keyChar == '+' || keyChar == '-' || keyChar == '*' || keyChar == '÷') {
            appendToDisplayField(Character.toString(keyChar));
        } else if (keyChar == '=' || keyChar == KeyEvent.VK_ENTER) {
            calculate();
        } else if (keyChar == KeyEvent.VK_BACK_SPACE) {
            clearEntry();
        }
    }

    
    private void clearEntry2() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            expression = expression.substring(0, expression.length() - 1);
            displayField.setText(expression);
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        char keyChar = e.getKeyChar();
        handleKeyboardInput(keyChar);
    }

    private void calculate2() {
        String expression = displayField.getText();

        try {
            double result = eval(expression);
            displayField.setText(Double.toString(result));
        } catch (Exception e) {
            displayField.setText("Error");
        }
    }
 

}

