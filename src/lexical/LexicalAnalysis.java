package lexical;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class LexicalAnalysis implements AutoCloseable {
    private int line;
    private PushbackInputStream input;
    private SymbolTable symbolTable;

    private final int INITIAL_STATE = 1;
    private final int FINAL_STATE = 13;

    public LexicalAnalysis(String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch(Exception e) {
            throw new LexicalException("Não foi possível abrir o arquivo: " + filename);
        }

        symbolTable = new SymbolTable();
        line = 1;
    }

    public void close() throws LexicalException {
        try {
            input.close();
        } catch(Exception e) {
            throw new LexicalException("Can't close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean delimiters(int ch){
      return ch == ' '
        || ch == '\n'
        || ch == '\r'
        || ch == '\t'
        || ch == ';'
        || ch == '*'
        || ch == '+'
        || ch == '-'
        || ch == '('
        || ch == ')'
        || ch == '/';
    }

    public Token getToken() throws IOException, LexicalException {
        Token token = new Token("", TokenType.END_OF_FILE);

        int state = INITIAL_STATE;
        int ch;

        while(state != FINAL_STATE) {
            ch = input.read();
            switch (state) {
                case INITIAL_STATE:
                    switch (ch) {
                        // EOF
                        case -1:
                            return token;
                        // Separators
                        case ' ':
                        case '\n':
                        case '\r':
                        case '\t':
                            if(ch == '\n')
                                line++;
                            state = INITIAL_STATE;
                            break;
                        // String literal
                        case '\"':
                            state = 2;
                            break;
                        // Symbols
                        case ';':
                        case ':':
                        case '(':
                        case ')':
                        case ',':
                        case '+':
                        case '-':
                        case '*':
                            token.lexeme += (char) ch;
                            state = FINAL_STATE;
                            break;
                        // Operators
                        case '=':
                        case '!':
                        case '>':
                        case '<':
                            token.lexeme += (char) ch;
                            state = 4;
                            break;
                        // AND
                        case '&':
                            token.lexeme += (char) ch;
                            state = 5;
                            break;
                        // OR
                        case '|':
                            token.lexeme += (char) ch;
                            state = 6;
                            break;
                        // Division or Comment
                        case '/':
                            token.lexeme += (char) ch;
                            state = 10;
                            break;
                        // Char literal
                        case '\'':
                            state = 11;
                            break;
                        default:
                            if(Character.isLetter(ch)) { // Identifier
                                token.lexeme += (char) ch;
                                state = 3;
                            } else if(Character.isDigit(ch)) {
                                token.lexeme += (char) ch;
                                state = 7;
                            } else {
                                token.lexeme += (char) ch;
                                token.type = TokenType.INVALID_TOKEN;
                                throw new LexicalException("erross na linha "+token.lexeme, line);


                            }
                            break;
                    }
                    break;
                case 2: // String
                    switch(ch) {
                        case '\"':
                            token.type = TokenType.STRING;
                            return token;
                        case '\n':
                            token.type = TokenType.UNEXPECTED_END_OF_STRING;
                            throw new LexicalException("Erro string ", line);
                        case -1:
                            token.lexeme += (char) ch;
                            token.type = TokenType.UNEXPECTED_EOF;
                            throw new LexicalException("Erro string ", line);
                        default:
                            token.lexeme += (char) ch;
                            state = 2;
                    }
                    break;
                case 3: // Identifier
                    if(Character.isLetterOrDigit(ch) || ch == '_') {
                        token.lexeme += (char) ch;
                        state = 3;
                    } else {
                        input.unread(ch);
                        state = FINAL_STATE;
                    }
                    break;
                case 4: // Operators
                    if(ch == '=') {
                        token.lexeme += (char) ch;
                    } else {
                        input.unread(ch);
                    }
                    state = FINAL_STATE;
                    break;
                case 5: // AND
                    if(ch == '&') {
                        token.lexeme += (char) ch;
                    } else {
                        input.unread(ch);
                    }
                    state = FINAL_STATE;
                    break;
                case 6: // OR
                    if(ch == '|') {
                        token.lexeme += (char) ch;
                    } else {
                        input.unread(ch);
                    }
                    state = FINAL_STATE;
                    break;
                case 7: // Number Literal
                    if(Character.isDigit(ch)) {
                        token.lexeme += (char) ch;
                        state = 7;
                    } else if(ch == '.') {
                        //System.out.println(" to no .");
                        token.lexeme += (char) ch;
                        state = 8;
                    }else if(delimiters(ch)){
                        input.unread(ch);
                        token.type = TokenType.INTEGER_CONST;
                        state=FINAL_STATE;
                        return token;
                    } else {
                        input.unread(ch);
                        throw new LexicalException("Erro de digito", line);
                    }
                    break;
                case 8: // Float literal
                  //System.out.println((char)ch+" sdafkjsdfnk");
                    if(Character.isDigit(ch)) {
                        token.lexeme += (char) ch;
                        state = 31;
                      }else{
                        input.unread(ch);
                        throw new LexicalException("Erro de float", line);
                      }
                    break;
                case 31:
                  if(Character.isDigit(ch)){
                    token.lexeme += (char)ch;
                    state = 31;
                  }else{
                    token.type = TokenType.FLOAT_CONST;
                    state = FINAL_STATE;
                  }
                break;
                case 9: // Comment 1
                    if(ch == '*') {
                        state = 14;
                    } else {
                        state = 9;
                    }
                    break;
                case 10: // Division
                    if(ch == '*') {
                        state = 9;
                    } else {
                        input.unread(ch);
                        state = FINAL_STATE;
                    }
                    break;
                case 11: // Character
                    if(charIsASCII(ch)) {
                        token.lexeme += (char) ch;
                        state = 12;
                    } else {
                        token.type = TokenType.INVALID_TOKEN;
                        return token;
                    }
                    break;
                case 12:
                    if(ch == '\'') {
                        token.type = TokenType.CHARACTER;
                        return token;
                    } else {
                        token.type = TokenType.INVALID_TOKEN;
                        return token;
                    }
                case 14: // Comment 2
                    if(ch == '/'){
                        state = INITIAL_STATE; // End of comment
                    } else if(ch == '*'){
                        state = 14;
                    } else {
                        state = 9;
                    }
                    break;
                default:
                    token.type = TokenType.INVALID_TOKEN;
                    return token;
            }
        }

        if(symbolTable.contains(token.lexeme))
            token.type = symbolTable.find(token.lexeme);
        else
            token.type = TokenType.IDENTIFIER;
            //System.out.println(token.lexeme + " : " + token.type.getValue());
        return token;
    }

    private boolean charIsASCII(int ch) {
        if(ch > 127)
            return false;
        return true;
    }

    // public void showST(){
    //   Iterator<Integer> it = symbolTable.keySet().iterator();
    //   while(it.hasNext()){
    //     Integer key = it.next();
    //     System.out.println(key +" : "+symbolTable.get(key));
    //   }
    // }
}
