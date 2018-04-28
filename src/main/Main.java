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
      String msg = "";

      do {
        token = la.getToken();
        System.out.println(token.lexeme + " : " + token.type);
        msg += token.lexeme + " : " + token.type +'\n';
      } while(token.type != TokenType.END_OF_FILE);




      if(token.type == TokenType.END_OF_FILE){
        System.out.println("SUCESSO !!!");
        JOptionPane.showMessageDialog(null,"Sucessoo!!!\n\n"+ msg);
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

  }
}
