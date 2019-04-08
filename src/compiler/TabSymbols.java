package compiler;

import java.util.HashMap;
import java.util.Map;

public class TabSymbols {
	
	private static TabSymbols instance = new TabSymbols();
	private Map<String, Token> table;
	
	private TabSymbols() {
		table = new HashMap<String, Token>();
		
		// Pre-carrega palavras reservadas
		table.put("+", new Token(TokenType.ADDSUB, "+"));
		table.put("-", new Token(TokenType.ADDSUB, "-"));
		table.put("*", new Token(TokenType.MULTDIV, "*"));
		table.put("/", new Token(TokenType.MULTDIV, "/"));
		table.put(";", new Token(TokenType.ATTRIB, ";"));
		table.put("(", new Token(TokenType.L_PAR, "("));
		table.put(")", new Token(TokenType.R_PAR, ")"));
		table.put("true", new Token(TokenType.LOGIC_VAL, "true"));
		table.put("false", new Token(TokenType.LOGIC_VAL, "false"));
		table.put("and", new Token(TokenType.LOGIC_OP, "and"));
		table.put("not", new Token(TokenType.LOGIC_OP, "not"));
		table.put("or", new Token(TokenType.LOGIC_OP, "or"));
		table.put("logic", new Token(TokenType.TYPE, "logic"));
		table.put("text", new Token(TokenType.TYPE, "text"));
		table.put("num", new Token(TokenType.TYPE, "num"));
		table.put("program", new Token(TokenType.PROGRAM, "program"));
		table.put("endprog", new Token(TokenType.END_PROG, "endprog"));
		table.put("begin", new Token(TokenType.BEGIN, "begin"));
		table.put("end", new Token(TokenType.END, "end"));
		table.put("if", new Token(TokenType.IF, "if"));
		table.put("then", new Token(TokenType.THEN, "then"));
		table.put("else", new Token(TokenType.ELSE, "else"));
		table.put("for", new Token(TokenType.FOR, "for"));
		table.put("while", new Token(TokenType.WHILE, "while"));
		table.put("declare", new Token(TokenType.DECLARE, "declare"));
		table.put("to", new Token(TokenType.TO, "to"));
		table.put("EOF", new Token(TokenType.EOF, "EOF"));
		// ...
	}
	
	public static TabSymbols getInstance() {
		return instance;
	}
	
	public Token addID(String lexeme, long lin, long col) {
		Token token = table.get(lexeme);
		if (token == null) {
			// Entao este token eh um ID
			token = new Token(TokenType.ID, lexeme);
			table.put(token.getLexeme(), token);
		}
		token.setCol(col);
		token.setLin(lin);
		return token;
	}

	public void printTabSymbols() {
		System.out.println(" ----------------------------------------------------------");
		System.out.println("|                  Tabela de Simbolos                      |");
		System.out.println(" ----------------------------------------------------------");

		for (Token token : table.values()) {
			System.out.println(token.getLexeme());
		}
	}

}
