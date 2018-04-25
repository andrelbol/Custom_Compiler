package lexical;

class LexicalException extends Exception {
  public LexicalException(String msg){
      super(msg+"kjdsfkjdfsnk");
  }
    public LexicalException(String msg, int linha){
        super(msg+" linha: "+linha);
    }
}
