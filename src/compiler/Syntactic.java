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
		//token.printToken(id);
		// CASO O 1 TOKEN GERADO PELO LEXICO NAO SEJA O FIM DO ARQUIVO O ANALISADOR SINTATICO 
		// FICA PEDINDO TOKENS PARA O LEXICO ATE QUE CHEGUE UM TOKEN QUE SEJA O FIM DO ARQUIVO
		while (token.getType() != TokenType.EOF) {
			// PRINTA O TOKEN GERADO
			token.printToken(id);
			id++;
			// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
			token = lexicon.nextToken();
		}
		
		processDerivas();
		
		// PRINTA A TABELA DE SIMBOLOS
		TabSymbols.getInstance().printTabSymbols();
		// PRINTA OS ERROS GERADOS NA VERIFICACAO, CASO HAJA
		ErrorHandler.getInstance().errorReport();
	}
	
	public void processDerivas() {
		
	}

	public void derivaS() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() == TokenType.PROGRAM)
			token = lexicon.nextToken();
		else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.ID)
			token = lexicon.nextToken();
		else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.TERM)
			token = lexicon.nextToken();
		else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.BEGIN || token.getType() == TokenType.DECLARE || token.getType() == TokenType.IF || token.getType() == TokenType.ID || token.getType() == TokenType.FOR ||
				token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaBloco();
			token = lexicon.nextToken();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.END)
			token = lexicon.nextToken();
		else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.END_PROG)
			token = lexicon.nextToken();
		else
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
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.ID)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		
		
	}
	
	public void derivaDecl() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.DECLARE)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaCond() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.IF)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	@SuppressWarnings("unused")
	public void derivaCndb() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.BEGIN || token.getType() == TokenType.DECLARE || token.getType() == TokenType.IF || token.getType() == TokenType.ID || token.getType() == TokenType.FOR ||
				token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaBloco();
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.DECLARE || token.getType() != TokenType.IF || token.getType() != TokenType.FOR || token.getType() != TokenType.WHILE ||
					token.getType() != TokenType.ID || token.getType() != TokenType.END || token.getType() != TokenType.END_PROG || token.getType() != TokenType.ELSE)		
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
	}
	
	public void derivaExp() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.LOGIC_VAL || token.getType() != TokenType.ID || token.getType() != TokenType.NUM_INT || token.getType() != TokenType.NUM_FLOAT ||
				token.getType() != TokenType.LITERAL || token.getType() != TokenType.L_PAR)		
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFId() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.LOGIC_OP) {
			lexicon.storeToken(token);
			derivaFValLog();
		} else if (token.getType() == TokenType.ARIT_AS || token.getType() == TokenType.ARIT_MD) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.L_PAR || token.getType() == TokenType.ID || token.getType() == TokenType.NUM_INT || token.getType() == TokenType.NUM_FLOAT) {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaFExpNum1() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.RELOP) {
			lexicon.storeToken(token);
			derivaExpNum();		
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM)	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
			}
		} else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaFNumInt() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.ARIT_AS || token.getType() == TokenType.ARIT_MD) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaFOpNum1();
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM) {	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum1() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.L_PAR || token.getType() == TokenType.ID || token.getType() == TokenType.NUM_INT || token.getType() == TokenType.NUM_FLOAT) {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaFExpNum2() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.RELOP) {
			lexicon.storeToken(token);
			derivaExpNum();		
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM)	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	@SuppressWarnings("unused")
	public void derivaFNumFloat() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.ARIT_AS || token.getType() == TokenType.ARIT_MD) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaFOpNum2();
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM)
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum2() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.L_PAR || token.getType() == TokenType.ID || token.getType() == TokenType.NUM_INT || token.getType() == TokenType.NUM_FLOAT) {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaFExpNum3() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.RELOP) {
			lexicon.storeToken(token);
			derivaExpNum();		
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM)	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());			
			}
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaFLPar() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.L_PAR || token.getType() == TokenType.ID || token.getType() == TokenType.NUM_INT || token.getType() == TokenType.NUM_FLOAT) {
			lexicon.storeToken(token);
			derivaExpNum();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.R_PAR)		
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaFRPar() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.RELOP) {
			lexicon.storeToken(token);
			derivaExpNum();		
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM)	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());			
			}
		} else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaFId1() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.LOGIC_OP) {
			lexicon.storeToken(token);
			derivaFValLog();
		} else if (token.getType() == TokenType.ARIT_AS || token.getType() == TokenType.ARIT_MD) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaExpNum();
		} else if (token.getType() != TokenType.RELOP)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	@SuppressWarnings("unused")
	public void derivaFValLog() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() == TokenType.LOGIC_OP) {
			lexicon.storeToken(token);
			derivaExpLo();
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.TERM || token.getType() != TokenType.R_PAR)	{	
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	@SuppressWarnings("unused")
	public void derivaXExpNum() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.ARIT_AS || token.getType() == TokenType.ARIT_MD) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaExpNum();
		} else if (token == null) {
			lexicon.storeToken(token);
			token = lexicon.nextToken();
			if (token.getType() != TokenType.RELOP || token.getType() != TokenType.TO || token.getType() != TokenType.BEGIN || token.getType() != TokenType.DECLARE ||
					token.getType() != TokenType.IF || token.getType() != TokenType.ID || token.getType() != TokenType.FOR ||
					token.getType() != TokenType.WHILE || token.getType() != TokenType.TERM || token.getType() != TokenType.R_PAR)
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaOpNum() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.ARIT_AS || token.getType() != TokenType.ARIT_MD)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaVal() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.ID || token.getType() != TokenType.NUM_INT || token.getType() != TokenType.NUM_FLOAT)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaRep() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.FOR) {
			lexicon.storeToken(token);
			derivaRepF();
		} else if (token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaRepW();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaRepF() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.FOR)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaExpNum() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.ID || token.getType() == TokenType.NUM_INT || token.getType() == TokenType.NUM_FLOAT) {
			lexicon.storeToken(token);
			derivaVal();
		} else if (token.getType() != TokenType.L_PAR)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	
	public void derivaRepW() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.WHILE)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaExpLo() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() != TokenType.LOGIC_VAL || token.getType() != TokenType.ID || token.getType() != TokenType.NUM_INT || token.getType() != TokenType.NUM_FLOAT ||
				token.getType() != TokenType.L_PAR)
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaBloco() throws IOException {
		Token token = lexicon.nextToken();
		
		if (token.getType() == TokenType.BEGIN)
			token = lexicon.nextToken();
		else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.DECLARE || token.getType() == TokenType.IF || token.getType() == TokenType.FOR || token.getType() == TokenType.WHILE || token.getType() == TokenType.ID) {
			lexicon.storeToken(token);
			derivaCmds();
			token = lexicon.nextToken();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.END)
			token = lexicon.nextToken();
		else 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		if (token.getType() == TokenType.ID || token.getType() == TokenType.DECLARE || token.getType() == TokenType.IF || token.getType() == TokenType.FOR || token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaCmd();
			token = lexicon.nextToken();
		} else
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
}
