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
		
		derivationS();
		
		// PRINTA A TABELA DE SIMBOLOS
		// TabSymbols.getInstance().printTabSymbols();
		// PRINTA OS ERROS GERADOS NA VERIFICACAO, CASO HAJA
		ErrorHandler.getInstance().errorReport();
	}
	
	private void reSyncS() throws IOException {
		try {
			Token token = lexicon.nextToken();
			while ((!FirstFollow.getInstance().isInFirst(NonTerm.CMD, token) && !token.getType().equals(TokenType.BEGIN)) || token.getType().equals(TokenType.EOF)) {
				token = lexicon.nextToken();
			}
			lexicon.storeToken(token);
		} catch (Exception e) {}
	}

	// -> program id term BLOCO end_prog term	
	private void derivationS() throws IOException {
		try {
			boolean hasError = false;
			Token token = lexicon.nextToken();
			if (!FirstFollow.getInstance().isInFirst(NonTerm.S, token)) {
				errors.addErro("Espera 'program'", token.getLexeme(), token.getLin(), token.getCol(), "S");
				if (token.getType().equals(TokenType.EOF))
					return;
				else if (token.getType().equals(TokenType.TERM))
					lexicon.storeToken(token);
			}
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.ID)) {
				errors.addErro("Espera 'ID'", token.getLexeme(), token.getLin(), token.getCol(), "S");
				if (token.getType().equals(TokenType.EOF))
					return;
				else if (token.getType().equals(TokenType.TERM))
					lexicon.storeToken(token);
			}
			verifyIfIdDeclared(token, true);
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.TERM)) {
				errors.addErro("Espera ';'", token.getLexeme(), token.getLin(), token.getCol(), "S");
				if (!token.getType().equals(TokenType.TERM)) {
					lexicon.storeToken(token);
					reSyncS();
					token = lexicon.nextToken();
				}
				if (token.getType().equals(TokenType.EOF))
					return;
				lexicon.storeToken(token);
				derivationBloco();
				hasError = true;
			}
			if (!hasError)
				derivationBloco();
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.END_PROG)) {
				errors.addErro("Espera 'end_prog'", token.getLexeme(), token.getLin(), token.getCol(), "S");
				if (token.getType().equals(TokenType.EOF))
					return;
			}
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.TERM)) {
				errors.addErro("Espera ';'", token.getLexeme(), token.getLin(), token.getCol(), "S");	
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
		} catch (Exception e) {}
	}

	// -> DECL CMDS | COND CMDS | REPF CMDS | REPW CMDS | ATRIB CMDS | ε						
	private void derivationCmds() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.DECL, token)) {
				derivationDecl();
				derivationCmds();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.COND, token)) {
				derivationCond();
				derivationCmds();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.REPF, token)) {
				derivationRepF();
				derivationCmds();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.REPW, token)) {
				derivationRepW();
				derivationCmds();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.ATRIB, token)) {
				verifyIfIdDeclared(token, false);
				derivationAtrib();
				derivationCmds();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.CMDS, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ 'declare|if|for|while|ID|end|ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "CMDS");
		} catch (Exception e) {}
	}
	
	//  -> DECL | COND | REP | ATRIB						
	private void derivationCmd() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.DECL, token)) {
				derivationDecl();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.COND, token)) {
				derivationCond();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.REP, token)) {
				lexicon.storeToken(token);
				derivationRep();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.ATRIB, token)) {
				verifyIfIdDeclared(token, false);
				derivationAtrib();
				return;
			}
			errors.addErro("Espera [ 'declare|if|for|while|ID' ]", token.getLexeme(), token.getLin(), token.getCol(), "CMD");	
		} catch (Exception e) {}
	}
	
	private void reSyncAtrib() throws IOException {
		try {
			Token token = lexicon.nextToken();
			while ((!FirstFollow.getInstance().isInFollow(NonTerm.CMDS, token) && !FirstFollow.getInstance().isInFirst(NonTerm.CMDS, token)) || token.getType().equals(TokenType.EOF)) {
				token = lexicon.nextToken();
			}
			lexicon.storeToken(token);
		} catch (Exception e) {}
	}
	
	//  -> id assign EXP term						
	private void derivationAtrib() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.ASSIGN)) {
				errors.addErro("Espera '<-'", token.getLexeme(), token.getLin(), token.getCol(), "ATRIB");
				lexicon.storeToken(token);
				reSyncAtrib();
				return;
			}
			derivationExp();
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.TERM)) {
				errors.addErro("Espera ';'", token.getLexeme(), token.getLin(), token.getCol(), "ATRIB");
				lexicon.storeToken(token);
				reSyncAtrib();
				return;
			}
		} catch (Exception e) {}
	}
	
	private void verifyIfIdDeclared(Token token, boolean isDeclaration) throws IOException {
		try {
			if (!token.getType().equals(TokenType.ID))
				return;
			if (isDeclaration) {
				if (TabSymbols.getInstance().verifyIfChecked(token)) {
					errors.addErro("Variavel ja declarada", token.getLexeme(), token.getLin(), token.getCol(), "DECL");
					return;
				}
				TabSymbols.getInstance().setDeclared(token);
				return;
			}
			if (!TabSymbols.getInstance().verifyIfChecked(token))
				errors.addErro("Variavel nao foi declarada", token.getLexeme(), token.getLin(), token.getCol(), "DECL");
		} catch (Exception e) {}
	}
	
	private void reSyncDecl() throws IOException {
		try {
			Token token = lexicon.nextToken();
			while ((!FirstFollow.getInstance().isInFollow(NonTerm.CMDS, token) && !FirstFollow.getInstance().isInFirst(NonTerm.CMDS, token)) || token.getType().equals(TokenType.EOF)) {
				token = lexicon.nextToken();
			}
			lexicon.storeToken(token);
		} catch (Exception e) {}
	}
	
	// -> declare id type term						
	private void derivationDecl() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.ID)) {
				errors.addErro("Espera 'ID'", token.getLexeme(), token.getLin(), token.getCol(), "DECL");
				lexicon.storeToken(token);
				reSyncDecl();
				return;
			}
			verifyIfIdDeclared(token, true);
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.TYPE)) {
				errors.addErro("Espera [ 'bool|text|int|float' ]", token.getLexeme(), token.getLin(), token.getCol(), "DECL");
				lexicon.storeToken(token);
				reSyncDecl();
				return;				
			}
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.TERM)) {
				errors.addErro("Espera ';'", token.getLexeme(), token.getLin(), token.getCol(), "DECL");
				lexicon.storeToken(token);
				reSyncDecl();
				return;
			}
		} catch (Exception e) {}
	}
	
	// -> if l_par EXPLO r_par then BLOCO CNDB						
	private void derivationCond() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.L_PAR)) {
				errors.addErro("Espera '('", token.getLexeme(), token.getLin(), token.getCol(), "COND");
				if (token.getType().equals(TokenType.EOF))
					return;
			}
			derivationExpLo();
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.R_PAR)) {
				errors.addErro("Espera ')'", token.getLexeme(), token.getLin(), token.getCol(), "COND");
				if (token.getType().equals(TokenType.EOF))
					return;
			}
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.THEN)) {
				errors.addErro("Espera 'then'", token.getLexeme(), token.getLin(), token.getCol(), "COND");
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
			derivationBloco();
			derivationCndb();
		} catch (Exception e) {}
	}
	
	// -> else BLOCO | ε						
	private void derivationCndb() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.CNDB, token)) {
				derivationBloco();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.CNDB, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ 'else|declare|if|for|while|ID|end|end_prog|else', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "CNDB");
		} catch (Exception e) {}
	}
	
	// -> logic_val FVALLOG | id FID | num_int FNUMINT | num_float FNUMFLOAT | l_par FLPAR | literal						
	private void derivationExp() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (token.getType().equals(TokenType.LOGIC_VAL)) {
				derivationFValLog();
				return;
			}
			if (token.getType().equals(TokenType.ID)) {
				verifyIfIdDeclared(token, false);
				derivationFId();
				return;
			}
			if (token.getType().equals(TokenType.NUM_INT)) {
				derivationFNumInt();
				return;
			}
			if (token.getType().equals(TokenType.NUM_FLOAT)) {
				derivationFNumFloat();
				return;
			}
			if (token.getType().equals(TokenType.L_PAR)) {
				derivationFLPar();
				return;
			}
			if (token.getType().equals(TokenType.LITERAL)) {
				return;
			}
			errors.addErro("Espera [ 'true|false', 'ID', 'n. inteiro', 'n. float', '(', 'texto' ]", token.getLexeme(), token.getLin(), token.getCol(), "EXP");
		} catch (Exception e) {}
	}
	
	// -> FVALLOG | OPNUM FOPNUM						
	private void derivationFId() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
				lexicon.storeToken(token);
				derivationFValLog();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivationOpNum();
				derivationFOpNum();
				return;
			}
			errors.addErro("Espera [ 'and|not|or', '+|-|/|*' ]", token.getLexeme(), token.getLin(), token.getCol(), "FID");
		} catch (Exception e) {}
	}
	
	// -> EXPNUM FEXPNUM_1						
	private void derivationFOpNum() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivationExpNum();
				derivationFExpNum1();
				return;
			}
			errors.addErro("Espera [ '(|ID|n. inteiro|n. float' ]", token.getLexeme(), token.getLin(), token.getCol(), "FOPNUM");
		} catch (Exception e) {}
	}
	
	// -> relop EXPNUM | ε						
	private void derivationFExpNum1() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM1, token)) {	
				derivationExpNum();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM1, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df', ';', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FEXPNUM1");
		} catch (Exception e) {}
	}
	
	// -> OPNUM FOPNUM_1
	// -> ε						
	private void derivationFNumInt() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivationOpNum();
				derivationFOpNum1();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FNUMINT, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '+|-|/|*', ';', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FNUMINT");
		} catch (Exception e) {}
	}
	
	// -> EXPNUM FEXPNUM_2						
	private void derivationFOpNum1() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivationExpNum();
				derivationFExpNum2();
				return;
			}
			errors.addErro("Espera [ '(|ID|n. inteiro|n. float' ]", token.getLexeme(), token.getLin(), token.getCol(), "FOPNUM1");
		} catch (Exception e) {}
	}
	
	// -> relop EXPNUM | ε						
	private void derivationFExpNum2() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM2, token)) {
				derivationExpNum();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM2, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df', ';', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FEXPNUM2");	
		} catch (Exception e) {}
	}
	
	// -> OPNUM FOPNUM_2	
	// -> ε
	private void derivationFNumFloat() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivationOpNum();
				derivationFOpNum2();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FNUMFLOAT, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '+|-|/|*', ';', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FNUMFLOAT");
		} catch (Exception e) {}
	}
	
	// -> EXPNUM FEXPNUM_3						
	private void derivationFOpNum2() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivationExpNum();
				derivationFExpNum3();
				return;
			}
			errors.addErro("Espera [ '(|ID|n. inteiro|n. float' ]", token.getLexeme(), token.getLin(), token.getCol(), "FOPNUM2");
		} catch (Exception e) {}
	}
	
	//  -> relop EXPNUM | ε						
	private void derivationFExpNum3() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM3, token)) {
				derivationExpNum();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FEXPNUM3, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df', ';', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FEXPNUM3");	
		} catch (Exception e) {}
	}

	// -> EXPNUM FEXPNUM						
	private void derivationFLPar() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.EXPNUM, token)) {
				lexicon.storeToken(token);
				derivationExpNum();
				derivationFExpNum();
				return;
			}
			errors.addErro("Espera [ '(|ID|n. inteiro|n. float' ]", token.getLexeme(), token.getLin(), token.getCol(), "FLPAR");
		} catch (Exception e) {}
	}
	
	// -> r_par FRPAR						
	private void derivationFExpNum() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!FirstFollow.getInstance().isInFirst(NonTerm.FEXPNUM, token)) {
				errors.addErro("Espera ')'", token.getLexeme(), token.getLin(), token.getCol(), "FEXPNUM");
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
			derivationFRPar();
		} catch (Exception e) {}
	}
	
	// -> relop EXPNUM | ε						
	private void derivationFRPar() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FRPAR, token)) {
				derivationExpNum();	
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FRPAR, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df', ';', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FRPAR");
		} catch (Exception e) {}
	}

	// -> relop EXPNUM
	// -> FVALLOG | OPNUM EXPNUM relop EXPNUM						
	private void derivationFId1() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (token.getType().equals(TokenType.RELOP)) {
				derivationExpNum();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
				lexicon.storeToken(token);
				derivationFValLog();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivationOpNum();
				derivationExpNum();
				token = lexicon.nextToken();
				if (!token.getType().equals(TokenType.RELOP)) {
					errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df' ]", token.getLexeme(), token.getLin(), token.getCol(), "FID1");
					if (token.getType().equals(TokenType.EOF))
						return;					
				}
				derivationExpNum();
				return;
			}
			errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df', 'and|not|or', '+|-|/|*' ]", token.getLexeme(), token.getLin(), token.getCol(), "FID1");
		} catch (Exception e) {}
	}
	
	// -> logic_op EXPLO | ε						
	private void derivationFValLog() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.FVALLOG, token)) {
				derivationExpLo();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.FVALLOG, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ 'and|not|or', ';|)', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "FVALLOG");
		} catch (Exception e) {}
	}
	
	// -> OPNUM EXPNUM | ε						
	private void derivationXExpNum() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token)) {
				lexicon.storeToken(token);
				derivationOpNum();
				derivationExpNum();
				return;
			}
			if (FirstFollow.getInstance().isInFollow(NonTerm.XEXPNUM, token)) {
				lexicon.storeToken(token);
				return;
			}
			errors.addErro("Espera [ '+|-|/|*', '$lt|$gt|$ge|$le|$eq|$df|to|begin|declare|if|ID|for|while|;|)', 'ε' ]", token.getLexeme(), token.getLin(), token.getCol(), "XEXPNUM");	
		} catch (Exception e) {}
	}
	
	// -> arit_as | arit_md						
	private void derivationOpNum() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!FirstFollow.getInstance().isInFirst(NonTerm.OPNUM, token))
				errors.addErro("Espera [ '+|-|/|*' ]", token.getLexeme(), token.getLin(), token.getCol(), "OPNUM");
		} catch (Exception e) {}
	}
	
	// -> id | num_int | num_float						
	private void derivationVal() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!FirstFollow.getInstance().isInFirst(NonTerm.VAL, token))
				errors.addErro("Espera [ 'ID|n. inteiro|n. float' ]", token.getLexeme(), token.getLin(), token.getCol(), "VAL");
			verifyIfIdDeclared(token, false);
		} catch (Exception e) {}
	}
	
	//  -> REPF | REPW						
	private void derivationRep() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.REPF, token)) {
				derivationRepF();
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.REPW, token)) {
				derivationRepW();
				return;
			}
			errors.addErro("Espera [ 'for|while' ]", token.getLexeme(), token.getLin(), token.getCol(), "REP");
		} catch (Exception e) {}
	}
	
	// -> for id attrib EXPNUM to EXPNUM BLOCO						
	private void derivationRepF() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.ID))
				errors.addErro("Espera 'ID'", token.getLexeme(), token.getLin(), token.getCol(), "REPF");
			verifyIfIdDeclared(token, false);
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.ASSIGN)) {
				errors.addErro("Espera '<-'", token.getLexeme(), token.getLin(), token.getCol(), "REPF");
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
			derivationExpNum();
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.TO)) {
				errors.addErro("Espera 'to'", token.getLexeme(), token.getLin(), token.getCol(), "REPF");
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
			derivationExpNum();
			derivationBloco();
		} catch (Exception e) {}
	}
	
	// -> VAL XEXPNUM | l_par EXPNUM r_par	
	private void derivationExpNum() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (FirstFollow.getInstance().isInFirst(NonTerm.VAL, token)) {
				lexicon.storeToken(token);
				derivationVal();
				derivationXExpNum();
				return;
			}
			if (token.getType().equals(TokenType.L_PAR)) {
				derivationExpNum();
				token = lexicon.nextToken();
				if (!token.getType().equals(TokenType.R_PAR))
					errors.addErro("Espera ')'", token.getLexeme(), token.getLin(), token.getCol(), "EXPNUM");
				return;
			}
			errors.addErro("Espera [ 'ID|n. inteiro|n. float', '(' ]", token.getLexeme(), token.getLin(), token.getCol(), "EXPNUM");
		} catch (Exception e) {}
	}
	
	// -> while l_par EXPLO r_par BLOCO						
	private void derivationRepW() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.L_PAR)) {
				errors.addErro("Espera '('", token.getLexeme(), token.getLin(), token.getCol(), "REPW");
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
			derivationExpLo();
			token = lexicon.nextToken();
			if (!token.getType().equals(TokenType.R_PAR)) {
				errors.addErro("Espera ')'", token.getLexeme(), token.getLin(), token.getCol(), "REPW");
				if (token.getType().equals(TokenType.EOF))
					return;				
			}
			derivationBloco();
		} catch (Exception e) {}
	}
	
	// -> logic_val FVALLOG | id FID_1 | num_int OPNUM EXPNUM relop EXPNUM | num_float OPNUM EXPNUM relop EXPNUM | l_par EXPNUM r_par relop EXPNUM						
	private void derivationExpLo() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (token.getType().equals(TokenType.LOGIC_VAL)) {
				derivationFValLog();
				return;
			}
			if (token.getType().equals(TokenType.ID)) {
				verifyIfIdDeclared(token, false);
				derivationFId1();
				return;
			}
			if (token.getType().equals(TokenType.NUM_INT)) {
				derivationOpNum();
				derivationExpNum();
				token = lexicon.nextToken();
				if (!token.getType().equals(TokenType.RELOP)) {
					errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df' ]", token.getLexeme(), token.getLin(), token.getCol(), "EXPLO");
					if (token.getType().equals(TokenType.EOF))
						return;					
				}
				derivationExpNum();
				return;
			} 
			if (token.getType().equals(TokenType.NUM_FLOAT)) {
				derivationOpNum();
				derivationExpNum();
				token = lexicon.nextToken();
				if (!token.getType().equals(TokenType.RELOP)) {
					errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df' ]", token.getLexeme(), token.getLin(), token.getCol(), "EXPLO");
					if (token.getType().equals(TokenType.EOF))
						return;					
				}
				derivationExpNum();
				return;
			}
			if (token.getType().equals(TokenType.L_PAR)) {
				derivationExpNum();
				token = lexicon.nextToken();
				if (!token.getType().equals(TokenType.R_PAR)) {
					errors.addErro("Espera ')'", token.getLexeme(), token.getLin(), token.getCol(), "EXPLO");
					if (token.getType().equals(TokenType.EOF))
						return;					
				}
				token = lexicon.nextToken();	
				if (!token.getType().equals(TokenType.RELOP)) {
					errors.addErro("Espera [ '$lt|$gt|$ge|$le|$eq|$df' ]", token.getLexeme(), token.getLin(), token.getCol(), "EXPLO");
					if (token.getType().equals(TokenType.EOF))
						return;					
				}
				derivationExpNum();
				return;
			}
			errors.addErro("Espera [ 'true|false', 'ID', 'n. inteiro', 'n. float', '(' ]", token.getLexeme(), token.getLin(), token.getCol(), "EXPLO");
		} catch (Exception e) {}
	}

	// -> begin CMDS end | CMD					
	private void derivationBloco() throws IOException {
		try {
			Token token = lexicon.nextToken();
			if (token.getType().equals(TokenType.BEGIN)) {
				derivationCmds();
				token = lexicon.nextToken();
				if (!token.getType().equals(TokenType.END))
					errors.addErro("Espera 'end'", token.getLexeme(), token.getLin(), token.getCol(), "BLOCO");
				return;
			}
			if (FirstFollow.getInstance().isInFirst(NonTerm.CMD, token)) {
				lexicon.storeToken(token);
				derivationCmd();
				return;
			}
			errors.addErro("Espera [ 'begin|declare|if|ID|for|while' ]", token.getLexeme(), token.getLin(), token.getCol(), "BLOCO");
		} catch (Exception e) {}
	}
}
