package syntatical;

import lexical.*;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.*;


public class SyntaticalAnalysis {

    private Token current;
    private LexicalAnalysis la;


    public SyntaticalAnalysis(LexicalAnalysis la) throws IOException{
        this.la = la;
        this.current = la.getToken();
    }

    public void matchToken(TokenType type) throws IOException{
        if(type == current.type){
            current = la.getToken();
        }else{
            showError();
        }
    }

    void showError(){
        System.out.println("ERRO : " + la.line() + " " + current.token + " " + current.type);
        if (null != current.type) {
            switch (current.type) {
                case UNEXPECTED_EOF:
                    System.out.println("ERRO EOF: " + la.line() + " " + current.token + " " + current.type);
                    break;
                case INVALID_TOKEN:
                    System.out.println("ERRO IT: " + la.line() + " " + current.token + " " + current.type);
                    break;
                default:
                    System.out.println("ERRO D: " + la.line() + " " + current.token + " " + current.type);
                    //System.out.printf("%02d: Operação inválida [" + current.token + "]", la.line());
                    break;
            }
    }

    void start() throws IOException{
        matchToken(TokenType.PROGRAM);
        identifier();
        body();
    }

    void body(){
        switch(current.type){
            case TokenType.DECLARE:
                matchToken(TokenType.DECLARE);
                decl_list();
                matchToken(TokenType.BEGIN);
                stmt_list();
                matchToken(TokenType.END);
                break;
            case TokenType.BEGIN:
                matchToken(TokenType.BEGIN);
                stmt_list();
                matchToken(TokenType.END);
                break;

            default:
                showError();
                break;
        }
    }

    void dcl_list(){
        decl();
        while(current.type == TokenType.SEMI_COLON){
            matchToken(TokenType.SEMI_COLON);
            decl();
        }
    }

    void decl(){
        ident_list();
        matchToken(TokenType.COLON);
        type();
    }

    void ident_list(){
        identifier();
        while(current.type == TokenType.COMMA){
            matchToken(TokenType.COMMA);
            identifier();
        }
    }

    void type(){
        switch(current.type){
            case TokenType.INT: matchToken(TokenType.INT); break;
            case TokenType.FLOAT: matchToken(TokenType.FLOAT); break;
            case TokenType.CHAR: matchToken(TokenType.CHAR); break;
            default showError(); break;
        }
    }

    void stmt_list(){
        stmt();
        while(current.type == TokenType.SEMI_COLON){
            matchToken(TokenType.SEMI_COLON);
            stmt();
        }
    }

    void stmt(){
        switch(current.type){
            case TokenType.IDENTIFIER: assign_stmt();break;
            case TokenType.IF: if_stmt(); break;
            case TokenType.WHILE: while_stmt(); break;
            case TokenType.REPEAT: repeat_stmt(); break;
            case TokenType.IN: read_stmt(); break;
            case TokenType.OUT: write_stmt(); break;
            default: showError(); break;
        }
    }

    void assign_stmt(){
        identifier();
        matchToken(TokenType.ASSIGN);
        simple_expr();
    }

    void if_stmt(){
        matchToken(TokenType.IF);
        condition();
        matchToken(TokenType.THEN);
        stmt_list();
        if_stmt_new();
    }

    void if_stmt_new(){
        switch(current.type){
            case TokenType.END: matchToken(TokenType.END);break;
            case TokenType.ELSE: matchToken(TokenType.ELSE); stmt_list();
                                  matchToken(TokenType.END); break;
            default: showError(); break;
        }
    }

    void condition(){ // precisa do switch ???
        expression();
    }

    void repeat_stmt(){
        matchToken(TokenType.REPEAT);
        stmt_list();
        stmt_suffix();
    }

    void stmt_suffix(){
        matchToken(TokenType.UNTIL);
        condition();
    }

    void while_stmt(){
        stmt_prefix();
        stmt_list();
        matchToken(TokenType.END);
    }

    void stmt_prefix(){
        matchToken(TokenType.WHILE);
        condition();
        matchToken(TokenType.DO);
    }

    void read_stmt(){
        matchToken(TokenType.IN);
        matchToken(TokenType.PAR_OPEN);
        identifier();
        matchToken(TokenType.PAR_CLOSE);
    }

    void write_stmt(){
        matchToken(TokenType.OUT);
        matchToken(TokenType.PAR_OPEN);
        writable();
        matchToken(TokenType.PAR_CLOSE);
    }

    void writable(){
        switch(current.type){
            case TokenType.PAR_OPEN:
            case TokenType.INTEGER_CONST:
            case TokenType.FLOAT_CONST
            case TokenType.CHARACTER:      // ??????????? tem que ver @andrelbol
            case TokenType.NEGATION:
            case TokenType.MINUS: simple_expr(); break;
            case TokenType.STRING: literal();
            default: showError();break;
        }
    }

    void expression(){ // colocar case ???
        simple_expr();
        expression_new();
    }

    void expression_new(){

    }

    void identifier(){
        switch (current.type) {
            case TokenType.IDENTIFIER: break;
            default:
                showError();break;
        }
    }


}
