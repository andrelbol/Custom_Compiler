package lexical;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

class LexicalAnalysis implements AutoCloseable {
    private int line;
    private PushbackInputStream input;
    private SymbolTable symbolTable;

    public LexicalAnalysis(String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch(Exception e) {
            throw new LexicalException("Não foi possível abrir o arquivo: " + filename);
        }

        symbolTable = new SymbolTable();
        line = 1;
    }

    public void close() throws LexicalException {
        try {
            input.close();
        } catch(Exception e) {
            throw new LexicalException("Can't close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Token getToken() throws IOException, LexicalException {
        Token token = new Token("", TokenType.END_OF_FILE);
        return token;
    }
}
