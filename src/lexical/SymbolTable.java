package lexical;

import compilador.DataType;
import compilador.LexemeData;
import java.util.*;

public class SymbolTable {
    public HashMap<String, LexemeData> st;

    public SymbolTable() {
        st = new HashMap<String, LexemeData>();
        st.put("program", new LexemeData(0,DataType.NA,TokenType.PROGRAM));
        st.put("declare", new LexemeData(0,DataType.NA,TokenType.DECLARE));
        st.put("begin", new LexemeData(0,DataType.NA,TokenType.BEGIN));
        st.put("end", new LexemeData(0,DataType.NA,TokenType.END));
        st.put("int", new LexemeData(0,DataType.NA,TokenType.INT));
        st.put("float", new LexemeData(0,DataType.NA,TokenType.FLOAT));
        st.put("char", new LexemeData(0,DataType.NA,TokenType.CHAR));
        st.put("if", new LexemeData(0,DataType.NA,TokenType.IF));
        st.put("then", new LexemeData(0,DataType.NA,TokenType.THEN));
        st.put("else", new LexemeData(0,DataType.NA,TokenType.ELSE));
        st.put("repeat", new LexemeData(0,DataType.NA,TokenType.REPEAT));
        st.put("until", new LexemeData(0,DataType.NA,TokenType.UNTIL));
        st.put("while", new LexemeData(0,DataType.NA,TokenType.WHILE));
        st.put("do", new LexemeData(0,DataType.NA,TokenType.DO));
        st.put("in", new LexemeData(0,DataType.NA,TokenType.IN));
        st.put("out", new LexemeData(0,DataType.NA,TokenType.OUT));

        st.put(";", new LexemeData(0,DataType.NA,TokenType.SEMI_COLON));
        st.put(":", new LexemeData(0,DataType.NA,TokenType.COLON));
        st.put("(", new LexemeData(0,DataType.NA,TokenType.PAR_OPEN));
        st.put(")", new LexemeData(0,DataType.NA,TokenType.PAR_CLOSE));
        st.put(",", new LexemeData(0,DataType.NA,TokenType.COMMA));

        st.put("=", new LexemeData(0,DataType.NA,TokenType.ASSIGN));
        st.put("!", new LexemeData(0,DataType.NA,TokenType.NEGATION));
        st.put("==", new LexemeData(0,DataType.NA,TokenType.EQUALS));
        st.put(">", new LexemeData(0,DataType.NA,TokenType.GREATER_THAN));
        st.put(">=", new LexemeData(0,DataType.NA,TokenType.GREATER_THAN_EQUAL));
        st.put("<", new LexemeData(0,DataType.NA,TokenType.LESS_THAN));
        st.put("<=", new LexemeData(0,DataType.NA,TokenType.LESS_THAN_EQUAL));
        st.put("!=", new LexemeData(0,DataType.NA,TokenType.NOT_EQUAL));
        st.put("+", new LexemeData(0,DataType.NA,TokenType.ADD));
        st.put("-", new LexemeData(0,DataType.NA,TokenType.MINUS));
        st.put("||", new LexemeData(0,DataType.NA,TokenType.OR));
        st.put("*", new LexemeData(0,DataType.NA,TokenType.TIMES));
        st.put("/", new LexemeData(0,DataType.NA,TokenType.DIV));
        st.put("&&", new LexemeData(0,DataType.NA,TokenType.AND));
        st.put("ÿ", new LexemeData(0,DataType.NA,TokenType.END_OF_FILE));
    }

    public boolean contains(String lexeme) {
        return st.containsKey(lexeme);
    }

    public TokenType find(String lexeme) {
        return this.contains(lexeme) ? st.get(lexeme).getTokenType() : TokenType.INVALID_TOKEN;
    }

    public String toString() {
      String result = "\nTabela de Símbolos:\n\n";
      Iterator it = this.st.entrySet().iterator();
      while(it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        result += ("<" + pair.getKey() + ", " + pair.getValue() + ">\n");
        it.remove();
      }
      return result;
    }
}
