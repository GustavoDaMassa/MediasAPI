package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IRegularExpressionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

@Service
public class ConvertToPolishNotationReverseImpl implements IConvertToPolishNotation {

    private final IRegularExpressionProcessor regularExpressionProcessor;
    @Autowired
    public ConvertToPolishNotationReverseImpl(IRegularExpressionProcessor regularExpressionProcessor) {
        this.regularExpressionProcessor = regularExpressionProcessor;
    }

    @Override
    public ArrayList<String> convertToPolishNotation(String averageMethod) {

        ArrayList<String> methodTokens = regularExpressionProcessor.compileRegex(averageMethod);
        Deque<String> stack = new ArrayDeque<>();
        ArrayList<String> polishNotation = new ArrayList<>();
        for (int i = 0; i < methodTokens.size() ; i++) {
            String token = methodTokens.get(i);
            if(stack.isEmpty()&&token.matches("[*+/\\-]|@M(\\[\\d+]\\()?"))stack.push(token);// empty or token is a delimiter
            else{
                if (token.matches("[+\\-]")) {
                    while (!stack.isEmpty()) {
                        if (stack.peek().matches(";|\\(|@M(\\[\\d+]\\()?"))break;
                        polishNotation.add(stack.pop());
                    }
                    stack.push(token);
                }
                else if (token.matches("[*/]")) {
                    while (stack.peek().matches("[*/]"))
                        polishNotation.add(stack.pop());
                    stack.push(token);
                }
                else if (token.matches(";|\\(|@M(\\[\\d+]\\()?")) {
                    stack.push(token);
                }
                else if (token.matches("\\)")) {
                    while (!stack.peek().matches("\\(|@M(\\[\\d+]\\()?"))
                        polishNotation.add(stack.pop());
                    if(stack.peek().matches("\\("))stack.pop();
                    else if(stack.peek().matches("@M(\\[\\d+]\\()?"))polishNotation.add(stack.pop());
                }
                else {
                    polishNotation.add(token);
                }
            }
        }
        while (!stack.isEmpty()){
            if(stack.peek().matches("\\("))stack.pop();
            else polishNotation.add(stack.pop());
        }
        return polishNotation;
    }
}
