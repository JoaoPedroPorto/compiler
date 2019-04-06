package compiler;

import java.util.HashMap;
import java.util.Map;

public class TabSymbols {
	
	private static TabSymbols instance = new TabSymbols();
	private Map<String, Token> table;
	
	private TabSymbols() {
		table = new HashMap<String, Token>();
		
		// Pre-carrega palavras reservadas
		table.put("for", new Token(TokenType.FOR, "for"));
		table.put("while", new Token(TokenType.WHILE, "while"));
		table.put("to", new Token(TokenType.TO, "while"));
		// ...
	}
	
	public static TabSymbols getInstance() {
		return instance;
	}
	
	public Token instalaID(String lexema, int lin, int col) {
		Token t = table.get(lexema);
		if (t == null) {
			// Entao este token eh um ID
			t = new Token(TokenType.ID, lexema);
			table.put(t.getLexema(), t);
		}
		t.setCol(col);
		t.setLin(lin);
		return t;
	}

}
