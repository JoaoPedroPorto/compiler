package compiler;

/*
 * 
 * Arabianos
 * 
 * Rafaela Penteado da Cunha
 * João Pedro Porto
 * Diego Fortunato
 * Ulisses Maia
 * João Matos
 * Daniel Dos Anjos Barros
 *
 */

public class ErrorReport {

    private String description;
    private String lexeme;
    private long line;
    private long column;

    public static  ErrorReport Create(String description, String lexeme, long line, long column) {
        return new ErrorReport(description, lexeme, line, column);
    }

    private ErrorReport(String description, String lexeme, long line, long column) {
        this.description = description;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    // GETTERS AND SETTERS
    
    public String getDescription() {
        return description;
    }
	public void setDescription(String description) {
        this.description = description;
    }
    public String getLexeme() {
        return lexeme;
    }
	public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
    public long getLine() {
        return line;
    }
	public void setLine(long line) {
        this.line = line;
    }
    public long getColumn() {
        return column;
    }
	public void setColumn(long column) {
        this.column = column;
    }

}

