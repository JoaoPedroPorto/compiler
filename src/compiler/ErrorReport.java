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

    private String getDescription() {
        return description;
    }
    @SuppressWarnings("unused")
	private void setDescription(String description) {
        this.description = description;
    }
    private String getLexeme() {
        return lexeme;
    }
    @SuppressWarnings("unused")
	private void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
    private long getLine() {
        return line;
    }
    @SuppressWarnings("unused")
	private void setLine(long line) {
        this.line = line;
    }
    public long getColumn() {
        return column;
    }
    @SuppressWarnings("unused")
	private void setColumn(long column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return ("Descricao: " + this.getDescription() + "\n" +
                "Lexema: " + this.getLexeme() + "\n" +
                "Linha: " + this.getLine() + "\n" +
                "Coluna: " + this.getColumn() + "\n" +
                "-----------------------------------------------------------\n");

    }

}

