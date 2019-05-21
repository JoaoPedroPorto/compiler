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

public class Token {
	private TokenType type;
	private String lexeme;
	private long lin;
	private long col;

	public Token(TokenType type, String lexeme, long lin, long col) {
		this.type = type;
		this.lexeme = lexeme;
		this.lin = lin;
		this.col = col;
	}

	public Token(TokenType type, String lexeme) {
		this.type = type;
		this.lexeme = lexeme;
	}
	
	// GETTERS AND SETTERS
	
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
	public String getLexeme() {
		return lexeme;
	}
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	public long getLin() {
		return lin;
	}
	public void setLin(long lin) {
		this.lin = lin;
	}
	public long getCol() {
		return col;
	}
	public void setCol(long col) {
		this.col = col;
	}

	public void printToken(int id) {
		System.out.format("%5s %15s %20s %80s",id, "(" + this.getLin() + ", " + this.getCol() + ")", this.getLexeme(), this.getType());
		System.out.println("\n\n#-------------------------------------------------------------------------------------------------------------------------------------------------#\n");
	}

}
