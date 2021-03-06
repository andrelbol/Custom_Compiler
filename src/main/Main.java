package main;

import lexical.*;
import syntatical.*;
import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    // if(args.length != 1) {
    //   System.out.println("Passe um arquivo como parâmetro");
    //   return;
    // }

    String file = JOptionPane.showInputDialog("Entre com o caminho do arquivo:");

    try (LexicalAnalysis l = new LexicalAnalysis(file)){
        Token token;
        SyntaticalAnalysis a = new SyntaticalAnalysis(l);
        if(a.start()){
            System.out.println("Sucesso!!! Código compilado com sucesso !!!");
            JOptionPane.showMessageDialog(null,"Sucesso!!! Código compilado com sucesso !!!");

            JTextArea ta = new JTextArea(20,30);
            ta.setText("Tokens Encontrados:\n\n"+a.msg);
            ta.setEditable(false);
            JScrollPane sp = new JScrollPane(ta);
            JOptionPane.showMessageDialog(null,sp);
        }
    }catch(Exception e){
        System.err.println(e.getMessage());
    }

    //String file  = args[0];

    // try {
    //   LexicalAnalysis la = new LexicalAnalysis(file);
    //   Token token;
    //   String msg = "";
    //   String symTab = "";
    //
    //   do {
    //     token = la.getToken();
    //     System.out.println(token.lexeme + " : " + token.type);
    //     msg += token.lexeme + " : " + token.type +'\n';
    //   } while(token.type != TokenType.END_OF_FILE);
    //
    //   if(token.type == TokenType.END_OF_FILE){
    //     System.out.println("SUCESSO !!!");
    //         JTextArea ta = new JTextArea(20,30);
    //         ta.setText("Sucessoo!!!\n\n"+msg);
    //         ta.setEditable(false);
    //         JScrollPane sp = new JScrollPane(ta);
    //         JOptionPane.showMessageDialog(null,sp);
    //         symTab=la.showSymbolTable();
    //         ta.setText(symTab);
    //         JOptionPane.showMessageDialog(null,sp);
    //
    //   }
    // } catch (Exception e) {
    //   System.out.println("Exception: " + e);
    // }

  }
}
