package lexical;

import java.util.*;

public class SymbolTable {
    private HashMap<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<String, TokenType>();

        st.put("program", TokenType.PROGRAM);
        st.put("declare", TokenType.DECLARE);
        st.put("begin", TokenType.BEGIN);
        st.put("end", TokenType.END);
        st.put("int", TokenType.INT);
        st.put("float", TokenType.FLOAT);
        st.put("char", TokenType.CHAR);
        st.put("if", TokenType.IF);
        st.put("then", TokenType.THEN);
        st.put("else", TokenType.ELSE);
        st.put("repeat", TokenType.REPEAT);
        st.put("until", TokenType.UNTIL);
        st.put("while", TokenType.WHILE);
        st.put("do", TokenType.DO);
        st.put("in", TokenType.IN);
        st.put("out", TokenType.OUT);

        st.put(";", TokenType.SEMI_COLON);
        st.put(":", TokenType.COLON);
        st.put("(", TokenType.PAR_OPEN);
        st.put(")", TokenType.PAR_CLOSE);
        st.put(",", TokenType.COMMA);

        st.put("=", TokenType.ASSIGN);
        st.put("!", TokenType.NEGATION);
        st.put("==", TokenType.EQUALS);
        st.put(">", TokenType.GREATER_THAN);
        st.put(">=", TokenType.GREATER_THAN_EQUAL);
        st.put("<", TokenType.LESS_THAN);
        st.put("<=", TokenType.LESS_THAN_EQUAL);
        st.put("!=", TokenType.NOT_EQUAL);
        st.put("+", TokenType.ADD);
        st.put("-", TokenType.MINUS);
        st.put("||", TokenType.OR);
        st.put("*", TokenType.TIMES);
        st.put("/", TokenType.DIV);
        st.put("&&", TokenType.AND);
        st.put("ÿ", TokenType.END_OF_FILE);
    }

    public boolean contains(String lexeme) {
        return st.containsKey(lexeme);
    }

    public TokenType find(String lexeme) {
        return this.contains(lexeme) ? st.get(lexeme) : TokenType.INVALID_TOKEN;
    }
}
