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
	// private First _first = First.getInstance();
	
	// INTANCIA O ANALISADOR LEXICO E PASSA NO CONSTRUTOR O ARQUIVO A SER VERIFICADO
	public Syntactic(String fileName) throws IOException {
		this.lexicon = new Lexicon(fileName);
	}
	
	public void process() throws IOException {
		/*int id = 1;
		// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
		Token token = lexicon.nextToken();
		// PRINTA OS TOKENS GERADOS
		System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                   TOKENS GERADOS                                                                   ");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%5s %15s %20s %60s", "#", "POS(x,y)", "LEXEMA", "TOKEN");
        System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
		// token.printToken(id);
		// CASO O 1 TOKEN GERADO PELO LEXICO NAO SEJA O FIM DO ARQUIVO O ANALISADOR SINTATICO 
		// FICA PEDINDO TOKENS PARA O LEXICO ATE QUE CHEGUE UM TOKEN QUE SEJA O FIM DO ARQUIVO
		while (token.getType() != TokenType.EOF) {
			// PRINTA O TOKEN GERADO
			token.printToken(id);
			id++;
			// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
			token = lexicon.nextToken();
		}*/
		
		derivaS();
		
		// PRINTA A TABELA DE SIMBOLOS
		// TabSymbols.getInstance().printTabSymbols();
		// PRINTA OS ERROS GERADOS NA VERIFICACAO, CASO HAJA
		ErrorHandler.getInstance().errorReport();
	}
	
	/*private void reSyncFollow(NonTerm nt) {
		try {
			Token token = null;
			do {
				token = lexicon.nextToken();
			} while (!FirstFollow.getInstance().isInFollow(nt, token));
			lexicon.storeToken(token);
		} catch (IOException e) {
		}
	}
	
	private void reSyncFirst(NonTerm nt) {
		try {
			Token token = null;
			do {
				token = lexicon.nextToken();
			} while (!FirstFollow.getInstance().isInFirst(nt, token));
			lexicon.storeToken(token);
		} catch (IOException e) {
		}
	}*/

	public void derivaS() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.S, token)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.ID) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TERM) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.BLOCO, token)) {
			lexicon.storeToken(token);
			derivaBloco();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.END_PROG) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TERM) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
	}
	
	
	
	
	public void derivaCmds() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() == TokenType.DECLARE) {
			lexicon.storeToken(token);
			derivaDecl();
			derivaCmds();
			return;
		}
		if (token.getType() == TokenType.IF) {
			lexicon.storeToken(token);
			derivaCond();
			derivaCmds();
			return;
		}
		if (token.getType() == TokenType.FOR) {
			lexicon.storeToken(token);
			derivaRepF();
			derivaCmds();
			return;
		}
		if (token.getType() == TokenType.WHILE) {
			lexicon.storeToken(token);
			derivaRepW();
			derivaCmds();
			return;
		}
		if (token.getType() == TokenType.ID) {
			lexicon.storeToken(token);
			derivaAtrib();
			derivaCmds();
			return;
		}
		if (FirstFollow.getInstance().isInFollow(NonTerm.CMDS, token)) {
			token = lexicon.nextToken();
			lexicon.storeToken(token);
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaCmd() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.DECL, token)) {
			lexicon.storeToken(token);
			derivaDecl();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.COND, token)) {
			lexicon.storeToken(token);
			derivaCond();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.REP, token)) {
			lexicon.storeToken(token);
			derivaRep();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.ATRIB, token)) {
			lexicon.storeToken(token);
			derivaAtrib();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaAtrib() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.ATRIB, token)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (token.getType() == TokenType.ASSIGN) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXP, token)) {
			lexicon.storeToken(token);
			derivaExp();
			token = lexicon.nextToken();
			lexicon.storeToken(token);
			return;
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TERM) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
	}
	
	public void derivaDecl() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.DECL, token)) {
			token = lexicon.nextToken();
		} else { 
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.ID) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TYPE) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TERM) {
			return;
		}
		// TENTAR SE RECUPERAR 
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaCond() throws IOException {
		Token token = lexicon.nextToken();
		
		if (FirstFollow.getInstance().isInFirst(NonTerm.COND, token)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (token.getType() == TokenType.L_PAR) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPLO, token)) {
			lexicon.storeToken(token);
			derivaExpLo();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.R_PAR) {
			token = lexicon.nextToken();	
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.THEN) {
			token = lexicon.nextToken();	
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.BLOCO, token)) {
			lexicon.storeToken(token);
			derivaBloco();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.CNDB, token)) {
			lexicon.storeToken(token);
			derivaCndb();
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR 
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaCndb() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.CNDB, token)) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.BLOCO, token)) {
				lexicon.storeToken(token);
				derivaBloco();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.CNDB, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		lexicon.storeToken(token);
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaExp() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() == TokenType.LOGIC_VAL) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)){
				lexicon.storeToken(token);
				derivaFValLog();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
			}
			return;
		}
		if (token.getType() == TokenType.ID) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FID, token)) {
				lexicon.storeToken(token);
				derivaFId();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
			}
			return;
		}
		if (token.getType() == TokenType.NUM_INT) {
			derivaFNumInt();
			return;
		}
		if (token.getType() == TokenType.NUM_FLOAT) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FNUMFLOAT, token)) {
				lexicon.storeToken(token);
				derivaFNumFloat();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		if (token.getType() == TokenType.L_PAR) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FLPAR, token)) {
				lexicon.storeToken(token);
				derivaFLPar();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		if (token.getType() == TokenType.LITERAL) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFId() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
			lexicon.storeToken(token);
			derivaFValLog();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			lexicon.storeToken(token);
			derivaOpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FOPNUM, token)) {
				lexicon.storeToken(token);			
				derivaFOpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
	}
	
	public void derivaFOpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
			lexicon.storeToken(token);
			derivaExpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM1, token)) {
				lexicon.storeToken(token);
				derivaFExpNum1();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
	}
	
	public void derivaFExpNum1() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM1, token)) {	
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM1, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFNumInt() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			lexicon.storeToken(token);
			derivaOpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FOPNUM1, token)){
				lexicon.storeToken(token);
				derivaFOpNum1();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FNUMINT, token)) {
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum1() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
			lexicon.storeToken(token);
			derivaExpNum();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM2, token)){
			lexicon.storeToken(token);
			derivaFExpNum2();
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum2() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM2, token)) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM2, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFNumFloat() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			lexicon.storeToken(token);
			derivaOpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FOPNUM2, token)) {
				lexicon.storeToken(token);
				derivaFOpNum2();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FNUMFLOAT, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFOpNum2() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
			lexicon.storeToken(token);
			derivaExpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM3, token)){
				lexicon.storeToken(token);
				derivaFExpNum3();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum3() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM3, token)) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM3, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFLPar() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FLPAR, token)) {
			lexicon.storeToken(token);
			derivaExpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM, token)) {
				lexicon.storeToken(token);
				derivaFExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFExpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM, token)) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FRPAR, token)) {
				lexicon.storeToken(token);
				derivaFRPar();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFRPar() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FRPAR, token)) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();	
				token = lexicon.nextToken();			
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FRPAR, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFId1() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
			lexicon.storeToken(token);
			derivaFValLog();
			token = lexicon.nextToken();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			lexicon.storeToken(token);
			derivaOpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);			
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.RELOP) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		if (token.getType() == TokenType.RELOP) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaFValLog() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPLO, token)) {
				lexicon.storeToken(token);
				derivaExpLo();
				token = lexicon.nextToken();
				return;
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.FVALLOG, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaXExpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			lexicon.storeToken(token);
			derivaOpNum();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.XEXPNUM, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaOpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());		
	}
	
	public void derivaVal() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.VAL, token)) {
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaRep() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.REPF, token)) {
			lexicon.storeToken(token);
			derivaRepF();
			token = lexicon.nextToken();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.REPW, token)) {
			lexicon.storeToken(token);
			derivaRepW();
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaRepF() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.REPF, token)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.ID) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.ASSIGN) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
			lexicon.storeToken(token);
			derivaExpNum();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TO) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
			lexicon.storeToken(token);
			derivaExpNum();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.BLOCO, token)) {
			lexicon.storeToken(token);
			derivaBloco();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
	}
	
	public void derivaExpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.VAL, token)) {
			lexicon.storeToken(token);
			derivaVal();
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.XEXPNUM, token)) {
				lexicon.storeToken(token);
				derivaXExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
			return;
		}
		if (token.getType() == TokenType.L_PAR) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.R_PAR) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaRepW() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.REPW, token)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (token.getType() == TokenType.L_PAR) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPLO, token)) {
				lexicon.storeToken(token);
				derivaExpLo();
				token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (token.getType() == TokenType.R_PAR) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.BLOCO, token)) {
			lexicon.storeToken(token);
			derivaBloco();
			token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	public void derivaExpLo() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() == TokenType.LOGIC_VAL) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)){
				lexicon.storeToken(token);
				derivaFValLog();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;
			}
			return;
		}
		if (token.getType() == TokenType.ID) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FID1, token)){
				lexicon.storeToken(token);
				derivaFId1();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
				return;	
			}
			return;
		}
		if (token.getType() == TokenType.NUM_INT) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivaOpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.RELOP) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		} 
		if (token.getType() == TokenType.NUM_FLOAT) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivaOpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.RELOP) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		if (token.getType() == TokenType.L_PAR) {
			token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.R_PAR) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.RELOP) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol()); 
	}

	public void derivaBloco() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType() == TokenType.BEGIN) {
			token = lexicon.nextToken();
			if (token == null || FirstFollow.getInstance().isInFirst(NonTerm.CMDS, token)) {
				lexicon.storeToken(token);
				derivaCmds();
				//TODO: VER token = lexicon.nextToken();
				return;
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType() == TokenType.END) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.CMD, token)) {
			lexicon.storeToken(token);
			derivaCmd();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
	}
}
