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

	public Lexicon(String fileName) throws IOException {
		this.fileName = fileName;
		fLoader = new FileLoader(fileName);
	}

	public Token nextToken() throws IOException {
		lexeme = new StringBuilder();
		line = fLoader.getLine();
		column = fLoader.getColumn();

		// CASO SEJA O FIM DO ARQUIVO, RETORNA TOKEN EOF
		try {
			character = fLoader.getNextChar();
		} catch (Exception ex) {
			return  token_EOF();
		}

		// VERIFICA SE O CARACTER GERADO CONTER ESPACO
		// CASO SEJA ELE PEDE NOVO TOKEN ATE QUE ESSA CONDICAO NAO SEJA MAIS VERDADE
		// TAMBEM RETORNA TOKEN EOF CASO O PROXIMO TOKEN SEJA O FIM DO ARQUIVO
		while (Character.isWhitespace(character)) {
			try {
				character = fLoader.getNextChar();
			} catch (Exception e) {
				return  token_EOF();
			}
		}

		// VERIFICA O CARACTER OBTIDO E RETONA O TOKEN GERADO AO FINAL, CASO O MESMO SEJA UM CARACTER VALIDO
		switch (character) {
			case '+':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '-':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '*':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '/':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '<':
				token = processAssign();
				break;
			case ';':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '(':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case ')':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
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
					token = processNumInt();
					break;
				}
				if (Character.isLetter(character) || character == '_') {
					token = processId();
					break;
				}
				lexeme.append(character);
				errors.addErro("Caracter inválido: ", lexeme.toString(), line, column);
				this.nextToken();
		}
		return token;
	}
	private Token processLiteral() throws IOException {
		try {
			lexeme.append(character);
			boolean hasLiteral = false;
			if (lexeme.charAt(lexeme.length() - 1) == '\'') {
				lexeme.append(character);
				errors.addErro("Caracter invalido para literal. Caractere ' \' ' nao e aceito para literal", lexeme.toString(), line, column);
				return this.nextToken();
			}
			do {
				character = fLoader.getNextChar();
				if (character == '\"')
					hasLiteral = true;
			} while(character != '\"');
			if (hasLiteral) {
				lexeme.append(character);
				return new Token(TokenType.LITERAL, lexeme.toString(), line, column);
			}
			lexeme.append(character);
			errors.addErro("Caracter invalido para literal. Faltando fechar aspas com '\"'.", lexeme.toString(), line, column);
			return this.nextToken();
		} catch (EOFException e) {
			lexeme.append(character);
			errors.addErro("Finalizado durante validacao de literal. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}
	
	private Token processId() throws IOException {
		try {
			lexeme.append(character);
			boolean hasId = true;
			do {
				character = fLoader.getNextChar();
				if (!Character.isLetter(character) && !Character.isDigit(character) && character != '_') {
					hasId = false;
					break;
				}
				if (character == '_' && 
						(Character.isLetter(lexeme.charAt(lexeme.length() - 1)) || Character.isDigit(lexeme.charAt(lexeme.length() - 1)))) {
					lexeme.append(character);
					errors.addErro("Caracter invalido para id. Encontrado letra antes de '_'.", lexeme.toString(), line, column);
					return this.nextToken();
				}
				lexeme.append(character);
			} while(Character.isLetter(character) || Character.isDigit(character) || character == '_');
			fLoader.resetLastChar();
			if (hasId) {
				return tabSymbols.addID(lexeme.toString(), line, column);
			}
			lexeme.append(character);
			errors.addErro("Caracter invalido para id.", lexeme.toString(), line, column);
			return this.nextToken();
		} catch (EOFException e) {
			lexeme.append(character);
			errors.addErro("Finalizado durante validacao de Id. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	/*private Token process_ID() throws IOException {
		lexeme.append(character);
		try {
			while (true) {
				character = fLoader.getNextChar();
				if (!Character.isLetter(character) && !Character.isDigit(character) && character != '_') {
					fLoader.resetLastChar();
					return tabSymbols.addID(lexeme.toString(), line, column);
				}
				lexeme.append(character);
			}
		} catch (EOFException e) {
			return tabSymbols.addID(lexeme.toString(), line, column);
		}
	}*/
	
	private Token processNumInt() throws IOException {
		try {
			lexeme.append(character);
			int countE = 0;
			int countPlus = 0;
			do {
				character = fLoader.getNextChar();
				if (!Character.isDigit(character) && character != 'E' && character != '+') {
					if (character == '.' || character == ',')
						return processNumFloat();
					fLoader.resetLastChar();
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico inteiro.", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == 'E' && (!Character.isDigit(lexeme.charAt(lexeme.length() - 1)) || countE != 0)) {
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico inteiro. Espera digito antes do caracter 'E' ou possue mais de um caracter 'E'.", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == '+' && (lexeme.charAt(lexeme.length() - 1) != 'E' || countPlus != 0)) {
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico inteiro. Espera caracter 'E' antes do simbolo '+' ou possue mais de um caracter '+'", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == 'E')
					countE ++;
				else if (character == '+')
					countPlus ++;
				lexeme.append(character);
			} while(character == '.' || Character.isDigit(character) || character == 'E' || character == '+' || character == ',');
			lexeme.append(character);
			return new Token(TokenType.NUM_INT, lexeme.toString(), line, column);	
		} catch (EOFException e) {
			errors.addErro("Finalizado durante validacao de literal numerico inteiro. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}
	
	private Token processNumFloat() throws IOException {
		try {
			if (lexeme.charAt(lexeme.length() - 1) == ',') {
				lexeme.append(character);
				errors.addErro("Caracter invalido para literal numerico decimal. Espera '.', mas contem ','.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			lexeme.append(character);
			int countE = 0;
			int countPlus = 0;
			int countPoint = 1;
			do {
				character = fLoader.getNextChar();
				if (character == ',') {
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico decimal. Espera '.', mas contem ','.", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == 'E' && (!Character.isDigit(lexeme.charAt(lexeme.length() - 1)) || countE != 0)) {
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico decimal. Espera digito antes do caracter 'E' ou possue mais de um caracter 'E'.", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == '+' && (lexeme.charAt(lexeme.length() - 1) != 'E' || countPlus != 0)) {
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico decimal. Espera caracter 'E' antes do simbolo '+' ou possue mais de um caracter '+'", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == '.' && (!Character.isDigit(lexeme.charAt(lexeme.length() - 1)) || countPoint > 1)) {
					lexeme.append(character);
					errors.addErro("Caracter invalido para literal numerico decimal. Espera digito antes do '.' ou ja possui '.'", lexeme.toString(), line, column);
					return this.nextToken();
				}
				if (character == 'E')
					countE ++;
				else if (character == '+')
					countPlus ++;
				else if (character == '.')
					countPoint ++;
				lexeme.append(character);
			} while(character == '.' || Character.isDigit(character) || character == 'E' || character == '+' || character == ',');
			lexeme.append(character);
			return new Token(TokenType.NUM_FLOAT, lexeme.toString(), line, column);
		} catch (EOFException e) {
			errors.addErro("Finalizado durante validacao de literal numerico decimal. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}
	
	private Token processAssign() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != '-') {
				lexeme.append(character);
				errors.addErro("Caracter invalido para atribuicao. Espera caracter '-'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			lexeme.append(character);
			return  new Token(TokenType.ASSIGN, lexeme.toString(), line, column);
		} catch (Exception e) {
			errors.addErro("Finalizado durante atribuicao. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private Token processRelop() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if ((character == 'l' || character == 'g' || character == 'e' || character == 'd')) {
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (character == 't' || character == 'e' || character == 'q' || character == 'f') {
					if ((character == 't' || character == 'e')
							&& (lexeme.charAt(lexeme.length() - 1) != 'l' && lexeme.charAt(lexeme.length() - 1) != 'g')) {
						lexeme.append(character);
						errors.addErro("Caracter inválido para operador logico. Espera [ 'l', 'g' ].", lexeme.toString(), line, column);
						return this.nextToken();
					} else if (character == 'q' && lexeme.charAt(lexeme.length() - 1) != 'e') {
						lexeme.append(character);
						errors.addErro("Caracter inválido para operador logico. Espera [ 'e' ].", lexeme.toString(), line, column);
						return this.nextToken();
					} else if (character == 'f' && lexeme.charAt(lexeme.length() - 1) != 'd') {
						lexeme.append(character);
						errors.addErro("Caracter inválido para operador logico. Espera [ 'd' ].", lexeme.toString(), line, column);
						return this.nextToken();
					}
					lexeme.append(character);
					return new Token(TokenType.RELOP, lexeme.toString(), line, column);
				} else {
					lexeme.append(character);
					errors.addErro("Caracter invalido para operador logico. Espera [ 't', 'e', 'q', 'f' ]", lexeme.toString(), line, column);
					return this.nextToken();
				}
			} else {
				lexeme.append(character);
				errors.addErro("Caracter invalido para operador logico. Espera [ 'l', 'g', 'e', 'd' ].", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (EOFException e) {
			errors.addErro("Finalizado durante validacao de operação logica. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private void ignoreComments() throws IOException {
		character = fLoader.getNextChar();
		try {
			if (character == '#') {
				while (true) {
					character = fLoader.getNextChar();
					if (character == '#') {
						character = fLoader.getNextChar();
							if (character == '}') {
								break;
						} else {
							fLoader.resetLastChar();
						}
					}
				}
			} else {
				errors.addErro("formatado inválido, esperado {# #}.", "comentários", line, column);
			}
		} catch (EOFException e) {
			errors.addErro("Finalizado durante declaração de comentário", "comentários", line, column);
		}
	}

	// RETORNA UM TOKEN DE FIM DE ARQUIVO
	private Token token_EOF(){
		return tabSymbols.addID("EOF", line, column);
	}

}
