package main;

import lexical.TokenType;

public class Main {
  public static void main(String[] args) {
    System.out.println("Valor de REPEAT: 11");
    TokenType t = TokenType.REPEAT;
    System.out.println("Valor resgatado: " + t.getValue());
  }
}