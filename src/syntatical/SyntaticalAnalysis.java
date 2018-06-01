package syntatical;

import lexical.*;
import lexical.TokenType;

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

    public SyntaticalAnalysis(LexicalAnalysis la) throws IOException, LexicalException {
        this.la = la;
        this.current = la.getToken();
    }

    public void matchToken(TokenType type) throws IOException, LexicalException {
        if (type == current.type) {
            System.out.println("Token: "+current.lexeme + "  "+ current.type);
            current = la.getToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.println("ERRO : " + la.getLine() + " " + current.lexeme + " " + current.type);
        if (null != current.type) {
            switch (current.type) {
            case UNEXPECTED_EOF:
                System.out.println("ERRO EOF: " + la.getLine() + " " + current.lexeme + " " + current.type);
                break;
            case INVALID_TOKEN:
                System.out.println("ERRO IT: " + la.getLine() + " " + current.lexeme + " " + current.type);
                break;
            default:
                System.out.println("ERRO D: " + la.getLine() + " " + current.lexeme + " " + current.type);
                //System.out.printf("%02d: Operação inválida [" + current.token + "]", la.line());
                break;
            }
        }
    }

    private void showError(String msg) {
        System.out.println("ERRO : "+msg +"  " + la.getLine() + " " + current.lexeme + " " + current.type);
        if (null != current.type) {
            switch (current.type) {
            case UNEXPECTED_EOF:
                System.out.println("ERRO EOF: " + la.getLine() + " " + current.lexeme + " " + current.type);
                break;
            case INVALID_TOKEN:
                System.out.println("ERRO IT: " + la.getLine() + " " + current.lexeme + " " + current.type);
                break;
            default:
                System.out.println("ERRO D: " + la.getLine() + " " + current.lexeme + " " + current.type);
                //System.out.printf("%02d: Operação inválida [" + current.token + "]", la.line());
                break;
            }
        }
    }

    public void start() throws IOException, LexicalException {
        matchToken(TokenType.PROGRAM);
        identifier();
        body();
    }

    private void body() throws IOException, LexicalException {
        switch (current.type) {
        case DECLARE:
            matchToken(TokenType.DECLARE);
            decl_list();
            matchToken(TokenType.BEGIN);
            stmt_list();
            matchToken(TokenType.END);
            break;
        case BEGIN:
            matchToken(TokenType.BEGIN);
            stmt_list();
            matchToken(TokenType.END);
            break;

        default:
            showError("Esperava-se um DECALRE OU BEGIN");
            break;
        }
    }

    private void decl_list() throws IOException, LexicalException {
        decl();
        while (current.type == TokenType.SEMI_COLON) {
            matchToken(TokenType.SEMI_COLON);
            decl();
        }
    }

    private void decl() throws IOException, LexicalException {
        ident_list();
        matchToken(TokenType.COLON);
        type();
    }

    private void ident_list() throws IOException, LexicalException {
        identifier();
        while (current.type == TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            identifier();
        }
    }

    private void type() throws IOException, LexicalException {
        switch (current.type) {
        case INT:
            matchToken(TokenType.INT);
            break;
        case FLOAT:
            matchToken(TokenType.FLOAT);
            break;
        case CHAR:
            matchToken(TokenType.CHAR);
            break;
        default:
            showError();
            break;
        }
    }

    private void stmt_list() throws IOException, LexicalException {
        stmt();
        while (current.type == TokenType.SEMI_COLON) {
            matchToken(TokenType.SEMI_COLON);
            stmt();
        }
    }

    private void stmt() throws IOException, LexicalException {
        switch (current.type) {
        case IDENTIFIER:
            assign_stmt();
            break;
        case IF:
            if_stmt();
            break;
        case WHILE:
            while_stmt();
            break;
        case REPEAT:
            repeat_stmt();
            break;
        case IN:
            read_stmt();
            break;
        case OUT:
            write_stmt();
            break;
        default:
            showError();
            break;
        }
    }

    private void assign_stmt() throws IOException, LexicalException {
        identifier();
        matchToken(TokenType.ASSIGN);
        simple_expr();
    }

    private void if_stmt() throws IOException, LexicalException {
        matchToken(TokenType.IF);
        condition();
        matchToken(TokenType.THEN);
        stmt_list();
        if_stmt_new();
    }

    private void if_stmt_new() throws IOException, LexicalException {
        switch (current.type) {
        case END:
            matchToken(TokenType.END);
            break;
        case ELSE:
            matchToken(TokenType.ELSE);
            stmt_list();
            matchToken(TokenType.END);
            break;
        default:
            showError();
            break;
        }
    }

    private void condition() throws IOException, LexicalException { // precisa do switch ???
        expression();
    }

    private void repeat_stmt() throws IOException, LexicalException {
        matchToken(TokenType.REPEAT);
        stmt_list();
        stmt_suffix();
    }

    private void stmt_suffix() throws IOException, LexicalException {
        matchToken(TokenType.UNTIL);
        condition();
    }

    private void while_stmt() throws IOException, LexicalException {
        stmt_prefix();
        stmt_list();
        matchToken(TokenType.END);
    }

    private void stmt_prefix() throws IOException, LexicalException {
        matchToken(TokenType.WHILE);
        condition();
        matchToken(TokenType.DO);
    }

    private void read_stmt() throws IOException, LexicalException {
        matchToken(TokenType.IN);
        matchToken(TokenType.PAR_OPEN);
        identifier();
        matchToken(TokenType.PAR_CLOSE);
    }

    private void write_stmt() throws IOException, LexicalException {
        matchToken(TokenType.OUT);
        matchToken(TokenType.PAR_OPEN);
        writable();
        matchToken(TokenType.PAR_CLOSE);
    }

    private void writable() throws IOException, LexicalException {
        switch (current.type) {
        case PAR_CLOSE:
        case INTEGER_CONST:
        case FLOAT_CONST:
        case CHARACTER: // ??????????? tem que ver @andrelbol
        case NEGATION:
        case EQUALS:
        case IDENTIFIER:
        case MINUS:
            simple_expr();
            break;
        case STRING:
            literal();
        default:
            showError();
            break;
        }
    }

    private void expression() throws IOException, LexicalException { // colocar case ???
        simple_expr();
        expression_new();
    }

    private void expression_new() throws IOException, LexicalException { // pela tabela não dá pra fazer a 21....
        switch (current.type){
        case END:
        case SEMI_COLON:
        case THEN:
        case ELSE:
        case UNTIL:
        case DO:
            break;
        case MINUS:
        case EQUALS:
        case GREATER_THAN:
        case GREATER_THAN_EQUAL:
        case LESS_THAN:
        case LESS_THAN_EQUAL:
        case NOT_EQUAL:
            relop();
            simple_expr(); break;
        default:
            showError(); break;
        }
    }

    private void simple_expr() throws IOException, LexicalException {
        term();
        simple_expr_new();
    }

    private void simple_expr_new() throws IOException, LexicalException {
        switch(current.type){
        case END:
        case SEMI_COLON:
        case THEN:
        case ELSE:
        case UNTIL:
        case DO:
        case GREATER_THAN:
        case LESS_THAN:
            break;
        case MINUS:
        case ADD:
        case NOT_EQUAL:
        case OR:
            addop(); term(); simple_expr_new(); break;
        default:
            showError(); break;
      }
    }

    private void identifier() throws IOException, LexicalException {
        switch (current.type) {
        case IDENTIFIER:
            matchToken(TokenType.IDENTIFIER);
            break;
        default:
            System.out.println("Erro ID");
            showError();
            break;
        }
    }

    private void term() throws IOException, LexicalException{
        factor_a(); term_new();
    }

    private void term_new() throws IOException, LexicalException{
        switch(current.type){
            case END:
            case SEMI_COLON:
            case THEN:
            case ELSE:
            case UNTIL:
            case DO:
            case PAR_CLOSE:
            case MINUS:
            case GREATER_THAN:
            case LESS_THAN:
            case OR:
                  break;
            case TIMES:
            case DIV:
            case AND:
                  mulop(); factor_a(); term_new(); break;
            default:
                showError();
                break;
        }

    }

    private void factor_a() throws IOException, LexicalException{
        switch(current.type){
            case PAR_CLOSE:
            case CHARACTER:
            case IDENTIFIER:
            case FLOAT_CONST:
            case INTEGER_CONST:
                factor(); break;
            case NEGATION:
                matchToken(TokenType.NEGATION);
                factor();
                break;
            case MINUS:
                matchToken(TokenType.MINUS);
                factor();
                break;
            default:
                showError();
                break;
        }
    }

    private void factor() throws IOException, LexicalException{
          switch(current.type){
              case PAR_OPEN:
                  matchToken(TokenType.PAR_OPEN);
                  expression();
                  matchToken(TokenType.PAR_CLOSE);
                  break;
              case CHARACTER:
              case INTEGER_CONST:
              case FLOAT_CONST:
                  constant();
                  break;
              case IDENTIFIER:
                  identifier();
                  break;
              default:
                  showError();
                  break;
          }
    }

    private void relop() throws IOException, LexicalException{
          switch(current.type){
              case EQUALS:
                  matchToken(TokenType.EQUALS);
                  break;
              case GREATER_THAN:
                  matchToken(TokenType.GREATER_THAN);
                  break;
              case GREATER_THAN_EQUAL:
                  matchToken(TokenType.GREATER_THAN_EQUAL);
                  break;
              case LESS_THAN:
                  matchToken(TokenType.LESS_THAN);
                  break;
              case LESS_THAN_EQUAL:
                  matchToken(TokenType.LESS_THAN_EQUAL);
                  break;
              case NOT_EQUAL:
                  matchToken(TokenType.NOT_EQUAL);
                  break;
              default:
                  showError();
                  break;
          }
    }

    private void addop() throws IOException, LexicalException{
          switch(current.type){
              case ADD:
                  matchToken(TokenType.ADD);
                  break;
              case MINUS:
                  matchToken(TokenType.MINUS);
                  break;
              case OR:
                  matchToken(TokenType.OR);
                  break;
              default:
                  showError(); break;
          }
    }

    private void mulop() throws IOException, LexicalException{
        switch(current.type){
            case TIMES:
                matchToken(TokenType.TIMES);
                break;
            case DIV:
                matchToken(TokenType.DIV);
                break;
            case AND:
                matchToken(TokenType.AND);
                break;
            default:
                showError(); break;
        }
    }

    private void constant() throws IOException, LexicalException{
        switch(current.type){
            case INTEGER_CONST:
                matchToken(TokenType.INTEGER_CONST);
                break;
            case FLOAT_CONST:
                matchToken(TokenType.FLOAT_CONST);
                break;
            case CHARACTER:
                matchToken(TokenType.CHARACTER);
                break;
            default:
                showError(); break;
        }
    }

    private void literal() throws IOException, LexicalException {
          switch(current.type){
            case STRING:
              matchToken(TokenType.STRING);
              break;
            default:
              showError(); break;
          }
    }


}
