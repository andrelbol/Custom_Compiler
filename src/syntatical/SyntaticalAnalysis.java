package syntatical;

import compilador.DataType;
import compilador.LexemeData;
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

import javax.swing.JOptionPane;

public class SyntaticalAnalysis {

    private Token current;
    private LexicalAnalysis la;
    public String msg;
    private SymbolTable symbolTable = new SymbolTable();
    private int offset = 0;

    public SyntaticalAnalysis(LexicalAnalysis la) throws IOException, LexicalException {
        this.la = la;
        this.current = la.getToken();
        this.msg = "";
    }

    public void matchToken(TokenType type) throws IOException, LexicalException {
        if (type == current.type) {
            System.out.println("Token: "+current.lexeme + "  "+ current.type);
            this.msg+="Token: "+current.lexeme + "  "+ current.type+"\n";
            current = la.getToken();
        } else {
            showError("Esperava-se "+type);
        }
    }

    // private void showError() {
    //     System.out.println("ERRO : " + la.getLine() +  " " + current.type +" " + current.lexeme);
    //     if (null != current.type) {
    //         switch (current.type) {
    //         case UNEXPECTED_EOF:
    //             System.out.println("ERRO EOF: " + la.getLine() + " " + current.lexeme + " " + current.type);
    //             break;
    //         case INVALID_TOKEN:
    //             System.out.println("ERRO IT: " + la.getLine() + " " + current.lexeme + " " + current.type);
    //             break;
    //         default:
    //             System.out.println("ERRO D: " + la.getLine() + " " + current.lexeme + " " + current.type);
    //             //System.out.printf("%02d: Operação inválida [" + current.token + "]", la.line());
    //             break;
    //         }
    //     }
    // }

    private void showError(String msg) {
        System.out.println("Erro Sintático: Linha "+la.getLine()+", " +msg+" mas encontrou "+ current.type +" " +current.lexeme);
        JOptionPane.showMessageDialog(null,"Erro Sintático: Linha "+la.getLine()+", " +msg+" mas encontrou "+ current.type +" " +current.lexeme);
        System.exit(0);
    }

