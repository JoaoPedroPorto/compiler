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
	
	private void reSyncS() throws IOException {
		Token token = lexicon.nextToken();
		while (!token.getType().equals(TokenType.TERM) && !token.getType().equals(TokenType.BEGIN) || token.getType().equals(TokenType.EOF)) {
			token = lexicon.nextToken();
		}
		lexicon.storeToken(token);
	}

	// -> program id term BLOCO end_prog term						
	public void derivaS() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.S, token)) {
			token = lexicon.nextToken();
		} else { // TENTAR SE RECUPERAR
			errors.addErro("Espera 'program'", token.getLexeme(), token.getLin(), token.getCol());
			reSyncS();
			token = lexicon.nextToken();
			if (token.getType().equals(TokenType.EOF)) {
				return;
			}
			derivaBloco();
		}
		if (token.getType() == TokenType.ID) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType() == TokenType.TERM) {
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
	
	// -> DECL CMDS | COND CMDS | REPF CMDS | REPW CMDS | ATRIB CMDS | ε						
	public void derivaCmds() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.DECLARE)) {
			derivaDecl();
			derivaCmds();
			return;
		}
		if (token.getType().equals(TokenType.IF)) {
			derivaCond();
			derivaCmds();
			return;
		}
		if (token.getType().equals(TokenType.FOR)) {
			derivaRepF();
			derivaCmds();
			return;
		}
		if (token.getType().equals(TokenType.WHILE)) {
			lexicon.storeToken(token);
			derivaRepW();
			derivaCmds();
			return;
		}
		if (token.getType().equals(TokenType.ID)) {
			// lexicon.storeToken(token);
			derivaAtrib();
			derivaCmds();
			return;
		}
		if (FirstFollow.getInstance().isInFollow(NonTerm.CMDS, token)) {
			lexicon.storeToken(token);
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	//  -> DECL | COND | REP | ATRIB						
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
			derivaAtrib();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	//  -> id assign EXP term						
	public void derivaAtrib() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.ASSIGN)) {
			derivaExp();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (token.getType().equals(TokenType.TERM)) {
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	public void derivaDecl() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.ID)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType().equals(TokenType.TYPE)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType().equals(TokenType.TERM)) {
			return;
		}
		// TENTAR SE RECUPERAR 
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	// -> if l_par EXPLO r_par then BLOCO CNDB						
	public void derivaCond() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.L_PAR)) {
			derivaExpLo();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
		}
		if (token.getType().equals(TokenType.R_PAR)) {
			token = lexicon.nextToken();	
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType().equals(TokenType.THEN)) {
			derivaBloco();
			derivaCndb();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
	}
	
	// -> else BLOCO | ε						
	public void derivaCndb() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.CNDB, token)) {
			derivaBloco();
			return;
		}
		if (FirstFollow.getInstance().isInFollow(NonTerm.CNDB, token)) {
			lexicon.storeToken(token);
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	// -> logic_val FVALLOG | id FID | num_int FNUMINT | num_float FNUMFLOAT | l_par FLPAR | literal						
	public void derivaExp() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.LOGIC_VAL)) {
			derivaFValLog();
			return;
		}
		if (token.getType().equals(TokenType.ID)) {
			derivaFId();
			return;
		}
		if (token.getType().equals(TokenType.NUM_INT)) {
			derivaFNumInt();
			return;
		}
		if (token.getType().equals(TokenType.NUM_FLOAT)) {
			derivaFNumFloat();
			return;
		}
		if (token.getType().equals(TokenType.L_PAR)) {
			derivaFLPar();
			return;
		}
		if (token.getType().equals(TokenType.LITERAL)) {
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	//  -> FVALLOG | OPNUM FOPNUM						
	public void derivaFId() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
			derivaFValLog();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			lexicon.storeToken(token);
			derivaOpNum();
			derivaFOpNum();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	//  -> EXPNUM FEXPNUM_1						
	public void derivaFOpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
			derivaFExpNum1();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	//  -> relop EXPNUM | ε						
	public void derivaFExpNum1() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM1, token)) {	
			derivaExpNum();
			return;
		}
		if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM1, token)) {
			lexicon.storeToken(token);
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	//  -> OPNUM FOPNUM_1						
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
			return;
		}
		if (FirstFollow.getInstance().isInFollow(NonTerm.FNUMINT, token)) {
			lexicon.storeToken(token);
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
	
	// -> FVALLOG | OPNUM EXPNUM relop EXPNUM						
	public void derivaFId1() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
			lexicon.storeToken(token);
			derivaFValLog();
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
				// token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	// -> logic_op EXPLO | ε						
	public void derivaFValLog() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
			derivaExpLo();
			return;
		}
		if (FirstFollow.getInstance().isInFollow(NonTerm.FVALLOG, token)) {
			lexicon.storeToken(token);
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
				// token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		} else if (FirstFollow.getInstance().isInFollow(NonTerm.XEXPNUM, token)) {
			lexicon.storeToken(token);
			// token = lexicon.nextToken();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	// -> arit_as | arit_md						
	public void derivaOpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());		
	}
	
	public void derivaVal() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.VAL, token)) {
			// token = lexicon.nextToken();
			// lexicon.storeToken(token);
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	//  -> REPF | REPW						
	public void derivaRep() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.REPF, token)) {
			derivaRepF();
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.REPW, token)) {
			derivaRepW();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());	
	}
	
	//  -> for id attrib EXPNUM to EXPNUM BLOCO						
	public void derivaRepF() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.ID)) {
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType().equals(TokenType.ASSIGN)) {
			derivaExpNum();
			token = lexicon.nextToken();
		} else {
			// TENTAR SE RECUPERAR 
			errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
		}
		if (token.getType().equals(TokenType.TO)) {
			derivaExpNum();
			derivaBloco();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
	
	// -> VAL XEXPNUM | l_par EXPNUM r_par	
	public void derivaExpNum() throws IOException {
		Token token = lexicon.nextToken();
		if (FirstFollow.getInstance().isInFirst(NonTerm.VAL, token)) {
			lexicon.storeToken(token);
			derivaVal();
			derivaXExpNum();
			return;
		}
		if (token.getType().equals(TokenType.L_PAR)) {
			derivaExpNum();
			token = lexicon.nextToken();
			if (token.getType().equals(TokenType.R_PAR)) {
				return;
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
	
	// -> logic_val FVALLOG | id FID_1 | num_int OPNUM EXPNUM relop EXPNUM | num_float OPNUM EXPNUM relop EXPNUM | l_par EXPNUM r_par relop EXPNUM						
	public void derivaExpLo() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.LOGIC_VAL)) {
			derivaFValLog();
			return;
		}
		if (token.getType().equals(TokenType.ID)) {
			derivaFId1();
			return;
		}
		if (token.getType().equals(TokenType.NUM_INT)) {
			derivaOpNum();
			derivaExpNum();
			token = lexicon.nextToken();
			if (token.getType().equals(TokenType.RELOP)) {
				derivaExpNum();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		} 
		if (token.getType().equals(TokenType.NUM_FLOAT)) {
			derivaOpNum();
			derivaExpNum();
			token = lexicon.nextToken();
			if (token.getType().equals(TokenType.RELOP)) {
				derivaExpNum();
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		if (token.getType().equals(TokenType.L_PAR)) {
			derivaExpNum();
			token = lexicon.nextToken();
			if (token.getType().equals(TokenType.R_PAR)) {
				token = lexicon.nextToken();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			if (token.getType().equals(TokenType.RELOP)) {
				derivaExpNum();
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol()); 
	}

	// -> begin CMDS end | CMD						
	public void derivaBloco() throws IOException {
		Token token = lexicon.nextToken();
		if (token.getType().equals(TokenType.BEGIN)) {
			derivaCmds();
			token = lexicon.nextToken();
			if (token.getType().equals(TokenType.END)) {
				return;
			} else {
				// TENTAR SE RECUPERAR
				errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
			}
			return;
		}
		if (FirstFollow.getInstance().isInFirst(NonTerm.CMD, token)) {
			lexicon.storeToken(token);
			derivaCmd();
			return;
		}
		// TENTAR SE RECUPERAR
		errors.addErro("Comando invalido", token.getLexeme(), token.getLin(), token.getCol());
	}
}
