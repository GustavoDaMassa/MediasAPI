package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.IdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RpnEvaluator {

    public double evaluate(List<String> tokens, Long projectionId, IdentifierResolver resolver) {
        Deque<Double> stack = new ArrayDeque<>();
        List<Double> values = new ArrayList<>();

        for (String token : tokens) {
            if (FormulaTokens.isNumber(token)) {
                stack.push(Double.parseDouble(token.replace(",", ".")));

            } else if (FormulaTokens.isIdentifier(token)) {
                stack.push(resolver.resolve(FormulaTokens.cleanBrackets(token), projectionId));

            } else if (FormulaTokens.isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/":
                        if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
                        stack.push(a / b);
                        break;
                }

            } else if (FormulaTokens.isFunction(token)) {
                values.add(stack.pop());
                Matcher m = Pattern.compile("(?<=\\[)\\d+").matcher(token.replaceAll("\\s", ""));
                int n = m.find() ? Integer.parseInt(m.group()) : 1;
                if (n > values.size())
                    throw new IllegalArgumentException("It is not possible to select more values than those provided");
                double result = 0;
                for (int j = 0; j < n; j++) {
                    result += Collections.max(values);
                    values.remove(Collections.max(values));
                }
                stack.push(result);

            } else {
                values.add(stack.pop());
            }
        }
        return stack.getFirst();
    }
}
