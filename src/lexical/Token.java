package lexical;

class Token {
    public String value;
    public TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }
}