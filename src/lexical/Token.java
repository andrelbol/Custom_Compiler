package lexical;

public class Token {
    public String lexeme;
    public TokenType type;

    public Token(String lexeme, TokenType type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public Token(){
    }
    public Token(String s){
      this.lexeme = s;
    }
}
