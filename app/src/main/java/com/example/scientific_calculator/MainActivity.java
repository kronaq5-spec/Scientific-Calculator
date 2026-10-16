
package com.example.scientific_calculator;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.ColorStateList;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView expressionText, resultText;
    private String expression = "";
    private boolean calculated = false;

    private final int bg = Color.rgb(13, 16, 28);
    private final int displayBg = Color.rgb(24, 29, 46);
    private final int numberBg = Color.rgb(34, 41, 61);
    private final int functionBg = Color.rgb(48, 56, 82);
    private final int purple = Color.rgb(137, 94, 255);
    private final int white = Color.WHITE;
    private final int muted = Color.rgb(157, 168, 193);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(0);
        createCalculator();
    }

    private void createCalculator() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(8), dp(16), dp(10));
        root.setBackgroundColor(bg);

        // Header
        TextView title = new TextView(this);
        title.setText("SCIENTIFIC CALCULATOR");
        title.setTextColor(muted);
        title.setTextSize(13);
        title.setTypeface(null, Typeface.BOLD);
        title.setLetterSpacing(0.12f);
        title.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(42)));

        // Large mobile display
        LinearLayout display = new LinearLayout(this);
        display.setOrientation(LinearLayout.VERTICAL);
        display.setGravity(Gravity.BOTTOM);
        display.setPadding(dp(18), dp(16), dp(18), dp(16));
        display.setBackground(round(displayBg, 24));

        LinearLayout.LayoutParams displayParams =
                new LinearLayout.LayoutParams(-1, 0, 1.25f);
        displayParams.setMargins(0, dp(5), 0, dp(16));
        root.addView(display, displayParams);

        TextView displayLabel = new TextView(this);
        displayLabel.setText("YOUR CALCULATION");
        displayLabel.setTextColor(muted);
        displayLabel.setTextSize(11);
        displayLabel.setLetterSpacing(0.1f);
        display.addView(displayLabel);

        expressionText = new TextView(this);
        expressionText.setText("");
        expressionText.setTextColor(muted);
        expressionText.setTextSize(20);
        expressionText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        expressionText.setMaxLines(2);

        LinearLayout.LayoutParams expressionParams =
                new LinearLayout.LayoutParams(-1, 0, 1);
        expressionParams.topMargin = dp(8);
        display.addView(expressionText, expressionParams);

        resultText = new TextView(this);
        resultText.setText("0");
        resultText.setTextColor(white);
        resultText.setTextSize(43);
        resultText.setTypeface(null, Typeface.BOLD);
        resultText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        resultText.setSingleLine(true);
        resultText.setHorizontallyScrolling(true);
        display.addView(resultText,
                new LinearLayout.LayoutParams(-1, dp(70)));

        // Scientific keys
        addRow(root, new String[]{"sin", "cos", "tan", "(", ")"}, 0);
        addRow(root, new String[]{"log", "ln", "√", "^", "π"}, 0);

        // Editing and arithmetic keys
        addRow(root, new String[]{"AC", "DEL", "%", "÷", "×"}, 0);
        addRow(root, new String[]{"7", "8", "9", "−", "+"}, 0);
        addRow(root, new String[]{"4", "5", "6", "1/x", "±"}, 0);
        addRow(root, new String[]{"1", "2", "3", "Ans", "0"}, 0);

        // Bottom row: exactly ONE equals button
        addRow(root, new String[]{"00", "0", ".", "=", ""}, 1);

        setContentView(root);
    }

    private void addRow(LinearLayout root, String[] keys, int bottomRow) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(-1, 0, 0.78f);
        rowParams.setMargins(0, dp(3), 0, dp(3));
        root.addView(row, rowParams);

        for (String key : keys) {
            if (key.isEmpty()) {
                View spacer = new View(this);
                row.addView(spacer,
                        new LinearLayout.LayoutParams(0, 1, 1));
                continue;
            }

            Button button = new Button(this);
            button.setText(key);
            button.setAllCaps(false);
            button.setTextSize(key.length() > 3 ? 13 : 18);
            button.setTextColor(white);
            button.setTypeface(null,
                    key.equals("=") ? Typeface.BOLD : Typeface.NORMAL);
            button.setMinWidth(0);
            button.setMinimumWidth(0);
            button.setMinHeight(0);
            button.setMinimumHeight(0);
            button.setPadding(dp(1), 0, dp(1), 0);
            button.setStateListAnimator(null);

            int color = getButtonColor(key);
            button.setBackground(round(color, 17));

            if (key.equals("=")) {
                button.setTextSize(23);
            }

            button.setOnClickListener(v -> onKey(key));

            LinearLayout.LayoutParams buttonParams =
                    new LinearLayout.LayoutParams(0, -1, 1);
            buttonParams.setMargins(dp(3), 0, dp(3), 0);
            row.addView(button, buttonParams);
        }
    }

    private int getButtonColor(String key) {
        if (key.equals("=")) return purple;
        if (key.equals("AC") || key.equals("DEL"))
            return Color.rgb(117, 49, 72);

        if (key.matches("[0-9]+") || key.equals("."))
            return numberBg;

        if (key.equals("+") || key.equals("−") ||
                key.equals("×") || key.equals("÷") ||
                key.equals("^"))
            return Color.rgb(65, 70, 108);

        return functionBg;
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(radius));
        return shape;
    }

    private void onKey(String key) {
        if (key.equals("AC")) {
            expression = "";
            expressionText.setText("");
            resultText.setText("0");
            calculated = false;
            return;
        }

        if (key.equals("DEL")) {
            if (!expression.isEmpty()) {
                expression = expression.substring(0,
                        expression.length() - 1);
            }
            expressionText.setText(expression);
            calculated = false;
            return;
        }

        if (key.equals("=")) {
            if (expression.isEmpty()) return;

            try {
                double result = new CalculatorParser(expression).parse();

                if (!Double.isFinite(result)) {
                    throw new IllegalArgumentException();
                }

                resultText.setText(format(result));
                expressionText.setText(expression + " =");
                expression = format(result);
                calculated = true;

            } catch (Exception e) {
                resultText.setText("Error");
                Toast.makeText(this,
                        "Invalid calculation. Check your input.",
                        Toast.LENGTH_SHORT).show();
                calculated = false;
            }
            return;
        }

        if (key.equals("±")) {
            if (expression.isEmpty()) {
                expression = "-";
            } else if (expression.startsWith("-")) {
                expression = expression.substring(1);
            } else {
                expression = "-(" + expression + ")";
            }
            calculated = false;

        } else if (key.equals("Ans")) {
            if (calculated) {
                expression = resultText.getText().toString();
            } else if (expression.isEmpty()) {
                expression = resultText.getText().toString();
            } else {
                expression += resultText.getText().toString();
            }
            calculated = false;

        } else if (key.equals("sin") || key.equals("cos")
                || key.equals("tan") || key.equals("log")
                || key.equals("ln")) {
            startNewCalculation();
            expression += key + "(";

        } else if (key.equals("√")) {
            startNewCalculation();
            expression += "sqrt(";

        } else if (key.equals("π")) {
            startNewCalculation();
            expression += "pi";

        } else if (key.equals("1/x")) {
            startNewCalculation();
            expression = "1/(" + expression + ")";

        } else {
            if (calculated &&
                    (key.matches("[0-9]+") || key.equals("00")
                            || key.equals("."))) {
                expression = "";
            }

            calculated = false;

            switch (key) {
                case "×": expression += "*"; break;
                case "÷": expression += "/"; break;
                case "−": expression += "-"; break;
                default: expression += key;
            }
        }

        expressionText.setText(expression);
    }

    private void startNewCalculation() {
        if (calculated) expression = "";
        calculated = false;
    }

    private String format(double value) {
        if (Math.abs(value) < 1e-12) value = 0;

        DecimalFormat df = new DecimalFormat(
                "0.##########",
                new DecimalFormatSymbols(Locale.US));
        df.setGroupingUsed(false);
        return df.format(value);
    }

    private int dp(float value) {
        return (int) (value *
                getResources().getDisplayMetrics().density + 0.5f);
    }

    // Expression parser
    private static class CalculatorParser {
        private final String input;
        private int pos = 0;

        CalculatorParser(String input) {
            this.input = input.replace(" ", "");
        }

        double parse() {
            double result = expression();
            if (pos != input.length()) {
                throw new IllegalArgumentException();
            }
            return result;
        }

        private boolean eat(char c) {
            if (pos < input.length() && input.charAt(pos) == c) {
                pos++;
                return true;
            }
            return false;
        }

        private double expression() {
            double value = term();

            while (true) {
                if (eat('+')) value += term();
                else if (eat('-')) value -= term();
                else return value;
            }
        }

        private double term() {
            double value = power();

            while (true) {
                if (eat('*')) value *= power();
                else if (eat('/')) {
                    double divisor = power();
                    if (divisor == 0) {
                        throw new IllegalArgumentException();
                    }
                    value /= divisor;
                } else if (eat('%')) {
                    value %= power();
                } else {
                    return value;
                }
            }
        }

        private double power() {
            double value = unary();
            if (eat('^')) {
                value = Math.pow(value, power());
            }
            return value;
        }

        private double unary() {
            if (eat('+')) return unary();
            if (eat('-')) return -unary();
            return atom();
        }

        private double atom() {
            if (eat('(')) {
                double value = expression();
                if (!eat(')')) throw new IllegalArgumentException();
                return value;
            }

            if (pos < input.length() &&
                    (Character.isDigit(input.charAt(pos))
                            || input.charAt(pos) == '.')) {
                int start = pos;

                while (pos < input.length() &&
                        (Character.isDigit(input.charAt(pos))
                                || input.charAt(pos) == '.')) {
                    pos++;
                }

                return Double.parseDouble(input.substring(start, pos));
            }

            if (pos < input.length() &&
                    Character.isLetter(input.charAt(pos))) {
                int start = pos;

                while (pos < input.length() &&
                        Character.isLetter(input.charAt(pos))) {
                    pos++;
                }

                String name = input.substring(start, pos);

                if (name.equals("pi")) return Math.PI;

                double argument;

                if (eat('(')) {
                    argument = expression();
                    if (!eat(')')) throw new IllegalArgumentException();
                } else {
                    argument = unary();
                }

                switch (name) {
                    case "sin":
                        return Math.sin(Math.toRadians(argument));

                    case "cos":
                        return Math.cos(Math.toRadians(argument));

                    case "tan":
                        if (Math.abs(Math.cos(
                                Math.toRadians(argument))) < 1e-12) {
                            throw new IllegalArgumentException();
                        }
                        return Math.tan(Math.toRadians(argument));

                    case "log":
                        if (argument <= 0) throw new IllegalArgumentException();
                        return Math.log10(argument);

                    case "ln":
                        if (argument <= 0) throw new IllegalArgumentException();
                        return Math.log(argument);

                    case "sqrt":
                        if (argument < 0) throw new IllegalArgumentException();
                        return Math.sqrt(argument);

                    default:
                        throw new IllegalArgumentException();
                }
            }

            throw new IllegalArgumentException();
        }
    }
}
