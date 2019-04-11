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

	public void printToken() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return (" Token: " + this.getType()
				+ "  Lexema: " + this.getLexeme()
				+ "  Linha: " + this.getLin()
				+ "  Coluna: " + this.getCol()
				+ "\n-----------------------------------------------------------\n");

	}
}
