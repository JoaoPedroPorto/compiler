package compiler;

import java.io.EOFException;
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

public class Lexicon {

	@SuppressWarnings("unused")
	private String fileName;
	private FileLoader fLoader;
	private StringBuilder lexeme;
	private char character;
	private long line;
	private long column;
	private Token token;
	private TabSymbols tabSymbols = TabSymbols.getInstance();
	private ErrorHandler errors = ErrorHandler.getInstance();
	private Token buffer;

	public Lexicon(String fileName) throws IOException {
		this.fileName = fileName;
		fLoader = new FileLoader(fileName);
	}

	public Token nextToken() throws IOException {
		
		if (buffer != null) {
			Token token = buffer;
			buffer = null;
			return token;
		}
		
		lexeme = new StringBuilder();
		line = fLoader.getLine();
		column = fLoader.getColumn();

		// CASO SEJA O FIM DO ARQUIVO, RETORNA TOKEN EOF
		try {
			character = fLoader.getNextChar();
		} catch (Exception ex) {
			return  tokenEOF();
		}

		// VERIFICA SE O CARACTER GERADO CONTER ESPACO
		// CASO SEJA ELE PEDE NOVO TOKEN ATE QUE ESSA CONDICAO NAO SEJA MAIS VERDADE
		// TAMBEM RETORNA TOKEN EOF CASO O PROXIMO TOKEN SEJA O FIM DO ARQUIVO
		while (Character.isWhitespace(character)) {
			try {
				character = fLoader.getNextChar();
			} catch (Exception e) {
				return  tokenEOF();
			}
		}

		// VERIFICA O CARACTER OBTIDO E RETONA O TOKEN GERADO AO FINAL, CASO O MESMO SEJA UM CARACTER VALIDO
		switch (character) {
			case '+':
				lexeme.append(character);
				token = new Token(TokenType.ARIT_AS, lexeme.toString(), line, column);
				break;
			case '-':
				lexeme.append(character);
				token = new Token(TokenType.ARIT_AS, lexeme.toString(), line, column);
				break;
			case '*':
				lexeme.append(character);
				token = new Token(TokenType.ARIT_MD, lexeme.toString(), line, column);
				break;
			case '/':
				lexeme.append(character);
				token = new Token(TokenType.ARIT_MD, lexeme.toString(), line, column);
				break;
			case '<':
				token = processAssign();
				break;
			case ';':
				lexeme.append(character);
				token = new Token(TokenType.TERM, lexeme.toString(), line, column);
				break;
			case '(':
				lexeme.append(character);
				token = new Token(TokenType.L_PAR, lexeme.toString(), line, column);
				break;
			case ')':
				lexeme.append(character);
				token = new Token(TokenType.R_PAR, lexeme.toString(), line, column);
				break;
			case '$':
				token = processRelop();
				break;
			case '\"':
				token = processLiteral();
				break;
			case '\'':
				token = processLiteral();
				break;
			case '{':
				ignoreComments();
				token = this.nextToken();
				break;
			default:
				if (Character.isDigit(character)) {
					token = processNumIntAndNumFloat();
					break;
				}
				if (Character.isLetter(character) || character == '_') {
					token = processId();
					break;
				}
				lexeme.append(character);
				errors.addErro("Caracter invalido: ", lexeme.toString(), line, column);
				this.nextToken();
		}
		return token;
	}
	
