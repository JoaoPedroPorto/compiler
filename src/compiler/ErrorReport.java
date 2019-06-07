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
    private String type;

    public static  ErrorReport Create(String description, String lexeme, long line, long column, String type) {
        return new ErrorReport(description, lexeme, line, column, type);
    }

    private ErrorReport(String description, String lexeme, long line, long column, String type) {
        this.description = description;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.type = type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}

