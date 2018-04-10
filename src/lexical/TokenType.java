package lexical;

public enum TokenType{

  // Keywords
  PROGRAM(1),
  DECLARE(2),
  BEGIN(3),
  END(4),
  INT(5),
  FLOAT(6),
  CHAR(7),
  IF(8),
  THEN(9),
  ELSE(10),
  REPEAT(11),
  UNTIL(12),
  WHILE(13),
  DO(14),
  IN(15),
  OUT(16);

  private int value;

  TokenType(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }
}