	private Token processLiteral() throws IOException {
		try {
			if (character == '\'') {
				errors.addErro("Erro Literal: Caractere ' \' ' nao e aceito para literal.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			do {
				lexeme.append(character);
				character = fLoader.getNextChar();
			} while(character != '\"');
			lexeme.append(character);
			if (character != '\"') {
				errors.addErro("Erro Literal: Faltando fechar aspas com '\"'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (EOFException e) {
			if (character != '\"' || lexeme.toString().length() == 1) {
				errors.addErro("Erro Literal: Faltando fechar aspas com '\"'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		}
		return new Token(TokenType.LITERAL, lexeme.toString(), line, column);
	}
	
	private Token processId() throws IOException {
		try {
			do {
				lexeme.append(character);
				character = fLoader.getNextChar();
			} while(Character.isLetter(character) || Character.isDigit(character) || character == '_');
			if (!Character.isLetter(character) && !Character.isDigit(character) && character != '_') {
				lexeme.append(character);
				errors.addErro("Erro validacao Id: Nao e possivel gerar um id com a seguinte sequencia '" + lexeme.toString() + "'", lexeme.toString(), line, column);
				return this.nextToken();
			}
			fLoader.resetLastChar();
		} catch (EOFException e) {
			if (!Character.isLetter(character) && !Character.isDigit(character) && character != '_') {
				lexeme.append(character);
				errors.addErro("Erro validacao Id: Nao e possivel gerar um id com a seguinte sequencia '" + lexeme.toString() + "'", lexeme.toString(), line, column);
				return this.nextToken();
			}
		}
		return tabSymbols.addID(lexeme.toString(), line, column);
	}
	
	private Token processNumIntAndNumFloat() throws IOException {
		TokenType tokenType = TokenType.NUM_INT;
		try {
			while (Character.isDigit(character)) {
				lexeme.append(character);
				character = fLoader.getNextChar();
			}
			if (character == '.') {
				tokenType = TokenType.NUM_FLOAT;
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (!Character.isDigit(character)) {
					lexeme.append(character);
					errors.addErro("Erro Literal Numerico Decimal: Espera 'digito' depois do '.'", lexeme.toString(), line, column);
					return this.nextToken();
				}
				while (Character.isDigit(character)) {
					lexeme.append(character);
					character = fLoader.getNextChar();
				}
			}
			if (character == 'E' || character == 'e') {
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (character != '-' && character != '+') {
					lexeme.append(character);
					errors.addErro("Erro Literal Numerico: Espera ['-', '+'] depois do ['E', 'e']", lexeme.toString(), line, column);
					return this.nextToken();
				}
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (!Character.isDigit(character)) {
					lexeme.append(character);
					errors.addErro("Erro Literal Numerico: Espera 'digito' depois do ['-', '+']", lexeme.toString(), line, column);
					return this.nextToken();
				}
				while (Character.isDigit(character)) {
					lexeme.append(character);
					character = fLoader.getNextChar();
				}
			}
			if (Character.isLetter(character)) {
				lexeme.append(character);
				errors.addErro("Erro Literal Numerico: Caractere '" + character + "' nao e valido", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (EOFException e) {
			try {
				Double.parseDouble(lexeme.toString());
			} catch (Exception e1) {
				errors.addErro("Erro Literal Numerico: Nao e possivel gerar um numero com a seguinte sequencia '" + lexeme.toString() + "'", lexeme.toString(), line, column);
				return this.nextToken();
			}
		}
		return new Token(tokenType, lexeme.toString(), line, column);
	}

	private Token processAssign() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != '-') {
				lexeme.append(character);
				errors.addErro("Erro Atribuicao: Espera '-' no lugar de '" + lexeme.toString() + "'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (Exception e) {
			if (lexeme.toString().length() != 2) {
				errors.addErro("Erro Atribuicao: Sequencia de caracteres nao formam uma atribuicao.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		}
		lexeme.append(character);
		return  new Token(TokenType.ASSIGN, lexeme.toString(), line, column);
	}
	
	private Token processRelop() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != 'l' && character != 'g' && character != 'e' && character != 'd') {
				lexeme.append(character);
				errors.addErro("Erro Operador Logico: Espera [ 'l', 'g', 'e', 'd' ] no lugar de '" + lexeme.toString() + "'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != 't' && character != 'e' && character != 'q' && character != 'f') {
				lexeme.append(character);
				errors.addErro("Erro Operador Logico: Espera [ 't', 'e', 'q', 'f' ] no lugar de '" + lexeme.toString() + "'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			if ((character == 't' || character == 'e')
					&& (lexeme.charAt(lexeme.length() - 1) != 'l' && lexeme.charAt(lexeme.length() - 1) != 'g')) {
				lexeme.append(character);
				errors.addErro("Erro Operador Logico: Espera [ 'l', 'g' ] no lugar de '" + lexeme.toString() + "'.", lexeme.toString(), line, column);
				return this.nextToken();
			} else if (character == 'q' && lexeme.charAt(lexeme.length() - 1) != 'e') {
				lexeme.append(character);
				errors.addErro("Erro Operador Logico: Espera [ 'e' ] no lugar de '" + lexeme.toString() + "'.", lexeme.toString(), line, column);
				return this.nextToken();
			} else if (character == 'f' && lexeme.charAt(lexeme.length() - 1) != 'd') {
				lexeme.append(character);
				errors.addErro("Erro Operador Logico: Espera [ 'd' ] no lugar de '" + lexeme.toString() + "'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (EOFException e) {
			if (lexeme.toString().length() != 3) {
				errors.addErro("Erro Operador Logico: Caracteres informados nao forma um operador valido.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		}
		lexeme.append(character);
		return new Token(TokenType.RELOP, lexeme.toString(), line, column);
	}

	public void storeToken(Token token) {
		this.buffer = token; 
	}
	

	private void ignoreComments() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != '#') {
				lexeme.append(character);
				errors.addErro("Erro Comentario: Espera {# comentario #}.", lexeme.toString(), line, column);
				return;
			}
			do {
				lexeme.append(character);
				character = fLoader.getNextChar();
			} while(character != '#');
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != '}') {
				errors.addErro("Erro Comentario: Espera {# comentario #}.", lexeme.toString(), line, column);
				return;
			}
		} catch (EOFException e) {
			errors.addErro("Erro Comentario: Espera {# comentario #}.", lexeme.toString(), line, column);
		}
	}

	// RETORNA UM TOKEN DE FIM DE ARQUIVO
	private Token tokenEOF(){
		return new Token(TokenType.EOF, "");
	}

}