    public boolean start() throws IOException, LexicalException {
      switch(current.type){
        case PROGRAM:
          matchToken(TokenType.PROGRAM);
          identifier();
          body();
          break;
        default:
          showError("Esperava-se PROGRAM"); break;
      }
      return true;
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
            showError("Esperava-se um DECLARE OU BEGIN");
            break;
        }
    }

    private void decl_list() throws IOException, LexicalException {
      switch(current.type){
        case IDENTIFIER:
            decl();
            while (current.type == TokenType.SEMI_COLON) {
                matchToken(TokenType.SEMI_COLON);
                decl();
            }break;
        default:
          showError("Esperava-se um IDENTIFICADOR 1"); break;
      }
    }

    private void decl() throws IOException, LexicalException {
        List<String> identList;
        DataType type;
      switch(current.type){
        case IDENTIFIER:
          identList = ident_list();
          matchToken(TokenType.COLON);
          type = type();
          for (String s : identList) {
            symbolTable.st.put(s, new LexemeData(offset,type,current.type));
            switch(type){
                case INT:   offset+=4; break;
                case FLOAT: offset+=8; break;
                case CHAR:  offset+=4; break;
            }
          }
          break;
        default:
            showError("Esperava-se um IDENTIFICADOR 2");
            break;
      }
    }

    private List<String> ident_list() throws IOException, LexicalException {
        List<String> identList = new ArrayList<>();
      switch(current.type){
        case IDENTIFIER:
          identList.add(identifier());
          while (current.type == TokenType.COMMA) {
              matchToken(TokenType.COMMA);
              identifier();
          }
          break;
        default:
            showError("Esperava-se um IDENTIFICADOR 3");
            break;
      }
      return identList;
    }

    private DataType type() throws IOException, LexicalException {
        switch (current.type) {
        case INT:
            matchToken(TokenType.INT);
            return DataType.INT;
        case FLOAT:
            matchToken(TokenType.FLOAT);
            return DataType.FLOAT;
        case CHAR:
            matchToken(TokenType.CHAR);
            return DataType.CHAR;
        default:
            showError("Esperava-se um INT ou FLOAT ou CHAR");
            break;
        }
        return null;
    }

    private void stmt_list() throws IOException, LexicalException {
      // switch(current.type){
      //   case IDENTIFIER:
          stmt();
          while (current.type == TokenType.SEMI_COLON) {
              matchToken(TokenType.SEMI_COLON);
              stmt();
          }
      //     break;
      //   default:
      //     showError("Esperava-se um IDENTIFICADOR 4"); break;
      // }
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
            showError("Esperava-se um IDENTIFICADOR, IF, WHILE, REPEAT, IN ou OUT"+
            "\nConsidere colocar ou retirar um ; SEMI_COLON");
            break;
        }
    }

    private void assign_stmt() throws IOException, LexicalException {
        switch(current.type){
          case IDENTIFIER:
              identifier();
              matchToken(TokenType.ASSIGN);
              simple_expr();
              break;
          default:
              showError("Esperava-se um IDENTIFICADOR 5"); break;
        }
    }

    private void if_stmt() throws IOException, LexicalException {
      switch(current.type){
        case IF:
            matchToken(TokenType.IF);
            condition();
            matchToken(TokenType.THEN);
            stmt_list();
            if_stmt_new();
            break;
        default:
            showError("Esperava-se um IF"); break;
      }
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
            showError("Esperava-se um END ou ELSE");
            break;
        }
    }

    private void condition() throws IOException, LexicalException { // precisa do switch ???
        switch(current.type){
            case PAR_OPEN:
            case NEGATION:
            case MINUS:
            case CHARACTER:
            case IDENTIFIER:
            case FLOAT_CONST:
            case INTEGER_CONST:
                expression(); break;
            default:
              showError("Esperava-se um (, !, -, CHARACTER, IDENTIFICADOR, ou NUMBER\n Considere colocar ou retirar um ; SEMI_COLON"); break;
        }
    }

    private void repeat_stmt() throws IOException, LexicalException {
        switch(current.type){
          case REPEAT:
              matchToken(TokenType.REPEAT);
              stmt_list();
              stmt_suffix();
              break;
          default:
              showError("Esperava-se um REPEAT"); break;
      }
    }

    private void stmt_suffix() throws IOException, LexicalException {
        switch(current.type){
          case UNTIL:
            matchToken(TokenType.UNTIL);
            condition();
            break;
          default:
            showError("Esperava-se um UNTIL"); break;
        }
    }

    private void while_stmt() throws IOException, LexicalException {
        switch(current.type){
          case WHILE:
            stmt_prefix();
            stmt_list();
            matchToken(TokenType.END);
            break;

          default:
            showError("Esperava-se um WHILE"); break;
      }
    }

    private void stmt_prefix() throws IOException, LexicalException {
      switch(current.type){
        case WHILE:
            matchToken(TokenType.WHILE);
            condition();
            matchToken(TokenType.DO);
            break;
        default:
            showError("Esperava-se um WHILE"); break;
      }
    }

    private void read_stmt() throws IOException, LexicalException {
      switch(current.type){
        case IN:
            matchToken(TokenType.IN);
            matchToken(TokenType.PAR_OPEN);
            identifier();
            matchToken(TokenType.PAR_CLOSE);
            break;

        default:
          showError("Esperava-se um IN");break;
      }
    }

    private void write_stmt() throws IOException, LexicalException {
      switch(current.type){
        case OUT:
            matchToken(TokenType.OUT);
            matchToken(TokenType.PAR_OPEN);
            writable();
            matchToken(TokenType.PAR_CLOSE);
            break;
        default:
          showError("Esperava-se um OUT"); break;
      }
    }

    private void writable() throws IOException, LexicalException {
        switch (current.type) {
        case PAR_CLOSE: // botar close
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
            break;
        default:
            showError("Esperava-se um (, STRING, NUMBER, !, - IDENTIFICADOR, ==, CHARACTER");
            break;
        }
    }

    private void expression() throws IOException, LexicalException { // colocar case ???
        switch(current.type){
          case PAR_OPEN:
          case NEGATION:
          case MINUS:
          case CHARACTER:
          case IDENTIFIER:
          case FLOAT_CONST:
          case INTEGER_CONST:
              simple_expr();
              expression_new();
              break;

          default:
              showError("Esperava-se um (, !, -, CHARACTER, NUMBER 1"); break;
        }
    }

    private void expression_new() throws IOException, LexicalException { // pela tabela não dá pra fazer a 21....
        switch (current.type){
        case END:
        case SEMI_COLON:
        case PAR_CLOSE:
        case THEN:
        case ELSE:
        case UNTIL:
        case DO:
            break;
        case EQUALS:
        case GREATER_THAN:
        case GREATER_THAN_EQUAL:
        case LESS_THAN:
        case LESS_THAN_EQUAL:
        case NOT_EQUAL:
            relop();
            simple_expr(); break;
        default:
            showError("Esperava-se várias coisas 1");break;
        }
    }

    private void simple_expr() throws IOException, LexicalException {
        switch(current.type){
          case PAR_OPEN:
          case NEGATION:
          case MINUS:
          case CHARACTER:
          case IDENTIFIER:
          case FLOAT_CONST:
          case INTEGER_CONST:
              term();
              simple_expr_new();
              break;
          default:
              showError("Esperava-se (, !, -, CHARACTER, NUMBER 2");break;
       }
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
        case GREATER_THAN_EQUAL:
        case LESS_THAN_EQUAL: // adcionando aqui
        case LESS_THAN:
        case PAR_CLOSE:
        case EQUALS:
        case NEGATION:
            break;
        case MINUS:
        case ADD:
        case OR:
            addop(); term(); simple_expr_new(); break;
        default:
            showError("Esperava-se várias coisas 2");break;
      }
    }

    private String identifier() throws IOException, LexicalException {
        switch (current.type) {
        case IDENTIFIER:
            matchToken(TokenType.IDENTIFIER);
            break;
        default:
            showError("Esperava-se um IDENTIFICADOR 6");
            break;
        }
        return current.lexeme;
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
            case PAR_OPEN:
            case PAR_CLOSE:
            case MINUS:
            case GREATER_THAN:
            case GREATER_THAN_EQUAL: // adcionando aqui
            case LESS_THAN_EQUAL:
            case LESS_THAN:
            case OR:
            case ADD:
            case EQUALS:
            case NEGATION:
                  break;
            case TIMES:
            case DIV:
            case AND:
                  mulop(); factor_a(); term_new(); break;
            default:
                showError("Esperava-se OPERADORES ou ;");
                break;
        }

    }

    private void factor_a() throws IOException, LexicalException{
        switch(current.type){
            case PAR_OPEN:
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
                showError("Esperava-se um !, - variáveis ou (");
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
                  showError("Esperava-se um (, NUMBER, CHARACTER ou IDENTIFICADOR");
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
                  showError("Esperava-se ==, <, <=, >, >= ou !=");
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
                  showError("Esperava-se um +, - ou ||"); break;
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
                showError("Esperava-se um *, / ou &&"); break;
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
                showError("Esperava-se um NUMBER"); break;
        }
    }

    private void literal() throws IOException, LexicalException {
          switch(current.type){
            case STRING:
              matchToken(TokenType.STRING);
              break;
            default:
              showError("Esperava-se uma STRING"); break;
          }
    }


}
