package compiler;

public class Token {
	private TokenType type;
	private String lexema;
	private int lin;
	private int col;
	
	public Token(TokenType type, String lexema) {
		this.type = type;
		this.lexema = lexema;
	}
	
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public int getLin() {
		return lin;
	}
	public void setLin(int lin) {
		this.lin = lin;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
}
