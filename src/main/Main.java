package main;

import lexical.LexicalAnalysis;
import lexical.TokenType;

public class Main {
  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("Passe um arquivo como parâmetro");
      return;
    }
    String file = args[0];
    try {
      LexicalAnalysis la = new LexicalAnalysis(file);  
      int i = 0;
      while(i < 10){
        la.getToken();
        i++;
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    
  }
}