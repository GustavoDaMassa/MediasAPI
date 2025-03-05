package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.IRegularExpressionProcessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegularExpressionProcessorImpl implements IRegularExpressionProcessor {

    @Override
    public ArrayList<String> compileRegex(String averageMethod) {
        //^(\d+(([.,])?\d+)?)(?=[\+\-\/\*])|(?<=[\(\+\-\*\/;])(\d+(([.,])?\d+)?)(?=[\)\/\*\+\-;])|
        // (?<=[\+\-\*\/](\d+(([.,])?\d+)?)$|[\/\*\+\-\(\);]|(?<=[\/\*\+\-\)\(;])@M(\[\d+\]\()?|
        // ^@M(\[\d+\]\()?|(?<!@)\w*[A-Za-z]\w*(\[(\d+(([.,])?\d+)?)\])?
        String doubleRegex = "(\\d+(([.,])?\\d+)?)";
        String operatorsRegex = "\\+\\-\\*\\/";
        String featuresRegex = "@M";
        String coefficientsRegex = String.format("^%s(?=[%s])|(?<=[%s\\(;])%s(?=[%s\\);])|(?<=[%s])%s$",
                doubleRegex,operatorsRegex,operatorsRegex,doubleRegex,operatorsRegex,operatorsRegex,doubleRegex);
        String delimitersRegex = String.format("[%s\\(\\)\\;]|(?<=[%s\\)\\(;])%s(\\[\\d+\\]\\()?|^%s(\\[\\d+\\]\\()?",
                operatorsRegex,operatorsRegex,featuresRegex,featuresRegex);
        String identifierRegex = String.format("(?<!@)\\w*[A-Za-z]\\w*(\\[%s\\])?",doubleRegex);
        String regex = String.format("%s|%s|%s",coefficientsRegex,delimitersRegex,identifierRegex);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(averageMethod.replaceAll("\\s",""));

        ArrayList<String> methodTokens = new ArrayList<>();
        while (matcher.find()) {
            methodTokens.add(matcher.group());
        }
        if(averageMethod.replaceAll("\\s","").replaceAll(regex,"").isEmpty())
            return methodTokens;
        else {
            throw new IllegalArgumentException("Method for calculating averages not accepted, formula terms are invalid:"+
                    " "+averageMethod.replaceAll("\\s","").replaceAll(regex,"-"));
        }
    }
}
