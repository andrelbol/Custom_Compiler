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

    private void start() throws IOException, LexicalException {
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
            showError();
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
        case PAR_OPEN:
        case INTEGER_CONST:
        case FLOAT_CONST:
        case CHARACTER: // ??????????? tem que ver @andrelbol
        case NEGATION:
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

    }

    private void simple_expr() throws IOException, LexicalException {

    }

    private void simple_expr_new() throws IOException, LexicalException {

    }

    private void identifier() throws IOException, LexicalException {
        switch (current.type) {
        case IDENTIFIER:
            break;
        default:
            showError();
            break;
        }
    }

    private void literal() throws IOException, LexicalException {

    }
}
