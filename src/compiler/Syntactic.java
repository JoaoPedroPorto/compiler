package compiler;

import java.io.IOException;

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

public class Syntactic {
	
	private Lexicon lexicon;
	private ErrorHandler errors = ErrorHandler.getInstance();
	
	// INTANCIA O ANALISADOR LEXICO E PASSA NO CONSTRUTOR O ARQUIVO A SER VERIFICADO
	public Syntactic(String fileName) throws IOException {
		this.lexicon = new Lexicon(fileName);
	}
	
	public void process() throws IOException {
		int id = 1;
		// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
		Token token = lexicon.nextToken();
		// PRINTA OS TOKENS GERADOS
		System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                   TOKENS GERADOS                                                                   ");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%5s %15s %20s %60s", "#", "POS(x,y)", "LEXEMA", "TOKEN");
        System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
		token.printToken(id);
		// CASO O 1 TOKEN GERADO PELO LEXICO NAO SEJA O FIM DO ARQUIVO O ANALISADOR SINTATICO 
		// FICA PEDINDO TOKENS PARA O LEXICO ATE QUE CHEGUE UM TOKEN QUE SEJA O FIM DO ARQUIVO
		while (token.getType() != TokenType.EOF) {
			id++;
			// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
			token = lexicon.nextToken();
			// PRINTA O TOKEN GERADO
			token.printToken(id);
		}
		// PRINTA A TABELA DE SIMBOLOS
		TabSymbols.getInstance().printTabSymbols();
		// PRINTA OS ERROS GERADOS NA VERIFICACAO, CASO HAJA
		ErrorHandler.getInstance().errorReport();
	}
	
	public void derivaS() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() != TokenType.PROGRAM)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaCmds() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.DECLARE) {
			lexicon.storeToken(token);
			derivaDecl();
		} else if (token.getType() == TokenType.IF) {
			lexicon.storeToken(token);
			derivaCond();
		} else if (token.getType() == TokenType.FOR) {
			lexicon.storeToken(token);
			derivaRepF();
		} else if (token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaRepW();
		} else if (token.getType() == TokenType.ID) {
			lexicon.storeToken(token);
			derivaAtrib();
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.END)
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaCmd() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.DECLARE) {
			lexicon.storeToken(token);
			derivaDecl();
		} else if (token.getType() == TokenType.IF) {
			lexicon.storeToken(token);
			derivaCond();
		} else if (token.getType() == TokenType.FOR || token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaRep();
		} else if (token.getType() == TokenType.ID) {
			lexicon.storeToken(token);
			derivaAtrib();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaAtrib() throws IOException {
		
	}
	
	public void derivaDecl() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.DECLARE)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaCond() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.IF)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaCndb() throws IOException {
		Token token = lexicon.nextToken();
			
		if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.DECLARE || token.getType() != TokenType.IF || token.getType() != TokenType.FOR || token.getType() != TokenType.WHILE || token.getType() != TokenType.ID || token.getType() != TokenType.END || token.getType() != TokenType.END_PROG || token.getType() != TokenType.ELSE)		
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
}
	
	public void derivaExp() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.LOGIC_VAL || token.getType() != TokenType.ID || token.getType() != TokenType.NUM_INT || token.getType() != TokenType.NUM_FLOAT || token.getType() != TokenType.LITERAL)		
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFId() {
		Token token = lexicon.nextToken();
		
		if (token == 'logic_op') {
			lexicon.storeToken(token);
			derivaEValLog();
		} else if (token == 'arit_as' || token == 'arit_md') {
			lexicon.storeToken(token);
			derivaOpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum() {
		Token token = lexicon.nextToken();
		
		if (token == 'l_par' || token == 'id' || token == 'num_int' || token == 'num_float') {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum1() {
		Token token = lexicon.nextToken();
		
		if (t == null) {
			lexicon.storeToken(token);
			if (t != 'term')	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else if (t != 'relop')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFNumInt() {
		Token token = lexicon.nextToken();
		
		if (token == 'arit_as' || token == 'arit_md') {
			lexicon.storeToken(token);
			derivaOpNum();
		} else if (token == null) {
			lexicon.storeToken(token);
			if (t != 'term') {	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum1() {
		Token token = lexicon.nextToken();
		
		if (token == 'l_par' || token == 'id' || token == 'num_int' || token == 'num_float') {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum2() {
		Token token = lexicon.nextToken();
		
		if (t == null) {
			lexicon.storeToken(token);
			if (t != 'term')	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else if (t != 'relop')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaFNumFloat() {
		Token token = lexicon.nextToken();
		
		if (token == 'arit_as' || token == 'arit_md') {
			lexicon.storeToken(token);
			derivaOpNum();
		} else if (token == null) {
			lexicon.storeToken(token);
			if (t != 'term') {	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum2() {
		Token token = lexicon.nextToken();
		
		if (token == 'l_par' || token == 'id' || token == 'num_int' || token == 'num_float') {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum3() {
		Token token = lexicon.nextToken();
		
		if (t == null) {
			lexicon.storeToken(token);
			if (t != 'term')	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else if (t != 'relop')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaFLPar() {
		Token token = lexicon.nextToken();
		
		if (token == 'l_par' || token == 'id' || token == 'num_int' || token == 'num_float') {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum() {
		Token token = lexicon.nextToken();
		
		if (t != 'r_par')		
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFRPar() {
		Token token = lexicon.nextToken();
		
		if (t == null) {
			lexicon.storeToken(token);
			if (t != 'term')	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else if (t != 'relop')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaFId1() {
		Token token = lexicon.nextToken();
		
		if (token.getTokenType() == TokenType.LOGIC_OP) {
			lexicon.storeToken(token);
			derivaEValLog();
		} else if (token.getTokenType() == TokenType.ARIT_AS || token.getTokenType() == TokenType.ARIT_MD) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaExpNum();
		} else if (token.getTokenType() == TokenType.RELOP)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaEValLog() {
		Token token = lexicon.nextToken();
		
		if (t == null) {
			lexicon.storeToken(token);
			if (t != 'term' || t != 'r_par')	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			token.getNextChar();
			}
		} else if (t != 'logic_val')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaXExpNum() {
		Token token = lexicon.nextToken();
		
		if (token == 'arit_as' || token == 'arit_md') {
			lexicon.storeToken(token);
			derivaOpNum();
		} else if (token == null) {
			if (t != 'relop' || t != 'to' || t != 'begin' || t != 'declare' || t != 'if'|| t != 'id' || t != 'for' || t != 'while' || t != 'term' || t != 'r_par')
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaOpNum() {
		Token token = lexicon.nextToken();
		
		if (token != 'arit_as' || token != 'arit_md')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaVal() {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.ID || token != 'num_int' || token != 'num_float')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaRep() {
		Token token = lexicon.nextToken();
		
		if (token == 'for') {
			lexicon.storeToken(token);
			derivaRepF();
		} else if (token == 'while') {
			lexicon.storeToken(token);
			derivaRepW();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaRepF() {
		Token token = lexicon.nextToken();
		
		if (token != 'for')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaExpNum() {
		Token token = lexicon.nextToken();
		
		if (token == 'id' || token == 'num_int' || token == 'num_float') {
			lexicon.storeToken(token);
			derivaVal();
		} else if (t != 'l_par')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaRepW() {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.WHILE)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaExpLo() {
		Token token = lexicon.nextToken();
		
		if (token != 'logic_val' || token.getType() != TokenType.ID || token != 'l_par' ||  token != 'num_int' || token != 'num_float')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaBloco() {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.ID || token.getType() == TokenType.DECLARE || token.getType() == TokenType.IF || token.getType() == TokenType.FOR || token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaCmd();
		} else if (t != 'begin')
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}

}
