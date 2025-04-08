package br.com.gustavohenrique.MediasAPI.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionProcessorImplTest {

    @Autowired
    @InjectMocks
    private RegularExpressionProcessorImpl regularExpressionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("compileRegex - Should return the string method segregated in tokens")
    void compileRegexSuccessfully() {
        var averageMethod = "(P1 + P2 + P3)/3+@M[2](P4)";
        var methodTokens = new ArrayList<>(List.of("(", "P1", "+", "P2", "+", "P3", ")", "/", "3", "+", "@M[2](", "P4", ")"));

        var response = regularExpressionProcessor.compileRegex(averageMethod);

        assertEquals(methodTokens.size(),response.size());
        assertEquals(methodTokens.get(0),response.get(0));
        assertEquals(methodTokens.get(1),response.get(1));
        assertEquals(methodTokens.get(2),response.get(2));
        assertEquals(methodTokens.get(3),response.get(3));
        assertEquals(methodTokens.get(4),response.get(4));
        assertEquals(methodTokens.get(5),response.get(5));
        assertEquals(methodTokens.get(6),response.get(6));
        assertEquals(methodTokens.get(7),response.get(7));
        assertEquals(methodTokens.get(8),response.get(8));
    }

    @Test
    @DisplayName("compileRegex - Should return exception variables with out operators")
    void compileRegexWithOutOperators() {
        var averageMethod = "(P1 + P2 + P3)3";
        var methodTokens = new ArrayList<>(List.of("(", "P1", "+", "P2", "+", "P3", ")", "3"));

        assertThrows(IllegalArgumentException.class, () -> regularExpressionProcessor.compileRegex(averageMethod));
    }

    @Test
    @DisplayName("compileRegex - Should return exception method with invalid symbols")
    void compileRegexInvalidSymbols() {
        var averageMethod = "Ç+P3*@M[2](P4)";
        var methodTokens = new ArrayList<>(List.of("Ç", "+", "P3", "*", "@M[2](", "P4", ")"));

        assertThrows(IllegalArgumentException.class, () -> regularExpressionProcessor.compileRegex(averageMethod));
    }
}