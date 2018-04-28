package lexical;

import javax.swing.JOptionPane;

class LexicalException extends Exception {
  public LexicalException(String msg){
      super(msg);
      JOptionPane.showMessageDialog(null, msg);
  }
    public LexicalException(String msg, int linha){
        super(msg+" linha: "+linha);
        JOptionPane.showMessageDialog(null, msg+" linha: "+linha);
    }
}
