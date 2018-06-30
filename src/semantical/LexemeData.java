package compilador;

import lexical.TokenType;

public class LexemeData {
    private int offset;
    private TokenType tokenType;
    private DataType dataType;
    
    public LexemeData(int i, DataType dataType, TokenType tokenType) {
        this.offset = i;
        this.tokenType = tokenType;
        this.dataType = dataType; 
    }

    public int getOffset() {
        return offset;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public DataType getDataType() {
        return dataType;
    }
    
}
