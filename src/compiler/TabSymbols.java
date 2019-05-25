package compiler;

import java.util.HashMap;
import java.util.Map;

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

public class TabSymbols {
	
	private static TabSymbols instance = new TabSymbols();
	private Map<String, Token> table;
	
	private TabSymbols() {
		table = new HashMap<String, Token>();
		// PRE-CARREGA PALAVRAS RESERVADAS
		/*
		table.put("+", new Token(TokenType.ARIT_AS, "+"));
		table.put("-", new Token(TokenType.ARIT_AS, "-"));
		table.put("*", new Token(TokenType.ARIT_MD, "*"));
		table.put("/", new Token(TokenType.ARIT_MD, "/"));
		table.put(";", new Token(TokenType.TERM, ";"));
		table.put("(", new Token(TokenType.L_PAR, "("));
		table.put(")", new Token(TokenType.R_PAR, ")"));
		*/
		table.put("true", new Token(TokenType.LOGIC_VAL, "true"));
		table.put("false", new Token(TokenType.LOGIC_VAL, "false"));
		table.put("and", new Token(TokenType.LOGIC_OP, "and"));
		table.put("not", new Token(TokenType.LOGIC_OP, "not"));
		table.put("or", new Token(TokenType.LOGIC_OP, "or"));
		table.put("bool", new Token(TokenType.TYPE, "bool"));
		table.put("text", new Token(TokenType.TYPE, "text"));
		table.put("int", new Token(TokenType.TYPE, "int"));
		table.put("float", new Token(TokenType.TYPE, "float"));
		table.put("num", new Token(TokenType.TYPE, "num"));
		table.put("program", new Token(TokenType.PROGRAM, "program"));
		table.put("end_prog", new Token(TokenType.END_PROG, "end_prog"));
		table.put("begin", new Token(TokenType.BEGIN, "begin"));
		table.put("end", new Token(TokenType.END, "end"));
		table.put("if", new Token(TokenType.IF, "if"));
		table.put("then", new Token(TokenType.THEN, "then"));
		table.put("else", new Token(TokenType.ELSE, "else"));
		table.put("for", new Token(TokenType.FOR, "for"));
		table.put("while", new Token(TokenType.WHILE, "while"));
		table.put("declare", new Token(TokenType.DECLARE, "declare"));
		table.put("to", new Token(TokenType.TO, "to"));
		/*
		table.put("<-", new Token(TokenType.ASSIGN, "<-"));
		table.put("EOF", new Token(TokenType.EOF, "EOF"));
		*/
	}
	
	public static TabSymbols getInstance() {
		return instance;
	}
	
	
	// CHAMADA PELO ANALISADOR LEXICO PARA ADICIONAR UM ID
	public Token addID(String lexeme, long lin, long col) {
		Token token = table.get(lexeme);
		// CASO TOKEN NAO EXISTA NA TABELA DE SIMBOLOS ELE E CONSIDERADO UM ID
		if (token == null) {
			token = new Token(TokenType.ID, lexeme);
			// ADICIONA NA TABELA DE SIMBOLOS
			table.put(token.getLexeme(), token);
		}
		token.setCol(col);
		token.setLin(lin);
		return token;
	}

	// FUNCAO RESPONSAVEL POR PRINTAR NA TELA TODOS OS SIMBOLOS
	public void printTabSymbols() {
		System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                   TABELA DE SIMBOLOS                                                                   ");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%5s %25s %50s", "#", "POS(x,y)", "LEXEMA");
        System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
        int i = 1;
		for (Token token : table.values()) {
			System.out.format("%5s %25s %50s",i, "(" + token.getLin() + ", " + token.getCol() + ")", token.getLexeme());
            System.out.println("\n\n#-------------------------------------------------------------------------------------------------------------------------------------------------#\n");
            i++;
		}
	}

}
