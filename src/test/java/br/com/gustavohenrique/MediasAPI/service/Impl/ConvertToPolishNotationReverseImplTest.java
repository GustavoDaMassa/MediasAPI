package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.service.Interfaces.IRegularExpressionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ConvertToPolishNotationReverseImplTest {

    @Autowired
    @InjectMocks
    private ConvertToPolishNotationReverseImpl convertToPolishNotationReverse;

    @Mock
    private IRegularExpressionProcessor regularExpressionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("ConvertPolishNotation - convert the token of the average method to the pos-fixed precedence order")
    void convertToPolishNotationSuccessfully() {
        var methodTokens = new ArrayList<>(List.of("p1", "+", "p2"));
        var polishNotation = new ArrayList<>(List.of("p1", "p2", "+"));
        var averageMethod = "p1+p2";

        when(regularExpressionProcessor.compileRegex(averageMethod)).thenReturn(methodTokens);

        var response = convertToPolishNotationReverse.convertToPolishNotation(averageMethod);

        assertEquals(polishNotation.size(),response.size());
        assertEquals(polishNotation.get(0),response.get(0));
        assertEquals(polishNotation.get(1),response.get(1));
        assertEquals(polishNotation.get(2),response.get(2));
    }

    @Test
    @DisplayName("ConvertPolishNotation - convert to pos-fixed precedence order with functionality extra")
    void convertToPolishNotationFunctionality() {
        var methodTokens = new ArrayList<>(List.of("@M[2](", "p1", ";", "p2", ")"));
        var polishNotation = new ArrayList<>(List.of("p1", "p2", ";", "@M[2]("));
        var averageMethod = "@M[2](p1;p2)";

        when(regularExpressionProcessor.compileRegex(averageMethod)).thenReturn(methodTokens);

        var response = convertToPolishNotationReverse.convertToPolishNotation(averageMethod);

        assertEquals(polishNotation.size(),response.size());
        assertEquals(polishNotation.get(0),response.get(0));
        assertEquals(polishNotation.get(1),response.get(1));
        assertEquals(polishNotation.get(2),response.get(2));
    }
}