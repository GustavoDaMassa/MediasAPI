package br.com.gustavohenrique.MediasAPI.service;

public class FormulaTokens {

    public static final String FORMULA_IDENTIFIER_REGEX = "(?<!@)\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?";

    private static final String TOKEN_IDENTIFIER = "\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?";
    private static final String NUMBER = "(\\d+(([.,])?\\d+)?)";
    private static final String OPERATOR = "[+*\\-/]";
    private static final String FUNCTION = "@M(\\[\\d+]\\()?";
    private static final String CLEAN_BRACKETS = "(\\[(\\d+(([.,])?\\d+)?)])?";

    public static boolean isIdentifier(String token) {
        return token.matches(TOKEN_IDENTIFIER);
    }

    public static boolean isNumber(String token) {
        return token.matches(NUMBER);
    }

    public static boolean isOperator(String token) {
        return token.matches(OPERATOR);
    }

    public static boolean isFunction(String token) {
        return token.matches(FUNCTION);
    }

    public static String cleanBrackets(String token) {
        return token.replaceAll(CLEAN_BRACKETS, "");
    }
}
