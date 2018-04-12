package lexical;

public enum TokenType{

  // Auxiliaries
  END_OF_FILE(0),
  INVALID_TOKEN(-1),

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
  OUT(16),
  // Symbols
  SEMI_COLON(17),
  COLON(18),
  PAR_OPEN(19),
  PAR_CLOSE(20),
  DOT(21), // .
  QUOTE_MARK(22), // "
  SINGLE_QUOTE_MARK(23), // '
  
  // Operators
  ASSIGN(24), // =
  NEGATION(25), // !
  EQUALS(26), // ==
  GREATER_THAN(27), // >
  GREATER_THAN_EQUAL(28), // >=
  LESS_THAN(29), // <
  LESS_THAN_EQUAL(30), // <=
  NOT_EQUAL(31), // !=
  ADD(32), // +
  MINUS(33), // -
  OR(34), // ||
  TIMES(35), // *
  DIV(36), // /
  AND(37), // &&

  // Others
  IDENTIFIER(38),
  INTEGER_CONST(39),
  STRING(40);

  private int value;

  TokenType(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }
}