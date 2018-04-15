package lexical;

class Token {
    public String lexeme;
    public TokenType type;

    public Token(String lexeme, TokenType type) {
        this.lexeme = lexeme;
        this.type = type;
    }
}