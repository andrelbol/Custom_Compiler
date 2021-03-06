package lexical;

import javax.swing.JOptionPane;

public class LexicalException extends Exception {
  public LexicalException(String msg){
      super(msg);
      JOptionPane.showMessageDialog(null, msg);
  }
    public LexicalException(String msg, int line){
        super("Erro Léxico: Linha "+ line +", " +msg);
        JOptionPane.showMessageDialog(null,"Erro Léxico: Linha "+ line +", "+ msg);
    }
}
