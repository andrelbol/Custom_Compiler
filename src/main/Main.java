package main;

import lexical.*;
import javax.swing.JOptionPane;

public class Main {
  public static void main(String[] args) {
    // if(args.length != 1) {
    //   System.out.println("Passe um arquivo como parâmetro");
    //   return;
    // }

    String file = JOptionPane.showInputDialog("Entre com o caminho do arquivo:");

    //String file  = args[0];

    try {
      LexicalAnalysis la = new LexicalAnalysis(file);
      Token token;

      do {
        token = la.getToken();
        System.out.println(token.lexeme + " : " + token.type);
      } while(token.type != TokenType.END_OF_FILE);
      la.showSymbolTable();
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

  }
}
